package client.util;

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

	public void testTimeLocal() throws Exception {
		TimeLocal tl = new TimeLocal(Configuration.getInstance().getGoogleTimezoneAPIKey());
		String result = tl.getLocalTime("2016 May 10 00:05 GMT", 42.365855, -71.009624);
		assertEquals("The time was not properly converted", result, "2016 May 9 20:05 EDT");
	}
}
