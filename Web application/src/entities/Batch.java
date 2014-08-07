package entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

@Entity
public class Batch {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	private int	qt;
	
	@OneToOne
	private ClientOrder order;
	
	@OneToOne(fetch=FetchType.EAGER)
	@NotNull
	private Biscuit biscuit;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Customization> customizations = new ArrayList<>();
	
	public Batch() { 
		setQt(1);
	}
	
	public Batch(int qt, Biscuit biscuit) {
		setQt(qt);
		this.biscuit = biscuit;
	}
	
	public int getQt() {
		return qt;
	}

	public void setQt(int qt) {
		if (qt <= 0)
			throw new ValidationException();
		this.qt = qt;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ClientOrder getOrder() {
		return order;
	}

	public void setOrder(ClientOrder order) {
		this.order = order;
	}

	public Biscuit getBiscuit() {
		return biscuit;
	}

	public void setBiscuit(Biscuit biscuit) {
		this.biscuit = biscuit;
	}

	public List<Customization> getCustomizations() {
		return customizations;
	}

	public void setCustomizations(List<Customization> customizations) {
		this.customizations = customizations;
	}
}
