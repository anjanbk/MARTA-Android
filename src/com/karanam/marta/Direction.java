package com.karanam.marta;

public class Direction {
	private String name;
	private Schedule weekday, weekend;
	
	public Direction(String name, Schedule weekday, Schedule weekend) {
		this.name = name;
		this.weekday = weekday;
		this.weekend = weekend;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Schedule getWeekday() {
		return weekday;
	}

	public void setWeekday(Schedule weekday) {
		this.weekday = weekday;
	}

	public Schedule getWeekend() {
		return weekend;
	}

	public void setWeekend(Schedule weekend) {
		this.weekend = weekend;
	}
	
	
	
}
