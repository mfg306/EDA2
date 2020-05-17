package org.eda2.greedy;
import java.util.Comparator;

public class ComparadorManometros implements Comparator<Manometro> {

	@Override
	public int compare(Manometro m1, Manometro m2) {
		return m1.compareTo(m2);
	}

}
