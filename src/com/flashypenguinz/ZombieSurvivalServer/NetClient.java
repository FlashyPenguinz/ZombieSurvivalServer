package com.flashypenguinz.ZombieSurvivalServer;

import java.net.InetAddress;

public class NetClient {

	private String clientName;
	private InetAddress address;
	private int port;
	
	private int playerId;
	
	public NetClient(String clientName, InetAddress address, int port) {
		this.clientName = clientName;
		this.address = address;
		this.port = port;
	}

	public String getClientName() {
		return clientName;
	}

	public InetAddress getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}
	
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	
	public int getPlayerId() {
		return playerId;
	}
	
}