package org.eda2;


public class Prubea {
	
	public static void mostrarMatriz(int[][] a){
		//Elemento a elemento		
		mostrarMatrizRec(a, 0, a.length-1);
	}
	
	private static void mostrarMatrizRec(int[][] a, int i, int j){
		if(i==j)
		{
			System.out.println(a[i]);
		} else {
			mostrarMatrizRec(a, i, j--);
		}
	}

	public static void main(String[] args) {
			
		int[][] array = new int[2][2];
		int contador = 0;
		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array[i].length; j++) {
				array[i][j] = contador++;
			}
		}
		
		mostrarMatriz(array);
		
		
	}
}
