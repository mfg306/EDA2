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
	 * Constructor de la clase Man�metro que asigna la presi�n
	 * pasada por par�metro como presi�n del man�metro creado
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
	 * Establece la presi�n del man�metro
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
	 * Establece la id del man�metro
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
	 * @return cadena con la presion del man�metro
	 */
	@Override
	public String toString() {
		return "" + String.format(Locale.US, "%.2f", this.presion);
	}	
}