package client.search;

import java.util.ArrayList;

import client.flight.Flight;
import client.reservation.ReservationOption;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FlightSearchSorterTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public FlightSearchSorterTest( String testName )
	{
		super( testName );
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite()
	{
		return new TestSuite( FlightSearchSorterTest.class );
	}

	public void testSorter() {
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
		ArrayList<Flight> list1 = new ArrayList<Flight>();
		list1.add(f1);
		list1.add(f2);
		list1.add(f3);
		ReservationOption res1 = new ReservationOption(list1);
		Flight f4 = new Flight(
				"A320",
				"25",
				"2807",
				"BOS",
				"2016 May 10 00:05 GMT",
				"LGA",
				"2016 May 10 00:30 GMT",
				"$607.11",
				9,
				"$45.79",
				85
				);
		ArrayList<Flight> list2 = new ArrayList<Flight>();
		list2.add(f4);
		ReservationOption res2 = new ReservationOption(list2);
		Flight f5 = new Flight(
				"A320",
				"25",
				"2812",
				"BOS",
				"2016 May 10 00:05 GMT",
				"LGA",
				"2016 May 10 00:30 GMT",
				"$67.11",
				9,
				"$12.79",
				85
				);
		Flight f6 = new Flight(
				"737",
				"25",
				"2822",
				"LGA",
				"2016 May 10 03:05 GMT",
				"DEN",
				"2016 May 10 08:30 GMT",
				"$87.11",
				6,
				"$8.79",
				67
				);
		Flight f7 = new Flight(
				"A320",
				"25",
				"13279",
				"DEN",
				"2016 May 10 10:05 GMT",
				"SFO",
				"2016 May 10 12:30 GMT",
				"$87.11",
				8,
				"$2.79",
				69
				);
		ArrayList<Flight> list3 = new ArrayList<Flight>();
		list3.add(f5);
		list3.add(f6);
		list3.add(f7);
		ReservationOption res3 = new ReservationOption(list3);
		ArrayList<ReservationOption> options = new ArrayList<ReservationOption>();
		options.add(res1);
		options.add(res2);
		options.add(res3);
		FlightSearchSorter sorter = new FlightSearchSorter();
		sorter.sortPrice(true, options, "FirstClass");
		assertEquals("The lowest price flight was not first", res1, options.get(0));
		sorter.sortPrice(false, options, "FirstClass");
		assertEquals("The highest price flight was not first", res2, options.get(0));
		sorter.sortPrice(true, options, "Coach");
		assertEquals("The lowest price flight was not first", res3, options.get(0));
		sorter.sortPrice(false, options, "Coach");
		assertEquals("The highest price flight was not first", res1, options.get(0));
		sorter.sortTime(true, options);
		assertEquals("The shortest total time was not first", res2, options.get(0));
		sorter.sortTime(false, options);
		assertEquals("The longest total time was not first", res1, options.get(0));
	}
}
