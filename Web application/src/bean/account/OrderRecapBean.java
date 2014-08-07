package bean.account;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import ejb.UserManager;
import entities.ClientOrder;
import entities.User;

@ManagedBean
@RequestScoped
public class OrderRecapBean implements Serializable {
	private static final long	serialVersionUID	= 1L;

	@EJB
	private UserManager manager;
	
	@ManagedProperty(value="#{sessionBean.user}")
	private User user;
	
	private List<ClientOrder> orders;
	
	@PostConstruct
	private void init() {
		orders = manager.getOrders(user,ClientOrder.ONGOING_STATE);
	}
	
	public List<ClientOrder> getOrders() {
		return orders;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
