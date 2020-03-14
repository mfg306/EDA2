package org.eda2;

public class Contador extends Medidor{

	private double consumo;

	public Contador() { // Consumo diario
		this.consumo = (Math.random() * (108 - 162 + 1) + 162);
	}
	
	public Contador(double consumo) {
		this.consumo = consumo;
	}

	public double getConsumo() {
		return this.consumo;
	}

	public void setConsumo(double consumo) {
		this.consumo = consumo;
	}

	@Override
	public String toString() {
		return "" + this.consumo;
	}

}
