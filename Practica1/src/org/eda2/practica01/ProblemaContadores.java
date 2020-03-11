package org.eda2.practica01;

import java.util.ArrayList;

public class ProblemaContadores { //Inicializar medidores
	
	private ParEdificios[][] pEdificios;

	//filas, columnas
	public ProblemaContadores(int i, int j) {
		if (i%2==0) pEdificios = new ParEdificios[i/2][j]; //i vale la mitad porque unimos dos columnas
		else pEdificios = new ParEdificios[(i+1)/2][j];
		for(int a=0; a<pEdificios.length; a++) {
			for(int b=0; b<pEdificios[a].length; b++) {
				pEdificios[a][b] = new ParEdificios();
			}
		}
	}
		
	public void inicializarContadores() {
		if (pEdificios.length%2==0) inicializarContadoresPar();
		else inicializarContadoresImpar();
	}
	
	private void inicializarContadoresPar() {
		for(int j=0; j<pEdificios[0].length; j++) {
			for(int i=0; i<pEdificios.length; i++) {
				this.pEdificios[i][j].setcDerecha(new Contador());
				this.pEdificios[i][j].setcIzquierda(new Contador());
				if (j==pEdificios[0].length-1 && i != pEdificios.length-1) this.pEdificios[i][j].setcMorado(new Contador());
				if (j!=0&&j!=pEdificios[0].length-1)this.pEdificios[i][j].setcVerde(new Contador());
				if (j!=0) {
					this.pEdificios[i][j].setMan(new Manometro()); //Inicializa los manometros
					this.pEdificios[i][j].getMan().setPresion(Math.random()); //Math.random()*(110-150+1)+150
				}
			}
		}
	}
	
	private void inicializarContadoresImpar() {
		inicializarContadoresPar();
		for (int j = 0;j<pEdificios[0].length;j++) {
			this.pEdificios[0][j].setcIzquierda(null);
		}
	}
	
	
	public String toString() {
		String resultado = "";
		for(int i=0; i<pEdificios.length; i++) {
			for(int j=0; j<pEdificios[i].length; j++) {
				resultado += pEdificios[i][j].toString() +"       "+ i+" "+j+"\n";
			}
			resultado += "\n";			
		}
		return resultado;
	}
	
	public String toStringM() { //Imprime en pantalla solo los manometros de la ciudad
		String resultado = "";
		for(int i=0; i<pEdificios.length; i++) {
			for(int j=0; j<pEdificios[i].length; j++) {
				resultado += pEdificios[i][j].toStringManometros() +"       "+ i+" "+j+"\n";
			}
			resultado += "\n";			
		}
		return resultado;
	}
	
	public String toStringC() { //Imprime en pantalla solo los manometros de la ciudad
		String resultado = "";
		for(int i=0; i<pEdificios.length; i++) {
			for(int j=0; j<pEdificios[i].length; j++) {
				resultado += pEdificios[i][j].toStringContadores() +"       "+ i+" "+j+"\n";

			}
			resultado += "\n";			
		}
		return resultado;
	}
	
	public ArrayList<String> resolverManometros() {
		ArrayList<String> resultado = new ArrayList<>();
		resultado.addAll(resolverManometrosGeneral());
		for (int i = 0;i<pEdificios.length;i++) {
			resultado.addAll(resolverManometrosDistribucion(i));
		}
		System.out.println(resultado.toString());
		return new ArrayList<String>();
	}
	
	public ArrayList<String> resolverManometrosGeneral() {
		ArrayList<String> resultadoGeneral = new ArrayList<>();
		double aux = 0;
		double current = 0;
		for (int i = pEdificios.length-1;i>=1;i--) {
			aux = pEdificios[i-1][pEdificios[0].length-1].getMan().getPresion();
			current = pEdificios[i][pEdificios[0].length-1].getMan().getPresion();
			if (aux<(current*10/100)) {
				resultadoGeneral.add("["+ i + ", " + (pEdificios[0].length-1) + "]" + " - " + "["+ (i-1) + ", " + (pEdificios[0].length-1) + "]");
			}
			
		}
		return resultadoGeneral;
	}
	
	public ArrayList<String> resolverManometrosDistribucion(int i) {
		ArrayList<String> resultadoDistribucion = new ArrayList<>();
		double aux = 0;
		double current = 0;
		for (int j = pEdificios[0].length-2;j>0;j--) {
			current = pEdificios[i][j].getMan().getPresion();
			aux = pEdificios[i][j+1].getMan().getPresion();
			if (current<(aux*10/100)) {
				resultadoDistribucion.add("["+ i + ", " + j + "]" + " - " + "["+ i + ", " + (j+1) + "]");
			}
		}
		return resultadoDistribucion;
	}
	
	
	

}
