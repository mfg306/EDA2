package org.eda2.greedy;

import java.util.ArrayList;

import java.util.TreeMap;

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
	private void inicializarContadoresPar(ParEdificios[][] pE) { // Deberiamos inicializar primero los de cada edificio y a partir de ese

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
		double x;
		
		for (int j = pEdificios[0].length - 1; j >= 1; j--) {
			for (int i = pEdificios.length - 1; i >= 0; i--) {
				if (i == ancho && j == alto) { // ManÃ³metro general
					this.pEdificios[i][j].setMan(new Manometro(Math.random() * (110 - 150 + 1) + 150));
					continue;
				}
				// Obtenemos el valor de la presion del manometro anterior para obtener el
				// siguiente a partir de Ã©l
				if (j == alto) pAnterior = this.pEdificios[i + 1][j].getMan().getPresion();
				else pAnterior = this.pEdificios[i][j + 1].getMan().getPresion();

				//Voy a generar el '13' de forma aleatoria para buscar reventones jeje
				x = Math.random() * (13 - 55 + 1) + 55; //cantidad a disminuir
				
				//si se ha perdido mÃ¡s de un 50%, entonces es porque ha habido algun problema tecnico en el 
				//manometro i, y no ha podido llegar la presion al manometro i+1
				
				if(x > 50) { //INVOCANDO EL CASO DEL REVENTON
					this.pEdificios[i][j].setMan(new Manometro(0.0));
				} else {
					error = pAnterior - (pAnterior * x / 100); //presionAnterior disminuida una cantidad x/100

//					error = pAnterior - (pAnterior * 13 / 100); // Margen de error del manometro
					this.pEdificios[i][j].setMan(new Manometro((Math.random() * (error - pAnterior + 1) + pAnterior)));
				}
				

			}
		}
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
		 * Solución: hemos comprobado todos los candidatos
		 * Condición de factibilidad: el contador tiene una rotura propia
		 * Función de selección: mayor gasto/mayor diferencia con la media/
		 * Función objetivo: Comprobar todos los contadores
		 * 
		 */
		public ArrayList<Contador> resolverContadoresGreedy() {
			ArrayList<Contador> candidatos = obtenerCandidatosContadores(); //todos los contadores
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
		
		
//		public Contador contadorMayorGasto(ArrayList<Contador> candidatos) {
//			double max = -1;
//			Contador maximo = new Contador();
//			for(Contador c: candidatos) {
//				if (c.getConsumo()>max) {
//					max = c.getConsumo();
//					maximo = c;
//				}
//			}
//			return maximo;
//		}
		
		public Contador contadorMayorGasto(ArrayList<Contador> candidatos) {
			double diferencia = -1;
			Contador resultado = new Contador();
			for (Contador c: candidatos) {
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
			String tipo = "";
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
		public ArrayList<Contador> obtenerCandidatosContadores(){
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
		
		
		//1a Manometros con un consumo mayor a 10%
		/*PSEUDOCODIGO
		 * candidatos
		 * elegidos
		 * posible
		 * 
		 * mientras candidatos.length != 0
		 * 		posible = manometro con menor presion
		 * 		elimina posible de candidatos
		 * 
		 * 		si el manometro tiene una rotura
		 * 			añade posible a elegidos
		 * 		fsi
		 * finmientras
		 * rettorna elegidos
		 *  
		 * 
		 * Candidatos: Todos los manometros
		 * Solución: hemos comprobado todos los candidatos
		 * Condición de factibilidad: el manometro tiene una rotura propia
		 * Función de selección: manometro de menor presion/mayor diferencia con el siguiente
		 * Función objetivo: Comprobar todos los manometros
		 * 
		 */
		
		public ArrayList<Manometro> resolverManometrosGreedy() {
			ArrayList<Manometro> candidatos = obtenerCandidatosManometros(); //todos los manometros
			ArrayList<Manometro> elegidos = new ArrayList<>();
			Manometro posible;
			
			while(!candidatos.isEmpty()) { //Solucion: hemos comprobado todos los manometros
				
				posible = manometroMenorPresion(candidatos);//Función de selección
				candidatos.remove(posible); //Eliminamos posible de la lista de candidatos
				if(roturaManometro(posible)) {//Devuelve true o false si el manometro tiene una rotura o no
					elegidos.add(posible);
				}
			}
			return elegidos;
		}
		
		public ArrayList<Manometro> obtenerCandidatosManometros(){
			ArrayList<Manometro> candidatos = new ArrayList<>();
			for (int i = 0;i<this.pEdificios.length;i++) 
				for (int j = 0;j<this.pEdificios[0].length;j++) {
					if (this.pEdificios[i][j].getMan() != null) candidatos.add(this.pEdificios[i][j].getMan());
				}
			return candidatos;
		}
		
		public Manometro manometroMenorPresion(ArrayList<Manometro> candidatos) {
			Manometro posible = new Manometro(Double.MAX_VALUE);
			for (Manometro m : candidatos) {
				if (m.getPresion() < posible.getPresion()) {
					posible = m;
				}
			}
			return posible;
		}
		
		public boolean roturaManometro(Manometro posible) { //Comprobamos si hay una rotura entre el manometro posible i el anterior
			int[] coordenadas = obtenerCoordenadasManometro(posible);
			int i = coordenadas[0];
			int j = coordenadas[1];
			Manometro anterior = new Manometro();
			if (j == this.pEdificios[0].length-1) { //TRONCAL FALLO ENTRE [i,j] Y [i+1,j]
				if(i==this.pEdificios.length-1) return false;
				anterior = this.pEdificios[i+1][j].getMan();
			} else { //DISTRIBUCION FALLO ENTRE [i,j] Y [i,j+1]
			 anterior = this.pEdificios[i][j+1].getMan();
			}
			if (posible.getPresion() < (anterior.getPresion()-anterior.getPresion()*0.1))
				return true;
			return false;
			
		}
		
		public int[] obtenerCoordenadasManometro(Manometro posible) {
			int[] coordenadas = new int[2];
			for (int i = 0;i<this.pEdificios.length;i++) {
				for (int j = 0;j<this.pEdificios[0].length;j++) {
					if (this.pEdificios[i][j].getMan()!=null) {
						if (this.pEdificios[i][j].getMan().getId() == (posible.getId())) {
							coordenadas[0] = i;
							coordenadas[1] = j;
							i = this.pEdificios.length;
							break;
						}
					}
				}
			}
			return coordenadas;
		}
		
		
		//b
		/*PSEUDOCODIGO
		 * candidatos
		 * elegidos
		 * posible
		 * 
		 * mientras candidatos.length != 0
		 * 		posible = manometro con menor presion
		 * 		elimina posible de candidatos
		 * 
		 * 		si el manometro tiene una rotura
		 * 			añade posible a elegidos
		 * 		fsi
		 * finmientras
		 * rettorna elegidos
		 *  
		 * 
		 * Candidatos: Todos los contadores rojos
		 * Solución: hemos comprobado todos los candidatos
		 * Condición de factibilidad: el contador tiene una rotura (+700%)
		 * Función de selección: contador con mayor gasto/contador con mayor gasto respecto a su media
		 * Función objetivo: Comprobar todos los candidatos
		 * 
		 */
		
		public ArrayList<Contador> resolverConsumidoresGreedy() {
			ArrayList<Contador> candidatos = obtenerCandidatosConsumidores(); //todos los manometros
			ArrayList<Contador> elegidos = new ArrayList<>();
			Contador posible;
			
			while(!candidatos.isEmpty()) { //Solucion: hemos comprobado todos los manometros
				
				posible = contadorMayorGasto(candidatos);//Función de selección
				candidatos.remove(posible); //Eliminamos posible de la lista de candidatos
				if(roturaContador(posible)) {//Devuelve true o false si el manometro tiene una rotura o no
					elegidos.add(posible);
				}
			}
			return elegidos;
		}
		
		public ArrayList<Contador> obtenerCandidatosConsumidores(){
			ArrayList<Contador> candidatos = new ArrayList<>();
			for (int i = 0;i<this.pEdificios.length;i++) {
				for (int j = 0;j<this.pEdificios[0].length;j++){
					if (this.pEdificios[i][j].getcDerecha() != null) {
						if (i!=this.pEdificios.length-1 || j!=this.pEdificios[0].length-1) {
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
			if (consumo > consumoMedio*7) return true;
			return false;
		}	

	
	
	
}