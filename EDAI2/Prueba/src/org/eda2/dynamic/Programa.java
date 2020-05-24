package org.eda2.dynamic;

public class Programa {

	public static void main(String[] args) {
		Damero d = new Damero(4,4,1,7, 5);
		double max = 0;
		d.establecerListaATRoturas();
		d.generarOP();
		
		
		double[][] table = d.maximizarDineroDadoWTT();
		
		for (int i = 0;i<table.length;i++) {
			for (int j = 0;j<table[0].length;j++) {
				System.out.print(table[i][j]+"	");
			}
			System.out.println("");
		}
		
		System.out.println("");
		
//		ParEdificios[][] pE = d.getDamero();
//		
//		for (int i = 0;i<pE.length;i++) {
//			for (int j = 0;j<pE[0].length;j++) {
//				System.out.print(pE[i][j]+"	");
//			}
//			System.out.println("");
//		}
	}
}
