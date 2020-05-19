package org.eda2.dynamic;

import java.util.ArrayList;
import java.util.PriorityQueue;

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
	
	public final static double WTT = 80; //horas 
	public final static double OP = 50000; //euros

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
		this.inicializarContadores();
	}

	/**
	 * 
	 * @return un array con la linea troncal
	 */
	public ParEdificios[] lineaTroncal() {
		ParEdificios[] pE = new ParEdificios[pEdificios.length];
		for (int i = 0; i < pE.length; i++)
			pE[i] = pEdificios[i][pEdificios[0].length - 1];
		return pE;
	}

	/**
	 * 
	 * @return un array con las medias de la linea troncal
	 */
	public ParEdificios[] lineaTroncalMedia() {
		ParEdificios[] pE = new ParEdificios[pEdificios.length];
		for (int i = 0; i < pE.length; i++)
			pE[i] = matrizMedias[i][pEdificios[0].length - 1];
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
	public void setParEdificio(int i, int j, Contador c, String tipo) {
		switch(tipo) {
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
					this.matrizMedias[i][j].setcDerecha(new Contador(Math.random() * (100 - 1000 + 1) + 1000));
				this.matrizMedias[i][j].setcIzquierda(new Contador(Math.random() * (100 - 1000 + 1) + 1000));
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
					this.matrizMedias[i][j].setcDerecha(new Contador(con)); // CONTADOR GENERAL
					continue;
				}
				if (j == this.matrizMedias[0].length - 1 && i != this.matrizMedias.length - 1) { // linea de distribucion
					con += this.matrizMedias[i][j - 1].getcVerde().getConsumo();
					con += this.matrizMedias[i][j].getcDerecha().getConsumo();
					con += this.matrizMedias[i][j].getcIzquierda().getConsumo();
					if (i != 0)
						con += this.matrizMedias[i - 1][j].getcMorado().getConsumo();
					this.matrizMedias[i][j].setcMorado(new Contador(con));
				} else if (this.matrizMedias[i][j - 1].getcVerde() == null) { // final de la linea de distribucion por abajo
					con += this.matrizMedias[i][j].getcDerecha().getConsumo();
					con += this.matrizMedias[i][j].getcIzquierda().getConsumo();
					con += this.matrizMedias[i][j - 1].getcDerecha().getConsumo();
					con += this.matrizMedias[i][j - 1].getcIzquierda().getConsumo();
					this.matrizMedias[i][j].setcVerde(new Contador(con));
				} else { // caso base
					con += this.matrizMedias[i][j].getcDerecha().getConsumo();
					con += this.matrizMedias[i][j].getcIzquierda().getConsumo();
					con += this.matrizMedias[i][j - 1].getcVerde().getConsumo();
					this.matrizMedias[i][j].setcVerde(new Contador(con));
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
	 * LLama al metodo Par o Impar segun el caso
	 */
	private void inicializarContadores() {
		if (columnas % 2 == 0)
			inicializarContadoresPar();
		else
			inicializarContadoresImpar();
	}

	/**
	 * Inicializa los datos de los contadores en el caso de que la ciudad tenga
	 * columnas pares
	 */
	private void inicializarContadoresPar() {
		// RECORREMOS EL ARRAY INICIALIZANDO LOS CONTADORES ROJOS
		for (int i = 0; i < this.pEdificios.length; i++) {
			for (int j = 0; j < this.pEdificios[0].length; j++) {
				if (i != this.pEdificios.length - 1 || j != this.pEdificios[0].length - 1) // CASILLA GENERAL
					this.pEdificios[i][j].setcDerecha(new Contador(Math.random() * (100 - 1000 + 1) + 1000, this.matrizMedias[i][j].getcDerecha()));
				this.pEdificios[i][j].setcIzquierda(new Contador(Math.random() * (100 - 1000 + 1) + 1000,this.matrizMedias[i][j].getcIzquierda()));
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
					this.pEdificios[i][j].setcDerecha(new Contador(con,this.matrizMedias[i][j].getcDerecha())); // CONTADOR GENERAL
					continue;
				}
				if (j == this.pEdificios[0].length - 1 && i != this.pEdificios.length - 1) { // linea de distribucion
					con += this.pEdificios[i][j - 1].getcVerde().getConsumo();
					con += this.pEdificios[i][j].getcDerecha().getConsumo();
					con += this.pEdificios[i][j].getcIzquierda().getConsumo();
					if (i != 0)
						con += this.pEdificios[i - 1][j].getcMorado().getConsumo();
					this.pEdificios[i][j].setcMorado(new Contador(con, this.matrizMedias[i][j].getcMorado()));
				} else if (this.pEdificios[i][j - 1].getcVerde() == null) { // final de la linea de distribucion por abajo
					con += this.pEdificios[i][j].getcDerecha().getConsumo();
					con += this.pEdificios[i][j].getcIzquierda().getConsumo();
					con += this.pEdificios[i][j - 1].getcDerecha().getConsumo();
					con += this.pEdificios[i][j - 1].getcIzquierda().getConsumo();
					this.pEdificios[i][j].setcVerde(new Contador(con, this.matrizMedias[i][j].getcVerde()));
				} else { // caso base
					con += this.pEdificios[i][j].getcDerecha().getConsumo();
					con += this.pEdificios[i][j].getcIzquierda().getConsumo();
					con += this.pEdificios[i][j - 1].getcVerde().getConsumo();
					this.pEdificios[i][j].setcVerde(new Contador(con, this.matrizMedias[i][j].getcVerde()));
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

	/**
	 * PROBLEMA a.2
	 * @return una lista con el id de los contadores que presentan una rotura propia
	 */
	public ArrayList<Integer> resolverContadoresRoturaPropiaGreedy() {
		ArrayList<Contador> candidatos = obtenerCandidatosContadores(); // todos los contadores
		ArrayList<Integer> elegidos = new ArrayList<>();
		Contador posible;
		
		//Para no tener que ir buscando el mayor en cada iteracion (busqueda es O(n) dentro del while O(n) ==> O(n2), vamos a darlos directamente ordenados
		//en funcion del criterio elegido y ya solo es ir cogiendo el primero 
		//Pasamos de n2 a n
		
		PriorityQueue<Contador> candidatosOrdenados = new PriorityQueue<>(new ComparadorContadores());
		
		for(Contador c : candidatos) {
			candidatosOrdenados.offer(c);
		}
		
		//Hemos sustituido la funcion de seleccion por una cola de prioridad que contiene los contadores ordenados por el criterio que 
		//hemos elegido (el criterio de la funcion de seleccion)
		
		while (candidatos.size() != 0) { // Solucion: hemos comprobado todos los contadores
//			posible = contadorMayorGasto(candidatos);// Funcion de seleccion
			posible = candidatosOrdenados.poll(); 
			candidatos.remove(posible); // Eliminamos posible de la lista de candidatos
			//El rotura propia falla
			if (roturaPropia(posible)) {// Devuelve true o false si el contador tiene una rotura propia o no
				elegidos.add(posible.getId());
			}
		}
		return elegidos;
	}

	/**
	 * @param candidatos lista de candidatos del que vamos a coger aquel 
	 * @return
	 */
	public Contador contadorMayorGasto(ArrayList<Contador> candidatos) {
		double diferencia = -1;
		Contador resultado = new Contador();
		for (Contador c : candidatos) {
			String[] coordenadas = obtenerCoordenadas(c);
			int i = Integer.parseInt(coordenadas[0]);
			int j = Integer.parseInt(coordenadas[1]);
			double consumoPE = pEdificios[i][j].getContador(coordenadas[2]).getConsumo();
			double consumoMedio = matrizMedias[i][j].getContador(coordenadas[2]).getConsumo();
			if ((consumoPE - consumoMedio) > diferencia) {
				diferencia = consumoPE - consumoMedio;
				resultado = c;
			}
		}
		return resultado;
	}

	/**
	 * @param con contador del que queremos ver si tiene una rotura propia y consumo
	 *            mayor de 5xmedia
	 * @return true si presenta las condiciones anteriormente dichas, false en caso
	 *         contrario
	 */
	public boolean roturaPropia(Contador con) { // Funcion de factibilidad
		String[] indices = obtenerCoordenadas(con);
		int i = Integer.parseInt(indices[0]); //Fallo parseInt
		int j = Integer.parseInt(indices[1]);
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
	 * @param con contador que queremos ubicar
	 * @return (i,j, tipo) donde el tipo es Derecha, Izquierda, Verde, Morado
	 */
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

	/**
	 * @return una lista con todos los candidatos a ser estudiados. En nuestro caso,
	 *         todos los contadores de nuestra red (verdes y morados)
	 */
	public ArrayList<Contador> obtenerCandidatosContadores() {
		ArrayList<Contador> candidatos = new ArrayList<>();

		for (int i = 0; i < this.pEdificios.length; i++) {
			for (int j = 0; j < this.pEdificios[i].length; j++) {
				if (this.pEdificios[i][j].getcMorado() != null)
					candidatos.add(this.pEdificios[i][j].getcMorado());
				if (this.pEdificios[i][j].getcVerde() != null)
					candidatos.add(this.pEdificios[i][j].getcVerde());
				if (i == this.pEdificios.length - 1 && j == this.pEdificios[0].length - 1)
					candidatos.add(this.pEdificios[i][j].getcDerecha());
			}
		}
		return candidatos;
	}


	// b
	/*
	 * PSEUDOCODIGO candidatos elegidos posible
	 * 
	 * mientras candidatos.length != 0 posible = manometro con menor presion elimina
	 * posible de candidatos
	 * 
	 * si el manometro tiene una rotura a�ade posible a elegidos fsi finmientras
	 * rettorna elegidos
	 * 
	 * 
	 * Candidatos: Todos los contadores rojos Soluci�n: hemos comprobado todos los
	 * candidatos Condici�n de factibilidad: el contador tiene una rotura (+700%)
	 * Funci�n de selecci�n: contador con mayor gasto/contador con mayor gasto
	 * respecto a su media Funci�n objetivo: Comprobar todos los candidatos
	 * 
	 */

	public ArrayList<Contador> resolverConsumidoresGreedy() {
		ArrayList<Contador> candidatos = obtenerCandidatosConsumidores(); // todos los manometros
		ArrayList<Contador> elegidos = new ArrayList<>();
		Contador posible;
		PriorityQueue<Contador> candidatosOrdenados = new PriorityQueue<>(new ComparadorContadores());

		for(Contador c : candidatos) {
			candidatosOrdenados.offer(c);
		}

		while (!candidatos.isEmpty()) { // Solucion: hemos comprobado todos los manometros
			posible = candidatosOrdenados.poll(); 
//			posible = contadorMayorGasto(candidatos);// Funci�n de selecci�n
			candidatos.remove(posible); // Eliminamos posible de la lista de candidatos
			if (roturaContador(posible)) {// Devuelve true o false si el manometro tiene una rotura o no
				elegidos.add(posible);
			}
		}
		return elegidos;
	}

	public ArrayList<Contador> obtenerCandidatosConsumidores() { //?? No los esta cogiendo todos?
		ArrayList<Contador> candidatos = new ArrayList<>();
		for (int i = 0; i < this.pEdificios.length; i++) {
			for (int j = 0; j < this.pEdificios[0].length; j++) {
				if (this.pEdificios[i][j].getcDerecha() != null) {
					if (i != this.pEdificios.length - 1 || j != this.pEdificios[0].length - 1) {
						candidatos.add(this.pEdificios[i][j].getcDerecha());
					}
				}
				if (this.pEdificios[i][j].getcIzquierda() != null)
					candidatos.add(this.pEdificios[i][j].getcIzquierda());
			}
		}
		return candidatos;
	}

	public boolean roturaContador(Contador posible) {
		String[] coordenadas = obtenerCoordenadas(posible);
		int i = Integer.parseInt(coordenadas[0]);
		int j = Integer.parseInt(coordenadas[1]);
		double consumo = posible.getConsumo();
		double consumoMedio = this.matrizMedias[i][j].getContador(coordenadas[2]).getConsumo();
		if (consumo > consumoMedio * 7) return true;
		return false;
	}
	
	
	
	//HASTA AQUI YA TENEMOS LA LISTA DE LOS CONTADORES 
	//HE BORRADO TODO LO DE LOS MANOMETROS PORQUE PARECE QUE NO SE USA
	
	/**
	 * @return una lista con el tamaño de cada fuga
	 */
	public ArrayList<Double> listaDTRoturas(){ //Esto va a salir muy poco eficiente
		ArrayList<Contador> listaRoturas = this.resolverConsumidoresGreedy();
		if(listaRoturas.isEmpty()) return null;
		ArrayList<Double> dt = new ArrayList<Double>();
		String[] coords;
		Contador media;
		double ip;
		double bc;
		Integer i,j;

		//Para cada contador que tenga rotura, vamos a buscar su correspondiente contador de la matriz de medias -> ip
		//buscamos tambien su contador previo -> bc
		
		for(Contador c : listaRoturas) {  //CREO Q LO DE LAS COORDENADAS ESTÁ MAL, EL PREVIO NO ES J-1, ES J+1 
			coords = this.obtenerCoordenadas(c);
			media = getContadorMediaDeUnContador(coords); //aqui ya va O(n3)
			ip = c.getConsumo()/media.getConsumo();
			//¿EL CONSUMO ULTIMO ES EL CONSUMO DEL CONTADOR QUE HA PROVOCADO LA ROTURA O DEL PREVIO?? 
			//SUPONGO QUE DEL PREVIO ASIQ...
			i = Integer.parseInt(coords[0]);
			
			
			//ESTO CREO Q ESTÁ MAL, SERÍA +1
			j = Integer.parseInt(coords[1])-1; //EL DE ARRIBA
			//SUPONGO QUE SI LA ROTURA ESTABA EN UN CONTADOR VERDE TENEMOS QUE GUARDAR EL ANTERIOR TAMBIÉN VERDE SUPONGO
			
			switch(coords[2]) {
				case "V":
					bc = this.pEdificios[i][j].getcVerde().getConsumo();
					break;
				case "M":
					//En el morado en realidad seria el de la izquierda, asiq vuelvo a la j suya y cambio la i
					bc = this.pEdificios[i-1][j+1].getcMorado().getConsumo();
					break;
				case "I":
					bc = this.pEdificios[i][j].getcIzquierda().getConsumo();
					break;
				case "D":
					bc = this.pEdificios[i][j].getcDerecha().getConsumo();
					break;
				default: throw new RuntimeException("Tipo erróneo");
			}
			
			dt.add(this.calcularDT(ip, bc));
		}
		
		return dt;
		
	}
	
	/**
	 * @param ip numero de veces que se ha incrementado con respecto a la media
	 * @param bc consumo ultimo de la manzana
	 * @return el dt
	 */
	public double calcularDT(double ip, double bc) {
		return 5*bc + 12*(ip-7); //AQUI PODRIAMOS AÑADIR DIRECTAMENTE EL TIEMPO BASE
	}
	

}