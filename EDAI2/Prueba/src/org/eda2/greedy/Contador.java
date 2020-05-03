package org.eda2.greedy;

public class Contador extends Medidor{

	private double consumo;
	private static int contador = 0; 
	private int id;

	public Contador() { 
		this.consumo = (Math.random() * (108 - 162 + 1) + 162); // Consumo diario
		this.id = Contador.contador;
		Contador.contador++;
	}
	
	public Contador(double consumo) {
		this.consumo = consumo;
		this.id = Contador.contador;
		Contador.contador++;
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

	@Override
	public String toString() {
		return "" + this.consumo;
	}
	
	public static void reiniciarId() {
		Contador.contador = 0;
	}
	
	@Override
	public boolean equals(Object other) {
		Contador otro = (Contador)other;
		return this.id == otro.id;
	}

}
