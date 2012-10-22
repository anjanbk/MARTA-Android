package com.karanam.marta;

import java.util.HashMap;
import java.util.ArrayList;

public class SystemData {
	public static final ArrayList<StationFile> stationArray;
	static {
		ArrayList<StationFile> aList = new ArrayList<StationFile>();
		aList.add(new StationFile("North Springs", "ns.json"));
		aList.add(new StationFile("Sandy Springs", "ss.json"));
		aList.add(new StationFile("Dunwoody", "dunwoody.json"));
		aList.add(new StationFile("Medical Center", "mc.json"));
		aList.add(new StationFile("Buckhead", "buckhead.json"));
		aList.add(new StationFile("Lindbergh Center", "lindbergh.json"));
		aList.add(new StationFile("Lenox", "lenox.json"));
		aList.add(new StationFile("Brookhaven/Oglethorpe", "brookhaven.json"));
		aList.add(new StationFile("Chamblee", "chamblee.json"));
		aList.add(new StationFile("Doraville", "doraville.json"));
		aList.add(new StationFile("Arts Center", "artscenter.json"));
		aList.add(new StationFile("Midtown", "midtown.json"));
		aList.add(new StationFile("North Avenue", "na.json"));
		aList.add(new StationFile("Civic Center", "civic.json"));
		aList.add(new StationFile("Peachtree Center", "peachtree.json"));
		aList.add(new StationFile("Five Points", "fivepoints.json"));
		aList.add(new StationFile("Garnett", "garnett.json"));
		aList.add(new StationFile("West End", "westend.json"));
		aList.add(new StationFile("Oakland City", "oaklandcity.json"));
		aList.add(new StationFile("Lakewood/Ft. McPherson", "lakewood.json"));
		aList.add(new StationFile("East Point", "eastpoint.json"));
		aList.add(new StationFile("College Park", "collegepark.json"));
		aList.add(new StationFile("Airport", "airport.json"));
		aList.add(new StationFile("Hamilton E. Homes", "hamilton.json"));
		aList.add(new StationFile("West Lake", "westlake.json"));
		aList.add(new StationFile("Ashby", "ashby.json"));
		aList.add(new StationFile("Bankhead", "bankhead.json"));
		aList.add(new StationFile("Vine City", "vinecity.json"));
		aList.add(new StationFile("Georgia Dome", "georgiadome.json"));
		aList.add(new StationFile("Georgia State", "georgiastate.json"));
		aList.add(new StationFile("King Memorial", "kingmemorial.json"));
		aList.add(new StationFile("Inman Park", "inman.json"));
		aList.add(new StationFile("Edgewood", "edgewood.json"));
		aList.add(new StationFile("East Lake", "eastlake.json"));
		aList.add(new StationFile("Decatur", "decatur.json"));
		aList.add(new StationFile("Avondale", "avondale.json"));
		aList.add(new StationFile("Kensington", "kensington.json"));
		aList.add(new StationFile("Indian Creek", "indiancreek.json"));
		stationArray = new ArrayList<StationFile>(aList);
	}
	
	public static  final HashMap<Integer, Integer> stationHashMap;
	static {
		HashMap<Integer, Integer> aMap = new HashMap<Integer, Integer>();
		aMap.put(1, 2);
		aMap.put(2, 3);
		aMap.put(3, 4);
		aMap.put(4, 5);
		aMap.put(5, 6);
		aMap.put(6, 7);
		aMap.put(7, 8);
		aMap.put(8, 9);
		aMap.put(288, 10); // Arts Center
		aMap.put(308, 11); // Midtown
		aMap.put(11, 12);
		aMap.put(12, 13);
		aMap.put(13, 14);
		aMap.put(14, 15);
		aMap.put(15, 16);
		aMap.put(16, 17);
		aMap.put(17, 18);
		aMap.put(18, 19);
		aMap.put(19, 20);
		aMap.put(20, 21);
		aMap.put(21, 22);
		aMap.put(22, 23);
		aMap.put(23, 24);
		aMap.put(24, 25);
		aMap.put(25, 26);
		aMap.put(26, 27);
		aMap.put(27, 28);
		aMap.put(28, 29);
		aMap.put(29, 30);
		aMap.put(30, 31);
		aMap.put(31, 32);
		aMap.put(32, 33);
		aMap.put(33, 34);
		aMap.put(34, 35);
		aMap.put(35, 36);
		aMap.put(36, 37);
		aMap.put(37, 38);
		aMap.put(38, 39);
		stationHashMap = new HashMap<Integer, Integer>(aMap);
	}
	
	public static class StationFile {
		public String name;
		public String resName;
		
		public StationFile(String name, String resName) {
			this.name = name;
			this.resName = resName;
		}
	}
}
