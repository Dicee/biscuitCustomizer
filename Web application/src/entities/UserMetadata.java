package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UserMetadata {
	@Id
	@ManyToOne
	private User user;
	@Id
	private String field;
	private String value;
	
	public UserMetadata() { }
	
	public UserMetadata(User user, String field, String value) {
		this.user = user;
		this.field = field;
		this.value = value;
	}

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
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
}
