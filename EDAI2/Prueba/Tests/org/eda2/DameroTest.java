package org.eda2;

import static org.junit.Assert.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class DameroTest {
	
	
	@Test
	public void TestInicializarContadoresDameroPar() {
		Damero damero = new Damero(4,4,500000); //Si es par de 4x4, no puede haber ninguna manzana que sea nula
		boolean resultado = true;
		ParEdificios[][] pE = damero.getDamero();
		
		for(int i=0; i<pE.length; i++) {
			for(int j=0; j<pE[i].length; j++) {
				//Esto no debería de pasar
				if(pE[i][j].getcDerecha() == null || pE[i][j].getcIzquierda() == null) resultado = false; 
			}
		}
				
		Assert.assertTrue(resultado);
	}
	
	@Test
	public void TestInicializarContadoresDameroImpar() {
		Damero damero = new Damero(3,3,500000); //Si es impar de 3x3, los contadores de la derecha de la ultima columna tienen que ser null
		boolean resultado = true;
		ParEdificios[][] pE = damero.getDamero();
		int columnas = pE.length-1;
		int contador = 0;
		
		for(int i=0; i<pE[0].length; i++) {
			if(pE[columnas][i].getcDerecha() == null) { //Los contadores derechos de la ultima columna tienen que estar todos a null
				resultado = false;
				contador++;
			}
		}
		
		Assert.assertFalse(resultado);
		Assert.assertEquals(contador, pE[0].length);
	}
	
	@Test
	public void TestCasillaGeneralPar() {
		Damero damero = new Damero(4,4,300000);
		double resultado = 300000.0;
		double resultadoObtenido = damero.getLitrosEdificio(4, 4, damero.getDamero());
		
		Assert.assertTrue(resultado - resultadoObtenido == 0.0);
	} 
	
	@Test
	public void TestCasillaGeneralImpar() {
		Damero damero = new Damero(3,3,300000);
		double resultado = 300000;
		double resultadoObtenido = damero.getLitrosEdificio(3, 3, damero.getDamero());
		
		Assert.assertTrue(resultado - resultadoObtenido == 0.0);
	}
	
	
	@Test
	public void TestInicializarContadores() { //Este ya no sirve -> Adaptarlo
		Damero damero = new Damero(3,3,300000);
		double resultadoEsperado = damero.getLitrosEdificio(3, 3, damero.getDamero());
		double resultadoSuma = damero.getLitrosCasillasSalvoCasillaGeneral();			
		
		//Puede que de todo lo que les estemos dando, no lo consuman todo
		//Lo que sí que no puede pasar es que consuman más de lo que hay
		Assert.assertTrue(resultadoEsperado >= resultadoSuma);
		Assert.assertTrue(false); 
	}
	
	@Test
	public void TestSoloHayContadoresMoradosEnUltimaFila() {
		Damero damero = new Damero(4,4,300000);
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
		Damero damero = new Damero(4,4,300000);
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
		Damero damero = new Damero(4,4,300000);
		ParEdificios[][] pE = damero.getDamero();
		boolean resultado = true;
		
		for(int i=0; i<pE.length; i++) {
			if(!resultado) break;
			for(int j=0; j<pE[i].length; j++) {
				if(pE[i][j].getcVerde() != null && pE[i][j].getcVerde().getConsumo() != pE[i][j].getcDerecha().getConsumo() + pE[i][j].getcIzquierda().getConsumo()) {
					resultado = false;
					break;
				}
			}
		}
		Assert.assertTrue(resultado);
	}
	
	
	@Test
	public void TestDatosFueraDeRango() {
		
		try {
			Damero damero = new Damero(4,4,600000);
		}catch(Exception e) {
			assertEquals(e.getMessage(), "Ha introducido demasiada agua y se han roto las tuberías.");
		}
		
		try {
			Damero damero = new Damero(4,4,10000);
		}catch(Exception e) {
			assertEquals(e.getMessage(), "El sistema no puede funcionar con tan poco cauce.");
		}
	}
	
	@Test
	public void TestConsumoExcesivoTroncal() { 
		Damero damero = new Damero(20,20,300000);
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
	public void TestConsumoExcesivoLineasDistribucion() {
		Damero damero = new Damero(5,5,300000);
		ArrayList<Object> contadores = new ArrayList<>();
		boolean condicion = false;
		int numeroLineas = damero.getDamero().length;
		contadores = damero.consumoExcesivoLineasDistribucion();
		ParEdificios[] dist;
		ParEdificios[] media;
		
		//Vamos a hacer lo mismo que antes. Vamos a buscar las roturas de forma bruta. Si encontramos al menos una, 
		//entonces nuetro algoritmo tiene que haber encontrado también al menos una.
		
		for(int i=0; i<numeroLineas; i++) {
			dist = damero.lineasDistribucion(i);
			media = damero.lineasDistribucionMedias(i);	
			for(int j=0; j<dist.length; j++) {
				if(dist[j].getcDerecha() != null && dist[j].getcDerecha().getConsumo() > 7*media[j].getcDerecha().getConsumo()) {
					condicion = true;
					break;
				}
				if(dist[j].getcIzquierda() != null && dist[j].getcIzquierda().getConsumo() > 7*media[j].getcIzquierda().getConsumo()) {
					condicion = true;
					break;
				}
				if(dist[j].getcMorado()!= null && dist[j].getcMorado().getConsumo() > 7*media[j].getcMorado().getConsumo()) {
					condicion = true;
					break;
				}
				if(dist[i].getcVerde() != null && dist[i].getcVerde().getConsumo() > 7*media[i].getcVerde().getConsumo()) {
					condicion = true;
					break;
				}

			}
		}
		
		//Si hemos encontrado una rotura, entonces contadores no puede estar vacio
		if(!condicion) {
			Assert.assertTrue(contadores.isEmpty());
		} else {
//			System.out.println("Si quito esta mierda no me pasa el test?? " + contadores.toString());
			Assert.assertTrue(!contadores.isEmpty());
		}
		
	}
	
	@Test
	public void TestIntervaloPresionCasillaGeneral() {
		Damero damero = new Damero(4,4,300000);
		
		double presionCasillaGeneral = damero.getDamero()[0][3].getMan().getPresion();
		boolean condicion = (presionCasillaGeneral > Damero.PRESION_MINIMA && presionCasillaGeneral < Damero.PRESION_MAXIMA);
		
		Assert.assertTrue(condicion);
	}
	
	
	@Test
	public void TestTiemposConsumoExcesivo() {
		long inicio = 0, fin = 0;
		long sumaTiempos = 0;
		ArrayList<Object> resultado = new ArrayList<Object>();
		String resultadoFinal = "";
		int contador = 0;
		ArrayList<Object> problemaTroncal;
		ArrayList<Object> problemaLineas; 
		
		
		//Estudiamos el problema de la troncal 
		while(contador != 10) {
			Damero damero = new Damero(4,4,400000);
			problemaTroncal = damero.consumoExcesivoTroncal();
			if(!problemaTroncal.isEmpty()) { 
				//Solo vamos a medir los tiempos para aquellos casos en los que haya rotura, si no no tiene sentido
				inicio = System.nanoTime();
				resultado.addAll(problemaTroncal);
				resultadoFinal += damero.interpretarSolucionConsumoExcesivo(problemaTroncal);
				System.out.println(resultadoFinal);
				fin = System.nanoTime();
				contador++;
			}
		}
		
		sumaTiempos += (fin-inicio);
		System.out.println("Tiempo medio: " + sumaTiempos/10);
		sumaTiempos = 0;
		contador = 0;
		
		//Estudiamos el problema de las lineas 
		while(contador != 10) {
			Damero damero = new Damero(4,4,400000);
			problemaLineas = damero.consumoExcesivoLineasDistribucion();
			if(!problemaLineas.isEmpty()) { 
				//Solo vamos a medir los tiempos para aquellos casos en los que haya rotura, si no no tiene sentido
				inicio = System.nanoTime();
				resultado.addAll(problemaLineas);
				resultadoFinal += damero.interpretarSolucionConsumoExcesivo(problemaLineas);
				System.out.println(resultadoFinal);
				fin = System.nanoTime();
				contador++;
			}
		}
		
			sumaTiempos += (fin-inicio);
			System.out.println("Tiempo medio: " + sumaTiempos/10);
			
	}
	



}
