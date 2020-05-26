package org.eda2.dynamic;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Damero {

	private int filas;
	private int columnas;
	private ParEdificios[][] pEdificios;
	private ParEdificios[][] matrizMedias;
	private ArrayList<Integer> listaClientesA = new ArrayList<>();
	// Consumo minimo y maximo del contador general
	public final static double CONSUMO_MINIMO_GENERAL = 300000;
	public final static double CONSUMO_MAXIMO_GENERAL = 500000;
	// Consumo minimo y maximo medio
	public final static double CONSUMO_MINIMO_MEDIO = 108;
	public final static double CONSUMO_MAXIMO_MEDIO = 162;
	// Presiones para la casilla general
	public final static double PRESION_MAXIMA = 150;
	public final static double PRESION_MINIMA = 110;
	public final static double BA = 1; //hora;
	public static int WTT = 146; //hora;
	
	private double[][] table;
	private ArrayList<Contador> beneficiosYPesos;
	


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
				
		this.inicializarContadoresMedia(100,1000);
		this.inicializarContadores(700,2000);
	}
	
	public Damero(int columnas, int filas, double canMinima, double canMaxima, int WTT) {
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
		this.inicializarContadoresMedia(canMinima/7, canMaxima/7);
		this.inicializarContadores(canMinima, canMaxima);
		Damero.WTT = WTT;
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
	

	// CONTADORES
	private void inicializarContadoresMedia(double canMax, double canMin) {
		if (columnas % 2 == 0)
			inicializarContadoresParMedia(canMax, canMin);
		else
			inicializarContadoresImparMedia(canMax, canMin);
	}

	/**
	 * Inicializa los datos de los contadores en el caso de que la ciudad tenga
	 * columnas pares
	 */
	private void inicializarContadoresParMedia(double canMax, double canMin) {
		
		//NOVEDAD::
		

		// CUANDO CREO UN CONTADOR BUSCO SU CORRESPONDIENDE CONTADOR EN LA MATRIZ MEDIAS (QUE YA ESTA INICIALIZADA, (HE SEPARADO LOS METODOS), Y SE LO PASO POR 
		// PARAMETRO EN EL CONSTRUCTOR --> PARA EL COMPARADOCONTADORES (el comparadorcontadores es para el metodo de resolvercontadores para tenerlos ordenados)
		
		
		// RECORREMOS EL ARRAY INICIALIZANDO LOS CONTADORES ROJOS
		for (int i = 0; i < this.matrizMedias.length; i++) {
			for (int j = 0; j < this.matrizMedias[0].length; j++) {
				if (i != this.matrizMedias.length - 1 || j != this.matrizMedias[0].length - 1) // CASILLA GENERAL
					this.matrizMedias[i][j].setcDerecha(new Contador(Math.random() * (canMin - canMax + 1) + canMax, i,j,"D"));
				this.matrizMedias[i][j].setcIzquierda(new Contador(Math.random() * (canMin - canMax + 1) + canMax, i,j,"I"));
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
	private void inicializarContadoresImparMedia(double canMax, double canMin) {
		inicializarContadoresParMedia(canMax, canMin);
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
	 * @return una lista con el id de aquellos contadores que presentan un consumo excesivo (mas del 700% con respecto a la media) 
	 */
	public ArrayList<Integer> resolverConsumidoresGreedy() {
		ArrayList<Contador> candidatos = obtenerCandidatosConsumidores(); // todos los contadores derecha e izquierda
		ArrayList<Integer> elegidos = new ArrayList<>();
		Contador posible;
		ArrayList<Contador> candidatosOrdenados = new ArrayList<>(candidatos);
		candidatosOrdenados.sort(new ComparadorContadores());

		while (!candidatosOrdenados.isEmpty()) { // Solucion: hemos comprobado todos los contadores
			posible = candidatosOrdenados.get(0); 
			candidatosOrdenados.remove(posible); // Eliminamos posible de la lista de candidatos
			if (roturaContador(posible)) {// Devuelve true o false si el contadores tiene una rotura o no
				elegidos.add(posible.getId());
			}
		}
		return elegidos;
	}
	
	public ArrayList<Contador> resolverConsumidoresVersionContadores(){
		ArrayList<Contador> candidatos = obtenerCandidatosConsumidores(); // todos los contadores derecha e izquierda
		ArrayList<Contador> elegidos = new ArrayList<>();
		Contador posible;
		ArrayList<Contador> candidatosOrdenados = new ArrayList<>(candidatos);
		candidatosOrdenados.sort(new ComparadorContadores());

		while (!candidatosOrdenados.isEmpty()) { // Solucion: hemos comprobado todos los contadores
			posible = candidatosOrdenados.get(0); 
			candidatosOrdenados.remove(posible); // Eliminamos posible de la lista de candidatos
			if (roturaContador(posible)) {// Devuelve true o false si el contador tiene una rotura o no
				elegidos.add(posible);
			}
		}
		return elegidos;
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
	public TreeMap<Contador, ArrayList<Double>> manzanasConsumoExcesivo(ArrayList<Integer> cRoturas){
		ArrayList<Integer> contadoresRoturas = new ArrayList<>(cRoturas); //Queremos una copia no editar la otra
		TreeMap<Contador, ArrayList<Double>> result = new TreeMap<>();
		Contador c = null;
		ArrayList<Double> datos;

		for(Integer id : contadoresRoturas) {
			datos = new ArrayList<>();
			c = this.obtenerContadorDadoId(id);
			//Con el id obtenemos el contador, con el contador obtenemos el contador medio
			datos.add(c.getConsumo());
			datos.add(c.getMedia().getConsumo());
			datos.add(c.getConsumo()/c.getMedia().getConsumo());
			
			if(c != null) result.put(c, datos);
		}
		
		return result;
	}
	
	/**
	 * Este metodo es como el manzanasConsumoExcesivo pero para conjunto de roturas aislados que no pertenezcan a un damero. 
	 * Asi, en el test podemos generar casos controlados de roturas
	 * @param cRoturas
	 * @return
	 */
	public TreeMap<Contador, ArrayList<Double>> manzanasConsumoExcesivoParaTest(ArrayList<Contador> cRoturas){
		ArrayList<Contador> contadoresRoturas = new ArrayList<>(cRoturas); //Queremos una copia no editar la otra
		TreeMap<Contador, ArrayList<Double>> result = new TreeMap<>();
		ArrayList<Double> datos = new ArrayList<>();

		for(Contador cont: contadoresRoturas) {
			datos.add(cont.getConsumo());
			datos.add(cont.getMedia().getConsumo());
			datos.add(cont.getConsumo()/cont.getMedia().getConsumo());
			
			if(cont != null) result.put(cont, datos);
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
	public String[] obtenerCoordenadas(Contador con) {
		String[] coordenadas = new String[3];
		boolean encontrado = false;
		String tipo = "";
		for (int i = 0; i < pEdificios.length; i++) {
			if (encontrado) break;
			for (int j = 0; j < pEdificios[0].length; j++) {
				// Le he añadido el metodo equals a la clase Contador
				if (this.pEdificios[i][j].getcDerecha() != null && this.pEdificios[i][j].getcDerecha().equals(con)) {
					encontrado = true;
					tipo = "D";
				}

				if (this.pEdificios[i][j].getcIzquierda() != null && this.pEdificios[i][j].getcIzquierda().equals(con)) {
					encontrado = true;
					tipo = "I";
				}

				if (this.pEdificios[i][j].getcMorado() != null && this.pEdificios[i][j].getcMorado().equals(con)) {
					encontrado = true;
					tipo = "M";
				}

				if (this.pEdificios[i][j].getcVerde() != null && this.pEdificios[i][j].getcVerde().equals(con)) {
					encontrado = true;
					tipo = "V";
				}

				// comprobamos si alguno de los contadores coincide
				if (encontrado) {
					coordenadas[0] = i + "";
					coordenadas[1] = j + "";
					coordenadas[2] = tipo;
					break;
				}
			}
		}
		return coordenadas;
	}
	
	/**
	 * @param coords salida de ObtenerCoordenadas
	 * @return su correspondiente contador media
	 */
	public Contador getContadorMediaDeUnContador(String[] coords) {
		Integer i = Integer.parseInt(coords[0]);
		Integer j = Integer.parseInt(coords[1]);
		String tipo = coords[2];
		
		switch(tipo) {
		case "V":
			return this.matrizMedias[i][j].getcVerde();
		case "M":
			return this.matrizMedias[i][j].getcVerde();
		case "D":
			return this.matrizMedias[i][j].getcDerecha();
		case "I":
			return this.matrizMedias[i][j].getcIzquierda();
		default : throw new RuntimeException("Fallo");
		} 

	}
	
	
	//PROGRAMACION DINAMICA
	
	//HASTA AQUI YA TENEMOS LA LISTA DE LOS CONTADORES 
	//HE BORRADO TODO LO DE LOS MANOMETROS PORQUE PARECE QUE NO SE USA
	
	/**
	 * @return un treemap con cada contador con su rotura
	 */
	public TreeMap<Contador, Double> establecerListaATRoturas(ArrayList<Integer> listaRoturas){ 
		if(listaRoturas.isEmpty()) return null;
		double ip,bc,dt;
		TreeMap<Contador, ArrayList<Double>> manzanasRoturas = this.manzanasConsumoExcesivo(listaRoturas);
		TreeMap<Contador, Double> resultado = new TreeMap<>();

		//Ir recorriendo las manzanas con roturas e ir introduciendo en resultado el contador que produce la rotura con su rotura
		for(Entry<Contador, ArrayList<Double>> c : manzanasRoturas.entrySet()) {
			bc = c.getValue().get(0);
			ip = c.getValue().get(2);
			dt = this.calcularDT(ip, bc);
			c.getKey().setAt(dt+BA);
			resultado.put(c.getKey(), dt+BA);
		}
		
		return resultado;
	}
	
	public TreeMap<Contador, Double> establecerListaATRoturasTest(ArrayList<Contador> listaRoturas){ 
		if(listaRoturas.isEmpty()) return null;
		double ip,bc,dt;
		TreeMap<Contador, ArrayList<Double>> manzanasRoturas = this.manzanasConsumoExcesivoParaTest(listaRoturas);
		TreeMap<Contador, Double> resultado = new TreeMap<>();

		//Ir recorriendo las manzanas con roturas e ir introduciendo en resultado el contador que produce la rotura con su rotura
		for(Entry<Contador, ArrayList<Double>> c : manzanasRoturas.entrySet()) {
			bc = c.getValue().get(0);
			ip = c.getValue().get(2);
			dt = this.calcularDT(ip, bc);
			c.getKey().setAt(dt+BA);
			resultado.put(c.getKey(), dt+BA);
		}
		
		return resultado;
	}
	
	/**
	 * @param ip numero de veces que se ha incrementado con respecto a la media
	 * @param bc consumo ultimo de la manzana
	 * @return el dt
	 */
	private double calcularDT(double ip, double bc) {
		return 5*bc + 12*(ip-7);
	}
	
	/**
	 * Genera un valor OP a cada manzana con fuga 
	 */
	public TreeMap<Contador, Double> generarOP(ArrayList<Integer> listaRoturas) {
		TreeMap<Contador, ArrayList<Double>> manzanasFugas = this.manzanasConsumoExcesivo(listaRoturas);
		TreeMap<Contador, Double> resultado = new TreeMap<>();
		double op;

		for(Contador c : manzanasFugas.keySet()) {  
			op = Math.random() * (1000 - 4000 + 1) + 4000;
			c.setOp(op);
			resultado.put(c, op);
		}
		
		return resultado;
	}
	
	public TreeMap<Contador, Double> generarOPTest(ArrayList<Contador> listaRoturas) {
		TreeMap<Contador, ArrayList<Double>> manzanasFugas = this.manzanasConsumoExcesivoParaTest(listaRoturas);
		TreeMap<Contador, Double> resultado = new TreeMap<>();
		double op;

		for(Contador c : manzanasFugas.keySet()) {  
			op = Math.random() * (1000 - 4000 + 1) + 4000;
			c.setOp(op);
			resultado.put(c, op);
		}
		
		return resultado;
	}
	
	
	/**
	 * Este metodo consiste en buscar la suma maxima de dinero sin superar el tiempo
	 * @return
	 */
	public double[][]  maximizarDineroDadoWTT(ArrayList<Contador> resolverConsumidoresVersionContadores) {
		
		//Parece un problema similar al de la mochila
		//Tenemos una cantidad maxima WTT que son las horas de trabajo
		//Tenemos que ir cogiendo contadores e ir viendo que no se supere WTT y obtener el beneficio (dinero) maximo 
		//El beneficio es OP que es lo que se cobra por cada rotura
		//Las horas de cada contador son el AT. 
		
		
		//Mochila de tamaño WTT 
		//Numero de objetos == numero de contadores con roturas 
		// Beneficio del contador == OP 
		// Peso del contador = AT
		
		
		//PASO 1. Hay que ordenar en funcion del peso
		//PASO 2. Rellenar la tabla d
		
		ArrayList<Contador> listaContadores = new ArrayList<>();
		
		//Este contador es un contador auxiliar. Empezamos a considerar los contadores a partir de la posicion 1
		Contador aux = new Contador(0.0);
		aux.setAt(0.0);

		listaContadores.add(aux);
		listaContadores.addAll(resolverConsumidoresVersionContadores);
		
		if(listaContadores.size() == 1) return null;
		
		int numContadores = listaContadores.size();
		int cota = WTT+1; //Puesto que consideramos el 0
		double[][] table = new double[numContadores][cota];
		
		
		//En esta misma lista tenemos los beneficios y los pesos (estan en las propiedades del contador) 
		//Ordenamos la lista en funcion del peso  (AT)
		listaContadores.sort(new ComparadorContadoresAT());
		
		this.beneficiosYPesos = listaContadores;
		
		//Ahora podemos empezar a rellenar la tabla 
		
		//Primera fila de 0
		for(int i=0; i<cota; i++) {
			table[0][i] = 0;
		}
				
		for(int i=1; i<numContadores; i++) {
			//En el momento en el que encontremos un peso que sea mayor que el tamaño de la mochila lo descartamos
			
			//Si nuestro objeto no cabe en la mochila, tenemos que hacer una copia de la fila de arriba 
			if(listaContadores.get(i).getAt() > cota) { //ESTO NO VIENE EN LOS APUNTES, LO HE INTRODUCIDO YO, SI NO, DA FALLO
				for(int j = 0; j<cota; j++) {
					table[i][j] = table[i-1][j];
				}
				continue;
			}
			
			//Si tenemos un objeto de peso 5, desde j = 0 hasta j = 4 no podemos meterlo, por lo que cogemos lo que ya teniamos metido antes
			for(int j=0; j<listaContadores.get(i).getAt(); j++) {
				table[i][j] = table[i-1][j]; //El de arriba
			}
						
			for(int k = (int)listaContadores.get(i).getAt(); k<cota; k++) {
				//El beneficio de lo que ya teniamos añadiendo el objeto vs sin añadirlo
				double prev = table[i-1][k-(int)listaContadores.get(i).getAt()]+ (int)listaContadores.get(i).getOp();
				table[i][k] = Math.max(prev, table[i-1][k]);
			}
		}
		
		this.table = table;
		return table;		
	}
	
	public String interpretarSolucionMaximizarDineroDadoWTT() {
		String objetos = "HAY QUE ENVIAR  LA OFERTA A: \n";
		String[] coords;
		int numContadores = this.resolverConsumidoresGreedy().size();
		if(numContadores == 0) return null;
		
		ArrayList<Integer> resultado = test(numContadores, Damero.WTT);
		
		for(Integer i : resultado) {
			coords = this.obtenerCoordenadas(this.beneficiosYPesos.get(i));
			objetos += "EL CONTADOR " + this.beneficiosYPesos.get(i) + " UBICADO EN " + "("+ coords[0] + "," + coords[1] + ") \n";
		}
		
		return objetos;
	}

	private ArrayList<Integer> test(int j, double c){
		
		if(j<0) throw new RuntimeException("Debe introducir un indice valido.");
		
		if(j>0) {
			if(c<this.beneficiosYPesos.get(j).getAt()) { //Si el objeto j no cabe en la mochila
				test(j-1, c); //Probamos con un objeto de peso menor
			} else {
				//Esto esta saliendo mal porque al hacer el casting no es el indice al que queremos acceder. Mañana intento arreglarlo. 
				//Intentare que todos nuestros datos sean enteros
				if(this.table[j-1][(int)(c-this.beneficiosYPesos.get(j).getAt())] + this.beneficiosYPesos.get(j).getOp() > this.table[j-1][(int)c]) {
					//Si hay beneficio con este objeto, entonces es solucion
					//Llamamos a un objeto mas pequeño y al añadir el objeto a la mochila ahora tenemos menos capacidad
					this.listaClientesA.add(j);
					test(j-1, c-this.beneficiosYPesos.get(j).getAt());
				} else {
					//Si no hay beneficio, no lo metemos y probamos con uno mas pequeño tambien
					test(j-1,c);
				}
			}
		}

		return this.listaClientesA; 
	}
}