package GameMenu.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;

import GameMenu.MultiPlayerGame;
import GameMenu.MultiPlayerSettings;
import MultiplayerEnemyScene.Scene2;
import MultiplayerEnemyScene.Scene3;
import GameMenu.sprite.Shot;
import javafx.application.Platform;
import javafx.scene.image.Image;

public class GameClient extends Thread {

	InetAddress ipaddress;
	DatagramSocket socket;
	MultiPlayerGame game;
	int code;
	String username;
	boolean isPlayer1;
	int port;
	Image playerBulletImage;
	boolean runLater1IsRun = false;
	boolean runLater2IsRun = false;
	boolean isRun = true;

	public GameClient(MultiPlayerGame game, String ipAddress, String username, boolean isPlayer1, int port) {
		this.game = game;
		this.username = username;
		this.isPlayer1 = isPlayer1;
		this.port = port;

		try {
			this.socket = new DatagramSocket();
			this.ipaddress = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method is receive packet
	 * 
	 * @return nothing
	 * 
	 * @author Zirui Wang
	 */
	@Override
	public void run() {

		while (isRun) {

			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);

			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					game.checkCollisions();
				}
			});

			handleData(packet.getData(), packet.getAddress(), packet.getPort());
		}
	}

	/**
	 * This method is to handle data that received. Data format: two number +
	 * username 00: login 01: move up 02: move down 03: move left 04: move right 05:
	 * shoot 06: set scene 07: score
	 * 
	 * @param data      the data that received as byte type
	 * @param ipAddress the ipAddress from server
	 * @param port      the port receive from the server
	 * 
	 * @author Zirui Wang
	 */
	private void handleData(byte[] data, InetAddress address, int port) {

		String message = new String(data).trim();

		if (message.equals("close")) {
			isRun = false;
			return;
		}

		if (message.equals("start")) {
			game.count4 = false;
			game.waitingJoinText.setVisible(false);
			game.start3.setVisible(true);
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			game.start3.setVisible(false);
			game.start2.setVisible(true);
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			game.start2.setVisible(false);
			game.start1.setVisible(true);
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			game.start1.setVisible(false);
			game.startText.setVisible(true);
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			game.startText.setVisible(false);
			game.start = true;
		} else {
			code = Integer.parseInt(message.substring(0, 2));

			if (code == 06) {

				String player = message.substring(2, 9);
				String scene = message.substring(9, 15);
				if (player.equals("player1")) {
					if (scene.equals("scene2")) {
						int index1 = message.indexOf("+");
						int index2 = message.indexOf("/");
						int index3 = message.indexOf("=");
						int index4 = message.indexOf("@");
						int index5 = message.indexOf("&");

						int x = Integer.parseInt(message.substring(15, index1));
						int y = Integer.parseInt(message.substring(index1 + 1, index2));
						int dx = Integer.parseInt(message.substring(index2 + 1, index3));
						int health = Integer.parseInt(message.substring(index3 + 1, index4));
						int powerupPosition = Integer.parseInt(message.substring(index4 + 1, index5));
						int property = Integer.parseInt(message.substring(index5 + 1));

						Scene2 scene2 = new Scene2(game, player, x, y, 0, dx, health);
						if (powerupPosition != -1) {
							Scene3 scene3 = new Scene3(game, player, powerupPosition, 0, 1, property);
						}
					}
				}
				if (player.equals("player2")) {
					if (scene.equals("scene2")) {
						int index1 = message.indexOf("+");
						int index2 = message.indexOf("/");
						int index3 = message.indexOf("=");
						int index4 = message.indexOf("@");
						int index5 = message.indexOf("&");

						int x = Integer.parseInt(message.substring(15, index1));
						int y = Integer.parseInt(message.substring(index1 + 1, index2));
						int dx = Integer.parseInt(message.substring(index2 + 1, index3));
						int health = Integer.parseInt(message.substring(index3 + 1, index4));
						int powerupPosition = Integer.parseInt(message.substring(index4 + 1, index5));
						int property = Integer.parseInt(message.substring(index5 + 1));

						Scene2 scene2 = new Scene2(game, player, x, y, 0, dx, health);
						if (powerupPosition != -1) {
							Scene3 scene3 = new Scene3(game, player, powerupPosition, 0, 1, property);
						}
					}
				}
			}

			String username;

			if (code == 07) {
				int index1 = message.indexOf("@");
				username = message.substring(2, index1);
			} else {
				username = message.substring(2);
			}

			if (!this.username.equals(username)) {
				if (isPlayer1) {
					// move up
					if (code == 01) {
						game.player2.setY(game.player2.getY() - MultiPlayerSettings.PLAYER_SHIP_SPEED);
					}
					// move down
					if (code == 02) {
						game.player2.setY(game.player2.getY() + MultiPlayerSettings.PLAYER_SHIP_SPEED);
					}
					// move left
					if (code == 03) {
						game.player2.setX(game.player2.getX() - MultiPlayerSettings.PLAYER_SHIP_SPEED);
					}
					// move right
					if (code == 04) {
						game.player2.setX(game.player2.getX() + MultiPlayerSettings.PLAYER_SHIP_SPEED);
					}
					// shoot
					if (code == 05) {
						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								game.isShoot = true;
								game.spawnPrimaryWeapon(game.player2);
							}
						});
					}
					if (code == 07) {
						game.score2 = Integer.parseInt(message.substring(message.indexOf("@") + 1));
					}
				} else {
					// move up
					if (code == 01) {
						game.player1.setY(game.player1.getY() - MultiPlayerSettings.PLAYER_SHIP_SPEED);
					}
					// move down
					if (code == 02) {
						game.player1.setY(game.player1.getY() + MultiPlayerSettings.PLAYER_SHIP_SPEED);
					}
					// move left
					if (code == 03) {
						game.player1.setX(game.player1.getX() - MultiPlayerSettings.PLAYER_SHIP_SPEED);
					}
					// move right
					if (code == 04) {
						game.player1.setX(game.player1.getX() + MultiPlayerSettings.PLAYER_SHIP_SPEED);
					}
					// shoot
					if (code == 05) {
						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								game.isShoot = true;
								game.spawnPrimaryWeapon(game.player1);
							}
						});
					}
					if (code == 07) {
						game.score1 = Integer.parseInt(message.substring(message.indexOf("@") + 1));
					}

				}
			}
//	}
		}
	}

	/**
	 * This method is send data to the server in order to synchronize
	 * 
	 * @param data the data to send
	 * 
	 * @author Zirui Wang
	 */
	public void sendData(String data) {
		byte[] toSend = data.getBytes();
		DatagramPacket packet = new DatagramPacket(toSend, toSend.length, ipaddress, port);

		if (!socket.isClosed()) {
			try {
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void closeSocketClient() {
		if (!socket.isClosed()) {
			socket.close();
		}
	}
}
