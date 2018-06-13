package com.flashypenguinz.ZombieSurvivalServer.events;

import java.util.HashMap;
import java.util.Map;

public class EventHandler implements DynamicRouter<Event> {

	private Map<Class<? extends Event>, Listener> listeners;

	  public EventHandler() {
		  listeners = new HashMap<Class<? extends Event>, Listener>();
	  }

	  @Override
	  public void registerListener(Class<? extends Event> contentType,
	      Channel<? extends Event> channel) {
	    listeners.put(contentType, (Listener) channel);
	  }

	  @Override
	  public void activate(Event content) {
	    listeners.get(content.getClass()).activate(content);
	  }
	
}
