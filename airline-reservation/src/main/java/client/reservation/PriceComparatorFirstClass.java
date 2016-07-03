package client.reservation;

import java.util.Comparator;

public class PriceComparatorFirstClass implements Comparator<ReservationOption> {
	@Override
	public int compare(ReservationOption r1, ReservationOption r2) {
		Double price1 = r1.getPrice("firstclass");
		Double price2 = r2.getPrice("firstclass");
		return price1.compareTo(price2);
	}
}
