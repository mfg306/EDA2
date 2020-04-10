package org.eda2.practica01;

import java.util.ArrayList;

public class Damero {
	
	private int filas;
	private int columnas;
	private ParEdificios[][] pEdificios;
	private ParEdificios[][] matrizMedias;
	private ArrayList<Object> roturasContadorTroncal = new ArrayList();
	private ArrayList<Object> roturasContadorLineasD = new ArrayList();
	
	private ParEdificios[] lineaTroncal;
	
	public final static double CONSUMO_MINIMO = 300000;
	public final static double CONSUMO_MAXIMO = 500000;
	//Presiones para la casilla general
	public final static double PRESION_MAXIMA = 150;
	public final static double PRESION_MINIMA = 110;
	
	private double suministroAgua;
	
	//CONSTRUCTOR
	public Damero(int columnas, int filas, double cauceInicial) {
		Contador.reiniciarId();
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
//		this.inicializarContadores(this.pEdificios);
//		setSuministroAgua(cauceInicial, this.pEdificios);
//		this.inicializarContadores(this.matrizMedias);
//		this.setSuministroAgua(500000,this.matrizMedias); //Datos de la empresa
//		this.inicializarManometros();
		
		
		//Podemos inicializar primero los contadores y luego los manometros en función de las roturas
		//que hayan tenido los contadores, ya que estos son mas complejos
		this.inicializarContadores();
		this.inicializarManometros();
		this.generarMatrizMedias();
	}
	
	//Obtener la linea troncal
	public ParEdificios[] lineaTroncal() {
		ParEdificios[] pE = new ParEdificios[pEdificios.length-1]; //Le quitamos la casilla general
		for (int i = 0;i<pE.length;i++) pE[i] = pEdificios[i][pEdificios[0].length-1];
		return pE;
	}
	
	//Obtener las medias de la linea troncal
	public ParEdificios[] lineaTroncalMedia() {
		ParEdificios[] pE = new ParEdificios[pEdificios.length-1]; //Le quitamos la casilla general
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
	private void inicializarContadores(ParEdificios[][] pEdificios) {
		// La primera fila tiene solo contadores morados, izq y der
		// El resto tiene verdes, izq y der
		// Lo inicializo todo a cero y cuando sepamos la cantidad inicial ya se la
		// asignamos a todos

		for (int i = 0; i < this.pEdificios.length; i++) {
			for (int j = 0; j < this.pEdificios[0].length; j++) {
				pEdificios[i][j] = new ParEdificios();
				pEdificios[i][j].setcDerecha(new Contador(0.0));
				pEdificios[i][j].setcIzquierda(new Contador(0.0));

				if (j == this.pEdificios[0].length - 1) { //En la casilla general no hay nada, solo un contador general
					pEdificios[i][j].setcMorado(new Contador(0.0));
				} else {
					pEdificios[i][j].setcVerde(new Contador(0.0));
				}
			}
		}

		if (this.filas % 2 == 0)
			this.inicializarContadoresPar(pEdificios);
		else
			this.inicializarContadoresImpar(pEdificios);
	}
	
	private void inicializarContadoresPar(ParEdificios[][] pEdificios) {
		int columnas = pEdificios.length - 1;
		for (int i = 0; i < pEdificios[0].length; i++) {
			pEdificios[columnas][i] = new ParEdificios();
			pEdificios[columnas][i].setcDerecha(new Contador(0.0));
			pEdificios[columnas][i].setcIzquierda(new Contador(0.0));

			if (i != pEdificios[0].length - 1)
				pEdificios[columnas][i].setcVerde(new Contador(0.0));
		}
	}

	/**
	 * La ultima columna la ponemos a null
	 */
	private void inicializarContadoresImpar(ParEdificios[][] pEdificios) {
		int columnas = pEdificios.length - 1;
		for (int i = 0; i < pEdificios[0].length; i++) {
			pEdificios[columnas][i] = new ParEdificios();
			pEdificios[columnas][i].setcIzquierda(new Contador(0.0));
			pEdificios[columnas][i].setcDerecha(null);
			if (i != pEdificios[0].length - 1)
				pEdificios[columnas][i].setcVerde(new Contador(0.0));
		}
	}
	
	
	
	
	
	
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
					this.pEdificios[i][j].setcDerecha(new Contador(con)); //CONTADOR GENERAL
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
	
	
	private ArrayList<Object> traducirIndices(int i) {
		double division = i / 2;
		Boolean esPar = (division % 2 == 0) ? true : false;
		double nuevaFila = Math.floor(division);
		ArrayList<Object> solucion = new ArrayList<>();

		solucion.add(nuevaFila);
		solucion.add(esPar);

		return solucion;
	}
	
	private void setSuministroAgua(double cauce, ParEdificios[][] pEdificios) {
		if (cauce > CONSUMO_MAXIMO)
			throw new RuntimeException("Ha introducido demasiada agua y se han roto las tuberías.");
		if (cauce < CONSUMO_MINIMO)
			throw new RuntimeException("El sistema no puede funcionar con tan poco cauce.");

		setLitrosEdificio(cauce, pEdificios);
	}
	
	private void setLitrosEdificio(double cauce, ParEdificios[][] pEdificios) { // ***
		int numTotalManzanas = (this.filas * this.columnas) - 1; // quitamos la general
		double porcionIndividual = cauce / numTotalManzanas;
		double porcentajeAVariar; // Esto es para que no todas las manzanas consuman exactamente lo mismo.
		double cantidadVerde;
		
		if (this.columnas % 2 != 0) {
			for (int i = 0; i < pEdificios.length; i++) {
				for (int j = 0; j < pEdificios[i].length; j++) {
					if (i == pEdificios.length - 1) { // En la ultima columna solo inicializamos izquierda y verde
						if (j == pEdificios[i].length - 1) pEdificios[i][j].setcIzquierda(new Contador(cauce)); // CASILLA GENERAL
						else {
							porcentajeAVariar = (Math.random() * (0 - 0.5 + 1) + 0.5);
							pEdificios[i][j].setcIzquierda(new Contador(porcionIndividual - (porcionIndividual * porcentajeAVariar)));
							// El contador verde en este caso tendra el consumo del edificio izquierdo
							pEdificios[i][j].setcVerde(pEdificios[i][j].getcVerde());
						}
					} else { //Para el resto de columnas, se da valor de forma normal
						porcentajeAVariar = (Math.random() * (0 - 0.5 + 1) + 0.5);
						pEdificios[i][j].setcIzquierda(new Contador(porcionIndividual - (porcionIndividual * porcentajeAVariar)));
						porcentajeAVariar = (Math.random() * (0 - 0.5 + 1) + 0.5);
						pEdificios[i][j].setcDerecha(new Contador(porcionIndividual - (porcionIndividual * porcentajeAVariar)));
						if (pEdificios[i][j].getcVerde() != null) {
							cantidadVerde = pEdificios[i][j].getcDerecha().getConsumo() + pEdificios[i][j].getcDerecha().getConsumo();
							pEdificios[i][j].setcVerde(new Contador(cantidadVerde));
						}
						if (pEdificios[i][j].getcMorado() != null) {
							//Aqui hay que dar valor el morado , no se si es mejor hacer un metodo aparte cuando ya lo tengamos todo

						}
					}
				}
			}
		} else { // Si es par, se da valor a todo lo que no esté a null
			for (int i = 0; i < pEdificios.length; i++) {
				for (int j = 0; j < pEdificios[i].length; j++) {
					//Aqui la casilla general es la ultima derecha
					if(i == pEdificios.length -1 && j == pEdificios[i].length -1) {
						pEdificios[i][j].setcDerecha(new Contador(cauce));
						porcentajeAVariar = (Math.random() * (0 - 0.5 + 1) + 0.5);
						pEdificios[i][j].setcIzquierda(new Contador(porcionIndividual - (porcionIndividual * porcentajeAVariar)));
					} else {
						porcentajeAVariar = (Math.random() * (0 - 0.5 + 1) + 0.5);
						pEdificios[i][j].setcIzquierda(new Contador(porcionIndividual - (porcionIndividual * porcentajeAVariar)));
						porcentajeAVariar = (Math.random() * (0 - 0.5 + 1) + 0.5);
						pEdificios[i][j].setcDerecha(new Contador(porcionIndividual - (porcionIndividual * porcentajeAVariar)));
						if (pEdificios[i][j].getcVerde() != null) {
							cantidadVerde = pEdificios[i][j].getcDerecha().getConsumo() + pEdificios[i][j].getcDerecha().getConsumo();
							pEdificios[i][j].setcVerde(new Contador(cantidadVerde));
						}
						if (pEdificios[i][j].getcMorado() != null) {
							//Aqui hay que dar valor el morado , no se si es mejor hacer un metodo aparte cuando ya lo tengamos todo
						}
					}
				}
			}
		}
	}

	public double getLitrosEdificio(int i, int j, ParEdificios[][] pEdificios) { // columnas, filas
		i--;
		j--;
		double filas = (double) this.traducirIndices(i).get(0);
		boolean par = (boolean) this.traducirIndices(i).get(1);

		if (filas > this.filas / 2 || i > this.columnas) {
			throw new IndexOutOfBoundsException( "Debe introducir un edificio válido. Compruebe que los índices son correctos.");
		}
		
		//Si el tablero es par
		if(this.columnas % 2 == 0) { //par es si la columna a la que accedemos es par
			return (par == true) ? pEdificios[(int) filas][j].getcIzquierda().getConsumo() : pEdificios[(int) filas][j].getcDerecha().getConsumo();
		} else { // Si no estamos en un tablero par 
			//Tenemos que ver si la posicion a la que queremos acceder es la ultima casilla, si es la ultima casilla 
			//no es derecha, es izquierda, porq la derecha esta a null 
			if((int) filas == this.pEdificios.length-1 && j == this.pEdificios[0].length-1) {
				return pEdificios[(int)filas][j].getcIzquierda().getConsumo();
			} else {
				return (par == true) ? pEdificios[(int) filas][j].getcIzquierda().getConsumo() : pEdificios[(int) filas][j].getcDerecha().getConsumo();
			}
		}
	}
	
	public double getLitrosCasillasSalvoCasillaGeneral() { //No contempla el caso de que sea impar el damero
		double resultado = 0;

		for (int i = 0; i < this.pEdificios.length; i++) {
			for (int j = 0; j < this.pEdificios[0].length; j++) {
				if (i == (this.columnas / 2) - 1 && j == this.filas - 1) break; // La general no
				System.out.println(i + "   " + j);
				if (columnas%2 != 0 && i == 0) {
					resultado += this.pEdificios[i][j].getcDerecha().getConsumo();
				} else {
					resultado += this.pEdificios[i][j].getcDerecha().getConsumo() + this.pEdificios[i][j].getcIzquierda().getConsumo();
				}
				
			}
		}
		return resultado;
	}
	
	public ArrayList<Object> consumoExcesivoTroncal() {
		int i = 0;
		int j = this.lineaTroncal().length - 1;
		
		return this.consumoExcesivoRec(this.lineaTroncal(),this.lineaTroncalMedia(), i, j);
	}
	
	
	public ArrayList<Object> consumoExcesivoLineasDistribucion(){
		ArrayList<Object> resultado = new ArrayList<>();
		int tamMax =  lineasDistribucion(1).length-1; //Todas las lineas tienen el mismo tamaño puesto que son matrices cuadradas. 
													// Le quitamos la ultima fila
		
		for(int i=0; i<tamMax; i++) { //Para cada fila llamamos al metodo consumoExcesivoRec 
			resultado = this.consumoExcesivoRec(lineasDistribucion(i),lineasDistribucionMedias(i), 0, lineasDistribucion(i).length-1);
//			if(roturasContadorLineasD.contains(resultado)) continue; //No añadimos duplicados
			if(!resultado.isEmpty()) roturasContadorLineasD.add(resultado);
		}
		return roturasContadorLineasD;
	}
	
	
	private ArrayList<Object> consumoExcesivoRec(ParEdificios[] pE, ParEdificios[] media, int i, int j) {
		int mitad;

		//Le voy a añadir un ID al Contador porque luego para buscarlo y decir en que casilla se encuentra creo que es lo mas 
		//rapido para buscarlo en funcion de esto
		
		
		//Lo que devuelve el ArrayList<Object>: 
		//1º -> Su ID (Como despues tenemos que hacer un estudio algoritmico de este metodo solo le metemos el ID para que no 
				//sea muy costoso. Luego hacemos un metodo que en función del ID nos diga donde se ubica el contador
		//2º -> El contador que ha provocado una rotura
		
		if (i >= j - 1) { // Caso base
			if(pE[i].getcDerecha().getConsumo() > 7*media[i].getcDerecha().getConsumo()) {
				roturasContadorTroncal.add(pE[i].getcDerecha().getId());
				roturasContadorTroncal.add(pE[i].getcDerecha());				
			}
			if(pE[i].getcIzquierda().getConsumo() > 7*media[i].getcIzquierda().getConsumo()) {
				roturasContadorTroncal.add(pE[i].getcIzquierda().getId());
				roturasContadorTroncal.add(pE[i].getcIzquierda());				
			}
			if(pE[i].getcMorado() != null && pE[i].getcMorado().getConsumo() > 7*media[i].getcMorado().getConsumo()) {
				roturasContadorTroncal.add(pE[i].getcMorado().getId());
				roturasContadorTroncal.add(pE[i].getcMorado());				
			}
			if(pE[i].getcVerde() != null && pE[i].getcVerde().getConsumo() > 7*media[i].getcVerde().getConsumo()) {
				roturasContadorTroncal.add(pE[i].getcVerde().getId());
				roturasContadorTroncal.add(pE[i].getcVerde());				
			}
			
		} else { // Casos recursivos
			mitad = (i + j) / 2;
			this.consumoExcesivoRec(pE, media, i, mitad - 1);
			this.consumoExcesivoRec(pE, media, mitad, j);
		}
		return this.roturasContadorTroncal;
	}
	
	public String interpretarSolucionConsumoExcesivo(ArrayList<Object> consumo) {
		String cadena = "";
		int contador = 0;
		
		//Obtenemos el ID del contador y lo buscamos en nuestra matriz

		if(!consumo.isEmpty()) {
			System.out.println("Debe revisar las siguientes casillas: ");
			for(Object o : consumo) {
				if(contador % 2 == 0) { //Solo nos interesa las posiciones pares
					Integer id = (Integer)o;
					
					for(int i=0; i<this.pEdificios.length; i++) {
						for(int j=0; j<this.pEdificios[i].length; j++) {
							if(Integer.compare(this.pEdificios[i][j].getcDerecha().getId(), id) == 0) {
								cadena += "* Casilla: [" + i+ ", " + j + "]\n";
							}
							if(Integer.compare(this.pEdificios[i][j].getcIzquierda().getId(), id) == 0) {
								cadena += "* Casilla: [" + i+ ", " + j + "]\n";

							}
							if(this.pEdificios[i][j].getcMorado() != null && Integer.compare(this.pEdificios[i][j].getcMorado().getId(), id) == 0) {
								cadena += "* Casilla: [" + i+ ", " + j + "]\n";
							}
							if(this.pEdificios[i][j].getcVerde() != null && Integer.compare(this.pEdificios[i][j].getcVerde().getId(), id) == 0) {
								cadena += "* Casilla: [" + i+ ", " + j + "]\n";
							}
						}
					}
				} 
				contador++;
			}
		}

		return cadena;
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	//MANOMETROS
	
	
	
	
	
	
	
	
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
	
	
	
	
	
	
	
	
	
	public double getSuministroAgua() {
		return suministroAgua;
	}
	
	
	
	
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
