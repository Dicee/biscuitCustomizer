package validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import utils.Dialogs;

@FacesValidator(value="passwordConfirmationValidator")
public class PasswordConfirmationValidator implements Validator {
	private static final String	passwordComp = "passwordComp";
	private static final String	pswdMsg		 = "Ce champ n'est pas identique au précédent";
	
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    	if (value == null || component == null)
    		throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,Dialogs.REQUIRED_FIELD,null));
    	else {
    		String conf = (String) ((UIInput) component.getAttributes().get(passwordComp)).getValue();
    		if (conf == null || !conf.equals(value))
    			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,pswdMsg,null));
    	}
    }
}