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
	

}
