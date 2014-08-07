package entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import utils.Dialogs;

@Entity
public class Address {
	public static final String DEFAULT_LABEL = "Adresse par défaut";
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@NotNull(message=Dialogs.REQUIRED_FIELD)
	@NotEmpty(message=Dialogs.REQUIRED_FIELD)
	private String label, nom, prenom, raisonSociale, voie, ville, codePostal;
	
	@OneToOne(fetch=FetchType.EAGER)
	private User owner;
	
	public Address() { }
	
	public Address(String label, String nom, String prenom, String raisonSociale, String voie,
			String ville, String codePostal) {
		this.label         = label;
		this.nom           = nom;
		this.prenom        = prenom;
		this.raisonSociale = raisonSociale;
		this.voie          = voie;
		this.ville         = ville;
		this.codePostal    = codePostal;
	}
	
	public void copy(Address address) {
		label         = address.label;
		nom           = address.nom;
		prenom        = address.prenom;
		raisonSociale = address.raisonSociale;
		voie          = address.voie;
		ville         = address.ville;
		codePostal    = address.codePostal;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
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

	public String getRaisonSociale() {
		return raisonSociale;
	}

	public void setRaisonSociale(String raisonSociale) {
		this.raisonSociale = raisonSociale;
	}

	public String getVoie() {
		return voie;
	}

	public void setVoie(String voie) {
		this.voie = voie;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
