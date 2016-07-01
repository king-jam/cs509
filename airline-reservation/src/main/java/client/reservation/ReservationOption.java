package client.reservation;

import client.flight.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	
	public int getNumLayovers() {
		try {
			return this.flightList.size()-1;
		} catch (Exception ex) {
			return 0;
		}
	}
	
	public double getPrice(boolean firstClass) {
		double totalPrice = 0.0;
		for (Flight temp : this.flightList) {
			if(firstClass) {
				String price = temp.getmPriceFirstclass();
				price = price.substring(1, price.length()-1);
				totalPrice += Double.parseDouble(price);
			} else {
				String price = temp.getmPriceCoach();
				price = price.substring(1, price.length()-1);
				totalPrice += Double.parseDouble(price);
			}
		}
		return totalPrice;
	}
	
	public String getTotalTime() {
		DateFormat flightDateFormat = new SimpleDateFormat("YYYY MMM DD HH:mm Z");
		long totalTime = 0;
		try {
			for(Flight temp : this.flightList) {
				Date departTime = new Date();
				Date arrivalTime = new Date();
				departTime = flightDateFormat.parse(temp.getmTimeDepart());
				arrivalTime = flightDateFormat.parse(temp.getmTimeArrival());
				totalTime += arrivalTime.getTime() - departTime.getTime();
			}
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return String.format("%02d:%02d:%02d",
				TimeUnit.MILLISECONDS.toHours(totalTime),
				TimeUnit.MILLISECONDS.toMinutes(totalTime) % TimeUnit.HOURS.toMinutes(1),
				TimeUnit.MILLISECONDS.toSeconds(totalTime) % TimeUnit.MINUTES.toSeconds(1)
				);
	}
}
