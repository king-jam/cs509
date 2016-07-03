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
	
	public void sortPrice(
			boolean ascending,
			ArrayList<ReservationOption> resOptions,
			String seatType) {
		
		if(seatType.equals("firstclass")) {
			Collections.sort(resOptions, new PriceComparatorFirstClass());
		} else if(seatType.equals("coach")) {
			Collections.sort(resOptions, new PriceComparatorCoach());
		}
		if (!ascending) {
			Collections.reverse(resOptions);
		}
	}
	
	public void sortTime(boolean ascending, ArrayList<ReservationOption> resOptions) {
		Collections.sort(resOptions, new TimeComparator());
		if (!ascending) {	
			Collections.reverse(resOptions);
		}
	}
}