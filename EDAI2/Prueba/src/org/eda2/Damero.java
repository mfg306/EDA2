package org.eda2;

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
	
	public String toString() {
		return "Tablero de -> " + this.filas + " , " + this.columnas;
//		String resultado = "";
//		for(int i=0; i<this.filas; i++) {
//			for(int j=0; j<this.columnas; j++) {
//				resultado += this.pEdificios[i][j];
//			}
//			resultado += "\n";
//		}
//		
//		return resultado;
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
	
	
	/**
	 * Para resolver el problema del contador, vamos a necesitar ir viendo cuánto ha consumido cada edificio
	 * @param i
	 * @param j
	 * @return
	 */
	public double getLitrosEdificio(int i, int j) {
		if(i > this.filas || j > this.columnas) throw new RuntimeException("Introduzca una ciudad válida");
		//Si accedemos a una fila par devolvemos el de la derecha
		if(i %2 == 0) return pEdificios[i][j].getConsumoContadorDerecha(); 
		else return pEdificios[i][j].getConsumoContadorIzquierda();
		
	}
	
	/**
	 * @return la suma de los litros de todas las casillas sin tener en cuenta la capacidad de la casilla general generadora
	 */
	public double getLitrosCasillasSalvoCasillaGeneral() {
		double resultado = 0;
		for(int i=0; i<filas/2; i++) {
			for(int j=0; j<columnas; j++) {
				resultado += pEdificios[i][j].getConsumoContadorDerecha() + pEdificios[i][j].getConsumoContadorIzquierda();
			}
		}
		
		return resultado;
	}
	
	public double getSuministroAgua() {
		return suministroAgua;
	}


	public void setSuministroAgua() {
		
		//El suministro de agua que generamos aleatoriamente al inicializar nuestros contadores no es correcto, 
		//lo que generemos tiene que ser igual a la suma de lo que cada casilla vaya consumiendo.
		
		double consumoTotal = 0;
		
		for(int i=0; i<this.filas; i++) {
			for(int j=0; j<this.columnas; j++) {
				if(i== this.filas && j== this.columnas) break;
				consumoTotal +=  this.pEdificios[i][j].getConsumoContadorDerecha() + this.pEdificios[i][j].getConsumoContadorIzquierda();
			}
		}
		
		this.suministroAgua = consumoTotal;
	}
	
	
	//MANOMETROS
	
	
	
	
	
	
}
