package client.search;

import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import client.dao.*;
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
	private ServerInterfaceCache mServerInterface;

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
		this.mServerInterface=ServerInterfaceCache.getInstance();
	}

	/**
	 * This method converts a date string from "yyyy MMM dd HH:mm z" format to "yyyy_MM_dd" format.
	 * 
	 * @param date represents the string in "yyyy MMM dd HH:mm z" format.
	 * @return string in "yyyy_MM_dd" format.
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
		} else {
			return false;
		}
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
	 * This method checks for the constraints that the layover_time >= 30min and layover_time<=3hrs.
	 * The above constraint is verified for connecting flights
	 * @param arrivalTime represents the arrival time of a flight in "yyyy MMM dd HH:mm z" string format.
	 * @param departureTime represents the departure time of a connecting flight in "yyyy MMM dd HH:mm z" string format.
	 * @return true if constraint is followed else returns false.
	 * @throws ParseException
	 */
	public boolean isValidLayover(String arrivalTime,String departureTime) throws ParseException{
		long layover = 0;
		DateTimeFormatter flightDateFormat = DateTimeFormatter.ofPattern("yyyy MMM d H:m z");
		LocalDateTime departTimeLocal = LocalDateTime.parse(departureTime,flightDateFormat);
		ZonedDateTime departTimeZoned = departTimeLocal.atZone(ZoneId.of("GMT"));
		long dTime = departTimeZoned.toInstant().toEpochMilli();
		LocalDateTime arrivalTimeLocal = LocalDateTime.parse(arrivalTime, flightDateFormat);
		ZonedDateTime arrivalTimeZoned = arrivalTimeLocal.atZone(ZoneId.of("GMT"));
		long aTime = arrivalTimeZoned.toInstant().toEpochMilli();
		layover=dTime-aTime;

		if(layover<Configuration.MIN_LAYOVER_TIME)
			return false;
		else if(layover>Configuration.MAX_LAYOVER_TIME)
			return false;
		else
			return true;
	}

	/**
	 * This method checks for the constraint that seats must exist on the flight.
	 * @param flight represents a flight object to be checked.
	 * @return true if constraint is followed else returns false.
	 */
	public boolean seatsAvailable(Flight flight) {
		if(this.mSeatPreference.equals("FirstClass")) {
			if(flight.getmSeatsFirstclass() == 0) {
				return false;
			}
		} else {
			if(flight.getmSeatsCoach() == 0) {
				return false;
			}
		}
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

		ArrayList<ReservationOption>reservedOptions=new ArrayList<ReservationOption>();
		Flights outboundflights=new Flights();
		Queue<ArrayList<Flight>> nodeQueue = new ArrayDeque<ArrayList<Flight>>();

		if(this.mDepartureAirportCode.equals(this.mArrivalAirportCode)) {
			return reservedOptions;
		}
		
		addFlights(this.mDepartureAirportCode,dateFormatter(this.mDepartureDate),outboundflights);
		for(int i = 0; i < outboundflights.size(); i++) {
			ArrayList<Flight> option=new ArrayList<Flight>(Configuration.MAX_LAYOVER+1);
			Flight flight = outboundflights.get(i);
			if(!seatsAvailable(flight)) {
				continue;
			}
			option.add(flight);
			nodeQueue.add(option);
		}

		while(!nodeQueue.isEmpty()) {
			ArrayList<Flight> current = nodeQueue.poll();
			Flight currFlight = current.get(current.size()-1);
			if(currFlight.getmCodeArrival().equals(this.mArrivalAirportCode)){
				reservedOptions.add(new ReservationOption(current));
				continue;
			}
			if ( current.size() > Configuration.MAX_LAYOVER) {
				continue;
			}
			Flights outFlights=new Flights();
			addFlights(currFlight.getmCodeArrival(),dateFormatter(currFlight.getmTimeArrival()),outFlights);
			if(checkNextDayFlight(currFlight.getmTimeArrival())){
				addFlights(currFlight.getmCodeArrival(),dateFormatter(addOneday(currFlight.getmTimeArrival())),outFlights);
			}
			Flight lastFlight = current.get(current.size()-1);
			for(Flight flight:outFlights) {
				ArrayList<Flight> option=new ArrayList<Flight>(Configuration.MAX_LAYOVER+1);
				if(!seatsAvailable(flight)) {
					continue;
				}
				if(!isValidLayover(lastFlight.getmTimeArrival(),flight.getmTimeDepart())) {
					continue;
				}
				option.addAll(current);
				option.add(flight);
				nodeQueue.add(option);
			}
		}
		return reservedOptions;
	}
	
	public boolean reserveFlight(ReservationOption selectedOption){
		//get the lock to the DB
		if(!mServerInterface.lock(Configuration.TICKET_AGENCY)){
			System.out.println("Lock not available.Try again Later");
			return false;
		}
		//creating a XML string of flight data
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		 try {
			Flight flight;
			Element flight_xml;
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.newDocument();
			Element root = document.createElement("Flights");
			document.appendChild(root);
			for(int j=0;j<3;j++){
				flight=selectedOption.getFlight(j);
				if(flight==null)
					break;
				flight_xml=document.createElement("Flight");
				flight_xml.setAttribute("number",flight.getmNumber());
				flight_xml.setAttribute("seating",this.mSeatPreference);
				root.appendChild(flight_xml);
				}
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(document), new StreamResult(writer));
			System.out.println("Xml filght data"+writer.toString());
			
			if(mServerInterface.buyTickets(Configuration.TICKET_AGENCY,writer.toString())){
				mServerInterface.unlock(Configuration.TICKET_AGENCY);
				return true;
			 }
			else{
				mServerInterface.unlock(Configuration.TICKET_AGENCY);
				mServerInterface.clearFlightCache();
				return false;
			}
		
		 } catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}
	
}
