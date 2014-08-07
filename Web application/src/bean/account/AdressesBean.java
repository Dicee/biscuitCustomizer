package bean.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import utils.Dialogs;
import ejb.GenericEJB;
import entities.Address;
import entities.User;

@ManagedBean(name="accountBean")
@ViewScoped
public class AdressesBean implements Serializable {
	private static final long	serialVersionUID	= 1L;
	private static final long	NEW_ADDRESS			= -1;

	@EJB
	private GenericEJB ejb;
	
	private Address selectedAddress;
	
	@ManagedProperty(value="#{sessionBean.user}")
	private User user;

	@PostConstruct
	private void init() {
		selectedAddress = ejb.find(Address.class,user.getDefaultAddress().getId());
		selectedAddress.setOwner(user);
	}
	
	public void editAddress() {
		Address editedAddress = ejb.merge(selectedAddress);
		if (user.getDefaultAddress().equals(selectedAddress))
			user.setDefaultAddress(editedAddress);
		else if (selectedAddress.getId() == NEW_ADDRESS) {
			user.addAddress(selectedAddress);
			user = ejb.merge(user);
		} else {
			user.removeAddress(selectedAddress);
			user.addAddress(editedAddress);
		}
		selectedAddress = editedAddress;
		user = ejb.merge(user);
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,Dialogs.EDIT_SUCCESSFUL, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public void updateAddress() {
		selectedAddress = ejb.find(Address.class,selectedAddress.getId());
	}
	
	public Address getSelectedAddress() {
		return selectedAddress;
	}

	public void setSelectedAddress(Address selectedAddress) {
		this.selectedAddress = selectedAddress;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public List<Address> getAddresses() {
		List<Address> result = new ArrayList<>(user.getAddresses());
		result.add(user.getDefaultAddress());
		return result;
	}
}
