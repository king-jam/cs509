package client.reservation;

import java.util.Comparator;

public class PriceComparatorCoach implements Comparator<ReservationOption> {
	@Override
	public int compare(ReservationOption r1, ReservationOption r2) {
		Double price1 = r1.getPrice("coach");
		Double price2 = r2.getPrice("coach");
		return price1.compareTo(price2);
	}
}
