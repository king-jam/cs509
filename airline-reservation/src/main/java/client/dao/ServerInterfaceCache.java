/**
 * 
 */
package client.dao;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;

import client.util.QueryFactory;


/**
 * This class provides a cached interface to the CS509 server.
 * It caches requests via ehcache to improve overall speed.
 *   
 * @author James
 * @version 1
 * @since 2016-07-05
 *
 */
public class ServerInterfaceCache {
	
	private final String mUrlBase = "http://cs509.cs.wpi.edu:8181/CS509.server/ReservationSystem";
	private CacheManager cacheManager;
	private Cache<String, String> airportCache;
	private Cache<String, String> flightCache;
	
	private static ServerInterfaceCache instance; 
	
	public static ServerInterfaceCache getInstance(){
		if(instance==null)
			instance=new ServerInterfaceCache();
		return instance;
	}
	
	private ServerInterfaceCache() {
		this.cacheManager = CacheManagerBuilder.newCacheManagerBuilder().withCache("airports",
			    CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
			        ResourcePoolsBuilder.newResourcePoolsBuilder()
			            .heap(10, EntryUnit.ENTRIES)
			            .offheap(10, MemoryUnit.MB)) 
			        )
			    .build(true);
		this.airportCache = this.cacheManager.getCache("airports", String.class, String.class);
		this.flightCache = this.cacheManager.createCache("flights", 
			    CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class, 
			    		ResourcePoolsBuilder.newResourcePoolsBuilder()
			    		.heap(10, EntryUnit.ENTRIES)
			    		.offheap(10, MemoryUnit.MB)).build());
	}
	
	/**
	 * Return an XML list of all the airports
	 * 
	 * Retrieve the list of airports available to the specified ticketAgency via HTTPGet of the server
	 * 
	 * @param team identifies the Team requesting the information
	 * @return xml string listing all airports
	 */	
	public String getAirports (String team) {

		URL url;
		HttpURLConnection connection;
		BufferedReader reader;
		String line;
		StringBuffer result = new StringBuffer();
		String res = this.airportCache.get(team);
		if (res == null) {
			try {
				/**
				 * Create an HTTP connection to the server for a GET 
				 */
				url = new URL(mUrlBase + QueryFactory.getAirports(team));
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
	
				/**
				 * If response code of SUCCESS read the XML string returned
				 * line by line to build the full return string
				 */
				int responseCode = connection.getResponseCode();
				if ((responseCode >= 200) && (responseCode <= 299)) {
					InputStream inputStream = connection.getInputStream();
					String encoding = connection.getContentEncoding();
					encoding = (encoding == null ? "URF-8" : encoding);
	
					reader = new BufferedReader(new InputStreamReader(inputStream));
					while ((line = reader.readLine()) != null) {
						result.append(line);
					}
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			res = result.toString();
			this.airportCache.put(team, res);
			return res;
		}
		return res;
	}
	
	/**
	 * Return XML string identifying flights departing specified airport on specified day
	 * 
	 * @param team is the requesting the list of departing flights
	 * @param airportCode is the 3 character code identifying departure airport
	 * @param day is the day of departing flights
	 * 
	 * @return the XML string returned from the server
	 */
	public String getFlights (String team, String airportCode, String day) {
		
		URL url;
		HttpURLConnection connection;
		BufferedReader reader;
		String line;
		StringBuffer result = new StringBuffer();
		String key = airportCode + day;
		String res = this.flightCache.get(key);
		if (res == null) {
			try {
				/**
				 * Create an HTTP connection to the server for a GET 
				 */
				url = new URL(mUrlBase + QueryFactory.getFlightsDeparting(team, airportCode, day));
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setRequestProperty("User-Agent", team);
	
				/**
				 * If response code of SUCCESS read the XML string returned
				 * line by line to build the full return string
				 */
				int responseCode = connection.getResponseCode();
				if ((responseCode >= 200) && (responseCode <= 299)) {
					InputStream inputStream = connection.getInputStream();
					String encoding = connection.getContentEncoding();
					encoding = (encoding == null ? "URF-8" : encoding);
	
					reader = new BufferedReader(new InputStreamReader(inputStream));
					while ((line = reader.readLine()) != null) {
						result.append(line);
					}
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			res = result.toString();
			this.flightCache.put(key, res);
			return res;
		}
		return res;
	}
	
	/**
	 * Lock the server database in preparation to making a reservation
	 * 
	 * @param team identifies the team locking the database
	 * 
	 * @return true if database locked successfully
	 */
	public boolean lock (String team) {
		URL url;
		HttpURLConnection connection;

		try {
			url = new URL(mUrlBase);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent", team);
			connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			
			String params = QueryFactory.lock(team);
			
			connection.setDoOutput(true);
			DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
			writer.writeBytes(params);
			writer.flush();
			writer.close();
			
			int responseCode = connection.getResponseCode();
			System.out.println("\nSending 'POST' to lock database");
			System.out.println(("\nResponse Code : " + responseCode));
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuffer response = new StringBuffer();
			
			while ((line = in.readLine()) != null) {
				response.append(line);
			}
			in.close();
			
			System.out.println(response.toString());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}

	/**
	 * Unlock the server database previously locked
	 * 
	 * @param team identifies the team requestiong the server database be unlocked
	 * 
	 * @return true if server database successfully unlocked
	 */
	public boolean unlock (String team) {
		URL url;
		HttpURLConnection connection;
		
		try {
			url = new URL(mUrlBase);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			
			String params = QueryFactory.unlock(team);
			
			connection.setDoOutput(true);
			connection.setDoInput(true);
			
			DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
			writer.writeBytes(params);
			writer.flush();
			writer.close();
		    
			int responseCode = connection.getResponseCode();
			System.out.println("\nSending 'POST' to unlock database");
			System.out.println(("\nResponse Code : " + responseCode));

			if ((responseCode >= 200) && (responseCode <= 299)) {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				StringBuffer response = new StringBuffer();

				while ((line = in.readLine()) != null) {
					response.append(line);
				}
				in.close();

				System.out.println(response.toString());
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Reserve a seat on one or more connecting flights
	 * 
	 * The XML string identifying the reserveation is created by the calling client. 
	 * This method creates the HTTP POST request to reserve the fligt(s) as specified
	 * 
	 * @param team identifying the team making the reservation
	 * @param xmlReservation is the string identifying the reservation to make
	 * 
	 * @return true if SUCCESS code returned from server
	 */
	public boolean buyTickets(String team, String xmlReservation) {
		URL url;
		HttpURLConnection connection;

		try {
			url = new URL(mUrlBase);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");

			String params = QueryFactory.reserve(team, xmlReservation);

			System.out.println("\nSending 'POST' to ReserveFlights");
			System.out.println("\nSending " + params);
			
			connection.setDoOutput(true);
			connection.setDoInput(true);
			
			DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
			writer.writeBytes(params);
			writer.flush();
			writer.close();
			
			int responseCode = connection.getResponseCode();
			System.out.println("\nSending 'POST' to ReserveFlights");
			System.out.println(("\nResponse Code : " + responseCode));

			if ((responseCode >= 200) && (responseCode <= 299)) {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				StringBuffer response = new StringBuffer();

				while ((line = in.readLine()) != null) {
					response.append(line);
				}
				in.close();

				System.out.println(response.toString());
				
				return true;
			} else {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				StringBuffer response = new StringBuffer();

				while ((line = in.readLine()) != null) {
					response.append(line);
				}
				in.close();

				System.out.println(response.toString());
				return false;
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	protected void finalize() throws Throwable {
		this.cacheManager.close();
	}
	
	/**
	 * Clears the flight cache
	 * 
	 */
	public void clearFlightCache(){
		this.flightCache.clear();
	}
	
	/**
	 * Reset the database to its original state
	 * 
	 * @param team identifies the team making the request
	 */
	public boolean resetDB(String team){
		URL url;
		HttpURLConnection connection;
		/**
		 * Create an HTTP connection to the server for a GET 
		 */
		try {
			url = new URL(mUrlBase + QueryFactory.reset(team));
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", team);
			/**
			 * If response code of SUCCESS read the XML string returned
			 * 
			 */
			int responseCode = connection.getResponseCode();
			if ((responseCode >= 200) && (responseCode <= 299))
				return true;
			else
				return false;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
	}
}
