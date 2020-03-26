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
	public final static double CONSUMO_MINIMO = 300000; //m3
	public final static double CONSUMO_MAXIMO = 500000; //m3


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
	public Damero(int filas, int columnas, double cauceInicial) {
		this.filas = filas;
		this.columnas = columnas;
		if (filas % 2 == 0) {
			pEdificios = new ParEdificios[columnas / 2][filas]; // i vale la mitad porque unimos dos columnas
			matrizMedias = new ParEdificios[columnas / 2][columnas];
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
	
		setSuministroAgua(cauceInicial);
		
		this.lineaTroncal = this.lineaTroncal(); 
	}
	
	
	/**
	 * @return la línea troncal de nuestro damero
	 */
	public ParEdificios[] lineaTroncal() {
		ParEdificios[] pE = new ParEdificios[pEdificios.length];
		for (int i = 0;i<pE.length;i++) {
			pE[i] = pEdificios[i][pEdificios[0].length-1];
		}
		return pE;
	}
	
	public ParEdificios[] lineaTroncalMedia() {
		ParEdificios[] pE = new ParEdificios[pEdificios.length];
		for (int i = 0;i<pE.length;i++) {
			pE[i] = matrizMedias[i][pEdificios[0].length-1];
		}
		return pE;
	}
	
	
	public ParEdificios[][] getDamero(){
		ParEdificios[][] resultado = new ParEdificios[this.pEdificios.length][this.pEdificios[0].length];
		
		for(int i=0; i<resultado.length; i++) {
			for(int j=0; j<resultado[i].length; j++) {
				resultado[i][j] = new ParEdificios();
				resultado[i][j] = this.pEdificios[i][j];
			}
		}
		
		return resultado;
	}
	

	// CONTADORES

	private void inicializarContadores() { 
		if(this.filas % 2 == 0) this.inicializarContadoresPar();
		//Y si no es par?
		
	}
	
	private void inicializarContadoresPar() { //Deberiamos inicializar primero los de cada edificio y a partir de ese
		//RECORREMOS EL ARRAY INICIALIZANDO LOS CONTADORES ROJOS
		for (int i = 0;i<pEdificios.length;i++) {
			for (int j = 0; j<pEdificios[0].length;j++) {
				this.pEdificios[i][j].setcDerecha(new Contador(Math.random() * (100 - 1000 + 1) + 1000));
				this.pEdificios[i][j].setcIzquierda(new Contador(Math.random() * (100 - 1000 + 1) + 1000));
			}
		}
		
		//INICIALIZAMOS LO CONTADORES VERDES Y MORADOS
		double con = 0;
		for (int i = 0;i<pEdificios.length;i++) {
			for (int j = 1; j<pEdificios[0].length;j++) {
				if (j==pEdificios[0].length-1) { //linea de distribucion
					con += pEdificios[i][j-1].getcVerde().getConsumo();
					con += pEdificios[i][j].getcDerecha().getConsumo();
					con += pEdificios[i][j].getcIzquierda().getConsumo();
					if (i!=0) {
						con += pEdificios[i-1][j].getcMorado().getConsumo();
					}
					
					pEdificios[i][j].setcMorado(new Contador(con));
					con=0;
				} else if (pEdificios[i][j-1].getcVerde()==null) {
					con += pEdificios[i][j-1].getcDerecha().getConsumo();
					con += pEdificios[i][j-1].getcIzquierda().getConsumo();
					con += pEdificios[i][j].getcDerecha().getConsumo();
					con += pEdificios[i][j].getcIzquierda().getConsumo();
					pEdificios[i][j].setcVerde(new Contador(con));
					con =0;
				} else {
					con += pEdificios[i][j-1].getcVerde().getConsumo();
					con += pEdificios[i][j].getcDerecha().getConsumo();
					con += pEdificios[i][j].getcIzquierda().getConsumo();
					pEdificios[i][j].setcVerde(new Contador(con));
					con =0;
				}
			}
		}
		
		//Arreglar la casilla i,j. Tiene que ser igual a la suma de todas
		
		for(int i = 0 ; i<this.columnas / 2; i++) {
			for(int j = 0 ; j<this.filas; j++) {
				if(i == (this.columnas/2)-1 && j == this.filas-1) break; //La general no
				con += this.pEdificios[i][j].getcDerecha().getConsumo() + this.pEdificios[i][j].getcIzquierda().getConsumo();
			}
		}
		
		
		
		pEdificios[(this.columnas/2)-1][this.filas-1].getcIzquierda().setConsumo(con);
		

	}
	
	public void generarMatrizMedias() {
		//RECORREMOS EL ARRAY INICIALIZANDO LOS CONTADORES ROJOS
				for (int i = 0;i<matrizMedias.length;i++) {
					for (int j = 0; j<matrizMedias[0].length;j++) {
						this.matrizMedias[i][j].setcDerecha(new Contador(Math.random() * (CONSUMO_MINIMO - CONSUMO_MAXIMO + 1) + CONSUMO_MAXIMO));
						this.matrizMedias[i][j].setcIzquierda(new Contador(Math.random() * (CONSUMO_MINIMO - CONSUMO_MAXIMO + 1) + CONSUMO_MAXIMO));
					}
				}
				
				//INICIALIZAMOS LO CONTADORES VERDES Y MORADOS
				double con = 0;
				for (int i = 0;i<matrizMedias.length;i++) {
					for (int j = 1; j<matrizMedias[0].length;j++) {
						if (j==matrizMedias[0].length-1) { //linea de distribucion
							con += matrizMedias[i][j-1].getcVerde().getConsumo();
							con += matrizMedias[i][j].getcDerecha().getConsumo();
							con += matrizMedias[i][j].getcIzquierda().getConsumo();
							if (i!=0) con += matrizMedias[i-1][j].getcMorado().getConsumo();
							matrizMedias[i][j].setcMorado(new Contador(con));
						} else if (matrizMedias[i][j-1].getcVerde()==null) {
							con += matrizMedias[i][j-1].getcDerecha().getConsumo();
							con += matrizMedias[i][j-1].getcIzquierda().getConsumo();
							con += matrizMedias[i][j].getcDerecha().getConsumo();
							con += matrizMedias[i][j].getcIzquierda().getConsumo();
							matrizMedias[i][j].setcVerde(new Contador(con));
						} else {
							con += matrizMedias[i][j-1].getcVerde().getConsumo();
							con += matrizMedias[i][j].getcDerecha().getConsumo();
							con += matrizMedias[i][j].getcIzquierda().getConsumo();
							matrizMedias[i][j].setcVerde(new Contador(con));
						}
						con = 0;
					}
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
	private void setSuministroAgua(double cauce) {
		if (cauce > CONSUMO_MAXIMO)
			throw new RuntimeException("Ha introducido demasiada agua y se han roto las tuberías.");
		if (cauce < CONSUMO_MINIMO)
			throw new RuntimeException("El sistema no puede funcionar con tan poco cauce.");
		this.suministroAgua = cauce;

		setLitrosEdificio(cauce);
	}
	
	
	
	/**
	 * @param cauce cantidad de agua que se desea suministrar a través de la casilla general
	 */
	private void setLitrosEdificio(double cauce) {
		//Para ser justos, vamos a darle a todas las manzanas la misma cantidad de agua. Luego ya veremos si ese agua que hemos generado
		//es mayor que la media
		
		
		int numTotalManzanas = (this.filas*this.columnas)-1; //quitamos la general
		double porcionIndividual = cauce/numTotalManzanas;
		double porcentajeAVariar; //Esto es para que no todas las manzanas consuman exactamente lo mismo.
		
		
		for(int i=0; i<this.columnas/2; i++) {
			for(int j=0; j<this.filas; j++) {
				if(i==(this.columnas/2)-1 && j == this.filas-1) {
					// this.pEdificios[(int)filas-1][columnas-1].getcIzquierda().getConsumo() : this.pEdificios[(int)filas-1][columnas-1].getcDerecha().getConsumo()  ;
					this.pEdificios[i][j].setcDerecha(new Contador(cauce));
				} else {
					porcentajeAVariar = (Math.random()*(0 - 0.5 + 1) + 0.5);
					this.pEdificios[i][j].setcDerecha(new Contador(porcionIndividual-(porcionIndividual*porcentajeAVariar)));
					porcentajeAVariar = (Math.random()*(0 - 0.5 + 1) + 0.5);
					this.pEdificios[i][j].setcIzquierda(new Contador(porcionIndividual-(porcionIndividual*porcentajeAVariar)));
				}

			}
		}
	}
	
	/**
	 * Dada una casilla, saber cuánto ha consumido
	 * @param i
	 * @param j
	 * @return
	 */
	public double getLitrosEdificio(int i, int j) { //columnas, filas
		i--;
		j--;
		double filas = (double)this.traducirIndices(i).get(0);
		boolean par = (boolean)this.traducirIndices(i).get(1);
		
		if(filas > this.filas/2 || i > this.columnas) {
			throw new IndexOutOfBoundsException("Debe introducir un edificio válido. Compruebe que los índices son correctos.");
		}
		return (par == true) ?  this.pEdificios[(int)filas][j].getcIzquierda().getConsumo() : this.pEdificios[(int)filas][j].getcDerecha().getConsumo()  ;
	
	}

	/**
	 * @return la suma de los litros de todas las casillas sin tener en cuenta la
	 *         capacidad de la casilla general generadora
	 */
	public double getLitrosCasillasSalvoCasillaGeneral() {
		
		double resultado = 0;
	
		for(int i = 0 ; i<this.columnas / 2; i++) {
			for(int j = 0 ; j<this.filas; j++) {
				if(i == (this.columnas/2)-1 && j == this.filas-1) break; //La general no
				resultado += this.pEdificios[i][j].getcDerecha().getConsumo() + this.pEdificios[i][j].getcIzquierda().getConsumo();
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

		int i=0;		
		int j=contadoresTroncal.length-1;
				
		resultado = this.consumoExcesivoRec(contadoresTroncal, i, j);
		
		return resultado;
	}
	
	/**
	 * @param troncal es un array con cada contador de cada casilla
	 * @param i inicio del array
	 * @param j final del array
	 * @return
	 */
	private ArrayList<Contador> consumoExcesivoRec(Contador[] troncal, int i, int j) {
		ArrayList<Contador> resultado = new ArrayList<>();
		Contador[] media = this.traducirMatrizParEdificiosAArrayContadores(this.lineaTroncalMedia());
		int mitad;

		if(i >= j-1) { //Caso base -> Si solo nos quedan dos elementos
			
			
		} else { //Casos recursivos
			mitad = (i + j)/2;
			this.consumoExcesivoRec(troncal, i, mitad-1);
			this.consumoExcesivoRec(troncal, mitad, j);
		}
		
		return resultado;
	}
	
	
	/**
	 * Para resolver este problema habíamos juntado todo en una estructura que habíamos llamado parEdificios. Para resolver el problema 
	 * con el algoritmo divide y vencerás, considero que es más sencillo tener a cada manzana en su casilla.
	 * @param pE
	 * @return
	 */
	private Contador[] traducirMatrizParEdificiosAArrayContadores(ParEdificios[] pE) {
		Contador[] contadores = new Contador[pE.length*2];
		int contador = 0;
		
		for(int i=0; i<pE.length; i++) {
			contadores[contador] = pE[i].getcIzquierda();
			contador++;
			contadores[contador] = pE[i].getcDerecha();
			contador++;
		}
		
		return contadores;
	}
	

	//NOTA -> PRIMERO TENEMOS QUE GENERAR LA CASILLA GENERAL (QUE TIENE QUE ESTAR ENTRE EL MAX Y EL MINIMO) 
	//Y LUEGO A PARTIR DE ESO IR DISTRIBUYENDO, PERO QUE LA SUMA NUNCA SEA MAYOR QUE EL MAXIMO
	//ES DECIR, GENERAMOS UNA CANTIDAD FIJA DE AGUA
	
	

	// MANOMETROS

}
