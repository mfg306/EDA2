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
<<<<<<< HEAD
	 * Constructor vacio
=======
	 * Constructor por defecto de la clase Man�metro
>>>>>>> 1d4b4021b062169bf1d925dcfe3af65e0bf84085
	 */
	public Manometro() {
		this.presion = 0;
		this.id = Manometro.contador;
	}
	
	/**
<<<<<<< HEAD
	 * @param presion a asignar a un manometro
=======
	 * Constructor de la clase Man�metro que asigna la presi�n
	 * pasada por par�metro como presi�n del man�metro creado
	 * @param presion
>>>>>>> 1d4b4021b062169bf1d925dcfe3af65e0bf84085
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

<<<<<<< HEAD
	
	/**
	 * Reiniciar los id a 0
=======
	/**
	 * Reinicia el valor del contador a 0
>>>>>>> 1d4b4021b062169bf1d925dcfe3af65e0bf84085
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