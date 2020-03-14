package org.eda2;
import java.util.ArrayList;

/**
 * @author marta
 * La representación de nuestro tablero viene dada por aristas
 */
public class Damero { 
	private int filas;
	private int columnas; 
	private ParEdificios[][] pEdificios;
	/**
	 * Cantidad de agua que suministramos en la casilla general
	 */
	private double suministroAgua;
	
	/**
	 * Nuestro Damero viene representado por pares de calles.
	 * @param filas 
	 * @param columnas
	 */
	public Damero(int filas, int columnas) {
		this.filas = filas;
		this.columnas = columnas;
		if (filas%2==0) pEdificios = new ParEdificios[filas/2][columnas]; //i vale la mitad porque unimos dos columnas
		else pEdificios = new ParEdificios[(filas+1)/2][columnas];
		for(int a=0; a<pEdificios.length; a++) {
			for(int b=0; b<pEdificios[a].length; b++) {
				pEdificios[a][b] = new ParEdificios();
			}
		}
	}
	
	
	//CONTADORES
	
	public void inicializarContadores() {
		if (pEdificios.length%2==0) inicializarContadoresPar();
		else inicializarContadoresImpar();
	}
	
	/**
	 * Inicializamos por columnas. A cada par de edificios le asignamos su contador al edificio izquierdo y su contador al 
	 * edificio derecho.
	 */
	private void inicializarContadoresPar() {
		for(int i=0; i<this.columnas/2; i++) {
			for(int j=0; j<this.filas; j++) {
				this.pEdificios[i][j].setcDerecha(new Contador());
				this.pEdificios[i][j].setcIzquierda(new Contador());
				if (j==pEdificios[0].length-1 && i != pEdificios.length-1) this.pEdificios[i][j].setcMorado(new Contador());
				if (j!=0&&j!=pEdificios[0].length-1)this.pEdificios[i][j].setcVerde(new Contador());
			}
		}
	}
	
	/**
	 * En el caso de que tengamos un número de filas impar, inicializaremos todas las casillas como si se tratase de un caso par. Sin 
	 * embargo, 
	 */
	private void inicializarContadoresImpar() {
		inicializarContadoresPar(); 
		for (int j = 0;j<pEdificios[0].length;j++) {
			this.pEdificios[0][j].setcIzquierda(null);
		}
	}
	
	public String toString() {
		String resultado = "";
		for(int i=0; i<this.filas; i++) {
			for(int j=0; j<this.columnas; j++) {
				resultado += this.pEdificios[i][j];
			}
			resultado += "\n";
		}
		
		return resultado;
	}
	
	
	/**
	 * El usuario desconoce la estructura ParEdificios y va a insertar la dirección de una manzana con respecto al damero total 
	 * @param i filas a hallar
	 * @return un ArrayList en cuya primera posicion insertamos el indice y en la segunda si el resultado de si el indice que nos pasaron
	 * era par. Si es par accederemos a la casilla de la izquierda; si es impar a la de la derecha
	 */
	private ArrayList<Object> traducirIndices(int i){
		double division = i/2;
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
		this.suministroAgua = cauce;
	}
	
	public void setLitrosEdificio() {
		double general = this.getSuministroAgua();
		
		for(int i=0; i<this.filas/2; i++) {
			Double nuevoIndice = (Double)this.traducirIndices(i).get(0);
			Boolean esPar = (Boolean)this.traducirIndices(i).get(1);
			for(int j=0; j<this.columnas; j++) {
			}
		}
		
		
	}
	
	
	/**
	 * Para resolver el problema del contador, vamos a necesitar ir viendo cuánto ha consumido cada edificio
	 * @param i
	 * @param j
	 * @return
	 */
	public double getLitrosEdificio(int i, int j) {
		//El usuario desconoce que se empieza por 0
		i--;
		j--;
		if(i > this.filas || j > this.columnas) throw new RuntimeException("Introduzca una ciudad válida");
		
		ArrayList<Object> traduccion = this.traducirIndices(i);
		
		 double nuevoIndice = (double)traduccion.get(0);
		 Boolean esPar = (Boolean)traduccion.get(1);
		
		if(esPar) return pEdificios[(int)nuevoIndice][j].getcIzquierda().getConsumo();
		else return pEdificios[(int)nuevoIndice][j].getcDerecha().getConsumo();
		
	}
	
	/**
	 * @return la suma de los litros de todas las casillas sin tener en cuenta la capacidad de la casilla general generadora
	 */
	public double getLitrosCasillasSalvoCasillaGeneral() {
		double resultado = 0;
		for(int i=0; i<filas/2; i++) {
			for(int j=0; j<columnas; j++) {
				resultado += this.pEdificios[i][j].getcIzquierda().getConsumo() + this.pEdificios[i][j].getcDerecha().getConsumo();
			}
		}
		return resultado;
	}
	
	public double getSuministroAgua() {
		return suministroAgua;
	}


	
	
	//MANOMETROS
	
	
	
	
	
	
}
