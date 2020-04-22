package org.eda2.practica01;

import java.util.Locale;

public class Contador {

	private double consumo;
	private static int contador = 0;
	private int id;

	public Contador() {
		this.consumo = (Math.random() * (108 - 162 + 1) + 162);
		this.id = Contador.contador;
		Contador.contador++;
	}
	
	public Contador(double consumo) { // Consumo diario
		this.consumo = consumo;
		this.id = Contador.contador;
		Contador.contador++;
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
	
	public static void reiniciarId() {
		Contador.contador = 0;
	}
	
	public boolean equals(Contador obj) {
        return (this.consumo == obj.consumo);
    }

	@Override
	public String toString() {
		return "" + String.format(Locale.US, "%.2f", this.consumo);
	}

}
