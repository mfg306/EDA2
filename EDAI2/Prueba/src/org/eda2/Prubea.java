package org.eda2;

public class Prubea {

	public static void main(String[] args) {

		Damero d = new Damero(4,4);
		
		System.out.println(d.toString());
		
		ParEdificios[] pE = d.lineaTroncal();
		
//		for(int i=0; i<pE.length; i++) {
//			System.out.println(pE);
//		}

	}
}