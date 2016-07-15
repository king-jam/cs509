package client.search;

import java.text.ParseException;
import java.util.ArrayList;

import client.search.FlightSearch;
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
  * @param testName
  */
 public FlightSearchTest(String testName){
	 super( testName );
 }
 
 /**
  * Testing dateFormatter() of the class
  */
 public void testDateFormatter(){
	 FlightSearch search=new FlightSearch(null, null, null, null);
	 String date ="2016 May 10 00:05 GMT";
	 String date_formatter="2016_05_10";
	 
	 try {
		assertEquals("The returned string  is not in  YYYY__MM_DD format",search.dateFormatter(date),date_formatter);
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
 }
 
 /**
  * Testing the method checkNextDayFlight() of the class
  */
 public void testCheckNextDayFlight(){
	 FlightSearch search=new FlightSearch(null, null, null, null);
	 String date1 ="2016 May 10 03:05 GMT";
	 String date2 ="2016 May 10 22:05 GMT";
	 String date3="2016 May 10 21:00 GMT";
	 
	 try {
		assertEquals("False value expected as time entered is before 9 pm",false,search.checkNextDayFlight(date1));
		assertEquals("True value expected as time entered is after 9 pm",true,search.checkNextDayFlight(date2));
		assertEquals("False value expected as time entered is 9 pm",false,search.checkNextDayFlight(date3));
		
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
	 
 }
 
 /**
  * testing addOneDay() method of the class
  */
 public void testAddOneDay(){
	 FlightSearch search=new FlightSearch(null, null, null, null);
	 String date ="2016 May 10 00:05 GMT";
	 String nextdate="2016 May 11 00:05 GMT";
	 
	 try {
		assertEquals("Next date not adding up,expected date +1",nextdate,search.addOneday(date));
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
 }
 
 /**
  * test isValidLayover() method of class
  */
 public void testIsValidLayover(){
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
	 
	 try {
		assertEquals("false value expected as layover time >3 hours", false,search.isValidLayover(arrival_time1, departure_time1));
		assertEquals("true value expected as layover time <3 hours", true,search.isValidLayover(arrival_time2, departure_time2));
		assertEquals("true value expected as layover time =3 hours", true,search.isValidLayover(arrival_time3, departure_time3));
		assertEquals("true value expected as layover time =30 mins", true,search.isValidLayover(arrival_time4, departure_time4));
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 
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
			0,
			"$28.79",
			0
			);
	
	FlightSearch search1=new FlightSearch(null, null, null, "FirstClass");
	FlightSearch search2=new FlightSearch(null, null, null, "Coach");
	
	assertEquals("Expected true as # seats First Class>0",true, search1.seatsAvailable(f1));
	assertEquals("Expected true as # seats Coach>0",true, search2.seatsAvailable(f1));
	assertEquals("Expected false as # seats First Class=0",false, search1.seatsAvailable(f2));
	assertEquals("Expected false as # seats Coach=0",false, search2.seatsAvailable(f2));
 
 }
 /**
  * Testing reserveFlight() of class
  */
 public void testReserveFlight(){
	 FlightSearch search1=new FlightSearch("BOS","LGA","2016 May 10 03:05 GMT", "FirstClass");
	 FlightSearch search2=new FlightSearch("BOS","LGA","2016 May 10 03:05 GMT", "Coach");
	 //Verifying the seat count increased after using the reservation functionality
	 try {

		assertEquals("Reservation functionality not working for booking first class seats", true,search1.reserveFlight(search1.getOptions().get(0)));
		assertEquals("Reservation functionality not working for booking Coach seats", true,search1.reserveFlight(search2.getOptions().get(0)));
		
		}
		
	 catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
	 
	 
	 
	 
 }
 
 

}

