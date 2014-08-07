package entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import utils.DateUtils;

public class ClientOrder {
	public static final String	CREATED_STATE		= "Créée";
	public static final String	ONGOING_STATE		= "En cours de traitement";
	public static final String	TO_DELIVER_STATE	= "Prête à livrer";
	public static final String	DELIVERED_STATE		= "Livrée";

	private long id;
	
	private long creationDate;
	
	private List<Batch> batches;
	private String	state;
	
	private User owner;
	
	public ClientOrder() {
		this.state        = CREATED_STATE;
		this.batches      = new ArrayList<>();
		this.creationDate = new Date().getTime();
	}
	
	public ClientOrder(User user) {
		this();
		this.owner = user;
	}
	
	public float getTTCPrice() {
		float price = 0;
		if (batches != null)
			for (Batch batch : batches) 
				price += batch.getQt() * batch.getBiscuit().getPrice();
		return price;
	}
	
	public float getHTPrice() {
		return getTTCPrice()/1.2f;
	}
	
	public float getTaxes() {
		return getTTCPrice() * (1 - 1/1.2f);
	}
	
	public String getFormattedCreationDate() {
		return new DateTime(creationDate).toString(DateUtils.SIMPLE_DATE);
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<Batch> getBatches() {
		return batches;
	}

	public void setBatches(List<Batch> batches) {
		this.batches = batches;
	}

	public long getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}
}
