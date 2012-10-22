package com.karanam.marta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.json.*;

public class Schedule {
	private String day;
	private ArrayList<Calendar> times;
	
	public Schedule (String day, Calendar[] times) {
		this.day = day;
		this.times = new ArrayList<Calendar>(Arrays.asList(times));
	}
	
	public Schedule (String day, JSONArray array) throws JSONException {
		times = new ArrayList<Calendar>();
		boolean pm = false;
		this.day = day;
		for (int i = 0; i < array.length(); i++) {
			Calendar c = CalendarUtils.getTimeFromString(array.getString(i));
			pm = c.get(Calendar.HOUR_OF_DAY) == 12;
			
			if (pm && c.get(Calendar.HOUR_OF_DAY) == 0)
			{
				c.add(Calendar.DAY_OF_MONTH, 1);
				pm = false;
			}
			
			times.add(i, c);
		}
	}
	
	public Schedule (String day, ArrayList<Calendar> times) {
		this.day = day;
		this.times = times;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public ArrayList<Calendar> getTimes() {
		return times;
	}

	public void setTimes(ArrayList<Calendar> times) {
		this.times = times;
	}
}
