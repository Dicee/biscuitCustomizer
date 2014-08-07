package bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import utils.Dialogs;
import utils.Pages;
import utils.RandomPasswordGenerator;
import ejb.UserManager;
import entities.User;

@ManagedBean
@RequestScoped
public class RetrievePasswordBean implements Serializable {
	private static final long	serialVersionUID	= 1L;

	private static final String message = "<p>Nous avons reçu une demande de changement de mot de passe pour votre compte Poult Taylor. "
			+ "Pour confirmer votre demande de changement de mot de passe, suivez le lien suivant <a href='%s'>%s</a> et utilisez le code de confirmation"
			+ " suivant : <b>%s</b>. Le nouveau mot de passe temporaire qui vous sera attribué est : <b>%s</b>. Vous pourrez à tout moment le modifier"
			+ " depuis la section <b>Mon compte</b>.</p><br/><br/>"
			+ "<p>Si vous n'avez pas demandé à changer de mot de passe, contactez un administrateur ou ignorez ce message.</p>";
	
	private static final String confirmPageURL = "localhost:8088/Stage/retrieveConfirm.xhtml";
	
	@EJB
	private UserManager manager;
	
	@NotNull(message=Dialogs.REQUIRED_FIELD)
	@NotEmpty(message=Dialogs.REQUIRED_FIELD)
	private String	email;
	
	private String confirmCode;
	
	private void reset() {
		email       = null;
		confirmCode = null;
	}

	public String retrieve() {
		User user = manager.getUserByEMail(email);
		if (user != null) {
			String newPassword = RandomPasswordGenerator.randomPassword(6,1,1);
			String confirmCode = RandomPasswordGenerator.randomPassword(12,3,3);
			manager.setRetrievePassword(user,confirmCode,newPassword);
			try {
				Properties logProperties = new Properties();
				logProperties.load(RetrievePasswordBean.class.getResourceAsStream("properties/log.properties"));
					
				String link    = confirmPageURL;
				String content = String.format(RetrievePasswordBean.message,link,link,confirmCode,newPassword);
				sendMail(logProperties.getProperty("username"),logProperties.getProperty("password"),email,content);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return Pages.signin;
	}
	 
	public void confirm() {
		User user = manager.getUserByEMail(email);
		String msg;
		FacesMessage.Severity sev;
		
		if (user != null && manager.retrievePassword(user,confirmCode)) {
			msg  = Dialogs.RETRIEVE_SUCCESSFUL;
			sev  = FacesMessage.SEVERITY_INFO;
			reset();
		} else {
			msg  = Dialogs.RETRIEVE_FAILED;
			sev  = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage message = new FacesMessage(sev,msg,null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	private static void sendMail(final String username, final String password, String to, String content) {
		Properties props = new Properties();
		props.put("mail.smtp.auth","true");
		props.put("mail.smtp.starttls.enable","true");
		props.put("mail.smtp.host","smtp.gmail.com");
		props.put("mail.smtp.port","587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username,password);
					}
				});
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
			message.setSubject("Demande de récupération de mot de passe");
			message.setContent(content,"text/html;charset=utf-8");
			
			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getConfirmCode() {
		return confirmCode;
	}

	public void setConfirmCode(String confirmCode) {
		this.confirmCode = confirmCode;
	}
}
