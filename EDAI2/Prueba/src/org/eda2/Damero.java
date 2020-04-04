package org.eda2;

import java.util.ArrayList;

/**
 * @author marta La representación de nuestro tablero viene dada por aristas
 */
public class Damero {
	private int filas;
	private int columnas;
	private ParEdificios[][] pEdificios;
	private ParEdificios[][] matrizMedias;
	public final static double CONSUMO_MINIMO = 300000; // m3 Son medidas que se dan en el enunciado
	public final static double CONSUMO_MAXIMO = 500000; // m3

	/**
	 * Nuestro Damero viene representado por pares de calles.
	 * 
	 * @param filas    el número de avenidas de nuestra ciudad
	 * @param columnas el número de calles de nuestra ciudad
	 */
	public Damero(int filas, int columnas, double cauceInicial) {
		this.filas = filas;
		this.columnas = columnas;
		if (filas % 2 == 0) {
			pEdificios = new ParEdificios[columnas / 2][filas]; // i vale la mitad porque unimos dos columnas
			matrizMedias = new ParEdificios[columnas / 2][filas];
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

		this.inicializarContadores(this.pEdificios);
		setSuministroAgua(cauceInicial, this.pEdificios);
		this.inicializarContadores(this.matrizMedias);
		this.setSuministroAgua(400000, this.matrizMedias); // Datos dados por la empresa
	}

	/**
	 * @return la línea troncal de nuestro damero
	 */
	public ParEdificios[] lineaTroncal() {
		ParEdificios[] pE = new ParEdificios[pEdificios.length]; // Le quitamos la casilla general
		for (int i = 0; i < pE.length; i++) {
			pE[i] = pEdificios[i][pEdificios[0].length - 1];
		}
		return pE;
	}

	public ParEdificios[] lineaTroncalMedia() {
		ParEdificios[] pE = new ParEdificios[pEdificios.length];
		for (int i = 0; i < pE.length; i++) {
			pE[i] = matrizMedias[i][pEdificios[0].length - 1];
		}
		return pE;
	}

	public ParEdificios[][] getDamero() {
		ParEdificios[][] resultado = new ParEdificios[this.pEdificios.length][this.pEdificios[0].length];

		for (int i = 0; i < resultado.length; i++) {
			for (int j = 0; j < resultado[i].length; j++) {
				resultado[i][j] = new ParEdificios();
				resultado[i][j] = this.pEdificios[i][j];
			}
		}
		return resultado;
	}

	// CONTADORES
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

				if (j == this.pEdificios[0].length - 1) {
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

	/**
	 * Terminamos de rellenar la última columna
	 */
	private void inicializarContadoresPar(ParEdificios[][] pEdificios) {
		int columnas = this.pEdificios.length - 1;
		for (int i = 0; i < this.pEdificios[0].length; i++) {
			pEdificios[columnas][i] = new ParEdificios();
			pEdificios[columnas][i].setcDerecha(new Contador(0.0));
			pEdificios[columnas][i].setcIzquierda(new Contador(0.0));
			if (i == this.pEdificios[0].length - 1)
				pEdificios[columnas][i].setcMorado(new Contador(0.0));
			if (i != this.pEdificios[0].length - 1)
				pEdificios[columnas][i].setcVerde(new Contador(0.0));
		}
	}

	/**
	 * La ultima columna la ponemos a null
	 */
	private void inicializarContadoresImpar(ParEdificios[][] pEdificios) {
		int columnas = this.pEdificios.length - 1;
		for (int i = 0; i < this.pEdificios[0].length; i++) {
			this.pEdificios[columnas][i] = new ParEdificios();
			this.pEdificios[columnas][i].setcIzquierda(new Contador(0.0));
			this.pEdificios[columnas][i].setcDerecha(null);
			if (i == this.pEdificios[0].length - 1)
				this.pEdificios[columnas][i].setcMorado(new Contador(0.0));
			if (i != this.pEdificios[0].length - 1)
				this.pEdificios[columnas][i].setcVerde(new Contador(0.0));

		}
	}

	public String toString() {
		String resultado = "";

		for (int j = 0; j < pEdificios[0].length; j++) {
			for (int i = 0; i < pEdificios.length; i++) {
				resultado += pEdificios[i][j].toString() + "(" + i + " ," + j + ")" + "\t" + " --> ";
			}
			resultado += "\n";
		}
		return resultado;
	}

	public String toStringMedias() {
		String resultado = "";

		for (int j = 0; j < this.matrizMedias[0].length; j++) {
			for (int i = 0; i < this.matrizMedias.length; i++) {
				resultado += this.matrizMedias[i][j].toString() + "(" + i + " ," + j + ")" + "\t" + " --> ";
			}
			resultado += "\n";
		}
		return resultado;
	}

	/**
	 * El usuario desconoce la estructura ParEdificios y va a insertar la dirección
	 * de una manzana con respecto al damero total
	 * 
	 * @param i filas a hallar
	 * @return un ArrayList en cuya primera posicion insertamos el indice y en la
	 *         segunda si el resultado de si el indice que nos pasaron era par. Si
	 *         es par accederemos a la casilla de la izquierda; si es impar a la de
	 *         la derecha
	 */
	private ArrayList<Object> traducirIndices(int i) {
		double division = i / 2;
		Boolean esPar = (division % 2 == 0) ? true : false;
		double nuevaFila = Math.floor(division);
		ArrayList<Object> solucion = new ArrayList<>();

		solucion.add(nuevaFila);
		solucion.add(esPar);

		return solucion;
	}

	/**
	 * @param cauce cantidad de agua que el usuario quiere introducir
	 */
	private void setSuministroAgua(double cauce, ParEdificios[][] pEdificios) {
		if (cauce > CONSUMO_MAXIMO)
			throw new RuntimeException("Ha introducido demasiada agua y se han roto las tuberías.");
		if (cauce < CONSUMO_MINIMO)
			throw new RuntimeException("El sistema no puede funcionar con tan poco cauce.");

		setLitrosEdificio(cauce, pEdificios);
	}

	/**
	 * En este método la empresa decide la cantidad de agua que quiere suministrar a
	 * través del túnel. De esta forma, entre todas las manzanas de la ciudad, no
	 * podrán consumir más que la cantidad de agua que se le ha sido proporcionada.
	 * Para ser justos, vamos a darle como máximo a cada una exactamente
	 * cauce/{(this.columnas/2*this.filas)-1}, es decir, lo mismo a todas. (Quitamos
	 * la casilla general) Eso no quiere decir que cada una pueda consumir menos de
	 * lo que se le ha dado.
	 * 
	 * El contador mide la cantidad de agua que se consume. En la casilla general,
	 * el contador mide el agua que hay en esa casilla, es decir, lo que la empresa
	 * genera. En cada casilla se mide lo que cada manzana consume de la parte que
	 * le corresponde.
	 * 
	 * 1. Generamos un número aleatorio entre [0,0.5] 2. Le quitamos a la parte que
	 * le corresponde a la manzana i,j esa cantidad aleatoria. De esta forma
	 * obtenemos datos más reales
	 * 
	 * @param cauce cantidad de agua que se desea suministrar a través de la casilla
	 *              general
	 */
	private void setLitrosEdificio(double cauce, ParEdificios[][] pEdificios) { // ***
		int numTotalManzanas = (this.filas * this.columnas) - 1; // quitamos la general
		double porcionIndividual = cauce / numTotalManzanas;
		double porcentajeAVariar; // Esto es para que no todas las manzanas consuman exactamente lo mismo.
		double cantidadVerde;

		for (int i = 0; i < this.pEdificios.length; i++) { //ESTA MAL, LA CASILLA GENERAL NO SE QUEDA CON EL CAUCE QUE METEMOS
			for (int j = 0; j < this.pEdificios[i].length; j++) {
				if (i == this.pEdificios.length - 1 && j == this.pEdificios[i].length - 1) {
					if (this.filas % 2 == 0) { // Si estamos en la ultima columna
						pEdificios[i][j].setcDerecha(new Contador(cauce)); // CASILLA GENERAL SITUACIÓN PAR
						porcentajeAVariar = (Math.random() * (0 - 0.5 + 1) + 0.5);
						pEdificios[i][j].setcIzquierda(new Contador(porcionIndividual - (porcionIndividual * porcentajeAVariar)));
					} else { // Aqui la derecha hay que ponerla a null
						pEdificios[i][j].setcIzquierda(new Contador(cauce)); // CASILLA GENERAL SITUACIÓN IMPAR
						porcentajeAVariar = (Math.random() * (0 - 0.5 + 1) + 0.5);
						pEdificios[i][j].setcDerecha(new Contador(porcentajeAVariar));
					}
				} else {
					porcentajeAVariar = (Math.random() * (0 - 0.5 + 1) + 0.5);
					pEdificios[i][j].setcDerecha(new Contador(porcionIndividual - (porcionIndividual * porcentajeAVariar)));
					porcentajeAVariar = (Math.random() * (0 - 0.5 + 1) + 0.5);
					pEdificios[i][j].setcIzquierda(new Contador(porcionIndividual - (porcionIndividual * porcentajeAVariar)));
					if (pEdificios[i][j].getcVerde() != null) { //Cuando haya un contador verde metele la suma del izquierda y derecha
						cantidadVerde = pEdificios[i][j].getcDerecha().getConsumo() + pEdificios[i][j].getcIzquierda().getConsumo();
						pEdificios[i][j].setcVerde(new Contador(cantidadVerde)); 
					}

				}
			}
		}
	}

	/**
	 * Dada una casilla, saber cuánto ha consumido
	 * 
	 * @param i
	 * @param j
	 * @return lo que ha connsumido la manzana i,j
	 */
	public double getLitrosEdificio(int i, int j) { // columnas, filas
		i--;
		j--;
		double filas = (double) this.traducirIndices(i).get(0);
		boolean par = (boolean) this.traducirIndices(i).get(1);

		if (filas > this.filas / 2 || i > this.columnas) {
			throw new IndexOutOfBoundsException(
					"Debe introducir un edificio válido. Compruebe que los índices son correctos.");
		}
		
		if((int)filas == this.pEdificios.length -1 && j == this.pEdificios[0].length -1 && this.pEdificios[0].length % 2 != 0) {
			return this.pEdificios[(int)filas][j].getcIzquierda().getConsumo();
		}
		
		return (par == true) ? this.pEdificios[(int) filas][j].getcIzquierda().getConsumo()
				: this.pEdificios[(int) filas][j].getcDerecha().getConsumo();

	}

	/**
	 * @return la suma de los litros de todas las casillas sin tener en cuenta la
	 *         capacidad de la casilla general generadora
	 */
	public double getLitrosCasillasSalvoCasillaGeneral() {
		double resultado = 0;

		for (int i = 0; i < this.columnas / 2; i++) {
			for (int j = 0; j < this.filas; j++) {
				if (i == (this.columnas / 2) - 1 && j == this.filas - 1)
					break; // La general no
				resultado += this.pEdificios[i][j].getcDerecha().getConsumo()
						+ this.pEdificios[i][j].getcIzquierda().getConsumo();
			}
		}
		return resultado;
	}

	public ArrayList<Contador> consumoExcesivoTroncal() {
		ArrayList<Contador> resultado = new ArrayList<>();
		Contador[] contadoresTroncal = this.traducirMatrizParEdificiosAArrayContadores(this.lineaTroncal());
		int i = 0;
		int j = contadoresTroncal.length - 1;

		resultado = this.consumoExcesivoRec(contadoresTroncal, i, j);

		return resultado;
	}

	/**
	 * @param troncal es un array con cada contador de cada casilla
	 * @param i       inicio del array
	 * @param j       final del array
	 * @return
	 */
	private ArrayList<Contador> consumoExcesivoRec(Contador[] troncal, int i, int j) {
		ArrayList<Contador> resultado = new ArrayList<>();
		Contador[] media = this.traducirMatrizParEdificiosAArrayContadores(this.lineaTroncalMedia());
		int mitad;
		Pair<Integer, Integer> coordenadas;
		int fila;
		int columna;

		// Solo lo estoy haciendo de cIzquierda y cDerecha porq nos falta darle valores
		// coherentes a los cMorados y cVerdes

		if (i >= j - 1) { // Caso base -> Si solo nos quedan dos elementos

		} else { // Casos recursivos
			mitad = (i + j) / 2;
			this.consumoExcesivoRec(troncal, i, mitad - 1);
			this.consumoExcesivoRec(troncal, mitad, j);
		}

		return resultado;

	}

	/**
	 * Para resolver este problema habíamos juntado todo en una estructura que
	 * habíamos llamado parEdificios. Para resolver el problema con el algoritmo
	 * divide y vencerás, considero que es más sencillo tener a cada manzana en su
	 * casilla.
	 * 
	 * @param pE
	 * @return cualquier estructura ParEdificios en una Matriz de contadores
	 */
	private Contador[] traducirMatrizParEdificiosAArrayContadores(ParEdificios[] pE) {
		Contador[] contadores = new Contador[(pE.length * 2) - 1]; // Quitamos la casilla general
		int contador = 0;

		for (int i = 0; i < pE.length; i++) {
			contadores[contador] = pE[i].getcIzquierda();
			contador++;
			if (i != pE.length - 1) {
				contadores[contador] = pE[i].getcDerecha();
				contador++;
			}
		}
		return contadores;
	}

	private Pair<Integer, Integer> coordenadasDelContadorPorId(int id) {
		Pair<Integer, Integer> coordenadas;
		for (int i = 0; i < this.pEdificios.length; i++) {
			for (int j = 0; j < this.pEdificios[i].length; j++) {
				if (this.pEdificios[i][j].getcIzquierda().getId() == id
						|| this.pEdificios[i][j].getcDerecha().getId() == id) {
					coordenadas = new Pair<Integer, Integer>(i, j);
					break;
				}
			}
		}
		return coordenadas;

	}

	// NOTA -> PRIMERO TENEMOS QUE GENERAR LA CASILLA GENERAL (QUE TIENE QUE ESTAR
	// ENTRE EL MAX Y EL MINIMO)
	// Y LUEGO A PARTIR DE ESO IR DISTRIBUYENDO, PERO QUE LA SUMA NUNCA SEA MAYOR
	// QUE EL MAXIMO
	// ES DECIR, GENERAMOS UNA CANTIDAD FIJA DE AGUA

	// MANOMETROS

}
