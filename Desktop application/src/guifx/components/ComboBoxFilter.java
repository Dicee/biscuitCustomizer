package guifx.components;

import java.util.Collection;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;

public class ComboBoxFilter<T> extends QueryFilter {
	public final ComboBox<T> comboBox;
	
	public ComboBoxFilter(String label, String fieldName, Collection<T> items) {
		super(label,fieldName);
		this.comboBox = new ComboBox<>(FXCollections.observableArrayList(items));
	}

	@Override
	public String getQuery() {
		return String.format("%s='%s'",fieldName,comboBox.getSelectionModel().getSelectedItem());
	}
}
