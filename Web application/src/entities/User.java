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
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import utils.Dialogs;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@NotNull(message=Dialogs.REQUIRED_FIELD)
	@NotEmpty(message=Dialogs.REQUIRED_FIELD)
	private String nom, prenom, email, password;
	@NotNull
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private Address defaultAddress;
	
	@OneToMany(fetch=FetchType.LAZY)
	private List<Address> addresses = new ArrayList<>();
	
	@OneToMany(fetch=FetchType.EAGER)
	private List<ClientOrder> orders = new ArrayList<>();

	public User() {	}
	
	public User(String nom, String prenom, String password, String email, Address address) {
		this.nom            = nom;
		this.prenom         = prenom;
		this.password       = password;
		this.email          = email;
		this.defaultAddress = address;
	}
	
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<ClientOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<ClientOrder> orders) {
		this.orders = orders;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Address getDefaultAddress() {
		return defaultAddress;
	}

	public void setDefaultAddress(Address defaultAddress) {
		this.defaultAddress = defaultAddress;
		this.defaultAddress.setOwner(this);
	}

	public void addAddress(Address a) {
		addresses.add(a);
		a.setOwner(this);
	}
	
	public void removeAddress(Address a) {
		addresses.remove(a);
		a.setOwner(null);
	}
	
	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
}
