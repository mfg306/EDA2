package org.eda2;

public class Manzana {
	
	private double volumen; //entre 300000 y 500000 m3
	
	public Manzana() {
		//(Math.random()*(X-Y+1)+Y; [x-y]
		this.volumen = Math.random()*(30000-50000+1)+50000;
	}
	
	public String toString() {
		return "" + this.volumen;
	}
}
