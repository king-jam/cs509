package client.reservation;

import java.util.Comparator;

public class TimeComparator implements Comparator<ReservationOption> {
	@Override
	public int compare(ReservationOption r1, ReservationOption r2) {
		String price1 = r1.getTotalTime();
		String price2 = r2.getTotalTime();
		return price1.compareTo(price2);
	}
}
