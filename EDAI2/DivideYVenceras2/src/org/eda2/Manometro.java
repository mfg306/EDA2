package org.eda2;

public class Manometro extends Medidores {

	private double presion;
	
	public Manometro() {
		this.presion = 0;
	}
	
	public double getPresion() {
		return this.presion;
	}
	public void setPresion(double presion) {
		this.presion = presion;
	}
	
	@Override
	public String toString() {
		return "" +this.presion;
	}
	
}
