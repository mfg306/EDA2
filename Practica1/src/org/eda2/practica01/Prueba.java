package org.eda2.practica01;

public class Prueba {

	public static void main(String[] args) {
		
		
		Damero d = new Damero(4,4,300000);
		ParEdificios[][] pE = d.getDamero();
		ParEdificios[][] medias = d.getMedias();
		

		
		
		System.out.println("DATOS");
		System.out.println(d.toString());
		System.out.println("");
		System.out.println("MEDIAS");
		System.out.println(d.toStringMmedias());
		
		
		
		
		
	}

}
