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
		double p;
		for (int i = 0; i < pEdificios.length; i++) {
			for (int j = 1; j < pEdificios[0].length; j++) {
				//Para cada contador tenemos que ver de forma aleatoria si tiene rotura
				p = Math.random();
				if (i == pEdificios.length - 1 && j == pEdificios[0].length - 1) {
					con += pEdificios[i][j].getcIzquierda().getConsumo();
					con += pEdificios[i][j - 1].getcVerde().getConsumo();
					con += pEdificios[i - 1][j].getcMorado().getConsumo();
					if (p>0.6) {
						con += 10000;
						System.out.println(i+" "+j);
					}
					this.pEdificios[i][j].setcDerecha(new Contador(con)); // CONTADOR GENERAL
					continue;
				}
				if (j == pEdificios[0].length - 1 && i != pEdificios.length - 1) { // linea de distribucion
					con += pEdificios[i][j - 1].getcVerde().getConsumo();
					con += pEdificios[i][j].getcDerecha().getConsumo();
					con += pEdificios[i][j].getcIzquierda().getConsumo();
					if (i != 0)
						con += pEdificios[i - 1][j].getcMorado().getConsumo();
					if (p>0.6) {
						System.out.println(i+" "+j);
						con += 10000;
					}
					pEdificios[i][j].setcMorado(new Contador(con)); //EL CONTADOR MORADO A VECES SALE COMO RESULTADO SIN ENTRAR EN P
				} else if (pEdificios[i][j - 1].getcVerde() == null) { // final de la linea de distribucion por abajo
					con += pEdificios[i][j].getcDerecha().getConsumo();
					con += pEdificios[i][j].getcIzquierda().getConsumo();
					con += pEdificios[i][j - 1].getcDerecha().getConsumo();
					con += pEdificios[i][j - 1].getcIzquierda().getConsumo();
					if (p>0.6) {
						System.out.println(i+" "+j);
						con += 10000;
					}
					pEdificios[i][j].setcVerde(new Contador(con));
				} else { // caso base
					con += pEdificios[i][j].getcDerecha().getConsumo();
					con += pEdificios[i][j].getcIzquierda().getConsumo();
					con += pEdificios[i][j - 1].getcVerde().getConsumo();
					if (p>0.6) {
						con += 10000;
						System.out.println(i+" "+j);
					}
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
	public String toStringMedias() {
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
	
		/*En este ejemplo tenemos el problema de que estamos simulando una solución aleatoria en la que no sabemos donde se 
		 * produciran las roturas, ni siquiera sabemos si habrá alguna. Por lo tanto, tenemos que definir los elementos del 
		 * problema de la siguiente forma: 
		 * - Conjunto de candidatos: todos los contadores del tablero (CREO QUE LOS ROJOS NO) --> LOS PODEMOS ALMACENAR EN UN ARRAY
		 * - Solucion parcial: seran todos aquellos contadores que presenten una rotura
		 * - Funcion de seleccion: elegimos a aquellos contadores que presenten una rotura / CONTADORES CON MAYOR GASTO DENTRO DE LOS QUE TIENEN ROTURA
		 * - Funcion de factibilidad: en realidad, creo que no nos haria falta, pero por seguir el esquema del Greedy podemos hacer 
		 * que nos verifique que al añadir el contador, el conjunto de todos los contadores de la solucion nuestra presentan roturas / LO VEO BIEN
		 * SEGUN EL EJEMPLO DE LAS MONEDAS, PODRIA SER COMPROBAR QUE TODAVIA NO HEMOS MIRADO TODOS LOS CONTADORES
		 * - Funcion de solucion: como tenemos una situacion aleatoria, no sabemos realmente cual debe ser la solucion, por lo que esto
		 * no podemos incluirlo. PUEDE SER COMPROBAR QUE EL ARRAY DE CANDIDATOS ESTA VACIO?*/
		
		//Conjunto de candidatos -> ArrayList porq la busqueda es O(1), en vez de O(n²) en nuestra matriz
		
		
	//ALEX
	/*PSEUDOCODIGO
	 * arrayListContadores
	 * elegidos
	 * 
	 * mientras contadores.length != 0
	 * 		x = contador con mayor consumo
	 * 		elimina x de candidatos
	 * 
	 * 		si el contador tiene una rotura && la rotura es propia
	 * 			añade x a elegidos
	 * 		fsi
	 * finmientras
	 * rettorna elegidos
	 *  
	 * 
	 * Candidatos: Contadores verdes y morados
	 * Solución: hemos comprobado todos los contadores
	 * Condición de factibilidad: el contador tiene una rotura propia
	 * Función de selección: mayor gasto
	 * Función objetivo: Comprobar todos los contadores
	 * 
	 */
	public ArrayList<Contador> resolverContadoresGreedy() {
		ArrayList<Contador> candidatos = obtenerCandidatos(); //todos los contadores
		ArrayList<Contador> elegidos = new ArrayList<>();
		Contador posible;
		
		while(candidatos.size()!=0) { //Solucion: hemos comprobado todos los contadores
			
			posible = contadorMayorGasto(candidatos);//Función de selección
			candidatos.remove(posible); //Eliminamos posible de la lista de candidatos
			if(roturaPropia(posible)) {//Devuelve true o false si el contador tiene una rotura propia o no
				elegidos.add(posible);
			}
		}
		return elegidos;
	}
	
	
	//OPCION 1
//	public Contador contadorMayorGasto(ArrayList<Contador> candidatos) {
//		double max = -1;
//		Contador maximo = new Contador();
//		for(Contador c: candidatos) {
//			if (c.getConsumo()>max) {
//				max = c.getConsumo();
//				maximo = c;
//			}
//		}
//		return maximo;
//	}
	
	//OPCION 2
	
	public Contador contadorMayorDiferencia(ArrayList<Contador> candidaots) {
		for(int i=0; i<)
	}
	
	
	//Devuelve true si el contador tiene una rotura y es propia
	public boolean roturaPropia(Contador con) {
		String[] indices = obtenerCoordenadas(con);
		int i = Integer.parseInt(indices[0]);
		int j = Integer.parseInt(indices[1]);
		if (!comprobarPropia(con, indices)) return false; //Si la rotura no es del propio contador, ya no nos interesa
		
		Contador media = new Contador();
		switch(indices[2]) {
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
		//comprueba si el consumo es mayor a la media en 5 veces
		if(consumo > mediaCon*5) return true;
		return false;
	}
	
	//Devuelve true si la rotura del contador es suya y false si es de alguno de los contiguos
	public boolean comprobarPropia(Contador con, String[] indices) {
		int i = Integer.parseInt(indices[0]);
		int j = Integer.parseInt(indices[1]);
		if (indices[2].equals("V")) {
			if (j == 1) { //Final de la linea de distribucion
				double verde = this.pEdificios[i][j].getcVerde().getConsumo();
				double dcha = this.pEdificios[i][j].getcDerecha().getConsumo();
				double izqda = this.pEdificios[i][j].getcIzquierda().getConsumo();
				double abajoD = this.pEdificios[i][j-1].getcDerecha().getConsumo();
				double abajoI = this.pEdificios[i][j-1].getcIzquierda().getConsumo();
				if (verde > (dcha+izqda+abajoD+abajoI)) return true;
			} else { //Caso más común
				double verde = this.pEdificios[i][j].getcVerde().getConsumo();
				double dcha = this.pEdificios[i][j].getcDerecha().getConsumo();
				double izqda = this.pEdificios[i][j].getcIzquierda().getConsumo();
				double verde2 = this.pEdificios[i][j-1].getcVerde().getConsumo();
				if (verde > (dcha+izqda+verde2)) return true;
			}
		} else if (indices[2].equals("M")){
			double morado = this.pEdificios[i][j].getcMorado().getConsumo();
			double dcha = this.pEdificios[i][j].getcDerecha().getConsumo();
			double verde = this.pEdificios[i][j-1].getcVerde().getConsumo();
			double izquierda = 0;
			if (this.pEdificios[i][j].getcIzquierda() != null) {
				izquierda = this.pEdificios[i][j].getcIzquierda().getConsumo();
			}
			double moradoI = 0;
			if (i>0 && this.pEdificios[i-1][j].getcMorado() != null) {
				moradoI = this.pEdificios[i-1][j].getcMorado().getConsumo();
			}
			if (morado > (dcha+izquierda+verde+moradoI)) return true;
			
		} else { //Casilla general
			double izquierda = this.pEdificios[i][j].getcIzquierda().getConsumo();
			double derecha = this.pEdificios[i][j].getcDerecha().getConsumo();//general
			double moradoI = this.pEdificios[i-1][j].getcMorado().getConsumo();
			double verdeAbajo = this.pEdificios[i][j-1].getcVerde().getConsumo();
			if (derecha > (izquierda+moradoI+verdeAbajo)) return true;
		}
		return false;
	}
	
	public String[] obtenerCoordenadas(Contador con) {
		String[] coordenadas = new String[3];
		boolean encontrado = false;
		String tipo = "";;
		for (int i = 0;i<pEdificios.length;i++) {
			if(encontrado) break;
			for (int j = 0;j<pEdificios[0].length;j++) {
				if(this.pEdificios[i][j].getcDerecha() != null && this.pEdificios[i][j].getcDerecha().getConsumo() == (con.getConsumo())){
					encontrado = true;
					tipo = "D";
				}
				if(this.pEdificios[i][j].getcIzquierda() != null && this.pEdificios[i][j].getcIzquierda().getConsumo() == (con.getConsumo())){
					encontrado = true;
					tipo = "I";
				}
				if(this.pEdificios[i][j].getcVerde() != null && this.pEdificios[i][j].getcVerde().getConsumo() == (con.getConsumo())){
					encontrado = true;
					tipo = "V";
				}
				if(this.pEdificios[i][j].getcMorado() != null && this.pEdificios[i][j].getcMorado().getConsumo() == (con.getConsumo())){
					encontrado = true;
					tipo = "M";
				}
				//comprobamos si alguno de los contadores coincide
				if(encontrado) {
					coordenadas[0] = i+"";
					coordenadas[1] = j+"";
					coordenadas[2] = tipo;
					break;
				}
			}
		}
		return coordenadas;
	}
	
	/**
	 * @return una lista con todos los candidatos a ser estudiados. En nuestro caso, todos los contadores de nuestra red (verdes y morados)
	 */
	public ArrayList<Contador> obtenerCandidatos(){
		ArrayList<Contador> candidatos = new ArrayList<>();
		
		for(int i=0; i<this.pEdificios.length; i++) {
			for(int j=0; j<this.pEdificios[i].length; j++) {
				if (this.pEdificios[i][j].getcMorado() != null) 
					candidatos.add(this.pEdificios[i][j].getcMorado());
				if (this.pEdificios[i][j].getcVerde() != null) 
					candidatos.add(this.pEdificios[i][j].getcVerde());
				if (i==this.pEdificios.length-1 && j == this.pEdificios[0].length-1) 
					candidatos.add(this.pEdificios[i][j].getcDerecha());
			}
		}
		return candidatos;
	}
}