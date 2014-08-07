package bean;

import java.io.Serializable;

import static utils.Dialogs.*;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import utils.Dialogs;
import utils.Pages;
import validators.EmailValidator;
import ejb.UserManager;
import entities.Address;
import entities.User;

@ManagedBean
@ViewScoped
@FacesValidator(value="emailAvalabilityValidator")
public class SubscribeBean implements Serializable, Validator {
	private static final long	serialVersionUID		= 8327675811912356612L;

	@EJB
	private UserManager manager;
	private User user;
	private String confirmPassword;
		
	public SubscribeBean() {
		reset();
	}
	
	public String subscribe() {
		Address address = user.getDefaultAddress();
		address.setNom(user.getNom());
		address.setPrenom(user.getPrenom());
		address.setLabel(Address.DEFAULT_LABEL);
		manager.persist(user);
		return Pages.signin;
	}
	
	private void reset() {
		user            = new User();
		confirmPassword = null;
		user.setDefaultAddress(new Address());
	}
	
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (value == null || component == null)
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,Dialogs.REQUIRED_FIELD,null));
		else
			try {
				String email = (String) value;
				if (!email.matches(EmailValidator.emailRegex))
					throw new ValidatorException(new FacesMessage(
							FacesMessage.SEVERITY_ERROR,EMAIL_FORMAT_ERROR,null));
				if (manager.getUserByEMail(email) != null)
					throw new ValidatorException(new FacesMessage(
							FacesMessage.SEVERITY_ERROR,UNAVAILABLE_EMAIL,null));
			} catch (Throwable t) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,t.getMessage(),null);
				FacesContext currentContext = FacesContext.getCurrentInstance();
				currentContext.addMessage(component.getClientId(currentContext),msg);
			}
	}
	
	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
