package com.karanam.marta;

import java.io.*;
import java.util.ArrayList;

import org.json.*;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.format.Time;


// Returns a station object from a JSON String
public class JSONParser {
	private Context context;
	
	public JSONParser(Context context) {
		this.context = context;
	}
	
	private String generateJsonStringFromFile(InputStream in) throws IOException {
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(isr);
		
		String line;
		StringBuilder json = new StringBuilder();
		while ((line = br.readLine()) != null)
			json.append(line);
		return json.toString();
			
	}
	
	public Station parse(String filename) throws IOException, JSONException {
		String json = generateJsonStringFromFile(context.getAssets().open(filename));
		
		JSONObject directionJson, jsonObj = new JSONObject(json);
		JSONArray array = jsonObj.getJSONArray("Position");
		JSONArray timeJsonArr;
		
		Schedule s1, s2;
		Direction d1, d2;
		
		Station station = new Station();
		station.setName(jsonObj.getString("Name"));
		station.setPosition(new StationPosition(Float.valueOf(array.get(0).toString()), Float.valueOf(array.get(1).toString())));
		
		if (jsonObj.has("Red")) {
			directionJson = jsonObj.getJSONObject("Red").getJSONObject("Northbound");
			timeJsonArr = directionJson.getJSONArray("Weekday");
			s1 = new Schedule("Weekday", timeJsonArr);
			timeJsonArr = jsonObj.getJSONObject("Red").getJSONObject("Northbound").getJSONArray("Weekend");
			s2 = new Schedule("Weekend", timeJsonArr);
			
			d1 = new Direction("Northbound", s1, s2);
			
			directionJson = jsonObj.getJSONObject("Red").getJSONObject("Southbound");
			timeJsonArr = directionJson.getJSONArray("Weekday");
			s1 = new Schedule("Weekday", timeJsonArr);
			timeJsonArr = directionJson.getJSONArray("Weekend");
			s2 = new Schedule("Weekend", timeJsonArr);
			
			d2 = new Direction("Southbound", s1, s2);
			
			station.addLine(new Line("Red", d1, d2));
		}
		
		if (jsonObj.has("Gold")) {
			directionJson = jsonObj.getJSONObject("Gold").getJSONObject("Northbound");
			timeJsonArr = directionJson.getJSONArray("Weekday");
			s1 = new Schedule("Weekday", timeJsonArr);
			timeJsonArr = directionJson.getJSONArray("Weekend");
			s2 = new Schedule("Weekend", timeJsonArr);
			
			d1 = new Direction("Northbound", s1, s2);
			
			directionJson = jsonObj.getJSONObject("Gold").getJSONObject("Southbound");
			timeJsonArr = directionJson.getJSONArray("Weekday");
			s1 = new Schedule("Weekday", timeJsonArr);
			timeJsonArr = directionJson.getJSONArray("Weekend");
			s2 = new Schedule("Weekend", timeJsonArr);
			
			d2 = new Direction("Southbound", s1, s2);
			
			station.addLine(new Line("Gold", d1, d2));
		}
		
		if (jsonObj.has("Green")) {
			directionJson = jsonObj.getJSONObject("Green").getJSONObject("Eastbound");
			timeJsonArr = directionJson.getJSONArray("Weekday");
			s1 = new Schedule("Weekday", timeJsonArr);
			timeJsonArr = directionJson.getJSONArray("Weekend");
			s2 = new Schedule("Weekend", timeJsonArr);
			
			d1 = new Direction("Eastbound", s1, s2);
			
			directionJson = jsonObj.getJSONObject("Green").getJSONObject("Westbound");
			timeJsonArr = directionJson.getJSONArray("Weekday");
			s1 = new Schedule("Weekday", timeJsonArr);
			timeJsonArr = directionJson.getJSONArray("Weekend");
			s2 = new Schedule("Weekend", timeJsonArr);
			
			d2 = new Direction("Westbound", s1, s2);
			
			station.addLine(new Line("Green", d1, d2));
		}
		
		if (jsonObj.has("Blue")) {
			directionJson = jsonObj.getJSONObject("Blue").getJSONObject("Eastbound");
			timeJsonArr = directionJson.getJSONArray("Weekday");
			s1 = new Schedule("Weekday", timeJsonArr);
			timeJsonArr = directionJson.getJSONArray("Weekend");
			s2 = new Schedule("Weekend", timeJsonArr);
			
			d1 = new Direction("Eastbound", s1, s2);
			
			directionJson = jsonObj.getJSONObject("Blue").getJSONObject("Westbound");
			timeJsonArr = directionJson.getJSONArray("Weekday");
			s1 = new Schedule("Weekday", timeJsonArr);
			timeJsonArr = directionJson.getJSONArray("Weekend");
			s2 = new Schedule("Weekend", timeJsonArr);
			
			d2 = new Direction("Westbound", s1, s2);
			
			station.addLine(new Line("Blue", d1, d2));
		}
		
		return station;
	}
}
