package org.eda2.backtracking;
import java.util.Comparator;

public class ComparadorContadoresOP implements Comparator<Contador>{
	
	@Override
	public int compare(Contador c1, Contador c2) {
		return Double.compare(c1.getOp(), c2.getOp());
	}

}
