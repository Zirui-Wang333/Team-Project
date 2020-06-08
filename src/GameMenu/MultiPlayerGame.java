package GameMenu;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import GameMenu.sprite.Player;
import GameMenu.sprite.PowerUp;
import GameMenu.sprite.Alien;
import GameMenu.sprite.Input;
import GameMenu.sprite.Life;
import GameMenu.network.GameClient;
import GameMenu.network.GameServer;
import GameMenu.sprite.Shot;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;

public class MultiPlayerGame extends Parent {
	Group root = new Group();

	Scene scene;

	public Pane playfieldLayer = new Pane();
	Pane scoreLayer = new Pane();
	Pane gameOver = new Pane();

	Image playerImage;
	Image player1Background;
	Image player2Background;
	Image player1BulletImage;
	Image player2BulletImage;
	public Image enemyImage;
	public Image powerup;
	public Image life;

	Pane root1;
	Pane layer;

	List<Shot> player1BulletList = new ArrayList<>();
	List<Shot> player2BulletList = new ArrayList<>();
	public List<Alien> player1Enemies = new ArrayList<>();
	public List<Alien> player2Enemies = new ArrayList<>();
	public List<Shot> player1EnemyBulletList = new ArrayList<>();
	public List<Shot> player2EnemyBulletList = new ArrayList<>();
	public List<PowerUp> player1Powerups = new ArrayList<>();
	public List<PowerUp> player2Powerups = new ArrayList<>();
	public List<Life> player1LifeList = new ArrayList<>();
	public List<Life> player2LifeList = new ArrayList<>();

	public Player player1;
	public Player player2;

	int player1PowerScore = 1;
	int player2PowerScore = 1;
	int port;
	public int isHost;
	public int k = 0;
	public int l = 0;
	public int score1 = 0;
	public int score2 = 0;
	public int player1Life = 2;
	public int player2Life = 2;
	boolean player1Collision = false;
	boolean player2Collision = false;
	public boolean player1Dead = false;
	public boolean player2Dead = false;
	public boolean isShoot = false;
	public boolean start = false;
	public boolean count3 = false;
	public boolean count4 = true;

	Text collisionText = new Text();
	Text player1Score = new Text();
	Text player2Score = new Text();
	Text player1LifeText = new Text();
	Text player2LifeText = new Text();
	Text player1PowerupText = new Text();
	Text player2PowerupText = new Text();
	Text player1LifeIncrease = new Text();
	Text player2LifeIncrease = new Text();
	Text player1PowerdownText = new Text();
	Text player2PowerdownText = new Text();
	public Text waitingJoinText = new Text();
	public Text player1GameOver = new Text();
	public Text player2GameOver = new Text();
	public Text player1Win = new Text();
	public Text player2Win = new Text();
	public Text start3 = new Text();
	public Text start2 = new Text();
	public Text start1 = new Text();
	public Text startText = new Text();

	double scene_width = MultiPlayerSettings.SCENE_WIDTH * 2;
	double scene_Height = MultiPlayerSettings.SCENE_HEIGHT;

	String username;
	String ipAddress = "localhost";

	Button musicControl;
	MediaPlayer mediaPlayer;

	MultiPlayerLife life11;
	MultiPlayerLife life12;
	MultiPlayerLife life21;
	MultiPlayerLife life22;

	GameServer socketServer;
	GameClient socketClient;

	AnimationTimer gameLoop;

	/**
	 * This is the initial and start method to run the game. This method is called
	 * in Main.java file. First step to start multiplayer part.
	 * 
	 * @param SecondaryStage
	 * @param isHost         Determine if the room was created, and if so, run the
	 *                       server
	 * @param username       Player username
	 * @param port           The port to connect between server and client
	 * @param ipAddress      the IP Address to connect to server. If player is the
	 *                       host then it is "localhost". If player not the host
	 *                       then it should input the server IP address by player.
	 * @param mediaPlayer    Shortcut to control music.
	 * @return Nothing.
	 * 
	 * @author Zirui Wang
	 */
	public void initAndStart(Stage SecondaryStage, int isHost, String username, int port, String ipAddress,
			MediaPlayer mediaPlayer) {

		this.ipAddress = ipAddress;
		this.isHost = isHost;
		this.username = username;
		this.port = port;
		this.mediaPlayer = mediaPlayer;

		try {
			startMultiPlayerGame(SecondaryStage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method start handle multiplayer, include a gameloop, handle input,
	 * handle damage, send message to server.
	 * 
	 * @param SecondaryStage
	 * @return Nothing.
	 * 
	 * @author Zirui Wang
	 */
	public void startMultiPlayerGame(Stage SecondaryStage) throws IOException {

		// If choose create the room, then a server and a client is created. If choose
		// join the room, then the client is created.
		if (isHost == 0) {
			socketServer = new GameServer(this, port);
			socketServer.start();

			socketClient = new GameClient(this, ipAddress, username, true, port);
			socketClient.sendData("00" + username);
			socketClient.start();

			musicControl = new Button("Music: On");
			musicControl.setTranslateX(0);
			musicControl.setTranslateY(MultiPlayerSettings.SCENE_HEIGHT - 55);

		} else {
			socketClient = new GameClient(this, ipAddress, username, false, port);
			socketClient.sendData("00" + username);
			socketClient.start();

			musicControl = new Button("Music: On");
			musicControl.setTranslateX(MultiPlayerSettings.SCENE_WIDTH);
			musicControl.setTranslateY(MultiPlayerSettings.SCENE_HEIGHT - 55);
		}

		musicControl.setStyle("-fx-background-color: #00ff00");

		// Import the images and set background.
		InputStream isbg = Files.newInputStream(Paths.get("resources/space.jpg"));
		player1Background = new Image(isbg);
		isbg.close();

		InputStream isbg2 = Files.newInputStream(Paths.get("resources/space2.jpg"));
		player2Background = new Image(isbg2);
		isbg2.close();

		InputStream isshot1 = Files.newInputStream(Paths.get("resources/Multiplayer1Bullet.png"));
		player1BulletImage = new Image(isshot1);
		isshot1.close();

		InputStream isshot2 = Files.newInputStream(Paths.get("resources/Multiplayer2Bullet.png"));
		player2BulletImage = new Image(isshot2);
		isshot2.close();

		InputStream ispw = Files.newInputStream(Paths.get("resources/PowerUp.gif"));
		powerup = new Image(ispw);
		ispw.close();

		InputStream isLife = Files.newInputStream(Paths.get("resources/Life.png"));
		life = new Image(isLife);
		isLife.close();

		ImageView imgView1 = new ImageView(player1Background);
		ImageView imgView2 = new ImageView(player2Background);

		imgView1.setFitWidth(MultiPlayerSettings.SCENE_WIDTH);
		imgView1.setFitHeight(MultiPlayerSettings.SCENE_HEIGHT);

		imgView2.setFitWidth(MultiPlayerSettings.SCENE_WIDTH);
		imgView2.setFitHeight(MultiPlayerSettings.SCENE_HEIGHT);
		imgView2.setX(MultiPlayerSettings.SCENE_WIDTH);

		root.getChildren().addAll(imgView1, imgView2, playfieldLayer, scoreLayer);
		scene = new Scene(root, scene_width, scene_Height);

		SecondaryStage.setScene(scene);
		SecondaryStage.show();

		InputStream is = Files.newInputStream(Paths.get("resources/spaceship1.png"));
		playerImage = new Image(is);
		is.close();

		InputStream is1 = Files.newInputStream(Paths.get("resources/goat.gif"));
		enemyImage = new Image(is1);
		is1.close();

		// create player1 life and player2 life.
		life11 = new MultiPlayerLife(playfieldLayer, playerImage, 60, MultiPlayerSettings.SCENE_HEIGHT - 30);
		life12 = new MultiPlayerLife(playfieldLayer, playerImage, 95, MultiPlayerSettings.SCENE_HEIGHT - 30);

		life21 = new MultiPlayerLife(playfieldLayer, playerImage, MultiPlayerSettings.SCENE_WIDTH + 60,
				MultiPlayerSettings.SCENE_HEIGHT - 30);
		life22 = new MultiPlayerLife(playfieldLayer, playerImage, MultiPlayerSettings.SCENE_WIDTH + 95,
				MultiPlayerSettings.SCENE_HEIGHT - 30);

		// initial
		createScoreLayer();
		createPlayer1();
		createPlayer2();
		createPlayer1Life();
		createPlayer2Life();

		gameLoop = new AnimationTimer() {

			@Override
			public void handle(long now) {

				// shortcup to control back ground music.
				musicControl.setOnMouseClicked(a -> {
					if (mediaPlayer.isMute()) {
						mediaPlayer.setMute(false);
						musicControl.setStyle("-fx-background-color: #00ff00");
						musicControl.setText("Music: On");
					} else {
						mediaPlayer.setMute(true);
						musicControl.setStyle("-fx-background-color: #ff0000");
						musicControl.setText("Music: Off");
					}
				});

				// waiting for player to join.
				if (start == false && count3 == false) {
					count3 = true;
					new Thread(new Runnable() {

						@Override
						public void run() {
							while (count4) {
								waitingJoinText.setVisible(true);
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								waitingJoinText.setVisible(false);
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					}).start();
				}

				// player join in and game start.
				if (start) {

					waitingJoinText.setVisible(false);

					handleMultiPlayer();

					player1.move();
					player1.updateUI();
					player2.move();
					player2.updateUI();

					if (player1Dead == false)
						spawnPrimaryWeaponObjects(player1);
					if (player2Dead == false)
						spawnPrimaryWeaponObjects(player2);

					if (player1Dead == false)
						spawnPrimaryWeapon(player1);
					if (player2Dead == false)
						spawnPrimaryWeapon(player2);

					player1Enemies.forEach(sprite -> sprite.move());
					player1Enemies.forEach(sprite -> sprite.updateUI());
					player1Enemies.forEach(sprite -> sprite.checkRemovability());
					removeSprites(player1Enemies);

					player2Enemies.forEach(sprite -> sprite.move());
					player2Enemies.forEach(sprite -> sprite.updateUI());
					player2Enemies.forEach(sprite -> sprite.checkRemovability());
					removeSprites(player2Enemies);
					player1EnemyBulletList.forEach(sprite -> sprite.move());
					player1EnemyBulletList.forEach(sprite -> sprite.updateUI());
					player2EnemyBulletList.forEach(sprite -> sprite.move());
					player2EnemyBulletList.forEach(sprite -> sprite.updateUI());

					player1BulletList.forEach(sprite -> sprite.move());
					player2BulletList.forEach(sprite -> sprite.move());
					checkCollisions();

					player1BulletList.forEach(sprite -> sprite.updateUI());
					player2BulletList.forEach(sprite -> sprite.updateUI());

					player1Powerups.forEach(sprite -> sprite.move());
					player1Powerups.forEach(sprite -> sprite.updateUI());

					player1LifeList.forEach(sprite -> sprite.move());
					player1LifeList.forEach(sprite -> sprite.updateUI());

					player2Powerups.forEach(sprite -> sprite.move());
					player2Powerups.forEach(sprite -> sprite.updateUI());

					player2LifeList.forEach(sprite -> sprite.move());
					player2LifeList.forEach(sprite -> sprite.updateUI());

					removePlayer1Powerups(player1Powerups);
					removePlayer2Powerups(player2Powerups);
					removePlayer1Lifes(player1LifeList);
					removePlayer2Lifes(player2LifeList);

					updateScore();

					if (player1Dead == false) {
						gameOverPlayer1();
						checkPlayer1Life();
					}
					if (player2Dead == false) {
						gameOverPlayer2();
						checkPlayer2Life();
					}
				}
			}
		};
		gameLoop.start();
	}

	/**
	 * This method check if player1 has 0 life left then die and game over, game
	 * loop stop.
	 * 
	 * @return Nothing.
	 * 
	 * @author Zirui Wang and Alin Buzatu
	 */
	public void gameOverPlayer1() {
		if (player1Life == -1) {
			player1GameOver.setFont(Font.font(null, FontWeight.BOLD, 50));
			player1GameOver.setStroke(Color.BLACK);
			player1GameOver.setFill(Color.RED);
			player1GameOver.setText("Player 1 GAME OVER");
			player1GameOver.setVisible(true);

			double x = (MultiPlayerSettings.SCENE_WIDTH - player1GameOver.getBoundsInLocal().getWidth()) / 2;
			double y = (MultiPlayerSettings.SCENE_HEIGHT - player1GameOver.getBoundsInLocal().getHeight()) / 2;
			player1GameOver.relocate(x, y);

			player1.removeFromLayer();
			player1.remove();
			player1BulletList.forEach(sprite -> sprite.removeFromLayer());
			player1Dead = true;
			gameLoop.stop();
		}
	}

	/**
	 * This method check if player1 has 0 life left then die and game over, game
	 * loop stop.
	 * 
	 * @return Nothing.
	 * 
	 * @author Zirui Wang and Alin Buzatu
	 */
	public void gameOverPlayer2() {
		if (player2Life == -1) {
			player2GameOver.setFont(Font.font(null, FontWeight.BOLD, 50));
			player2GameOver.setStroke(Color.BLACK);
			player2GameOver.setFill(Color.RED);
			player2GameOver.setText("Player 2 GAME OVER");
			player2GameOver.setVisible(true);

			double x = (MultiPlayerSettings.SCENE_WIDTH - player2GameOver.getBoundsInLocal().getWidth()) / 2;
			double y = (MultiPlayerSettings.SCENE_HEIGHT - player2GameOver.getBoundsInLocal().getHeight()) / 2;
			player2GameOver.relocate(x + MultiPlayerSettings.SCENE_WIDTH, y);

			player2.removeFromLayer();
			player2.remove();
			player2BulletList.forEach(sprite -> sprite.removeFromLayer());
			player2Dead = true;
			gameLoop.stop();
		}
	}

	/**
	 * This method is to set and show player score, power up text, power down text,
	 * life increase text and so on.
	 * 
	 * @return Nothing.
	 * 
	 * @author Zirui Wang and Alin Buzatu
	 */
	public void createScoreLayer() {

		collisionText.setFont(Font.font(null, FontWeight.BOLD, 64));
		collisionText.setStroke(Color.BLACK);
		collisionText.setFill(Color.RED);

		player1Score.setFont(Font.font(null, FontWeight.BOLD, 25));
		player1Score.setStroke(Color.BLACK);
		player1Score.setFill(Color.RED);

		player2Score.setFont(Font.font(null, FontWeight.BOLD, 25));
		player2Score.setStroke(Color.BLACK);
		player2Score.setFill(Color.RED);

		player1PowerupText.setFont(Font.font(null, FontWeight.BOLD, 25));
		player1PowerupText.setStroke(Color.YELLOW);
		player1PowerupText.setFill(Color.PINK);
		player1PowerupText.setText("Power Up");
		player1PowerupText.relocate(MultiPlayerSettings.SCENE_WIDTH / 2 - 50, MultiPlayerSettings.SCENE_HEIGHT / 2);
		player1PowerupText.setVisible(false);

		player2PowerupText.setFont(Font.font(null, FontWeight.BOLD, 25));
		player2PowerupText.setStroke(Color.YELLOW);
		player2PowerupText.setFill(Color.PINK);
		player2PowerupText.setText("Power Up");
		player2PowerupText.relocate(MultiPlayerSettings.SCENE_WIDTH / 2 + MultiPlayerSettings.SCENE_WIDTH,
				MultiPlayerSettings.SCENE_HEIGHT / 2);
		player2PowerupText.setVisible(false);

		player1PowerdownText.setFont(Font.font(null, FontWeight.BOLD, 25));
		player1PowerdownText.setStroke(Color.GREEN);
		player1PowerdownText.setFill(Color.PINK);
		player1PowerdownText.setText("Power Down");
		player1PowerdownText.relocate(MultiPlayerSettings.SCENE_WIDTH / 2 - 50, MultiPlayerSettings.SCENE_HEIGHT / 2);
		player1PowerdownText.setVisible(false);

		player2PowerdownText.setFont(Font.font(null, FontWeight.BOLD, 25));
		player2PowerdownText.setStroke(Color.GREEN);
		player2PowerdownText.setFill(Color.PINK);
		player2PowerdownText.setText("Power Down");
		player2PowerdownText.relocate(MultiPlayerSettings.SCENE_WIDTH / 2 + MultiPlayerSettings.SCENE_WIDTH,
				MultiPlayerSettings.SCENE_HEIGHT / 2);
		player2PowerdownText.setVisible(false);

		player1LifeIncrease.setFont(Font.font(null, FontWeight.BOLD, 25));
		player1LifeIncrease.setStroke(Color.GREEN);
		player1LifeIncrease.setFill(Color.PINK);
		player1LifeIncrease.setText("Life + 1");
		player1LifeIncrease.relocate(MultiPlayerSettings.SCENE_WIDTH / 2 - 50, MultiPlayerSettings.SCENE_HEIGHT / 2);
		player1LifeIncrease.setVisible(false);

		player2LifeIncrease.setFont(Font.font(null, FontWeight.BOLD, 25));
		player2LifeIncrease.setStroke(Color.GREEN);
		player2LifeIncrease.setFill(Color.PINK);
		player2LifeIncrease.setText("Life + 1");
		player2LifeIncrease.relocate(MultiPlayerSettings.SCENE_WIDTH / 2 + MultiPlayerSettings.SCENE_WIDTH,
				MultiPlayerSettings.SCENE_HEIGHT / 2);
		player2LifeIncrease.setVisible(false);

		start3.setFont(Font.font(null, FontWeight.BOLD, 100));
		start3.setStroke(Color.GREEN);
		start3.setFill(Color.PINK);
		start3.setText("3");
		start3.relocate(MultiPlayerSettings.SCENE_WIDTH - 35, MultiPlayerSettings.SCENE_HEIGHT / 2 - 50);
		start3.setVisible(false);

		start2.setFont(Font.font(null, FontWeight.BOLD, 100));
		start2.setStroke(Color.GREEN);
		start2.setFill(Color.PINK);
		start2.setText("2");
		start2.relocate(MultiPlayerSettings.SCENE_WIDTH - 35, MultiPlayerSettings.SCENE_HEIGHT / 2 - 50);
		start2.setVisible(false);

		start1.setFont(Font.font(null, FontWeight.BOLD, 100));
		start1.setStroke(Color.GREEN);
		start1.setFill(Color.PINK);
		start1.setText("1");
		start1.relocate(MultiPlayerSettings.SCENE_WIDTH - 35, MultiPlayerSettings.SCENE_HEIGHT / 2 - 50);
		start1.setVisible(false);

		startText.setFont(Font.font(null, FontWeight.BOLD, 100));
		startText.setStroke(Color.GREEN);
		startText.setFill(Color.PINK);
		startText.setText("Start");
		startText.relocate(MultiPlayerSettings.SCENE_WIDTH - 115, MultiPlayerSettings.SCENE_HEIGHT / 2 - 50);
		startText.setVisible(false);

		waitingJoinText.setFont(Font.font(null, FontWeight.BOLD, 80));
		waitingJoinText.setStroke(Color.GREEN);
		waitingJoinText.setFill(Color.PINK);
		waitingJoinText.setText("Waiting For Player to Join");
		waitingJoinText.relocate(MultiPlayerSettings.SCENE_WIDTH - 550, MultiPlayerSettings.SCENE_HEIGHT / 2 - 50);
		waitingJoinText.setVisible(false);

		scoreLayer.getChildren().addAll(collisionText, player1Score, player2Score, player1GameOver, player2GameOver,
				player1LifeText, player2LifeText, player1PowerupText, player2PowerupText, player1PowerdownText,
				player2PowerdownText, player1LifeIncrease, player2LifeIncrease, start3, start2, start1, startText,
				waitingJoinText, musicControl);

		collisionText.setText("Collision");
		double x = (MultiPlayerSettings.SCENE_WIDTH - collisionText.getBoundsInLocal().getWidth()) / 2;
		double y = (MultiPlayerSettings.SCENE_HEIGHT - collisionText.getBoundsInLocal().getHeight()) / 2;
		collisionText.relocate(x, y);
		player1Score.relocate(0, 0);
		player2Score.relocate(MultiPlayerSettings.SCENE_WIDTH, 0);
		collisionText.setText("");

		collisionText.setBoundsType(TextBoundsType.VISUAL);

	}

	/**
	 * This method is to createPlayer1
	 * 
	 * @return Nothing.
	 * 
	 * @author Zirui Wang and Alin Buzatu
	 */
	public void createPlayer1() {

		Input input = new Input(scene);

		input.addListeners();

		Image image = playerImage;

		double x = (MultiPlayerSettings.SCENE_WIDTH - image.getWidth()) / 2.0;
		double y = MultiPlayerSettings.SCENE_HEIGHT * 0.7;

		Player player1 = new Player(playfieldLayer, image, x, y, 0, 0, 0, 0, MultiPlayerSettings.PLAYER_SHIP_HEALTH, 1,
				MultiPlayerSettings.PLAYER_SHIP_SPEED, input, x, y, 1);

		this.player1 = player1;

	}

	/**
	 * This method is to createPlayer2
	 * 
	 * @return Nothing.
	 * 
	 * @author Zirui Wang and Alin Buzatu
	 */
	public void createPlayer2() {

		Input input = new Input(scene);

		input.addListeners();

		Image image = playerImage;

		double x = (MultiPlayerSettings.SCENE_WIDTH - image.getWidth()) / 2.0;
		double y = MultiPlayerSettings.SCENE_HEIGHT * 0.7;

		Player player = new Player(playfieldLayer, image, x + MultiPlayerSettings.SCENE_WIDTH, y, 0, 0, 0, 0,
				MultiPlayerSettings.PLAYER_SHIP_HEALTH, 1, MultiPlayerSettings.PLAYER_SHIP_SPEED, input,
				x + MultiPlayerSettings.SCENE_WIDTH, y, 2);

		this.player2 = player;

	}

	/**
	 * This method is to create player1 "life" text in the life bottom corner, and
	 * add "life" picture to layer
	 * 
	 * @return Nothing.
	 * 
	 * @author Zirui Wang
	 */
	public void createPlayer1Life() {
		life11.addToLayer();
		life12.addToLayer();
		player1LifeText.setFont(Font.font(null, FontWeight.BOLD, 25));
		player1LifeText.setStroke(Color.BLACK);
		player1LifeText.setFill(Color.GREEN);
		player1LifeText.setText("Life: ");
		player1LifeText.setVisible(true);
		player1LifeText.relocate(0, MultiPlayerSettings.SCENE_HEIGHT - 30);
	}

	/**
	 * This method is to create player2 "life" text in the life bottom corner, and
	 * add "life" picture to layer
	 * 
	 * @return Nothing.
	 * 
	 * @author Zirui Wang
	 */
	public void createPlayer2Life() {
		life21.addToLayer();
		life22.addToLayer();
		player2LifeText.setFont(Font.font(null, FontWeight.BOLD, 25));
		player2LifeText.setStroke(Color.BLACK);
		player2LifeText.setFill(Color.RED);
		player2LifeText.setText("Life: ");
		player2LifeText.setVisible(true);
		player2LifeText.relocate(MultiPlayerSettings.SCENE_WIDTH, MultiPlayerSettings.SCENE_HEIGHT - 30);
	}

	/**
	 * This method is to check the player shooting. If player press space key then
	 * it will shoot. When shooting, the number of bullets will be determined based
	 * on the current power up level
	 * 
	 * @return Nothing.
	 * 
	 * @author Zirui Wang and Alin Buzatu
	 */
	public void spawnPrimaryWeaponObjects(Player player) {

		player.chargePrimaryWeapon();

		if ((isHost == 0 && player == player1) || (isHost != 0 && player == player2)) {
			if (player.isFirePrimaryWeapon()) {

				if (player == player1) {
					if (player1PowerScore == 1) {
						Shot player1Bullet;
						Image image1 = player1BulletImage;
						double x = player.getPrimaryWeaponX() - image1.getWidth() / 2.0;
						double y = player.getPrimaryWeaponY();
						double speed = player.getPrimaryWeaponBulletSpeed();
						player1Bullet = new Shot(playfieldLayer, image1, x, y, 0, -speed, 0, 0);
						player1BulletList.add(player1Bullet);
						player.unchargePrimaryWeapon();
					}
					if (player1PowerScore == 2) {
						Shot playerBullet;

						Image image = player1BulletImage;

						double x = player.getPrimaryWeaponX() - image.getWidth() / 2.0;
						double y = player.getPrimaryWeaponY();
						double speed = player.getPrimaryWeaponBulletSpeed();

						playerBullet = new Shot(playfieldLayer, image, x - 2, y, 0, -speed, 0, 0);
						player1BulletList.add(playerBullet);
						playerBullet = new Shot(playfieldLayer, image, x + 2, y, 0, -speed, 0, 0);
						player1BulletList.add(playerBullet);
						player.unchargePrimaryWeapon();
					}
					if (player1PowerScore == 3) {
						Shot playerBullet;

						Image image = player1BulletImage;

						double x = player.getPrimaryWeaponX() - image.getWidth() / 2.0;
						double y = player.getPrimaryWeaponY();

						double speed = player.getPrimaryWeaponBulletSpeed();
						playerBullet = new Shot(playfieldLayer, image, x - 3, y, 0, -speed, 0, 0);
						player1BulletList.add(playerBullet);
						playerBullet = new Shot(playfieldLayer, image, x, y, 0, -speed, 0, 0);
						player1BulletList.add(playerBullet);
						playerBullet = new Shot(playfieldLayer, image, x + 3, y, 0, -speed, 0, 0);
						player1BulletList.add(playerBullet);
						player.unchargePrimaryWeapon();
					}
					if (player1PowerScore == 4) {
						Shot playerBullet;

						Image image = player1BulletImage;

						double x = player.getPrimaryWeaponX() - image.getWidth() / 2.0;
						double y = player.getPrimaryWeaponY();

						double speed = player.getPrimaryWeaponBulletSpeed();
						playerBullet = new Shot(playfieldLayer, image, x - 6, y, 0, -speed, 0, 0);
						player1BulletList.add(playerBullet);
						playerBullet = new Shot(playfieldLayer, image, x - 2, y, 0, -speed, 0, 0);
						player1BulletList.add(playerBullet);
						playerBullet = new Shot(playfieldLayer, image, x + 2, y, 0, -speed, 0, 0);
						player1BulletList.add(playerBullet);
						playerBullet = new Shot(playfieldLayer, image, x + 6, y, 0, -speed, 0, 0);
						player1BulletList.add(playerBullet);
						player.unchargePrimaryWeapon();
					}
					if (player1PowerScore >= 5) {
						Shot playerBullet;

						Image image = player1BulletImage;

						double x = player.getPrimaryWeaponX() - image.getWidth() / 2.0;
						double y = player.getPrimaryWeaponY();

						double spread = player.getPrimaryWeaponBulletSpread();
						double count = player.getPrimaryWeaponBulletCount();
						double speed = player.getPrimaryWeaponBulletSpeed();
						playerBullet = new Shot(playfieldLayer, image, x, y, 0, -speed, 0, 0);
						player1BulletList.add(playerBullet);

						for (int i = 0; i < count / 2.0; i++) {

							// left
							playerBullet = new Shot(playfieldLayer, image, x, y, -spread * i, -speed, 0, 0);
							player1BulletList.add(playerBullet);

							// right
							playerBullet = new Shot(playfieldLayer, image, x, y, spread * i, -speed, 0, 0);
							player1BulletList.add(playerBullet);

						}
						player.unchargePrimaryWeapon();
					}

				}

				if (player == player2) {
					if (player2PowerScore == 1) {
						Shot player2Bullet;
						Image image2 = player2BulletImage;
						double x = player.getPrimaryWeaponX() - image2.getWidth() / 2.0;
						double y = player.getPrimaryWeaponY();
						double speed = player.getPrimaryWeaponBulletSpeed();
						player2Bullet = new Shot(playfieldLayer, image2, x, y, 0, -speed, 0, 0);
						player2BulletList.add(player2Bullet);
						player.unchargePrimaryWeapon();
					}
					if (player2PowerScore == 2) {
						Shot playerBullet;

						Image image = player2BulletImage;

						double x = player.getPrimaryWeaponX() - image.getWidth() / 2.0;
						double y = player.getPrimaryWeaponY();
						double speed = player.getPrimaryWeaponBulletSpeed();

						playerBullet = new Shot(playfieldLayer, image, x - 2, y, 0, -speed, 0, 0);
						player2BulletList.add(playerBullet);
						playerBullet = new Shot(playfieldLayer, image, x + 2, y, 0, -speed, 0, 0);
						player2BulletList.add(playerBullet);
						player.unchargePrimaryWeapon();
					}
					if (player2PowerScore == 3) {
						Shot playerBullet;

						Image image = player2BulletImage;

						double x = player.getPrimaryWeaponX() - image.getWidth() / 2.0;
						double y = player.getPrimaryWeaponY();

						double speed = player.getPrimaryWeaponBulletSpeed();
						playerBullet = new Shot(playfieldLayer, image, x - 3, y, 0, -speed, 0, 0);
						player2BulletList.add(playerBullet);
						playerBullet = new Shot(playfieldLayer, image, x, y, 0, -speed, 0, 0);
						player2BulletList.add(playerBullet);
						playerBullet = new Shot(playfieldLayer, image, x + 3, y, 0, -speed, 0, 0);
						player2BulletList.add(playerBullet);
						player.unchargePrimaryWeapon();
					}
					if (player2PowerScore == 4) {
						Shot playerBullet;

						Image image = player2BulletImage;

						double x = player.getPrimaryWeaponX() - image.getWidth() / 2.0;
						double y = player.getPrimaryWeaponY();

						double speed = player.getPrimaryWeaponBulletSpeed();
						playerBullet = new Shot(playfieldLayer, image, x - 6, y, 0, -speed, 0, 0);
						player2BulletList.add(playerBullet);
						playerBullet = new Shot(playfieldLayer, image, x - 2, y, 0, -speed, 0, 0);
						player2BulletList.add(playerBullet);
						playerBullet = new Shot(playfieldLayer, image, x + 2, y, 0, -speed, 0, 0);
						player2BulletList.add(playerBullet);
						playerBullet = new Shot(playfieldLayer, image, x + 6, y, 0, -speed, 0, 0);
						player2BulletList.add(playerBullet);
						player.unchargePrimaryWeapon();
					}
					if (player2PowerScore >= 5) {
						Shot playerBullet;

						Image image = player2BulletImage;

						double x = player.getPrimaryWeaponX() - image.getWidth() / 2.0;
						double y = player.getPrimaryWeaponY();

						double spread = player.getPrimaryWeaponBulletSpread();
						double count = player.getPrimaryWeaponBulletCount();
						double speed = player.getPrimaryWeaponBulletSpeed();
						playerBullet = new Shot(playfieldLayer, image, x, y, 0, -speed, 0, 0);
						player2BulletList.add(playerBullet);

						for (int i = 0; i < count / 2.0; i++) {

							// left
							playerBullet = new Shot(playfieldLayer, image, x, y, -spread * i, -speed, 0, 0);
							player2BulletList.add(playerBullet);

							// right
							playerBullet = new Shot(playfieldLayer, image, x, y, spread * i, -speed, 0, 0);
							player2BulletList.add(playerBullet);

						}
						player.unchargePrimaryWeapon();
					}

				}

				socketClient.sendData("05" + username);

			}
		}

	}

	/**
	 * This method is to remove life picture if player1 "eat" the life buff. Then
	 * check if player1 have [0,1] life then life + 1
	 * 
	 * @return Nothing.
	 * 
	 * @author Zirui Wang
	 */
	public void removePlayer1Lifes(List<? extends Life> spriteList) {
		Iterator<? extends Life> iter = spriteList.iterator();
		while (iter.hasNext()) {
			Life sprite = iter.next();
			if (sprite.isRemovable()) {
				sprite.removeFromLayer();
				iter.remove();
				if (spriteList.equals(player1LifeList)) {
					if (player1Life < 2) {
						player1Life++;

						new Thread(new Runnable() {

							@Override
							public void run() {
								player1LifeIncrease.setVisible(true);
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								player1LifeIncrease.setVisible(false);
							}
						}).start();
					}
				}
			}
		}
	}

	/**
	 * This method is to remove life picture if player2 "eat" the life buff. Then
	 * check if player2 have [0,1] life then life + 1
	 * 
	 * @return Nothing.
	 * 
	 * @author Zirui Wang
	 */
	public void removePlayer2Lifes(List<? extends Life> spriteList) {
		Iterator<? extends Life> iter = spriteList.iterator();
		while (iter.hasNext()) {
			Life sprite = iter.next();
			if (sprite.isRemovable()) {
				sprite.removeFromLayer();
				iter.remove();
				if (spriteList.equals(player2LifeList)) {
					if (player2Life < 2) {
						player2Life++;
						new Thread(new Runnable() {

							@Override
							public void run() {
								player2LifeIncrease.setVisible(true);
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								player2LifeIncrease.setVisible(false);
							}
						}).start();
					}
				}
			}
		}
	}

	/**
	 * This method is to remove power up picture if player1 "eat" the power up buff.
	 * Then check if player1 have [0,4] power up level then power up level +1
	 * 
	 * @return Nothing.
	 * 
	 * @author Zirui Wang
	 * @author Alin Buzatu
	 */
	public void removePlayer1Powerups(List<? extends PowerUp> spriteList) {
		Iterator<? extends PowerUp> iter = spriteList.iterator();
		while (iter.hasNext()) {
			PowerUp sprite = iter.next();
			if (sprite.isRemovable()) {
				sprite.removeFromLayer();
				iter.remove();
				if (spriteList.equals(player1Powerups)) {
					if (sprite.getProperty() == 0) {
						if (player1PowerScore < 5) {
							player1PowerScore++;
							new Thread(new Runnable() {

								@Override
								public void run() {
									player1PowerupText.setVisible(true);
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									player1PowerupText.setVisible(false);
								}
							}).start();
						}
					}
					if (sprite.getProperty() == 1) {
						if (player2PowerScore > 1 && player2Dead == false) {
							player2PowerScore--;
							new Thread(new Runnable() {

								@Override
								public void run() {
									player2PowerdownText.setVisible(true);
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									player2PowerdownText.setVisible(false);
								}
							}).start();
						}
					}
				}

			}
		}
	}

	/**
	 * This method is to remove power up picture if player2 "eat" the power up buff.
	 * Then check if player2 have [0,4] power up level then power up level +1
	 * 
	 * @return Nothing.
	 * 
	 * @author Zirui Wang
	 * @author Alin Buzatu
	 */
	public void removePlayer2Powerups(List<? extends PowerUp> spriteList) {
		Iterator<? extends PowerUp> iter = spriteList.iterator();
		while (iter.hasNext()) {
			PowerUp sprite = iter.next();
			if (sprite.isRemovable()) {
				sprite.removeFromLayer();
				iter.remove();
				if (spriteList.equals(player2Powerups)) {
					if (sprite.getProperty() == 0) {
						if (player2PowerScore < 5) {
							player2PowerScore++;
							new Thread(new Runnable() {

								@Override
								public void run() {
									player2PowerupText.setVisible(true);
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									player2PowerupText.setVisible(false);
								}
							}).start();
						}
					}
					if (sprite.getProperty() == 1) {
						if (player1PowerScore > 1 && player1Dead == false) {
							player1PowerScore--;
							new Thread(new Runnable() {

								@Override
								public void run() {
									player1PowerdownText.setVisible(true);
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									player1PowerdownText.setVisible(false);
								}

							}).start();
						}
					}
				}
			}
		}
	}

	/**
	 * This method is to Synchronize multiplayer shooting
	 * 
	 * @return Nothing.
	 * 
	 * @author Zirui Wang
	 * @author Alin Buzatu
	 */
	public void spawnPrimaryWeapon(Player player) {

		player.chargePrimaryWeapon();

		if (isShoot) {
			isShoot = false;
			if (player == player1) {
				if (player1PowerScore == 1) {
					Shot player1Bullet;
					Image image1 = player1BulletImage;
					double x = player.getPrimaryWeaponX() - image1.getWidth() / 2.0;
					double y = player.getPrimaryWeaponY();
					double speed = player.getPrimaryWeaponBulletSpeed();
					player1Bullet = new Shot(playfieldLayer, image1, x, y, 0, -speed, 0, 0);
					player1BulletList.add(player1Bullet);
					player.unchargePrimaryWeapon();
				}
				if (player1PowerScore == 2) {
					Shot playerBullet;

					Image image = player1BulletImage;

					double x = player.getPrimaryWeaponX() - image.getWidth() / 2.0;
					double y = player.getPrimaryWeaponY();
					double speed = player.getPrimaryWeaponBulletSpeed();

					playerBullet = new Shot(playfieldLayer, image, x - 2, y, 0, -speed, 0, 0);
					player1BulletList.add(playerBullet);
					playerBullet = new Shot(playfieldLayer, image, x + 2, y, 0, -speed, 0, 0);
					player1BulletList.add(playerBullet);
					player.unchargePrimaryWeapon();
				}
				if (player1PowerScore == 3) {
					Shot playerBullet;

					Image image = player1BulletImage;

					double x = player.getPrimaryWeaponX() - image.getWidth() / 2.0;
					double y = player.getPrimaryWeaponY();

					double speed = player.getPrimaryWeaponBulletSpeed();
					playerBullet = new Shot(playfieldLayer, image, x - 3, y, 0, -speed, 0, 0);
					player1BulletList.add(playerBullet);
					playerBullet = new Shot(playfieldLayer, image, x, y, 0, -speed, 0, 0);
					player1BulletList.add(playerBullet);
					playerBullet = new Shot(playfieldLayer, image, x + 3, y, 0, -speed, 0, 0);
					player1BulletList.add(playerBullet);
					player.unchargePrimaryWeapon();
				}
				if (player1PowerScore == 4) {
					Shot playerBullet;

					Image image = player1BulletImage;

					double x = player.getPrimaryWeaponX() - image.getWidth() / 2.0;
					double y = player.getPrimaryWeaponY();

					double speed = player.getPrimaryWeaponBulletSpeed();
					playerBullet = new Shot(playfieldLayer, image, x - 6, y, 0, -speed, 0, 0);
					player1BulletList.add(playerBullet);
					playerBullet = new Shot(playfieldLayer, image, x - 2, y, 0, -speed, 0, 0);
					player1BulletList.add(playerBullet);
					playerBullet = new Shot(playfieldLayer, image, x + 2, y, 0, -speed, 0, 0);
					player1BulletList.add(playerBullet);
					playerBullet = new Shot(playfieldLayer, image, x + 6, y, 0, -speed, 0, 0);
					player1BulletList.add(playerBullet);
					player.unchargePrimaryWeapon();
				}
				if (player1PowerScore >= 5) {
					Shot playerBullet;

					Image image = player1BulletImage;

					double x = player.getPrimaryWeaponX() - image.getWidth() / 2.0;
					double y = player.getPrimaryWeaponY();

					double spread = player.getPrimaryWeaponBulletSpread();
					double count = player.getPrimaryWeaponBulletCount();
					double speed = player.getPrimaryWeaponBulletSpeed();
					playerBullet = new Shot(playfieldLayer, image, x, y, 0, -speed, 0, 0);
					player1BulletList.add(playerBullet);

					for (int i = 0; i < count / 2.0; i++) {

						// left
						playerBullet = new Shot(playfieldLayer, image, x, y, -spread * i, -speed, 0, 0);
						player1BulletList.add(playerBullet);

						// right
						playerBullet = new Shot(playfieldLayer, image, x, y, spread * i, -speed, 0, 0);
						player1BulletList.add(playerBullet);

					}
					player.unchargePrimaryWeapon();
				}

			}
			if (player == player2) {
				if (player2PowerScore == 1) {
					Shot player2Bullet;
					Image image2 = player2BulletImage;
					double x = player.getPrimaryWeaponX() - image2.getWidth() / 2.0;
					double y = player.getPrimaryWeaponY();
					double speed = player.getPrimaryWeaponBulletSpeed();
					player2Bullet = new Shot(playfieldLayer, image2, x, y, 0, -speed, 0, 0);
					player2BulletList.add(player2Bullet);
					player.unchargePrimaryWeapon();
				}
				if (player2PowerScore == 2) {
					Shot playerBullet;

					Image image = player2BulletImage;

					double x = player.getPrimaryWeaponX() - image.getWidth() / 2.0;
					double y = player.getPrimaryWeaponY();
					double speed = player.getPrimaryWeaponBulletSpeed();

					playerBullet = new Shot(playfieldLayer, image, x - 2, y, 0, -speed, 0, 0);
					player2BulletList.add(playerBullet);
					playerBullet = new Shot(playfieldLayer, image, x + 2, y, 0, -speed, 0, 0);
					player2BulletList.add(playerBullet);
					player.unchargePrimaryWeapon();
				}
				if (player2PowerScore == 3) {
					Shot playerBullet;

					Image image = player2BulletImage;

					double x = player.getPrimaryWeaponX() - image.getWidth() / 2.0;
					double y = player.getPrimaryWeaponY();

					double speed = player.getPrimaryWeaponBulletSpeed();
					playerBullet = new Shot(playfieldLayer, image, x - 3, y, 0, -speed, 0, 0);
					player2BulletList.add(playerBullet);
					playerBullet = new Shot(playfieldLayer, image, x, y, 0, -speed, 0, 0);
					player2BulletList.add(playerBullet);
					playerBullet = new Shot(playfieldLayer, image, x + 3, y, 0, -speed, 0, 0);
					player2BulletList.add(playerBullet);
					player.unchargePrimaryWeapon();
				}
				if (player2PowerScore == 4) {
					Shot playerBullet;

					Image image = player2BulletImage;

					double x = player.getPrimaryWeaponX() - image.getWidth() / 2.0;
					double y = player.getPrimaryWeaponY();

					double speed = player.getPrimaryWeaponBulletSpeed();
					playerBullet = new Shot(playfieldLayer, image, x - 6, y, 0, -speed, 0, 0);
					player2BulletList.add(playerBullet);
					playerBullet = new Shot(playfieldLayer, image, x - 2, y, 0, -speed, 0, 0);
					player2BulletList.add(playerBullet);
					playerBullet = new Shot(playfieldLayer, image, x + 2, y, 0, -speed, 0, 0);
					player2BulletList.add(playerBullet);
					playerBullet = new Shot(playfieldLayer, image, x + 6, y, 0, -speed, 0, 0);
					player2BulletList.add(playerBullet);
					player.unchargePrimaryWeapon();
				}
				if (player2PowerScore >= 5) {
					Shot playerBullet;

					Image image = player2BulletImage;

					double x = player.getPrimaryWeaponX() - image.getWidth() / 2.0;
					double y = player.getPrimaryWeaponY();

					double spread = player.getPrimaryWeaponBulletSpread();
					double count = player.getPrimaryWeaponBulletCount();
					double speed = player.getPrimaryWeaponBulletSpeed();
					playerBullet = new Shot(playfieldLayer, image, x, y, 0, -speed, 0, 0);
					player2BulletList.add(playerBullet);

					for (int i = 0; i < count / 2.0; i++) {

						// left
						playerBullet = new Shot(playfieldLayer, image, x, y, -spread * i, -speed, 0, 0);
						player2BulletList.add(playerBullet);

						// right
						playerBullet = new Shot(playfieldLayer, image, x, y, spread * i, -speed, 0, 0);
						player2BulletList.add(playerBullet);

					}
					player.unchargePrimaryWeapon();
				}
			}
		}
	}

	/**
	 * This method is to remove enemies if the get killed by player and increase
	 * score
	 * 
	 * @return Nothing.
	 * 
	 * @author Zirui Wang
	 * @author Alin Buzatu
	 */
	public void removeSprites(List<? extends Alien> spriteList) {

		Iterator<? extends Alien> iter = spriteList.iterator();
		while (iter.hasNext()) {
			Alien sprite = iter.next();

			if (sprite.isRemovable()) {
				if (sprite.getIsDie()) {
					if (spriteList == player1Enemies)
						score1 += 10;
					if (spriteList == player2Enemies)
						score2 += 10;
				}
				sprite.removeFromLayer();

				iter.remove();
			}
		}
	}

	/**
	 * This method is to check if enemy get damage or player get damage. And check
	 * if they are removeable
	 * 
	 * @return Nothing.
	 * 
	 * @author Zirui Wang
	 * @author Alin Buzatu
	 */
	public void checkCollisions() {

		player1Collision = false;
		player2Collision = false;

		// player1
		if (player1.getInvincible() == false) {
			for (Alien enemy : player1Enemies) {
				if (player1.collidesWith(enemy)) {
					player1Collision = true;
				}
			}
		}
		for (Shot shot : player1BulletList) {
			for (Alien enemy : player1Enemies) {
				if (shot.collidesWith(enemy)) {
					shot.stopMovement();
					shot.removeFromLayer();
					shot.remove();
					shot.setX(0);
					enemy.getDamagedBy(player1);
					if (enemy.getHealth() <= 0.0) {
						enemy.remove();
						enemy.setDie(true);
					}
				}
			}
		}
		if (player1.getInvincible() == false) {
			for (Shot shot : player1EnemyBulletList) {
				if (shot.collidesWith(player1)) {
					shot.stopMovement();
					shot.removeFromLayer();
					shot.remove();
					shot.setX(0);
					player1.setHealth(player1.getHealth() - 5);
					if (player1.getHealth() <= 0) {
						player1Collision = true;
						gameOverPlayer1();
					}
				}
			}
		}
		for (PowerUp powerup : player1Powerups) {
			if (player1.collidesWith(powerup)) {
//				player1PowerScore++;
				powerup.remove();
			}
		}
		for (Life life : player1LifeList) {
			if (player1.collidesWith(life)) {
				life.remove();
			}
		}

		// player2
		if (player2.getInvincible() == false) {
			for (Alien enemy : player2Enemies) {
				if (player2.collidesWith(enemy)) {
					player2Collision = true;
				}
			}
		}
		for (Shot shot : player2BulletList) {
			for (Alien enemy : player2Enemies) {
				if (shot.collidesWith(enemy)) {
					shot.stopMovement();
					shot.removeFromLayer();
					shot.remove();
					shot.setX(0);
					enemy.getDamagedBy(player2);
					if (enemy.getHealth() <= 0.0) {
						enemy.remove();
						enemy.setDie(true);
					}

				}
			}
		}

		if (player2.getInvincible() == false) {
			for (Shot shot : player2EnemyBulletList) {
				if (shot.collidesWith(player2)) {
					shot.stopMovement();
					shot.removeFromLayer();
					shot.remove();
					shot.setX(0);
					player2.setHealth(player2.getHealth() - 5);
					if (player2.getHealth() <= 0) {
						player2Collision = true;
						gameOverPlayer2();
					}
				}
			}
		}
		for (PowerUp powerup : player2Powerups) {
			if (player2.collidesWith(powerup)) {
//				player2PowerScore++;
				powerup.remove();
			}
		}
		for (Life life : player2LifeList) {
			if (player2.collidesWith(life)) {
				life.remove();
			}
		}
	}

	/**
	 * This method is to update score text.
	 * 
	 * @return Nothing.
	 * 
	 * @author Zirui Wang
	 * @author Alin Buzatu
	 */
	public void updateScore() {
		player1Score.setText(Integer.toString(score1));
		player2Score.setText(Integer.toString(score2));
	}

	/**
	 * This method is to check if player1 die and has life, then resurgence, and life - 1. Otherwise game over.
	 * 
	 * @return Nothing.
	 * 
	 * @author Zirui Wang
	 */
	public void checkPlayer1Life() {

		if (player1Life == 2) {
			life11.isVisible(true);
			life12.isVisible(true);
		}
		if (player1Life == 1) {
			life11.isVisible(true);
			life12.isVisible(false);
		}
		if (player1Life == 0) {
			life11.isVisible(false);
			life12.isVisible(false);
		}

		if (player1Collision == true) {
			player1Collision = false;
			player1Life = player1Life - 1;
			player1.remove();
			player1.removeFromLayer();
			if (player1Life > -1) {
				if (player1Life == 2) {
					life11.isVisible(true);
					life12.isVisible(true);
				}
				if (player1Life == 1) {
//					life12.removeFromLayer();
					life11.isVisible(true);
					life12.isVisible(false);
					createPlayer1();
				}
				if (player1Life == 0) {
//					life11.removeFromLayer();
					life11.isVisible(false);
					life12.isVisible(false);
					createPlayer1();
				}
				player1.setInvincible(true);
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						player1.setInvincible(false);
					}
				}).start();

				new Thread(new Runnable() {
					int tmp = 0;

					@Override
					public void run() {
						while (tmp++ < 13) {
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									player1.removeFromLayer();
								}
							});
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									player1.addToLayer();
								}
							});
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();
			}
		}
	}

	/**
	 * This method is to check if player2 die and has life, then resurgence, and life - 1. Otherwise game over.
	 * 
	 * @return Nothing.
	 * 
	 * @author Zirui Wang
	 */
	public void checkPlayer2Life() {
		if (player2Life == 2) {
			life21.isVisible(true);
			life22.isVisible(true);
		}
		if (player2Life == 1) {
			life21.isVisible(true);
			life22.isVisible(false);
		}
		if (player2Life == 0) {
			life21.isVisible(false);
			life22.isVisible(false);
		}
		if (player2Collision == true) {
			player2Collision = false;
			player2Life = player2Life - 1;
			player2.remove();
			player2.removeFromLayer();
			if (player2Life > -1) {
				if (player2Life == 2) {
					life21.isVisible(true);
					life22.isVisible(true);
				}
				if (player2Life == 1) {
					life21.isVisible(true);
					life22.isVisible(false);
					createPlayer2();
				}
				if (player2Life == 0) {
					life21.isVisible(false);
					life22.isVisible(false);
					createPlayer2();
				}
				player2.setInvincible(true);
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						player2.setInvincible(false);
					}
				}).start();

				new Thread(new Runnable() {
					int tmp = 0;

					@Override
					public void run() {
						while (tmp++ < 13) {
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									player2.removeFromLayer();
								}
							});
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									player2.addToLayer();
								}
							});
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();
			}
		}
	}

	/**
	 * This method is to handle the input from keyboard and send to server to synchronize
	 * 
	 * @return Nothing.
	 * 
	 * @author Zirui Wang
	 */
	public void handleMultiPlayer() {
		if (isHost == 0) {
			player1.processInput();

			if (player1.input.isMoveUp()) {
				socketClient.sendData("01" + username);
			}
			if (player1.input.isMoveDown()) {
				socketClient.sendData("02" + username);
			}
			if (player1.input.isMoveLeft()) {
				socketClient.sendData("03" + username);
			}
			if (player1.input.isMoveRight()) {
				socketClient.sendData("04" + username);
			}
			socketClient.sendData("07" + username + "@" + score1);
		} else {
			player2.processPlayer2Input();

			if (player2.input.isMoveUp2()) {
				socketClient.sendData("01" + username);
			}
			if (player2.input.isMoveDown2()) {
				socketClient.sendData("02" + username);
			}
			if (player2.input.isMoveLeft2()) {
				socketClient.sendData("03" + username);
			}
			if (player2.input.isMoveRight2()) {
				socketClient.sendData("04" + username);
			}
			socketClient.sendData("07" + username + "@" + score2);
		}
	}

}
