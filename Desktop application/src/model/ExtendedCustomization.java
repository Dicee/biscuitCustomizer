package model;

import entities.Customization;

public class ExtendedCustomization extends Customization {
	public ExtendedCustomization(float x, float y, int mode, String data, int size) {
		super(x,y,mode,data,size);
	}

	public String getModeString() {
		return getMode() == URL_MODE ? "QR code" : "Texte";
	}
}
