package org.eda2.dyv;

import java.util.ArrayList;

public class Damero {

	private int filas;
	private int columnas;
	private ParEdificios[][] pEdificios;
	private ParEdificios[][] matrizMedias;
	private ArrayList<Integer> roturasContadorTroncal = new ArrayList<>();
	private ArrayList<Integer> roturasContadorLineasD = new ArrayList<>();
	private ArrayList<Integer> roturasContadorProblemaRecursivo = new ArrayList<>();
	private ArrayList<Integer> roturasManometroProblemaRecursivo = new ArrayList<>();
	private ArrayList<Integer> roturasManometroTroncal = new ArrayList<>();
	private ArrayList<Integer> roturasManometroLineasD = new ArrayList<>();
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
		Manometro.reiniciarID();
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
		this.inicializarContadores(this.pEdificios);
		this.inicializarContadores(this.matrizMedias);
		this.inicializarManometros();
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
	public ArrayList<Integer> getRoturasContadorTroncal() {
		return roturasContadorTroncal;
	}

	/**
	 * 
	 * @return roturasContadorLineasD
	 */
	public ArrayList<Integer> getRoturasContadorLineasD() {
		return roturasContadorLineasD;
	}
	
	/**
	 * 
	 * @return roturasManometroTroncal
	 */
	public ArrayList<Integer> getRoturasManometroTroncal() {
		return roturasManometroTroncal;
	}

	/**
	 * Establece el valor de roturasManometroTroncal
	 * @param roturasManometroTroncal
	 */
	public void setRoturasManometroTroncal(ArrayList<Integer> roturasManometroTroncal) {
		this.roturasManometroTroncal = roturasManometroTroncal;
	}

	/**
	 * 
	 * @return roturasManometroLineasD
	 */
	public ArrayList<Integer> getRoturasManometroLineasD() {
		return roturasManometroLineasD;
	}

	/**
	 * Establece el valor de roturasManometroLineasD
	 * @param roturasManometroLineasD
	 */
	public void setRoturasManometroLineasD(ArrayList<Integer> roturasManometroLineasD) {
		this.roturasManometroLineasD = roturasManometroLineasD;
	}


	// CONTADORES
	/**
	 * LLama al metodo Par o Impar segun el caso
	 */
	private void inicializarContadores(ParEdificios[][] pE) {
		if (columnas % 2 == 0) inicializarContadoresPar(pE);
		else inicializarContadoresImpar(pE);
	}

	/**
	 * Inicializa los datos de los contadores en el caso de que la ciudad tenga
	 * columnas pares
	 */
	private void inicializarContadoresPar(ParEdificios[][] pE) { 

		// RECORREMOS EL ARRAY INICIALIZANDO LOS CONTADORES ROJOS
		for (int i = 0; i < pE.length; i++) {
			for (int j = 0; j < pE[0].length; j++) {
				if (i != pE.length - 1 || j != pE[0].length - 1) // CASILLA GENERAL
					pE[i][j].setcDerecha(new Contador(Math.random() * (100 - 1000 + 1) + 1000));
				pE[i][j].setcIzquierda(new Contador(Math.random() * (100 - 1000 + 1) + 1000));
			}
		}

		// INICIALIZAMOS LO CONTADORES VERDES Y MORADOS Y EL GENERAL
		double con = 0;
		for (int i = 0; i < pE.length; i++) {
			for (int j = 1; j < pE[0].length; j++) {
				if (i == pEdificios.length - 1 && j == pE[0].length - 1) {
					con += pE[i][j].getcIzquierda().getConsumo();
					con += pE[i][j - 1].getcVerde().getConsumo();
					con += pE[i - 1][j].getcMorado().getConsumo();
					pE[i][j].setcDerecha(new Contador(con)); // CONTADOR GENERAL
					continue;
				}
				if (j == pE[0].length - 1 && i != pE.length - 1) { // linea de distribucion
					con += pE[i][j - 1].getcVerde().getConsumo();
					con += pE[i][j].getcDerecha().getConsumo();
					con += pE[i][j].getcIzquierda().getConsumo();
					if (i != 0)
						con += pE[i - 1][j].getcMorado().getConsumo();
					pE[i][j].setcMorado(new Contador(con));
				} else if (pE[i][j - 1].getcVerde() == null) { // final de la linea de distribucion por abajo
					con += pE[i][j].getcDerecha().getConsumo();
					con += pE[i][j].getcIzquierda().getConsumo();
					con += pE[i][j - 1].getcDerecha().getConsumo();
					con += pE[i][j - 1].getcIzquierda().getConsumo();
					pE[i][j].setcVerde(new Contador(con));
				} else { // caso base
					con += pE[i][j].getcDerecha().getConsumo();
					con += pE[i][j].getcIzquierda().getConsumo();
					con += pE[i][j - 1].getcVerde().getConsumo();
					pE[i][j].setcVerde(new Contador(con));
				}
				con = 0;
			}
		}
	}

	/**
	 * Inicializa los datos de los contadores en el caso de que la ciudad tenga
	 * columnas impares
	 */
	private void inicializarContadoresImpar(ParEdificios[][] pE) {
		inicializarContadoresPar(pE);
		for (int j = 0; j < pE[0].length; j++) {
			pE[0][j].setcIzquierda(null);
		}
	}

	/**
	 * @return string con los datos de cada casilla del damero y sus respectivas coordenadas
	 */
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

	/**
	 * 
	 * @return string con las medias de los contadores de la ciudad
	 */
	public String toStringMedias() {
		String resultado = "";
		for(int j=0; j<matrizMedias[0].length; j++) {
			for(int i=0; i<matrizMedias.length; i++) {
				resultado +=  matrizMedias[i][j].toString() + "(" + i+" ,"+j + ")" + "\t";
			}
			resultado += "\n";			
		}
		return resultado;
	}

	/**
	 * 
	 * @return string que contiene los datos de todos los manometros de la ciudad
	 */
	public String toStringM() {
		String resultado = "";
		for(int j=0; j<pEdificios[0].length; j++) {
			for(int i=0; i<pEdificios.length; i++) {
				resultado +=  pEdificios[i][j].toStringManometros() + "(" + i+" ,"+j + ")" + "\t";
			}
			resultado += "\n";	
		}
		return resultado;
	}

	/**
	 * 
	 * @return string con todos los contadores de la ciudad
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
	public double getLitrosEdificio(int i, int j, ParEdificios[][] pEdificios, String cont) {
		if (cont.equals("D")) return pEdificios[i][j].getcDerecha().getConsumo();
		else if (cont.equals("I")) return pEdificios[i][j].getcIzquierda().getConsumo();
		else if (cont.equals("V")) return pEdificios[i][j].getcVerde().getConsumo();
		else return pEdificios[i][j].getcMorado().getConsumo();
	}

	
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

	/**
	 * @return un ArrayList de Object en el que guardaremos el contador que ha
	 *         provocado una rotura de segundo grado en la linea troncal. (ID,
	 *         Contador)
	 */
	public ArrayList<Integer> consumoExcesivoTroncal() {
		ArrayList<Integer> resultado = new ArrayList<>();
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
	public ArrayList<Integer> consumoExcesivoLineasDistribucion() {
		ArrayList<Integer> resultado = new ArrayList<>();
		int tamMax = this.pEdificios.length;

		for (int i = 0; i < tamMax; i++) { // Para cada columna llamamos al metodo consumoExcesivoRec
			resultado = this.consumoExcesivoRec(lineasDistribucion(i), lineasDistribucionMedias(i), 0, lineasDistribucion(i).length - 1);
			if (!resultado.isEmpty()) {
				roturasContadorLineasD.clear();
				roturasContadorLineasD.addAll(resultado);
			}
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
	 * @param i  posicion inicial a partir de la cual vamos a empezar a buscar
	 * @param j  posicion final de la búsqueda
	 * @return un ArrayList con todos los contadores que han presentado una rotura
	 */
	private ArrayList<Integer> consumoExcesivoRec(ParEdificios[] pE, ParEdificios[] media, int i, int j) {
		int mitad;

		if (i == j) { // Caso base
			if (pE[i].getcDerecha() != null && pE[i].getcDerecha().getConsumo() > 7 * media[i].getcDerecha().getConsumo()) {
				roturasContadorProblemaRecursivo.add(pE[i].getcDerecha().getId());
			}
			if (pE[i].getcIzquierda() != null && pE[i].getcIzquierda().getConsumo() > 7 * media[i].getcIzquierda().getConsumo()) {
				roturasContadorProblemaRecursivo.add(pE[i].getcIzquierda().getId());
			}
			if (pE[i].getcMorado() != null && pE[i].getcMorado().getConsumo() > 7 * media[i].getcMorado().getConsumo()) {
				roturasContadorProblemaRecursivo.add(pE[i].getcMorado().getId());
			}
			if (pE[i].getcVerde() != null && pE[i].getcVerde().getConsumo() > 7 * media[i].getcVerde().getConsumo()) {
				roturasContadorProblemaRecursivo.add(pE[i].getcVerde().getId());
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
	public String interpretarSolucionConsumoExcesivo(ArrayList<Integer> consumo) {
		String cadena = "";

		// Obtenemos el ID del contador y lo buscamos en nuestra matriz
		if (!consumo.isEmpty()) {
			for (Integer id : consumo) {
					for (int i = 0; i < this.pEdificios.length; i++) {
						for (int j = 0; j < this.pEdificios[i].length; j++) {
							if (pEdificios[i][j].containsContadorID(id)) {
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
		double x;
		
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

				x = Math.random() * (13 - 55 + 1) + 55; //cantidad a disminuir
				
				//si se ha perdido más de un 50%, entonces es porque ha habido algun problema tecnico en el 
				//manometro i, y no ha podido llegar la presion al manometro i+1
				
				if(x > 50) { //INVOCANDO EL CASO DEL REVENTON
					this.pEdificios[i][j].setMan(new Manometro(0.0));
				} else {
					error = pAnterior - (pAnterior * x / 100); //presionAnterior disminuida una cantidad x/100
					this.pEdificios[i][j].setMan(new Manometro((Math.random() * (error - pAnterior + 1) + pAnterior)));
				}
				

			}
		}
	}
	
	/**
	 * @param i columna del ParEdificio al que queremos acceder
	 * @param j fila del ParEdificio al que queremos acceder
	 * @return la presion 
	 */
	public double getPresionPar(int i, int j) {
		return this.pEdificios[i][j].getMan().getPresion();
	}
	
	/**
	 * @return una lista con los IDS de los manometros que presentan una rotura de 
	 * primer grado en la linea troncal
	 */
	public ArrayList<Integer> perdidaExcesivaPresionTroncal(){
		ArrayList<Integer> resultado = new ArrayList<>();
		ParEdificios[] pE = this.lineaTroncal();
		this.roturasManometroTroncal.clear();
		
		resultado = this.perdidaExcesivaPresionRec(pE, 0, pE.length-1);
		
		if(!resultado.isEmpty()) {
			this.roturasContadorTroncal.clear();
			this.roturasManometroTroncal.addAll(resultado);
		}
		
		return resultado;
	}
	
	/**
	 * @return una lista con los IDS de los manometros que presentan una rotura de 
	 * primer grado en las lineas de distribucion
	 */
	public ArrayList<Integer> perdidaExcesivaPresionLineasDistribucion(){
		ArrayList<Integer> resultado = new ArrayList<>();
		int tamMax = this.pEdificios.length;
		
		
		ParEdificios[] pE = new ParEdificios[this.pEdificios[0].length];
			
		for(int i=0; i<tamMax; i++) {
			//Tenemos que quitarle la primera fila porque no hay manometros
			pE = this.elminarPrimeraFila(this.lineasDistribucion(i));
			resultado = this.perdidaExcesivaPresionRec(pE, 0, pE.length-1);
			
			if(!resultado.isEmpty()) {
				roturasManometroLineasD.clear();
				roturasManometroLineasD.addAll(resultado);
			}
		}
		return roturasManometroLineasD;
	}
	
	

	/**
	 * @param pE array de ParEdificios al que queremos eliminarle la primera fila
	 * @return el array sin la primera fila, puesto que los manometros son nulos, los quitamos
	 */
	public ParEdificios[] elminarPrimeraFila(ParEdificios[] pE) {
		ParEdificios[] resultado = new ParEdificios[pE.length-1];
		for(int i=0; i<resultado.length; i++) {
			resultado[i] = pE[i+1];
		}
		
		return resultado;
	}
		
	/**
	 * Es muy similar al anterior. Sin embargo, aqui necesitamos que el elemento de la mitad se mantenga en ambas llamadas. Y en el caso base
	 * necesitamos dos elementos en lugar de uno
	 * @return
	 */
	private ArrayList<Integer> perdidaExcesivaPresionRec(ParEdificios[] pE, int i, int j){
		int mitad;
		if(j-i == 1) {
			if(pE[i].getMan().getPresion() < (pE[j].getMan().getPresion() - 0.1*pE[j].getMan().getPresion())) {
				this.roturasManometroProblemaRecursivo.add(pE[i].getMan().getId());
			}
		} else {
			mitad = (i + j)/2;
			perdidaExcesivaPresionRec(pE, i, mitad);
			perdidaExcesivaPresionRec(pE, mitad, j);
		}
		return this.roturasManometroProblemaRecursivo;		
	}
	
	
	/**
	 * @param roturas la lista con las roturas encontradas
	 * @return una cadena con las casillas en las que hay roturas
	 */
	public String interpretarSolucionPerdidaExcesivo(ArrayList<Integer> roturas){
		String cadena = "";
		
		for(Integer id : roturas) {
			for(int i=0; i<this.pEdificios.length; i++) {
				for(int j=0; j<this.pEdificios[i].length; j++) {
					if(this.pEdificios[i][j].getMan() == null) continue;
					if(id.equals(this.pEdificios[i][j].getMan().getId())) cadena += "* Casilla: [" + i + ", " + j + "]. El manometro ha provocado la rotura\n";
				}
			}
		}
		return cadena;
	}
}