package validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import utils.Dialogs;

@FacesValidator(value="emailValidator")
public class EmailValidator implements Validator {
	public static final String	emailRegex			= "[\\w\\.-]*\\w@[\\w\\.-]*\\w\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]";

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (value == null || component == null)
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,Dialogs.REQUIRED_FIELD,null));
		else
			try {
				String email = (String) value;
				if (!email.matches(emailRegex))
					throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,Dialogs.EMAIL_FORMAT_ERROR,null));
			} catch (Throwable t) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,t.getMessage(),null);
				FacesContext currentContext = FacesContext.getCurrentInstance();
				currentContext.addMessage(component.getClientId(currentContext),msg);
			}
	}
}
