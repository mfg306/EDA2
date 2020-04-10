package org.eda2.practica01;

import java.util.ArrayList;

public class Damero {
	
	private int filas;
	private int columnas;
	private ParEdificios[][] pEdificios;
	private ParEdificios[][] matrizMedias;
	private ArrayList<Object> roturasContadorTroncal = new ArrayList<Object>();
	private ArrayList<Object> roturasContadorLineasD = new ArrayList<Object>();
	private ArrayList<Object> roturasProblemaRecursivo = new ArrayList<>();
	
	public final static double CONSUMO_MINIMO_GENERAL = 300000;
	public final static double CONSUMO_MAXIMO_GENERAL = 500000;
	
	public final static double CONSUMO_MINIMO_MEDIO = 300000;
	public final static double CONSUMO_MAXIMO_MEDIO = 500000;
	//Presiones para la casilla general
	public final static double PRESION_MAXIMA = 150;
	public final static double PRESION_MINIMA = 110;
	
	
	
	//CONSTRUCTOR
	public Damero(int columnas, int filas) {
		Contador.reiniciarId();
		this.roturasContadorTroncal.clear();
		this.roturasContadorLineasD.clear();
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
		
		//Podemos inicializar primero los contadores y luego los manometros en función de las roturas
		//que hayan tenido los contadores, ya que estos son mas complejos
		this.inicializarContadores();
		this.inicializarManometros();
		this.generarMatrizMedias();
	}
	
	//Obtener la linea troncal
	public ParEdificios[] lineaTroncal() {
		ParEdificios[] pE = new ParEdificios[pEdificios.length]; 
		for (int i = 0;i<pE.length;i++) pE[i] = pEdificios[i][pEdificios[0].length-1];
		return pE;
	}
	
	//Obtener las medias de la linea troncal
	public ParEdificios[] lineaTroncalMedia() {
		ParEdificios[] pE = new ParEdificios[pEdificios.length]; 
		for (int i = 0; i<pE.length;i++) pE[i] = matrizMedias[i][pEdificios[0].length-1];
		return pE;
	}
	
	//Obtener una determinada linea de distribucion
	public ParEdificios[] lineasDistribucion(int i) {
		ParEdificios[] pE = new ParEdificios[this.pEdificios[0].length-1]; //Le quitamos la linea troncal
		for (int j = 0;j<this.pEdificios[i].length-1;j++) pE[j] = this.pEdificios[i][j];
		return pE;
	}
	
	//Obtener las medias de una determinada linea de distribucion
	public ParEdificios[] lineasDistribucionMedias(int i){
		ParEdificios[] pE = new ParEdificios[this.matrizMedias[0].length-1]; //Le quitamos la linea troncal
		for(int j=0; j<this.matrizMedias[i].length-1; j++) pE[j] = this.matrizMedias[i][j];
		return pE;
	}
	
	//Getter y Setters
	public ParEdificios[][] getDamero() {
		return this.pEdificios;
	}
	
	public ParEdificios[][] getMedias() {
		return this.matrizMedias;
	}
	
	public ArrayList<Object> getRoturasContadorTroncal() {
		return roturasContadorTroncal;
	}
	
	public ArrayList<Object> getRoturasContadorLineasD() {
		return roturasContadorLineasD;
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
				if (i!=pEdificios.length-1 || j!=pEdificios[0].length-1) //CASILLA GENERAL
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
					//System.out.println("  izq" +  pEdificios[i][j].getcIzquierda().getConsumo());
					con += pEdificios[i][j-1].getcVerde().getConsumo();
					//System.out.println("verde   " + pEdificios[i][j-1].getcVerde().getConsumo());
					con += pEdificios[i-1][j].getcMorado().getConsumo();
					//System.out.println("morado   " + pEdificios[i-1][j].getcMorado().getConsumo());
					this.pEdificios[i][j].setcDerecha(new Contador(con)); //CONTADOR GENERAL
					System.out.println("");
					System.out.println("");
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
		
		//MATRIZ MEDIAS
		public void generarMatrizMedias() {
			//RECORREMOS EL ARRAY INICIALIZANDO LOS CONTADORES ROJOS
					for (int i = 0;i<matrizMedias.length;i++) {
						for (int j = 0; j<matrizMedias[0].length;j++) {
							this.matrizMedias[i][j].setcDerecha(new Contador(Math.random() * (CONSUMO_MINIMO_MEDIO - CONSUMO_MAXIMO_MEDIO + 1) + CONSUMO_MAXIMO_MEDIO));
							this.matrizMedias[i][j].setcIzquierda(new Contador(Math.random() * (CONSUMO_MINIMO_MEDIO - CONSUMO_MAXIMO_MEDIO + 1) + CONSUMO_MAXIMO_MEDIO));
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
					matrizMedias[matrizMedias.length-1][matrizMedias[0].length-1].setcDerecha(new Contador(Math.random() * (CONSUMO_MINIMO_MEDIO*7 - CONSUMO_MAXIMO_MEDIO*7 + 1) + CONSUMO_MAXIMO_MEDIO*7));
		}
		
		
	//ToString
	
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
	
	

	
	public double getLitrosEdificio(int i, int j, ParEdificios[][] pEdificios, String cont) { // columnas, filas
		//DIVM
		if (cont.equals("D")) return pEdificios[i][j].getcDerecha().getConsumo();
		else if (cont.equals("I")) return pEdificios[i][j].getcIzquierda().getConsumo();
		else if (cont.equals("V")) return pEdificios[i][j].getcVerde().getConsumo();
		else return pEdificios[i][j].getcMorado().getConsumo();
	}
	
	//REVISAR
	//EL VALOR DEBERIA COINCIDIR CON LA CASILLA GENERAL PERO NO COINCIDE
	public double getLitrosCasillasSalvoCasillaGeneral() {
		double resultado = 0;

		for (int i = 0; i < this.pEdificios.length; i++) {
			for (int j = 0; j < this.pEdificios[0].length; j++) {
				if (i == (this.pEdificios.length-1) && j == this.pEdificios[0].length - 1) {// La general no
					resultado+=this.pEdificios[i][j].getcIzquierda().getConsumo();
				} else if (columnas%2 != 0 && i == 0) {
					resultado += this.pEdificios[i][j].getcDerecha().getConsumo();
				} else {
					resultado += this.pEdificios[i][j].getcDerecha().getConsumo() + this.pEdificios[i][j].getcIzquierda().getConsumo();
				}
				
			}
		}
		return resultado;
	}
	
	
	//DyV
	/**
	 * @return un ArrayList de Object en el que guardaremos el contador que ha provocado una rotura de segundo grado en la linea troncal. 
	 * (ID, Contador)
	 */
	public ArrayList<Object> consumoExcesivoTroncal() {
		ArrayList<Object> resultado = new ArrayList<>();
		int i = 0;
		int j = this.lineaTroncal().length-1;
		
		roturasProblemaRecursivo.clear();
		resultado = this.consumoExcesivoRec(this.lineaTroncal(),this.lineaTroncalMedia(), i, j);
		
		if(!resultado.isEmpty()) this.roturasContadorTroncal.addAll(resultado);
		
		return resultado;
	}

	
	/**
	 * @return un ArrayList de Object en el que guardaremos el contador que ha provocado una rotura de segundo grado en las lineas de distribucion.
	 *  (ID, Contador)
	 */
	public ArrayList<Object> consumoExcesivoLineasDistribucion(){
		ArrayList<Object> resultado = new ArrayList<>();
		int tamMax = this.pEdificios.length;
		
		roturasProblemaRecursivo.clear();

		for(int i=0; i<tamMax; i++) { //Para cada columna llamamos al metodo consumoExcesivoRec 
			resultado = this.consumoExcesivoRec(lineasDistribucion(i),lineasDistribucionMedias(i), 0, lineasDistribucion(i).length-1);
			if(!resultado.isEmpty()) roturasContadorLineasD.addAll(resultado);
		}
		return roturasContadorLineasD;
	}
	
	/**
	 * Nuestro algoritmo recursivo va a dividir el problema hasta el máximo. Es decir, vamos a coger nuestro array de parEdificios 
	 * y vamos a simplificarlo hasta quedarnos con un solo par. Sobre este par podemos ver si se dan las condiciones necesarias 
	 * para que se produzca una rotura
	 * @param pE es la estructura a la que vamos a buscarle roturas de contadores
	 * @param i posicion inicial a partir de la cual vamos a empzar a buscar
	 * @param j posicion final de la búsqueda
	 * @return un ArrayList con todos los contadores que han presentado una rotura
	 */
	private ArrayList<Object> consumoExcesivoRec(ParEdificios[] pE, ParEdificios[] media, int i, int j) {
		int mitad;
		System.out.println(i+"  "+j);
		System.out.println(pE.length);
		System.out.println(media.length);
		//Le voy a añadir un ID al Contador porque luego para buscarlo y decir en que casilla se encuentra creo que es lo mas 
		//rapido para buscarlo en funcion de esto
		
		
		//Lo que devuelve el ArrayList<Object>: 
		//1º -> Su ID (Como despues tenemos que hacer un estudio algoritmico de este metodo solo le metemos el ID para que no 
				//sea muy costoso. Luego hacemos un metodo que en función del ID nos diga donde se ubica el contador
		//2º -> El contador que ha provocado una rotura
		
		if (i == j) { // Caso base
			if(pE[i].getcDerecha() != null && pE[i].getcDerecha().getConsumo() > 7*media[i].getcDerecha().getConsumo()) {
					roturasProblemaRecursivo.add(pE[i].getcDerecha().getId());
					roturasProblemaRecursivo.add(pE[i].getcDerecha());
				
			}
			if(pE[i].getcIzquierda() != null && pE[i].getcIzquierda().getConsumo() > 7*media[i].getcIzquierda().getConsumo()) {
					roturasProblemaRecursivo.add(pE[i].getcIzquierda().getId());
					roturasProblemaRecursivo.add(pE[i].getcIzquierda());	
			}
			if(pE[i].getcMorado() != null && pE[i].getcMorado().getConsumo() > 7*media[i].getcMorado().getConsumo()) {
					roturasProblemaRecursivo.add(pE[i].getcMorado().getId());
					roturasProblemaRecursivo.add(pE[i].getcMorado());	
			}
			if(pE[i].getcVerde() != null && pE[i].getcVerde().getConsumo() > 7*media[i].getcVerde().getConsumo()) {
					roturasProblemaRecursivo.add(pE[i].getcVerde().getId());
					roturasProblemaRecursivo.add(pE[i].getcVerde());		
			}
			
		} else { // Casos recursivos
			mitad = (i + (j+1)) / 2;
			this.consumoExcesivoRec(pE, media, i, mitad - 1);
			this.consumoExcesivoRec(pE, media, mitad, j);
		}
		return roturasProblemaRecursivo;
	}
	
	/**
	 * @return una cadena con las casillas en las que se ha producido una rotura
	 */
	public String interpretarSolucionConsumoExcesivo(ArrayList<Object> consumo) {
		String cadena = "";
		int contador = 0;
		
		//Obtenemos el ID del contador y lo buscamos en nuestra matriz

		if(!consumo.isEmpty()) {
			for(Object o : consumo) {
				if(contador % 2 == 0) { //Solo nos interesa las posiciones pares
					Integer id = (Integer)o;
					
					for(int i=0; i<this.pEdificios.length; i++) {
						for(int j=0; j<this.pEdificios[i].length; j++) {
							if(Integer.compare(this.pEdificios[i][j].getcDerecha().getId(), id) == 0) {
								cadena += "* Casilla: [" + i+ ", " + j + "]. El contador derecha ha provocado la rotura\n";
							}
							if(Integer.compare(this.pEdificios[i][j].getcIzquierda().getId(), id) == 0) {
								cadena += "* Casilla: [" + i+ ", " + j + "]. El contador izquierda ha provocado la rotura\n";
							}
							if(this.pEdificios[i][j].getcMorado() != null && Integer.compare(this.pEdificios[i][j].getcMorado().getId(), id) == 0) {
								cadena += "* Casilla: [" + i+ ", " + j + "]. El contador morado ha provocado la rotura\n";
							}
							if(this.pEdificios[i][j].getcVerde() != null && Integer.compare(this.pEdificios[i][j].getcVerde().getId(), id) == 0) {
								cadena += "* Casilla: [" + i+ ", " + j + "]. El contador verde ha provocado la rotura\n";
							}
						}
					}
				} 
				contador++;
			}
		}

		return (cadena.isEmpty())? "No hay roturas.\n ": cadena;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	//MANOMETROS
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//RESOLVER
	
//	public String resolverMedidores() {
//		ArrayList<String> roturas = new ArrayList<String>();
//		roturas.add("Manometros: ");
//		roturas.addAll(resolverManometros());
//		roturas.add("");
//		roturas.add("Contadores: ");
//		roturas.addAll(resolverContadores());
//		this.roturas = roturas;
//		return toStringSolucion(roturas);
//	}
//	
//	private ArrayList<String> resolverContadores() { //Para esto tendriamos que generar la matriz de medias y comparar casilla por casilla
//		ArrayList<String> resultado = new ArrayList<>();
//		for (int i = 0;i<matrizMedias.length;i++) {
//			for (int j = 0; j<matrizMedias[0].length;j++) {
//				if (pEdificios[i][j].getcDerecha().getConsumo() > (matrizMedias[i][j].getcDerecha().getConsumo()*7)) {
//					//resultado.add("D: " + i + " " + j);
//					if (i == matrizMedias.length-1 && j==matrizMedias[0].length-1) resultado.add("Contador general");
//					else resultado.add("Edificio " + ((i*2)+2) + " de la calle " + (j+1));
//				}
//				if (pEdificios[i][j].getcIzquierda() != null && 
//						(pEdificios[i][j].getcIzquierda().getConsumo() > (matrizMedias[i][j].getcIzquierda().getConsumo()*7))) {
//					//resultado.add("I: " + i + " " + j);
//					resultado.add("Edificio " + ((i*2)+1) + " de la calle " + (j+1));
//				}
//				if (pEdificios[i][j].getcMorado() != null && 
//						(pEdificios[i][j].getcMorado().getConsumo() > 
//						(matrizMedias[i][j].getcMorado().getConsumo()*7))) {
//					//resultado.add("M: " + i + " " + j);
//					resultado.add("Contador troncal entre las manzanas " + ((i*2)+1) + " y " + ((i*2)+2) + " de la calle " + (j+1));
//				}
//				if (pEdificios[i][j].getcVerde() != null && 
//						(pEdificios[i][j].getcDerecha().getConsumo() > (matrizMedias[i][j].getcVerde().getConsumo()*7))) {
//					//resultado.add("V: " + i + " " + j);
//					resultado.add("Contador de distribucion de las manzanas " + ((i*2)+1) + " y " + ((i*2)+2) + " de la calle " + (j+1));
//				}
//			}
//		}
//		return resultado;
//	}
//	
//	private ArrayList<String> resolverManometros() {
//		ArrayList<String> general = new ArrayList<>();
//		ArrayList<String> distribucion = new ArrayList<>();
//		ArrayList<String> resultado = new ArrayList<>();
//		
//		general.addAll(resolverManometrosGeneral());
//		for (int i = 0;i<pEdificios.length;i++) {
//			distribucion.addAll(resolverManometrosDistribucion(i));
//		}
//		
//		resultado.add("TRONCAL");
//		if(general.isEmpty()) resultado.add("No se han producido roturas en la linea troncal.");
//		else {
//			resultado.add("Se han producido roturas entre las manzanas:");
//			resultado.addAll(general);
//		}
//		
//		resultado.add("DISTRIBUCION");
//		if (distribucion.isEmpty()) resultado.add("No se han producido roturas en ninguna linea de distribucion.");
//		else {
//			resultado.add("Se han producido roturas entre las manzanas:");
//			resultado.addAll(distribucion);
//		}
//		
//		return resultado;
//	}
//	
//	private ArrayList<String> resolverManometrosRecursivo() {
//		ArrayList<String> resultado = new ArrayList<>();
//		for (int i = 0;i<pEdificios.length;i++) {
//			for (int j = 0;j<pEdificios[0].length;j++) {
//				
//			}
//		}
//		resultado.addAll(resolverManometrosRecursivo());
//		return resultado;
//	}
//	
//	private ArrayList<String> resolverManometrosGeneral() {
//		ArrayList<String> resultadoGeneral = new ArrayList<>();
//		double aux;
//		double current;
//		for (int i = pEdificios.length-1;i>=1;i--) {
//			aux = pEdificios[i-1][pEdificios[0].length-1].getMan().getPresion();
//			current = pEdificios[i][pEdificios[0].length-1].getMan().getPresion();
//			if (aux<(current-current*10/100))
//				resultadoGeneral.add( ((2*i)+1) + " - " + ((2*i)+2));
//		}
//		return resultadoGeneral;
//	}
//	
//	private ArrayList<String> resolverManometrosDistribucion(int i) {
//		ArrayList<String> resultadoDistribucion = new ArrayList<>();
//		double aux;
//		double current;
//		
//		for (int j = pEdificios[0].length-2;j>1;j--) {
//			current = pEdificios[i][j].getMan().getPresion();
//			aux = pEdificios[i][j-1].getMan().getPresion();
//			if (aux<(current-current*10/100)) {
//				//resultadoDistribucion.add("["+ i + ", " + j + "]" + " - " + "["+ i + ", " + (j+1) + "]");
//				resultadoDistribucion.add(((2*i)+1) + " - " + ((2*i)+2) + "\t De la calle " + j+1);
//			}
//				
//				
//		}
//		return resultadoDistribucion;
//	}
}
