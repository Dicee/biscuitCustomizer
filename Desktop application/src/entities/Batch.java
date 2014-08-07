package entities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.ValidationException;

import com.sun.istack.internal.NotNull;

public class Batch {
	private long id;
	
	private int	qt;
	
	private ClientOrder order;
	
	private Biscuit biscuit;
	
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
			throw new IllegalArgumentException();
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
