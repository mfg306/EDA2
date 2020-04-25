package org.eda2.dyv;
import java.util.ArrayList;

/**
 * Programa que muestra el funcionamiento
 * @author marta
 *
 */
public class Programa {

	public static void main(String[] args) {
		Damero damero= new Damero(4,4);

		ArrayList<Integer> roturasContadoresTroncal;
		ArrayList<Integer> roturasContadoresLineas;
		ArrayList<Integer> roturasContadoresTotal = new ArrayList<>();
		ArrayList<Integer> roturasManometrosTroncal;
		ArrayList<Integer> roturasManometrosLineas;
		ArrayList<Integer> roturasManometrosTotal = new ArrayList<>();
		
			

		System.out.println("A continuación, le mostramos los datos de su damero: ");
		System.out.println(damero.toString());
		
		
		roturasManometrosTroncal = damero.perdidaExcesivaPresionTroncal();
		roturasManometrosTotal.addAll(roturasManometrosTroncal);
		roturasManometrosLineas = damero.perdidaExcesivaPresionLineasDistribucion();
		if(!roturasManometrosTotal.containsAll(roturasManometrosLineas)) roturasManometrosTotal.addAll(roturasManometrosLineas);
		
		if(!roturasManometrosTotal.isEmpty()) {
			System.out.println("Hemos encontrado las siguientes roturas de primer grado:");
			System.out.println(damero.interpretarSolucionPerdidaExcesivo(roturasManometrosTotal));

		} else {
			System.out.println("No se han encontrado roturas de primer grado.");
		}
		
		roturasContadoresTroncal = damero.consumoExcesivoTroncal();
		roturasContadoresLineas = damero.consumoExcesivoLineasDistribucion();
		roturasContadoresTotal.addAll(roturasContadoresTroncal);
		if(!roturasContadoresTotal.containsAll(roturasContadoresLineas)) roturasContadoresTotal.addAll(roturasContadoresLineas);

		if(!roturasContadoresTotal.isEmpty()) {
			System.out.println("Hemos encontrado las siguientes roturas de segundo grado:");
			System.out.println(damero.interpretarSolucionConsumoExcesivo(roturasContadoresTotal));
		}else {
			System.out.println("No se han encontrado roturas de segundo grado.");
		}
	}

}
