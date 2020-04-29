package org.eda2.dyv;

public class ParEdificios {

	private Contador cDerecha;
	private Contador cIzquierda;
	private Contador cVerde;
	private Contador cMorado;
	private Manometro man;
	
	/**
	 * Constructor por defecto de la clase ParEdificios
	 */
	public ParEdificios() {

	}
	
	/**
	 * Constructor de la clase ParEdificios que establece el valor de todos los
	 * atributos de la clase
	 * @param cDerecha
	 * @param cIzquierda
	 * @param cVerde
	 * @param cMorado
	 * @param man
	 */
	public ParEdificios(Contador cDerecha, Contador cIzquierda, Contador cVerde, Contador cMorado, Manometro man) {
		this.cDerecha = cDerecha;
		this.cIzquierda = cIzquierda;
		this.cMorado = cMorado;
		this.cVerde = cVerde;
		this.man = man;
	}
	
	/**
	 * 
	 * @return cDerecha, el contador derecho del ParEdificios
	 */
	public Contador getcDerecha() {
		return cDerecha;
	}

	/**
	 * Establece el valor del contador derecho del ParEdificios
	 * @param cDerecha
	 */
	public void setcDerecha(Contador cDerecha) {
		this.cDerecha = cDerecha;
	}

	/**
	 * 
	 * @return cIzquierda, el contador izquierdo del ParEdificios
	 */
	public Contador getcIzquierda() {
		return cIzquierda;
	}

	/**
	 * Establece el valor del contador izquierdo del ParEdificios
	 * @param cIzquierda
	 */
	public void setcIzquierda(Contador cIzquierda) {
		this.cIzquierda = cIzquierda;
	}

	/**
	 * 
	 * @return cVerde, el contador de la linea de distribución
	 * del ParEdificios
	 */
	public Contador getcVerde() {
		return cVerde;
	}

	/**
	 * Establece el valor contador de la linea de distribución
	 * del ParEdificios
	 * @param cVerde
	 */
	public void setcVerde(Contador cVerde) {
		this.cVerde = cVerde;
	}

	/**
	 * 
	 * @return cMorado, el contador de la linea troncal del
	 * ParEdificios
	 */
	public Contador getcMorado() {
		return cMorado;
	}

	/**
	 * Establece el valor del contador de la linea troncal
	 * del ParEdificios
	 * @param cMorado
	 */
	public void setcMorado(Contador cMorado) {
		this.cMorado = cMorado;
	}
	
	/**
	 * 
	 * @return man, manómetro del ParEdificios
	 */
	public Manometro getMan() {
		return man;
	}

	/**
	 * Establece el valor del manómetro del ParEdificios
	 * @param man
	 */
	public void setMan(Manometro man) {
		this.man = man;
	}
	
	/**
	 * 
	 * @param id
	 * @return true si el ParEdificios contiene el contador con la
	 * id pasada por parámetro
	 */
	public boolean containsContadorID(int id) {
		if(this.cDerecha!=null && Integer.compare(id, this.cDerecha.getId())==0) return true;
		else if(this.cIzquierda!=null && Integer.compare(id, this.cIzquierda.getId())==0) return true;
		else if(this.cVerde!=null && Integer.compare(id, this.cVerde.getId())==0) return true;
		else if(this.cMorado!=null && Integer.compare(id, this.cMorado.getId())==0) return true;
		return false;
	}
	
	/**
	 * 
	 * @param id
	 * @return true si el manómetro del ParEdificios tiene
	 * la id pasada por parámetro
	 */
	public boolean containsManometroID(int id) {
		if(this.man.getId() == id) return true;
		return false;
	}
	
	/** 
	 * 
	 * @param id
	 * @return tipo del contador con la id pasada por parámetro
	 * si existe en el ParEdificios
	 */
	public String getTipo(int id) {
		if(Integer.compare(id, this.cDerecha.getId())==0) return "D";
		else if(Integer.compare(id, this.cIzquierda.getId())==0) return "I";
		else if(Integer.compare(id, this.cVerde.getId())==0) return "V";
		else if(Integer.compare(id, this.cMorado.getId())==0) return "M";
		return null;
	}
	
	/**
	 * @return cadena con el valor de los contadores y del manómetro 
	 * del ParEdificios
	 */
	public String toString() {
		return "I " + this.getcIzquierda() + " | D " + this.getcDerecha() + " | M " + this.getcMorado() + " | V " + this.getcVerde() + " Man" + this.getMan() ;
	}
	
	/**
	 * 
	 * @return cadena con la información del manómetro del ParEdificios
	 */
	public String toStringManometros() {
		return "Man: " + this.getMan();
	}
	
	/**
	 * 
	 * @return cadena con la información de los contadores del ParEdificios
	 */
	public String toStringContadores() {
		return "I " + this.getcIzquierda() + " | D " + this.getcDerecha() + " | M " + this.getcMorado() + " | V " + this.getcVerde() ;
	}
}