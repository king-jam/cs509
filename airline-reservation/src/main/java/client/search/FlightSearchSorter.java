package client.search;


import java.util.ArrayList;
import java.util.Collections;
import client.reservation.*;

/**
 * This class performs sort based on reservation options 
 * 
 * @author Forkey
 * @version 1
 * @since 06/28/2016
 */
public class FlightSearchSorter {

	/**
	 * This method takes a list of ReservationOption objects and sorts them
	 * based on price in ascending or descending order depending on input
	 * @param ascending (required) true if ascending, false if descending order
	 * @param resOptions list(required) the reservation options to sort
	 * @param seatType (required) The type of seat to display "firstclass" or "coach" 
	 */
	public void sortPrice(
			boolean ascending,
			ArrayList<ReservationOption> resOptions,
			String seatType) {

		if(seatType.equals("FirstClass")) {
			Collections.sort(resOptions, new PriceComparatorFirstClass());
		} else if(seatType.equals("Coach")) {
			Collections.sort(resOptions, new PriceComparatorCoach());
		}
		if (!ascending) {
			Collections.reverse(resOptions);
		}
	}
	/**
	 * This method takes a list of ReservationOption objects and sorts them
	 * based on price in ascending or descending order depending on input
	 * @param ascending (required) true if ascending, false if descending order
	 * @param resOptions list(required) the reservation options to sort
	 */
	public void sortTime(boolean ascending, ArrayList<ReservationOption> resOptions) {
		Collections.sort(resOptions, new TimeComparator());
		if (!ascending) {
			Collections.reverse(resOptions);
		}
	}
}