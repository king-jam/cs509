/**
 * 
 */
package client.util;
/**
 * This class is global configuration object storing project specific settings.
 * 
 * @author Kartik
 * @version 1
 * @since 07/03/2016
 *
 */
public final class Configuration {
	/**
	 * Static definition of Configuration instance for singleton pattern
	 */
	private static Configuration instance; 
	/**
	 * private default constructor for singleton pattern
	 * 
	 * The default constructor initializes the object instance to default / invalid values.
	 * 
	 * precondition none
	 * postcondition attributes are initialized with valid structures 
	 */
	private Configuration(){};
	/**
	 * get the Configuration instance
	 * 
	 * @return the singleton instance of the global Configuration object
	 */
	public static Configuration getInstance(){
		if(instance==null)
			instance=new Configuration();
		return instance;
	}
	/**
	 * Data attributes for a Configuration
	 */
	public static String TICKET_AGENCY="Team01";
	public static final int MAX_LAYOVER=2;
	public static final long MAX_LAYOVER_TIME=2*3600*1000;//in milli seconds
	public static final long MIN_LAYOVER_TIME=1800*1000;//in milli seconds
	public static final int DAY_HOUR_NEXT_FLIGHT=21;//in 24 hr format,9:00pm
	/**
	 * get the ticket agency string
	 * 
	 * @return the ticket agency string
	 */
	public static String getAgency() {
		return TICKET_AGENCY;
	}
	/**
	 * get the max layovers value for search limits
	 * 
	 * @return the max layovers value
	 */
	public static int getMaxLayover() {
		return MAX_LAYOVER;
	}
	/**
	 * get the max layovers time for search limits
	 * 
	 * @return the max layovers time
	 */
	public static long getMaxLayoverTime() {
		return MAX_LAYOVER_TIME;
	}
	/**
	 * get the min layovers time for search limits
	 * 
	 * @return min max layovers time
	 */
	public static long getMinLayoverTime() {
		return MIN_LAYOVER_TIME;
	}
	/**
	 * get the hour/day cutoff for search limits
	 * 
	 * @return get the hour/day cutoff
	 */
	public static int getDayHourNextFlight() {
		return DAY_HOUR_NEXT_FLIGHT;
	}
}
