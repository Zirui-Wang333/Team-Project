package GameMenu.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import GameMenu.MultiPlayerGame;
import GameMenu.MultiPlayerSettings;
import GameMenu.network.MultiPlayer;

public class GameServer extends Thread {

	DatagramSocket socket;
	MultiPlayerGame game;
	List<MultiPlayer> connectedPlayers = new ArrayList<MultiPlayer>();
	int port;
	int code;
	String username;
	int count1 = 0;
	int count2 = 0;
	boolean spawPlayer1Enemies = true;
	boolean spawPlayer2Enemies = true;
	boolean thread1IsRun = false;
	boolean thread2IsRun = false;
	boolean isRun = true;

	public GameServer(MultiPlayerGame game, int port) {
		this.game = game;
		this.port = port;

		try {
			this.socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is to receive the data
	 * 
	 * @return Nothing.
	 * 
	 * @author Zirui Wang
	 */
	@Override
	public void run() {

		while (isRun) {

//			if (thread1IsRun == false)
//				handleScene1();
			if (thread2IsRun == false)
				handleScene2And3();

			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);

			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}

			handleInput(packet.getData(), packet.getAddress(), packet.getPort());

		}
	}

	/**
	 * This method is to handle the received data. Received data formate: two number
	 * + username 00: login 01: move up 02: move down 03: move left 04: move right
	 * 05: shoot 06: set scene
	 * 
	 * @param data      the data that received as byte type
	 * @param ipAddress the ipAddress from client
	 * @param port      the port receive to the client
	 * 
	 * @author Zirui Wang
	 */
	private void handleInput(byte[] data, InetAddress ipAddress, int port) {

		/**
		
		 */

		String message = new String(data).trim();
		code = Integer.parseInt(message.substring(0, 2));
		username = message.substring(2);

		if (code == 00) {
			System.out.println("[" + ipAddress.getHostAddress() + " : " + port + "]" + username + " has login");
			MultiPlayer multiPlayer = new MultiPlayer(ipAddress, port, username);
			connectedPlayers.add(multiPlayer);
		} else {
			sendDataToAll(data);
		}

	}

	/**
	 * This method is send data to all client in order to synchronize
	 * 
	 * @param data the data to send
	 * 
	 * @author Zirui Wang
	 */
	public void sendDataToAll(byte[] data) {
		for (MultiPlayer p : connectedPlayers) {
			sendData(data, p.getIPAddress(), p.getPort());
		}
	}

	/**
	 * This method is send data to a client in order to synchronize
	 * 
	 * @param data the data to send
	 * 
	 * @author Zirui Wang
	 */
	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		if (!socket.isClosed()) {
			try {
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void handleScene1() {
		thread1IsRun = true;
		new Thread(new Runnable() {

			@Override
			public void run() {

				while (true) {
					if (connectedPlayers.size() == 2) {
						if (count1 == 0) {
							count1 = 1;
							try {
								sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						if (game.player1Enemies.isEmpty()) {
							spawPlayer1Enemies = true;
						}
						if (game.player2Enemies.isEmpty()) {
							spawPlayer2Enemies = true;
						}
						if (spawPlayer1Enemies) {
							spawPlayer1Enemies = false;
							sendDataToAll(("06player1scene1").getBytes());
						}

						if (spawPlayer2Enemies) {
							spawPlayer2Enemies = false;
							sendDataToAll(("06player2scene1").getBytes());
						}
					}
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();
	}

	/**
	 * This method is to handle scene2 and scene3
	 * 
	 * @return nothing
	 * 
	 * @author Zirui Wang
	 */
	public void handleScene2And3() {
		thread2IsRun = true;
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					if (connectedPlayers.size() == 2) {
						if (count2 == 0) {
							count2 = 1;
							sendDataToAll("start".getBytes());
							try {
								sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						Random random = new Random();
						int x = random.nextInt((int) MultiPlayerSettings.SCENE_WIDTH - 50) + 20;
						int y = -1;
						int dx = random.nextInt(4) + 1;
						int health = random.nextInt(19) + 1;
						int playerNumber = random.nextInt(2);
						int powerupPosition;
						int isPowerup = random.nextInt(100);
						int property;
						int lifeIncrease = random.nextInt(2);

						if (isPowerup > 90) {
							powerupPosition = random.nextInt((int) MultiPlayerSettings.SCENE_WIDTH - 50) + 20;
							property = random.nextInt(4);
							if (property >= 2) {
								if (lifeIncrease == 0) {
									property = -1;
									powerupPosition = -1;
								}
							}
						} else {
							powerupPosition = -1;
							property = -1;
						}
						if (playerNumber == 0) {
							if (game.score1 > 150) {
								dx = (int) (dx + 0.02 * game.score1);
								health = (int) (health + 4 + 0.03 * game.score1);
							}
							sendDataToAll(("06player1scene2" + x + "+" + y + "/" + dx + "=" + health + "@"
									+ powerupPosition + "&" + property).getBytes());
						}
						if (playerNumber == 1) {
							if (game.score2 > 150) {
								dx = (int) (dx + 0.02 * game.score2);
								health = (int) (health + 4 + 0.03 * game.score2);
							}
							sendDataToAll(("06player2scene2" + x + "+" + y + "/" + dx + "=" + health + "@"
									+ powerupPosition + "&" + property).getBytes());
						}

					}

					try {
						sleep((long) (500 - (game.score1 + game.score2) * 0.02));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public void closeSocketServer() {
		if (!socket.isClosed()) {
			socket.close();
		}
	}
}
