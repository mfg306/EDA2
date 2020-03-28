package org.eda2;

public class Contador extends Medidor{

	private double consumo;
	private static int id = 0; //Creo que después va a ser útil para en el divide y venceras poder buscar rapidamente
							// con el id a qué casilla pertenece para dar una solución

	public Contador() { // Consumo diario
		this.consumo = (Math.random() * (108 - 162 + 1) + 162);
		id++;
	}
	
	public Contador(double consumo) {
		this.consumo = consumo;
		id++;
	}

	public double getConsumo() {
		return this.consumo;
	}

	public void setConsumo(double consumo) {
		this.consumo = consumo;
	}
	
	public static int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "" + this.consumo;
	}

}
