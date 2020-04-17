package org.eda2.dyv;

import java.util.ArrayList;

public class Damero {

	private int filas;
	private int columnas;
	private ParEdificios[][] pEdificios;
	private ParEdificios[][] matrizMedias;
	private ArrayList<Object> roturasContadorTroncal = new ArrayList<>();
	private ArrayList<Object> roturasContadorLineasD = new ArrayList<>();
	private ArrayList<Object> roturasContadorProblemaRecursivo = new ArrayList<>();
	private ArrayList<Manometro> roturaManometroProblemaRecursivo = new ArrayList<>();
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

	public String toString() {
		String resultado = "";
		for(int j=0; j<pEdificios[0].length; j++) {
			for(int i=0; i<pEdificios.length; i++) {
				resultado +=  pEdificios[i][j].toString() + "(" + i+" ,"+j + ")" + "\t";
			}
			resultado += "\n";			
		}
		return resultado;
	}

	public String toStringMedias() {
		String resultado = "";
		for (int i = 0; i < this.columnas / 2; i++) {
			for (int j = 0; j < this.filas; j++) {
				resultado += this.matrizMedias[i][j] + "\t\t";
			}
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

	// DyV
	/**
	 * @return un ArrayList de Object en el que guardaremos el contador que ha
	 *         provocado una rotura de segundo grado en la linea troncal. (ID,
	 *         Contador)
	 */
	public ArrayList<Object> consumoExcesivoTroncal() {
		ArrayList<Object> resultado = new ArrayList<>();
		int i = 0;
		int j = this.lineaTroncal().length - 1;

		roturasContadorProblemaRecursivo.clear();
		resultado = this.consumoExcesivoRec(this.lineaTroncal(), this.lineaTroncalMedia(), i, j);

		if (!resultado.isEmpty()) this.roturasContadorTroncal.addAll(resultado);

		return resultado;
	}

	/**
	 * @return un ArrayList de Object en el que guardaremos el contador que ha
	 *         provocado una rotura de segundo grado en las lineas de distribucion.
	 *         (ID, Contador)
	 */
	public ArrayList<Object> consumoExcesivoLineasDistribucion() {
		ArrayList<Object> resultado = new ArrayList<>();
		int tamMax = this.pEdificios.length;

		roturasContadorProblemaRecursivo.clear();

		for (int i = 0; i < tamMax; i++) { // Para cada columna llamamos al metodo consumoExcesivoRec
			resultado = this.consumoExcesivoRec(lineasDistribucion(i), lineasDistribucionMedias(i), 0, lineasDistribucion(i).length - 1);
			if (!resultado.isEmpty()) roturasContadorLineasD.addAll(resultado);
		}
		return roturasContadorLineasD;
	}

	/**
	 * Nuestro algoritmo recursivo va a dividir el problema hasta el máximo. Es
	 * decir, vamos a coger nuestro array de parEdificios y vamos a simplificarlo
	 * hasta quedarnos con un solo par. Sobre este par podemos ver si se dan las
	 * condiciones necesarias para que se produzca una rotura
	 * 
	 * @param pE es la estructura a la que vamos a buscarle roturas de contadores
	 * @param i  posicion inicial a partir de la cual vamos a empzar a buscar
	 * @param j  posicion final de la búsqueda
	 * @return un ArrayList con todos los contadores que han presentado una rotura
	 */
	private ArrayList<Object> consumoExcesivoRec(ParEdificios[] pE, ParEdificios[] media, int i, int j) {
		int mitad;
		// Le voy a añadir un ID al Contador porque luego para buscarlo y decir en que
		// casilla se encuentra creo que es lo mas
		// rapido para buscarlo en funcion de esto

		// Lo que devuelve el ArrayList<Object>:
		// 1º -> Su ID (Como despues tenemos que hacer un estudio algoritmico de este
		// metodo solo le metemos el ID para que no
		// sea muy costoso. Luego hacemos un metodo que en función del ID nos diga donde
		// se ubica el contador
		// 2º -> El contador que ha provocado una rotura

		if (i == j) { // Caso base
			if (pE[i].getcDerecha() != null
					&& pE[i].getcDerecha().getConsumo() > 7 * media[i].getcDerecha().getConsumo()) {
				roturasContadorProblemaRecursivo.add(pE[i].getcDerecha().getId());
				roturasContadorProblemaRecursivo.add(pE[i].getcDerecha());
			}
			if (pE[i].getcIzquierda() != null
					&& pE[i].getcIzquierda().getConsumo() > 7 * media[i].getcIzquierda().getConsumo()) {
				roturasContadorProblemaRecursivo.add(pE[i].getcIzquierda().getId());
				roturasContadorProblemaRecursivo.add(pE[i].getcIzquierda());
			}
			if (pE[i].getcMorado() != null
					&& pE[i].getcMorado().getConsumo() > 7 * media[i].getcMorado().getConsumo()) {
				roturasContadorProblemaRecursivo.add(pE[i].getcMorado().getId());
				roturasContadorProblemaRecursivo.add(pE[i].getcMorado());
			}
			if (pE[i].getcVerde() != null && pE[i].getcVerde().getConsumo() > 7 * media[i].getcVerde().getConsumo()) {
				roturasContadorProblemaRecursivo.add(pE[i].getcVerde().getId());
				roturasContadorProblemaRecursivo.add(pE[i].getcVerde());
			}
		} else { // Casos recursivos
			mitad = (i + (j + 1)) / 2;
			this.consumoExcesivoRec(pE, media, i, mitad - 1);
			this.consumoExcesivoRec(pE, media, mitad, j);
		}
		return roturasContadorProblemaRecursivo;
	}

	/**
	 * @return una cadena con las casillas en las que se ha producido una rotura
	 */
	public String interpretarSolucionConsumoExcesivo(ArrayList<Object> consumo) {
		String cadena = "";
		int contador = 0;

		// Obtenemos el ID del contador y lo buscamos en nuestra matriz
		if (!consumo.isEmpty()) {
			for (Object o : consumo) {
				if (contador % 2 == 0) { // Solo nos interesa las posiciones pares
					Integer id = (Integer) o;
					for (int i = 0; i < this.pEdificios.length; i++) {
						for (int j = 0; j < this.pEdificios[i].length; j++) {
							if (pEdificios[i][j].containsID(id)) {
								String tipo = pEdificios[i][j].getTipo(id);
								if (tipo.equals("D"))
									cadena += "* Casilla: [" + i + ", " + j + "]. El contador derecho ha provocado la rotura\n";
								else if (tipo.equals("I"))
									cadena += "* Casilla: [" + i + ", " + j + "]. El contador izquierdo ha provocado la rotura\n";
								else if (tipo.equals("M"))
									cadena += "* Casilla: [" + i + ", " + j + "]. El contador morado ha provocado la rotura\n";
								else if (tipo.equals("V"))
									cadena += "* Casilla: [" + i + ", " + j + "]. El contador verde ha provocado la rotura\n";
							}
						}
					}
				}
				contador++;
			}
		}
		return (cadena.isEmpty()) ? "No hay roturas.\n " : cadena;
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
	


	
	/**
	 * @return
	 */
	public ArrayList<Manometro> perdidaExcesivaPresionTroncal(){
		ParEdificios[] pE = this.lineaTroncal();
		return this.perdidaExcesivaPresionRec(pE, 0, pE.length-1);
	}
	
	/**
	 * @return
	 */
	public ArrayList<Manometro> perdidaExcesivaPresionLineasDistribucion(){
		ArrayList<Manometro> resultado = new ArrayList<>();
		int tamMax = this.pEdificios.length;
		ParEdificios[] pE = new ParEdificios[this.pEdificios[0].length];
		roturaManometroProblemaRecursivo.clear();
		
		
		for(int i=0; i<tamMax; i++) {
			//Tenemos que quitarle la primera fila porque no hay manometros
			pE = this.elminarPrimeraFila(this.lineasDistribucion(i));
			resultado = this.perdidaExcesivaPresionRec(pE, 0, pE.length-1);
			if(!resultado.isEmpty()) roturaManometroProblemaRecursivo.addAll(resultado);
		}
		return resultado;
	}
	
	public ParEdificios[] elminarPrimeraFila(ParEdificios[] pE) {
		ParEdificios[] resultado = new ParEdificios[pE.length];
		for(int i=1; i<pE.length; i++) {
			resultado[i] = pE[i];
		}
		
		return resultado;
	}
		
	/**
	 * Es muy similar al anterior. Sin embargo, aqui necesitamos que el elemento de la mitad se mantenga en ambas llamadas. Y en el caso base
	 * necesitamos dos elementos en lugar de uno
	 * @return
	 */
	private ArrayList<Manometro> perdidaExcesivaPresionRec(ParEdificios[] pE, int i, int j){
		int mitad;
		if(j-i == 1) {
			if(pE[i].getMan().getPresion() < (pE[j].getMan().getPresion() - 0.1*pE[j].getMan().getPresion())) {
				this.roturaManometroProblemaRecursivo.add(pE[i].getMan());
			}
		} else {
			mitad = (i + j)/2;
			perdidaExcesivaPresionRec(pE, i, mitad);
			perdidaExcesivaPresionRec(pE, mitad, j);
		}
		return this.roturaManometroProblemaRecursivo;		
	}
	
}