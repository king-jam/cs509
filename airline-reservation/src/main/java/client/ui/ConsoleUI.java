/**
 * 
 */
package client.ui;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import client.airport.Airport;
import client.airport.Airports;
import client.dao.ServerInterfaceCache;
import client.flight.Flight;
import client.reservation.*;
import client.search.FlightSearch;
import client.search.FlightSearchSorter;
import client.util.*;
/**
 * This class is a debug runner UI for testing and validation full functionality.
 * 
 * @author James
 * @version 1
 * @since 07/03/2016
 */
public class ConsoleUI {	
	/**
	 * Global Data attributes for a ConsoleUI
	 */
	private static final String quitKeyword = "q";
	/**
	 * prints a formatted ReservationOption for display
	 * 
	 * @param option contains the ReservationOption 
	 * @param seatPreference contains the client seat preference for price display
	 * 
	 */
	public static void printReservationOption(ReservationOption option, String seatPreference) {
		Flight flight;
		for(int j = 0; j < option.getNumFlights(); j++){
			flight=option.getFlight(j);
			System.out.print("\t"+flight.getmNumber()+"\t"+flight.getmCodeDepart()+
					"--->"+flight.getmCodeArrival());
			System.out.println("\t"+flight.getmTimeDepart()+" - "+flight.getmTimeArrival());
		}

		System.out.println("\tDeparture: "+option.getFlight(0).getmTimeDepart());
		System.out.println("\tArrival: "+option.getFlight(option.getNumFlights()-1).getmTimeArrival());
		System.out.println("\tTotal Travel Time: "+option.getTotalTime());
		System.out.println("\tTotal Price: $"+String.format( "%.2f", option.getPrice(seatPreference) ));
	}
	/**
	 * prints an invalid selection message and pauses for client to see it
	 */
	public static void printInvalidSelection() {
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println("!!!!!!  Invalid Selection  !!!!!!");
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * prints an invalid format message and pauses for client to see it
	 */
	public static void printInvalidFormat() {
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println("!!!!!!    Invalid Format   !!!!!!");
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}
	/**
	 * checks the input string to see if client wants to exit the program
	 * 
	 * @param input string contains the console input from the client
	 * 
	 */
	public static void exitCheck(String input) {
		if(input.equals(quitKeyword)) {
			System.out.println("Exiting");
			System.exit(0);
		}
		return;
	}
	/**
	 * parses the input string for integers and handles errors
	 * 
	 * @param input string contains the console input from the client
	 * 
	 */
	public static int parseSelectionInt(String input) {
		int selection = 0;
		try {
			selection = Integer.parseInt(input);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			printInvalidSelection();
			selection = -1;
		}
		return selection;
	}
	/**
	 * sorts the reservation to be displayed in order for a client
	 * 
	 * @param options contains the ReservationOption array return from Search
	 * @param op string contains a sort request type
	 * @param seatPref string contains the seating preference for sorting price by
	 * 
	 */
	public static void sortReservationOptions(ArrayList<ReservationOption> options, String op, String seatPref)
	{
		FlightSearchSorter sorter = new FlightSearchSorter();
		if(op.equals("pa")) {
			sorter.sortPrice(true,options,seatPref);
		} else if (op.equals("pd")) {
			sorter.sortPrice(false,options,seatPref);
		} else if (op.equals("ta")) {
			sorter.sortTime(true,options);
		} else if (op.equals("td")) {
			sorter.sortTime(false,options);
		} else {
			printInvalidSelection();
		}
	}
	/**
	 * main runner function to take user input, search for flights, and display options
	 * 
	 */
	public static void main(String[] args) throws ParseException {

		String mDepartureAirportCode = "";
		String mArrivalAirportCode = "";
		boolean mOneWay = false;
		String mSeatPreference = "";
		String mDepartureDate = "";
		String mReturnDate = "";
		ArrayList<ReservationOption> selectedOptions = new ArrayList<ReservationOption>();
		ServerInterfaceCache mServerInterface = new ServerInterfaceCache();
		Airports airports = new Airports();
		String airportData = mServerInterface.getAirports(Configuration.getAgency());
		airports.addAll(airportData);
		Collections.sort(airports, new Comparator<Airport>() {
			public int compare(Airport airport1, Airport airport2)
			{
				return  airport1.code().compareTo(airport2.code());
			}
		});

		boolean inputReady = false;
		Scanner scan = new Scanner(System.in);

		System.out.println("Team 01 - Console Debug Prototype");
		System.out.println();
		System.out.println(" Enter "+quitKeyword+" at any prompt to quit.");
		System.out.println();
		System.out.println("!!!!!!!!!!!STARTING!!!!!!!!!!!!!!");
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		boolean retry = false;
		while(!retry) {
			while(!inputReady) {
				boolean depAirport = false;
				while(!depAirport) {
					System.out.println("\tDEPARTURE AIRPORTS");
					for(int i = 0; i < airports.size(); i++) {
						System.out.printf("%d:\t%s\t%s\n",i+1,airports.get(i).code(),airports.get(i).name());
					}
					System.out.print("Please select a DEPARTURE airport [enter #]: ");
					String input = scan.next();
					exitCheck(input);
					int selection = parseSelectionInt(input);
					if(selection < 0 || selection > airports.size()) {
						printInvalidSelection();
					} else {
						depAirport = true;
						mDepartureAirportCode = airports.get(selection-1).code();
					}
				}
				boolean arrAirport = false;
				while(!arrAirport) {
					System.out.println("\tARRIVAL AIRPORTS");
					for(int i = 0; i < airports.size(); i++) {
						System.out.printf("%d:\t%s\t%s\n",i+1,airports.get(i).code(),airports.get(i).name());
					}
					System.out.print("Please select a ARRIVAL airport [enter #]: ");
					String input = scan.next();
					exitCheck(input);
					int selection = parseSelectionInt(input);
					if(selection < 0 || selection > airports.size()) {
						printInvalidSelection();
					} else {
						arrAirport = true;
						mArrivalAirportCode = airports.get(selection-1).code();
					}
				}
				boolean oneWaySelection = false;
				while(!oneWaySelection) {
					System.out.print("Is the flight one way [y/n]: ");
					String input = scan.next();
					exitCheck(input);
					if(input.equals("y") || input.equals("Y")) {
						mOneWay = true;
						oneWaySelection = true;
					} else if(input.equals("n") || input.equals("N")) {
						mOneWay = false;
						oneWaySelection = true;
					} else {
						printInvalidSelection();
					}
				}
				boolean seatPref = false;
				while(!seatPref) {
					System.out.println("\tSEAT PREFERENCE");
					System.out.printf("\t1 - Coach\n");
					System.out.printf("\t2 - First Class\n");
					System.out.print("Please select seat preference [enter #]: ");
					String input = scan.next();
					exitCheck(input);
					int selection = parseSelectionInt(input);
					if(selection < 1 || selection > 2) {
						printInvalidSelection();
					} else {
						seatPref = true;
						if(selection == 1) {
							mSeatPreference = "coach";
						} else {
							mSeatPreference = "firstclass";
						}
					}
				}
				boolean depDate = false;
				while(!depDate) {
					System.out.print("Please enter DEPARTURE date [format - 2016 May 10 15:25 (use 24hr time)]:");
					String in1 = scan.next();
					String in2 = scan.nextLine();
					String departDate = in1 + in2 + " GMT";
					exitCheck(departDate);
					DateTimeFormatter flightDateFormat= DateTimeFormatter.ofPattern("yyyy MMM d H:m z");
					try {
						@SuppressWarnings("unused")
						LocalDateTime departTimeLocal = LocalDateTime.parse(departDate,flightDateFormat);
					} catch (DateTimeParseException e) {
						printInvalidFormat();
						break;
					}
					depDate = true;
					mDepartureDate = departDate;
				}
				if(!mOneWay) {
					boolean retDate = false;
					while(!retDate) {
						System.out.print("Please enter RETURN date [format - 2016 May 10 15:25 (use 24hr time)]:");
						String in1 = scan.next();
						String in2 = scan.nextLine();
						String returnDate = in1 + in2 + " GMT";
						exitCheck(returnDate);
						DateTimeFormatter flightDateFormat= DateTimeFormatter.ofPattern("yyyy MMM d H:m z");
						try {
							@SuppressWarnings("unused")
							LocalDateTime departTimeLocal = LocalDateTime.parse(returnDate,flightDateFormat);
						} catch (DateTimeParseException e) {
							printInvalidFormat();
							break;
						}
						retDate = true;
						mReturnDate = returnDate;
					}
				}
				inputReady = true;
			}
			ArrayList<ReservationOption> toOptions = new ArrayList<ReservationOption>();
			FlightSearch search=new FlightSearch(
					mDepartureAirportCode,
					mArrivalAirportCode,
					mDepartureDate,
					mSeatPreference);
			try {
				System.out.println("!!!!!!!!SEARCHING!!!!!!!!!!!");
				long start = System.currentTimeMillis();
				toOptions = search.getOptions();
				long end = System.currentTimeMillis();
				System.out.printf("Total Time: %d", end-start);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(toOptions.isEmpty()) {
				System.out.println("No Flights to Destination, please change date");
				exitCheck("q");
			}
			boolean selectedToReservation = false;
			while(!selectedToReservation) {
				System.out.println("Flight Options TO Destination");
				for(int i = 0; i < toOptions.size(); i++){
					ReservationOption option = toOptions.get(i);
					System.out.printf("%d.\t---------------------------------"
							+ "------------------------------------\n",i+1);
					printReservationOption(option, mSeatPreference);
				}
	
				System.out.print("Please select a reservation option or display mechanism [enter #/pa/pd/ta/td]: ");
				String input = scan.next();
				exitCheck(input);
				if(input.equals("pa") ||
						input.equals("pd") ||
						input.equals("ta") ||
						input.equals("td"))
				{
					sortReservationOptions(toOptions, input, mSeatPreference);
					continue;
				}
				int selection = parseSelectionInt(input);
				if(selection < 0 || selection > toOptions.size()) {
					printInvalidSelection();
				} else {
					selectedToReservation = true;
					selectedOptions.add(toOptions.get(selection-1));
				}
			}
			if(!mOneWay) {
				boolean selectedFromReservation = false;
				ArrayList<ReservationOption> retOptions = new ArrayList<ReservationOption>();
				FlightSearch search2=new FlightSearch(
						mArrivalAirportCode, // previous arrival airport is now departure
						mDepartureAirportCode, // previous departure airport is now arrival
						mReturnDate, 
						mSeatPreference);
				try {
					System.out.println("!!!!!!!!SEARCHING!!!!!!!!!!!");
					long start = System.currentTimeMillis();
					retOptions = search2.getOptions();
					long end = System.currentTimeMillis();
					System.out.printf("Total Time: %d", end-start);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(retOptions.isEmpty()) {
					System.out.println("No Flights to Destination, please change date");
					exitCheck("q");
				}
				while(!selectedFromReservation) {
					System.out.println("Flight Options TO Destination");
					for(int i = 0; i < retOptions.size(); i++){
						ReservationOption option = retOptions.get(i);
						System.out.printf("%d.\t---------------------------------"
								+ "------------------------------------\n",i+1);
						printReservationOption(option, mSeatPreference);
					}
	
					System.out.print("Please select a reservation option [enter #]: ");
					String input = scan.next();
					exitCheck(input);
					if(input.equals("pa") ||
							input.equals("pd") ||
							input.equals("ta") ||
							input.equals("td"))
					{
						sortReservationOptions(toOptions, input, mSeatPreference);
						continue;
					}
					int selection = parseSelectionInt(input);
					if(selection < 0 || selection > retOptions.size()) {
						printInvalidSelection();
					} else {
						selectedFromReservation = true;
						selectedOptions.add(retOptions.get(selection-1));
					}
				}
			}
			boolean confirmed = false;
			while(!confirmed) {
				if(selectedOptions.isEmpty() || (selectedOptions.size() > 2)) {
					break;
				}
				// print toTrip
				ReservationOption option = selectedOptions.get(0);
				System.out.println("------------TO TRIP-------------");
				printReservationOption(option, mSeatPreference);
	
				// print returnTrip
				if(!mOneWay) {
					option = selectedOptions.get(1);
					System.out.println("------------RETURN TRIP-------------");
					printReservationOption(option, mSeatPreference);	
				}
				System.out.println("-----------------------------------------");
				System.out.println("-----------RESERVATION SUMMARY-----------");
				System.out.println("Seat Type: "+mSeatPreference);
				double price = 0;
				for(ReservationOption ops:selectedOptions) {
					price += ops.getPrice(mSeatPreference);
				}
				System.out.println("Total Price: $"+String.format( "%.2f", price));
	
				System.out.print("Confirm Reservation? [y/n]: ");
				String input = scan.next();
				if(input.equals("q")) {
					System.out.println("Exiting");
					System.exit(0);
				} else {
					if(input.equals("y") || input.equals("Y")) {
						// reserve flights here
						retry = true;
						confirmed = true;
					} else if(input.equals("n") || input.equals("N")) {
						System.out.println("Graceful case not supported yet - restart app");
						confirmed = true;
					} else {
						printInvalidSelection();
					}
				}
			}
		}
		System.out.println("Thank you for testing!");
		scan.close();
		System.exit(0);
	}
}