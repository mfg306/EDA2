package org.eda2;


public class Prubea {

	public static void main(String[] args) {

		Damero d = new Damero(4,4,300000); //Con 2x2 da fallo
		ParEdificios[][] pE = d.getDamero();
		
		for(int i=0; i<pE.length; i++) {
			for(int j=0; j<pE[i].length; j++) {
//				System.out.println(pE[i][j].getcIzquierda().getId());

			}
		}
		

		
		
		
//		System.out.println(d.consumoExcesivoTroncal().toString());
		

		
	}
}
