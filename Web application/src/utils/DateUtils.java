package utils;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {
	public static final DateTimeFormatter	SIMPLE_DATE		= DateTimeFormat.forPattern("dd/MM/yyyy");
	public static final DateTimeFormatter	DATE_AND_TIME	= DateTimeFormat.forPattern("dd/MM/yyyy hh:mm");
}
