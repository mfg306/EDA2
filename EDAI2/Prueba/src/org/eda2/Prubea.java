package org.eda2;

public class Prubea {

	public static void main(String[] args) {
//		Contador.reiniciarId();

		Damero d = new Damero(4,4,300000);
		ParEdificios[][] pE = d.getDamero();
		
		for(int i=0; i<pE.length; i++) {
			for(int j=0; j<pE[i].length; j++) {
				System.out.println(pE[i][j].getcDerecha().getId());
				System.out.println(pE[i][j].getcIzquierda().getId());
				if(pE[i][j].getcMorado()!= null) System.out.println(pE[i][j].getcMorado().getId());
				if(pE[i][j].getcVerde()!= null) System.out.println(pE[i][j].getcVerde().getId());
				
				System.out.println("--------");

			}
		}
		
		
		

	}
}
