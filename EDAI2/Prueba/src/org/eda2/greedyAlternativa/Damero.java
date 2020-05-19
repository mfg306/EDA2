package org.eda2.greedyAlternativa;

import java.util.ArrayList;
//import java.util.HashMap;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeSet;


//Puesto que lo que mas vamos a hacer es buscar, lo mejor es usar una estructura basada en arboles. Vamos a usar un HashMap
//La clave sera un string que representa las coordenadas donde estaria ubicado si estuviese en la matriz que usamos en dyv
//el valor sera un ParEdificio que representa las dos casillas 

public class Damero {
	
	private int filas;
	private int columnas; 
	
	private HashMap<String, ParEdificios> pEdificios;
	private HashMap<String, ParEdificios> matrizMedias;
	
	// Consumo minimo y maximo del contador general
	public final static double CONSUMO_MINIMO_GENERAL = 300000;
	public final static double CONSUMO_MAXIMO_GENERAL = 500000;
	// Consumo minimo y maximo medio
	public final static double CONSUMO_MINIMO_MEDIO = 108;
	public final static double CONSUMO_MAXIMO_MEDIO = 162;
	// Presiones para la casilla general
	public final static double PRESION_MAXIMA = 150;
	public final static double PRESION_MINIMA = 110;
	
	public Damero(int columnas, int filas) {
		Contador.reiniciarId();
		this.filas = filas;
		this.columnas = columnas;
		pEdificios = new HashMap<>();
		matrizMedias = new HashMap<>();
		
		this.inicializarContadoresMedia(1000, 100);
		this.inicializarContadores(9000, 900);
	}
	
	
	public Damero(int columnas, int filas, double cantInicial, double cantFinal) {
		Contador.reiniciarId();
		this.filas = filas;
		this.columnas = columnas;
		pEdificios = new HashMap<>();
		matrizMedias = new HashMap<>();
		
		this.inicializarContadoresMedia(1000, 100);
		this.inicializarContadores(cantInicial, cantFinal);
	}
	
		
	
	private void inicializarContadores(double cantInicial, double cantFinal) {
		if (columnas % 2 == 0) inicializarContadoresPar(cantInicial, cantFinal);
		else inicializarContadoresImpar(cantInicial, cantFinal);
	}
	
	private void inicializarContadoresPar(double cantInicial, double cantFinal) {
		ParEdificios aux;
		int filas = this.filas; 
		int columnas = (this.columnas+1)/2;
		
		
		for (int i = 0; i < columnas; i++) {
			for (int j = 0; j < filas; j++) {
				aux = new ParEdificios();
				// CASILLA GENERAL
				if (i != (columnas) - 1 || j != filas - 1) 
					aux.setcDerecha(new Contador(Math.random() * (cantInicial - cantFinal+ 1) + cantFinal,this.matrizMedias.get("(" + i + "," + j + ")").getcDerecha(), i,j,"D"));
				aux.setcIzquierda(new Contador(Math.random() * (cantInicial - cantFinal + 1) + cantFinal, this.matrizMedias.get("(" + i + "," + j + ")").getcIzquierda(), i,j,"I"));
				this.pEdificios.put("(" + i + "," + j + ")", aux);
			}
		}
		
		
		// INICIALIZAMOS LO CONTADORES VERDES Y MORADOS Y EL GENERAL
		double con = 0;
		for (int i = 0; i < columnas; i++) {
			for (int j = 1; j < filas; j++) {
				if (i == columnas - 1 && j == filas - 1) {
					con += this.pEdificios.get("(" + i + "," + j +")").getcIzquierda().getConsumo();
					con+= this.pEdificios.get("(" + i + "," + (j-1) + ")").getcVerde().getConsumo();
					con+= this.pEdificios.get("(" + (i-1) + "," + j + ")").getcMorado().getConsumo(); //AQUI 
					this.pEdificios.get("(" + i + "," + j + ")").setcDerecha(new Contador(con,this.matrizMedias.get("(" + i + "," + j + ")").getcDerecha(),i,j,"D"));// CONTADOR GENERAL
					continue;
				}
				if (j == filas - 1 && i != columnas - 1) { // linea de distribucion
					con += this.pEdificios.get("(" + i + "," + (j-1) +")").getcVerde().getConsumo();
					con += this.pEdificios.get("(" + i + "," + j +")").getcDerecha().getConsumo();
					con += this.pEdificios.get("(" + i + "," + j +")").getcIzquierda().getConsumo();
					if (i != 0) con += this.pEdificios.get("(" + (i-1) + "," + j +")").getcMorado().getConsumo();
					
					this.pEdificios.get("(" + i + "," + j + ")").setcMorado(new Contador(con,this.matrizMedias.get("(" + i + "," + j + ")").getcMorado(),i,j,"M"));// CONTADOR GENERAL
					
				} else if (this.pEdificios.get("(" + i + "," + (j-1) +")").getcVerde() == null) { // final de la linea de distribucion por abajo
					
					con += this.pEdificios.get("(" + i + "," + j +")").getcDerecha().getConsumo();
					con += this.pEdificios.get("(" + i + "," + j +")").getcIzquierda().getConsumo(); 
					con += this.pEdificios.get("(" + i + "," + (j-1) +")").getcDerecha().getConsumo();
					con += this.pEdificios.get("(" + i + "," + (j-1) +")").getcIzquierda().getConsumo();

					this.pEdificios.get("(" + i + "," + j + ")").setcVerde(new Contador(con,this.matrizMedias.get("(" + i + "," + j + ")").getcVerde(),i,j,"V"));// CONTADOR GENERAL
					
				} else { // caso base
					con += this.pEdificios.get("(" + i + "," + j +")").getcDerecha().getConsumo();
					con += this.pEdificios.get("(" + i + "," + j +")").getcIzquierda().getConsumo();
					con += this.pEdificios.get("(" + i + "," + (j-1) +")").getcVerde().getConsumo();
					
					this.pEdificios.get("(" + i + "," + j + ")").setcVerde(new Contador(con,this.matrizMedias.get("(" + i + "," + j + ")").getcVerde(),i,j,"V"));
				}
				con = 0;
			}
		}
	}
	
	private void inicializarContadoresImpar(double cantInicial, double cantFinal) {
		inicializarContadoresPar(cantInicial, cantFinal);
		for (int j = 0; j < this.filas; j++) {
			this.pEdificios.get("(" + 0 + "," + j + ")").setcIzquierda(null);;
		}

	}
	
	private void inicializarContadoresMedia(double cantInicial, double cantFinal) {
		if (columnas % 2 == 0) inicializarContadoresMediaPar(cantInicial, cantFinal);
		else inicializarContadoresMediaImpar(cantInicial, cantFinal);
	}
	
	private void inicializarContadoresMediaPar(double cantInicial, double cantFinal) {
		ParEdificios aux;
		int filas = this.filas; 
		int columnas = (this.columnas+1)/2;
		
		for (int i = 0; i < columnas; i++) {
			for (int j = 0; j < filas; j++) {
				aux = new ParEdificios();
				// CASILLA GENERAL
				if (i != (columnas) - 1 || j != filas - 1) 
					aux.setcDerecha(new Contador(Math.random() * (cantInicial - cantFinal+ 1) + cantFinal, i,j,"D"));
				aux.setcIzquierda(new Contador(Math.random() * (cantInicial - cantFinal + 1) + cantFinal, i,j,"I"));
				this.matrizMedias.put("(" + i + "," + j + ")", aux);
			}
		}
		
		
		// INICIALIZAMOS LO CONTADORES VERDES Y MORADOS Y EL GENERAL
		double con = 0;
		for (int i = 0; i < columnas; i++) {
			for (int j = 1; j < filas; j++) {
				if (i == columnas - 1 && j == filas - 1) {
					con += this.matrizMedias.get("(" + i + "," + j +")").getcIzquierda().getConsumo();
					con+= this.matrizMedias.get("(" + i + "," + (j-1) + ")").getcVerde().getConsumo();
					con+= this.matrizMedias.get("(" + (i-1) + "," + j + ")").getcMorado().getConsumo(); //AQUI 
					this.matrizMedias.get("(" + i + "," + j + ")").setcDerecha(new Contador(con,i,j,"D"));// CONTADOR GENERAL
					continue;
				}
				if (j == filas - 1 && i != columnas - 1) { // linea de distribucion
					con += this.matrizMedias.get("(" + i + "," + (j-1) +")").getcVerde().getConsumo();
					con += this.matrizMedias.get("(" + i + "," + j +")").getcDerecha().getConsumo();
					con += this.matrizMedias.get("(" + i + "," + j +")").getcIzquierda().getConsumo();
					if (i != 0) con += this.matrizMedias.get("(" + (i-1) + "," + j +")").getcMorado().getConsumo();
					
					this.matrizMedias.get("(" + i + "," + j + ")").setcMorado(new Contador(con,i,j,"M"));// CONTADOR GENERAL
					
				} else if (this.matrizMedias.get("(" + i + "," + (j-1) +")").getcVerde() == null) { // final de la linea de distribucion por abajo
					
					con += this.matrizMedias.get("(" + i + "," + j +")").getcDerecha().getConsumo();
					con += this.matrizMedias.get("(" + i + "," + j +")").getcIzquierda().getConsumo(); 
					con += this.matrizMedias.get("(" + i + "," + (j-1) +")").getcDerecha().getConsumo();
					con += this.matrizMedias.get("(" + i + "," + (j-1) +")").getcIzquierda().getConsumo();

					this.matrizMedias.get("(" + i + "," + j + ")").setcVerde(new Contador(con,i,j,"V"));// CONTADOR GENERAL
					
				} else { // caso base
					con += this.matrizMedias.get("(" + i + "," + j +")").getcDerecha().getConsumo();
					con += this.matrizMedias.get("(" + i + "," + j +")").getcIzquierda().getConsumo();
					con += this.matrizMedias.get("(" + i + "," + (j-1) +")").getcVerde().getConsumo();
					
					this.matrizMedias.get("(" + i + "," + j + ")").setcVerde(new Contador(con,i,j,"V"));
				}
				con = 0;
			}
		}
	}
	
	private void inicializarContadoresMediaImpar(double cantInicial, double cantFinal) {
		inicializarContadoresMediaPar(cantInicial, cantFinal);
		for (int j = 0; j < this.filas; j++) {
			this.matrizMedias.get("(" + 0 + "," + j + ")").setcIzquierda(null);;
		}

	}
	
	public void setParEdificioContador(int i, int j, Contador c) {
		c.setMedia(this.matrizMedias.get("(" + i + "," + j + ")").getcDerecha());
		c.setI(i);
		c.setJ(j);
		switch(c.getTipo()) {
		case "V":
			if(j == 0 || j == filas) throw new RuntimeException("Ahí no se debe colocar.");
			this.pEdificios.get("(" + i + "," + j + ")").setcVerde(c);
			break;
		case "M":
			if(j== 0) throw new RuntimeException("Ahí no se debe colocar.");
			this.pEdificios.get("(" + i + "," + j + ")").setcMorado(c);
			break;
		case "I":
			if(filas % 2 != 0 && i == 0) throw new RuntimeException("Ahí no se debe colocar.");
			this.pEdificios.get("(" + i + "," + j + ")").setcIzquierda(c);
			break;
		case "D":
			this.pEdificios.get("(" + i + "," + j + ")").setcDerecha(c);
			break;
		default: throw new RuntimeException("Tipo no válido");
		}
	} 
	
	public HashMap<String, ParEdificios> getParEdificios(){
		return this.pEdificios;
	}
	
	public HashMap<String, ParEdificios> getMediaParEdificios(){
		return this.matrizMedias;
	}
	
	public ParEdificios[] lineaTroncal() {
		ParEdificios[] pE = new ParEdificios[(this.columnas+1)/2];
		for (int i = 0; i < pE.length; i++) pE[i] = pEdificios.get("(" + i + "," + (this.filas-1) + ")");
		return pE;
	}

	/**
	 * 
	 * @return un array con las medias de la linea troncal
	 */
	public ParEdificios[] lineaTroncalMedia() {
		ParEdificios[] pE = new ParEdificios[(this.columnas+1)/2];
		for (int i = 0; i < pE.length; i++) pE[i] = this.matrizMedias.get("(" + i + "," + (this.filas-1) + ")");
		return pE;
	}
	
	public ParEdificios[] lineasDistribucion(int i) {
		ParEdificios[] pE = new ParEdificios[this.filas - 1]; // Le quitamos la linea troncal
		
		for (int j = 0; j < pE.length; j++) pE[j] = this.pEdificios.get("(" + i + "," + j + ")");
		return pE;
	}
	
	public ParEdificios[] lineasDistribucionMedia(int i) {
		ParEdificios[] pE = new ParEdificios[this.filas - 1]; // Le quitamos la linea troncal
		
		for (int j = 0; j < pE.length; j++) pE[j] = this.matrizMedias.get("(" + i + "," + j + ")");
		return pE;
	}
	
	
	public String toString() {
		String resultado = "";
		for(Entry<String, ParEdificios> pE : this.pEdificios.entrySet()) {
			resultado += pE.getKey() + " => " + pE.getValue() + "\n";
		}
		return resultado;
	}
	
	public String toStringMedias() {
		String resultado = "";
		for(Entry<String, ParEdificios> pE : this.matrizMedias.entrySet()) {
			resultado += pE.getKey() + " => " + pE.getValue() + "\n";
		}
		return resultado;
	}
	
	public ArrayList<Contador> obtenerCandidatosContadores() {
		ArrayList<Contador> candidatos = new ArrayList<>();
		String clave = "";
		String[] split;
		Integer i, j; 
		
		for(Entry<String, ParEdificios> pE : this.pEdificios.entrySet()) {
			clave = pE.getKey();
			//El formato era (i,j), vamos a quitarle los dos parentesis 
			clave = clave.substring(1,clave.length()-1); 
			split = clave.split(",");
			i = Integer.parseInt(split[0]);
			j = Integer.parseInt(split[1]);
			if(pE.getValue().getcMorado() != null) candidatos.add(pE.getValue().getcMorado());
			if(pE.getValue().getcVerde() != null) candidatos.add(pE.getValue().getcVerde());
			if(i.equals(((this.columnas+1)/2)-1) && j.equals(this.filas-1)) candidatos.add(pE.getValue().getcDerecha());
		}

		return candidatos;
	}
	
	public boolean roturaPropia(Contador con) { // Funcion de factibilidad
		Integer i = con.getI();
		Integer j = con.getJ();
		String[] indices = {i.toString(),j.toString(), con.getTipo()};
		if (!comprobarRoturaPropia(con, indices)) return false; // Si la rotura no es del propio contador, ya no nos interesa
		String clave = "(" + i + "," + j + ")";
		Contador media = new Contador();
		
		switch (indices[2]) {
		case "D":
			media = this.matrizMedias.get(clave).getcDerecha();
			break;
		case "I":
			media = this.matrizMedias.get(clave).getcIzquierda();
			break;
		case "V":
			media = this.matrizMedias.get(clave).getcVerde();
			break;
		case "M":
			media = this.matrizMedias.get(clave).getcMorado();
			break;
		}
		double consumo = con.getConsumo();
		double mediaCon = media.getConsumo();
		// comprueba si el consumo es mayor a la media en 5 veces
		if (consumo > mediaCon * 5) return true;
		return false;
	}
	
	public boolean comprobarRoturaPropia(Contador con, String[] indices) { // Este metodo comprueba si hay rotura																// propia
		int i = Integer.parseInt(indices[0]);
		int j = Integer.parseInt(indices[1]);
		
		String clave = "(" + i + "," + j + ")";
		String claveAbajo = "(" + i + "," + (j-1) + ")";
		String claveIzquierda = "(" + (i-1) + "," + j + ")";
		
		if (indices[2].equals("V")) {
			if (j == 1) { // Final de la linea de distribucion
				double verde = this.pEdificios.get(clave).getcVerde().getConsumo();
				double dcha = this.pEdificios.get(clave).getcDerecha().getConsumo();
				double izqda = (this.pEdificios.get(clave).getcIzquierda() != null)  ? this.pEdificios.get(clave).getcIzquierda().getConsumo() : 0;
				double abajoD = this.pEdificios.get(claveAbajo).getcDerecha().getConsumo();
				double abajoI = (this.pEdificios.get(claveAbajo).getcIzquierda() != null) ? this.pEdificios.get(claveAbajo).getcIzquierda().getConsumo() : 0;
				if (verde > (dcha + izqda + abajoD + abajoI)) return true;
			} else { // Caso mas comun
				double verde = this.pEdificios.get(clave).getcVerde().getConsumo(); 
				double dcha = this.pEdificios.get(clave).getcDerecha().getConsumo();
				double izqda = (this.pEdificios.get(clave).getcIzquierda() != null)  ? this.pEdificios.get(clave).getcIzquierda().getConsumo() : 0;
				double verde2 = this.pEdificios.get(claveAbajo).getcVerde().getConsumo();
				if (verde > (dcha + izqda + verde2)) return true;
			}
		} else if (indices[2].equals("M")) {
			double morado = this.pEdificios.get(clave).getcMorado().getConsumo(); 
			double dcha = this.pEdificios.get(clave).getcDerecha().getConsumo();
			double verde = this.pEdificios.get(claveAbajo).getcVerde().getConsumo();
			// He cambiado las dos lineas de abajo
			double izquierda = (this.pEdificios.get(clave).getcIzquierda() != null) ? this.pEdificios.get(clave).getcIzquierda().getConsumo() : 0; //FALLO 
			double moradoI = (i > 0 && this.pEdificios.get(claveIzquierda).getcMorado() != null) ? this.pEdificios.get(claveIzquierda).getcMorado().getConsumo() : 0;
			if (morado > (dcha + izquierda + verde + moradoI)) return true;

		} else { // Casilla general
			double izquierda = (this.pEdificios.get(clave).getcIzquierda() != null) ? this.pEdificios.get(clave).getcIzquierda().getConsumo() : 0;
			double derecha = this.pEdificios.get(clave).getcDerecha().getConsumo(); //general PERO SOLO PARA EL CASO DE DAMERO PAR, NO?
			double moradoI;
			double verdeAbajo;
			if(clave.equals("(0,0)")) {
				moradoI = 0; //NO hay izquierda 
				verdeAbajo = 0;
			} else {
				moradoI = this.pEdificios.get(claveIzquierda).getcMorado().getConsumo();
				verdeAbajo= this.pEdificios.get(claveAbajo).getcVerde().getConsumo();
			}
			if (derecha > (izquierda + moradoI + verdeAbajo)) return true;
		}
		return false;
	}


	
	public ArrayList<Integer> resolverContadoresRoturaPropiaGreedy() { //PROBADO
		ArrayList<Contador> candidatos = obtenerCandidatosContadores(); // todos los contadores
		ArrayList<Integer> elegidos = new ArrayList<>();
		Contador posible;
		ArrayList<Contador> candidatosOrdenados = new ArrayList<>(candidatos);
		candidatosOrdenados.sort(new ComparadorContadores());
		
		while (candidatosOrdenados.size() != 0) { // Solucion: hemos comprobado todos los contadores
			posible = candidatosOrdenados.get(0);
			candidatosOrdenados.remove(posible); // Eliminamos posible de la lista de candidatos
			if (roturaPropia(posible)) {// Devuelve true o false si el contador tiene una rotura propia o no
				elegidos.add(posible.getId());
			}
		}
		return elegidos;
	}
	
	
	public TreeSet<Contador> obtenerCandidatosConsumidores() { //AQUI SI QUE HAY QUE TENER YA EN CUENTA LOS VERDES Y ROJOS NO?
		TreeSet<Contador> candidatos = new TreeSet<>();
		String clave = "";
		String[] split;
		Integer i, j; 
		
		for(Entry<String, ParEdificios> pE : this.pEdificios.entrySet()) {
			clave = pE.getKey();
			//El formato era (i,j), vamos a quitarle los dos parentesis 
			clave = clave.substring(1,clave.length()-1); 
			split = clave.split(",");
			i = Integer.parseInt(split[0]);
			j = Integer.parseInt(split[1]);
			
			if(pE.getValue().getcIzquierda() != null) candidatos.add(pE.getValue().getcIzquierda());
			if(pE.getValue().getcDerecha() != null) {
				if( i != ((columnas+1)/2)-1 || j!= filas-1) candidatos.add(pE.getValue().getcDerecha());
			
			}
			if(pE.getValue().getcVerde() != null) candidatos.add(pE.getValue().getcVerde());
			if(pE.getValue().getcMorado() != null) candidatos.add(pE.getValue().getcMorado());

		}
		
		return candidatos;
	}
	
	public boolean roturaContador(Contador posible) {
		int i = posible.getI();
		int j = posible.getJ();
		double consumo = posible.getConsumo();
		double consumoMedio = this.matrizMedias.get("(" + i + "," + j + ")").getContador(posible.getTipo()).getConsumo();

		if (consumo > consumoMedio * 7) return true;
		return false;
	}
	
	public TreeSet<Integer> resolverConsumidoresGreedy() {
		TreeSet<Contador> candidatos = obtenerCandidatosConsumidores(); // todos los manometros
		TreeSet<Integer> elegidos = new TreeSet<>();
		Contador posible;
		ArrayList<Contador> candidatosOrdenados = new ArrayList<>(candidatos);
		candidatosOrdenados.sort(new ComparadorContadores());

		while (!candidatosOrdenados.isEmpty()) { // Solucion: hemos comprobado todos los manometros
			posible = candidatosOrdenados.get(0);
			candidatosOrdenados.remove(posible); // Eliminamos posible de la lista de candidatosÇ
			if (roturaContador(posible)) {// Devuelve true o false si el manometro tiene una rotura o no
				elegidos.add(posible.getId());
			}
		}
		return elegidos;
	}
	
	
}
