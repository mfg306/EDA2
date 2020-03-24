package org.eda2;

import static org.junit.Assert.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class DameroTest {
	
	@Test
	public void TestInicializarContadores() {
		Damero damero = new Damero(4,4);
	
		
		//La cantidad de agua, debe ser igual a la suma del resto de casillas
		double resultadoEsperado = damero.getLitrosEdificio(4, 4);
		double resultadoSuma = damero.getLitrosCasillasSalvoCasillaGeneral();
				
		//¿POR QUÉ FALLA SI SON IGUALES????????
		
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
	public void TestAlgoritmoRecursivo() {
		Damero damero = new Damero(4,4);
		
		ArrayList<Contador> contadores = new ArrayList<>();
		
		contadores = damero.consumoExcesivoTroncal();
		
		System.out.println(contadores.toString());
		
		

	}


	
	@Test
	public void TestInicializarDameros() {
		
	}

}
