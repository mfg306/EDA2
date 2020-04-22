package org.eda2.practica02;

import java.util.Locale;

public class Manometro {

	private double presion;
	
	public Manometro() {
		this.presion = 0;
	}
	
	public Manometro (double presion) {
		this.presion = presion;
	}
	
	public double getPresion() {
		return this.presion;
	}
	
	public void setPresion(double presion) {
		this.presion = presion;
	}
	
	@Override
	public String toString() {
		return "" + String.format(Locale.US, "%.2f", this.presion);
	}
	
}
