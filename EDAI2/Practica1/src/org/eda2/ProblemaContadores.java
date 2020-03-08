package org.eda2;

import java.util.ArrayList;

public class ProblemaContadores { //Inicializar medidores
	
	private ParCalles[][] pCalles;

	//filas, columnas
	public ProblemaContadores(int i, int j) {
		if (i%2==0) pCalles = new ParCalles[i/2][j]; //i vale la mitad porque unimos dos columnas
		else pCalles = new ParCalles[(i+1)/2][j];
		int longitud = pCalles.length;
		int largitud = pCalles[0].length;
		for(int a=0; a<pCalles.length; a++) {
			for(int b=0; b<pCalles[a].length; b++) {
				pCalles[a][b] = new ParCalles();
			}
		}
	}
	
//	public void inicializarContadores() {
//		for(int j=0; j<pCalles[0].length; j++) {
//			for(int i=0; i<pCalles.length; i++) {
//				if(pCalles[0].length % 2 == 0) {
//					this.pCalles[i][j] = new ParCalles();
//					this.pCalles[i][j].setcDerecha(new Contador());
//					this.pCalles[i][j].setcIzquierda(new Contador());
//					if(i == pCalles.length-1 ) this.pCalles[i][j].setcMorado(new Contador());
//					else this.pCalles[i][j].setcVerde(new Contador());
//					
//					if(i == 0) { // no añadir verdes en la primera fila
//						
//					}
//				} else {
//					
//					if( j== 0) {
//						if(i == pCalles.length-1) {
//							this.pCalles[i][j].setcDerecha(new Contador());
//							this.pCalles[i][j].setcMorado(new Contador());
//						} else {
//							this.pCalles[i][j].setcDerecha(new Contador());
//							this.pCalles[i][j].setcVerde(new Contador());
//						}
//					} else {
//						if(i == pCalles.length-1) {
//							this.pCalles[i][j].setcDerecha(new Contador());
//							this.pCalles[i][j].setcIzquierda(new Contador());
//							this.pCalles[i][j].setcMorado(new Contador());
//						} else {
//							this.pCalles[i][j].setcDerecha(new Contador());
//							this.pCalles[i][j].setcIzquierda(new Contador());
//							this.pCalles[i][j].setcVerde(new Contador());
//						}
//					}
//				}
//				
//				if(i == 0) { // no añadir verdes en la primera fila
//					
//				}
//			}
//		}
//	}
	
	public void inicializarContadores() {
		if (pCalles.length%2==0) inicializarContadoresPar();
		else inicializarContadoresImpar();
	}
	
	public void inicializarContadoresPar() {
		for(int j=0; j<pCalles[0].length; j++) {
			for(int i=0; i<pCalles.length; i++) {
				this.pCalles[i][j].setcDerecha(new Contador());
				this.pCalles[i][j].setcIzquierda(new Contador());
				if (j==pCalles[0].length-1 && i != pCalles.length-1) this.pCalles[i][j].setcMorado(new Contador());
				if (j!=0&&j!=pCalles[0].length-1)this.pCalles[i][j].setcVerde(new Contador());
				if (j!=0) {
					this.pCalles[i][j].setMan(new Manometro()); //Inicializa los manometros
					this.pCalles[i][j].getMan().setPresion(Math.random()); //Math.random()*(110-150+1)+150
				}
				
			}
		}
	}
	
	public void inicializarContadoresImpar() {
		inicializarContadoresPar();
		for (int j = 0;j<pCalles[0].length;j++) {
			this.pCalles[0][j].setcIzquierda(null);
		}
	}
	
	
	public String toString() {
		String resultado = "";
		for(int i=0; i<pCalles.length; i++) {
			for(int j=0; j<pCalles[i].length; j++) {
				resultado += pCalles[i][j].toString() +"       "+ i+" "+j+"\n";

			}
			resultado += "\n";			
		}
		return resultado;
	}
	
	public String toStringM() { //Imprime en pantalla solo los manometros de la ciudad
		String resultado = "";
		for(int i=0; i<pCalles.length; i++) {
			for(int j=0; j<pCalles[i].length; j++) {
				resultado += pCalles[i][j].toStringManometros() +"       "+ i+" "+j+"\n";

			}
			resultado += "\n";			
		}
		return resultado;
	}
	
	public String toStringC() { //Imprime en pantalla solo los manometros de la ciudad
		String resultado = "";
		for(int i=0; i<pCalles.length; i++) {
			for(int j=0; j<pCalles[i].length; j++) {
				resultado += pCalles[i][j].toStringContadores() +"       "+ i+" "+j+"\n";

			}
			resultado += "\n";			
		}
		return resultado;
	}
	
	public ArrayList<String> resolverManometros() {
		ArrayList<String> resultado = new ArrayList<>();
		resultado.addAll(resolverManometrosGeneral());
		for (int i = 0;i<pCalles.length;i++) {
			resultado.addAll(resolverManometrosDistribucion(i));
		}
		System.out.println(resultado.toString());
		return new ArrayList<String>();
	}
	
	public ArrayList<String> resolverManometrosGeneral() {
		ArrayList<String> resultadoGeneral = new ArrayList<>();
		double aux = 0;
		double current = 0;
		for (int i = pCalles.length-1;i>=1;i--) {
			aux = pCalles[i-1][pCalles[0].length-1].getMan().getPresion();
			current = pCalles[i][pCalles[0].length-1].getMan().getPresion();
			if (aux<(current*10/100)) {
				resultadoGeneral.add("["+ i + ", " + (pCalles[0].length-1) + "]" + " - " + "["+ (i-1) + ", " + (pCalles[0].length-1) + "]");
			}
			
		}
		return resultadoGeneral;
	}
	
	public ArrayList<String> resolverManometrosDistribucion(int i) {
		ArrayList<String> resultadoDistribucion = new ArrayList<>();
		double aux = 0;
		double current = 0;
		for (int j = pCalles[0].length-2;j>0;j--) {
			current = pCalles[i][j].getMan().getPresion();
			aux = pCalles[i][j+1].getMan().getPresion();
			if (current<(aux*10/100)) {
				resultadoDistribucion.add("["+ i + ", " + j + "]" + " - " + "["+ i + ", " + (j+1) + "]");
			}
		}
		return resultadoDistribucion;
	}
	
	
	

}
