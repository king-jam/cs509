/**
 * 
 */
package client.util;

/**
 * @author blake
 *
 */
public class QueryFactory {
	
	public static String getAirports(String ticketAgency) {
		return "?team=" + ticketAgency + "&action=list&list_type=airports";
	}
	
	public static String getAirplanes(String ticketAgency) {
		return "?team=" + ticketAgency + "&action=list&list_type=airplanes";
	}
	
	public static String getFlightsDeparting(String team, String airportCode, String day) {
		String query = "?team=" + team;
		query = query + "&action=list";
		query = query + "&list_type=departing";
		query = query + "&airport=" + airportCode;
		query = query + "&day=" + day;
		return query;
	}
	public static String lock (String ticketAgency) {
		return "team=" + ticketAgency + "&action=lockDB";
	}
	
	public static String unlock (String ticketAgency) {
		return "team=" + ticketAgency + "&action=unlockDB";
	}
	
	public static String reserve (String ticketAgency, String xmlFlights) {
		String query = "team=" + ticketAgency;
		query = query + "&action=buyTickets";
		query = query + "&flightData=" + xmlFlights;
		return query;
	}

}
