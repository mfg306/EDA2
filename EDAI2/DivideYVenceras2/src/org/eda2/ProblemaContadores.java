package org.eda2;

public class ProblemaContadores {
	
	private ParCalles[][] pCalles; //por aristas

	//filas, columnas
	public ProblemaContadores(int i, int j) {
		if (i%2==0) pCalles = new ParCalles[i/2][j]; //i vale la mitad porque unimos dos columnas
		else pCalles = new ParCalles[(i+1)/2][j];
		for(int a=0; a<pCalles.length; a++) {
			for(int b=0; b<pCalles[a].length; b++) {
				pCalles[a][b] = new ParCalles();
			}
		}
	}
	
	public void inicializarContadores() {
		if (pCalles.length%2==0) inicializarContadoresPar();
		else inicializarContadoresImpar();
	}
	
	private void inicializarContadoresPar() {
		for(int j=0; j<pCalles[0].length; j++) {
			for(int i=0; i<pCalles.length; i++) {
				this.pCalles[i][j].setcDerecha(new Contador());
				this.pCalles[i][j].setcIzquierda(new Contador());
				if (j==pCalles[0].length-1 && i != pCalles.length-1) this.pCalles[i][j].setcMorado(new Contador());
				if (j!=0&&j!=pCalles[0].length-1)this.pCalles[i][j].setcVerde(new Contador());
			}
		}
	}
	
	private void inicializarContadoresImpar() {
//		inicializarContadoresPar();
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
	
	
	

}