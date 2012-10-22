package com.karanam.marta;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a train station.
 * A train station has a name, a position on the map, and lines which pass through it.
 * @author AKaranam
 *
 */
public class Station  {
	private String name;
	
	@SerializedName("Position")
	private StationPosition position;
	
	private ArrayList<Line> lines;
	
	public Station() {
		lines = new ArrayList<Line>();
	}

	public ArrayList<Line> getLines() {
		return lines;
	}
	
	public void addLine(Line l) {
		lines.add(l);
	}

	public void setLines(ArrayList<Line> lines) {
		this.lines = lines;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StationPosition getPosition() {
		return position;
	}

	public void setPosition(StationPosition position) {
		this.position = position;
	}
	
	public Line getLineByName(String name) {
		for (Line l : lines) {
			if (l.getName().equals(name))
				return l;
		}
		return null;
	}
}
