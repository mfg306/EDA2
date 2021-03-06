package org.eda2.dyv;

/**
 * @author marta
 *
 */

public class Contador {

	private double consumo;
	private static int contador = 0; 
	private int id;

	/**
	 * Constructor vacio
	 * Constructor por defecto de la clase Contador
	 */
	public Contador() { 
		this.consumo = (Math.random() * (108 - 162 + 1) + 162); // Consumo diario
		this.id = Contador.contador;
		Contador.contador++;
	}
	
	/**
	 * @param consumo el consumo que queremos asignarle al contador
	 * Constructor de la clase Contador que asigna el consumo
	 * pasado por par�metro como consumo del contador creado
	 * @param consumo
	 */
	public Contador(double consumo) {
		this.consumo = consumo;
		this.id = Contador.contador;
		Contador.contador++;
	}

	/**
	 * 
	 * @return consumo
	 */
	public double getConsumo() {
		return this.consumo;
	}

	/**
	 * Establece es consumo del contador
	 * @param consumo
	 */
	public void setConsumo(double consumo) {
		this.consumo = consumo;
	}
	
	/**
	 * 
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Reinicia el cotador a 0
	 */
	public static void reiniciarId() {
		Contador.contador = 0;
	}
	
	/**
	 * @return cadena con el id y el consumo del contador
	 */
	@Override
	public String toString() {
		return "(" +this.id+ ")" + this.consumo;
	}
}