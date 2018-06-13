package com.flashypenguinz.ZombieSurvivalServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.flashypenguinz.ZombieSurvival.net.packets.Packet;
import com.flashypenguinz.ZombieSurvival.net.packets.Packet00Login;
import com.flashypenguinz.ZombieSurvival.net.packets.Packet01Disconnect;
import com.flashypenguinz.ZombieSurvival.net.packets.Packet02PlayerChange;
import com.flashypenguinz.ZombieSurvival.net.packets.Packet03EntityMove;
import com.flashypenguinz.ZombieSurvival.net.packets.Packet04BulletChange;
import com.flashypenguinz.ZombieSurvival.net.packets.PacketType;

public class Server extends Thread {

	private DatagramSocket socket;

	private GameHandler gameHandler;
	
	public Server(int port, GameHandler gameHandler) {
		try {
			this.socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.gameHandler = gameHandler;
	}

	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			parsePacket(data, packet.getAddress(), packet.getPort());
		}
	}

	private void parsePacket(byte[] data, InetAddress ipAddress, int port) {
		String message = new String(data).trim();
		PacketType type = Packet.lookupPacket(message.substring(0, 2));
		if (type == PacketType.LOGIN) {
			gameHandler.handlePlayerLogin(ipAddress, port, new Packet00Login(data));
		} else if (type == PacketType.DISCONNECT) {
			gameHandler.handlePlayerDisconnet(ipAddress, port, new Packet01Disconnect());
		} else if (type == PacketType.PLAYER_CHANGE) {
			gameHandler.handlePlayerChange(ipAddress, port, new Packet02PlayerChange(data));
		} else if (type == PacketType.ENTITY_MOVE) {
			gameHandler.handleEntityMove(ipAddress, port, new Packet03EntityMove(data));
		} else if (type == PacketType.BULLET_CHANGE) {
			gameHandler.handleBulletChange(ipAddress, port, new Packet04BulletChange(data));
		}
	}

	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length,
				ipAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendDataToAllClients(byte[] data) {
		for (NetClient p : gameHandler.getGameManager().getConnectedPlayers()) {
			sendData(data, p.getAddress(), p.getPort());
		}
	}

	public void sendDataToRestClients(byte[] data, InetAddress address, int port) {
		for (NetClient p : gameHandler.getGameManager().getConnectedPlayers()) {
			if (!(p.getAddress().equals(address) && p.getPort() == port)) {
				sendData(data, p.getAddress(), p.getPort());
			}
		}
	}

}
