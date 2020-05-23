package org.eda2.dynamic;

import java.util.ArrayList;

public class Prueba {
	
	public static final int WTT = 5;

	public static double[][] maximizarDineroDadoWTT() {

		Contador aux = new Contador(0.0);
		aux.setAt(0.0);
		ArrayList<Contador> resolverConsumidoresVersionContadores = new ArrayList<Contador>();
		
		// --Contadores
			
		Contador c1 = new Contador(4.0); 
		c1.setAt(4.0);
		c1.setOp(2.0);
		Contador c2 = new Contador(2.0); 
		c2.setAt(2.0);	
		c2.setOp(3.0);
		Contador c3 = new Contador(6.0); 
		c3.setAt(5.0);
		c3.setOp(4.60);
		
		//--
		
		resolverConsumidoresVersionContadores.add(c1);
		resolverConsumidoresVersionContadores.add(c2);
		resolverConsumidoresVersionContadores.add(c3);

		ArrayList<Contador> listaContadores = new ArrayList<>();
		listaContadores.add(aux); //Es un valor auxiliar, a la posicion 0 no vamos a acceder -> Asi tiene el mismo tamaño que la matriz (nContadores+1)
		listaContadores.addAll(resolverConsumidoresVersionContadores);
		if (listaContadores.size() == 1) return null;
		int numContadores = listaContadores.size();
		int cota = WTT;
		double[][] table = new double[numContadores][cota+1];

		listaContadores.sort(new ComparadorContadoresAT());
		

		for (int i = 0; i < cota; i++) {
			table[0][i] = 0;
		}

		for (int i = 1; i < numContadores; i++) {
			if (listaContadores.get(i).getAt() > cota) continue; // No cabe en la mochila

			for (int j = 0; j < listaContadores.get(i).getAt(); j++) {
				table[i][j] = table[i - 1][j]; // El de arriba
			}

			for (int k = (int) listaContadores.get(i).getAt(); k < cota+1; k++) {
				double prev = table[i - 1][k - (int) listaContadores.get(i).getAt()] + listaContadores.get(i).getOp();
				table[i][k] = Math.max(prev, table[i - 1][k]);
			}
		}

		return table;

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

		double[][] d = maximizarDineroDadoWTT();
		
		if(d == null) System.out.println("No se han encontrado roturas");
		else {
			for(int i=0; i<d.length; i++) {
				for(int j=0; j<d[i].length; j++) {
					System.out.printf(d[i][j] + "\t");
				}
				System.out.println();
			}
		}


	}

}
