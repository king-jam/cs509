/**
 * 
 */
package client.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.google.maps.*;
import com.google.maps.model.LatLng;
/**
 * This class provides a hook to the Google Timezone API to 
 * convert time string from reservations to local time of the airports.
 *   
 * @author James
 * @version 1
 * @since 2016-07-05
 *
 */
public class TimeLocal {

	protected GeoApiContext context;

	public TimeLocal(String apiKey) {
		this.context = new GeoApiContext().setApiKey(apiKey)
				.setConnectTimeout(1, TimeUnit.SECONDS)
				.setReadTimeout(1, TimeUnit.SECONDS)
				.setWriteTimeout(1, TimeUnit.SECONDS);
	};

	public String getLocalTimeLatLng(String gmtTime, double latitude, double longitude) throws Exception {
		TimeZone tz = null;
		String localTime = "";
		LatLng location = new LatLng(latitude,longitude);
		DateTimeFormatter flightDateFormat = DateTimeFormatter.ofPattern("yyyy MMM d HH:mm z");
		LocalDateTime timeLocal = LocalDateTime.parse(gmtTime,flightDateFormat);
		ZonedDateTime timeZoned = timeLocal.atZone(ZoneId.of("GMT"));
		PendingResult<TimeZone> result = TimeZoneApi.getTimeZone(this.context, location);
		try {
			tz = result.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.awaitIgnoreError();
		if (tz != null) {
			ZonedDateTime newTimeZoned = timeZoned.withZoneSameInstant(tz.toZoneId());
			localTime = newTimeZoned.format(flightDateFormat);
		}
		return localTime;
	}

	public TimeZone getTimeZone(double latitude, double longitude) throws Exception {
		TimeZone tz = null;
		LatLng location = new LatLng(latitude,longitude);
		PendingResult<TimeZone> result = TimeZoneApi.getTimeZone(this.context, location);
		try {
			tz = result.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tz;
	}

	public String getLocalTime(String gmtTime, TimeZone tz) throws Exception {
		String localTime = "";
		DateTimeFormatter flightDateFormat = DateTimeFormatter.ofPattern("yyyy MMM d HH:mm z");
		LocalDateTime timeLocal = LocalDateTime.parse(gmtTime,flightDateFormat);
		ZonedDateTime timeZoned = timeLocal.atZone(ZoneId.of("GMT"));
		ZonedDateTime newTimeZoned = timeZoned.withZoneSameInstant(tz.toZoneId());
		localTime = newTimeZoned.format(flightDateFormat);
		return localTime;
	}
}
