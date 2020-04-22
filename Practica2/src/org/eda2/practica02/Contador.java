package org.eda2.practica02;

public class Contador {

	private double consumo;
	private static int id = 0; //Creo que despu�s va a ser �til para en el divide y venceras poder buscar rapidamente
							// con el id a qu� casilla pertenece para dar una soluci�n

	public Contador() { // Consumo diario
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
	
	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "" + this.consumo;
	}
	
	public static void reiniciarId() {
		Contador.id = 0;
	}

}
