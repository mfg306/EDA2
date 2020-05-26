package org.eda2.dynamic;

import org.junit.Assert;
import org.junit.Test;
import java.util.TreeMap;
import java.util.ArrayList;

public class DameroTest {

	// TESTS DE INICICALIZACIÓN
	@Test
	public void TestInicializarContadoresDameroPar() {
		Damero damero = new Damero(4, 4);// Si es par de 4x4, no puede haber ninguna manzana que sea nula
		boolean resultado = true;
		ParEdificios[][] pE = damero.getDamero();

		for (int i = 0; i < pE.length; i++) {
			for (int j = 0; j < pE[i].length; j++) {
				// Esto no debería de pasar
				if (pE[i][j].getcDerecha() == null || pE[i][j].getcIzquierda() == null)
					resultado = false;
			}
		}

		Assert.assertTrue(resultado);
	}

	@Test
	public void TestInicializarContadoresDameroImpar() {
		Damero damero = new Damero(3, 3); // Si es impar de 3x3, los contadores de la izquierda de la ultima columna
											// tienen que ser null
		boolean resultado = true;
		ParEdificios[][] pE = damero.getDamero();
		int contador = 0;

		for (int i = 0; i < pE[0].length; i++) {
			if (pE[0][i].getcIzquierda() == null) {
				resultado = false;
				contador++;
			}
		}
		Assert.assertFalse(resultado);
		Assert.assertEquals(contador, pE[0].length);
	}

	@Test
	public void TestSoloHayContadoresMoradosEnUltimaFila() {
		Damero damero = new Damero(100, 100);
		ParEdificios[][] pE = damero.getDamero();
		boolean resultado = true;
		int contador = 0;

		for (int i = 0; i < pE.length; i++) {
			for (int j = 0; j < pE[i].length; j++) {
				if (i == pE.length - 1 && j == pE[i].length - 1)
					continue;
				if (pE[i][j].getcMorado() != null)
					contador++;
				else {
					if (pE[i][j].getcMorado() != null) {
						resultado = false;
						break;
					}
				}
			}
		}
		Assert.assertEquals(resultado, true);
		Assert.assertEquals(contador, pE.length - 1); // HAY QUE QUITAR LA CASILLA GENERAL, PORQUE AQUI NO HAY
	}

	@Test
	public void TestNoHayContadoresVerdesEnLaUltimaFila() {
		Damero damero = new Damero(100, 100);
		ParEdificios[][] pE = damero.getDamero();
		boolean resultado = true;
		int j = pE[0].length - 1;

		for (int i = 0; i < pE.length; i++) {
			if (pE[i][j].getcVerde() != null) {
				resultado = false;
				break;
			}
		}
		Assert.assertEquals(resultado, true);
	}

	@Test
	public void TestContadorVerdeSumaIzquierdaYDerechaSituacionPar() {
		Damero damero = new Damero(4, 4);
		ParEdificios[][] pE = damero.getDamero();
		boolean resultado = true;

		for (int i = 0; i < pE.length; i++) {
			if (!resultado) break;
			for (int j = 1; j < pE[i].length; j++) {
				if (j == 1) {
					if (pE[i][j].getcVerde().getConsumo() != (pE[i][j].getcDerecha().getConsumo()
									+ pE[i][j].getcIzquierda().getConsumo() + pE[i][j - 1].getcDerecha().getConsumo()
									+ pE[i][j - 1].getcIzquierda().getConsumo())) {
						resultado = false;
						break;
					}
				} else {
					if (pE[i][j].getcVerde() != null
							&& pE[i][j].getcVerde().getConsumo() != (pE[i][j].getcDerecha().getConsumo()
									+ pE[i][j].getcIzquierda().getConsumo() + pE[i][j - 1].getcVerde().getConsumo())) {
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
		Damero damero = new Damero(3, 3);
		double consumoCasillaGeneral = damero.getLitrosEdificio(1, 2, damero.getDamero(), "D");
		double resultadoSuma = damero.getLitrosCasillasSalvoCasillaGeneral();

		Assert.assertTrue(consumoCasillaGeneral >= resultadoSuma);
	}
	
	@Test
	public void TestObtenerContadorDadoId() {
		Damero d = new Damero(3,3);
		Contador c = new Contador();
		c.setTipo("D");
		int id = c.getId();
		d.setParEdificioContador(0, 0, c);

		Assert.assertEquals(c, d.obtenerContadorDadoId(id));
	}
	
		
	@Test
	public void TestResolverConsumidoresGreedy() {
		Damero d = new Damero(3,3);
		Contador c1 = new Contador(10000,1,0,"D");
		Contador c2 = new Contador(140000,1,1,"I");
		ArrayList<Integer> expectedResult = new ArrayList<>();
		Integer id1 = c1.getId();
		Integer id2 = c2.getId();
		
		expectedResult.add(id1);
		expectedResult.add(id2);

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
	public void TestResolverConsumidoresGreedyVersionContadores() {
		Damero d = new Damero(3,3);
		Contador c1 = new Contador(10000,1,0,"D");
		Contador c2 = new Contador(140000,1,1,"I");
		ArrayList<Contador> expectedResult = new ArrayList<>();
		Contador id1 = c1;
		Contador id2 = c2;
		
		expectedResult.add(id1);
		expectedResult.add(id2);

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
		
		Assert.assertEquals(expectedResult,d.resolverConsumidoresVersionContadores());
	}
	
	
	@Test
	public void TestManzanasConsumoExcesivo() {
		
		Damero d = new Damero(3,3);
		boolean condicion = true;
		Contador c1 = new Contador();
		c1.setTipo("D");
		c1.setMedia(new Contador());
		Integer id1 = c1.getId();
		
		d.setParEdificioContador(0, 0, c1);
		
		ArrayList<Integer> cRoturas = new ArrayList<>();
		cRoturas.add(id1);
		
		TreeMap<Contador, ArrayList<Double>> manzanas = d.manzanasConsumoExcesivo(cRoturas);
		
		for(ArrayList<Double> c : manzanas.values()) {
			if(!c.get(0).equals(c1.getConsumo())) condicion = false;
			if(!c.get(1).equals(c1.getMedia().getConsumo())) condicion = false;
			if(!c.get(2).equals(c1.getConsumo()/c1.getMedia().getConsumo())) condicion = false;
		}
		
		Assert.assertTrue(condicion);
		
	}
	
	@Test
	public void TestManzanasConsumoExcesivoParaTest() {
		
		Damero d = new Damero(3,3);
		boolean condicion = true;
		Contador c1 = new Contador();
		c1.setTipo("D");
		c1.setMedia(new Contador());
		
		d.setParEdificioContador(0, 0, c1);
		
		ArrayList<Contador> cRoturas = new ArrayList<>();
		cRoturas.add(c1);
		
		TreeMap<Contador, ArrayList<Double>> manzanas = d.manzanasConsumoExcesivoParaTest(cRoturas);
		
		for(ArrayList<Double> c : manzanas.values()) {
			if(!c.get(0).equals(c1.getConsumo())) condicion = false;
			if(!c.get(1).equals(c1.getMedia().getConsumo())) condicion = false;
			if(!c.get(2).equals(c1.getConsumo()/c1.getMedia().getConsumo())) condicion = false;
		}
		
		Assert.assertTrue(condicion);
		
	}
	
	@Test
	public void TestListaATRoturas() {
		TreeMap<Contador, Double> expected = new TreeMap<>();
		Damero d = new Damero(3, 3);
		Contador c = new Contador(800000, 1, 0, "I");
		// columna, fila
		d.setParEdificioContador(0, 0, new Contador(0, 0, 0, "D"));

		d.setParEdificioContador(0, 1, new Contador(0, 0, 1, "D"));
		d.setParEdificioContador(0, 1, new Contador(20, 0, 1, "V"));

		d.setParEdificioContador(0, 2, new Contador(20, 0, 2, "D"));
		d.setParEdificioContador(0, 2, new Contador(5000, 0, 2, "M"));

		d.setParEdificioContador(1, 0, c); //Rotura 
		d.setParEdificioContador(1, 0, new Contador(0, 1, 0, "D"));

		d.setParEdificioContador(1, 1, new Contador(0, 1, 1, "I"));
		d.setParEdificioContador(1, 1, new Contador(0, 1, 1, "D"));
		d.setParEdificioContador(1, 1, new Contador(0, 1, 1, "V"));

		d.setParEdificioContador(1, 2, new Contador(0, 1, 2, "I"));
		d.setParEdificioContador(1, 2, new Contador(0, 1, 2, "D"));
		
		expected.put(c, (5*c.getConsumo() + 12*((c.getConsumo()/c.getMedia().getConsumo())-7))+ Damero.BA);
		
		Assert.assertEquals(expected, d.establecerListaATRoturas(d.resolverConsumidoresGreedy()));
	}
	

	
	@Test
	public void TestMaximizarDineroDadoWTT() {
		
		Damero d = new Damero(3,3,1,7, 150);
		double max = 0;
		ArrayList<Integer> listaRoturas = d.resolverConsumidoresGreedy();
		d.establecerListaATRoturas(listaRoturas);
		d.generarOP(listaRoturas);
		
		
		double[][] table = d.maximizarDineroDadoWTT(d.resolverConsumidoresVersionContadores());
		int filas = d.resolverConsumidoresGreedy().size();
		int columnas = Damero.WTT;
		
		//vamos a buscar el valor maximo
		if (table!=null) { //Si hay solucion
			for(int i=0; i<table.length; i++) {
				for(int j=0; j<table[i].length; j++) {
					if(table[i][j] > max) {
						max = table[i][j];
					}
				}
			}
			Assert.assertTrue(table[filas][columnas] == max);
		}
	}
	
	
	@Test
	public void TestTiemposMaximizarDineroDadoWTT() {
		
		//En este problema tenemos dos parametros: el WTT y el numero de contadores con roturas, para hacer el test
		//vamos a considerar una lista que supuestamente tiene roturas, asi podemos ir incrementandola en cada iteracion 
		Damero d = new Damero(3,3,1,7, 100);
		long ini = 0, fin = 0, suma = 0;
		int n = Damero.WTT;
		ArrayList<Contador> listaRoturasContador = new ArrayList<>();
		int contador = 0;
		
		while(contador != 10) {
			
			//Creamos una lista de contadores que tenga el mismo tamaño que WTT
			while(listaRoturasContador.size() != n) {
				Contador c = new Contador();
				c.setMedia(new Contador());
				listaRoturasContador.add(c);
			}
			
			//A esta lista le metemos los AT y OP			
			d.establecerListaATRoturasTest(listaRoturasContador);
			d.generarOPTest(listaRoturasContador);
			
			//Resolvemos el problema con esta lista
			ini = System.nanoTime();
			double[][] table = d.maximizarDineroDadoWTT(listaRoturasContador);
			fin = System.nanoTime();
			
			if(table != null) {
				suma += (fin - ini);
				contador++;
			}
		}
		
		//De esta forma, tenemos un estudio teorico mas controlado de ambos parametros. Antes, no sabiamos cuantas roturas podian 
		//aparecer puerto que surgian de forma aleatoria. Ahora vamos a estudiar problemas de nxn
		
		System.out.println(suma/10);
	}
	
	@Test
	public void TestInterpretarSolucionMaximizarDineroDadoWTT() {
		Damero d = new Damero(3,3,1,7, 9000);
		ArrayList<Integer> listaRoturas = d.resolverConsumidoresGreedy();
		d.establecerListaATRoturas(listaRoturas);
		d.generarOP(listaRoturas);
		
		if(!listaRoturas.isEmpty()) {
			//Hay que dejar esto para que se inicialize en la clase
			d.maximizarDineroDadoWTT(d.resolverConsumidoresVersionContadores());
			String lista = d.interpretarSolucionMaximizarDineroDadoWTT();
			System.out.println(lista);
		} else {
			System.out.println("No hay roturas.");
		}

	}
	
	@Test
	public void TestTiemposResolverConsumidoresGreedy() {
		int n=3;
		long inicio = 0, fin = 0, sumaTiempos = 0; 
		int contador = 0;
		ArrayList<Integer> resultado = new ArrayList<>();
		
		while(contador != 10) {
			Damero d = new Damero(n,n,n*1000, n*10000,  150);
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
		
	
}