package client.search;

import java.io.Console;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
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
	private boolean mOneWay;
	private String mSeatPrefrence;
	private String mDepartureDate;
	private String mReturnDate;
	private Configuration mConfig;
	private ServerInterface mServerInterface;
	
	
	public FlightSearch(String departureAirportCode,
			String ArrivalAirportCode, boolean oneWay, String seatPrefrence,
			String Departuredate, String ReturnDate) {
		
		this.mDepartureAirportCode = departureAirportCode;
		this.mArrivalAirportCode = ArrivalAirportCode;
		this.mOneWay = oneWay;
		this.mSeatPrefrence = seatPrefrence;
		this.mDepartureDate = Departuredate;
		this.mReturnDate = ReturnDate;
		this.mConfig=Configuration.getInstance();
		this.mServerInterface=new ServerInterface();
	}
	
	public String dateFormatter(String date) throws ParseException{
		
		SimpleDateFormat formatter=new  SimpleDateFormat("yyyy MMM dd HH:mm z");
		SimpleDateFormat departureDateFormatter=new  SimpleDateFormat("yyyy_MM_dd");
		departureDateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		String departuredate=departureDateFormatter.format(formatter.parse(date));
		return departuredate;
	
	}
	public boolean checkDepartureTime(String arrivalTime,String departureTime) throws ParseException{
		SimpleDateFormat formatter=new  SimpleDateFormat("yyyy MMM dd HH:mm z");
		Date arrival=formatter.parse(arrivalTime);
		Date departure=formatter.parse(departureTime);
		if(arrival.after(departure))
			return true;
		else
			return false;
	}
	
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
	
	public String addOneday(String date) throws ParseException{
		SimpleDateFormat formatter=new  SimpleDateFormat("yyyy MMM dd HH:mm z");
		Calendar calendar=GregorianCalendar.getInstance();
		calendar.setTime(formatter.parse(date));
		calendar.add(Calendar.DATE, 1);
		return formatter.format(calendar.getTime());
			
	}
	
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
	
	public void addFlights(String airportCode,String departuredate,Flights flights){
		String xmlFlightData=mServerInterface.getFlights(mConfig.TICKET_AGENCY,
				airportCode,departuredate);
		flights.addAll(xmlFlightData);
			
	}
	
	public ArrayList<ReservationOption> getOptions() throws ParseException{
		
		ArrayList<Flight> reservedflights=new ArrayList<Flight>();
		ArrayList<ReservationOption>reservedOptions=new ArrayList<ReservationOption>();
		Flights firstOutboundflights=new Flights();
		Flights secondOutboundflights=new Flights();
		Flights thirdOutboundflights=new Flights();


		addFlights(this.mDepartureAirportCode,dateFormatter(this.mDepartureDate),firstOutboundflights);

		for(Flight flight:firstOutboundflights){
			
			//determining flight with no layover
			if(flight.getmCodeArrival().equals(this.mArrivalAirportCode)){
				reservedflights.add(flight);
				reservedOptions.add(new ReservationOption(reservedflights));
				reservedflights.clear();	
				
			}
			
			else{

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
							reservedOptions.add(new ReservationOption(reservedflights));
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
										reservedOptions.add(new ReservationOption(reservedflights));
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
