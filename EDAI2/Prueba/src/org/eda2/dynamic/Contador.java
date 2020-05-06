package org.eda2.dynamic;

public class Contador {

	private double consumo;
	private static int contador = 0; 
	private int id;
	private Contador media;
	
	public Contador() { 
		this.consumo = (Math.random() * (108 - 162 + 1) + 162); // Consumo diario
		this.id = Contador.contador;
		Contador.contador++;
	}
	
	public Contador(double consumo) {
		this.consumo = consumo;
		this.id = Contador.contador;
		Contador.contador++;
	}
	
	public Contador(double consumo, Contador media) {
		this.consumo = consumo;
		this.id = Contador.contador;
		Contador.contador++;
		this.media = media;
		
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

}
