package org.eda2;

import static org.junit.Assert.*;
import org.junit.jupiter.api.Test;

public class DameroTest {
	
	@Test
	public void TestInicializarContadores() {
		Damero damero = new Damero(4,4);

//		damero.inicializarContadores();
		damero.setSuministroAgua(150);
		
		System.out.println(damero.toString());
		
		//La casilla general tiene que tener más litros que cualquier otra casilla
		assertTrue(damero.getLitrosEdificio(4, 4) > damero.getLitrosEdificio(0, 1));
		assertTrue(damero.getLitrosEdificio(4, 4) > damero.getLitrosEdificio(0, 2));
		assertTrue(damero.getLitrosEdificio(4, 4) > damero.getLitrosEdificio(0, 3));
		assertTrue(damero.getLitrosEdificio(4, 4) > damero.getLitrosEdificio(1, 1));
		assertTrue(damero.getLitrosEdificio(4, 4) > damero.getLitrosEdificio(2, 3));


		
		//Además, su cantidad de agua, debe ser igual a la suma del resto de casillas
		double resultadoEsperado = damero.getLitrosEdificio(4, 4);
		double resultadoSuma = damero.getLitrosCasillasSalvoCasillaGeneral();
		
		System.out.println(resultadoEsperado);
		System.out.println(resultadoSuma);
		
		assertEquals(resultadoEsperado, resultadoSuma);
		
	}
	
	@Test
	public void TestDatosFueraDeRango() {
		Damero damero = new Damero(4,4);
		
		try {
			damero.setSuministroAgua(200);
		}catch(Exception e) {
			assertEquals(e.getMessage(), "Ha introducido demasiada agua y se han roto las tuberías.");
		}
		
		
		try {
			damero.setSuministroAgua(20);
		}catch(Exception e) {
			assertEquals(e.getMessage(), "El sistema no puede funcionar con tan poco cauce.");
		}
		
	}


	
	@Test
	public void TestInicializarDameros() {
		
	}

}