package com.karanam.marta;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This class serves as the controller between the UI front-end and the JSON parser
 * @author AKaranam
 *
 */
public class StationManager {
	public static final String DIRECTION_NORTHBOUND = "Northbound";
	public static final String DIRECTION_SOUTHBOUND = "Southbound";
	public static final String DIRECTION_EASTBOUND = "Eastbound";
	public static final String DIRECTION_WESTBOUND = "Westbound";
	
	// This public Station object represents the current station selected
	// All clients have access to it at any given time
	public static Station station;
}
