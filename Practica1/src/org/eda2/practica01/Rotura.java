package org.eda2.practica01;

import java.io.*;
import java.util.*;

public class Rotura {

	private int nMan=0;
	private int nCont = 0;
	
	public static void main(String[] args) {
		
		
		
		//C:\WORKSPACES\EDAII\Practicas\src\org\edaii\practica01
		
		String directorioEntrada = System.getProperty("user.dir") + File.separator +
		           "src" + File.separator + 
		           "org" + File.separator +
		           "edaii" + File.separator + 
		           "practica01" + File.separator;
		System.out.println(directorioEntrada + "ArchivoEjemplo.txt");
				   
		cargarArchivo(directorioEntrada + "ArchivoEjemplo.txt");
		
	}
	
	public void comprobarRotura() {
		
	}
	
	public static void cargarArchivo(String archivo) {
		Scanner scan = null;
		String linea;
		String[] items;
		int m = 0;
		int n = 0;
		int j = 0;
		double[][] man;
		//nMan = 0;
		int primeraLinea = 0;
		try {
			scan = new Scanner(new File(archivo));
		} catch (IOException e) {
			System.out.println("Error al cargar el archivo");
			System.exit(-1);
		}
		while(scan.hasNextLine()){
			linea = scan.nextLine();
			if (linea.isEmpty()) continue;
			items = linea.split("[,]");
			if (primeraLinea==0) {
				m = Integer.parseInt(items[0]);
				n = Integer.parseInt(items[1]);
				primeraLinea++;
				System.out.println("linea de coordenadas");
			} else {
				man = new double[m][n];
				for (int i = 0;i<m;i++) {
					//man[i][j] = Double.parseDouble(items[i]);
				}
			}
			
			//if (!this.mapa.containsKey(items[0])) mapa.put(items[0], new TreeMap<String, TreeSet<String>>());
			//if (this.mapa.get(items[0]).get(items[1]) == null) mapa.get(items[0]).put(items[1], new TreeSet<String>());
			//if (!this.mapa.get(items[0]).get(items[1]).contains(items[2])) mapa.get(items[0]).get(items[1]).add(items[2]);
		}
		scan.close();
	}
}
