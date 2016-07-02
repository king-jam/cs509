package client.util;

public final class Configuration {
	private static Configuration instance; 
	
	private Configuration(){};
	
	public static Configuration getInstance(){
		if(instance==null)
			instance=new Configuration();
		return instance;
	}
	
	public static String TICKET_AGENCY="Team01";
	public static final int MAX_LAYOVER=2;
	public static final long MAX_LAYOVER_TIME=2*3600*1000;//in milli seconds
	public static final long MIN_LAYOVER_TIME=1800*1000;//in milli seconds
	public static final int DAY_HOUR_NEXT_FLIGHT=21;//in 24 hr format,9:00pm
	
	public static String getAgency() {
		return TICKET_AGENCY;
	}
	
	public static int getMaxLayover() {
		return MAX_LAYOVER;
	}

	public static long getMaxLayoverTime() {
		return MAX_LAYOVER_TIME;
	}
	
	public static long getMinLayoverTime() {
		return MIN_LAYOVER_TIME;
	}
	
	public static int getDayHourNextFlight() {
		return DAY_HOUR_NEXT_FLIGHT;
	}
}
