/**
 * 
 */
package client.reservation;

import java.util.Comparator;

/**
 * This class implements a Comparator for ReservationOption based on the ticket
 * type being 'firstclass'. It provides support for utilizing libraries to do sorting
 * such as Collections.
 * 
 * @author James
 * @version 1
 * @since 07/03/2016
 *
 */
public class PriceComparatorFirstClass implements Comparator<ReservationOption> {
	
	/**
	 * compare function of ReservationOption class based on first class prices.
	 * 
	 * @param r1 is first ReservationOption to compare
	 * @param r2 is the second ReservationOption to compare
	 * 
	 * @return -1 if less than, 0 if equal, 1 if greater than
	 */
	@Override
	public int compare(ReservationOption r1, ReservationOption r2) {
		Double price1 = r1.getPrice("firstclass");
		Double price2 = r2.getPrice("firstclass");
		return price1.compareTo(price2);
	}
}
