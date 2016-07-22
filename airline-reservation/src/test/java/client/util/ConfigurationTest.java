package client.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ConfigurationTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public ConfigurationTest( String testName )
	{
		super( testName );
	}
	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite()
	{
		return new TestSuite( ConfigurationTest.class );
	}

	public void testConfiguration() {
		Configuration config = Configuration.getInstance();
		assertEquals("The singleton was not returned", config, Configuration.getInstance());
		assertEquals("The agency did not match", "Team01", Configuration.getAgency());
		assertEquals("The max layovers did not match", 2, Configuration.getMaxLayover());
		assertEquals("The max layover time did not match", 3*3600*1000, Configuration.getMaxLayoverTime());
		assertEquals("The min layover time did not match", 1800*1000, Configuration.getMinLayoverTime());
		assertEquals("The cutoff day/hour did not match", 21, Configuration.getDayHourNextFlight());
	}
}
