package org.eda2;
import java.util.ArrayList;


public class ProblemaManometros { //por aristas
	
	private Manometro[][] manometro;
	
	
	//filas, columnas
	public ProblemaManometros(int i, int j) {
		manometro = new Manometro[i][j];
	}
	
	
	public void inicializarManometro() {
		for(int j=0; j<manometro[0].length; j++) {
			for(int i=0; i<manometro.length; i++) {
				if(j%2==0) {
					manometro[i][j] = new Manometro();
					if(j==manometro[0].length-1 && i == manometro.length-1) {
						manometro[i][j].setPresion(Math.random()*(110-150+1)+150); //El MG
					} else manometro[i][j].setPresion(Math.random()*(0-150+1)+150);
				}
			}
		}
	}
	
	public String toString() {
		String resultado = "";
		for(int i=0; i<manometro.length; i++) {
			for(int j=0; j<manometro[i].length; j++) {
				resultado += manometro[i][j] +",";

			}
			resultado += "\n";			
		}
		return resultado;
	}
	
	public ArrayList<Manometro> gestionManometrosGeneral(){
		
		ArrayList<Manometro> resultado = new ArrayList<>();
		
		
		
		return resultado;
		
		
	}
	
	

}
