package com.flashypenguinz.ZombieSurvivalServer;

import java.util.ArrayList;
import java.util.List;

import com.flashypenguinz.ZombieSurvival.net.entities.NetEntity;
import com.flashypenguinz.ZombieSurvival.net.entities.NetPlayer;

public class EntityManager {

	private List<NetEntity> entities = new ArrayList<NetEntity>();

	public void addEntity(NetEntity entity) {
		entities.add(entity);
	}
	
	public NetEntity getEntityById(int id) {
		for (NetEntity entity : entities) {
			if (entity.getId() == id) {
				return entity;
			}
		}
		return null;
	}
	
	public void removeEntityById(int id) {
		NetEntity toBeRemoved = null;
		for (NetEntity entity : entities)
			if (entity.getId() == id) {
				toBeRemoved = entity;
				break;
			}
		if(toBeRemoved != null)
			entities.remove(toBeRemoved);
	}
	
	public NetPlayer getPlayerById(int id) {
		for (NetPlayer player : getPlayers())
				if(player.getId() == id)
					return player;
		return null;
	}
	
	public List<NetEntity> getEntities() {
		return entities;
	}
	
	public List<NetPlayer> getPlayers() {
		List<NetPlayer> players = new ArrayList<NetPlayer>();
		for(NetEntity entity: entities)
			if(entity instanceof NetPlayer)
				players.add((NetPlayer) entity);
		return players;
	}
	
}
