package org.eda2.greedy;

/**
 * @author marta y alex
 *
 */
public class Contador {

	private double consumo;
	private static int contador = 0; 
	private int id;
	private int i;
	private int j;
	private String tipo;
	private Contador media;

	
	/**
	 * Constructor por defecto
	 */
	public Contador() { 
		this.consumo = (Math.random() * (108 - 162 + 1) + 162); // Consumo diario
		this.id = Contador.contador;
		Contador.contador++;
	}
	
	/**
	 * @param consumo cantidad de cauce
	 * @param i su coordenada i en el damero
	 * @param j su coordenada j en el damero
	 * @param tipo derecha, izquierda,...
	 */
	public Contador(double consumo, int i, int j, String tipo) {
		this.consumo = consumo;
		this.id = Contador.contador;
		Contador.contador++;
		this.i = i;
		this.j = j;
		this.tipo = tipo;
	}
	
	/**
	 * @param consumo su candtidad de cauce
	 * @param media su respectivo contador media
	 * @param i su coordenada i en el damero
	 * @param j su coordenada j en el damero
	 * @param tipo derecha, izquierda, ... 
	 */
	public Contador(double consumo, Contador media, int i, int j, String tipo) {
		this.consumo = consumo;
		this.id = Contador.contador;
		Contador.contador++;
		this.media = media;
		this.i = i;
		this.j = j;
		this.tipo = tipo;
		
	}
	/**
	 * @return el consumo del contador
	 */
	public double getConsumo() {
		return this.consumo;
	}

	/**
	 * @param consumo del contador
	 */
	public void setConsumo(double consumo) {
		this.consumo = consumo;
	}
	
	/**
	 * @return el id del contador
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return el contador Media 
	 */
	public Contador getMedia() {
		return media;
	}

	/**
	 * @param media el contador que queremos establecer 
	 */
	public void setMedia(Contador media) {
		this.media = media;
	}
	
	/**
	 * @return coordenada i 
	 */
	public int getI() {
		return i;
	}

	/**
	 * @param i la coordenada i 
	 */
	public void setI(int i) {
		this.i = i;
	}

	/**
	 * @return la coordenada j
	 */
	public int getJ() {
		return j;
	}

	/**
	 * @param j la coordenada j 
	 */
	public void setJ(int j) {
		this.j = j;
	}
	
	/**
	 * @return el tipo derecha, izquierda, verde, morado
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo que queremos establecer
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "" + this.consumo;
	}
	
	/**
	 * Establecer el contador a 0
	 */
	public static void reiniciarId() {
		Contador.contador = 0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		Contador otro = (Contador)other;
		return this.id == otro.id;
	}

}
