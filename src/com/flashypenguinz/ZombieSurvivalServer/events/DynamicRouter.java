package com.flashypenguinz.ZombieSurvivalServer.events;

public interface DynamicRouter<E extends Event> {
	
	  public void registerListener(Class<? extends E> contentType,
	      Channel<? extends E> channel);
	  
	  public abstract void activate(E content);
}