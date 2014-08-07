package entities;


public class Biscuit {
	private String ref;
	private String name; 
	private float price;
	private int piecesPerBatch;
	private int edgeLength, xTop, yTop;

	public Biscuit() { }

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getPiecesPerBatch() {
		return piecesPerBatch;
	}

	public void setPiecesPerBatch(int piecesPerBatch) {
		this.piecesPerBatch = piecesPerBatch;
	}

	public int getyTop() {
		return yTop;
	}

	public void setyTop(int yTop) {
		this.yTop = yTop;
	}

	public int getxTop() {
		return xTop;
	}

	public void setxTop(int xTop) {
		this.xTop = xTop;
	}

	public int getEdgeLength() {
		return edgeLength;
	}

	public void setEdgeLength(int edgeLength) {
		this.edgeLength = edgeLength;
	}
}
