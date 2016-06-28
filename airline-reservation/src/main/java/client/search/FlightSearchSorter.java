package client.search;


import java.util.ArrayList;
import java.util.Collections;
import client.reservation.*;

/**
 * This class performs sort based on reservation options 
 * We didn't implement Compareable, so we have to do this all manually
 * 
 * @author Forkey
 * @version 1
 * @since 06/28/2016
 */
public class FlightSearchSorter {
	
	public ArrayList<ReservationOption> sortPrice(boolean ascending, ArrayList<ReservationOption> resOptions) {
		ArrayList<ReservationOption> sortedReservations = new ArrayList<ReservationOption>();
		
		for (ReservationOption resOption : resOptions) {
			ReservationOption cheapestReservation = cheapestReservation(resOptions);
			sortedReservations.add(cheapestReservation);
			resOptions.remove(resOption);
		}
		if (!ascending) {
			Collections.reverse(sortedReservations);
		}
		return sortedReservations;
	}
	
	public ArrayList<ReservationOption> sortTime(boolean ascending, ArrayList<ReservationOption> resOptions) {
		ArrayList<ReservationOption> sortedReservations = new ArrayList<ReservationOption>();
		
		for (ReservationOption resOption : resOptions) {
			ReservationOption fastestReservation = fastestReservation(resOptions);
			sortedReservations.add(fastestReservation);
			resOptions.remove(resOption);
		}
		if (!ascending) {	
			Collections.reverse(sortedReservations);
		}
		return sortedReservations;
	}
	
	public ReservationOption cheapestReservation(ArrayList<ReservationOption> resOptions) {
		ReservationOption cheapestRes = resOptions.get(0);
		for (ReservationOption resOption: resOptions) {
			//TODO: need to consider coach seating
			if (resOption.getPrice(true) < cheapestRes.getPrice(true)) {
				cheapestRes = resOption;
			}
		}
		return cheapestRes;
	}
	
	public ReservationOption fastestReservation(ArrayList<ReservationOption> resOptions) {
		ReservationOption fastestRes = resOptions.get(0);
		//We could use int or double here, IDC.  Used LONG because thatis technically what timeUnit uses.
		long hours = 0;
		long minutes = 0;
		long seconds = 0;
		String totalTime = resOptions.get(0).getTotalTime();
		getTimeValues(totalTime, hours, minutes, seconds);

		for (ReservationOption resOption: resOptions) {
			String resTotalTime = resOption.getTotalTime();
			long resHours = 0;
			long resMinutes = 0;
			long resSeconds = 0;
			getTimeValues(resTotalTime, resHours, resMinutes, resSeconds);

			if (resHours < hours) {
				fastestRes = resOption;
			} else if (resHours == hours) {
				if (resMinutes < minutes){
					fastestRes = resOption;
				} else if (resMinutes == minutes) {
					if (resSeconds < seconds) {
						fastestRes = resOption;
					}
				}
			}
		}
		return fastestRes;
	}
	
	private void getTimeValues(String totalTime, long hours, long minutes, long seconds) {
		String[] timeValues = totalTime.split(":");
		hours = Long.parseLong(timeValues[0]);
		minutes = Long.parseLong(timeValues[1]);
		seconds = Long.parseLong(timeValues[2]);
	}
	


}