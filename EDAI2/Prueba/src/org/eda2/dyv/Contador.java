package org.eda2.dyv;

<<<<<<< HEAD
/**
 * @author marta
 *
 */
=======
>>>>>>> 1d4b4021b062169bf1d925dcfe3af65e0bf84085
public class Contador {

	private double consumo;
	private static int contador = 0; 
	private int id;

	/**
<<<<<<< HEAD
	 * Constructor vacio
=======
	 * Constructor por defecto de la clase Contador
>>>>>>> 1d4b4021b062169bf1d925dcfe3af65e0bf84085
	 */
	public Contador() { 
		this.consumo = (Math.random() * (108 - 162 + 1) + 162); // Consumo diario
		this.id = Contador.contador;
		Contador.contador++;
	}
	
	/**
<<<<<<< HEAD
	 * @param consumo el consumo que queremos asignarle al contador
=======
	 * Constructor de la clase Contador que asigna el consumo
	 * pasado por parámetro como consumo del contador creado
	 * @param consumo
>>>>>>> 1d4b4021b062169bf1d925dcfe3af65e0bf84085
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
<<<<<<< HEAD
	
	/**
	 * Reinicia el contador a 0
	 */
	public static void reiniciarId() {
		Contador.contador = 0;
	}

=======
>>>>>>> 1d4b4021b062169bf1d925dcfe3af65e0bf84085
}