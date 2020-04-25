package org.eda2.practica02;

import java.util.Locale;

public class Manometro {

	private double presion;
	private static int contador = 0;
	private int id;
	
	public Manometro() {
		this.presion = 0;
		this.id=Manometro.contador;
		Manometro.contador++;
	}
	
	public Manometro (double presion) {
		this.presion = presion;
		this.id=Manometro.contador;
		Manometro.contador++;
	}
	
	public double getPresion() {
		return this.presion;
	}
	
	public void setPresion(double presion) {
		this.presion = presion;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void serId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "" + String.format(Locale.US, "%.2f", this.presion);
	}
	
}