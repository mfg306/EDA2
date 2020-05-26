package org.eda2.backtracking;

public class ParEdificios {

	private Contador cDerecha;
	private Contador cIzquierda;
	private Contador cVerde;
	private Contador cMorado;
	
	//Constructor vacío
	public ParEdificios() {

	}
		
	public ParEdificios(Contador cDerecha, Contador cIzquierda, Contador cVerde, Contador cMorado) {
		this.cDerecha = cDerecha;
		this.cIzquierda = cIzquierda;
		this.cMorado = cMorado;
		this.cVerde = cVerde;
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
	
	public boolean containsContadorID(int id) {
		if(this.cDerecha!=null && Integer.compare(id, this.cDerecha.getId())==0) return true;
		else if(this.cIzquierda!=null && Integer.compare(id, this.cIzquierda.getId())==0) return true;
		else if(this.cVerde!=null && Integer.compare(id, this.cVerde.getId())==0) return true;
		else if(this.cMorado!=null && Integer.compare(id, this.cMorado.getId())==0) return true;
		return false;
	}
	
	public String getTipo(int id) {
		if(Integer.compare(id, this.cDerecha.getId())==0) return "D";
		else if(Integer.compare(id, this.cIzquierda.getId())==0) return "I";
		else if(Integer.compare(id, this.cVerde.getId())==0) return "V";
		else if(Integer.compare(id, this.cMorado.getId())==0) return "M";
		return null;
	}
	
	public Contador getContador(String tipo) {
		if (tipo.equals("D")) return this.getcDerecha();
		else if (tipo.equals("I")) return this.getcIzquierda();
		else if (tipo.equals("V")) return this.getcVerde();
		else return this.getcMorado();
	}
	
	public String toString() {
		return "I " + this.getcIzquierda() + " | D " + this.getcDerecha() + " | M " + this.getcMorado() + " | V " + this.getcVerde();
	}
	
	public String toStringContadores() {
		return "I " + this.getcIzquierda() + " | D " + this.getcDerecha() + " | M " + this.getcMorado() + " | V " + this.getcVerde() ;
	}
	
}