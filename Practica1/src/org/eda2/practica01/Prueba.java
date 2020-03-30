package org.eda2.practica01;

public class Prueba {

	public static void main(String[] args) {
		
		Damero pc = new Damero(4,4,0);
		//pc.setSuministroAgua(110);
		//System.out.println("Array de Medias: \n \n" + pc.toStringMmedias());
		
		System.out.println("Los medidores son: " + "\n \n" + pc.toString());	
		System.out.println();
		
		System.out.println(pc.toStringMmedias());
		System.out.println(pc.resolverMedidores());
		
		
		
	}

}
