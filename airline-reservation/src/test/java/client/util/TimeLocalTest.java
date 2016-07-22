package client.util;

import java.time.ZoneId;
import java.util.TimeZone;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TimeLocalTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public TimeLocalTest( String testName )
	{
		super( testName );
	}
	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite()
	{
		return new TestSuite( TimeLocalTest.class );
	}

	public void testTimeLocalLatLng() throws Exception {
		TimeLocal tl = new TimeLocal(Configuration.getInstance().getGoogleTimezoneAPIKey());
		String result = tl.getLocalTimeLatLng("2016 May 10 00:05 GMT", 42.365855, -71.009624);
		assertEquals("The time was not properly converted", result, "2016 May 9 20:05 EDT");
	}
	
	public void testGetTimeZone() throws Exception {
		TimeLocal tl = new TimeLocal(Configuration.getInstance().getGoogleTimezoneAPIKey());
		TimeZone result = tl.getTimeZone(42.365855, -71.009624);
		assertEquals("The time was not properly converted", result.getID(), "America/New_York");
	}
	
	public void testGetLocalTime() throws Exception {
		TimeLocal tl = new TimeLocal(Configuration.getInstance().getGoogleTimezoneAPIKey());
		String result = tl.getLocalTime("2016 May 10 00:05 GMT", TimeZone.getTimeZone(ZoneId.of("America/New_York")));
		assertEquals("The time was not properly converted", result, "2016 May 9 20:05 EDT");
	}
}
