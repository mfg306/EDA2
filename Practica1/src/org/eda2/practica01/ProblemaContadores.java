package org.eda2.practica01;

import java.util.ArrayList;

public class ProblemaContadores {
	
	private ParEdificios[][] pEdificios;
	private ParEdificios[][] matrizMedias;
	private boolean par; //Lo necesito para diferenciar a la hora de inicializar los medidores

	//Constructor
	public ProblemaContadores(int i, int j) {
		if (i%2==0) {
			pEdificios = new ParEdificios[i/2][j]; //i vale la mitad porque unimos dos columnas
			matrizMedias = new ParEdificios[i/2][j];
			this.par = true;
		} else {
			pEdificios = new ParEdificios[(i+1)/2][j];
			matrizMedias = new ParEdificios[(i+1)/2][j];
			this.par = false;
		}
		
		for(int a=0; a<pEdificios.length; a++) { 
			for(int b=0; b<pEdificios[a].length; b++) {
				pEdificios[a][b] = new ParEdificios();
				matrizMedias[a][b] = new ParEdificios();
			}
		}
		generarMatrizMedias();
	}
		
	public void inicializarMedidores() {
		inicializarMedidoresPar();
		if (!par)
			for (int j = 0;j<pEdificios[0].length;j++) 
				this.pEdificios[0][j].setcIzquierda(null);
	}
	
	private void inicializarMedidoresPar() { //Los datos de los manometros ya tienen sentido
		for(int j=pEdificios[0].length-1; j>=0; j--) {
			for(int i=pEdificios.length-1; i>=0; i--) {
				//Ancho y largo de nuestra matriz de ParEdificios
				int ancho = pEdificios.length-1;
				int largo = pEdificios[0].length-1;
				//Inicializa los contadores
				this.pEdificios[i][j].setcDerecha(new Contador(Math.random() * (100 - 1000 + 1) + 1000)); //Todas las casillas tienen contadores a la derecha y a la izquierda
				this.pEdificios[i][j].setcIzquierda(new Contador(Math.random() * (100 - 800 + 1) + 800));
				if (j == largo && i != ancho) this.pEdificios[i][j].setcMorado(new Contador(Math.random() * (100 - 800 + 1) + 800)); //Inicializa los contadores morados
				if (j != 0 && j != largo) this.pEdificios[i][j].setcVerde(new Contador(Math.random() * (100 - 800 + 1) + 800)); //Inicializa los contadores verdes
				//Inicializacion de los manometros
				if (j != 0) { 
					this.pEdificios[i][j].setMan(new Manometro()); //Crea los manometros
					if (i==ancho && j==largo) this.pEdificios[i][j].getMan().setPresion(Math.random()*(110-150+1)+150);
					else {
						if (j==largo) { //Conducto general
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
	
	public ArrayList<String> resolverMedidores() {
		ArrayList<String> roturas = new ArrayList<String>();
		roturas.add("Manometros: ");
		roturas.addAll(resolverManometros());
		roturas.add("Contadores: ");
		roturas.addAll(resolverContadores());
		return roturas;
	}
	
	private ArrayList<String> resolverManometros() {
		ArrayList<String> resultado = new ArrayList<>();
		resultado.addAll(resolverManometrosGeneral());
		for (int i = 0;i<pEdificios.length;i++) {
			resultado.addAll(resolverManometrosDistribucion(i));
		}
		return resultado;
	}
	
	private ArrayList<String> resolverManometrosGeneral() {
		ArrayList<String> resultadoGeneral = new ArrayList<>();
		double aux;
		double current;
		for (int i = pEdificios.length-1;i>=1;i--) {
			aux = pEdificios[i-1][pEdificios[0].length-1].getMan().getPresion();
			current = pEdificios[i][pEdificios[0].length-1].getMan().getPresion();
			if (aux<(current-current*10/100))
				resultadoGeneral.add("["+ i + ", " + (pEdificios[0].length-1) + "]" + " - " + "["+ (i-1) + ", " + (pEdificios[0].length-1) + "]");
		}
		return resultadoGeneral;
	}
	
	private ArrayList<String> resolverManometrosDistribucion(int i) {
		ArrayList<String> resultadoDistribucion = new ArrayList<>();
		double aux;
		double current;
		for (int j = pEdificios[0].length-2;j>1;j--) {
			current = pEdificios[i][j].getMan().getPresion();
			aux = pEdificios[i][j-1].getMan().getPresion();
			if (aux<(current-current*10/100))
				resultadoDistribucion.add("["+ i + ", " + j + "]" + " - " + "["+ i + ", " + (j+1) + "]");
		}
		return resultadoDistribucion;
	}
	
	private ArrayList<String> resolverContadores() { //Para esto tendriamos que generar la matriz de medias y comparar casilla por casilla
		ArrayList<String> resultado = new ArrayList<>();
		//sresultado.addAll(resolverContadoresGeneral());
		for (int i = 0;i<matrizMedias.length;i++) {
			for (int j = 0; j<matrizMedias[0].length;j++) {
				if (pEdificios[i][j].getcDerecha().getConsumo() > (matrizMedias[i][j].getcDerecha().getConsumo()*7)) resultado.add(i + " " + j);
				if (pEdificios[i][j].getcIzquierda().getConsumo() > (matrizMedias[i][j].getcIzquierda().getConsumo()*7)) resultado.add(i + " " + j);
				if (pEdificios[i][j].getcMorado()!= null && (pEdificios[i][j].getcMorado().getConsumo() > (matrizMedias[i][j].getcMorado().getConsumo()*7))) resultado.add(i + " " + j);
				if (pEdificios[i][j].getcVerde()!= null && (pEdificios[i][j].getcDerecha().getConsumo() > (matrizMedias[i][j].getcDerecha().getConsumo()*7))) resultado.add(i + " " + j);
			}
		}
		return resultado;
	}
	
	//toString
	public String toString() {
		String resultado = "";
		for(int i=0; i<pEdificios.length; i++) {
			for(int j=0; j<pEdificios[i].length; j++) resultado += pEdificios[i][j].toString() +"       "+ i+" "+j+"\n";
			resultado += "\n";			
		}
		return resultado;
	}
	
	public String toStringM() { //Imprime en pantalla solo los manometros de la ciudad
		String resultado = "";
		for(int i=0; i<pEdificios.length; i++) {
			for(int j=0; j<pEdificios[i].length; j++) resultado += pEdificios[i][j].toStringManometros() +"       "+ i+" "+j+"\n";
			resultado += "\n";			
		}
		return resultado;
	}
	
	public String toStringC() { //Imprime en pantalla solo los manometros de la ciudad
		String resultado = "";
		for(int i=0; i<pEdificios.length; i++) {
			for(int j=0; j<pEdificios[i].length; j++) resultado += pEdificios[i][j].toStringContadores() +"       "+ i+" "+j+"\n";
			resultado += "\n";			
		}
		return resultado;
	}
	
	public String toStringMmedias() {
		String resultado = "";
		for (int i = 0;i<matrizMedias.length;i++) {
			for (int j = 0;j<this.matrizMedias[0].length;j++) {
				resultado += this.matrizMedias[i][j].toStringContadores();
				resultado += "\n";
			}
		}
		return resultado;
	}
	
	public void generarMatrizMedias() {
		int ancho = pEdificios.length-1;
		int largo = pEdificios[0].length-1;
		for(int i=0; i<matrizMedias.length; i++) 
			for(int j=0; j<matrizMedias[0].length; j++) {
				this.matrizMedias[i][j].setcDerecha(new Contador(Math.random() * (108 - 162 + 1) + 162)); //Todas las casillas tienen contadores a la derecha y a la izquierda
				this.matrizMedias[i][j].setcIzquierda(new Contador(Math.random() * (108 - 162 + 1) + 162));
				if (j == largo && i != ancho) this.matrizMedias[i][j].setcMorado(new Contador(Math.random() * (108 - 162 + 1) + 162)); //Inicializa los contadores morados
				if (j != 0 && j != largo) this.matrizMedias[i][j].setcVerde(new Contador(Math.random() * (108 - 162 + 1) + 162)); //Inicializa los contadores verdes
			}
	}
}
