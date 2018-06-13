package com.flashypenguinz.ZombieSurvivalServer;

import java.util.Scanner;

public class ZombieSurvivalServer {

	public static boolean running = true;
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		Thread scannerThread = new Thread(() -> {
			while(!scanner.nextLine().equalsIgnoreCase("exit")) {}
			scanner.close();
			running = false;;
		});
		scannerThread.start();		
		
		GameManager gm = new GameManager();
		
		while(running) {
			gm.update();
		}
		return;
	}
	
}
