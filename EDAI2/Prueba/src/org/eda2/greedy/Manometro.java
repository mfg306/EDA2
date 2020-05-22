package org.eda2.greedy;

import java.util.Locale;

/**
 * @author marta y alex
 *
 */
public class Manometro {

	private double presion;
	private static int contador = 0; 
	private int id;
	private String tipo;
	private int i;
	private int j;

	
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
	public Manometro (double presion, int i, int j) {
		this.presion = presion;
		this.i = i;
		this.j = j;
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
	 * @return coordenada i 
	 */
	public int getI() {
		return i;
	}

	/**
	 * @param i la coordenada i que queremos establecer
	 */
	public void setI(int i) {
		this.i = i;
	}

	/**
	 * @return la coordenada j 
	 */
	public int getJ() {
		return j;
	}

	/**
	 * @param j coordenada j que queremos establecer
	 */
	public void setJ(int j) {
		this.j = j;
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
	
	/**
	 * @param m manometro con el que queremos comparar
	 * @return el resultado 
	 */
	public int compareTo(Manometro m) {
		return Double.compare(this.presion, m.presion);
	}
}