package guifx.components;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public abstract class QueryFilter {
	public final CheckBox	checkBox;
	protected String		fieldName;

	public QueryFilter(String label, String fieldName) {
		this.checkBox  = new CheckBox(label);
		this.fieldName = fieldName;
	}
	
	public boolean isSelected() {
		return checkBox.isSelected();
	}
	
	public abstract String getQuery();
}

