package org.eda2.practica01;

import java.util.Locale;

public class Contador {

	private double consumo;

	public Contador(double consume) { // Consumo diario
		this.consumo = consume;//(Math.random() * (108 - 162 + 1) + 162);
	}

	public double getConsumo() {
		return this.consumo;
	}

	public void setConsumo(double consumo) {
		this.consumo=consumo;
	}

	@Override
	public String toString() {
		return "" + String.format(Locale.US, "%.2f", this.consumo);
	}

}
