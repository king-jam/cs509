/**
 * 
 */
package client.airport;

/**
 * This class holds values pertaining to a single Airport. Class member attributes
 * are the same as defined by the CS509 server API and store values after conversion from
 * XML received from the server to Java primitives. Attributes are accessed via getter and 
 * setter methods.
 * 
 * @author blake
 * @version 1.1
 * @since 2016-02-24
 *
 */
public class Airport {
	
	/**
	 * Constant values used for latitude and longitude range validation
	 */
	static final double MAX_LATITUDE = 90.0;
	static final double MIN_LATITUDE = -90.0;
	static final double MAX_LONGITUDE = 180.0;
	static final double MIN_LONGITUDE = -180.0;
	
	/**
	 * Airport attributes as defined by the CS509 server interface XML
	 */
	private String mName;              // Full name of the airport
	private String mCode;              // Three character code of the airport
	private double mLatitude;          // Latitude of airport in decimal format
	private double mLongitude;         // Longitude of the airport in decimal format
	
	/**
	 * Default constructor
	 * 
	 * Constructor without params. Requires object fields to be explicitly
	 * set using setter methods
	 * 
	 * @precondition None
	 * @postcondition member attributes are initialized to invalid default values
	 */	
	public Airport () {
		mName = "";
		mCode = "";
		mLatitude = Double.MAX_VALUE;
		mLongitude = Double.MAX_VALUE;
	}
	
	/**
	 * Initializing constructor.
	 * 
	 * All attributes are initialized with input values
	 *  
	 * @param name The human readable name of the airport
	 * @param code The 3 letter code for the airport
	 * @param latitude The north/south coordinate of the airport 
	 * @param longitude The east/west coordinate of the airport
	 * 
	 * @preconditions code is a 3 character string, latitude and longitude are valid values
	 * @postconditions member attributes are initialized with input parameter values
	 */
	public Airport (String name, String code, double latitude, double longitude) {
		mName = name;
		mCode = code;
		mLatitude = latitude;
		mLongitude = longitude;
	}
	
	/**
	 * Initializing constructor with all params as type String. Converts latitude and longitude
	 * values to required double format and invokes another ctor which initializes object using
	 * expected types.
	 * 
	 * @param name The human readable name of the airport
	 * @param code The 3 letter code for the airport
	 * @param latitude is the string representation of latitude decimal format 
	 * @param longitude is the String representation of the longitude in decimal format
	 * 
	 * @preconditions the latitude and longitude are valid String representations of decimal values
	 */
	public Airport (String name, String code, String latitude, String longitude) {
		this (name, code, Double.parseDouble(latitude), Double.parseDouble(latitude));
	}
	
	/**
	 * Set the airport name
	 * 
	 * @param name The human readable name of the airport
	 */
	public void name (String name) {
		mName = name;
	}
	
	/**
	 * get the airport name
	 * 
	 * @return Airport name
	 */
	public String name () {
		return mName;
	}
	
	/**
	 * set the airport 3 letter code
	 * 
	 * @param code The 3 letter code for the airport
	 */
	public void code (String code) {
		mCode = code;
	}
	
	/**
	 * Get the 3 letter airport code
	 * 
	 * @return The 3 letter airport code
	 */
	public String code () {
		return mCode;
	}
	
	/**
	 * Set the latitude for the airport
	 * 
	 * @param latitude The north/south coordinate of the airport 
	 */
	public void latitude (double latitude) {
		mLatitude = latitude;
	}
	
	/**
	 * Get the latitude for the airport
	 * 
	 * @return The north/south coordinate of the airport 
	 */
	public double latitude () {
		return mLatitude;
	}
	
	/**
	 * Set the longitude for the airport
	 * 
	 * @param longitude The east/west coordinate of the airport
	 */
	public void longitude (double longitude) {
		mLongitude = longitude;
	}
	
	/**
	 * get the longitude for the airport
	 * 
	 * @return The east/west coordinate of the airport
	 */
	public double longitude () {
		return mLongitude;
	}

	/**
	 * Determine if two airport objects are the same airport
	 * 
	 * Compare another object to this airport and return true if the other 
	 * object specifies the same airport as this object
	 * 
	 * @param obj is the object to compare against this object
	 * @return true if the param is the same airport as this, else false
	 */
	@Override
	public boolean equals (Object obj) {
		// every object is equal to itself
		if (obj == this)
			return true;
		
		// null not equal to anything
		if (obj == null)
			return false;
		
		// can't be equal if obj is not an instance of Airport
		if (!(obj instanceof Airport)) 
			return false;
		
		// if all fields are equal, the Airports are the same
		Airport rhs = (Airport) obj;
		if ((rhs.mName.equals(mName)) &&
				(rhs.mCode.equals(mCode)) &&
				(rhs.mLatitude == mLatitude) &&
				(rhs.mLongitude == mLongitude)) {
			return true;
		}
		
		return false;	
	}
	
	/**
	 * Determine if object instance has valid attribute data
	 * 
	 * Verifies the name is not null and not an empty string. Verifies code is 3 characters in length.
	 * Verifies latitude is between +90.0 north pole and -90.0 south pole.
	 * Verifies longitude is between +180.0 east prime meridian and -180.0 west prime meridian.
	 * 
	 * @return true if object passes above validation checks
	 * 
	 */
	public boolean isValid() {
		
		// If the name isn't valid, the object isn't valid
		if ((mName == null) || (mName == ""))
			return false;
		
		// If we don't have a 3 character code, object isn't valid
		if ((mCode == null) || (mCode.length() != 3))
			return false;
		
		// Verify latitude and longitude are within range
		if ((mLatitude > MAX_LATITUDE) || (mLatitude < MIN_LATITUDE) ||
			(mLongitude > MAX_LONGITUDE) || (mLongitude < MIN_LONGITUDE)) {
			return false;
		}
		
		return true;
	}
}
