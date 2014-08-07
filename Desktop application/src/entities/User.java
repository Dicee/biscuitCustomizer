package entities;

import java.util.ArrayList;
import java.util.List;

public class User {
	private long id;
	
	private String nom, prenom, email, password;
	private Address defaultAddress;
	
	private List<Address> addresses = new ArrayList<>();
	
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
