package com.karanam.marta;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CalendarUtils {
	public static long difference(Calendar c1, Calendar c2, int unit) {

		differenceCheckUnit(unit);

		Map<Integer, Long> unitEstimates = differenceGetUnitEstimates();

		Calendar first = (Calendar) c1.clone();
		Calendar last = (Calendar) c2.clone();

		long difference = c2.getTimeInMillis() - c1.getTimeInMillis();

		long unitEstimate = unitEstimates.get(unit).longValue();
		long increment = (long) Math.floor((double) difference / (double) unitEstimate);
		increment = Math.max(increment, 1);

		long total = 0;

		while (increment > 0) {
			add(first, unit, increment);
			if (first.after(last)) {
				add(first, unit, increment * -1);
				increment = (long) Math.floor(increment / 2);
			} else {
				total += increment;
			}
		}

		return total;

	}

	private static Map<Integer, Long> differenceGetUnitEstimates() {
		Map<Integer, Long> unitEstimates = new HashMap<Integer, Long>();
		unitEstimates.put(Calendar.YEAR, 1000l * 60 * 60 * 24 * 365);
		unitEstimates.put(Calendar.MONTH, 1000l * 60 * 60 * 24 * 30);
		unitEstimates.put(Calendar.DAY_OF_MONTH, 1000l * 60 * 60 * 24);
		unitEstimates.put(Calendar.HOUR_OF_DAY, 1000l * 60 * 60);
		unitEstimates.put(Calendar.MINUTE, 1000l * 60);
		unitEstimates.put(Calendar.SECOND, 1000l);
		unitEstimates.put(Calendar.MILLISECOND, 1l);
		return unitEstimates;
	}

	private static void differenceCheckUnit(int unit) {
		List<Integer> validUnits = new ArrayList<Integer>();
		validUnits.add(Calendar.YEAR);
		validUnits.add(Calendar.MONTH);
		validUnits.add(Calendar.DAY_OF_MONTH);
		validUnits.add(Calendar.HOUR_OF_DAY);
		validUnits.add(Calendar.MINUTE);
		validUnits.add(Calendar.SECOND);
		validUnits.add(Calendar.MILLISECOND);

		if (!validUnits.contains(unit)) {
			throw new RuntimeException(
					"CalendarUtils.difference one of these units" + 
					"Calendar.YEAR," +
					"Calendar.MONTH," +
					"Calendar.DAY_OF_MONTH," +
					"Calendar.HOUR_OF_DAY," +
					"Calendar.MINUTE," +
					"Calendar.SECOND," +
					"Calendar.MILLISECOND."
					);
		}
	}

	public static void add(Calendar c, int unit, long increment) {
		while (increment > Integer.MAX_VALUE) {
			c.add(unit, Integer.MAX_VALUE);
			increment -= Integer.MAX_VALUE;
		}
		c.add(unit, (int) increment);
	}
	
	/**
	 * Parses the string representation of the time and returns a Java Calendar object.
	 * @param timeStr
	 * @return
	 */
	public static Calendar getTimeFromString(String timeStr) {
		DateFormat format = new SimpleDateFormat("hh:mm");
		Calendar c = new GregorianCalendar();
		
		Date date;
		try {
			date = (Date)format.parse(timeStr.substring(0, timeStr.length()-1));
			c.setTime(date);
			
			if (timeStr.charAt(timeStr.length()-1) == 'p')
				c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR) + 12);
			else
				c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR));
			
			Calendar now = Calendar.getInstance();
			
			c.set(Calendar.DATE, now.get(Calendar.DATE));
			c.set(Calendar.MONTH, now.get(Calendar.MONTH));
			c.set(Calendar.YEAR, now.get(Calendar.YEAR));
			
			return c;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static String getStringFromTime(Calendar time) {
		StringBuilder timeStr = new StringBuilder();
			
		timeStr.append(time.get(Calendar.HOUR))
				.append(":")
				.append(time.get(Calendar.MINUTE))
				.append(" ")
				.append( time.get(Calendar.HOUR_OF_DAY) >= 12 ? "PM" : "AM");
		
		return timeStr.toString();
	}
	
	public static boolean isWeekend() {
		Calendar now = Calendar.getInstance();
		return (now.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (now.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
	}
	
	public static Calendar getNextTrain(String direction) {
		Calendar nextTrain = null;
		
		for (Line l : StationManager.station.getLines()) {
			// First check if the line goes in the specified direction
			Direction d = l.getDirection1();
			
			if (d.getName().equals(direction)) {
				// Load appropriate schedule
				Schedule currSchedule = isWeekend() ? d.getWeekend() : d.getWeekday();
				int x = 0;
				nextTrain = currSchedule.getTimes().get(x++); 
				
				while (nextTrain.before(Calendar.getInstance()) && x < currSchedule.getTimes().size()) {
					nextTrain = currSchedule.getTimes().get(x);
					x++;
				}
			}
		}
		
		return nextTrain;
	}
}
