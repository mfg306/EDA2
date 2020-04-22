package org.eda2.testPractica01;

import static org.junit.Assert.*;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import org.eda2.practica01.*;

public class DameroTest {
	
	@Test
	public void TestInicializarContadoresDameroPar() {
		Damero damero = new Damero(4,4);//Si es par de 4x4, no puede haber ninguna manzana que sea nula
		boolean resultado = true;
		ParEdificios[][] pE = damero.getDamero();
		
		for(int i=0; i<pE.length; i++) {
			for(int j=0; j<pE[i].length; j++) {
				//Esto no deber�a de pasar
				if(pE[i][j].getcDerecha() == null || pE[i][j].getcIzquierda() == null) resultado = false; 
			}
		}
				
		Assert.assertTrue(resultado);
	}
	
	@Test
	public void TestInicializarContadoresDameroImpar() {
		Damero damero = new Damero(3,3); //Si es impar de 3x3, los contadores de la izquierda de la ultima columna tienen que ser null
		boolean resultado = true;
		ParEdificios[][] pE = damero.getDamero();
		int contador = 0;
		
		for(int i=0; i<pE[0].length; i++) {
			if(pE[0][i].getcIzquierda() == null) {
				resultado = false;
				contador++;
			}
		}
		Assert.assertFalse(resultado);
		Assert.assertEquals(contador, pE[0].length);
	}
	
	@Test
	public void TestPerdidaDemasiadoGrandeSeEscapaLaPresion() {
		//El test de si hay un reventon se queda a 0
		Damero damero = new Damero(4,4);
		ParEdificios[][] pE = damero.getDamero();
		boolean reventon = false;
		boolean casillaACero = false;
		
		for(int i=0; i<pE.length; i++) {
			for(int j=0; j<pE[i].length; j++) {
				if(pE[i][j].getMan() == null || j==pE[i].length-1) continue;
				if(pE[i][j].getMan().getPresion() < (pE[i][j+1].getMan().getPresion()- 0.5*pE[i][j+1].getMan().getPresion())) {
					reventon = true;
				}
				if(pE[i][j].getMan().getPresion() == 0.0) casillaACero = true;
			}
		}

		//Tienen que ir emparejadas
		if(reventon || casillaACero) {
			Assert.assertTrue(reventon && casillaACero);
		}
		
	}
	
	@Test
	public void TestInicializarContadores() {
		Damero damero = new Damero(3,3);
		double resultadoEsperado = damero.getLitrosEdificio(1, 2, damero.getDamero(),"D");
		double resultadoSuma = damero.getLitrosCasillasSalvoCasillaGeneral();

		Assert.assertTrue(resultadoEsperado >= resultadoSuma);
	}
	
	@Test
	public void TestSoloHayContadoresMoradosEnUltimaFila() {
		Damero damero = new Damero(4,4);
		ParEdificios[][] pE = damero.getDamero();
		boolean resultado = true;
		int contador = 0;
				
		for(int i=0; i<pE.length; i++) {
			for(int j=0; j<pE[i].length; j++) {
				if(i==pE.length-1 && j==pE[i].length-1) continue;
				if(pE[i][j].getcMorado() != null) contador++;
				else {
					if(pE[i][j].getcMorado() != null) {
						resultado = false;
						break;
					}
				}
			}
		}
		Assert.assertEquals(resultado, true);
		Assert.assertEquals(contador, pE.length-1); //HAY QUE QUITAR LA CASILLA GENERAL, PORQUE AQUI NO HAY
	}
	
	@Test
	public void TestNoHayContadoresVerdesEnLaUltimaFila() {
		Damero damero = new Damero(4,4);
		ParEdificios[][] pE = damero.getDamero();
		boolean resultado = true;
		int j = pE[0].length-1;
		
		for(int i=0; i<pE.length; i++) {
			if(pE[i][j].getcVerde() != null) {
				resultado = false;
				break;
			}
		}
		Assert.assertEquals(resultado, true);
	}
	
	@Test
	public void TestContadorVerdeSumaIzquierdaYDerechaSituacionPar() {
		Damero damero = new Damero(4,4);
		ParEdificios[][] pE = damero.getDamero();
		boolean resultado = true;
		
		for(int i=0; i<pE.length; i++) {
			if(!resultado) break;
			for(int j=1; j<pE[i].length; j++) {
				if (j==1) {
					if(pE[i][j].getcVerde().getConsumo() != (pE[i][j].getcDerecha().getConsumo() + pE[i][j].getcIzquierda().getConsumo() + pE[i][j-1].getcDerecha().getConsumo() + pE[i][j-1].getcIzquierda().getConsumo())) {
						resultado = false;
						break;
					}
				} else {
					if(pE[i][j].getcVerde() != null && pE[i][j].getcVerde().getConsumo() != (pE[i][j].getcDerecha().getConsumo() + pE[i][j].getcIzquierda().getConsumo() + pE[i][j-1].getcVerde().getConsumo())) {
						resultado = false;
						break;
					}
				}
				
			}
		}
		Assert.assertTrue(resultado);
	}
	
	@Test
	public void TestConsumoExcesivoTroncalPar() { 
		Damero damero = new Damero(4,4);
		ArrayList<Object> contadores = new ArrayList<>();
		boolean condicion = false;
		ArrayList<Object> encontrados = new ArrayList<>();
				
		ParEdificios[] troncal = damero.lineaTroncal();
		ParEdificios[] troncalMedia = damero.lineaTroncalMedia();
		
		//Vamos a buscar en el damero de forma bruta si hay algun caso en el que haya una rotura
		for(int i=0; i<troncal.length; i++) {
			if(troncal[i].getcDerecha() != null && troncal[i].getcDerecha().getConsumo() > 7*troncalMedia[i].getcDerecha().getConsumo()) {
				condicion = true; 
				encontrados.add(troncal[i].getcDerecha().getConsumo());
				break;
			}
			if(troncal[i].getcIzquierda()!= null && troncal[i].getcIzquierda().getConsumo() > 7*troncalMedia[i].getcIzquierda().getConsumo()) {
				condicion = true; 
				encontrados.add(troncal[i].getcIzquierda().getConsumo());
				break;
			}
			if(troncal[i].getcMorado() != null && troncal[i].getcMorado().getConsumo() > 7*troncalMedia[i].getcMorado().getConsumo()) {
				condicion = true; 
				encontrados.add(troncal[i].getcMorado().getConsumo());
				break;
			}
			
			if(troncal[i].getcVerde() != null && troncal[i].getcVerde().getConsumo() > 7*troncalMedia[i].getcVerde().getConsumo()) {
				condicion = true; 
				encontrados.add(troncal[i].getcVerde().getConsumo());
				break;
			}
		}
		
		contadores = damero.consumoExcesivoTroncal();

		//Si hemos encontrado una rotura, entonces contadores no puede estar vacio
		if(!condicion) {
			Assert.assertTrue(contadores.isEmpty());
		} else {
			Assert.assertTrue(!contadores.isEmpty()); 
		}
	}
	
	@Test
	public void TestConsumoExcesivoTroncalImPar() { 
		Damero damero = new Damero(5,5);
		ArrayList<Object> contadores = new ArrayList<>();
		boolean condicion = false;
		ArrayList<Object> encontrados = new ArrayList<>();
				
		ParEdificios[] troncal = damero.lineaTroncal();
		ParEdificios[] troncalMedia = damero.lineaTroncalMedia();
		
		//Vamos a buscar en el damero de forma bruta si hay algun caso en el que haya una rotura
		for(int i=0; i<troncal.length; i++) {
			if(troncal[i].getcDerecha() != null && troncal[i].getcDerecha().getConsumo() > 7*troncalMedia[i].getcDerecha().getConsumo()) {
				condicion = true; 
				encontrados.add(troncal[i].getcDerecha().getConsumo());
				break;
			}
			if(troncal[i].getcIzquierda()!= null && troncal[i].getcIzquierda().getConsumo() > 7*troncalMedia[i].getcIzquierda().getConsumo()) {
				condicion = true; 
				encontrados.add(troncal[i].getcIzquierda().getConsumo());
				break;
			}
			if(troncal[i].getcMorado() != null && troncal[i].getcMorado().getConsumo() > 7*troncalMedia[i].getcMorado().getConsumo()) {
				condicion = true; 
				encontrados.add(troncal[i].getcMorado().getConsumo());
				break;
			}
			
			if(troncal[i].getcVerde() != null && troncal[i].getcVerde().getConsumo() > 7*troncalMedia[i].getcVerde().getConsumo()) {
				condicion = true; 
				encontrados.add(troncal[i].getcVerde().getConsumo());
				break;
			}
		}
		
		contadores = damero.consumoExcesivoTroncal();

		//Si hemos encontrado una rotura, entonces contadores no puede estar vacio
		if(!condicion) {
			Assert.assertTrue(contadores.isEmpty());
		} else {
			Assert.assertTrue(!contadores.isEmpty()); 
		}
	}
	
	
	@Test
	public void TestConsumoExcesivoLineasDistribucionPar() {
		Damero damero = new Damero(4,4);
		ArrayList<Object> contadores = new ArrayList<>();
		boolean condicion = false;
		contadores = damero.consumoExcesivoLineasDistribucion();
		int numeroLineas = 2;
		ParEdificios[] dist;
		ParEdificios[] media;
		
		//Vamos a hacer lo mismo que antes. Vamos a buscar las roturas de forma bruta. Si encontramos al menos una, 
		//entonces nuetro algoritmo tiene que haber encontrado tambi�n al menos una.
		
		for(int i=0; i<numeroLineas; i++) {
			dist = damero.lineasDistribucion(i);
			media = damero.lineasDistribucionMedias(i);	
			for(int j=0; j<dist.length; j++) {
				if(dist[j].getcDerecha().getConsumo() > 7*media[j].getcDerecha().getConsumo()) {
					condicion = true;
					break;
				}
				if(dist[j].getcIzquierda().getConsumo() > 7*media[j].getcIzquierda().getConsumo()) {
					condicion = true;
					break;
				}
				if(dist[j].getcMorado()!= null && dist[j].getcMorado().getConsumo() > 7*media[j].getcMorado().getConsumo()) {
					condicion = true;
					break;
				}
			}
		}
		
		//Si hemos encontrado una rotura, entonces contadores no puede estar vacio
		if(!condicion) {
			Assert.assertTrue(contadores.isEmpty());
		} else {
			Assert.assertTrue(!contadores.isEmpty());
		}
	}
	
	@Test
	public void TestConsumoExcesivoLineasDistribucionImPar() {
		Damero damero = new Damero(5,5);
		ArrayList<Object> contadores = new ArrayList<>();
		boolean condicion = false;
		contadores = damero.consumoExcesivoLineasDistribucion();
		int numeroLineas = 2;
		ParEdificios[] dist;
		ParEdificios[] media;
		
		//Vamos a hacer lo mismo que antes. Vamos a buscar las roturas de forma bruta. Si encontramos al menos una, 
		//entonces nuetro algoritmo tiene que haber encontrado tambi�n al menos una.
		
		for(int i=0; i<numeroLineas; i++) {
			dist = damero.lineasDistribucion(i);
			media = damero.lineasDistribucionMedias(i);	
			for(int j=0; j<dist.length; j++) {
				if(dist[j].getcDerecha().getConsumo() > 7*media[j].getcDerecha().getConsumo()) {
					condicion = true;
					break;
				}
				if(dist[j].getcIzquierda()!=null && dist[j].getcIzquierda().getConsumo() > 7*media[j].getcIzquierda().getConsumo()) {
					condicion = true;
					break;
				}
				if(dist[j].getcMorado()!= null && dist[j].getcMorado().getConsumo() > 7*media[j].getcMorado().getConsumo()) {
					condicion = true;
					break;
				}
			}
		}
		
		//Si hemos encontrado una rotura, entonces contadores no puede estar vacio
		if(!condicion) {
			Assert.assertTrue(contadores.isEmpty());
		} else {
			Assert.assertTrue(!contadores.isEmpty());
		}
	}
	
	@Test
	public void TestIntervaloPresionCasillaGeneral() {
		Damero damero = new Damero(4,4);
		
		double presionCasillaGeneral = damero.getDamero()[1][3].getMan().getPresion();
		boolean condicion = (presionCasillaGeneral > Damero.PRESION_MINIMA && presionCasillaGeneral < Damero.PRESION_MAXIMA);
		
		Assert.assertTrue(condicion);
	}
	
	@Test
	public void TestDecrementoDePresionLineaTroncal() {
		Damero damero = new Damero(4,4);
		ParEdificios[] troncal = damero.lineaTroncal();
		boolean condicion = true;
		
		for(int i=0; i<troncal.length; i++) {
			if(i==troncal.length-1) break;
			if(troncal[i].getMan().getPresion() > troncal[i+1].getMan().getPresion()) {
				condicion = false;
				break;
			}
		}
		
		Assert.assertTrue(condicion);
	}
	
	@Test
	public void TestDecrementoDePresionLineasDistribucion() {
		Damero damero = new Damero(4,4);
		ParEdificios[] lineas;
		int n = damero.getDamero().length;
		boolean condicion = true;
		
		for(int i=0; i<n; i++) {
			lineas = damero.lineasDistribucion(i);
			for(int j=0; j<lineas.length; j++) {
				if(j == lineas.length -1 || j == 0) continue;
				if(lineas[j].getMan().getPresion() > lineas[j].getMan().getPresion()) {
					condicion = false;
					break;
				}
			}
		}

		Assert.assertTrue(condicion);
	}
	
	@Test
	public void TestNoHayManometrosEnLaPrimeraFila() {
		Damero damero = new Damero(4,4);
		ParEdificios[][] pE = damero.getDamero();
		boolean condicion = true;
		int j=0;
		
		for(int i=0; i<pE.length; i++) {
			if(pE[i][j].getMan() != null) {
				condicion = false;
				break;
			}
		}
		Assert.assertTrue(condicion);		
	}
	
	
	@Test 
	public void TestDiminuyeLaPresion() {
		//Tiene que salir menos presi�n de la que entra 
		Damero damero = new Damero(5,5);
		
		int filas = damero.getDamero().length-1;
		int columnas = damero.getDamero()[0].length-1;
		
		double presionCasillaOrigen = damero.getPresionPar(filas, columnas); //casilla general, donde se inicia todo
		double presionCasillaFinal = damero.getPresionPar(0, 1); 
		
		Assert.assertTrue(presionCasillaOrigen > presionCasillaFinal);
		
	}
	
	@Test
	public void TestPerdidaPresionTroncal() {
		Damero damero = new Damero(4,4);
		boolean rotura = false;
		ParEdificios[] troncal = damero.lineaTroncal();
		ArrayList<Integer> roturas = damero.perdidaExcesivaPresionTroncal();
		ArrayList<Integer> roturasI = new ArrayList<>(); //para ver obtenemos el mismo resultado
		

		for(int i=0; i<troncal.length; i++) {
			if(i == troncal.length-1) continue;
			if(troncal[i].getMan().getPresion() < (troncal[i+1].getMan().getPresion() - (troncal[i+1].getMan().getPresion()*0.1))){
				rotura = true;
				roturasI.add(troncal[i].getMan().getId());
			}
		}

		if(rotura) {
			Assert.assertTrue(!roturas.isEmpty());
			Assert.assertEquals(roturas, roturasI);
		} else {		
			Assert.assertTrue(roturas.isEmpty());
		}
		
	}
	
	@Test
	public void TestPerdidaPresionLineasDistribucion() {
		Damero damero = new Damero(4,4);
		boolean rotura = false;
		ParEdificios[] linea;
		int numLineas = damero.getDamero().length;
		ArrayList<Integer> roturasI = new ArrayList<>(); //para ver obtenemos el mismo resultado
		ArrayList<Integer> resultado = damero.perdidaExcesivaPresionLineasDistribucion();

		for(int i=0; i<numLineas; i++) {
			linea = damero.lineasDistribucion(i);
			for(int j=1; j<linea.length; j++) { //En la primera fila no hay manometros
				if(j==linea.length-1) continue;
				if(linea[j].getMan().getPresion() < (linea[j+1].getMan().getPresion() - (linea[j+1].getMan().getPresion()*0.1))){
					rotura = true;
					roturasI.add(linea[j].getMan().getId());
				}
			}
		}
		
		if(!rotura) {
			Assert.assertTrue(resultado.isEmpty());

		} else {
			Assert.assertTrue(!resultado.isEmpty());
			Assert.assertEquals(resultado, roturasI);
		}
	}
	
	
	@Test
	public void TestTiemposConsumoExcesivo() {
		long inicio = 0, fin = 0;
		long sumaTiempos = 0;
		int contador = 0;
		ArrayList<Object> problemaTroncal;
		ArrayList<Object> problemaLineas; 
		

		while(contador != 10) { //Vamos a hacer 10 veces cada i, pero buscando las roturas
			Damero damero = new Damero(3500,3500); //4096 OutOfMemoryError
			inicio = System.nanoTime();
			problemaTroncal = damero.consumoExcesivoTroncal();
			problemaLineas = damero.consumoExcesivoLineasDistribucion();
		
			fin = System.nanoTime();
			if(!problemaTroncal.isEmpty() || !problemaLineas.isEmpty()) { //El problema puede tener roturas en una u otro
				sumaTiempos += (fin-inicio);
				contador ++;
			}
		}
		System.out.println(sumaTiempos/10);

	}
	
	@Test
	public void TestTiemposPerdidaExcesiva() {
		long inicio = 0, fin = 0;
		long sumaTiempos = 0;
		int contador = 0;
		ArrayList<Integer> problemaTroncal;
		ArrayList<Integer> problemaLineas; 
		

		while(contador != 10) { //Vamos a hacer 10 veces cada i, pero buscando las roturas
			Damero damero = new Damero(4,4); //MAX 4096
			inicio = System.nanoTime();
			problemaTroncal = damero.perdidaExcesivaPresionTroncal();
			problemaLineas = damero.perdidaExcesivaPresionLineasDistribucion();
			fin = System.nanoTime();
			if(!problemaTroncal.isEmpty() || !problemaLineas.isEmpty()) { //El problema puede tener roturas en una u otro
				sumaTiempos += (fin-inicio);
				contador ++;
			}
		}
		//System.out.println(sumaTiempos/10);

	}

}
