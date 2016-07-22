/**
 * 
 */
package client.airport;

import java.util.Comparator;
/**
 * This class implements a Comparator for Airports based on the code.
 * It provides support for utilizing libraries to do sorting
 * such as Collections.
 * 
 * @author James
 * @version 1
 * @since 07/22/2016
 *
 */
public class CodeComparator implements Comparator<Airport> {
	/**
	 * compare function of Airport class based on code
	 * 
	 * @param r1 is first Airport to compare
	 * @param r2 is the second Airport to compare
	 * 
	 * @return -1 if less than, 0 if equal, 1 if greater than
	 */
	public int compare(Airport r1, Airport r2) {
		String code1 = r1.code();
		String code2 = r2.code();
		return code1.compareTo(code2);
	}
}
