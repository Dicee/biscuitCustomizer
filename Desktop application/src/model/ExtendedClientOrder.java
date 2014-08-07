package model;

import entities.ClientOrder;

public class ExtendedClientOrder extends ClientOrder {
	public int getNumberOfBatches() {
		return getBatches().size();
	}
	
	public String getName() {
		return getOwner().getNom() + " " + getOwner().getPrenom();
	}
	
	public long getOwnerId() {
		return getOwner().getId();
	}
	
	@Override
	public String toString() {
		return "Commande n°" + getId() + " - " + getName() + " - " + getFormattedCreationDate();
	}
}
