package org.eda2.greedy;

import java.util.ArrayList;

public class Damero {

	private int filas;
	private int columnas;
	private ParEdificios[][] pEdificios;
	private ParEdificios[][] matrizMedias;
	private ArrayList<Object> roturasContadorTroncal = new ArrayList<Object>();
	private ArrayList<Object> roturasContadorLineasD = new ArrayList<Object>();
	// Consumo minimo y maximo del contador general
	public final static double CONSUMO_MINIMO_GENERAL = 300000;
	public final static double CONSUMO_MAXIMO_GENERAL = 500000;
	// Consumo minimo y maximo medio
	public final static double CONSUMO_MINIMO_MEDIO = 108;
	public final static double CONSUMO_MAXIMO_MEDIO = 162;
	// Presiones para la casilla general
	public final static double PRESION_MAXIMA = 150;
	public final static double PRESION_MINIMA = 110;

	/**
	 * Constructor de la clase Damero que inicializa los datos de los medidores y
	 * las medias de los contadores
	 * 
	 * @param columnas
	 * @param filas
	 */
	public Damero(int columnas, int filas) {
		Contador.reiniciarId();
		this.roturasContadorTroncal.clear();
		this.roturasContadorLineasD.clear();
		this.filas = filas;
		this.columnas = columnas;
		if (filas % 2 == 0) {
			pEdificios = new ParEdificios[columnas / 2][filas]; // i vale la mitad porque unimos dos columnas
			matrizMedias = new ParEdificios[columnas / 2][filas]; // Creamos tambien la matriz de medias de consumo
		} else {
			pEdificios = new ParEdificios[(columnas + 1) / 2][filas];
			matrizMedias = new ParEdificios[(columnas + 1) / 2][filas];
		}

		for (int a = 0; a < pEdificios.length; a++) {
			for (int b = 0; b < pEdificios[a].length; b++) {
				pEdificios[a][b] = new ParEdificios();
				matrizMedias[a][b] = new ParEdificios();
			}
		}
		this.inicializarContadores();
		this.inicializarManometros();
		this.inicializarMedias();
	}

	/**
	 * 
	 * @return un array con la linea troncal
	 */
	public ParEdificios[] lineaTroncal() {
		ParEdificios[] pE = new ParEdificios[pEdificios.length];
		for (int i = 0; i < pE.length; i++) pE[i] = pEdificios[i][pEdificios[0].length - 1];
		return pE;
	}

	/**
	 * 
	 * @return un array con las medias de la linea troncal
	 */
	public ParEdificios[] lineaTroncalMedia() {
		ParEdificios[] pE = new ParEdificios[pEdificios.length];
		for (int i = 0; i < pE.length; i++) pE[i] = matrizMedias[i][pEdificios[0].length - 1];
		return pE;
	}

	/**
	 * 
	 * @param i
	 * @return un array con la linea de distribucion i
	 */
	public ParEdificios[] lineasDistribucion(int i) {
		ParEdificios[] pE = new ParEdificios[this.pEdificios[0].length - 1]; // Le quitamos la linea troncal
		for (int j = 0; j < this.pEdificios[i].length - 1; j++) pE[j] = this.pEdificios[i][j];
		return pE;
	}

	/**
	 * 
	 * @param i
	 * @return un array con las medias de los contadores de la linea de distribucion
	 *         i
	 */
	public ParEdificios[] lineasDistribucionMedias(int i) {
		ParEdificios[] pE = new ParEdificios[this.matrizMedias[0].length - 1]; // Le quitamos la linea troncal
		for (int j = 0; j < this.matrizMedias[i].length - 1; j++) pE[j] = this.matrizMedias[i][j];
		return pE;
	}

	// Getter y Setters
	/**
	 * 
	 * @return la matriz con los datos de los contadores y los medidores
	 */
	public ParEdificios[][] getDamero() {
		return this.pEdificios;
	}

	/**
	 * 
	 * @return la matriz de medias
	 */
	public ParEdificios[][] getMedias() {
		return this.matrizMedias;
	}

	/**
	 * 
	 * @return roturasContadorTroncal
	 */
	public ArrayList<Object> getRoturasContadorTroncal() {
		return roturasContadorTroncal;
	}

	/**
	 * 
	 * @return roturasContadorLineasD
	 */
	public ArrayList<Object> getRoturasContadorLineasD() {
		return roturasContadorLineasD;
	}

	// CONTADORES
	/**
	 * LLama al metodo Par o Impar segun el caso
	 */
	public void inicializarContadores() {
		if (columnas % 2 == 0) inicializarContadoresPar();
		else inicializarContadoresImpar();
	}

	/**
	 * Inicializa los datos de los contadores en el caso de que la ciudad tenga
	 * columnas pares
	 */
	private void inicializarContadoresPar() { // Deberiamos inicializar primero los de cada edificio y a partir de ese

		// RECORREMOS EL ARRAY INICIALIZANDO LOS CONTADORES ROJOS
		for (int i = 0; i < pEdificios.length; i++) {
			for (int j = 0; j < pEdificios[0].length; j++) {
				if (i != pEdificios.length - 1 || j != pEdificios[0].length - 1) // CASILLA GENERAL
					this.pEdificios[i][j].setcDerecha(new Contador(Math.random() * (100 - 1000 + 1) + 1000));
				this.pEdificios[i][j].setcIzquierda(new Contador(Math.random() * (100 - 1000 + 1) + 1000));
			}
		}

		// INICIALIZAMOS LO CONTADORES VERDES Y MORADOS Y EL GENERAL
		double con = 0;
		for (int i = 0; i < pEdificios.length; i++) {
			for (int j = 1; j < pEdificios[0].length; j++) {

				if (i == pEdificios.length - 1 && j == pEdificios[0].length - 1) {
					con += pEdificios[i][j].getcIzquierda().getConsumo();
					con += pEdificios[i][j - 1].getcVerde().getConsumo();
					con += pEdificios[i - 1][j].getcMorado().getConsumo();
					this.pEdificios[i][j].setcDerecha(new Contador(con)); // CONTADOR GENERAL
					continue;
				}
				if (j == pEdificios[0].length - 1 && i != pEdificios.length - 1) { // linea de distribucion
					con += pEdificios[i][j - 1].getcVerde().getConsumo();
					con += pEdificios[i][j].getcDerecha().getConsumo();
					con += pEdificios[i][j].getcIzquierda().getConsumo();
					if (i != 0)
						con += pEdificios[i - 1][j].getcMorado().getConsumo();
					pEdificios[i][j].setcMorado(new Contador(con));
				} else if (pEdificios[i][j - 1].getcVerde() == null) { // final de la linea de distribucion por abajo
					con += pEdificios[i][j].getcDerecha().getConsumo();
					con += pEdificios[i][j].getcIzquierda().getConsumo();
					con += pEdificios[i][j - 1].getcDerecha().getConsumo();
					con += pEdificios[i][j - 1].getcIzquierda().getConsumo();
					pEdificios[i][j].setcVerde(new Contador(con));
				} else { // caso base
					con += pEdificios[i][j].getcDerecha().getConsumo();
					con += pEdificios[i][j].getcIzquierda().getConsumo();
					con += pEdificios[i][j - 1].getcVerde().getConsumo();
					pEdificios[i][j].setcVerde(new Contador(con));
				}
				con = 0;
			}
		}
	}

	/**
	 * Inicializa los datos de los contadores en el caso de que la ciudad tenga
	 * columnas impares
	 */
	private void inicializarContadoresImpar() {
		inicializarContadoresPar();
		for (int j = 0; j < pEdificios[0].length; j++) {
			this.pEdificios[0][j].setcIzquierda(null);
		}
	}

	// MANOMETROS
	/**
	 * Inicializa los datos de los manometros de la ciudad
	 */
	private void inicializarManometros() {
		int ancho = pEdificios.length - 1;
		int alto = pEdificios[0].length - 1;
		double pAnterior, error;
		for (int j = pEdificios[0].length - 1; j >= 1; j--) {
			for (int i = pEdificios.length - 1; i >= 0; i--) {
				if (i == ancho && j == alto) { // Manómetro general
					this.pEdificios[i][j].setMan(new Manometro(Math.random() * (110 - 150 + 1) + 150));
					continue;
				}
				// Obtenemos el valor de la presion del manometro anterior para obtener el
				// siguiente a partir de él
				if (j == alto) pAnterior = this.pEdificios[i + 1][j].getMan().getPresion();
				else pAnterior = this.pEdificios[i][j + 1].getMan().getPresion();

				error = pAnterior - (pAnterior * 13 / 100); // Margen de error del manometro
				this.pEdificios[i][j].setMan(new Manometro((Math.random() * (error - pAnterior + 1) + pAnterior)));
			}
		}
	}

	// MATRIZ DE MEDIAS
	/**
	 * LLama al metodo de inicializar la matriz de medias par o impar segun las
	 * columnas que tenga la ciudad
	 */
	public void inicializarMedias() {
		if (columnas % 2 == 0) inicializarMediasPar();
		else inicializarMediasImpar();
	}

	/**
	 * Inicializa la matriz de medias para las ciudades con columnas pares
	 */
	private void inicializarMediasPar() { // Deberiamos inicializar primero los de cada edificio y a partir de ese

		// RECORREMOS EL ARRAY INICIALIZANDO LOS CONTADORES ROJOS
		for (int i = 0; i < matrizMedias.length; i++) {
			for (int j = 0; j < matrizMedias[0].length; j++) {
				if (i != matrizMedias.length - 1 || j != matrizMedias[0].length - 1) // CASILLA GENERAL
					this.matrizMedias[i][j].setcDerecha(new Contador(Math.random() * (CONSUMO_MINIMO_MEDIO - CONSUMO_MAXIMO_MEDIO + 1) + CONSUMO_MAXIMO_MEDIO));
				this.matrizMedias[i][j].setcIzquierda(new Contador(Math.random() * (CONSUMO_MINIMO_MEDIO - CONSUMO_MAXIMO_MEDIO + 1) + CONSUMO_MAXIMO_MEDIO));
			}
		}

		// INICIALIZAMOS LO CONTADORES VERDES Y MORADOS Y EL GENERAL
		double con = 0;
		for (int i = 0; i < matrizMedias.length; i++) {
			for (int j = 1; j < matrizMedias[0].length; j++) {

				if (i == matrizMedias.length - 1 && j == matrizMedias[0].length - 1) {
					con += matrizMedias[i][j].getcIzquierda().getConsumo();
					con += matrizMedias[i][j - 1].getcVerde().getConsumo();
					con += matrizMedias[i - 1][j].getcMorado().getConsumo();
					this.matrizMedias[i][j].setcDerecha(new Contador(con)); // CONTADOR GENERAL
					continue;
				}
				if (j == matrizMedias[0].length - 1 && i != matrizMedias.length - 1) { // linea de distribucion
					con += matrizMedias[i][j - 1].getcVerde().getConsumo();
					con += matrizMedias[i][j].getcDerecha().getConsumo();
					con += matrizMedias[i][j].getcIzquierda().getConsumo();
					if (i != 0)
						con += matrizMedias[i - 1][j].getcMorado().getConsumo();
					matrizMedias[i][j].setcMorado(new Contador(con));
				} else if (matrizMedias[i][j - 1].getcVerde() == null) { // final de la linea de distribucion por abajo
					con += matrizMedias[i][j].getcDerecha().getConsumo();
					con += matrizMedias[i][j].getcIzquierda().getConsumo();
					con += matrizMedias[i][j - 1].getcDerecha().getConsumo();
					con += matrizMedias[i][j - 1].getcIzquierda().getConsumo();
					matrizMedias[i][j].setcVerde(new Contador(con));
				} else { // caso base
					con += matrizMedias[i][j].getcDerecha().getConsumo();
					con += matrizMedias[i][j].getcIzquierda().getConsumo();
					con += matrizMedias[i][j - 1].getcVerde().getConsumo();
					matrizMedias[i][j].setcVerde(new Contador(con));
				}
				con = 0;
			}
		}
	}

	/**
	 * Inicializa la matriz de medias para las ciudades con columnas impares
	 */
	private void inicializarMediasImpar() {
		inicializarMediasPar();
		for (int j = 0; j < matrizMedias[0].length; j++)
			this.matrizMedias[0][j].setcIzquierda(null);
	}

	// ToString
	/**
	 * @return un string con los datos de los medidores de la estructura pEdificios
	 */
	public String toString() {
		String resultado = "";
		for (int j = 0; j < pEdificios[0].length; j++) {
			for (int i = 0; i < pEdificios.length; i++)
				resultado += pEdificios[i][j].toString() + "       " + i + " " + j + "\t";
			resultado += "\n";
		}
		return resultado;
	}

	/**
	 * 
	 * @return un string que contiene los datos de todos los manometros de la ciudad
	 */
	public String toStringM() {
		String resultado = "";
		for (int i = 0; i < pEdificios.length; i++) {
			for (int j = 0; j < pEdificios[i].length; j++)
				resultado += pEdificios[i][j].toStringManometros() + "       " + i + " " + j + "\n";
			resultado += "\n";
		}
		return resultado;
	}

	/**
	 * 
	 * @return un string con todos los contadores de la ciudad
	 */
	public String toStringC() {
		String resultado = "";
		for (int i = 0; i < pEdificios.length; i++) {
			for (int j = 0; j < pEdificios[i].length; j++)
				resultado += pEdificios[i][j].toStringContadores() + "       " + i + " " + j + "\n";
			resultado += "\n";
		}
		return resultado;
	}

	/**
	 * 
	 * @return un string con las medias de los contadores
	 */
	public String toStringMmedias() {
		String resultado = "";
		for (int i = 0; i < matrizMedias.length; i++)
			for (int j = 0; j < this.matrizMedias[0].length; j++)
				resultado += this.matrizMedias[i][j].toStringContadores() + "\t" + i + " " + j + "\n";
		return resultado;
	}

	/**
	 * 
	 * @param i
	 * @param j
	 * @param pEdificios
	 * @param cont
	 * @return el consumo de un determinado edificio
	 */
	public double getLitrosEdificio(int i, int j, ParEdificios[][] pEdificios, String cont) { // columnas, filas
		if (cont.equals("D")) return pEdificios[i][j].getcDerecha().getConsumo();
		else if (cont.equals("I")) return pEdificios[i][j].getcIzquierda().getConsumo();
		else if (cont.equals("V")) return pEdificios[i][j].getcVerde().getConsumo();
		else return pEdificios[i][j].getcMorado().getConsumo();
	}

	// REVISAR
	// EL VALOR DEBERIA COINCIDIR CON LA CASILLA GENERAL PERO NO COINCIDE
	/**
	 * 
	 * @return el consumo de toda la ciudad sin tener en cuenta la casilla general
	 */
	public double getLitrosCasillasSalvoCasillaGeneral() {
		double resultado = 0;

		for (int i = 0; i < this.pEdificios.length; i++) {
			for (int j = 0; j < this.pEdificios[0].length; j++) {
				if (i == (this.pEdificios.length - 1) && j == this.pEdificios[0].length - 1)
					resultado += this.pEdificios[i][j].getcIzquierda().getConsumo();
				else if (columnas % 2 != 0 && i == 0) resultado += this.pEdificios[i][j].getcDerecha().getConsumo();
				else resultado += this.pEdificios[i][j].getcDerecha().getConsumo() + this.pEdificios[i][j].getcIzquierda().getConsumo();
			}
		}
		return resultado;
	}

	//Greedy
	
	public ArrayList<Object> consumoExcesivoContadores() {
		/*En este ejemplo tenemos el problema de que estamos simulando una solución aleatoria en la que no sabemos donde se 
		 * produciran las roturas, ni siquiera sabemos si habrá alguna. Por lo tanto, tenemos que definir los elementos del 
		 * problema de la siguiente forma: 
		 * - Conjunto de candidatos: todos los contadores del tablero
		 * - Solucion parcial: seran todos aquellos contadores que presenten una rotura
		 * - Funcion de seleccion: elegimos a aquellos contadores que presenten una rotura
		 * - Funcion de factibilidad: en realidad, creo que no nos haria falta, pero por seguir el esquema del Greedy podemos hacer 
		 * que nos verifique que al añadir el contador, el conjunto de todos los contadores de la solucion nuestra presentan roturas 
		 * - Funcion de solucion: como tenemos una situacion aleatoria, no sabemos realmente cual debe ser la solucion, por lo que esto
		 * no podemos incluirlo.*/
		
		//Conjunto de candidatos -> ArrayList porq la busqueda es O(1), en vez de O(n²) en nuestra matriz
		ArrayList<Contador> candidatos = this.obtenerCandidatos();
		
		
	}
	
	/**
	 * @return una lista con todos los candidatos a ser estudiados. En nuestro caso, todos los contadores de nuestro damero
	 */
	public ArrayList<Contador> obtenerCandidatos(){
		ArrayList<Contador> candidatos = new ArrayList<>();
		
		for(int i=0; i<this.pEdificios.length; i++) {
			for(int j=0; j<this.pEdificios[i].length; j++) {
				candidatos.add(this.pEdificios[i][j].getcDerecha());
				candidatos.add(this.pEdificios[i][j].getcIzquierda());
				candidatos.add(this.pEdificios[i][j].getcMorado());
				candidatos.add(this.pEdificios[i][j].getcVerde());
			}
		}
		
		return candidatos;
	}
	
	/**
	 * @return una lista con todos los contadores que presentan roturas
	 */
	public ArrayList<Contador> contadoresConRoturas(){
		ArrayList<Contador> s = new ArrayList<>();
			
		for(int i=0; i<this.pEdificios.length; i++) {
			for(int j=0; j<this.pEdificios[i].length; j++) {
				if(this.pEdificios[i][j].getcDerecha().getConsumo() > 5*this.matrizMedias[i][j].getcDerecha().getConsumo()) {
					s.add(this.pEdificios[i][j].getcDerecha());
				}
				if(this.pEdificios[i][j].getcIzquierda().getConsumo() > 5*this.matrizMedias[i][j].getcIzquierda().getConsumo()) {
					s.add(this.pEdificios[i][j].getcIzquierda());
				}
				if(this.pEdificios[i][j].getcMorado().getConsumo() > 5*this.matrizMedias[i][j].getcMorado().getConsumo()) {
					s.add(this.pEdificios[i][j].getcMorado());
				}
				if(this.pEdificios[i][j].getcVerde().getConsumo() > 5*this.matrizMedias[i][j].getcVerde().getConsumo()) {
					s.add(this.pEdificios[i][j].getcVerde());
				}
			}
		}
		return s;
	}
	
	//La funcion de seleccion determina el mejor candidato del conjunto de candidatos seleccionables
	
	public Contador funcionSeleccion(ArrayList<Contador> candidatos) {
		//¿Cual es el mejor candidato?
	}
	
	
	
	
	
}