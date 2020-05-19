package org.eda2.dynamic;
import java.util.Comparator;

public class ComparadorContadoresAT implements Comparator<Contador>{

	@Override
	public int compare(Contador c1, Contador c2) {
		return Double.compare(c1.getAt(), c2.getAt());
	}

	
	
}
