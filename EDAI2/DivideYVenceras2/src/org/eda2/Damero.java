package org.eda2;

public class Damero { //Representación de la ciudad
	private int filas;
	private int columnas; 
	private Manzana[][] manzanas;
	
	public Damero() {
		
	}
	
	public Damero(int filas, int columnas) {
		this.filas = filas;
		this.columnas = columnas;

		manzanas = new Manzana[filas][columnas];
		
		
		//Nuestro problema va a estar formado 

		
		}
	
	
	@Override
	public String toString() {
		String resultado = "";
		
		for(int i=0; i<filas; i++) {
			for(int j=0; j<columnas; j++) {
				resultado += this.manzanas[i][j] + "\t";
			}
			resultado += "\n";
		}
		return resultado;
		
	}

}
