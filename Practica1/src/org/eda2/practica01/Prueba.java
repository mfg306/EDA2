package org.eda2.practica01;
import java.util.ArrayList;

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
		
		pc.inicializarContadores();
		System.out.println("hola");
		
		System.out.println("Los contadores pares son: " + "\n \n" + pc.toStringM());
		
//		ProblemaContadores p2 = new ProblemaContadores(5, 5);
//		
//		p2.inicializarContadores();
//		
//		System.out.println("Los contadores impares son: " + "\n \n" + p2.toString());

		pc.resolverManometros();
		
	}

}
