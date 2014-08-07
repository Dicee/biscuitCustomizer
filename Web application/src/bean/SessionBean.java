package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import utils.Dialogs;
import utils.Pages;
import ejb.GenericEJB;
import ejb.UserManager;
import entities.Batch;
import entities.ClientOrder;
import entities.User;

@ManagedBean
@SessionScoped
public class SessionBean implements Serializable {
	private static final long	serialVersionUID	= 1L;
	private static final String	loginMessage		= "<b>Authentification échouée : </b>Ce nom d'utilisateur n'existe pas !";
	private static final String	failurePswdMessage	= "<b>Authentification échouée : </b>Mot de passe incorrect !";
	private static final String	connectedMessage	= "<b>Authentification réussie : </b>Vous êtes maintenant connecté au site";

	@EJB
	private UserManager			userManager;
	@EJB
	private GenericEJB			ejb;

	private boolean				connected			= false;
	@NotNull(message = Dialogs.REQUIRED_FIELD)
	@NotEmpty(message = Dialogs.REQUIRED_FIELD)
	private String				email, pswd;

	private User				user;
	private ClientOrder			order;

	@PostConstruct
	private void init() {
		reset();
		order = isConnected() ? getCurrentOrder() : new ClientOrder();
	}
	
	private void reset() {
		email     = "";
		pswd      = "";
		user      = null;
		connected = false;
	}


	public ClientOrder getCurrentOrder() {
		ClientOrder order = userManager.getCurrentOrder(user);
		if (order == null)
			ejb.persist(order = new ClientOrder(user));
		return order;
	}

	public String connect() {
		User m = userManager.getUserByEMail(email);
		String msg;
		FacesMessage.Severity sev;

		if (m == null) {
			msg = loginMessage;
			sev = FacesMessage.SEVERITY_ERROR;
		} else if (connected = m.getPassword().equals(UserManager.sha1(pswd))) {
			msg = connectedMessage;
			user = m;
			sev = FacesMessage.SEVERITY_INFO;

			// We merge the content of the cart, stored in the session, with the
			// database
			ClientOrder order = getCurrentOrder();
			order.getBatches().addAll(this.order.getBatches());
			this.order = ejb.merge(order);
		} else {
			msg = failurePswdMessage;
			sev = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage message = new FacesMessage(sev,msg,null);
		FacesContext.getCurrentInstance().addMessage(null,message);
		return sev == FacesMessage.SEVERITY_INFO ? Pages.home : Pages.signin;
	}

	public String disconnect() {
		init();
		return Pages.home;
	}

	public String order() {

		if (isConnected()) {
			if (order.getBatches().isEmpty()) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, Dialogs.EMPTY_CART, null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				return Pages.cart;
			} else {

				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, Dialogs.ORDER_SUCCESSFUL,null);
				FacesContext.getCurrentInstance().addMessage(null, message);

				order.setState(ClientOrder.ONGOING_STATE);
				order.setCreationDate(new Date().getTime());
				ejb.merge(order);

				Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
				int 				size	= order.getBatches().size();
				for (int i = 0; i < size; i++) {
					Batch batch = order.getBatches().get(i);
					batch.setQt(Integer.parseInt(params.get(String.format("form:batches:%d:qt", i))));
					ejb.merge(batch);
				}
				order = getCurrentOrder();
				return Pages.cart;
			}
		} else {
			return Pages.signin;
		}
	}
	
	public void addBatch(Batch b) {
		System.out.println(b.getCustomizations());
		b.setOrder(order);
		order.getBatches().add(b);
		if (isConnected())
			order = ejb.merge(order);
	}

	public void removeBatch() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		Batch batch = order.getBatches().remove(Integer.parseInt(params.get("index")));
		if (isConnected()) {
			order = ejb.merge(order);
			ejb.remove(Batch.class,batch.getId());
		}
	}

	public List<Batch> getBatches() {
		return new ArrayList<>(order.getBatches());
	}

	public ClientOrder getOrder() {
		return order;
	}

	public String getLogin() {
		return email;
	}

	public void setLogin(String login) {
		this.email = login;
	}

	public String getPassword() {
		return pswd;
	}

	public void setPassword(String pswd) {
		this.pswd = pswd;
	}

	public boolean isConnected() {
		return connected && user != null;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
