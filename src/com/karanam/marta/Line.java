package com.karanam.marta;

/**
 * Represents a subway line.
 * A line has a name, and exactly two directions, outbound and inbound.
 * @author AKaranam
 *
 */
public class Line {
	private String name;
	private Direction direction1, direction2;
	
	public Line(String name, Direction direction1, Direction direction2) {
		this.name = name;
		this.direction1 = direction1;
		this.direction2 = direction2;
	}
	
	public String getName()
	{
		return name;
	}

	public Direction getDirection1() {
		return direction1;
	}

	public void setDirection1(Direction direction1) {
		this.direction1 = direction1;
	}

	public Direction getDirection2() {
		return direction2;
	}

	public void setDirection2(Direction direction2) {
		this.direction2 = direction2;
	}
	
	
}
