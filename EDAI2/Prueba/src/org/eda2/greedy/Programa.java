package org.eda2.greedy;

import java.util.ArrayList;

public class Programa {

	public static void main(String[] args) {
		
		Damero d = new Damero(6,6);
		
		ArrayList<Contador> a = d.resolverContadoresRoturaPropiaGreedy();
		System.out.println(a);
		System.out.println(a.size());
		System.out.println(d.toStringC());
		System.out.println("");
		System.out.println(d.toStringMedias());
	}
}
