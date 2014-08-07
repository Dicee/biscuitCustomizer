package validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import utils.Dialogs;

@FacesValidator(value="qtValidator")
public class QtValidator implements Validator {
	private static final String errorMsg = "Veuillez saisir un entier positif";
	
	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		if (value == null || component == null)
    		throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,Dialogs.REQUIRED_FIELD,null));
    	else {
    		int qt = (int) value;
    		if (qt <= 0)
   				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMsg,null));
    	}
	}
}
