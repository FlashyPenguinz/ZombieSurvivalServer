package com.mudkiper202.ZombieSurvivalServer;

public class ZombieSurvivalServer {

	public static void main(String[] args) {
		Server server = new Server(8192);
		server.start();
	}
	
}
