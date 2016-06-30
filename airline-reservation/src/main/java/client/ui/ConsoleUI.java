/**
 * 
 */
package client.ui;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import client.airport.Airport;
import client.airport.Airports;
import client.dao.ServerInterface;
import client.search.FlightSearch;
import client.search.ReservationOptionDummy;
import client.util.*;
/**
 * @author James
 *
 */
public class ConsoleUI {	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws ParseException {

		String mDepartureAirportCode = "";
		String mArrivalAirportCode = "";
		boolean mOneWay = false;
		String mSeatPrefrence = "";
		String mDepartureDate = "2016 May 10 00:05 GMT";
		String mReturnDate = "2016 May 10 00:30 GMT";
		ServerInterface mServerInterface = new ServerInterface();
		Configuration mConfig = Configuration.getInstance();;
		Airports airports = new Airports();
		String airportData = mServerInterface.getAirports(mConfig.TICKET_AGENCY);
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
		System.out.println("..........................................");
		System.out.println();
		System.out.println(" Enter 'q' at any prompt to quit.");
		System.out.println();
		while(!inputReady) {
			boolean depAirport = false;
			while(!depAirport) {
				System.out.println("Please select a DEPARTURE airport [enter #]:");
				for(int i = 0; i < airports.size(); i++) {
					System.out.printf("%d:\t%s\t%s\n",i+1,airports.get(i).code(),airports.get(i).name());
				}
				System.out.print("Please select a DEPARTURE airport [enter #]: ");
				String input = scan.next();
				if(input.equals("q")) {
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
				System.out.println("Please select an ARRIVAL airport [enter #]:");
				for(int i = 0; i < airports.size(); i++) {
					System.out.printf("%d:\t%s\t%s\n",i+1,airports.get(i).code(),airports.get(i).name());
				}
				System.out.print("Please select a ARRIVAL airport [enter #]: ");
				String input = scan.next();
				if(input.equals("q")) {
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
				System.out.println("Is the flight one way [y/n]: ");
				String input = scan.next();
				if(input.equals("q")) {
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
				System.out.println("Please select seat preference [enter #]:");
				System.out.printf("\t1 - Coach\n");
				System.out.printf("\t2 - First Class\n");
				System.out.print("Please select seat preference [enter #]: ");
				String input = scan.next();
				if(input.equals("q")) {
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
							mSeatPrefrence = "coach";
						} else {
							mSeatPrefrence = "firstclass";
						}
					}
				}
			}
			boolean depDate = false;
			while(!depDate) {
//				System.out.print("Please enter DEPARTURE date [format - 2016 May 10 00:05]:");
//				String input = scan.nextLine();
//				//String input = "2016 May 10 00:05 GMT";
//				if(input.equals("q")) {
//					System.exit(0);
//				} else {
					//SimpleDateFormat formatter=new  SimpleDateFormat("yyyy MMM dd HH:mm z");
					//SimpleDateFormat departureDateFormatter=new  SimpleDateFormat("yyyy_MM_dd");
					//departureDateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
					//try {
					//	departureDateFormatter.format(formatter.parse(input));
					//} catch (ParseException e) {
					//	System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					//	System.out.println("!!!!!!    Invalid Format   !!!!!!");
					//	System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					//	break;
					//}
					depDate = true;
					//mDepartureDate = input;
				//}
			}
			if(!mOneWay) {
				boolean retDate = false;
				while(!retDate) {
//					System.out.print("Please enter RETURN date [format - 2016 May 10 00:05]:");
//					String input = scan.nextLine();
//					//String input = "2016 May 10 00:30 GMT";
//					if(input.equals("q")) {
//						System.exit(0);
//					} else {
						//SimpleDateFormat formatter=new  SimpleDateFormat("yyyy MMM dd HH:mm z");
						//try {
						//	formatter.parse(input);
						//} catch (ParseException e) {
						//	System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						//	System.out.println("!!!!!!    Invalid Format   !!!!!!");
						//	System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						//	break;
						//}
						retDate = true;
						//mReturnDate = input;
					//}
				}
			}
			inputReady = true;
		}
		boolean selectedReservation = false;
		while(!selectedReservation) {
			FlightSearch search=new FlightSearch(
					mDepartureAirportCode,
					mArrivalAirportCode,
					mOneWay,
					mSeatPrefrence,
					mDepartureDate,
					mReturnDate);
			try {
				ArrayList<ReservationOptionDummy> ar = search.getOptions();
				System.out.println(ar.size());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			selectedReservation = true;
		}
		System.out.println("Finished!");
		scan.close();
		System.exit(0);
	}
}
