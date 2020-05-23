package org.eda2.dynamic;


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

	
	public Contador() { 
		this.consumo = (Math.random() * (108 - 162 + 1) + 162); // Consumo diario
		this.id = Contador.contador;
		Contador.contador++;
	}
	
	public Contador(double consumo) {
		this.consumo = consumo;
	}
	
	public Contador(double consumo, int i, int j, String tipo) {
		this.consumo = consumo;
		this.id = Contador.contador;
		Contador.contador++;
		this.i = i;
		this.j = j;
		this.tipo = tipo;
	}
	
	public Contador(double consumo, Contador media, int i, int j, String tipo) {
		this.consumo = consumo;
		this.id = Contador.contador;
		Contador.contador++;
		this.media = media;
		this.i = i;
		this.j = j;
		this.tipo = tipo;
		
	}
	public double getConsumo() {
		return this.consumo;
	}

	public void setConsumo(double consumo) {
		this.consumo = consumo;
	}
	
	public int getId() {
		return id;
	}
	
	public Contador getMedia() {
		return media;
	}

	public void setMedia(Contador media) {
		this.media = media;
	}
	
	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public void setOp(double op) {
		this.op = op;
	}
	
	public double getOp() {
		return this.op;
	}
	
	public double getAt() {
		return at;
	}

	public void setAt(double at) {
		this.at = at;
	}

	@Override
	public String toString() {
		return "" + this.consumo;
	}
	
	public static void reiniciarId() {
		Contador.contador = 0;
	}
	
	@Override
	public boolean equals(Object other) {
		Contador otro = (Contador)other;
		return this.id == otro.id;
	}

	@Override
	public int compareTo(Contador o) {
		return Double.compare(this.consumo, o.consumo);
	}

}
