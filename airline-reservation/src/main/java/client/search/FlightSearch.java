package client.search;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;

import client.dao.ServerInterface;
import client.flight.*;
import client.util.*;
import client.reservation.*;


/**
 * This class performs search and reserve operation
 * 
 * @author Kartik
 * @version 1
 * @since 06/26/2016
 */
public class FlightSearch {
	/**
	 * Member attributes describing a flight search
	 */
	private String mDepartureAirportCode;
	private String mArrivalAirportCode;
	private String mDepartureDate;
	private String mSeatPreference;
	private String mTicketAgency;
	private ServerInterface mServerInterface;
	
	/**
	 * Constructor
	 * 
	 * @param departureAirportCode (required) valid departure airport code.
	 * @param ArrivalAirportCode (required) valid arrival airport code.
	 * @param seatPrefrence represents first class or coach seating.
	 * @param Departuredate represents departure date in "yyyy MMM dd HH:mm z" format.
	 */
	public FlightSearch(String departureAirportCode,
			String arrivalAirportCode,
			String departuredate, String seatPreference) {
		
		this.mDepartureAirportCode = departureAirportCode;
		this.mArrivalAirportCode = arrivalAirportCode;
		this.mDepartureDate = departuredate;
		this.mSeatPreference = seatPreference;
		this.mTicketAgency=Configuration.getAgency();
		this.mServerInterface=new ServerInterface();
	}
	
	/**
	 * This method converts a date string from "yyyy MMM dd HH:mm z" format to "yyyy_MM_dd" format.
	 * 
	 * @param date represents the string in "yyyy MMM dd HH:mm z" format.
	 * @return string in "yyyy_MM_dd"
	 * @throws ParseException
	 */
	public String dateFormatter(String date) throws ParseException{
		
		SimpleDateFormat formatter=new  SimpleDateFormat("yyyy MMM dd HH:mm z");
		SimpleDateFormat departureDateFormatter=new  SimpleDateFormat("yyyy_MM_dd");
		departureDateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		String departuredate=departureDateFormatter.format(formatter.parse(date));
		return departuredate;
	}
	
	/**
	 * This method checks if the arrival time of a particular flight is after the departure time of another.
	 * 
	 * @param arrivalTime represents the string in "yyyy MMM dd HH:mm z" format.
	 * @param departureTime represents the string in "yyyy MMM dd HH:mm z" format.
	 * @return
	 * @throws ParseException
	 */
	public boolean checkDepartureTime(String arrivalTime,String departureTime) throws ParseException{
		SimpleDateFormat formatter=new  SimpleDateFormat("yyyy MMM dd HH:mm z");
		Date arrival=formatter.parse(arrivalTime);
		Date departure=formatter.parse(departureTime);
		if(arrival.after(departure))
			return true;
		else
			return false;
	}
	
	/**
	 * This method verifies whether a arrival time of a flight is after 9pm.
	 * It is a constraint to check for connecting flights in the next day.
	 * 
	 * @param arrivalTime represents the arrival time of a flight in "yyyy MMM dd HH:mm z" string format.
	 * @return true if arrival time of a flight is after 9pm else returns false boolean value.
	 * @throws ParseException
	 */
	public boolean checkNextDayFlight(String arrivalTime) throws ParseException {
		SimpleDateFormat formatter=new  SimpleDateFormat("yyyy MMM dd HH:mm z");
		Calendar calendar=GregorianCalendar.getInstance();
		calendar.setTime(formatter.parse(arrivalTime));
		int hour=calendar.get(Calendar.HOUR_OF_DAY);
		int minute=calendar.get(Calendar.MINUTE);
		if((hour==Configuration.DAY_HOUR_NEXT_FLIGHT && minute>0)||(hour>Configuration.DAY_HOUR_NEXT_FLIGHT)){
			return true;		
		}
		else
			return false;
	}
	
	/**
	 * This method adds one day to date given as the parameter.
	 * 
	 * @param date represents the date  in "yyyy MMM dd HH:mm z" string format.
	 * @return date which is incremented by one,also represented in "yyyy MMM dd HH:mm z" string format.
	 * @throws ParseException
	 */
	public String addOneday(String date) throws ParseException{
		SimpleDateFormat formatter=new  SimpleDateFormat("yyyy MMM dd HH:mm z");
		Calendar calendar=GregorianCalendar.getInstance();
		calendar.setTime(formatter.parse(date));
		calendar.add(Calendar.DATE, 1);
		return formatter.format(calendar.getTime());
	}
	
	/**
	 * This method checks for the constraint that the layover_time >= 30min and layover_time<=3hrs.
	 * The above constraint is verified for connecting flights
	 * @param arrivalTime represents the arrival time of a flight in "yyyy MMM dd HH:mm z" string format.
	 * @param departureTime represents the departure time of a flight in "yyyy MMM dd HH:mm z" string format.
	 * @return true if constraint is followed else returns false.
	 * @throws ParseException
	 */
	public boolean checkLayoverTime(String arrivalTime,String departureTime) throws ParseException{
		SimpleDateFormat formatter=new  SimpleDateFormat("yyyy MMM dd HH:mm z");
		long arrival=formatter.parse(arrivalTime).getTime();
		long departure=formatter.parse(departureTime).getTime();
		long layover=departure-arrival;
		
		if(layover<Configuration.MIN_LAYOVER_TIME)
			return false;
		else if(layover>Configuration.MAX_LAYOVER_TIME)
			return false;
		else
			return true;
	}
	
	/**
	 * This method uses the server interface to get flight information and then add it to a collection.
	 * 
	 * @param airportCode (required) valid arrival airport code.
	 * @param departuredate in "yyyy_MM_dd" string format.
	 * @param flights is of the type {@link client.flight.Flights} to hold Flight objects({@link client.flight.Flight}).
	 */
	public void addFlights(String airportCode,String departuredate,Flights flights){
		String xmlFlightData=mServerInterface.getFlights(mTicketAgency,
				airportCode,departuredate);
		flights.addAll(xmlFlightData);
	}
	
	/**
	 * This method clones a list of type ArrayList<Flight>
	 * @param list
	 * @return  a cloned list of type ArrayList<Flight>
	 */
	public static ArrayList<Flight> cloneList(ArrayList<Flight> list) {
		ArrayList<Flight> clone = new ArrayList<Flight>(list.size());
	    for(Flight item: list) 
	    	clone.add(item);
	    return clone;
	}
	
	/**
	 * This method uses the search parameters to search for valid flights 
	 * @return an arrayList of the type {@link client.reservation.ReservationOption}
	 * @throws ParseException
	 */
	public ArrayList<ReservationOption> getOptions() throws ParseException{
		
		ArrayList<Flight> reservedflights=new ArrayList<Flight>();
		ArrayList<ReservationOption>reservedOptions=new ArrayList<ReservationOption>();
		Flights firstOutboundflights=new Flights();
		Flights secondOutboundflights=new Flights();
		Flights thirdOutboundflights=new Flights();

		addFlights(this.mDepartureAirportCode,dateFormatter(this.mDepartureDate),firstOutboundflights);

		for(Flight flight:firstOutboundflights){
			
			// eliminate all flights without seats for our seat type
			if(mSeatPreference.equals("firstclass")) {
				if(flight.getmSeatsFirstclass() == 0) {
					continue;
				}
			} else {
				if(flight.getmSeatsCoach() == 0) {
					continue;
				}
			}
			
			//determining flight with no layover
			if(flight.getmCodeArrival().equals(this.mArrivalAirportCode)){
				reservedflights.add(flight);
				reservedOptions.add(new ReservationOption(cloneList(reservedflights)));
				reservedflights.clear();	
			} else {
				    addFlights(flight.getmCodeArrival(),dateFormatter(flight.getmTimeArrival()),secondOutboundflights);
				    if(checkNextDayFlight(flight.getmTimeArrival())){
				    	addFlights(flight.getmCodeArrival(),dateFormatter(addOneday(flight.getmTimeArrival())),secondOutboundflights);
				    }
					for(Flight firstLayoverFlight:secondOutboundflights){
						
						if(checkDepartureTime(flight.getmTimeArrival(),firstLayoverFlight.getmTimeDepart()))
							continue;
						
						if(firstLayoverFlight.getmCodeArrival().equals(this.mArrivalAirportCode)){
							if(checkLayoverTime(flight.getmTimeArrival(),firstLayoverFlight.getmTimeDepart())){
							reservedflights.add(flight);
							reservedflights.add(firstLayoverFlight);
							reservedOptions.add(new ReservationOption(cloneList(reservedflights)));
							reservedflights.clear();
							}
						}
						else{
							
							addFlights(firstLayoverFlight.getmCodeArrival(),dateFormatter(firstLayoverFlight.getmTimeArrival()),thirdOutboundflights);
							
							if(checkNextDayFlight(firstLayoverFlight.getmTimeArrival())){
						    	addFlights(firstLayoverFlight.getmCodeArrival(),dateFormatter(addOneday(firstLayoverFlight.getmTimeArrival())),thirdOutboundflights);
						    }
							
							for(Flight secondLayoverFlight:thirdOutboundflights){
								if(checkDepartureTime(firstLayoverFlight.getmTimeArrival(),secondLayoverFlight.getmTimeDepart()))
									continue;
								if(secondLayoverFlight.getmCodeArrival().equals(this.mArrivalAirportCode)){
									if(checkLayoverTime(flight.getmTimeArrival(),firstLayoverFlight.getmTimeDepart()) &&
											checkLayoverTime(firstLayoverFlight.getmTimeArrival(),secondLayoverFlight.getmTimeDepart())){
										
										reservedflights.add(flight);
										reservedflights.add(firstLayoverFlight);
										reservedflights.add(secondLayoverFlight);
										reservedOptions.add(new ReservationOption(cloneList(reservedflights)));
										reservedflights.clear();
											
									}
									
								}
								
							}
							thirdOutboundflights.clear();
							
						}
						
					}
					secondOutboundflights.clear();		
			}
		}
		return reservedOptions;
	}
}
