package com.flashypenguinz.ZombieSurvivalServer.events.listeners;

import com.flashypenguinz.ZombieSurvivalServer.events.Event;
import com.flashypenguinz.ZombieSurvivalServer.events.Listener;
import com.flashypenguinz.ZombieSurvivalServer.events.types.PlayerJoinEvent;

public class PlayerJoinListener extends Listener {

	@Override
	public void activate(Event event) {
		PlayerJoinEvent e = (PlayerJoinEvent) event;
		System.out.println(e.getUsername());
	}
	
}
