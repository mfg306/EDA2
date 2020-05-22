package org.eda2.greedy;

import java.util.ArrayList;

public class Prueba {
	public static void main(String[] args) {

		Damero d = new Damero(4, 4);
		
		ArrayList<Integer> elegidos = d.resolverManometrosGreedy();
		System.out.println(elegidos.toString());
		System.out.println("");
		System.out.println(d.generarListaTramosManometros(elegidos));
		System.out.println("");
		System.out.println("");
		
		ArrayList<Integer> e = d.resolverContadoresRoturaPropiaGreedy();
		System.out.println(e.toString());
		System.out.println("");
		System.out.println(d.generarListaTramosContadores(e));
		System.out.println("");
	}
}
