package org.eda2.backtracking;

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
			if (!resultado)
				break;
			for (int j = 1; j < pE[i].length; j++) {
				if (j == 1) {
					if (pE[i][j].getcVerde()
							.getConsumo() != (pE[i][j].getcDerecha().getConsumo()
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

		d.setParEdificioContador(1, 0, c); // Rotura
		d.setParEdificioContador(1, 0, new Contador(0, 1, 0, "D"));

		d.setParEdificioContador(1, 1, new Contador(0, 1, 1, "I"));
		d.setParEdificioContador(1, 1, new Contador(0, 1, 1, "D"));
		d.setParEdificioContador(1, 1, new Contador(0, 1, 1, "V"));

		d.setParEdificioContador(1, 2, new Contador(0, 1, 2, "I"));
		d.setParEdificioContador(1, 2, new Contador(0, 1, 2, "D"));

		expected.put(c, (5 * c.getConsumo() + 12 * ((c.getConsumo() / c.getMedia().getConsumo()) - 7)) + Damero.BA);

		Assert.assertEquals(expected, d.establecerListaATRoturas(d.resolverConsumidoresGreedy()));
	}

	@Test
	public void TestMinimizarMI() {
		Damero d = new Damero(3, 3, 1, 7, 100, 2000);

		ArrayList<Contador> roturas = d.resolverConsumidoresVersionContadores();
		d.generarOPTest(roturas);
		d.establecerListaATRoturasTest(roturas);

		System.out.println(d.minimizarMI(roturas));
	}

	@Test
	public void TestMinimizarMIAvanzado() {
		Damero d = new Damero(3, 3, 1, 7, 100, 2000);

		ArrayList<Contador> roturas = new ArrayList<>();

		Contador c1 = new Contador();
		c1.setOp(50.0);
		c1.setMedia(new Contador());
		Contador c2 = new Contador();
		c2.setOp(3000.0);
		c2.setMedia(new Contador());
		Contador c3 = new Contador();
		c3.setOp(1500.0);
		c3.setMedia(new Contador());

		roturas.add(c1);
		roturas.add(c2);
		roturas.add(c3);

		System.out.println(d.minimizarMI(roturas));

	}

	@Test
	public void TestMinimizarWTTDadosMI() {

		Damero d = new Damero(3, 3, 1, 7, 100, 2000);

		ArrayList<Contador> roturas = new ArrayList<>();

		Contador c1 = new Contador();
		c1.setOp(50.0);
		c1.setAt(50.0);
		c1.setMedia(new Contador());
		Contador c2 = new Contador();
		c2.setOp(3000.0);
		c2.setAt(3000.0);
		c2.setMedia(new Contador());
		Contador c3 = new Contador();
		c3.setOp(1500.0);
		c3.setAt(1500.0);
		c3.setMedia(new Contador());

		roturas.add(c1);
		roturas.add(c2);
		roturas.add(c3);

		System.out.println(d.minimizarWTTDadosMI(d.minimizarMI(roturas)));
	}

	@Test
	public void TestTiemposMinimizarWTTDadosMI() {
		int n = 100;
		Damero d = new Damero(3, 3, 1, 7, 100, 2000);
		ArrayList<Contador> listaContadores = new ArrayList<>();
		int contador = 0;
		long ini = 0, fin = 0, sum = 0;

		// Tamaño del problema -> numero de roturas

		while (contador != 10) {

			while (listaContadores.size() != n) {
				Contador c = new Contador();
				c.setMedia(new Contador());
				listaContadores.add(c);
			}

			d.establecerListaATRoturasTest(listaContadores);
			d.generarOPTest(listaContadores);

			ini = System.nanoTime();
			d.minimizarWTTDadosMI(d.minimizarMI(listaContadores));
			fin = System.nanoTime();

			sum += (fin - ini);
			contador++;
		}
		
		System.out.println(sum/10);

	}

}