package client.reservation;

import client.flight.Flight;
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
     * test the getFlight method of the class
     */
    public void testReservationOption()
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
    	ReservationOption res = new ReservationOption(f1, f2, f3);
    	
        assertEquals("The returned flight (0) does not match", f1, res.getFlight(0) );
        assertEquals("The returned flight (1) does not match", f2, res.getFlight(1) );
        assertEquals("The returned flight (2) does not match", f3, res.getFlight(2) );
        assertEquals("The returned # of flights does not match", 3, res.getNumFlights() );
        assertEquals("The returned # of layovers does not match", 2, res.getNumLayovers() );
        assertEquals("The returned price does not match Coach", 70.37, res.getPrice("Coach") );
        assertEquals("The returned price does not match First Class", 211.33, res.getPrice("FirstClass") );
        assertEquals("The returned total time does not match", "13:25", res.getTotalTime() );

    }
}
