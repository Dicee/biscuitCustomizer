package bean.account;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import bean.SessionBean;
import utils.Pages;
import ejb.GenericEJB;
import entities.Address;
import entities.User;

@ManagedBean
@ViewScoped
public class CreateAddressBean implements Serializable {
	private static final long	serialVersionUID	= 1L;

	@EJB
	private GenericEJB ejb;
	
	@ManagedProperty(value="#{sessionBean.user}")
	private User user;
	
	@ManagedProperty(value="#{sessionBean}")
	private SessionBean sessionBean;
	
	private Address address = new Address();
	
	public String create() {
		user.addAddress(address);
		ejb.persist(address);
		sessionBean.setUser(ejb.merge(user));
		return Pages.addresses;
	}
	
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}
}
