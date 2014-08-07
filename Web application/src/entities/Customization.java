package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Customization {
	public static final int URL_MODE   = 0;
	public static final int TEXT_MODE  = 1;
	public static final int IMAGE_MODE = 2;
	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private int mode, size;
	private float x, y;

	private String data;
	
	public Customization() { }

	public Customization(float x, float y, int mode, String data, int size) {
		this.x 		= x;
		this.y 		= y;
		this.mode 	= mode;
		this.data 	= data;
		this.size 	= size;
	}

	public int getMode() {
		return mode;
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		switch (mode) {
			case URL_MODE  : return "QR Code" ;
			case TEXT_MODE : return "Texte"   ;
			default	       : return "Image"   ;				
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
