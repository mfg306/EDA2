package org.eda2.practica02;

import java.util.ArrayList;

public class Programa {

	public static void main(String[] args) {
		
		Damero d = new Damero(4,4);
		
		//RESOLVER CONTADORES (1B)
		
//		ArrayList<Contador> con = d.resolverContadoresGreedy();
//		System.out.println("RESULTADO");
//		System.out.println(con);
//		System.out.println("");
//		System.out.println("DATOS");
//		System.out.println(d.toStringC());
//		System.out.println("");
//		System.out.println("MEDIAS");
//		System.out.println(d.toStringMedias());
		
		
		//RESOLVER MANOMETROS (1A)
		
//		ArrayList<Manometro> man = d.resolverManometrosGreedy();
//		System.out.println("RESULTADO");
//		System.out.println(man);
//		System.out.println("");
//		System.out.println("DATOS");
//		System.out.println(d.toStringM());
		
		//B
		ArrayList<Contador> cons = d.resolverConsumidoresGreedy();
		System.out.println(cons.toString());
		System.out.println("DATOS");
		System.out.println(d.toStringC());
		System.out.println("MEDIAS");
		System.out.println(d.toStringMedias());
	}
}
