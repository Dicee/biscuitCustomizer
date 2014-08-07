package model;

import entities.Batch;

public class ExtendedBatch extends Batch {
	public String getBiscuitRef() {
		return getBiscuit().getRef();
	}
	
	public int getNumberOfCustomizations() {
		return getCustomizations().size();
	}
}
