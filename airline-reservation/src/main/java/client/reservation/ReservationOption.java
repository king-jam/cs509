package client.reservation;

import client.flight.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ReservationOption {

	private ArrayList<Flight> flightList;
	
	
	public ReservationOption(
			Flight flightOne,
			Flight flightTwo,
			Flight flightThree) {
		if(flightOne != null) { 
			this.flightList.add(flightOne);
		}
		if(flightTwo != null) { 
			this.flightList.add(flightTwo);
		}
		if(flightThree != null) {
			this.flightList.add(flightThree);
		}
	}
	
	public ReservationOption(Flight flightOne, Flight flightTwo) {
		this(flightOne, flightTwo, null);
	}
	
	public ReservationOption(Flight flightOne) {
		this(flightOne,null,null);
	}
	
	public ReservationOption(ArrayList<Flight> flightList) {
		this.flightList = flightList;
	}

	public Flight getFlight(int index) {
		Flight flight;
		try {
			flight = this.flightList.get(index);
		} catch (Exception ex) {
			flight = null;
		}
		return flight;
	}
	
	public int getNumFlights() {
		try {
			return this.flightList.size();
		} catch (Exception ex) {
			return 0;
		}
	}
	
	public int getNumLayovers() {
		try {
			return this.flightList.size()-1;
		} catch (Exception ex) {
			return 0;
		}
	}
	
	public double getPrice(String seatPreference) {
		double totalPrice = 0.0;
		boolean firstClass = false;
		if(seatPreference.equals("firstclass")) {
			firstClass = true;
		} else {
			firstClass = false;
		}
		for (Flight temp : this.flightList) {
			if(firstClass) {
				String price = temp.getmPriceFirstclass();
				price = price.substring(1, price.length());
				totalPrice += Double.parseDouble(price);
			} else {
				String price = temp.getmPriceCoach();
				price = price.substring(1, price.length());
				totalPrice += Double.parseDouble(price);
			}
		}
		return totalPrice;
	}
	
	public String getTotalTime() {
		DateTimeFormatter flightDateFormat = DateTimeFormatter.ofPattern("yyyy MMM d H:m z");
		long totalTime = 0;
		try {
			LocalDateTime departTimeLocal = LocalDateTime.parse(this.getFlight(0).getmTimeDepart(),flightDateFormat);
			ZonedDateTime departTimeZoned = departTimeLocal.atZone(ZoneId.of("GMT"));
			long departTime = departTimeZoned.toInstant().toEpochMilli();
			LocalDateTime arrivalTimeLocal = LocalDateTime.parse(this.getFlight(this.getNumFlights()-1).getmTimeArrival(), flightDateFormat);
			ZonedDateTime arrivalTimeZoned = arrivalTimeLocal.atZone(ZoneId.of("GMT"));
			long arrivalTime = arrivalTimeZoned.toInstant().toEpochMilli();
			totalTime = arrivalTime - departTime;
		} catch (DateTimeParseException ex) {
			ex.printStackTrace();
		}
		return String.format("%02d:%02d",
				TimeUnit.MILLISECONDS.toHours(totalTime),
				TimeUnit.MILLISECONDS.toMinutes(totalTime) % TimeUnit.HOURS.toMinutes(1)
				);
	}
}
