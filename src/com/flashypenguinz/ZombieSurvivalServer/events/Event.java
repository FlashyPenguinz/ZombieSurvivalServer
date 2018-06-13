package com.flashypenguinz.ZombieSurvivalServer.events;

public class Event implements Message {

	@Override
	public Class<? extends Event> getType() {
		return getClass();
	}
	
}
