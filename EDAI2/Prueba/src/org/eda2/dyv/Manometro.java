package org.eda2.dyv;

import java.util.Locale;

/**
 * @author marta
 *
 */
public class Manometro {

	private double presion;
	private static int contador = 0; 
	private int id;
	
	/**
	 * Constructor vacio
	 */
	public Manometro() {
		this.presion = 0;
		this.id = Manometro.contador;
	}
	
	/**
	 * @param presion a asignar a un manometro
	 */
	public Manometro (double presion) {
		this.presion = presion;
		this.id = Manometro.contador;
		Manometro.contador++;
	}
	
	public double getPresion() {
		return this.presion;
	}
	
	public void setPresion(double presion) {
		this.presion = presion;
	}
	
	@Override
	public String toString() {
		return "" + String.format(Locale.US, "%.2f", this.presion);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	/**
	 * Reiniciar los id a 0
	 */
	public static void reiniciarID() {
		contador = 0;
	}
	
	
}