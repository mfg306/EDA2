package org.eda2;

public class Factorial {
	
	public static int factorialRecursivo(int x) {
		if(x == 1) return 1;
		else {
			return x*factorialRecursivo(x-1);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println(factorialRecursivo(4));

	}

}
