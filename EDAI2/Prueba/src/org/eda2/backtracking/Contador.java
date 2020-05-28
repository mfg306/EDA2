package org.eda2.backtracking;


/**
 * @author marta y alex
 *
 */
public class Contador implements Comparable<Contador>{

	private double consumo;
	private static int contador = 0; 
	private int id;
	private int i;
	private int j;
	private String tipo;
	private Contador media;
	private double op;
	private double at;

	
	/**
	 * Constructor por defecto
	 */
	public Contador() { 
		this.consumo = (Math.random() * (108 - 162 + 1) + 162); // Consumo diario
		this.id = Contador.contador;
		Contador.contador++;
	}
	
	/**
	 * @param consumo que queremos establecer
	 */
	public Contador(double consumo) {
		this.consumo = consumo;
	}
	
	/**
	 * @param consumo que queremos establecer
	 * @param i su coordenada i en el damero
	 * @param j su coordenada j en el damero
	 * @param tipo izquierda, derecha, verde, morado
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
	 * @param consumo que queremos establecer
	 * @param media su contador media
	 * @param i su coordenada i en el damero
	 * @param j su coordenada j en el damero
	 * @param tipo izquierda, derecha, verde, morado
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
	 * @return la media del contador
	 */
	public Contador getMedia() {
		return media;
	}

	/**
	 * @param media del contador
	 */
	public void setMedia(Contador media) {
		this.media = media;
	}
	
	/**
	 * @return su coordenada i 
	 */
	public int getI() {
		return i;
	}

	/**
	 * @param i coordenada
	 */
	public void setI(int i) {
		this.i = i;
	}

	/**
	 * @return su coordenada j 
	 */
	public int getJ() {
		return j;
	}

	/**
	 * @param j coordenada
	 */
	public void setJ(int j) {
		this.j = j;
	}
	
	/**
	 * @return el tipo 
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo (derecha, izquierda, verde, morado)
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	/**
	 * @param op su ingreso
	 */
	public void setOp(double op) {
		this.op = op;
	}
	
	/**
	 * @return el ingreso del contador
	 */
	public double getOp() {
		return this.op;
	}
	
	/**
	 * @return su tiempo de atencion
	 */
	public double getAt() {
		return at;
	}

	/**
	 * @param at tiempo de atencion
	 */
	public void setAt(double at) {
		this.at = at;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "" + this.consumo;
	}
	
	/**
	 * Establece el contador a 0
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

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Contador o) {
		return Double.compare(this.consumo, o.consumo);
	}

}
