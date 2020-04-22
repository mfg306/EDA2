package org.eda2.practica02;

import java.util.ArrayList;

public class Programa {

	public static void main(String[] args) {
		Damero d = new Damero(4,4);
		ArrayList<Contador> con = d.solucion();
		System.out.println(con);
		System.out.println("");
		System.out.println(d.toStringC());
		System.out.println("");
		System.out.println(d.toStringMedias());
	}
}
