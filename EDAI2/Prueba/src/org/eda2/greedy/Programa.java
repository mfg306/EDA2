package org.eda2.greedy;

import java.util.ArrayList;

public class Programa {

	public static void main(String[] args) {
		
		Damero d = new Damero(6,6);
		
		ArrayList<Integer> resultado = d.resolverConsumidoresGreedy();
		
		System.out.println(d.toStringC());
		System.out.println("");
		System.out.println(d.toStringMedias());
		System.out.println("");
		System.out.println(resultado.toString());
	}
}
