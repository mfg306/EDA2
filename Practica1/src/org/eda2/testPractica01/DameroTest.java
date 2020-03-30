package org.eda2.testPractica01;

import static org.junit.Assert.*;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import org.eda2.practica01.*;

public class DameroTest {
	
	@Test
	public void TestInicializarContadoresDameroPar() { //Me he liado con la implementacion 
		Damero damero = new Damero(4,4,500000);
		//Si es par de 4x4, no puede haber ninguna manzana que sea nula
				
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
	public void TestInicializarContadoresDameroImpar() { //Me he liado con la implementacion 
		Damero damero = new Damero(3,3,500000);
		//Si es impar de 3x3, los contadores de la izquierda de la primera columna tienen que ser null
				
		boolean resultado = true;
		ParEdificios[][] pE = damero.getDamero();
		int i = 0;

		for(int j=0; j<pE[i].length; j++) {
			if(pE[i][j].getcIzquierda() != null) resultado = false;
		}

		Assert.assertTrue(resultado);
	}
	
	
	@Test
	public void TestInicializarContadores() {
		Damero damero = new Damero(4,4,300000);
	
		double resultadoEsperado = damero.getLitrosEdificio(4, 4);
		double resultadoSuma = damero.getLitrosCasillasSalvoCasillaGeneral();			
		
		//Puede que de todo lo que les estemos dando, no lo consuman todo
		//Lo que sí que no puede pasar es que consuman más de lo que hay
		Assert.assertTrue(resultadoEsperado >= resultadoSuma);
		
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
	public void TestAlgoritmoRecursivo() {
		Damero damero = new Damero(4,4,300000);
		ArrayList<Contador> contadores = new ArrayList<>();
		
		//contadores = damero.consumoExcesivoTroncal();
	}
	
	
	

//	@Test
//	public void TestInicializarDameros() {
//		
//	}

}
