package com.flashypenguinz.ZombieSurvivalServer.events;

public interface Channel<E extends Event> {

	public void activate(E event);
	
}
