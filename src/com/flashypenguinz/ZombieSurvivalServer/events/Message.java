package com.flashypenguinz.ZombieSurvivalServer.events;

public interface Message {

	public Class<? extends Event> getType();
	
}
