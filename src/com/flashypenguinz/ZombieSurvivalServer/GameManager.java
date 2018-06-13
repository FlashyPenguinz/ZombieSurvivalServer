package com.flashypenguinz.ZombieSurvivalServer;

import java.util.ArrayList;
import java.util.List;

import com.flashypenguinz.ZombieSurvival.map.Map;

public class GameManager {

	private Server server;
	private List<NetClient> connectedPlayers = new ArrayList<NetClient>();
	private GameHandler gameHandler;
	
	private EntityManager em;
	private Map map;
	
	public GameManager() {
		this.gameHandler = new GameHandler(this);
		this.server = new Server(8192, gameHandler);
		this.server.start();
		this.em = new EntityManager();
		this.map = new Map(Map.loadMapFile("data/map.txt"));
	}
	
	public void update() {
		
	}
	
	public Server getServer() {
		return server;
	}
	
	public List<NetClient> getConnectedPlayers() {
		return connectedPlayers;
	}
	
	public GameHandler getGameHandler() {
		return gameHandler;
	}
	
	public EntityManager getEntityManager() {
		return em;
	}
	
}
