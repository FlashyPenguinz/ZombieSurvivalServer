package com.flashypenguinz.ZombieSurvivalServer;

import java.net.InetAddress;

import com.flashypenguinz.ZombieSurvival.net.entities.NetBullet;
import com.flashypenguinz.ZombieSurvival.net.entities.NetEntity;
import com.flashypenguinz.ZombieSurvival.net.entities.NetPlayer;
import com.flashypenguinz.ZombieSurvival.net.packets.Packet00Login;
import com.flashypenguinz.ZombieSurvival.net.packets.Packet01Disconnect;
import com.flashypenguinz.ZombieSurvival.net.packets.Packet02PlayerChange;
import com.flashypenguinz.ZombieSurvival.net.packets.Packet03EntityMove;
import com.flashypenguinz.ZombieSurvival.net.packets.Packet04BulletChange;

/**
 *	Handles receives and gives to the server
 */
public class GameHandler {

	private GameManager gm;
	
	public GameHandler(GameManager gm) {
		this.gm = gm;
	}
	
	public void handlePlayerLogin(InetAddress ipAddress, int port, Packet00Login packet) {
		System.out.println("[" + ipAddress.getHostAddress() + ":" + port
				+ "] " + packet.getUsername() + " has connected...");
		NetClient playerClient = new NetClient(packet.getUsername(),
				ipAddress, port);
		gm.getConnectedPlayers().add(playerClient);
		for (NetPlayer player : gm.getEntityManager().getPlayers()) {
			Packet02PlayerChange changePacket = new Packet02PlayerChange(0,
					player.getId(), player.getUsername(), player.getX(),
					player.getY(), player.getTextureName(),
					(int) player.getTextureCoords().x,
					(int) player.getTextureCoords().y);
			gm.getServer().sendData(changePacket.getData(), ipAddress, port);
		}
		gm.getServer().sendDataToRestClients(packet.getData(), ipAddress, port);
	}
	
	public void handlePlayerDisconnet(InetAddress ipAddress, int port, Packet01Disconnect packet) {
		NetClient client = getClientByIpAndPort(ipAddress, port);
		System.out.println("[" + ipAddress.getHostAddress() + ":" + port
				+ "] " + client.getClientName() + " has disconnected...");
		NetPlayer player = gm.getEntityManager().getPlayerById(client.getPlayerId());
		gm.getConnectedPlayers().remove(client);
		gm.getEntityManager().removeEntityById(player.getId());
		Packet02PlayerChange playerChangePacket = new Packet02PlayerChange(
				1, player.getId(), "", 0, 0, "", 0, 0);
		gm.getServer().sendDataToRestClients(playerChangePacket.getData(), ipAddress, port);
	}
	
	public void handlePlayerChange(InetAddress ipAddress, int port, Packet02PlayerChange packet) {
		if (packet.getType() == 0) {
			System.out.println("Player joined with username: "+packet.getUsername()+" and Id: "+packet.getId());
			NetPlayer player = new NetPlayer(packet.getId(),
					packet.getUsername(), packet.getX(), packet.getY(),
					packet.getTextureName(), packet.getTexX(),
					packet.getTexY());
			gm.getEntityManager().addEntity(player);
			getClientByIpAndPort(ipAddress, port).setPlayerId(
					player.getId());
			gm.getServer().sendDataToRestClients(packet.getData(), ipAddress, port);
		}
	}
	
	public void handleEntityMove(InetAddress ipAddress, int port, Packet03EntityMove packet) {
		NetEntity movePlayer = gm.getEntityManager().getEntityById(packet.getId());
		if (movePlayer != null) {
			movePlayer.setPosition(packet.getX(), packet.getY());
			movePlayer.setRotation(packet.getRotation());
			gm.getServer().sendDataToRestClients(packet.getData(), ipAddress, port);
		}
	}
	
	public void handleBulletChange(InetAddress ipAddress, int port, Packet04BulletChange packet) {
		if (packet.getType() == 0) {
			gm.getEntityManager().addEntity(new NetBullet(packet.getId(), packet.getPlayerId(), packet.getX(), packet.getY(), packet.getRotation()));
			gm.getServer().sendDataToRestClients(packet.getData(), ipAddress, port);
		}
	}
	
	public NetClient getClientByIpAndPort(InetAddress address, int port) {
		for (NetClient p : gm.getConnectedPlayers()) {
			if (p.getAddress().equals(address) && p.getPort() == port)
				return p;
		}
		return null;
	}
	
	public GameManager getGameManager() {
		return gm;
	}
	
}
