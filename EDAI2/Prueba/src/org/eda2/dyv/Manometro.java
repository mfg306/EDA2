package org.eda2.dyv;

import java.util.Locale;

public class Manometro {

	private double presion;
	private static int contador = 0; 
	private int id;
	
	/**
	 * Constructor por defecto de la clase Manómetro
	 */
	public Manometro() {
		this.presion = 0;
		this.id = Manometro.contador;
	}
	
	/**
	 * Constructor de la clase Manómetro que asigna la presión
	 * pasada por parámetro como presión del manómetro creado
	 * @param presion
	 */
	public Manometro (double presion) {
		this.presion = presion;
		this.id = Manometro.contador;
		Manometro.contador++;
	}
	
	/**
	 * 
	 * @return presion
	 */
	public double getPresion() {
		return this.presion;
	}
	
	/**
	 * Establece la presión del manómetro
	 * @param presion
	 */
	public void setPresion(double presion) {
		this.presion = presion;
	}
	
	/**
	 * 
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Establece la id del manómetro
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Reinicia el valor del contador a 0
	 */
	public static void reiniciarID() {
		contador = 0;
	}
	
	/**
	 * @return cadena con la presion del manómetro
	 */
	@Override
	public String toString() {
		return "" + String.format(Locale.US, "%.2f", this.presion);
	}	
}