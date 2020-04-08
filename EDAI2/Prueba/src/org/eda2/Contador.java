package org.eda2;

public class Contador extends Medidor{

	private double consumo;// Consumo diario
	private static int numContador = 0;
	private int id = numContador; 

	public Contador() { 
		this.consumo = (Math.random() * (108 - 162 + 1) + 162); 
		this.id = numContador++;
	}
	
	public Contador(double consumo) {
		this.consumo = consumo;
		this.id = numContador++;
	}

	public double getConsumo() {
		return this.consumo;
	}

	public void setConsumo(double consumo) {
		this.consumo = consumo;
	}
	
	public int getId() {
		return id;
	}
	
	public static int getNumContador() {
		return numContador;
	}

	@Override
	public String toString() {
		return "" + this.consumo;
	}
	
	public static void reiniciarId() {
		Contador.numContador = 0;
	}
	
	
	

}
