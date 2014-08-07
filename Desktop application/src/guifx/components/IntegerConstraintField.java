package guifx.components;

import javafx.scene.control.TextField;

import org.controlsfx.dialog.Dialogs;

public class IntegerConstraintField extends TextField {
	public static final int	ERROR_RETURN	= Integer.MAX_VALUE;
	
    public void setValue(int i) {
        setText(i + "");
    }
    
    public boolean isEmpty() {
    	return getText().equals("");
    }
    
	public int getValue() {
		try {
			int n = Integer.parseInt(getText());
			return n;
		} catch (NumberFormatException nfe) {
			Dialogs.create().owner(this).
				title("Erreur").
				masthead("Une erreur s'est produite").
				message("Format de nombre invalide").
				showError();
			return ERROR_RETURN;
		} 
	}
}
