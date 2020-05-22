package org.eda2.greedy;

import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author alex y marta
 *
 */
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
	public void TestComprobarRoturaPropiaVerde() {
		
		//Vamos a hacer un damero personalizado en el que sepamos que hay rotura propia y luego llamamos al metodo para que lo compruebe
		
		Damero d = new Damero(3,3);
		Contador c = new Contador(5000,1,1,"V");
		
		
		d.setParEdificioContador(0, 0, new Contador(0,0,0,"D"));
		
		d.setParEdificioContador(0, 1, new Contador(0,0,1,"D"));
		d.setParEdificioContador(0, 1, new Contador(0,0,1,"V"));
		
		d.setParEdificioContador(0, 2, new Contador(0,0,2,"D"));
		d.setParEdificioContador(0, 2, new Contador(0,0,2,"M"));
		
		d.setParEdificioContador(1, 0, new Contador(20,1,0,"I"));
		d.setParEdificioContador(1, 0, new Contador(20,1,0,"D"));
		
		d.setParEdificioContador(1, 1, new Contador(20,1,1,"I"));
		d.setParEdificioContador(1, 1, new Contador(20,1,1,"D"));
		d.setParEdificioContador(1, 1, c);
		
		d.setParEdificioContador(1, 2, new Contador(0,1,2,"I"));
		d.setParEdificioContador(1, 2, new Contador(0,1,2,"D"));
		
		String[] coords = {Integer.toString(c.getI()), Integer.toString(c.getJ()), c.getTipo()};

		
		Assert.assertTrue((d.comprobarRoturaPropia(c , coords)));
	}
	
	@Test 
	public void TestComprobarRoturaPropiaMorado() {
		
		//Vamos a hacer un damero personalizado en el que sepamos que hay rotura propia y luego llamamos al metodo para que lo compruebe
		
		Damero d = new Damero(3,3);
		Contador c = new Contador(5000,0,2,"M");
		//columna, fila
		d.setParEdificioContador(0, 0, new Contador(0,0,0,"D"));
		
		d.setParEdificioContador(0, 1, new Contador(0,0,1,"D"));
		d.setParEdificioContador(0, 1, new Contador(20,0,1,"V"));
		
		d.setParEdificioContador(0, 2, new Contador(20,0,2,"D"));
		d.setParEdificioContador(0, 2, c);
		
		d.setParEdificioContador(1, 0, new Contador(0,1,0,"I"));
		d.setParEdificioContador(1, 0, new Contador(0,1,0,"D"));
		
		d.setParEdificioContador(1, 1, new Contador(0,1,1,"I"));
		d.setParEdificioContador(1, 1, new Contador(0,1,1,"D"));
		d.setParEdificioContador(1, 1, new Contador(0,1,1,"V"));
		
		d.setParEdificioContador(1, 2, new Contador(0,1,2,"I") );
		d.setParEdificioContador(1, 2, new Contador(0,1,2,"D"));

		String[] coords = {Integer.toString(c.getI()), Integer.toString(c.getJ()), c.getTipo()};
		
		Assert.assertTrue((d.comprobarRoturaPropia(c , coords)));
		
	}
	
	
	@Test
	public void TestRoturaPropia() {
		Damero d = new Damero(3,3);
		Contador c = new Contador(25000,0,2,"M");
		//columna, fila
		d.setParEdificioContador(0, 0, new Contador(0,0,0,"D"));
		
		d.setParEdificioContador(0, 1, new Contador(0,0,1,"D"));
		d.setParEdificioContador(0, 1, new Contador(20,0,1,"V"));
		
		d.setParEdificioContador(0, 2, new Contador(20,0,2,"D"));
		d.setParEdificioContador(0, 2, c);
		
		d.setParEdificioContador(1, 0, new Contador(0,1,0,"I"));
		d.setParEdificioContador(1, 0, new Contador(0,1,0,"D"));
		
		d.setParEdificioContador(1, 1, new Contador(0,1,1,"I"));
		d.setParEdificioContador(1, 1, new Contador(0,1,1,"D"));
		d.setParEdificioContador(1, 1, new Contador(0,1,1,"V"));
		
		d.setParEdificioContador(1, 2, new Contador(0,1,2,"I"));
		d.setParEdificioContador(1, 2, new Contador(0,1,2,"D"));
		
		Assert.assertTrue(d.roturaPropia(c));
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
		Damero d = new Damero(3,3);
		Contador c1 = new Contador(25000,0,2,"M");
		Contador c2 = new Contador(14000,1,1,"V");
		ArrayList<Integer> expectedResult = new ArrayList<>();
		Integer id1 = c1.getId();
		Integer id2 = c2.getId();
		
		expectedResult.add(id1);
		expectedResult.add(id2);
		
		d.setParEdificioContador(0, 0, new Contador(0,0,0,"D"));
		
		d.setParEdificioContador(0, 1, new Contador(0,0,1,"D"));
		d.setParEdificioContador(0, 1, new Contador(20,0,1,"V"));
		
		d.setParEdificioContador(0, 2, new Contador(20,0,2,"D"));
		d.setParEdificioContador(0, 2, c1);
		
		d.setParEdificioContador(1, 0, new Contador(0,1,0,"I"));
		d.setParEdificioContador(1, 0, new Contador(0,1,0,"D"));
		
		d.setParEdificioContador(1, 1, new Contador(0,1,1,"I"));
		d.setParEdificioContador(1, 1, new Contador(0,1,1,"D"));
		d.setParEdificioContador(1, 1, c2);
		
		d.setParEdificioContador(1, 2, new Contador(0,1,2,"I"));
		d.setParEdificioContador(1, 2, new Contador(0,1,2,"D"));
		
		System.out.println(d.resolverContadoresRoturaPropiaGreedy());
		System.out.println(expectedResult);
		
		Assert.assertEquals(d.resolverContadoresRoturaPropiaGreedy(), expectedResult);	
		
	}
	
	@Test
	public void TestResolverManometrosGreedyLineasDistribucion() { 
		Damero d = new Damero(3,3);
		ArrayList<Integer> expected = new ArrayList<>();
		
		Manometro m1 = new Manometro();
		Manometro m2 = new Manometro();
		Manometro m3 = new Manometro();
		Manometro m4 = new Manometro();
		Manometro m5 = new Manometro();
		Manometro m6 = new Manometro();
		
		m1.setPresion(200.0);
		m2.setPresion(90.0); //ROTURA
		m3.setPresion(200.0); 
		m4.setPresion(198.5);
		m5.setPresion(197.0);
		m6.setPresion(196.9);
		
		expected.add(m2.getId());

		d.setParEdificioManometro(0, 0, m2);
		d.setParEdificioManometro(0, 1, m1);
		d.setParEdificioManometro(0, 2, m3);
		d.setParEdificioManometro(1, 0, m4);
		d.setParEdificioManometro(1, 1, m5);
		d.setParEdificioManometro(1, 2, m6);
		
		Assert.assertEquals(d.resolverManometrosGreedy(), expected);

	}
	
	@Test
	public void TestResolverManometrosGreedyLineaTroncal() { 
		Damero d = new Damero(3,3);
		ArrayList<Integer> expected = new ArrayList<>();
		
		Manometro m1 = new Manometro();
		Manometro m2 = new Manometro();
		Manometro m3 = new Manometro();
		Manometro m4 = new Manometro();
		Manometro m5 = new Manometro();
		Manometro m6 = new Manometro();
		
		m1.setPresion(200.0);
		m2.setPresion(199.0); 
		m3.setPresion(50.0); //ROTURA
		m4.setPresion(198.5);
		m5.setPresion(197.0);
		m6.setPresion(196.9);
		
		expected.add(m3.getId());

		d.setParEdificioManometro(0, 0, m2);
		d.setParEdificioManometro(0, 1, m1);
		d.setParEdificioManometro(0, 2, m3);
		d.setParEdificioManometro(1, 0, m4);
		d.setParEdificioManometro(1, 1, m5);
		d.setParEdificioManometro(1, 2, m6);
		
		Assert.assertEquals(d.resolverManometrosGreedy(), expected);
	}
	
	
	@Test
	public void TestResolverConsumidoresGreedy() {
		Damero d = new Damero(3,3);
		Contador c1 = new Contador(10000,1,0,"D");
		Contador c2 = new Contador(140000,1,1,"I");
		ArrayList<Integer> expectedResult = new ArrayList<>();
		Integer id1 = c1.getId();
		Integer id2 = c2.getId();
		
		expectedResult.add(id2);
		expectedResult.add(id1);

		
		d.setParEdificioContador(0, 0, new Contador(0,0,0,"D"));
		
		d.setParEdificioContador(0, 1, new Contador(0,0,1,"D"));
		d.setParEdificioContador(0, 1, new Contador(20,0,1,"V"));
		
		d.setParEdificioContador(0, 2, new Contador(20,0,2,"D"));
		d.setParEdificioContador(0, 2, new Contador(20,0,2,"M"));
		
		d.setParEdificioContador(1, 0, new Contador(0,1,0,"I"));
		d.setParEdificioContador(1, 0, c1);
		
		d.setParEdificioContador(1, 1, c2);
		d.setParEdificioContador(1, 1, new Contador(0,1,1,"D"));
		d.setParEdificioContador(1, 1, new Contador(20,1,1,"V")); //ROTURA
		
		d.setParEdificioContador(1, 2, new Contador(0,1,2,"I"));
		d.setParEdificioContador(1, 2, new Contador(0,1,2,"D"));
		d.resolverConsumidoresGreedy();
		
		Assert.assertTrue(expectedResult.equals(d.resolverConsumidoresGreedy()));
	}
	
	@Test
	public void TestTiemposResolverContadoresGreedy() {
		int n=3; //Con los pares no encuentra nunca roturas propias
		long inicio = 0, fin = 0, sumaTiempos = 0; 
		int contador = 0;
		ArrayList<Integer> resultado = new ArrayList<>();
		
		while(contador != 10) {
			Damero d = new Damero(n,n);
			inicio = System.nanoTime();
			resultado = d.resolverContadoresRoturaPropiaGreedy();
			fin = System.nanoTime();
			if(!resultado.isEmpty()) { //Vamos a descartar aquellos casos en los que no haya roturas
				sumaTiempos += (fin-inicio);
				contador++;
			}
		}
		
		System.out.println(sumaTiempos/10);
	}
	

	@Test
	public void TestTiemposResolverConsumidoresGreedy() {
		int n=3;
		long inicio = 0, fin = 0, sumaTiempos = 0; 
		int contador = 0;
		ArrayList<Integer> resultado = new ArrayList<>();
		
		while(contador != 10) {
			Damero d = new Damero(n,n,n*1000, n*10000);
			inicio = System.nanoTime();
			resultado = d.resolverConsumidoresGreedy();
			fin = System.nanoTime();
			if(!resultado.isEmpty()) { //Vamos a descartar aquellos casos en los que no haya roturas
				sumaTiempos += (fin-inicio);
				contador++;
			}
			
		}
		
		System.out.println(sumaTiempos/10);
	}
	
	
	@Test
	public void TesTiempostResolverManometrosGreedy() { 
		int n=3; //MAX: 1024
		long inicio = 0, fin = 0, sumaTiempos = 0; 
		int contador = 0;
		ArrayList<Integer> resultado = new ArrayList<>();
		
		while(contador != 10) {
			Damero d = new Damero(n,n);
			inicio = System.nanoTime();
			resultado = d.resolverManometrosGreedy();
			fin = System.nanoTime();
			if(!resultado.isEmpty()) { //Vamos a descartar aquellos casos en los que no haya roturas
				sumaTiempos += (fin-inicio);
				contador++;
			}
		}
		
		System.out.println(sumaTiempos/10);
		
	}
	

}