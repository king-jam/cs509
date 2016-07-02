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
import client.dao.ServerInterface;
import client.flight.Flight;
import client.reservation.ReservationOption;
import client.search.FlightSearch;
import client.util.*;
/**
 * @author James
 *
 */
public class ConsoleUI {	

	public static void main(String[] args) throws ParseException {

		String mDepartureAirportCode = "";
		String mArrivalAirportCode = "";
		boolean mOneWay = false;
		String mSeatPreference = "";
		String mDepartureDate = "2016 May 10 00:05 GMT";
		String mReturnDate = "2016 May 10 00:30 GMT";
		ArrayList<ReservationOption> selectedOptions = new ArrayList<ReservationOption>();
		ServerInterface mServerInterface = new ServerInterface();
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
		System.out.println(" Enter 'q' at any prompt to quit.");
		System.out.println();
		System.out.println("!!!!!!!!!!!STARTING!!!!!!!!!!!!!!");
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		while(!inputReady) {
			boolean depAirport = false;
			while(!depAirport) {
				System.out.println("\tDEPARTURE AIRPORTS");
				for(int i = 0; i < airports.size(); i++) {
					System.out.printf("%d:\t%s\t%s\n",i+1,airports.get(i).code(),airports.get(i).name());
				}
				System.out.print("Please select a DEPARTURE airport [enter #]: ");
				String input = scan.next();
				if(input.equals("q")) {
					System.out.println("Exiting");
					System.exit(0);
				} else {
					int selection = Integer.parseInt(input);
					if(selection < 0 || selection > airports.size()) {
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						System.out.println("!!!!!!  Invalid Selection  !!!!!!");
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						try {
							TimeUnit.SECONDS.sleep(2);
						} catch(InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						depAirport = true;
						mDepartureAirportCode = airports.get(selection-1).code();
					}
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
				if(input.equals("q")) {
					System.out.println("Exiting");
					System.exit(0);
				} else {
					int selection = Integer.parseInt(input);
					if(selection < 0 || selection > airports.size()) {
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						System.out.println("!!!!!!  Invalid Selection  !!!!!!");
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						try {
							TimeUnit.SECONDS.sleep(2);
						} catch(InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						arrAirport = true;
						mArrivalAirportCode = airports.get(selection-1).code();
					}
				}
			}
			boolean oneWaySelection = false;
			while(!oneWaySelection) {
				System.out.print("Is the flight one way [y/n]: ");
				String input = scan.next();
				if(input.equals("q")) {
					System.out.println("Exiting");
					System.exit(0);
				} else {
					if(input.equals("y") || input.equals("Y")) {
						mOneWay = true;
						oneWaySelection = true;
					} else if(input.equals("n") || input.equals("N")) {
						mOneWay = false;
						oneWaySelection = true;
					} else {
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						System.out.println("!!!!!!  Invalid Selection  !!!!!!");
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						try {
							TimeUnit.SECONDS.sleep(2);
						} catch(InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			boolean seatPref = false;
			while(!seatPref) {
				System.out.println("\tSEAT PREFERENCE");
				System.out.printf("\t1 - Coach\n");
				System.out.printf("\t2 - First Class\n");
				System.out.print("Please select seat preference [enter #]: ");
				String input = scan.next();
				if(input.equals("q")) {
					System.out.println("Exiting");
					System.exit(0);
				} else {
					int selection = Integer.parseInt(input);
					if(selection < 0 || selection > 2) {
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						System.out.println("!!!!!!  Invalid Selection  !!!!!!");
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						try {
							TimeUnit.SECONDS.sleep(2);
						} catch(InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						seatPref = true;
						if(selection == 1) {
							mSeatPreference = "coach";
						} else {
							mSeatPreference = "firstclass";
						}
					}
				}
			}
			boolean depDate = false;
			while(!depDate) {
				System.out.print("Please enter DEPARTURE date [format - 2016 May 10 15:25 (use 24hr time)]:");
				String in1 = scan.next();
				String in2 = scan.nextLine();
				String departDate = in1 + in2 + " GMT";
				if(departDate.equals("q")) {
					System.out.println("Exiting");
					System.exit(0);
				} else {
					DateTimeFormatter flightDateFormat= DateTimeFormatter.ofPattern("yyyy MMM d H:m z");
					try {
						@SuppressWarnings("unused")
						LocalDateTime departTimeLocal = LocalDateTime.parse(departDate,flightDateFormat);
					} catch (DateTimeParseException e) {
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						System.out.println("!!!!!!    Invalid Format   !!!!!!");
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						break;
					}
					depDate = true;
					mDepartureDate = departDate;
				}
			}
			if(!mOneWay) {
				boolean retDate = false;
				while(!retDate) {
					System.out.print("Please enter RETURN date [format - 2016 May 10 15:25 (use 24hr time)]:");
					String in1 = scan.next();
					String in2 = scan.nextLine();
					String returnDate = in1 + in2 + " GMT";
					if(returnDate.equals("q")) {
						System.out.println("Exiting");
						System.exit(0);
					} else {
						DateTimeFormatter flightDateFormat= DateTimeFormatter.ofPattern("yyyy MMM d H:m z");
						try {
							@SuppressWarnings("unused")
							LocalDateTime departTimeLocal = LocalDateTime.parse(returnDate,flightDateFormat);
						} catch (DateTimeParseException e) {
							System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
							System.out.println("!!!!!!    Invalid Format   !!!!!!");
							System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
							break;
						}
						retDate = true;
						mReturnDate = returnDate;
					}
				}
			}
			inputReady = true;
		}
		ArrayList<ReservationOption> options = new ArrayList<ReservationOption>();
		Flight flight;
		FlightSearch search=new FlightSearch(
				mDepartureAirportCode,
				mArrivalAirportCode,
				mDepartureDate,
				mSeatPreference);
		try {
			System.out.println("!!!!!!!!SEARCHING!!!!!!!!!!!");
			options = search.getOptions();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(options.isEmpty()) {
			System.out.println("No Flights to Destination, please change date");
			System.out.println("Exiting");
			System.exit(0);
		}
		boolean selectedToReservation = false;
		while(!selectedToReservation) {
			System.out.println("Flight Options TO Destination");
			for(int i = 0; i < options.size(); i++){
				ReservationOption option = options.get(i);
				System.out.printf("%d.\t---------------------------------"
						+ "------------------------------------\n",i+1);
				for(int j = 0; j < option.getNumFlights(); j++){
					flight=option.getFlight(j);
					System.out.print("\t"+flight.getmNumber()+"\t"+flight.getmCodeDepart()+
							"--->"+flight.getmCodeArrival());
					System.out.println("\t"+flight.getmTimeDepart()+" - "+flight.getmTimeArrival());
				}

				System.out.println("\tDeparture: "+option.getFlight(0).getmTimeDepart());
				System.out.println("\tArrival: "+option.getFlight(option.getNumFlights()-1).getmTimeArrival());
				System.out.println("\tTotal Travel Time: "+option.getTotalTime());
				System.out.println("\tTotal Price: $"+String.format( "%.2f", option.getPrice(mSeatPreference) ));
			}

			System.out.print("Please select a reservation option [enter #]: ");
			String input = scan.next();
			if(input.equals("q")) {
				System.out.println("Exiting");
				System.exit(0);
			} else {
				int selection = Integer.parseInt(input);
				if(selection < 0 || selection > options.size()) {
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					System.out.println("!!!!!!  Invalid Selection  !!!!!!");
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					try {
						TimeUnit.SECONDS.sleep(2);
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					selectedToReservation = true;
					selectedOptions.add(options.get(selection-1));
				}
			}
		}
		if(!mOneWay) {
			boolean selectedFromReservation = false;
			options.clear();
			FlightSearch search2=new FlightSearch(
					mArrivalAirportCode, // previous arrival airport is now departure
					mDepartureAirportCode, // previous departure airport is now arrival
					mReturnDate, 
					mSeatPreference);
			try {
				System.out.println("!!!!!!!!SEARCHING!!!!!!!!!!!");
				options = search2.getOptions();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while(!selectedFromReservation) {
				System.out.println("Flight Options TO Destination");
				for(int i = 0; i < options.size(); i++){
					ReservationOption option = options.get(i);
					System.out.printf("%d.\t---------------------------------"
							+ "------------------------------------\n",i+1);
					for(int j = 0; j < option.getNumFlights(); j++){
						flight=option.getFlight(j);
						System.out.print("\t"+flight.getmNumber()+"\t"+flight.getmCodeDepart()+
								"--->"+flight.getmCodeArrival());
						System.out.println("\t"+flight.getmTimeDepart()+" - "+flight.getmTimeArrival());
					}

					System.out.println("\tDeparture: "+option.getFlight(0).getmTimeDepart());
					System.out.println("\tArrival: "+option.getFlight(option.getNumFlights()-1).getmTimeArrival());
					System.out.println("\tTotal Travel Time: "+option.getTotalTime());
					System.out.println("\tTotal Price: $"+String.format( "%.2f", option.getPrice(mSeatPreference) ));
				}

				System.out.print("Please select a reservation option [enter #]: ");
				String input = scan.next();
				if(input.equals("q")) {
					System.out.println("Exiting");
					System.exit(0);
				} else {
					int selection = Integer.parseInt(input);
					if(selection < 0 || selection > options.size()) {
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						System.out.println("!!!!!!  Invalid Selection  !!!!!!");
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						try {
							TimeUnit.SECONDS.sleep(2);
						} catch(InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						selectedFromReservation = true;
						selectedOptions.add(options.get(selection-1));
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
				for(int j = 0; j < option.getNumFlights(); j++){
					flight=option.getFlight(j);
					System.out.print("\t"+flight.getmNumber()+"\t"+flight.getmCodeDepart()+
							"--->"+flight.getmCodeArrival());
					System.out.println("\t"+flight.getmTimeDepart()+" - "+flight.getmTimeArrival());
				}
				System.out.println("\tDeparture: "+option.getFlight(0).getmTimeDepart());
				System.out.println("\tArrival: "+option.getFlight(option.getNumFlights()-1).getmTimeArrival());
				System.out.println("\tTotal Travel Time: "+option.getTotalTime());
				System.out.println("\tTotal Price: $"+String.format( "%.2f", option.getPrice(mSeatPreference) ));

				// print returnTrip
				if(!mOneWay) {
					option = selectedOptions.get(1);
					System.out.println("------------RETURN TRIP-------------");
					for(int j = 0; j < option.getNumFlights(); j++){
						flight=option.getFlight(j);
						System.out.print("\t"+flight.getmNumber()+"\t"+flight.getmCodeDepart()+
								"--->"+flight.getmCodeArrival());
						System.out.println("\t"+flight.getmTimeDepart()+" - "+flight.getmTimeArrival());
					}
					System.out.println("\tDeparture: "+option.getFlight(0).getmTimeDepart());
					System.out.println("\tArrival: "+option.getFlight(option.getNumFlights()-1).getmTimeArrival());
					System.out.println("\tTotal Travel Time: "+option.getTotalTime());
					System.out.println("\tTotal Price: $"+String.format( "%.2f", option.getPrice(mSeatPreference)));
				}
				System.out.println("-----------------------------------------");
				System.out.println("-----------RESERVATION SUMMARY-----------");
				System.out.println("Seat Type: "+mSeatPreference);
				double price = 0;
				for(ReservationOption ops:selectedOptions) {
					price += ops.getPrice(mSeatPreference);
				}
				System.out.println("Total Price: "+String.format( "%.2f", price));
				
				System.out.print("Confirm Reservation? [y/n]: ");
				String input = scan.next();
				if(input.equals("q")) {
					System.out.println("Exiting");
					System.exit(0);
				} else {
					if(input.equals("y") || input.equals("Y")) {
						// reserve flight here
						confirmed = true;
					} else if(input.equals("n") || input.equals("N")) {
						// reserve flight here
						confirmed = true;
					} else {
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						System.out.println("!!!!!!  Invalid Selection  !!!!!!");
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						try {
							TimeUnit.SECONDS.sleep(2);
						} catch(InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			System.out.println("Thank you for testing!");
			scan.close();
			System.exit(0);
		}
	}
}