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
	
	private void inicializarContadoresPar() { //Los datos de los manometros ya tienen sentido
		for(int j=pEdificios[0].length-1; j>=0; j--) {
			for(int i=pEdificios.length-1; i>=0; i--) {
				//System.out.println(i+" "+j);
				this.pEdificios[i][j].setcDerecha(new Contador(0)); //Todas las casillas tienen contadores a la derecha y a la izquierda
				this.pEdificios[i][j].setcIzquierda(new Contador(0));
				if (j==pEdificios[0].length-1 && i != pEdificios.length-1) this.pEdificios[i][j].setcMorado(new Contador(0)); //Inicializa los contadores morados
				if (j!=0&&j!=pEdificios[0].length-1)this.pEdificios[i][j].setcVerde(new Contador(0)); //Inicializa los contadores verdes
				if (j!=0) { //Inicializacion de los manometros
					this.pEdificios[i][j].setMan(new Manometro()); //Crea los manometros
					if (i==pEdificios.length-1 && j==pEdificios[0].length-1) this.pEdificios[i][j].getMan().setPresion(Math.random()*(110-150+1)+150);
					else {
						if (j==pEdificios[0].length-1) { //Conducto general
							double pAnterior = this.pEdificios[i+1][j].getMan().getPresion();
							double error = pAnterior - (pAnterior*13/100); //Margen de error del manometro
							this.pEdificios[i][j].getMan().setPresion(Math.random()*(error-pAnterior+1)+pAnterior);
						} else {
							double pAnterior = this.pEdificios[i][j+1].getMan().getPresion();
							double error = pAnterior - (pAnterior*13/100); //Margen de error del manometro
							this.pEdificios[i][j].getMan().setPresion(Math.random()*(error-pAnterior+1)+pAnterior);
						}
						
					}
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
		double aux;
		double current;
		for (int i = pEdificios.length-1;i>=1;i--) {
			aux = pEdificios[i-1][pEdificios[0].length-1].getMan().getPresion();
			current = pEdificios[i][pEdificios[0].length-1].getMan().getPresion();
			if (aux<(current-current*10/100)) {
				resultadoGeneral.add("["+ i + ", " + (pEdificios[0].length-1) + "]" + " - " + "["+ (i-1) + ", " + (pEdificios[0].length-1) + "]");
			}
			
		}
		return resultadoGeneral;
	}
	
	public ArrayList<String> resolverManometrosDistribucion(int i) {
		ArrayList<String> resultadoDistribucion = new ArrayList<>();
		double aux = 0;
		double current = 0;
		for (int j = pEdificios[0].length-2;j>1;j--) {
			current = pEdificios[i][j].getMan().getPresion();
			aux = pEdificios[i][j-1].getMan().getPresion();
			if (aux<(current-current*10/100)) {
				resultadoDistribucion.add("["+ i + ", " + j + "]" + " - " + "["+ i + ", " + (j+1) + "]");
			}
		}
		return resultadoDistribucion;
	}
	
	
	

}
