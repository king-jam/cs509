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
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import client.airport.Airport;
import client.airport.Airports;
import client.airport.CodeComparator;
import client.dao.*;
import client.flight.Flight;
import client.reservation.*;
import client.search.FlightSearch;
import client.search.FlightSearchSorter;
import client.util.*;
/**
 * This class is a debug runner UI for testing and validating full functionality.
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
	public static Airports airports = new Airports();
	/**
	 * prints a formatted ReservationOption for display
	 * 
	 * @param option contains the ReservationOption 
	 * @param seatPreference contains the client seat preference for price display
	 * @throws Exception if timezone api fails to get a timezone
	 * 
	 */
	public static void printReservationOption(ReservationOption option, String seatPreference) throws Exception {
		Flight flight;
		String startTime = "";
		String endTime = "";
		TimeLocal timeLocal = new TimeLocal(Configuration.getInstance().getGoogleTimezoneAPIKey());
		for(int j = 0; j < option.getNumFlights(); j++){
			flight=option.getFlight(j);
			System.out.print("\t"+flight.getmNumber()+"\t"+flight.getmCodeDepart()+
					"--->"+flight.getmCodeArrival());
			Airport depAirport = getAirportFromCode(flight.getmCodeDepart());
			Airport arrAirport = getAirportFromCode(flight.getmCodeArrival());
			String departTime = timeLocal.getLocalTime(flight.getmTimeDepart(), depAirport.timezone());
			String arrivalTime = timeLocal.getLocalTime(flight.getmTimeArrival(), arrAirport.timezone());
			System.out.println("\t"+departTime+" - "+arrivalTime);
			if(j == 0) {
				startTime = departTime;
			}
			if(j == option.getNumFlights()-1) {
				endTime = arrivalTime;
			}
		}

		System.out.println("\tDeparture: "+startTime);
		System.out.println("\tArrival: "+endTime);
		System.out.println("\tTotal Travel Time: "+option.getTotalTime());
		System.out.println("\tTotal Price: $"+String.format( "%.2f", option.getPrice(seatPreference) ));
	}
	/**
	 * Returns an airport object from the airport code
	 * 
	 * @param code contains the three letter airport code 
	 * @return Airport object for reference
	 */
	public static Airport getAirportFromCode(String code) {
		int index = Collections.binarySearch(airports,
				new Airport("", code, 0.0, 0.0),
				new CodeComparator());
		if(index < 0) {
			return null;
		}
		return airports.get(index);
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
	 * @return integer if the string can be properly parsed to that
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
	 * @param args are arguments passed in by OS
	 * @throws Exception as a general collector of errors through run
	 * 
	 */
	public static void main(String[] args) throws Exception {
		Logger.getLogger("").setLevel(Level.OFF); 
		String mDepartureAirportCode = "";
		String mArrivalAirportCode = "";
		boolean mOneWay = false;
		String mSeatPreference = "";
		String mDepartureDate = "";
		String mReturnDate = "";
		ArrayList<ReservationOption> selectedOptions = new ArrayList<ReservationOption>();
		ServerInterfaceCache mServerInterface = ServerInterfaceCache.getInstance();
		ExecutorService executor = Executors.newWorkStealingPool();
		Callable<Airports> apTask = () -> {
			Airports aps = new Airports();
			String airportData = mServerInterface.getAirports(Configuration.getAgency());
			aps.addAll(airportData);
			for(Airport ap:aps) {
				ap.timezoneGoogle();
			}
			Collections.sort(aps, new CodeComparator());
			return aps;
		};
		Future<Airports> airportFuture = executor.submit(apTask);

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
			boolean inputReady = false;
			while(!inputReady) {
				boolean depAirport = false;
				while(!depAirport) {
					try {
						airports = airportFuture.get();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
					System.out.println("\tDEPARTURE AIRPORTS");
					for(int i = 0; i < airports.size(); i++) {
						System.out.printf("%d:\t%s\t%s\n",i+1,airports.get(i).code(),airports.get(i).name());
					}
					System.out.print("Please select a DEPARTURE airport [enter #]: ");
					String input = scan.next();
					exitCheck(input);
					int selection = parseSelectionInt(input);
					if(selection <= 0 || selection > airports.size()) {
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
					if(selection <= 0 || selection > airports.size()) {
						printInvalidSelection();
					} else {
						arrAirport = true;
						mArrivalAirportCode = airports.get(selection-1).code();
					}
				}
				boolean depDate = false;
				while(!depDate) {
					boolean flag = false;
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
						flag = true;
					}
					if(!flag) {
						depDate = true;
						mDepartureDate = departDate;
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
							mSeatPreference = "Coach";
						} else {
							mSeatPreference = "FirstClass";
						}
					}
				}
				inputReady = true;
			}
			// kick off one way search here
			FlightSearch search=new FlightSearch(
					mDepartureAirportCode,
					mArrivalAirportCode,
					mDepartureDate,
					mSeatPreference);
			Callable<ArrayList<ReservationOption>> toTask = () -> {
				ArrayList<ReservationOption> results = new ArrayList<ReservationOption>();
				try {
					results = search.getOptions();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return results;
			};
			Future<ArrayList<ReservationOption>> searchFuture = executor.submit(toTask);
			inputReady = false;
			while(!inputReady) {
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
				if(!mOneWay) {
					boolean retDate = false;
					while(!retDate) {
						System.out.print("Please enter RETURN date [format - 2016 May 10 15:25 (use 24hr time)]:");
						String in1 = scan.next();
						String in2 = scan.nextLine();
						String returnDate = in1 + in2 + " GMT";
						exitCheck(returnDate);
						DateTimeFormatter flightDateFormat= DateTimeFormatter.ofPattern("yyyy MMM d HH:mm z");
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
			try {
				System.out.println("!!!!!!!!SEARCHING!!!!!!!!!!!");
				long start = System.currentTimeMillis();
				toOptions = searchFuture.get();
				long end = System.currentTimeMillis();
				System.out.printf("Total Waiting Time: %d\n", end-start);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			if(toOptions.isEmpty()) {
				System.out.println("No Flights to Destination, please change date");
				exitCheck("q");
			}
			// kick off the next search while they select to flights
			if(!mOneWay) {
				FlightSearch search2=new FlightSearch(
						mArrivalAirportCode,
						mDepartureAirportCode,
						mReturnDate,
						mSeatPreference);
				Callable<ArrayList<ReservationOption>> retTask = () -> {
					ArrayList<ReservationOption> results = new ArrayList<ReservationOption>();
					try {
						results = search2.getOptions();
					} catch (ParseException e) {
						e.printStackTrace();
					} 
					return results;
				};
				searchFuture = executor.submit(retTask);
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
				if(selection <= 0 || selection > toOptions.size()) {
					printInvalidSelection();
				} else {
					selectedToReservation = true;
					selectedOptions.add(toOptions.get(selection-1));
				}
			}
			if(!mOneWay) {
				ArrayList<ReservationOption> retOptions = new ArrayList<ReservationOption>();
				try {
					System.out.println("!!!!!!!!SEARCHING!!!!!!!!!!!");
					long start = System.currentTimeMillis();
					retOptions = searchFuture.get();
					long end = System.currentTimeMillis();
					System.out.printf("Total Waiting Time: %d\n", end-start);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				if(retOptions.isEmpty()) {
					System.out.println("No Return Flights, please change date");
					exitCheck("q");
				}
				boolean selectedFromReservation = false;
				while(!selectedFromReservation) {
					System.out.println("Flight Options FROM Destination");
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
					if(selection <= 0 || selection > retOptions.size()) {
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
						int attempts = 0;
						for(ReservationOption op:selectedOptions) {
							attempts = 0;
							// chose random number of 3 for retries
							while(!op.reserveFlights(mSeatPreference) && attempts < 3) {
								System.out.println("!!!Failed to reserve, trying again!!");
								attempts++;
							}
							if(attempts > 2) { // magic number to account for retry attempts.
								System.out.println("!!!Failed to reserve tickets, exiting!!");
								break;
							}
						}
						if(attempts > 2) {
							retry = false;
						} else {
							retry = true;
						}
						confirmed = true;
					} else if(input.equals("n") || input.equals("N")) {
						System.out.println("!Restarting selection process!");
						retry = false;
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