package org.eda2.practica01;

import java.util.ArrayList;

public class Programa {

	public static void main(String[] args) {
		
		
		Damero d = new Damero(4,4);
		ParEdificios[][] pE = d.getDamero();
		ParEdificios[][] medias = d.getMedias();
		ParEdificios[] troncal = d.lineaTroncal();
		System.out.println("LINEA TRONCAL");
		for (int i = 0;i<troncal.length;i++) {
			System.out.println(troncal[i].toString());
		}
		System.out.println("");

		ArrayList<Object> problemaTroncal = d.consumoExcesivoTroncal();
		
		System.out.println("DATOS");
		System.out.println(d.toString());
		System.out.println("");
		System.out.println("MEDIAS");
		System.out.println(d.toStringMmedias());
		
		
		
		
	}

}
