/**
 * 
 */
package client.flight;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class holds values pertaining to a single flight from one airport to another. 
 * Class member attributes are the same as defined by the CS509 server API and store 
 * values after conversion from XML received from the server to Java primitives. 
 * Attributes are accessed via getter and setter methods.
 * 
 * @author blake
 * @version 1
 * @since 2016-02-24
 *
 */
public class Flight {

	/**
	 * Member attributes describing a flight
	 */
	private String mAirplane;
	private String mFlightTime;
	private String mNumber;
	private String mCodeDepart;
	private String mTimeDepart;
	private String mCodeArrival;
	private String mTimeArrival;
	private String mPriceFirstclass;
	private int mSeatsFirstclass;
	private String mPriceCoach;
	private int mSeatsCoach;
	
	public Flight (
			String airplane,
			String flightTime,
			String number,
			String codeDepart,
			String timeDepart,
			String codeArrival,
			String timeArrival,
			String priceFirstclass,
			int seatsFirstclass,
			String priceCoach,
			int seatsCoach) {
		
		mAirplane = airplane;
		mFlightTime = flightTime;
		mNumber = number;
		mCodeDepart = codeDepart;
		mTimeDepart = timeDepart;
		mCodeArrival = codeArrival;
		mTimeArrival = timeArrival;
		mPriceFirstclass = priceFirstclass;
		mSeatsFirstclass = seatsFirstclass;
		mPriceCoach = priceCoach;
		mSeatsCoach = seatsCoach;
	}

	/**
	 * Determine if a Flight is reasonably valid
	 * 
	 * @return true if the Flight instance is reasonable valid
	 */
	public boolean isValid() {
		try {
			if ((mAirplane == null) || (mAirplane.length() == 0)) {
				return false;
			}
			if (Integer.parseInt(mFlightTime) <= 0) {
				return false;
			}
			if (Integer.parseInt(mNumber) <= 0) {
				return false;
			}
			if (mCodeDepart.length() != 3) {
				return false;
			}
			if (mCodeArrival.length() != 3) {
				return false;
			}
			// verify departure time and arrival time are expected formats
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm z");
			@SuppressWarnings("unused")
			Date tmpDate = sdf.parse(mTimeDepart);
			tmpDate = sdf.parse(mTimeArrival);
			
			/**
			 * Need to match:
			 * $ at start of string
			 * some number of digits for dollars
			 * . as dollar cents separator
			 * two digits as cents
			 * end of string. no following characters
			 */
			
			if (!mPriceFirstclass.matches("^\\$\\d*\\.\\d\\d$")) {
				return false;
			}
			if (!mPriceCoach.matches("^\\$\\d*\\.\\d\\d$")) {
				return false;
			}
			
			if (mSeatsFirstclass < 0) {
				return false;
			}
			if (mSeatsCoach < 0) {
				return false;
			}
		} catch (Exception ex) {
			// On any exception, including parse exceptions, the object is not valid
			return false;
		}
		
		// Nothing invalid detected, the object instance looks good
		return true;
	}
	
	/**
	 * @return the mAirplane
	 */
	public String getmAirplane() {
		return mAirplane;
	}

	/**
	 * @param mAirplane the mAirplane to set
	 */
	public void setmAirplane(String mAirplane) {
		this.mAirplane = mAirplane;
	}

	/**
	 * @return the mFlightTime
	 */
	public String getmFlightTime() {
		return mFlightTime;
	}

	/**
	 * @param mFlightTime the mFlightTime to set
	 */
	public void setmFlightTime(String mFlightTime) {
		this.mFlightTime = mFlightTime;
	}

	/**
	 * @return the mNumber
	 */
	public String getmNumber() {
		return mNumber;
	}

	/**
	 * @param mNumber the mNumber to set
	 */
	public void setmNumber(String mNumber) {
		this.mNumber = mNumber;
	}

	/**
	 * @return the mCodeDepart
	 */
	public String getmCodeDepart() {
		return mCodeDepart;
	}

	/**
	 * @param mCodeDepart the mCodeDepart to set
	 */
	public void setmCodeDepart(String mCodeDepart) {
		this.mCodeDepart = mCodeDepart;
	}

	/**
	 * @return the mTimeDepart
	 */
	public String getmTimeDepart() {
		return mTimeDepart;
	}

	/**
	 * @param mTimeDepart the mTimeDepart to set
	 */
	public void setmTimeDepart(String mTimeDepart) {
		this.mTimeDepart = mTimeDepart;
	}

	/**
	 * @return the mCodeArrival
	 */
	public String getmCodeArrival() {
		return mCodeArrival;
	}

	/**
	 * @param mCodeArrival the mCodeArrival to set
	 */
	public void setmCodeArrival(String mCodeArrival) {
		this.mCodeArrival = mCodeArrival;
	}

	/**
	 * @return the mTimeArrival
	 */
	public String getmTimeArrival() {
		return mTimeArrival;
	}

	/**
	 * @param mTimeArrival the mTimeArrival to set
	 */
	public void setmTimeArrival(String mTimeArrival) {
		this.mTimeArrival = mTimeArrival;
	}

	/**
	 * @return the mPriceFirstclass
	 */
	public String getmPriceFirstclass() {
		return mPriceFirstclass;
	}

	/**
	 * @param mPriceFirstclass the mPriceFirstclass to set
	 */
	public void setmPriceFirstclass(String mPriceFirstclass) {
		this.mPriceFirstclass = mPriceFirstclass;
	}

	/**
	 * @return the mSeatsFirstclass
	 */
	public int getmSeatsFirstclass() {
		return mSeatsFirstclass;
	}

	/**
	 * @param mSeatsFirstclass the mSeatsFirstclass to set
	 */
	public void setmSeatsFirstclass(int mSeatsFirstclass) {
		this.mSeatsFirstclass = mSeatsFirstclass;
	}

	/**
	 * @return the mPriceCoach
	 */
	public String getmPriceCoach() {
		return mPriceCoach;
	}

	/**
	 * @param mPriceCoach the mPriceEconomy to set
	 */
	public void setmPriceCoach(String mPriceCoach) {
		this.mPriceCoach = mPriceCoach;
	}

	/**
	 * @return the mSeatsEconomy
	 */
	public int getmSeatsCoach() {
		return mSeatsCoach;
	}

	/**
	 * @param mSeatsCoach the mSeatsEconomy to set
	 */
	public void setmSeatsCoach(int mSeatsCoach) {
		this.mSeatsCoach = mSeatsCoach;
	}	
}
 

