package entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.joda.time.DateTime;

import utils.DateUtils;

@Entity
public class ClientOrder {
	public static final String	VALIDATED_STATE	= "Validée";
	public static final String	ONGOING_STATE	= "En cours de traitement";
	public static final String	CREATED			= "Créée";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private long creationDate;
	
	@OneToOne
	private List<Batch> batches;
	private String	state;
	
	@ManyToOne
	private User owner;
	
	public ClientOrder() {
		this.state        = CREATED;
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
