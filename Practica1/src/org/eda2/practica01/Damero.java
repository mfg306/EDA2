package org.eda2.practica01;

import java.util.ArrayList;

public class Damero {
	
	private int filas;
	private int columnas;
	private ParEdificios[][] pEdificios;
	private ParEdificios[][] matrizMedias;
	private ArrayList<String> roturas;
	
	private ParEdificios[] lineaTroncal;
	
	public final static double CONSUMO_MINIMO = 108;
	public final static double CONSUMO_MAXIMO = 162;
	
	private double suministroAgua;
	
	//CONSTRUCTOR
	public Damero(int columnas, int filas, double cauceInicial) {
		this.filas = filas;
		this.columnas = columnas;
		if (filas % 2 == 0) {
			pEdificios = new ParEdificios[columnas/2][filas]; //i vale la mitad porque unimos dos columnas
			matrizMedias = new ParEdificios[columnas/2][filas]; //Creamos tambien la matriz de medias de consumo
		} else {
			pEdificios = new ParEdificios[(columnas+1) / 2][filas];
			matrizMedias = new ParEdificios[(columnas+1) / 2][filas];
		}
		
		for(int a = 0; a < pEdificios.length; a++) { 
			for(int b = 0; b < pEdificios[a].length; b++) {
				pEdificios[a][b] = new ParEdificios();
				matrizMedias[a][b] = new ParEdificios();
			}
		}
		this.inicializarMedidores();
		generarMatrizMedias();
		this.lineaTroncal = generarLineaTroncal();
	}
	
	public ParEdificios[] generarLineaTroncal() {
		ParEdificios[] pE = new ParEdificios[pEdificios.length];
		for (int i = 0;i<pE.length;i++) pE[i] = pEdificios[i][pEdificios[0].length-1];
		return pE;
	}
	
	public ParEdificios[][] getDamero() {
		return this.pEdificios;
	}
	
	//MEDIDORES
	
	private void inicializarMedidores() {
		inicializarContadores();
		inicializarManometros();
	}
	
	
	
	//CONTADORES
	
	public void inicializarContadores() {
		if(columnas%2==0) inicializarContadoresPar();
		else inicializarContadoresImpar();
	}
	
	private void inicializarContadoresPar() { //Deberiamos inicializar primero los de cada edificio y a partir de ese
		
		int y = pEdificios.length;
		int jota = pEdificios[0].length;
		
		//RECORREMOS EL ARRAY INICIALIZANDO LOS CONTADORES ROJOS
		for (int i = 0;i<pEdificios.length;i++) {
			for (int j = 0; j<pEdificios[0].length;j++) {
				if (i!=pEdificios.length-1 || j!=pEdificios[0].length-1) 
					this.pEdificios[i][j].setcDerecha(new Contador(Math.random() * (100 - 1000 + 1) + 1000));
				this.pEdificios[i][j].setcIzquierda(new Contador(Math.random() * (100 - 1000 + 1) + 1000));
			}
		}
		
		//INICIALIZAMOS LO CONTADORES VERDES Y MORADOS Y EL GENERAL
		double con = 0;
		for (int i = 0;i < pEdificios.length;i++) {
			for (int j = 1; j < pEdificios[0].length;j++) {
				
				if (i==pEdificios.length-1 && j==pEdificios[0].length-1) {
					con += pEdificios[i][j].getcIzquierda().getConsumo();
					con += pEdificios[i][j-1].getcVerde().getConsumo();
					con += pEdificios[i-1][j].getcMorado().getConsumo();
					this.pEdificios[i][j].setcDerecha(new Contador(con));
					continue;
				}
				if (j==pEdificios[0].length-1 && i != pEdificios.length-1) { //linea de distribucion
					con += pEdificios[i][j-1].getcVerde().getConsumo();
					con += pEdificios[i][j].getcDerecha().getConsumo();
					con += pEdificios[i][j].getcIzquierda().getConsumo();
					if (i!=0) con += pEdificios[i-1][j].getcMorado().getConsumo();
					pEdificios[i][j].setcMorado(new Contador(con));
				} else if (pEdificios[i][j-1].getcVerde()==null) { //final de la linea de distribucion por abajo
					con += pEdificios[i][j].getcDerecha().getConsumo();
					con += pEdificios[i][j].getcIzquierda().getConsumo();
					con += pEdificios[i][j-1].getcDerecha().getConsumo();
					con += pEdificios[i][j-1].getcIzquierda().getConsumo();
					pEdificios[i][j].setcVerde(new Contador(con));
				} else { //caso base
					con += pEdificios[i][j].getcDerecha().getConsumo();
					con += pEdificios[i][j].getcIzquierda().getConsumo();
					con += pEdificios[i][j-1].getcVerde().getConsumo();
					pEdificios[i][j].setcVerde(new Contador(con));
				}
				con = 0;
			}
		}
	}
	
	private void inicializarContadoresImpar() {
		inicializarContadoresPar();
		for (int j = 0; j < pEdificios[0].length;j++) {
			this.pEdificios[0][j].setcIzquierda(null);
		}
	}
	
	
	
	//MANOMETROS
	
	private void inicializarManometros() {
		
		int ancho = pEdificios.length-1;
		int alto = pEdificios[0].length-1;
		double pAnterior, error;
		for(int j=pEdificios[0].length-1; j>=1; j--) {
			for(int i=pEdificios.length-1; i>=0; i--) {				
				if (i==ancho && j==alto) { //Manómetro general
					this.pEdificios[i][j].setMan(new Manometro(Math.random()*(110-150+1)+150));
					continue;
				}
				//Obtenemos el valor de la presion del manometro anterior para obtener el siguiente a partir de él
				if (j==alto) pAnterior = this.pEdificios[i+1][j].getMan().getPresion();
				else pAnterior = this.pEdificios[i][j+1].getMan().getPresion();
				
				error = pAnterior - (pAnterior*13/100); //Margen de error del manometro
				this.pEdificios[i][j].setMan(new Manometro((Math.random()*(error-pAnterior+1)+pAnterior))); //Establecemos la presion del manómetro
			}
		}
	}
	
	
	
	//TOSTRING
	
	public String toString() {
		String resultado = "";
		for(int j=0; j<pEdificios[0].length; j++) {
			for(int i=0; i<pEdificios.length; i++) resultado += pEdificios[i][j].toString() +"       "+ i+" "+j+"\t";
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
				resultado += this.matrizMedias[i][j].toStringContadores() + "\t" + i + " " + j;
				resultado += "\n";
			}
		}
		return resultado;
	}
	
	//MATRIZ MEDIAS
	public void generarMatrizMedias() {
		//RECORREMOS EL ARRAY INICIALIZANDO LOS CONTADORES ROJOS
				for (int i = 0;i<matrizMedias.length;i++) {
					for (int j = 0; j<matrizMedias[0].length;j++) {
						this.matrizMedias[i][j].setcDerecha(new Contador(Math.random() * (CONSUMO_MINIMO - CONSUMO_MAXIMO + 1) + CONSUMO_MAXIMO));
						this.matrizMedias[i][j].setcIzquierda(new Contador(Math.random() * (CONSUMO_MINIMO - CONSUMO_MAXIMO + 1) + CONSUMO_MAXIMO));
					}
				}
				
				//INICIALIZAMOS LO CONTADORES VERDES Y MORADOS
				double con = 0;
				for (int i = 0;i<matrizMedias.length;i++) {
					for (int j = 1; j<matrizMedias[0].length;j++) {
						con += matrizMedias[i][j].getcDerecha().getConsumo();
						con += matrizMedias[i][j].getcIzquierda().getConsumo();
						if (j==matrizMedias[0].length-1) { //linea de distribucion
							con += matrizMedias[i][j-1].getcVerde().getConsumo();
							if (i!=0) con += matrizMedias[i-1][j].getcMorado().getConsumo();
							matrizMedias[i][j].setcMorado(new Contador(con));
						} else if (matrizMedias[i][j-1].getcVerde()==null) {
							con += matrizMedias[i][j-1].getcDerecha().getConsumo();
							con += matrizMedias[i][j-1].getcIzquierda().getConsumo();
							matrizMedias[i][j].setcVerde(new Contador(con));
						} else {
							con += matrizMedias[i][j-1].getcVerde().getConsumo();
							matrizMedias[i][j].setcVerde(new Contador(con));
						}
						con = 0;
					}
				}
				
				//CONTADOR GENERAL
				matrizMedias[matrizMedias.length-1][matrizMedias[0].length-1].setcDerecha(new Contador(Math.random() * (CONSUMO_MINIMO*7 - CONSUMO_MAXIMO*7 + 1) + CONSUMO_MAXIMO*7));
	}
	
	public void setSuministroAgua(double cauce) {
		if (cauce > CONSUMO_MAXIMO)
			throw new RuntimeException("Ha introducido demasiada agua y se han roto las tuberias");
		if (cauce < CONSUMO_MINIMO)
			throw new RuntimeException("El sistema no puede funcionar con tan poco cauce");
		this.suministroAgua = cauce;
		setLitrosEdificio();
	}
	
	private void setLitrosEdificio() {
		double rotura = Math.round(Math.random()*100)/100;
		double targetSide;
		
		if (rotura < 0.7) {
			for (int i = 0; i < this.columnas /2; i++) {
				for (int j = 0; j < this.filas;j++) {
					this.pEdificios[i][j].setcDerecha(new Contador(Math.random()*(CONSUMO_MAXIMO-CONSUMO_MINIMO+1)+CONSUMO_MAXIMO));
					this.pEdificios[i][j].setcIzquierda(new Contador(Math.random()*(CONSUMO_MAXIMO-CONSUMO_MINIMO+1)+CONSUMO_MAXIMO));
				}
			}
		} else {
			for (int i = 0; i < this.columnas/2; i++) {
				for (int j = 0;j<this.filas;j++) {
					targetSide = Math.round(Math.random()*1000)/10;
					if (Math.round(Math.random()*1000)/10 > 0.5) {
						if (targetSide % 2 == 0) {
							this.pEdificios[i][j].setcDerecha(new Contador((Math.random()*(CONSUMO_MAXIMO-CONSUMO_MINIMO+1)+CONSUMO_MAXIMO)* rotura + 100));
							this.pEdificios[i][j].setcIzquierda(new Contador(Math.random()*(CONSUMO_MAXIMO-CONSUMO_MINIMO+1)+CONSUMO_MAXIMO));
						} else {
							this.pEdificios[i][j].setcDerecha(new Contador(Math.random()*(CONSUMO_MAXIMO-CONSUMO_MINIMO+1)+CONSUMO_MAXIMO));
							this.pEdificios[i][j].setcIzquierda(new Contador((Math.random()*(CONSUMO_MAXIMO-CONSUMO_MINIMO+1)+CONSUMO_MAXIMO)* rotura + 100));
						}
					}
				}
			}
		}
	}
	
	public ArrayList<Object> traducirIndices(int i) {
		double division = i/2;
		Boolean esPar = (division % 2 == 0) ? true : false;
		double nuevaFila = Math.floor(division);
		
		ArrayList<Object> solucion = new ArrayList<>();
		
		solucion.add(nuevaFila);
		solucion.add(esPar);
		
		return solucion;
	}
	
	public double getLitrosEdificio(int i, int j) {
		// El usuario desconoce que se empieza por 0
		i--;
		j--;
		if (i > this.columnas || j > this.filas)
			throw new RuntimeException("Introduzca un edificio v�lido");

		ArrayList<Object> traduccion = this.traducirIndices(i);

		double nuevoIndice = (double) traduccion.get(0);
		Boolean esPar = (Boolean) traduccion.get(1);

		if (esPar)
			return pEdificios[(int) nuevoIndice][j].getcIzquierda().getConsumo();
		else
			return pEdificios[(int) nuevoIndice][j].getcDerecha().getConsumo();

	}
	
	public double getLitrosCasillasSalvoCasillaGeneral() {
		double resultado = 0;
		for (int i = 0; i < columnas / 2; i++) {
			for (int j = 0; j < filas; j++) {
				resultado += this.pEdificios[i][j].getcIzquierda().getConsumo()
						+ this.pEdificios[i][j].getcDerecha().getConsumo();
			}
		}
		return resultado;
	}

	public double getSuministroAgua() {
		return suministroAgua;
	}
	
//	public ArrayList<Contador> consumoExcesivoTroncal(){
//		ArrayList<Contador> resultado = new ArrayList<>();
//		Contador[] contadoresTroncal = this.traducirMatrizParEdificiosAArrayContadores(this.lineaTroncal);
//
//		resultado = this.consumoExcesivoRec(contadoresTroncal, this.columnas/2, this.filas);
//		
//		return resultado;
//	}
	
	private ArrayList<Contador> consumoExcesivoRec(Contador[] troncal, int i, int j) {
		ArrayList<Contador> resultado = new ArrayList<>();
		
		int inicio = 0;
		int fin = this.columnas-1;
		int mitad;
		
		if(inicio >= fin) return resultado;
		else { //Casos recursivos
			mitad = (inicio + fin)/2;
			 this.consumoExcesivoRec(troncal, inicio, mitad);
			 this.consumoExcesivoRec(troncal, mitad+1, fin);
			 
		}
		return resultado;
	}
	
	
	private Contador[] traducirMatrizParEdificiosAArrayContadores(ParEdificios[] pE) {
		Contador[] contadores = new Contador[pE.length*2];
		
		for(int i=0; i<contadores.length; i++) {
			contadores[i] = pE[i].getcDerecha();
			contadores[i+1] = pE[i].getcIzquierda();
		}
		
		return contadores;
	}
	
	public String toStringSolucion(ArrayList<String> solucion) {
		String cadena = "";
		for (int i = 0;i<this.roturas.size();i++) {
			cadena += this.roturas.get(i) + "\n";
			
		}
		return cadena;
		
	}
	
	//RESOLVER
	
	public String resolverMedidores() {
		ArrayList<String> roturas = new ArrayList<String>();
		roturas.add("Manometros: ");
		roturas.addAll(resolverManometros());
		roturas.add("");
		roturas.add("Contadores: ");
		roturas.addAll(resolverContadores());
		this.roturas = roturas;
		return toStringSolucion(roturas);
	}
	
	private ArrayList<String> resolverContadores() { //Para esto tendriamos que generar la matriz de medias y comparar casilla por casilla
		ArrayList<String> resultado = new ArrayList<>();
		for (int i = 0;i<matrizMedias.length;i++) {
			for (int j = 0; j<matrizMedias[0].length;j++) {
				if (pEdificios[i][j].getcDerecha().getConsumo() > (matrizMedias[i][j].getcDerecha().getConsumo()*7)) {
					//resultado.add("D: " + i + " " + j);
					if (i == matrizMedias.length-1 && j==matrizMedias[0].length-1) resultado.add("Contador general");
					else resultado.add("Edificio " + ((i*2)+2) + " de la calle " + (j+1));
				}
				if (pEdificios[i][j].getcIzquierda() != null && 
						(pEdificios[i][j].getcIzquierda().getConsumo() > (matrizMedias[i][j].getcIzquierda().getConsumo()*7))) {
					//resultado.add("I: " + i + " " + j);
					resultado.add("Edificio " + ((i*2)+1) + " de la calle " + (j+1));
				}
				if (pEdificios[i][j].getcMorado() != null && 
						(pEdificios[i][j].getcMorado().getConsumo() > 
						(matrizMedias[i][j].getcMorado().getConsumo()*7))) {
					//resultado.add("M: " + i + " " + j);
					resultado.add("Contador troncal entre las manzanas " + ((i*2)+1) + " y " + ((i*2)+2) + " de la calle " + (j+1));
				}
				if (pEdificios[i][j].getcVerde() != null && 
						(pEdificios[i][j].getcDerecha().getConsumo() > (matrizMedias[i][j].getcVerde().getConsumo()*7))) {
					//resultado.add("V: " + i + " " + j);
					resultado.add("Contador de distribucion de las manzanas " + ((i*2)+1) + " y " + ((i*2)+2) + " de la calle " + (j+1));
				}
			}
		}
		return resultado;
	}
	
	private ArrayList<String> resolverManometros() {
		ArrayList<String> general = new ArrayList<>();
		ArrayList<String> distribucion = new ArrayList<>();
		ArrayList<String> resultado = new ArrayList<>();
		
		general.addAll(resolverManometrosGeneral());
		for (int i = 0;i<pEdificios.length;i++) {
			distribucion.addAll(resolverManometrosDistribucion(i));
		}
		
		resultado.add("TRONCAL");
		if(general.isEmpty()) resultado.add("No se han producido roturas en la linea troncal.");
		else {
			resultado.add("Se han producido roturas entre las manzanas:");
			resultado.addAll(general);
		}
		
		resultado.add("DISTRIBUCION");
		if (distribucion.isEmpty()) resultado.add("No se han producido roturas en ninguna linea de distribucion.");
		else {
			resultado.add("Se han producido roturas entre las manzanas:");
			resultado.addAll(distribucion);
		}
		
		return resultado;
	}
	
	private ArrayList<String> resolverManometrosRecursivo() {
		ArrayList<String> resultado = new ArrayList<>();
		for (int i = 0;i<pEdificios.length;i++) {
			for (int j = 0;j<pEdificios[0].length;j++) {
				
			}
		}
		resultado.addAll(resolverManometrosRecursivo());
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
				resultadoGeneral.add( ((2*i)+1) + " - " + ((2*i)+2));
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
			if (aux<(current-current*10/100)) {
				//resultadoDistribucion.add("["+ i + ", " + j + "]" + " - " + "["+ i + ", " + (j+1) + "]");
				resultadoDistribucion.add(((2*i)+1) + " - " + ((2*i)+2) + "\t De la calle " + j+1);
			}
				
				
		}
		return resultadoDistribucion;
	}
}
