package org.eda2;

public class Contador extends Medidores {

	private double consumo;

	public Contador() { // Consumo diario
		this.consumo = (Math.random() * (108 - 162 + 1) + 162);
	}

	public double getConsumo() {
		return this.consumo;
	}

	public void setConsumo() {
	}

	@Override
	public String toString() {
		return "" + this.consumo;
	}

}
