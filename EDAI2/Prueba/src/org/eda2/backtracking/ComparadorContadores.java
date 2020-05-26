package org.eda2.backtracking;

import java.util.Comparator;

public class ComparadorContadores implements Comparator<Contador> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 * El criterio para comparar va a ser en funcion de si la 
	 */
	@Override
	public int compare(Contador c1, Contador c2) {
		//Para que sea mas sencillo voy a hacerlo con contadores directamente, por lo tanto a cada contador le he metido tambien
		//como atributo su media 
		
		double diferencia1 = c1.getConsumo()/c1.getMedia().getConsumo();
		double diferencia2 = c2.getConsumo()/c2.getMedia().getConsumo();
		
		return (diferencia1 > diferencia2) ?  1 : (diferencia1 == diferencia2) ? 0 : -1; 
		
		
		
	}

}
