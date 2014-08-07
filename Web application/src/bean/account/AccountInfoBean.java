package bean.account;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import utils.Dialogs;
import bean.SessionBean;
import ejb.UserManager;

@ManagedBean
@ViewScoped
public class AccountInfoBean implements Serializable {
	private static final long	serialVersionUID	= 1L;

	@EJB
	private UserManager manager;
	
	@ManagedProperty(value="#{sessionBean}")
	private SessionBean sessionBean;
	
	@NotNull(message = Dialogs.REQUIRED_FIELD)
	@NotEmpty(message = Dialogs.REQUIRED_FIELD)
	private String pswd, confirmPswd;

	public void changePassword() {
		sessionBean.getUser().setPassword(UserManager.sha1(pswd));
		sessionBean.setUser(manager.merge(sessionBean.getUser()));
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,Dialogs.EDIT_SUCCESSFUL, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public void changeEmail() {
		sessionBean.setUser(manager.merge(sessionBean.getUser()));
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,Dialogs.EDIT_SUCCESSFUL, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public String getPassword() {
		return pswd;
	}

	public void setPassword(String pswd) {
		this.pswd = pswd;
	}

	public String getConfirmPassword() {
		return confirmPswd;
	}

	public void setConfirmPassword(String confirmPswd) {
		this.confirmPswd = confirmPswd;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}
}
