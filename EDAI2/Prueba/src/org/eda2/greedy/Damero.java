package org.eda2.greedy;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

/*
 * CAMBIOS QUE HE HECHO: 
 * - HE QUITADO LOS METODOS OBTENERCOORDENAS PARA CONTADORES Y MANOMETROS PORQUE TENIA UN ORDEN CUADRATICO. LO QUE HE HECHO HA SIDO EN LA 
 * INICIALIZACION COMO YA VAMOS TENIENDO LOS INDICES PUES SE LO ASIGNAMOS COMO PROPIEDAD A LOS CONTADORES Y MANOMETROS Y ASI TENEMOS UN ORDEN 
 * CONSTANTE (TAMBIEN LE HE METIDO EL TIPO "D", "I", ETC)
 * 
 * - EN LOS ALGORITMOS GREEDY PUSE UNA PRIORITYQUEUE PORQUE ERA MAS RAPIDO PERO ME HE DADO CUENTA DE QUE UN ARRAYLIST ES TODAVIA MAS RAPIDO PORQUE 
 * PARA OBTENERLO EL ORDEN ES CONSTANTE Y CON LA PRIORITYQUE CREO QUE ERA NLOGN 
 * 
 * - HE HECHO UN NUEVO CONSTRUCTOR DONDE LE PASAMOS COMO PARAMETRO TAMBIEN EL RANGO DE CAUCE PORQUE HE PENSADO QUE POR EJEMPLO PARA UN TAMAÑO DE 
 * 1000X1000 SI LE LLEGAN 0,0001 LITROS PUES VA A SER MUY DIFICIL ENCONTRAR UNA ROTURA 
 * 
 *  - HE METIDO TAMBIEN LA LISTA DE MANZANAS A LOS QUE HAY QUE ENVIAR EL AVISO DE CONSUMO EXCESIVO 
 *  
 * - LO UNICO QUE FALTA ES ESTO:   Se generan dos listas de tramos a estudiar por parte de los técnicos de la compañía. Se
		detallarán los tramos, indicando la ubicación en la trama urbana - inicio y final, según
		las coordenadas (avenida,calle) -.
 * 
 * */

public class Damero {

	private int filas;
	private int columnas;
	private ParEdificios[][] pEdificios;
	private ParEdificios[][] matrizMedias;
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
		this.inicializarContadoresMedia();
		this.inicializarContadores(700,7000);
		this.inicializarManometros();
	}
	
	public Damero(int columnas, int filas, double canMinima, double canMaxima) {
		Contador.reiniciarId();
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
		this.inicializarContadoresMedia();
		this.inicializarContadores(canMinima, canMaxima);
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
		for (int j = 0; j < this.pEdificios[i].length - 1; j++)
			pE[j] = this.pEdificios[i][j];
		return pE;
	}

	/**
	 * 
	 * @param i
	 * @return un array con las medias de los contadores de la linea de distribucion
	 *
	 */
	public ParEdificios[] lineasDistribucionMedias(int i) {
		ParEdificios[] pE = new ParEdificios[this.matrizMedias[0].length - 1]; // Le quitamos la linea troncal
		for (int j = 0; j < this.matrizMedias[i].length - 1; j++)
			pE[j] = this.matrizMedias[i][j];
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
	
	//PARA CUANDO NO QUEREMOS PERSONALIZAR EL DAMERO EN LOS TETS
	
	
	//columna, fila
	public void setParEdificioContador(int i, int j, Contador c) {
		c.setMedia(this.matrizMedias[i][j].getcDerecha());
		c.setI(i);
		c.setJ(j);
		switch(c.getTipo()) {
		case "V":
			if(j == 0 || j == this.pEdificios[0].length) throw new RuntimeException("Ahí no se debe colocar.");
			this.pEdificios[i][j].setcVerde(c);
			break;
		case "M":
			if(j== 0) throw new RuntimeException("Ahí no se debe colocar.");
			this.pEdificios[i][j].setcMorado(c);
			break;
		case "I":
			if(this.pEdificios[0].length % 2 != 0 && i == 0) throw new RuntimeException("Ahí no se debe colocar.");
			this.pEdificios[i][j].setcIzquierda(c);
			break;
		case "D":
			this.pEdificios[i][j].setcDerecha(c);
			break;
		default: throw new RuntimeException("Tipo no válido");
		}
	} 
	
	public void setParEdificioManometro(int i, int j, Manometro m) {
		m.setI(i);
		m.setJ(j);
		this.pEdificios[i][j].setMan(m);
	}
		
	

	// CONTADORES
	private void inicializarContadoresMedia() {
		if (columnas % 2 == 0)
			inicializarContadoresParMedia();
		else
			inicializarContadoresImparMedia();
	}

	/**
	 * Inicializa los datos de los contadores en el caso de que la ciudad tenga
	 * columnas pares
	 */
	private void inicializarContadoresParMedia() {
		
		//NOVEDAD::
		

		// CUANDO CREO UN CONTADOR BUSCO SU CORRESPONDIENDE CONTADOR EN LA MATRIZ MEDIAS (QUE YA ESTA INICIALIZADA, (HE SEPARADO LOS METODOS), Y SE LO PASO POR 
		// PARAMETRO EN EL CONSTRUCTOR --> PARA EL COMPARADOCONTADORES (el comparadorcontadores es para el metodo de resolvercontadores para tenerlos ordenados)
		
		
		// RECORREMOS EL ARRAY INICIALIZANDO LOS CONTADORES ROJOS
		for (int i = 0; i < this.matrizMedias.length; i++) {
			for (int j = 0; j < this.matrizMedias[0].length; j++) {
				if (i != this.matrizMedias.length - 1 || j != this.matrizMedias[0].length - 1) // CASILLA GENERAL
					this.matrizMedias[i][j].setcDerecha(new Contador(Math.random() * (100 - 1000 + 1) + 1000, i,j,"D"));
				this.matrizMedias[i][j].setcIzquierda(new Contador(Math.random() * (100 - 1000 + 1) + 1000, i,j,"I"));
			}
		}
		// INICIALIZAMOS LO CONTADORES VERDES Y MORADOS Y EL GENERAL
		double con = 0;
		for (int i = 0; i < this.matrizMedias.length; i++) {
			for (int j = 1; j < this.matrizMedias[0].length; j++) {
				if (i == pEdificios.length - 1 && j == this.matrizMedias[0].length - 1) {
					con += this.matrizMedias[i][j].getcIzquierda().getConsumo();
					con += this.matrizMedias[i][j - 1].getcVerde().getConsumo();
					con += this.matrizMedias[i - 1][j].getcMorado().getConsumo();
					this.matrizMedias[i][j].setcDerecha(new Contador(con,i,j,"D")); // CONTADOR GENERAL
					continue;
				}
				if (j == this.matrizMedias[0].length - 1 && i != this.matrizMedias.length - 1) { // linea de distribucion
					con += this.matrizMedias[i][j - 1].getcVerde().getConsumo();
					con += this.matrizMedias[i][j].getcDerecha().getConsumo();
					con += this.matrizMedias[i][j].getcIzquierda().getConsumo();
					if (i != 0)
						con += this.matrizMedias[i - 1][j].getcMorado().getConsumo();
					this.matrizMedias[i][j].setcMorado(new Contador(con,i,j,"M"));
				} else if (this.matrizMedias[i][j - 1].getcVerde() == null) { // final de la linea de distribucion por abajo
					con += this.matrizMedias[i][j].getcDerecha().getConsumo();
					con += this.matrizMedias[i][j].getcIzquierda().getConsumo();
					con += this.matrizMedias[i][j - 1].getcDerecha().getConsumo();
					con += this.matrizMedias[i][j - 1].getcIzquierda().getConsumo();
					this.matrizMedias[i][j].setcVerde(new Contador(con,i,j,"V"));
				} else { // caso base
					con += this.matrizMedias[i][j].getcDerecha().getConsumo();
					con += this.matrizMedias[i][j].getcIzquierda().getConsumo();
					con += this.matrizMedias[i][j - 1].getcVerde().getConsumo();
					this.matrizMedias[i][j].setcVerde(new Contador(con,i,j,"V"));
				}
				con = 0;
			}
		}
	}

	/**
	 * Inicializa los datos de los contadores en el caso de que la ciudad tenga
	 * columnas impares
	 */
	private void inicializarContadoresImparMedia() {
		inicializarContadoresParMedia();
		for (int j = 0; j < this.matrizMedias[0].length; j++) {
			this.matrizMedias[0][j].setcIzquierda(null);
		}
	}

	
	/**
	 * Llama al metodo Par o Impar segun el caso
	 */
	private void inicializarContadores(double cMinima, double cMaxima) {
		if (columnas % 2 == 0)
			inicializarContadoresPar(cMinima, cMaxima);
		else
			inicializarContadoresImpar(cMinima, cMaxima);
	}

	/**
	 * Inicializa los datos de los contadores en el caso de que la ciudad tenga
	 * columnas pares
	 */
	private void inicializarContadoresPar(double cMinima, double cMaxima) {
		// RECORREMOS EL ARRAY INICIALIZANDO LOS CONTADORES ROJOS
		for (int i = 0; i < this.pEdificios.length; i++) {
			for (int j = 0; j < this.pEdificios[0].length; j++) {
				if (i != this.pEdificios.length - 1 || j != this.pEdificios[0].length - 1) // CASILLA GENERAL
					this.pEdificios[i][j].setcDerecha(new Contador(Math.random() * (cMinima - cMaxima+ 1) + cMaxima, this.matrizMedias[i][j].getcDerecha(), i,j,"D"));
				this.pEdificios[i][j].setcIzquierda(new Contador(Math.random() * (cMinima - cMaxima+ 1) + cMaxima,this.matrizMedias[i][j].getcIzquierda(),i,j, "I"));
			}
		}
		// INICIALIZAMOS LO CONTADORES VERDES Y MORADOS Y EL GENERAL
		double con = 0;
		for (int i = 0; i < this.pEdificios.length; i++) {
			for (int j = 1; j < this.pEdificios[0].length; j++) {
				if (i == pEdificios.length - 1 && j == this.pEdificios[0].length - 1) {
					con += this.pEdificios[i][j].getcIzquierda().getConsumo();
					con += this.pEdificios[i][j - 1].getcVerde().getConsumo();
					con += this.pEdificios[i - 1][j].getcMorado().getConsumo();
					this.pEdificios[i][j].setcDerecha(new Contador(con,this.matrizMedias[i][j].getcDerecha(),i,j,"D")); // CONTADOR GENERAL
					continue;
				}
				if (j == this.pEdificios[0].length - 1 && i != this.pEdificios.length - 1) { // linea de distribucion
					con += this.pEdificios[i][j - 1].getcVerde().getConsumo();
					con += this.pEdificios[i][j].getcDerecha().getConsumo();
					con += this.pEdificios[i][j].getcIzquierda().getConsumo();
					if (i != 0)
						con += this.pEdificios[i - 1][j].getcMorado().getConsumo();
					this.pEdificios[i][j].setcMorado(new Contador(con, this.matrizMedias[i][j].getcMorado(),i,j,"M"));
				} else if (this.pEdificios[i][j - 1].getcVerde() == null) { // final de la linea de distribucion por abajo
					con += this.pEdificios[i][j].getcDerecha().getConsumo();
					con += this.pEdificios[i][j].getcIzquierda().getConsumo();
					con += this.pEdificios[i][j - 1].getcDerecha().getConsumo();
					con += this.pEdificios[i][j - 1].getcIzquierda().getConsumo();
					this.pEdificios[i][j].setcVerde(new Contador(con, this.matrizMedias[i][j].getcVerde(),i,j,"V"));
				} else { // caso base
					con += this.pEdificios[i][j].getcDerecha().getConsumo();
					con += this.pEdificios[i][j].getcIzquierda().getConsumo();
					con += this.pEdificios[i][j - 1].getcVerde().getConsumo();
					this.pEdificios[i][j].setcVerde(new Contador(con, this.matrizMedias[i][j].getcVerde(),i,j,"V"));
				}
				con = 0;
			}
		}
	}

	/**
	 * Inicializa los datos de los contadores en el caso de que la ciudad tenga
	 * columnas impares
	 */
	private void inicializarContadoresImpar(double cMinima, double cMaxima) {
		inicializarContadoresPar(cMinima, cMaxima);
		for (int j = 0; j < this.pEdificios[0].length; j++) {
			this.pEdificios[0][j].setcIzquierda(null);
		}
	}

	public String toString() {
		String resultado = "";
		for (int j = 0; j < pEdificios[0].length; j++) {
			for (int i = 0; i < pEdificios.length; i++) {
				resultado += pEdificios[i][j].toString() + "(" + i + " ," + j + ")" + "\t";
			}
			resultado += "\n";
		}
		return resultado;
	}

	public String toStringMedias() {
		String resultado = "";
		for (int j = 0; j < this.matrizMedias[0].length; j++) {
			for (int i = 0; i < this.matrizMedias.length; i++) {
				resultado += this.matrizMedias[i][j].toString() + "(" + i + " ," + j + ")" + "\t";
			}
			resultado += "\n";
		}
		return resultado;
	}

	/**
	 * 
	 * @return un string que contiene los datos de todos los manometros de la ciudad
	 */
	public String toStringManometros() {
		String resultado = "";
		for (int j = 0; j < pEdificios[0].length; j++) {
			for (int i = 0; i < pEdificios.length; i++) {
				resultado += pEdificios[i][j].toStringManometros() + "(" + i + " ," + j + ")" + "\t";
			}
			resultado += "\n";
		}
		return resultado;
	}
	
	public String toStringManometrosMedia() {
		String resultado = "";
		for (int j = 0; j < this.matrizMedias[0].length; j++) {
			for (int i = 0; i < this.matrizMedias.length; i++) {
				resultado += this.matrizMedias[i][j].toStringManometros() + "(" + i + " ," + j + ")" + "\t";
			}
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
		if (cont.equals("D"))
			return pEdificios[i][j].getcDerecha().getConsumo();
		else if (cont.equals("I"))
			return pEdificios[i][j].getcIzquierda().getConsumo();
		else if (cont.equals("V"))
			return pEdificios[i][j].getcVerde().getConsumo();
		else
			return pEdificios[i][j].getcMorado().getConsumo();
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
				else if (columnas % 2 != 0 && i == 0)
					resultado += this.pEdificios[i][j].getcDerecha().getConsumo();
				else
					resultado += this.pEdificios[i][j].getcDerecha().getConsumo()
							+ this.pEdificios[i][j].getcIzquierda().getConsumo();
			}
		}
		return resultado;
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
								cadena += "* Casilla: [" + i + ", " + j
										+ "]. El contador derecho ha provocado la rotura\n";
							else if (tipo.equals("I"))
								cadena += "* Casilla: [" + i + ", " + j
										+ "]. El contador izquierdo ha provocado la rotura\n";
							else if (tipo.equals("M"))
								cadena += "* Casilla: [" + i + ", " + j
										+ "]. El contador morado ha provocado la rotura\n";
							else if (tipo.equals("V"))
								cadena += "* Casilla: [" + i + ", " + j
										+ "]. El contador verde ha provocado la rotura\n";
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
					this.pEdificios[i][j].setMan(new Manometro(Math.random() * (110 - 150 + 1) + 150,i,j));
					continue;
				}
				// Obtenemos el valor de la presion del manometro anterior para obtener el
				// siguiente a partir de él
				if (j == alto)
					pAnterior = this.pEdificios[i + 1][j].getMan().getPresion();
				else
					pAnterior = this.pEdificios[i][j + 1].getMan().getPresion();

				x = Math.random() * (13 - 55 + 1) + 55; // cantidad a disminuir

				if (x > 50) { // INVOCANDO EL CASO DEL REVENTON
					this.pEdificios[i][j].setMan(new Manometro(0.0,i,j));
				} else {
					error = pAnterior - (pAnterior * x / 100);

					this.pEdificios[i][j].setMan(new Manometro((Math.random() * (error - pAnterior + 1) + pAnterior),i,j));
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
	 * PROBLEMA a.2
	 * @return una lista con el id de los contadores que presentan una rotura propia
	 */
	public ArrayList<Integer> resolverContadoresRoturaPropiaGreedy() { //PROBADO
		ArrayList<Contador> candidatos = obtenerCandidatosContadores(); // todos los contadores
		ArrayList<Integer> elegidos = new ArrayList<>();
		Contador posible;
		ArrayList<Contador> candidatosOrdenados = new ArrayList<>(candidatos);
		candidatosOrdenados.sort(new ComparadorContadores()); //mas eficiente asi que con cola de prioridad
		
		while (!candidatosOrdenados.isEmpty()) { // Solucion: hemos comprobado todos los contadores
			posible = candidatosOrdenados.get(0); 
			candidatosOrdenados.remove(posible); // Eliminamos posible de la lista de candidatos
			if (roturaPropia(posible)) {// Devuelve true o false si el contador tiene una rotura propia o no
				elegidos.add(posible.getId());
			}
		}
		return elegidos;
	}


	/**
	 * @param con contador del que queremos ver si tiene una rotura propia y consumo
	 *            mayor de 5xmedia
	 * @return true si presenta las condiciones anteriormente dichas, false en caso
	 *         contrario
	 */
	public boolean roturaPropia(Contador con) { // Funcion de factibilidad
//		String[] indices = obtenerCoordenadas(con);
//		int i = Integer.parseInt(indices[0]); 
//		int j = Integer.parseInt(indices[1]);
		Integer i = con.getI();
		Integer j = con.getJ();
		String[] indices = {i.toString(),j.toString(), con.getTipo()};
		if (!comprobarRoturaPropia(con, indices)) return false; // Si la rotura no es del propio contador, ya no nos interesa
		
		Contador media = new Contador();
		switch (indices[2]) {
		case "D":
			media = this.matrizMedias[i][j].getcDerecha();
			break;
		case "I":
			media = this.matrizMedias[i][j].getcIzquierda();
			break;
		case "V":
			media = this.matrizMedias[i][j].getcVerde();
			break;
		case "M":
			media = this.matrizMedias[i][j].getcMorado();
			break;
		}
		double consumo = con.getConsumo();
		double mediaCon = media.getConsumo();
		// comprueba si el consumo es mayor a la media en 5 veces
		if (consumo > mediaCon * 5) return true;
		return false;
	}

	/**
	 * @param con     contador del que queremos comprobar la rotura
	 * @param indices salida del metodo obtenerCoordenadas
	 * @return true si la rotura es propia, false en caso contrario
	 */
	public boolean comprobarRoturaPropia(Contador con, String[] indices) { // Este metodo comprueba si hay rotura																// propia
		int i = Integer.parseInt(indices[0]);
		int j = Integer.parseInt(indices[1]);
		
		if (indices[2].equals("V")) {
			if (j == 1) { // Final de la linea de distribucion
				double verde = this.pEdificios[i][j].getcVerde().getConsumo();
				double dcha = this.pEdificios[i][j].getcDerecha().getConsumo();
				double izqda = (this.pEdificios[i][j].getcIzquierda() != null) ? this.pEdificios[i][j].getcIzquierda().getConsumo()  : 0 ;
				double abajoD = this.pEdificios[i][j - 1].getcDerecha().getConsumo();
				double abajoI = (this.pEdificios[i][j - 1].getcIzquierda() != null) ? this.pEdificios[i][j - 1].getcIzquierda().getConsumo() : 0.0 ;
				if (verde > (dcha + izqda + abajoD + abajoI)) return true;
			} else { // Caso mas comun
				double verde = this.pEdificios[i][j].getcVerde().getConsumo();
				double dcha = this.pEdificios[i][j].getcDerecha().getConsumo();
				double izqda = (this.pEdificios[i][j].getcIzquierda() != null) ? this.pEdificios[i][j].getcIzquierda().getConsumo()  : 0 ;
				double verde2 = this.pEdificios[i][j - 1].getcVerde().getConsumo();
				if (verde > (dcha + izqda + verde2)) return true;
			}
		} else if (indices[2].equals("M")) {
			double morado = this.pEdificios[i][j].getcMorado().getConsumo();
			double dcha = this.pEdificios[i][j].getcDerecha().getConsumo();
			double verde = this.pEdificios[i][j - 1].getcVerde().getConsumo();
			// He cambiado las dos lineas de abajo
			double izquierda = (this.pEdificios[i][j].getcIzquierda() != null) ? izquierda = this.pEdificios[i][j].getcIzquierda().getConsumo() : 0.0;
			double moradoI = (i > 0 && this.pEdificios[i - 1][j].getcMorado() != null) ? this.pEdificios[i - 1][j].getcMorado().getConsumo() : 0.0;
			if (morado > (dcha + izquierda + verde + moradoI)) return true;

		} else { // Casilla general
			double izquierda = this.pEdificios[i][j].getcIzquierda().getConsumo();
			double derecha = this.pEdificios[i][j].getcDerecha().getConsumo();// general -> ESTO SOLO PARA EL CASO PAR?
			double moradoI = this.pEdificios[i - 1][j].getcMorado().getConsumo();
			double verdeAbajo = this.pEdificios[i][j - 1].getcVerde().getConsumo();
			if (derecha > (izquierda + moradoI + verdeAbajo))
				return true;
		}
		return false;
	}


	/**
	 * @return una lista con todos los candidatos a ser estudiados. En nuestro caso,
	 *         todos los contadores de nuestra red (verdes y morados)
	 */
	public ArrayList<Contador> obtenerCandidatosContadores() {
		ArrayList<Contador> candidatos = new ArrayList<>();

		for (int i = 0; i < this.pEdificios.length; i++) {
			for (int j = 0; j < this.pEdificios[i].length; j++) {
				if (this.pEdificios[i][j].getcMorado() != null) candidatos.add(this.pEdificios[i][j].getcMorado());
				if (this.pEdificios[i][j].getcVerde() != null) candidatos.add(this.pEdificios[i][j].getcVerde());
				if (i == this.pEdificios.length - 1 && j == this.pEdificios[0].length - 1) candidatos.add(this.pEdificios[i][j].getcDerecha());
			}
		}
		return candidatos;
	}

	// 1a Manometros con un consumo mayor a 10%

	/**
	 * @return una lista con el id de los manometros que presentan una rotura
	 */
	public ArrayList<Integer> resolverManometrosGreedy() {
		ArrayList<Manometro> candidatos = obtenerCandidatosManometros(); // todos los manometros
		ArrayList<Integer> elegidos = new ArrayList<>();
		Manometro posible;
		ArrayList<Manometro> candidatosOrdenados = new ArrayList<>(candidatos);
		candidatos.sort(new ComparadorManometros());

		while (!candidatosOrdenados.isEmpty()) { // Solucion: hemos comprobado todos los manometros
			posible = candidatosOrdenados.get(0);
			candidatosOrdenados.remove(posible); // Eliminamos posible de la lista de candidatos
			if (roturaManometro(posible)) {// Devuelve true o false si el manometro tiene una rotura o no
				elegidos.add(posible.getId());
			}
		}
		return elegidos;
	}
	

	/**
	 * @return una lista con todos los manometros del damero, puesto que todos son candidatos a ser estudiados
	 */
	public ArrayList<Manometro> obtenerCandidatosManometros() {
		ArrayList<Manometro> candidatos = new ArrayList<>();
		for (int i = 0; i < this.pEdificios.length; i++)
			for (int j = 0; j < this.pEdificios[0].length; j++) {
				if (this.pEdificios[i][j].getMan() != null)
					candidatos.add(this.pEdificios[i][j].getMan());
			}
		return candidatos;
	}


	/**
	 * @param posible candidato a presentar rotura
	 * @return true si el manometro presenta una rotura
	 */
	public boolean roturaManometro(Manometro posible) { // Comprobamos si hay una rotura entre el manometro posible i el anterior
		int i= posible.getI();
		int j = posible.getJ();
		
		Manometro anterior = new Manometro();
		if (j == this.pEdificios[0].length - 1) { // TRONCAL FALLO ENTRE [i,j] Y [i+1,j]
			if (i == this.pEdificios.length - 1) return false;
			anterior = this.pEdificios[i + 1][j].getMan(); 
		} else { // DISTRIBUCION FALLO ENTRE [i,j] Y [i,j+1]
			anterior = this.pEdificios[i][j + 1].getMan();
		}
		if (posible.getPresion() < (anterior.getPresion() - anterior.getPresion() * 0.1))
			return true;
		return false;

	}

	// b
	
	/**
	 * @return una lista con el id de aquellos contadores que presentan un consumo excesivo (mas del 700% con respecto a la media) 
	 */
	public ArrayList<Integer> resolverConsumidoresGreedy() {
		ArrayList<Contador> candidatos = obtenerCandidatosConsumidores(); // todos los manometros
		ArrayList<Integer> elegidos = new ArrayList<>();
		Contador posible;
		ArrayList<Contador> candidatosOrdenados = new ArrayList<>(candidatos);
		candidatosOrdenados.sort(new ComparadorContadores());

		while (!candidatosOrdenados.isEmpty()) { // Solucion: hemos comprobado todos los manometros
			posible = candidatosOrdenados.get(0); 
			candidatosOrdenados.remove(posible); // Eliminamos posible de la lista de candidatos
			if (roturaContador(posible)) {// Devuelve true o false si el manometro tiene una rotura o no
				elegidos.add(posible.getId());
			}
		}
		return elegidos;
	}
	
	public String[] obtenerCoordenadasDadoId(Integer id) {
		String[] coords = new String[2];
		for(int i=0; i<this.pEdificios.length; i++) {
			for(int j=0; j<this.pEdificios[i].length; j++) {
				if(this.pEdificios[i][j].getcDerecha() != null && this.pEdificios[i][j].getcDerecha().getId() == id) {
					coords[0] = Integer.toString(this.pEdificios[i][j].getcDerecha().getI());
					coords[1] = Integer.toString(this.pEdificios[i][j].getcDerecha().getJ());
				}
				if(this.pEdificios[i][j].getcIzquierda() != null && this.pEdificios[i][j].getcIzquierda().getId() == id) {
					coords[0] = Integer.toString(this.pEdificios[i][j].getcIzquierda().getI());
					coords[1] = Integer.toString(this.pEdificios[i][j].getcIzquierda().getJ());
				}
				if(this.pEdificios[i][j].getcMorado() != null && this.pEdificios[i][j].getcMorado().getId() == id) {
					coords[0] = Integer.toString(this.pEdificios[i][j].getcMorado().getI());
					coords[1] = Integer.toString(this.pEdificios[i][j].getcMorado().getJ());
				}
				if(this.pEdificios[i][j].getcVerde() != null && this.pEdificios[i][j].getcVerde().getId() == id) {
					coords[0] = Integer.toString(this.pEdificios[i][j].getcVerde().getI());
					coords[1] = Integer.toString(this.pEdificios[i][j].getcVerde().getJ());
				}
			}
		}
		
		return coords;
	}
	
	public Contador obtenerContadorDadoId(Integer id) {
		Contador c = null; 
		
		for(int i=0; i<this.pEdificios.length; i++) {
			for(int j=0; j<this.pEdificios[i].length; j++) {
				if(this.pEdificios[i][j].getcDerecha() != null && this.pEdificios[i][j].getcDerecha().getId() == id) {
					c = this.pEdificios[i][j].getcDerecha();
				}
				if(this.pEdificios[i][j].getcIzquierda() != null && this.pEdificios[i][j].getcIzquierda().getId() == id) {
					c = this.pEdificios[i][j].getcIzquierda();
				}
				if(this.pEdificios[i][j].getcMorado() != null && this.pEdificios[i][j].getcMorado().getId() == id) {
					c = this.pEdificios[i][j].getcMorado();
				}
				if(this.pEdificios[i][j].getcVerde() != null && this.pEdificios[i][j].getcVerde().getId() == id) {
					c = this.pEdificios[i][j].getcVerde();
				}
			}
		}
		
		return c;
	}
	
	//LISTADO CON LAS MANZANAS A LAS QUE HAY QUE ENVIARLE EL AVISO 
	//Clave del TreeMap -> coordenadas de la rotura del contador (destino= 
	//Valor -> TreeSet donde guardamos en el siguiente orden (conusmo medio,
	 			//,consumoActual, division)
	public TreeMap<String, ArrayList<Double>> manzanasConsumoExcesivo(ArrayList<Integer> cRoturas){
		ArrayList<Integer> contadoresRoturas = new ArrayList<>(cRoturas); //Queremos una copia no editar la otra
		String[] coords = new String[2];
		TreeMap<String, ArrayList<Double>> result = new TreeMap<>();
		Contador c = null;
		
		ArrayList<Double> datos;
		
		for(Integer id : contadoresRoturas) {
			datos = new ArrayList<>();
			coords = this.obtenerCoordenadasDadoId(id);
			//Con el id obtenemos el contador, con el contador obtenemos el contador medio
			c = this.obtenerContadorDadoId(id);
			datos.add(c.getConsumo());
			datos.add(c.getMedia().getConsumo());
			datos.add(c.getConsumo()/c.getMedia().getConsumo());
			
			if(c != null) result.put("(" + coords[0] + ", "+ coords[1] + ")", datos);
		}
		
		return result;
	}

	/**
	 * @return todos los contadores finales del damero
	 */
	public ArrayList<Contador> obtenerCandidatosConsumidores() { 
		ArrayList<Contador> candidatos = new ArrayList<>();
		for (int i = 0; i < this.pEdificios.length; i++) {
			for (int j = 0; j < this.pEdificios[0].length; j++) {
				if (this.pEdificios[i][j].getcDerecha() != null) {
					if (i != this.pEdificios.length - 1 || j != this.pEdificios[0].length - 1) {
						candidatos.add(this.pEdificios[i][j].getcDerecha());
					}
				}
				if (this.pEdificios[i][j].getcIzquierda() != null) candidatos.add(this.pEdificios[i][j].getcIzquierda());
				
			}
		}
		return candidatos;
	}

	/**
	 * Aqui tenemos otro tipo de rotura que se presenta por un consumo excesivo con respecto a la media
	 * @param posible contador que queremos comprobar si presenta una rotura 
	 * @return true si la presenta 
	 */
	public boolean roturaContador(Contador posible) {
		int i = posible.getI();
		int j = posible.getJ();
		String tipo = posible.getTipo();
		
		double consumo = posible.getConsumo();
		double consumoMedio = this.matrizMedias[i][j].getContador(tipo).getConsumo();
		if (consumo > consumoMedio * 7) {
			return true;
		}
		return false;
	}
	
	
}