package org.eda2;

import static org.junit.Assert.*;
import org.junit.jupiter.api.Test;

public class DameroTest {
	
	@Test
	public void TestInicializarContadores() {
		Damero damero = new Damero(4,4);
		
		damero.inicializarContadores();
		
		//La casilla general tiene que tener más litros que cualquier otra casilla
		assertTrue(damero.getLitrosEdificio(4, 4) > damero.getLitrosEdificio(0, 1));
		
		//Además, su cantidad de agua, debe ser igual a la suma del resto de casillas
		double resultadoEsperado = damero.getLitrosEdificio(4, 4);
		double resultadoSuma = damero.getLitrosCasillasSalvoCasillaGeneral();
		
		assertEquals(resultadoEsperado, resultadoSuma);
		
	}
	
	@Test
	public void TestInicializarDameros() {
		
	}

}
