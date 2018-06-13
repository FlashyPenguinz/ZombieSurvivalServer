package com.flashypenguinz.ZombieSurvivalServer.events.types;

import com.flashypenguinz.ZombieSurvivalServer.events.Event;

public class PlayerJoinEvent extends Event {

	private int id;
	private String username;
	private float x, y;
	private String textureName;
	private int texX, texY;
	
	public PlayerJoinEvent(int id, String username, float x, float y, String textureName, int texX, int texY) {
		this.id = id;
		this.username = username;
		this.x = x;
		this.y = y;
		this.textureName = textureName;
		this.texX = texX;
		this.texY = texY;
	}
	
	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public String getTextureName() {
		return textureName;
	}

	public int getTexX() {
		return texX;
	}

	public int getTexY() {
		return texY;
	}
}
