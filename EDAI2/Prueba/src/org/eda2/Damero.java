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
	private ParEdificios[] lineaTroncal;


	public final static double CONSUMO_MINIMO = 108;
	public final static double CONSUMO_MAXIMO = 162;

	/**
	 * Cantidad de agua que suministramos en la casilla general
	 */
	private double suministroAgua;

	/**
	 * Nuestro Damero viene representado por pares de calles.
	 * 
	 * @param filas    el número de avenidas de nuestra ciudad
	 * @param columnas el número de calles de nuestra ciudad
	 */
	public Damero(int filas, int columnas) {
		this.filas = filas;
		this.columnas = columnas;
		if (filas % 2 == 0) {
			pEdificios = new ParEdificios[filas / 2][columnas]; // i vale la mitad porque unimos dos columnas
			matrizMedias = new ParEdificios[filas / 2][columnas];
		} else {
			pEdificios = new ParEdificios[(filas + 1) / 2][columnas];
			matrizMedias = new ParEdificios[(filas + 1) / 2][columnas];
		}
		for (int a = 0; a < pEdificios.length; a++) {
			for (int b = 0; b < pEdificios[a].length; b++) {
				pEdificios[a][b] = new ParEdificios();
				matrizMedias[a][b] = new ParEdificios();
			}
		}

		inicializarContadores();
		this.lineaTroncal = this.lineaTroncal(); 
	}
	
	
	/**
	 * @return la línea troncal de nuestro damero
	 */
	public ParEdificios[] lineaTroncal() {
		int j = this.pEdificios[0].length - 1;
		ParEdificios[] pE = new ParEdificios[this.pEdificios.length];
		
			for(int i=0; i<this.pEdificios.length; i++) {
				pE[i] = this.pEdificios[i][j];
			}
		
		return pE;
	}
	

	// CONTADORES

	private void inicializarContadores() { 
		for (int i = 0; i < this.pEdificios[0].length; i++) {
			for (int j = 0; j < this.pEdificios.length; j++) {
				
				
				//En todas las casillas vamos a tener un cIzquierda y un cDerecha
				//Solo va a haber cMorado en la última fila
				//Los verdes van a estar en todas las filas menos la primera y la ultima

				//Inicializamos los datos actuales
				this.pEdificios[j][i].setcDerecha(new Contador());
				this.pEdificios[j][i].setcIzquierda(new Contador());
				if(i == this.pEdificios.length - 1) this.pEdificios[j][i].setcMorado(new Contador());
				if(i != 0 || i == this.pEdificios[0].length - 1) this.pEdificios[j][i].setcVerde(new Contador());

				if(this.filas % 2 != 0) {
					if(i == this.pEdificios[0].length - 1) this.pEdificios[j][i].setcIzquierda(null);
				} 
				
				// Inicializamos los datos de las medias
				this.matrizMedias[j][i].setcDerecha(new Contador());
				this.matrizMedias[j][i].setcIzquierda(new Contador());
				if(i == 0) this.pEdificios[j][i].setcMorado(new Contador());
				if(i != 0 || i == this.pEdificios[0].length - 1) this.pEdificios[j][i].setcVerde(new Contador());
			
			}
		}
	}


	public String toString() {
		String resultado = "";
		for (int i = 0; i < this.filas / 2; i++) {
			for (int j = 0; j < this.columnas; j++) {
				resultado += this.pEdificios[i][j] + "\t\t";
			}
			resultado += "\n";
		}

		return resultado;
	}

	public String toStringMedias() {
		String resultado = "";
		for (int i = 0; i < this.filas / 2; i++) {
			for (int j = 0; j < this.columnas; j++) {
				resultado += this.matrizMedias[i][j] + "\t\t";
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

//	/**
//	 * Le damos un valor al contador general
//	 */
//	public void setSuministroAgua() {
//		//El suministro de agua que generamos aleatoriamente al inicializar nuestros contadores no es correcto, 
//		//lo que generemos tiene que ser igual a la suma de lo que cada casilla vaya consumiendo.
//		
//		double consumoTotal = 0;
//		
//		for(int i=0; i<this.filas/2; i++) {
//			for(int j=0; j<this.columnas; j++) {
//				if(i== this.filas && j== this.columnas) break;
//				consumoTotal +=  this.pEdificios[i][j].getcDerecha().getConsumo() + this.pEdificios[i][j].getcIzquierda().getConsumo();
//			}
//		}
//				
//		double nuevoIndice = (double)this.traducirIndices(filas-1).get(0);	
//		Boolean esPar = (Boolean)this.traducirIndices(filas-1).get(1);
//		
//		Contador c = new Contador();
//		c.setConsumo(consumoTotal);
//		
//		if(esPar) this.pEdificios[(int)nuevoIndice][this.columnas-1].setcIzquierda(c);
//		else this.pEdificios[(int)nuevoIndice][this.columnas-1].setcDerecha(c);
//		
//		this.suministroAgua = consumoTotal;
//	}

	/**
	 * @param cauce cantidad de agua que el usuario quiere introducir
	 */
	public void setSuministroAgua(double cauce) {
		if (cauce > CONSUMO_MAXIMO)
			throw new RuntimeException("Ha introducido demasiada agua y se han roto las tuberías.");
		if (cauce < CONSUMO_MINIMO)
			throw new RuntimeException("El sistema no puede funcionar con tan poco cauce.");
		this.suministroAgua = cauce;

		setLitrosEdificio();
	}

	/**
	 * Cada contador indica lo que cada manzana consume. Para invocar el caso en el
	 * que haya una rotura, vamos a suponer que con un 0,1 de probabilidad aparece
	 * una manzana que consume de forma excesiva.
	 */
	private void setLitrosEdificio() {

		// Si nuestro rotura > 0.7, entonces generaremos target roturas
		double rotura = Math.round(Math.random() * 100.0) / 100.0;

		// Si targetSide es par, irá en la casilla derecha
		double targetSide;

		if (rotura < 0.7) {
			for (int i = 0; i < this.filas / 2; i++) {
				for (int j = 0; j < this.columnas; j++) {
					this.pEdificios[i][j].setcDerecha(
							new Contador(Math.random() * (CONSUMO_MINIMO - CONSUMO_MAXIMO + 1) + CONSUMO_MAXIMO));
					this.pEdificios[i][j].setcIzquierda(
							new Contador(Math.random() * (CONSUMO_MINIMO - CONSUMO_MAXIMO + 1) + CONSUMO_MAXIMO));
				}
			}

		} else {
			System.out.println("!!");
			for (int i = 0; i < this.filas / 2; i++) {
				for (int j = 0; j < this.columnas; j++) {
					targetSide = Math.round(Math.random() * 1000.0) / 10.0;
					if (Math.round(Math.random() * 1000.0) / 10.0 > 0.5) {
						if (targetSide % 2 == 0) {
							this.pEdificios[i][j].setcDerecha(new Contador((Math.random() * (CONSUMO_MINIMO - CONSUMO_MAXIMO + 1) + CONSUMO_MAXIMO) * rotura * 100));
							this.pEdificios[i][j].setcIzquierda(new Contador(Math.random() * (CONSUMO_MINIMO - CONSUMO_MAXIMO + 1) + CONSUMO_MAXIMO));
						} else {
							this.pEdificios[i][j].setcDerecha(new Contador(Math.random() * (CONSUMO_MINIMO - CONSUMO_MAXIMO + 1) + CONSUMO_MAXIMO));
							this.pEdificios[i][j].setcIzquierda(new Contador((Math.random() * (CONSUMO_MINIMO - CONSUMO_MAXIMO + 1) + CONSUMO_MAXIMO) * rotura * 100));
						}
					}
				}
			}
		}
	}

	/**
	 * Para resolver el problema del contador, vamos a necesitar ir viendo cuánto ha
	 * consumido cada edificio
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public double getLitrosEdificio(int i, int j) {
		// El usuario desconoce que se empieza por 0
		i--;
		j--;
		if (i > this.filas || j > this.columnas)
			throw new RuntimeException("Introduzca una ciudad válida");

		ArrayList<Object> traduccion = this.traducirIndices(i);

		double nuevoIndice = (double) traduccion.get(0);
		Boolean esPar = (Boolean) traduccion.get(1);

		if (esPar)
			return pEdificios[(int) nuevoIndice][j].getcIzquierda().getConsumo();
		else
			return pEdificios[(int) nuevoIndice][j].getcDerecha().getConsumo();

	}

	/**
	 * @return la suma de los litros de todas las casillas sin tener en cuenta la
	 *         capacidad de la casilla general generadora
	 */
	public double getLitrosCasillasSalvoCasillaGeneral() {
		double resultado = 0;
		for (int i = 0; i < filas / 2; i++) {
			for (int j = 0; j < columnas; j++) {
				resultado += this.pEdificios[i][j].getcIzquierda().getConsumo()
						+ this.pEdificios[i][j].getcDerecha().getConsumo();
			}
		}
		return resultado;
	}

	public double getSuministroAgua() {
		return suministroAgua;
	}
	
	
	public ArrayList<Contador> consumoExcesivoTroncal(){
		ArrayList<Contador> resultado = new ArrayList<>();
		Contador[] contadoresTroncal = this.traducirMatrizParEdificiosAArrayContadores(this.lineaTroncal);

		resultado = this.consumoExcesivoRec(contadoresTroncal, this.filas/2, this.columnas);
		
		return resultado;
	}
	
	/**
	 * @param troncal es un array con cada contador de cada casilla
	 * @param i
	 * @param j
	 * @return
	 */
	private ArrayList<Contador> consumoExcesivoRec(Contador[] troncal, int i, int j) {
		ArrayList<Contador> resultado = new ArrayList<>();
		
		int inicio = 0;
		int fin = this.filas-1;
		int mitad;
		
		if(inicio >= fin) {
			
			return resultado;
		} else { //Casos recursivos
			mitad = (inicio + fin)/2;
			 this.consumoExcesivoRec(troncal, inicio, mitad);
			 this.consumoExcesivoRec(troncal, mitad+1, fin);
		}
	}
	
	
	/**
	 * Para resolver este problema habíamos juntado todo en una estructura que habíamos llamado parEdificios. Para resolver el problema 
	 * con el algoritmo divide y vencerás, considero que es más sencillo tener a cada manzana en su casilla.
	 * @param pE
	 * @return
	 */
	private Contador[] traducirMatrizParEdificiosAArrayContadores(ParEdificios[] pE) {
		Contador[] contadores = new Contador[pE.length*2];
		
		for(int i=0; i<contadores.length; i++) {
			contadores[i] = pE[i].getcDerecha();
			contadores[i+1] = pE[i].getcIzquierda();
		}
		
		return contadores;
	}
	
	
	

	// MANOMETROS

}