package bean.account;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import ejb.GenericEJB;
import ejb.UserManager;
import entities.ClientOrder;
import entities.User;

@ManagedBean
@ViewScoped
public class OrderDetailBean implements Serializable {
	private static final long	serialVersionUID	= 1L;
	
	@EJB
	private UserManager manager;
	@EJB
	private GenericEJB ejb;

	@ManagedProperty(value="#{sessionBean.user}")
	private User user;

	private ClientOrder order;
	
	@PostConstruct
	private void init() {
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		long               id     = Long.parseLong(params.get("orderId"));
		order                     = ejb.find(ClientOrder.class,id);
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public ClientOrder getOrder() {
		return order;
	}
}
