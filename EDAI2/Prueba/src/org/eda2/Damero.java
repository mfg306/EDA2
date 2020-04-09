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
	//Estas presiones solo para la casilla general
	public final static double PRESION_MAXIMA = 150;
	public final static double PRESION_MINIMA = 110;
	private ArrayList<Object> roturasContadorTroncal = new ArrayList<>(); 
	private ArrayList<Object> roturasContadorLineasD = new ArrayList<>(); 


	/**
	 * Nuestro Damero viene representado por pares de calles.
	 * 
	 * @param filas    el número de avenidas de nuestra ciudad
	 * @param columnas el número de calles de nuestra ciudad
	 */
	public Damero(int filas, int columnas, double cauceInicial) {
		Contador.reiniciarId(); 
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
		this.setSuministroAgua(500000, this.matrizMedias); // Datos dados por la empresa
		this.inicializarManometros();
	}
	
	

	/**
	 * @return la línea troncal de nuestro damero --> última fila, quitando la casilla general
	 */
	public ParEdificios[] lineaTroncal() { //NO LO ESTÁ COGIENDO POR COLUMNAS
		ParEdificios[] pE = new ParEdificios[pEdificios.length-1]; // Le quitamos la casilla general
		for (int i = 0; i < pE.length; i++) {
			pE[i] = pEdificios[i][pEdificios[0].length - 1];
		}
		return pE;
	}

	/**
	 * @return la linea troncal de los datos de la media --> última fila, quitando la casilla general
	 */
	public ParEdificios[] lineaTroncalMedia() {
		ParEdificios[] pE = new ParEdificios[pEdificios.length-1];//Le quitamos la ultima fila
		for (int i = 0; i < pE.length; i++) {
			pE[i] = matrizMedias[i][pEdificios[0].length - 1];
		}
		return pE;
	}
	

	/**
	 * @param i la linea que queremos obtener (existen diversas lineas de distribucion)
	 * @return dicha linea de distribucion
	 */
	public ParEdificios[] lineasDistribucion(int i){
		ParEdificios[] pE = new ParEdificios[this.pEdificios[0].length-1]; //Le quitamos la ultima fila
		
		for(int j=0; j<this.pEdificios[i].length-1; j++) {
			pE[j] = this.pEdificios[i][j];
		}
		
		return pE;
	}
	
	/**
	 * @param i la linea que queremos obtener (existen diversas lineas de distribucion)
	 * @return dicha linea de distribucion
	 */
	public ParEdificios[] lineasDistribucionMedias(int i){
		ParEdificios[] pE = new ParEdificios[this.matrizMedias[0].length-1]; //Le quitamos la ultima fila
		
		for(int j=0; j<this.matrizMedias[i].length-1; j++) {
			pE[j] = this.matrizMedias[i][j];
		}
		
		return pE;
	}
	

	/**
	 * @return nuestro damero de la ciudad
	 */
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
	
	
	/**
	 * @return las medias de la ciudad
	 */
	public ParEdificios[][] getMedias() {
		ParEdificios[][] resultado = new ParEdificios[this.matrizMedias.length][this.matrizMedias[0].length];

		for (int i = 0; i < resultado.length; i++) {
			for (int j = 0; j < resultado[i].length; j++) {
				resultado[i][j] = new ParEdificios();
				resultado[i][j] = this.matrizMedias[i][j];
			}
		}
		return resultado;
	}
	
	public ArrayList<Object> getRoturasContadorTroncal() {
		return roturasContadorTroncal;
	}
	
	
	public ArrayList<Object> getRoturasContadorLineasD() {
		return roturasContadorLineasD;
	}
	
	// CONTADORES
	
	/**
	 * @param pEdificios la estructura que queremos inicializar. Así, nos sirve
	 *                   tanto para los datos actuales como para las medias
	 */
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

	/**
	 * Terminamos de rellenar la última columna
	 */
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

		// A la casilla general hay que meterle el cauce.
		// * Si es par, la casilla general sera la ultima derecha (La ultima derecha hay
		// que ponerla a null porque
		// realmente no hay nada)
		// * Si es impar, la casilla general sera la ultima izquierda

		// En el resto de casos, inicializamos cada casilla, si no está inicializada a
		// null. Es decir, si contadorVerde es null,
		// es porque antes hemos decidido que hay no tiene que haber uno, por lo que no
		// le metemos nada y seguimos. Lo mismo con el morado

		// Si estamos en un damero impar, entonces la ultima columna derecha tenemos que
		// dejarla a null

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

	/**
	 * Dada una casilla, saber cuánto ha consumido
	 * 
	 * @param i
	 * @param j
	 * @return lo que ha connsumido la manzana i,j
	 */
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


	/**
	 * @return la suma de los litros de todas las casillas sin tener en cuenta la
	 *         capacidad de la casilla general generadora
	 */
	public double getLitrosCasillasSalvoCasillaGeneral() {
		double resultado = 0;

		for (int i = 0; i < this.columnas / 2; i++) {
			for (int j = 0; j < this.filas; j++) {
				if (i == (this.columnas / 2) - 1 && j == this.filas - 1) break; // La general no
				resultado += this.pEdificios[i][j].getcDerecha().getConsumo() + this.pEdificios[i][j].getcIzquierda().getConsumo();
			}
		}
		return resultado;
	}

	/**
	 * @return un ArrayList de Object en el que guardaremos el contador que ha provocado una rotura de segundo grado en la linea troncal. 
	 * (ID, Contador)
	 */
	public ArrayList<Object> consumoExcesivoTroncal() {
		ArrayList<Object> resultado = new ArrayList<>();
		int i = 0;
		int j = this.lineaTroncal().length - 1;
		
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
				
		for(int i=0; i<tamMax; i++) { //Para cada columna llamamos al metodo consumoExcesivoRec 
			resultado = this.consumoExcesivoRec(lineasDistribucion(i),lineasDistribucionMedias(i), 0, lineasDistribucion(i).length-1);
//			if(roturasContadorLineasD.contains(resultado)) continue; //No añadimos duplicados
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
		ArrayList<Object> resultado = new ArrayList<>();

		//Le voy a añadir un ID al Contador porque luego para buscarlo y decir en que casilla se encuentra creo que es lo mas 
		//rapido para buscarlo en funcion de esto
		
		
		//Lo que devuelve el ArrayList<Object>: 
		//1º -> Su ID (Como despues tenemos que hacer un estudio algoritmico de este metodo solo le metemos el ID para que no 
				//sea muy costoso. Luego hacemos un metodo que en función del ID nos diga donde se ubica el contador
		//2º -> El contador que ha provocado una rotura
		
		if (i >= j - 1) { // Caso base
			if(pE[i].getcDerecha() != null && pE[i].getcDerecha().getConsumo() > 7*media[i].getcDerecha().getConsumo()) {
				resultado.add(pE[i].getcDerecha().getId());
				resultado.add(pE[i].getcDerecha());				
			}
			if(pE[i].getcIzquierda() != null && pE[i].getcIzquierda().getConsumo() > 7*media[i].getcIzquierda().getConsumo()) {
				resultado.add(pE[i].getcIzquierda().getId());
				resultado.add(pE[i].getcIzquierda());				
			}
			if(pE[i].getcMorado() != null && pE[i].getcMorado().getConsumo() > 7*media[i].getcMorado().getConsumo()) {
				resultado.add(pE[i].getcMorado().getId());
				resultado.add(pE[i].getcMorado());				
			}
			if(pE[i].getcVerde() != null && pE[i].getcVerde().getConsumo() > 7*media[i].getcVerde().getConsumo()) {
				resultado.add(pE[i].getcVerde().getId());
				resultado.add(pE[i].getcVerde());				
			}
			
		} else { // Casos recursivos
			mitad = (i + j) / 2;
			this.consumoExcesivoRec(pE, media, i, mitad - 1);
			this.consumoExcesivoRec(pE, media, mitad, j);
		}
		return resultado;
	}
	
	/**
	 * @return una cadena con las casillas en las que se ha producido una rotura
	 */
	public String interpretarSolucionConsumoExcesivo(ArrayList<Object> consumo) {
		String cadena = "";
		int contador = 0;
		int prueba = 0;
		
		
		//Obtenemos el ID del contador y lo buscamos en nuestra matriz

		if(!consumo.isEmpty()) {
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
	

	// MANOMETROS
	private void inicializarManometros() {
			int ancho = pEdificios.length-1;
			int alto = pEdificios[0].length-1;
			double pAnterior, error;
			for(int j=pEdificios[0].length-1; j>=1; j--) {
				for(int i=pEdificios.length-1; i>=0; i--) {				
					if (i==ancho && j==alto) { //Manómetro general
						this.pEdificios[i][j].setMan(new Manometro(Math.random()*(PRESION_MINIMA-PRESION_MAXIMA+1)+PRESION_MAXIMA));
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

}
