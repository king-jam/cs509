package client.reservation;

import java.text.ParseException;
import java.util.ArrayList;

import client.dao.ServerInterfaceCache;
import client.flight.Flight;
import client.search.FlightSearch;
import client.util.Configuration;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ReservationOptionTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public ReservationOptionTest( String testName )
	{
		super( testName );
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite()
	{
		return new TestSuite( ReservationOptionTest.class );
	}

	/**
	 * test the positive scenarios of the class
	 */
	public void testReservationOptionClean()
	{
		Flight f1 = new Flight(
				"A320",
				"25",
				"2807",
				"BOS",
				"2016 May 10 00:05 GMT",
				"LGA",
				"2016 May 10 00:30 GMT",
				"$67.11",
				9,
				"$18.79",
				85
				);
		Flight f2 = new Flight(
				"737",
				"25",
				"2822",
				"LGA",
				"2016 May 10 03:05 GMT",
				"DEN",
				"2016 May 10 08:30 GMT",
				"$87.11",
				6,
				"$28.79",
				67
				);
		Flight f3 = new Flight(
				"A320",
				"25",
				"13279",
				"DEN",
				"2016 May 10 10:05 GMT",
				"SFO",
				"2016 May 10 13:30 GMT",
				"$57.11",
				8,
				"$22.79",
				69
				);
		ArrayList<Flight> list = new ArrayList<Flight>();
		list.add(f1);
		list.add(f2);
		list.add(f3);
		ReservationOption res = new ReservationOption(list);

		assertEquals("The returned flight (0) does not match", f1, res.getFlight(0));
		assertEquals("The returned flight (1) does not match", f2, res.getFlight(1));
		assertEquals("The returned flight (2) does not match", f3, res.getFlight(2));
		assertEquals("The returned # of flights does not match", 3, res.getNumFlights());
		assertEquals("The returned # of layovers does not match", 2, res.getNumLayovers());
		assertEquals("The returned price does not match Coach", 70.37, res.getPrice("Coach"));
		assertEquals("The returned price does not match First Class", 211.33, res.getPrice("FirstClass"));
		assertEquals("The returned total time does not match", "13:25", res.getTotalTime());
	}


	/**
	 * test the empty scenarios of the class
	 */
	public void testReservationOptionEmpty()
	{
		ReservationOption res = new ReservationOption();

		assertEquals("The returned flight is not null", null, res.getFlight(0));
		assertEquals("The returned flight is not null", null, res.getFlight(1));
		assertEquals("The returned flight is not null", null, res.getFlight(2));
		assertEquals("The returned # of flights is not 0", 0, res.getNumFlights());
		assertEquals("The returned # of layovers is not 0", 0, res.getNumLayovers());
		assertEquals("The returned price is not 0.00", 0.00, res.getPrice("Coach"));
		assertEquals("The returned price is not 0.00", 0.00, res.getPrice("FirstClass"));
		assertEquals("The returned total time is not 00:00", "00:00", res.getTotalTime());
	}

	/**
	 * test the empty scenarios of the class
	 */
	public void testReservationOptionNull()
	{
		ReservationOption res = new ReservationOption(null);

		assertEquals("The returned flight is not null", null, res.getFlight(0));
		assertEquals("The returned flight is not null", null, res.getFlight(1));
		assertEquals("The returned flight is not null", null, res.getFlight(2));
		assertEquals("The returned # of flights is not 0", 0, res.getNumFlights());
		assertEquals("The returned # of layovers is not 0", 0, res.getNumLayovers());
		assertEquals("The returned price is not 0.00", 0.00, res.getPrice("Coach"));
		assertEquals("The returned price is not 0.00", 0.00, res.getPrice("FirstClass"));
		assertEquals("The returned total time is not 00:00", "00:00", res.getTotalTime());
	}

	/**
	 * test the malformed string scenarios of the class
	 */
	public void testReservationOptionFormat() {
		Flight f1 = new Flight(
				"A320",
				"25",
				"2807",
				"BOS",
				"May 10 2016 00:05 GMT",
				"LGA",
				"May 10 2016 00:30 GMT",
				"$67.11",
				9,
				"$18.79",
				85
				);
		ArrayList<Flight> list = new ArrayList<Flight>();
		list.add(f1);
		ReservationOption res = new ReservationOption(list);
		assertEquals("The returned flight is not a match", f1, res.getFlight(0));
		assertEquals("The returned flight is not null", null, res.getFlight(1));
		assertEquals("The returned flight is not null", null, res.getFlight(2));
		assertEquals("The returned # of flights is not 1", 1, res.getNumFlights());
		assertEquals("The returned # of layovers is not 0", 0, res.getNumLayovers());
		assertEquals("The returned price is not a match", 18.79, res.getPrice("Coach"));
		assertEquals("The returned price is not a match", 67.11, res.getPrice("FirstClass"));
		assertEquals("The returned total time is not INVALID", "INVALID", res.getTotalTime());
	}
	
	/**
	 * Testing reserveFlight() of class
	 * 
	 * @throws ParseException is thrown if the flightsearch options returns a parse failure
	 */
	public void testReserveFlight() throws ParseException{
		//testing whether the getoptions() returns the same no .of results for a set of parameters
		//testing whether the first three results match
		ServerInterfaceCache serverInterfaceCache=ServerInterfaceCache.getInstance();
		//reseting the database to its original state
		boolean resetCheck=serverInterfaceCache.resetDB(Configuration.TICKET_AGENCY);
		assertEquals("Not able to ResetDB to original state",true,resetCheck);

		FlightSearch search1=new FlightSearch("BOS","LGA","2016 May 10 03:05 GMT", "FirstClass");
		FlightSearch search2=new FlightSearch("BOS","LGA","2016 May 10 03:05 GMT", "Coach");
		//Verifying the seat count increased after using the reservation functionality

		assertEquals("Reservation functionality not working for booking first class seats", true,search1.getOptions().get(0).reserveFlights("FirstClass"));
		assertEquals("Reservation functionality not working for booking Coach seats", true,search2.getOptions().get(0).reserveFlights("Coach"));
	}

}