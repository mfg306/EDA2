package org.eda2.practica01;

public class Prueba {

	public static void main(String[] args) {
		
		
//		ProblemaManometros pm = new ProblemaManometros(5, 5); 
//		
//		pm.inicializarManometro();
//		
//		System.out.println("Los manometros son: "+"\n \n" + pm.toString());
//		
//		System.out.println();
//		System.out.println();
//		System.out.println();
		
		ProblemaContadores pc = new ProblemaContadores(6, 6);
		
		System.out.println("Array de Medias: \n \n" + pc.toStringMmedias());
		
		pc.inicializarMedidores();
		
		System.out.println("Los medidores son: " + "\n \n" + pc.toString());
		
//		ProblemaContadores p2 = new ProblemaContadores(5, 5);
//		
//		p2.inicializarContadores();
//		
//		System.out.println("Los contadores impares son: " + "\n \n" + p2.toString());
		System.out.println(pc.toStringMmedias());
		System.out.println(pc.resolverMedidores());
		
	}

}
