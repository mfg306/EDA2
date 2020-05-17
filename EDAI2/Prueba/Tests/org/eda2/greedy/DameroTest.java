package org.eda2.greedy;

import java.util.ArrayList;

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
	public void TestInicializarManometrosDameroPar() {
		Damero damero = new Damero(4,4);
		ParEdificios[][] pE = damero.getDamero();
		boolean manometroNulo = false;
		for (int i = 0;i<pE.length;i++) {
			for (int j = 0;j<pE[0].length;j++) { //No puede haber ningun manometro sin inicializar
				if (pE[i][j].getMan() == null) manometroNulo = true;
			}
		}
		Assert.assertTrue(manometroNulo);
	}
	
	
	@Test
	public void TestInicializarManometrosDameroImar() {
		Damero damero = new Damero(3,3);
		ParEdificios[][] pE = damero.getDamero();
		boolean manometroNulo = false;
		for (int i = 0;i<pE.length;i++) {
			for (int j = 0;j<pE[0].length;j++) { //No puede haber ningun manometro sin inicializar
				if (pE[i][j].getMan() == null) manometroNulo = true;
			}
		}
		Assert.assertTrue(manometroNulo);
	}
	
	
	//TESTS DE COMPROBACIÓN DE CONTADORES
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
	public void TestContadorVerdeSumaIzquierdaYDerechaSituacionPar() { //Este test fallaba (ya esta arreglado). Si la rotura es propia no tiene por que sumar
		Damero damero = new Damero(4,4);
		ParEdificios[][] pE = damero.getDamero();
		boolean resultado = true;
		
		for(int i=0; i<pE.length; i++) {
			if(!resultado) break;
			for(int j=1; j<pE[i].length; j++) {
				if (j==1) {
					if(pE[i][j].getcVerde().getConsumo() != (pE[i][j].getcDerecha().getConsumo() + pE[i][j].getcIzquierda().getConsumo() + pE[i][j-1].getcDerecha().getConsumo() + pE[i][j-1].getcIzquierda().getConsumo())) {
						if (!damero.comprobarRoturaPropia(pE[i][j].getcVerde(),damero.obtenerCoordenadas(pE[i][j].getcVerde()))) {
							resultado = false;
							break;
						}
						
					}
				} else {
					if(pE[i][j].getcVerde() != null && pE[i][j].getcVerde().getConsumo() != (pE[i][j].getcDerecha().getConsumo() + pE[i][j].getcIzquierda().getConsumo() + pE[i][j-1].getcVerde().getConsumo())) {
						if (!damero.comprobarRoturaPropia(pE[i][j].getcVerde(),damero.obtenerCoordenadas(pE[i][j].getcVerde()))) {
							resultado = false;
							break;
						}
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
	
	
	//TESTS COMPROBACIÓN MANÓMETROS
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
		Damero damero = new Damero(45,45);
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
		Damero damero = new Damero(25,25);
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
	public void TestObtenerCoordenadas() {
		Damero d = new Damero(3,3);
		String[] expected = {"0","1", "D"};
		Contador c = new Contador(5000);
		
		d.setParEdificio(0, 1, c, "D");
		
		String[] result = d.obtenerCoordenadas(c);
		
		Assert.assertArrayEquals(expected, result);

	}
	
	@Test 
	public void TestComprobarRoturaPropiaVerde() { //TEST A.2
		
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
	public void TestComprobarRoturaPropiaMorado() { //TEST A.2
		
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

		String[] coordenadas = d.obtenerCoordenadas(c);
		boolean resultado = d.comprobarRoturaPropia(c, coordenadas);
		Assert.assertTrue(resultado);
		
	}
	
	
	@Test 
	public void TestDiminuyeLaPresion() {
		//Tiene que salir menos presión de la que entra 
		Damero damero = new Damero(100,100);
		
		int filas = damero.getDamero().length-1;
		int columnas = damero.getDamero()[0].length-1;
		
		double presionCasillaOrigen = damero.getPresionPar(filas, columnas); //casilla general, donde se inicia todo
		double presionCasillaFinal = damero.getPresionPar(0, 1); 
		
		Assert.assertTrue(presionCasillaOrigen > presionCasillaFinal);
	}
	
	@Test
	public void TestResolverContadoresGreedy() {
		Damero damero = new Damero(5,5);
		
		damero.resolverContadoresRoturaPropiaGreedy();
	}
	
	@Test
	public void TestManometrosGreedy() { //TEST A.1
		Damero damero = new Damero(3,3);
		Manometro m1 = new Manometro(42); //DA FALLO
		Manometro m2 = new Manometro(44); //DA FALLO
		
		Manometro m3 = new Manometro(50); //NO DA FALLO
		Manometro m4 = new Manometro(47); //NO DA FALLO
		
		damero.getDamero()[1][2].setMan(m3); // CASILLA GENERAL
		damero.getDamero()[0][2].setMan(m4); // LINEA GENERAL
		damero.getDamero()[1][1].setMan(m2); //DEBAJO CASILLA GENERAL
		damero.getDamero()[0][1].setMan(m1); //DEBAJO LINEA GENERAL
		
		
		ArrayList<Manometro> resultado = damero.resolverManometrosGreedy();
		boolean contiene = resultado.contains(m1) && resultado.contains(m2);
		boolean noContiene1 = !resultado.contains(m3) && !resultado.contains(m4);
		
		
		Assert.assertTrue(noContiene1); //Comprobamos que los manometros que no dan fallo no estan en la lista
		Assert.assertTrue(contiene); //Comprobamos que los manometros que dan fallo estan en la lista	
	}
	
	@Test
	public void TestManometrosGreedy2() { //TEST A.1 AHORA LA ROTURA ESTÁ EN LA LINEA GENERAL
		Damero damero = new Damero(3,3);
		
		Manometro m1 = new Manometro(50); //NO DA FALLO
		Manometro m2 = new Manometro(44); //DA FALLO
		Manometro m3 = new Manometro(45); //NO DA FALLO
		Manometro m4 = new Manometro(42); //NO DA FALLO
		
		damero.getDamero()[1][2].setMan(m1); // CASILLA GENERAL
		damero.getDamero()[0][2].setMan(m2); // LINEA GENERAL
		damero.getDamero()[1][1].setMan(m3); //DEBAJO CASILLA GENERAL
		damero.getDamero()[0][1].setMan(m4); //DEBAJO LINEA GENERAL
		
		
		ArrayList<Manometro> resultado = damero.resolverManometrosGreedy();
		boolean contiene = resultado.contains(m2);
		boolean noContiene = !resultado.contains(m1) && !resultado.contains(m3) && !resultado.contains(m4);
		
		Assert.assertTrue(noContiene);
		Assert.assertTrue(contiene);	
	}
	
//	@Test
//	public void TestConsumidoresGreedy() {
//		Damero damero = new Damero(3,3);
//		
//		damero.setParEdificio(0, 0, new Contador(1), "D");
//		damero.setParEdificio(0, 1, new Contador(2), "D");
//		damero.setParEdificio(0, 2, new Contador(3), "D");
//		
//		damero.setParEdificio(1, 0, new Contador(4), "I");
//		damero.setParEdificio(1, 1, new Contador(5), "I");
//		damero.setParEdificio(1, 2, new Contador(6), "I");
//		
//		damero.setParEdificio(1, 0, new Contador(7), "D");
//		damero.setParEdificio(1, 1, new Contador(8), "D");
//		damero.setParEdificio(1, 2, new Contador(9), "D");
//		
//		
//		damero.resolverConsumidoresGreedy();
//	}


}