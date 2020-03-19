package org.eda2;

/**
 * @author marta
 * Esta clase representa un par de edificios con todos los medidores que posee cada uno de estos 
 */
public class ParEdificios {

	private Contador cDerecha;
	private Contador cIzquierda;
	private Contador cVerde;
	private Contador cMorado;
	private Manometro man;
	
	
	//Constructor vacío
	public ParEdificios() {
		
	}
	
	public ParEdificios(Contador cDerecha, Contador cIzquierda, Contador cVerde, Contador cMorado, Manometro man) {
		this.cDerecha = cDerecha;
		this.cIzquierda = cIzquierda;
		this.cMorado = cMorado;
		this.cVerde = cVerde;
		this.man = man;
	}
	
	public Contador getcDerecha() {
		return cDerecha;
	}

	public void setcDerecha(Contador cDerecha) {
		this.cDerecha = cDerecha;
	}

	public Contador getcIzquierda() {
		return cIzquierda;
	}

	public void setcIzquierda(Contador cIzquierda) {
		this.cIzquierda = cIzquierda;
	}

	public Contador getcVerde() {
		return cVerde;
	}

	public void setcVerde(Contador cVerde) {
		this.cVerde = cVerde;
	}

	public Contador getcMorado() {
		return cMorado;
	}

	public void setcMorado(Contador cMorado) {
		this.cMorado = cMorado;
	}
	
	public Manometro getMan() {
		return man;
	}

	public void setMan(Manometro man) {
		this.man = man;
	}
	
	public String toString() {
		return this.getcIzquierda() + " | " + this.getcDerecha();
	}
	
	public String toStringManometros() {
		return "Man: " + this.getMan();
	}
	
	public String toStringContadores() {
		return "Dcha: " + this.getcDerecha() + ", Izq: " + this.getcIzquierda() + ", M: " + this.getcMorado() + ", V: " + this.getcVerde();
	}
	
	
	
}