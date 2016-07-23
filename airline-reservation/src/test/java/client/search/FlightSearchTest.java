package client.search;

import java.text.ParseException;
import java.util.ArrayList;

import client.search.FlightSearch;
import client.util.Configuration;
import client.dao.ServerInterfaceCache;
import client.flight.Flight;
import client.reservation.ReservationOption;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FlightSearchTest extends TestCase {
	/**
	 * 
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite( FlightSearchTest.class );
		// TODO Auto-generated constructor stub

	}
	/**
	 * Create the test case
	 * 
	 * @param testName is the passed in test name to the TestCase
	 */
	public FlightSearchTest(String testName){
		super( testName );
	}

	/**
	 * Testing dateFormatter() of the class
	 * 
	 * @throws ParseException is thrown if the test date formatter isn't able to parse
	 */
	public void testDateFormatter() throws ParseException {
		FlightSearch search=new FlightSearch(null, null, null, null);
		String date ="2016 May 10 00:05 GMT";
		String date_formatter="2016_05_10";

		assertEquals("The returned string  is not in  YYYY__MM_DD format",search.dateFormatter(date),date_formatter);

	}

	/**
	 * Testing the method checkNextDayFlight() of the class
	 * 
	 * @throws ParseException is thrown if the test formatter isn't able to parse
	 */
	public void testCheckNextDayFlight() throws ParseException{
		FlightSearch search=new FlightSearch(null, null, null, null);
		String date1 ="2016 May 10 03:05 GMT";
		String date2 ="2016 May 10 22:05 GMT";
		String date3="2016 May 10 21:00 GMT";

		assertEquals("False value expected as time entered is before 9 pm",false,search.checkNextDayFlight(date1));
		assertEquals("True value expected as time entered is after 9 pm",true,search.checkNextDayFlight(date2));
		assertEquals("False value expected as time entered is 9 pm",false,search.checkNextDayFlight(date3));
	}

	/**
	 * testing addOneDay() method of the class
	 * 
	 * @throws ParseException is thrown if the test formatter fails to parse the date
	 */
	public void testAddOneDay() throws ParseException {
		FlightSearch search=new FlightSearch(null, null, null, null);
		String date ="2016 May 10 00:05 GMT";
		String nextdate="2016 May 11 00:05 GMT";
		assertEquals("Next date not adding up,expected date +1",nextdate,search.addOneday(date));
	}

	/**
	 * test isValidLayover() method of class
	 * 
	 * @throws ParseException is thrown if the test formatter fails to parse the date
	 */
	public void testIsValidLayover() throws ParseException{
		FlightSearch search=new FlightSearch(null, null, null, null);
		//Scenario where layover time >3 hours
		String arrival_time1 ="2016 May 10 00:05 GMT";
		String departure_time1="2016 May 10 04:05 GMT";
		//Scenario where layover time<3 hours
		String arrival_time2="2016 May 10 01:05 GMT";
		String departure_time2="2016 May 10 02:30 GMT";
		//Scenario where layover time=3 hours
		String arrival_time3 ="2016 May 10 00:00 GMT";
		String departure_time3="2016 May 10 03:00 GMT";
		//Scenario where layover time =30 mins
		String arrival_time4 ="2016 May 10 00:00 GMT";
		String departure_time4="2016 May 10 00:30 GMT";

		assertEquals("false value expected as layover time >3 hours", false,search.isValidLayover(arrival_time1, departure_time1));
		assertEquals("true value expected as layover time <3 hours", true,search.isValidLayover(arrival_time2, departure_time2));
		assertEquals("true value expected as layover time =3 hours", true,search.isValidLayover(arrival_time3, departure_time3));
		assertEquals("true value expected as layover time =30 mins", true,search.isValidLayover(arrival_time4, departure_time4));
	}
	/**
	 * testing SeatsAvailable() of class
	 */
	public void testSeatsAvailable(){
		//Flight having  FristClass seats >0 and Coach seats>0
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
		//FLight having FirstClass seats =0 and Coach seats=0
		Flight f2 = new Flight(
				"737",
				"25",
				"2822",
				"LGA",
				"2016 May 10 03:05 GMT",
				"DEN",
				"2016 May 10 08:30 GMT",
				"$87.11",
				29, // max first class seats on 737 is 28
				"$28.79",
				100 // max coach seats on 737 is 100 - shows flight is full
				);

		FlightSearch search1=new FlightSearch(null, null, null, "FirstClass");
		FlightSearch search2=new FlightSearch(null, null, null, "Coach");

		assertEquals("Expected true as # seats First Class<available",true, search1.seatsAvailable(f1));
		assertEquals("Expected true as # seats Coach<available",true, search2.seatsAvailable(f1));
		assertEquals("Expected false as # seats First Class>available",false, search1.seatsAvailable(f2));
		assertEquals("Expected false as # seats Coach=available",false, search2.seatsAvailable(f2));

	}

	public void testGetOptions() throws ParseException{
		ServerInterfaceCache serverInterfaceCache=ServerInterfaceCache.getInstance();
		boolean resetCheck=serverInterfaceCache.resetDB(Configuration.TICKET_AGENCY);
		assertEquals("Not able to ResetDB to original state",true,resetCheck);
		FlightSearch search=new FlightSearch("BOS","LGA","2016 May 11 17:00 GMT", "FirstClass");
		ArrayList<ReservationOption> testOptions=new ArrayList<ReservationOption>();
		ArrayList<ReservationOption> expectedOptions=search.getOptions();
		ArrayList<Flight> list=new ArrayList<Flight>();
		Flight f1=new Flight(
				"A340",
				"103",
				"2854",
				"BOS",
				"2016 May 11 04:05 GMT",
				"RDU", 
				"2016 May 11 05:48 GMT",
				"$242.79",
				30, 
				"$28.99",
				67);
		Flight f2=new Flight(
				"737",
				"58",
				"25055",
				"RDU",
				"2016 May 11 07:58 GMT",
				"LGA", 
				"2016 May 11 08:56 GMT",
				"$157.55",
				24, 
				"$44.11",
				68);
		list.add(f1);
		list.add(f2);
		ReservationOption option=new ReservationOption(list);
		testOptions.add(option);
		list =new ArrayList<Flight>();
		Flight f3=new Flight(
				"737",
				"32",
				"2855",
				"BOS",
				"2016 May 11 04:32 GMT",
				"EWR", 
				"2016 May 11 05:04 GMT",
				"$86.57",
				14, 
				"$24.24",
				60);
		Flight f4=new Flight(
				"A340",
				"2",
				"19952",
				"EWR",
				"2016 May 11 07:51 GMT",
				"LGA", 
				"2016 May 11 07:53 GMT",
				"$5.56",
				17, 
				"$0.66",
				241);
		list.add(f3);
		list.add(f4);

		option=new ReservationOption(list);
		testOptions.add(option);
		list =new ArrayList<Flight>();

		Flight f5=new Flight(
				"A310",
				"158",
				"2859",
				"BOS",
				"2016 May 11 07:43 GMT",
				"TPA", 
				"2016 May 11 10:21 GMT",
				"$499.78",
				23, 
				"$59.97",
				37);
		Flight f6=new Flight(
				"767",
				"151",
				"31381",
				"TPA",
				"2016 May 11 13:03 GMT",
				"LGA", 
				"2016 May 11 15:34 GMT",
				"$109.90",
				6, 
				"$57.15",
				56); 
		list.add(f5);
		list.add(f6);

		option=new ReservationOption(list);
		testOptions.add(option);
		//expecting 8 search results for a given set of parameters
		assertEquals("Expected no.of search results different",9,expectedOptions.size());

		for(int i=0;i<testOptions.size();i++){
			for(int j=0;j<testOptions.get(i).getNumFlights();j++){
				assertEquals((i+1)+" th result not matching",testOptions.get(i).getFlight(j).getmAirplane(),
						expectedOptions.get(i).getFlight(j).getmAirplane());
				assertEquals((i+1)+" th result not matching",testOptions.get(i).getFlight(j).getmNumber(),
						expectedOptions.get(i).getFlight(j).getmNumber());
				assertEquals((i+1)+" th result not matching",testOptions.get(i).getFlight(j).getmCodeArrival(),
						expectedOptions.get(i).getFlight(j).getmCodeArrival());
				assertEquals((i+1)+" th result not matching",testOptions.get(i).getFlight(j).getmCodeDepart(),
						expectedOptions.get(i).getFlight(j).getmCodeDepart());
				assertEquals((i+1)+" th result not matching",testOptions.get(i).getFlight(j).getmTimeDepart(),
						expectedOptions.get(i).getFlight(j).getmTimeDepart());

			}
		}
	}
}

