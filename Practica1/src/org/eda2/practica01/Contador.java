package org.eda2.practica01;

import java.util.Locale;

public class Contador {

	private double consumo;
	private static int numContador = 0;
	private int id = numContador;

	public Contador() {
		this.consumo = (Math.random() * (108 - 162 + 1) + 162);
		this.id = numContador++;
	}
	
	public Contador(double consumo) { // Consumo diario
		this.consumo = consumo;//(Math.random() * (108 - 162 + 1) + 162);
		this.id = numContador++;
	}

	public double getConsumo() {
		return this.consumo;
	}

	public void setConsumo(double consumo) {
		this.consumo=consumo;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getNumContador() {
		return numContador;
	}
	
	public static void reiniciarId() {
		Contador.numContador = 0;
	}

	@Override
	public String toString() {
		return "" + String.format(Locale.US, "%.2f", this.consumo);
	}

}
