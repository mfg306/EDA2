package org.eda2.practica01;

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
	
	public boolean containsContadorID(int id) {
		if(this.cDerecha!=null && Integer.compare(id, this.cDerecha.getId())==0) return true;
		else if(this.cIzquierda!=null && Integer.compare(id, this.cIzquierda.getId())==0) return true;
		else if(this.cVerde!=null && Integer.compare(id, this.cVerde.getId())==0) return true;
		else if(this.cMorado!=null && Integer.compare(id, this.cMorado.getId())==0) return true;
		return false;
	}
	
	public boolean containsManometroID(int id) {
		if(this.man.getId()==id) return true;
		return false;
	}
	
	public String getTipo(int id) {
		if(Integer.compare(id, this.cDerecha.getId())==0) return "D";
		else if(Integer.compare(id, this.cIzquierda.getId())==0) return "I";
		else if(Integer.compare(id, this.cVerde.getId())==0) return "V";
		else if(Integer.compare(id, this.cMorado.getId())==0) return "M";
		return null;
	}
	
	public String toString() {
		return "Dcha: " + this.getcDerecha() + ", Izq: " + this.getcIzquierda() + ", M: " + this.getcMorado() + ", V: " + this.getcVerde() + ", Man: " + this.getMan();
	}
	
	public String toStringManometros() {
		return "Man: " + this.getMan();
	}
	
	public String toStringContadores() {
		return "Dcha: " + this.getcDerecha() + ", Izq: " + this.getcIzquierda() + ", M: " + this.getcMorado() + ", V: " + this.getcVerde();
	}
	
	public double getConsumoV() {
		return this.cVerde.getConsumo();
	}
	
	public double getConsumoD() {
		return this.cDerecha.getConsumo();
	}
	
	public double getConsumoI() {
		return this.cIzquierda.getConsumo();
	}
	
	public double getConsumoM() {
		return this.cMorado.getConsumo();
	}
	
	
}
