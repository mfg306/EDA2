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
	}

	// CONTADORES

	private void inicializarContadores() {
		for (int i = 0; i < this.columnas / 2; i++) {
			for (int j = 0; j < this.filas; j++) {
				if( i == this.columnas-1) {
					
				}
				
				// Inicializamos los datos actuales
				this.pEdificios[i][j].setcDerecha(new Contador());
				this.pEdificios[i][j].setcIzquierda(new Contador());
				if (j == pEdificios[0].length - 1 && i != pEdificios.length - 1)
					this.pEdificios[i][j].setcMorado(new Contador());
				if (j != 0 && j != pEdificios[0].length - 1)
					this.pEdificios[i][j].setcVerde(new Contador());

				// Inicializamos los datos de las medias
				this.matrizMedias[i][j].setcDerecha(new Contador());
				this.matrizMedias[i][j].setcIzquierda(new Contador());

				if (j == this.matrizMedias[0].length - 1 && i != this.matrizMedias.length - 1)
					this.matrizMedias[i][j].setcMorado(new Contador());
				if (j != 0 && j != this.matrizMedias[0].length - 1)
					this.matrizMedias[i][j].setcVerde(new Contador());
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

	// MANOMETROS

}
