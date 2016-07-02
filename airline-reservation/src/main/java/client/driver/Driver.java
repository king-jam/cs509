/**
 * 
 */
package client.driver;

import client.reservation.*;
import java.text.ParseException;
import java.util.ArrayList;

import client.flight.Flight;
import client.search.*;

public class Driver {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FlightSearch search=new FlightSearch("BOS","LGA","2016 May 10 00:05 GMT","firstclass");
		try {
			ArrayList<ReservationOption> ar =search.getOptions();
			Flight flight;
			for(ReservationOption reservation:ar ){
				System.out.println("---------------------------");
				for(int i=0;i<3;i++){
					flight=reservation.getFlight(i);
					if(flight==null)
						break;
					else{
						System.out.println(flight.getmAirplane()+" "+flight.getmNumber()+" "+
								flight.getmCodeDepart()+"--->"+flight.getmCodeArrival());
						System.out.println("Departure:"+flight.getmTimeDepart()+" Arrival:"+flight.getmTimeArrival());
						System.out.println();	
					}
						
					
				}
				System.out.println("---------------------------");
			}
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		/*ServerInterface resSys = new ServerInterface();
		
		// Try to get a list of airports
		String xmlAirport = resSys.getAirports("01");
		System.out.println(xmlAirport);

		// Get a sample list of flights from server
		String xmlFlights = resSys.getFlights("Team01", "BOS", "2016_05_10");
		System.out.println(xmlFlights);
		
		// Create the aggregate flights
		Flights flights = new Flights();
		flights.addAll(xmlFlights);
		
		//try to reserve a coach seat on one of the flights
		Flight flight = flights.get(0);
		String flightNumber = flight.getmNumber();
		int seatsReservedStart = flight.getmSeatsCoach();
		
		String xmlReservation = "<Flights>"
				+ "<Flight number=\"" + flightNumber + "\" seating=\"Coach\"/>"
				+ "</Flights>";
		
		
		// Try to lock the database, purchase ticket and unlock database
		resSys.lock("WorldPlaneInc");
		resSys.buyTickets("WorldPlaneInc", xmlReservation);
		resSys.unlock("WorldPlaneInc");
		
		// Verify the operation worked
		xmlFlights = resSys.getFlights("WorldPlaneInc", "BOS", "2016_05_10");
		System.out.println(xmlFlights);
		flights.clear();
		flights.addAll(xmlFlights);
		
		// Find the flight number just updated
		int seatsReservedEnd = seatsReservedStart;
		for (Flight f : flights) {
			String tmpFlightNumber = f.getmNumber();
			if (tmpFlightNumber.equals(flightNumber)) {
				seatsReservedEnd = f.getmSeatsCoach();
				break;
			}
		}
		if (seatsReservedEnd == (seatsReservedStart + 1)) {
			System.out.println("Seat Reserved Successfully");
		} else {
			System.out.println("Reservation Failed");
		}*/
	}
}
