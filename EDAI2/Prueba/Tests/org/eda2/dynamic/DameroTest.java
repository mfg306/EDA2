package org.eda2.dynamic;

import org.junit.Assert;
import org.junit.Test;

public class DameroTest {
	
	//TESTS DE INICICALIZACIÓN
	@Test
	public void TestInicializarContadoresDameroPar() {
		Damero damero = new Damero(4,4);//Si es par de 4x4, no puede haber ninguna manzana que sea nula
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
	public void TestSoloHayContadoresMoradosEnUltimaFila() {
		Damero damero = new Damero(100,100);
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
		Damero damero = new Damero(100,100);
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
	public void TestComprobarCasillaGeneral() {
		Damero damero = new Damero(3,3);
		double consumoCasillaGeneral = damero.getLitrosEdificio(1, 2, damero.getDamero(),"D");
		double resultadoSuma = damero.getLitrosCasillasSalvoCasillaGeneral();

		Assert.assertTrue(consumoCasillaGeneral >= resultadoSuma);
	}
	
	@Test
	public void TestObtenerCoordenadas() {
		Damero d = new Damero(3,3);
		String[] expected = {"0","1", "D"};
		Contador c = new Contador(5000);
		
		d.setParEdificio(0, 1, c, "D");
		
		String[] result = d.obtenerCoordenadas(c);
		
		Assert.assertArrayEquals(expected, result);

	}
	
	@Test 
	public void TestComprobarRoturaPropiaVerde() {
		
		//Vamos a hacer un damero personalizado en el que sepamos que hay rotura propia y luego llamamos al metodo para que lo compruebe
		
		Damero d = new Damero(3,3);
		Contador c = new Contador(5000);
		//columna, fila
		d.setParEdificio(0, 0, new Contador(0), "D");
		
		d.setParEdificio(0, 1, new Contador(0), "D");
		d.setParEdificio(0, 1, new Contador(0), "V");
		
		d.setParEdificio(0, 2, new Contador(0), "D");
		d.setParEdificio(0, 2, new Contador(0), "M");
		
		d.setParEdificio(1, 0, new Contador(20), "I");
		d.setParEdificio(1, 0, new Contador(20), "D");
		
		d.setParEdificio(1, 1, new Contador(20), "I");
		d.setParEdificio(1, 1, new Contador(20), "D");
		d.setParEdificio(1, 1, c, "V");
		
		d.setParEdificio(1, 2, new Contador(0), "I");
		d.setParEdificio(1, 2, new Contador(0), "D");

		Assert.assertTrue((d.comprobarRoturaPropia(c , d.obtenerCoordenadas(c))));
		
	}
	
	@Test 
	public void TestComprobarRoturaPropiaMorado() {
		
		//Vamos a hacer un damero personalizado en el que sepamos que hay rotura propia y luego llamamos al metodo para que lo compruebe
		
		Damero d = new Damero(3,3);
		Contador c = new Contador(5000);
		//columna, fila
		d.setParEdificio(0, 0, new Contador(0), "D");
		
		d.setParEdificio(0, 1, new Contador(0), "D");
		d.setParEdificio(0, 1, new Contador(20), "V");
		
		d.setParEdificio(0, 2, new Contador(20), "D");
		d.setParEdificio(0, 2, c, "M");
		
		d.setParEdificio(1, 0, new Contador(0), "I");
		d.setParEdificio(1, 0, new Contador(0), "D");
		
		d.setParEdificio(1, 1, new Contador(0), "I");
		d.setParEdificio(1, 1, new Contador(0), "D");
		d.setParEdificio(1, 1, new Contador(0), "V");
		
		d.setParEdificio(1, 2, new Contador(0), "I");
		d.setParEdificio(1, 2, new Contador(0), "D");

		Assert.assertTrue((d.comprobarRoturaPropia(c , d.obtenerCoordenadas(c))));
		
	}
	
	@Test
	public void TestListaDTRoturas() {
		
		Damero d = new Damero(3,3);
		Contador c = new Contador(5000);
		//columna, fila
		d.setParEdificio(0, 0, new Contador(0), "D");
		
		d.setParEdificio(0, 1, new Contador(0), "D");
		d.setParEdificio(0, 1, new Contador(20), "V");
		
		d.setParEdificio(0, 2, new Contador(20), "D");
		d.setParEdificio(0, 2, c, "M");
		
		d.setParEdificio(1, 0, new Contador(0), "I");
		d.setParEdificio(1, 0, new Contador(0), "D");
		
		d.setParEdificio(1, 1, new Contador(0), "I");
		d.setParEdificio(1, 1, new Contador(0), "D");
		d.setParEdificio(1, 1, new Contador(0), "V");
		
		d.setParEdificio(1, 2, new Contador(0), "I");
		d.setParEdificio(1, 2, new Contador(0), "D");
		
		d.resolverConsumidoresGreedy().toString();
		
		System.out.println(d.listaDTRoturas());
	}

	
	

}