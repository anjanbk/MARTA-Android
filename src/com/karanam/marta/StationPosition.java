package com.karanam.marta;

/**
 * Represents a position as a percentage of the map's dimensions
 * @author AKaranam
 *
 */
public class StationPosition {
	private float x, y;

	public StationPosition(float x, float y) {
			super();
			this.x = x;
			this.y = y;
		}
	
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	
	
	
}
