package org.eda2;

public class Prueba {
	
	public static int select(int[] t, int k) {
		return selectRec(t, 0, t.length-1, k);
	}
	
	private static int selectRec(int[] t, int ini, int fin, int k) {
		
		if(ini == fin) return t[ini];
		
		int p = reorgniza(t, ini, fin);
		int k1 = p - ini +1;
		
		if(k <= k1) {
			return selectRec(t, ini, p, k);
		} else return selectRec(t, p+1, fin, k-k1);
	}

	
	private static int reorgniza(int[] t, int ini, int fin) {
		
		int x = t[ini];
		int i = ini -1;
		int j = fin +1;
		
		while(true) {
			do {
				j--;
			}while(t[j] > x);
			
			do {
				i++;
			}while(t[i] < x);
			
			if(i < j) {
				int z=t[i];
				t[i] = t[j];
				t[j] = z;
			} else {
				return(j);
			}
		}
	}

	
	
	public static void main(String[] args) {
		int[] array = {4,3,2,8};
		
		System.out.println(select(array, 7));
	}
}
