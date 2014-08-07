package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class CustomizationMetadata {
	@Id
	@ManyToOne
	private Customization custom;
	@Id
	private String field;
	private String value;
	
	public CustomizationMetadata() { }
	
	public CustomizationMetadata(Customization custom, String field, String value) {
		this.custom = custom;
		this.field  = field;
		this.value  = value;
	}

	public String getField() {
		return field;
	}
	
	public void setField(String field) {
		this.field = field;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public Customization getCustom() {
		return custom;
	}

	public void setCustom(Customization custom) {
		this.custom = custom;
	}
}
