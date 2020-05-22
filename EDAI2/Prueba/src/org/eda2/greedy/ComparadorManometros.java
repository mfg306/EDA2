package org.eda2.greedy;
import java.util.Comparator;

/**
 * @author marta y alex
 *
 */
public class ComparadorManometros implements Comparator<Manometro> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Manometro m1, Manometro m2) {
		return m1.compareTo(m2);
	}

}
