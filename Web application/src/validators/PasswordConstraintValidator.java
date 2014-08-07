package validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import utils.Dialogs;

@FacesValidator(value="passwordConstraintValidator")
public class PasswordConstraintValidator implements Validator {
	private static final String	errorMsg	 = "Votre mot de passe doit contenir au moins 8 caractères dont au moins une majuscule et un chiffre";
	
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    	if (value == null || component == null)
    		throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,Dialogs.REQUIRED_FIELD,null));
    	else {
    		String pswd   = (String) value;
    		String regex1 = "(.)*[A-Z](.)*";
    		String regex2 = "(.)*[a-z](.)*";
    		String regex3 = "(.)*[0-9](.)*";
   			if (pswd.length() < 6 || !pswd.matches(regex1) || !pswd.matches(regex2) || !pswd.matches(regex3))
   				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMsg,null));
    	}
    }
}