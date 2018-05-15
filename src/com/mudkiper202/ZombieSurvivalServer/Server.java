package com.mudkiper202.ZombieSurvivalServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.mudkiper202.ZombieSurvival.net.entities.NetBullet;
import com.mudkiper202.ZombieSurvival.net.entities.NetEntity;
import com.mudkiper202.ZombieSurvival.net.entities.NetPlayer;
import com.mudkiper202.ZombieSurvival.net.packets.Packet;
import com.mudkiper202.ZombieSurvival.net.packets.Packet00Login;
import com.mudkiper202.ZombieSurvival.net.packets.Packet02PlayerChange;
import com.mudkiper202.ZombieSurvival.net.packets.Packet03EntityMove;
import com.mudkiper202.ZombieSurvival.net.packets.Packet04BulletChange;
import com.mudkiper202.ZombieSurvival.net.packets.Packet05BulletMove;
import com.mudkiper202.ZombieSurvival.net.packets.PacketType;

public class Server extends Thread {

	private DatagramSocket socket;
	private EntityManager em = new EntityManager();

	private List<NetClient> connectedPlayers = new ArrayList<NetClient>();

	public Server(int port) {
		try {
			this.socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
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
			Packet00Login loginPacket = new Packet00Login(data);
			System.out.println("[" + ipAddress.getHostAddress() + ":" + port
					+ "] " + loginPacket.getUsername() + " has connected...");
			NetClient playerClient = new NetClient(loginPacket.getUsername(),
					ipAddress, port);
			this.connectedPlayers.add(playerClient);
			for (NetPlayer player : em.getPlayers()) {
				Packet02PlayerChange changePacket = new Packet02PlayerChange(0,
						player.getId(), player.getUsername(), player.getX(),
						player.getY(), player.getTextureName(),
						(int) player.getTextureCoords().x,
						(int) player.getTextureCoords().y);
				sendData(changePacket.getData(), ipAddress, port);
			}
			sendDataToRestClients(data, ipAddress, port);
		} else if (type == PacketType.DISCONNECT) {
			NetClient client = getClientByIpAndPort(ipAddress, port);
			System.out.println("[" + ipAddress.getHostAddress() + ":" + port
					+ "] " + client.getClientName() + " has disconnected...");
			NetPlayer player = em.getPlayerById(client.getPlayerId());
			connectedPlayers.remove(client);
			em.removeEntityById(player.getId());
			Packet02PlayerChange playerChangePacket = new Packet02PlayerChange(
					1, player.getId(), "", 0, 0, "", 0, 0);
			sendDataToRestClients(playerChangePacket.getData(), ipAddress, port);
		} else if (type == PacketType.PLAYER_CHANGE) {
			Packet02PlayerChange packet = new Packet02PlayerChange(data);
			if (packet.getType() == 0) {
				System.out.println("Player joined with username: "+packet.getUsername()+" and Id: "+packet.getId());
				NetPlayer player = new NetPlayer(packet.getId(),
						packet.getUsername(), packet.getX(), packet.getY(),
						packet.getTextureName(), packet.getTexX(),
						packet.getTexY());
				em.addEntity(player);
				getClientByIpAndPort(ipAddress, port).setPlayerId(
						player.getId());
				sendDataToRestClients(data, ipAddress, port);
			}
		} else if (type == PacketType.ENTITY_MOVE) {
			Packet03EntityMove packet = new Packet03EntityMove(data);
			NetEntity movePlayer = em.getEntityById(packet.getId());
			if (movePlayer != null) {
				movePlayer.setPosition(packet.getX(), packet.getY());
				movePlayer.setRotation(packet.getRotation());
				sendDataToRestClients(data, ipAddress, port);
			}
		} else if (type == PacketType.BULLET_CHANGE) {
			Packet04BulletChange packet = new Packet04BulletChange(data);
			if (packet.getType() == 0) {
				em.addEntity(new NetBullet(packet.getId(), packet.getPlayerId(), packet.getX(), packet.getY(), packet.getRotation()));
				sendDataToRestClients(data, ipAddress, port);
			} else {
				em.removeEntityById(packet.getId());
				sendDataToRestClients(data, ipAddress, port);
			}
		} else if (type == PacketType.BULLET_MOVE) {
			Packet05BulletMove packet = new Packet05BulletMove(data);
			NetEntity moveBullet = em.getEntityById(packet.getId());
			if (moveBullet != null) {
				moveBullet.setPosition(packet.getX(), packet.getY());
				sendDataToRestClients(data, ipAddress, port);
			}
		}
	}

	public NetClient getClientByIpAndPort(InetAddress address, int port) {
		for (NetClient p : connectedPlayers) {
			if (p.getAddress().equals(address) && p.getPort() == port)
				return p;
		}
		System.out.println("Erorr");
		return null;
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
		for (NetClient p : connectedPlayers) {
			sendData(data, p.getAddress(), p.getPort());
		}
	}

	public void sendDataToRestClients(byte[] data, InetAddress address, int port) {
		for (NetClient p : connectedPlayers) {
			if (!(p.getAddress().equals(address) && p.getPort() == port)) {
				sendData(data, p.getAddress(), p.getPort());
			}
		}
	}

}
