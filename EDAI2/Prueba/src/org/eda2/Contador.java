package org.eda2;

public class Contador extends Medidor{

	private double consumo;
	private static int numContador = 0;
	private int id = numContador; //Creo que después va a ser útil para en el divide y venceras poder buscar rapidamente
							// con el id a qué casilla pertenece para dar una solución

	public Contador() { // Consumo diario
		this.consumo = (Math.random() * (108 - 162 + 1) + 162);
		numContador++;
	}
	
	public Contador(double consumo) {
		this.consumo = consumo;
		id++;
		numContador++;
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

}
