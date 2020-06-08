package GameMenu;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import GameMenu.sprite.Singleplayer;
import GameMenu.sprite.Alien;
import GameMenu.sprite.Input;
import GameMenu.sprite.Sprite;
import GameMenu.Settings;
import GameMenu.sprite.Shot;
import GameMenu.sprite.PowerUp;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import javafx.scene.paint.Color;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;

public class StartGame extends Parent {
	private Group root = new Group();
	private Stage stage;

	private Random rnd = new Random();
	private Scene scene;
	private Pane playfieldLayer = new Pane();
	private Pane scoreLayer = new Pane();
	private Image SingleplayerImage;
	private Image enemyImage;
	private Image background;
	private Image SingleplayerBulletImage;
	private Image powerup;
	private Image Life;
	private List<PowerUp> powerups = new ArrayList<>();
	private List<Shot> SingleplayerBulletList = new ArrayList<>();
	private List<Singleplayer> players = new ArrayList<>();
	private List<Alien> enemies = new ArrayList<>();

	private int k = 0;
	private boolean ok = true;
	private int count = 0;
	private int score = 0;
	private int powerScore = 1;
	private int ship = 0;
	private Text collisionText = new Text();
	private Text Score = new Text();
	private Text GameOver = new Text();
	private Text NextLevel = new Text();
	private boolean collision = false;
	private Label label1 = new Label();

	private MultiPlayerLife life1;
	private MultiPlayerLife life2;

	private Text playerLifeText = new Text();

	public void LevelOne(Stage SecondaryStage, int spaceshipNumber) throws IOException {
		stage = SecondaryStage;
		
		InputStream isbg = Files.newInputStream(Paths.get("resources/space.jpg"));
		background = new Image(isbg);
		isbg.close();
		
		InputStream isshot = Files.newInputStream(Paths.get("resources/shot.png"));
		SingleplayerBulletImage = new Image(isshot);
		isshot.close();
		
		ImageView imgView = new ImageView(background);
		imgView.setFitWidth(Settings.SCENE_WIDTH);
		imgView.setFitHeight(Settings.SCENE_HEIGHT);
		
		InputStream ispw = Files.newInputStream(Paths.get("resources/PowerUp.gif"));
		powerup = new Image(ispw);
		ispw.close();

		root.getChildren().addAll(imgView, playfieldLayer, scoreLayer, label1);

		scene = new Scene(root, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);

		SecondaryStage.setScene(scene);
		
		InputStream islf = Files.newInputStream(Paths.get("resources/life.png"));
		Life = new Image(islf);
		islf.close();
		
		ship = spaceshipNumber;
		if(spaceshipNumber == 0) {  //choose the selected ship in the customize menu
		InputStream is = Files.newInputStream(Paths.get("resources/spaceship1.png"));
		SingleplayerImage = new Image(is);
		is.close();
		}
		if(spaceshipNumber == 1) {
			InputStream is = Files.newInputStream(Paths.get("resources/ship1.png"));
			SingleplayerImage = new Image(is);
			is.close();
		}
		if(spaceshipNumber == 2) {
			InputStream is = Files.newInputStream(Paths.get("resources/ship2.png"));
			SingleplayerImage = new Image(is);
			is.close();
		}
		if(spaceshipNumber == 3) {
			InputStream is = Files.newInputStream(Paths.get("resources/ship3.png"));
			SingleplayerImage = new Image(is);
			is.close();
		}
		if(spaceshipNumber == 4) {
			InputStream is = Files.newInputStream(Paths.get("resources/ship4.png"));
			SingleplayerImage = new Image(is);
			is.close();
		}
		if(spaceshipNumber == 5) {
			InputStream is = Files.newInputStream(Paths.get("resources/ship5.png"));
			SingleplayerImage = new Image(is);
			is.close();
		}
		
		InputStream is1 = Files.newInputStream(Paths.get("resources/goat.gif"));
		enemyImage = new Image(is1);
		is1.close();
		
		createScoreLayer();
		createSingleplayers();
		
		/**
		 * Here starts the animation loop which is the main part of every level. This part contains the movement of the players and enemies,
		 * and also the updates of the UI to have smooth transitions when they move
		 */

		AnimationTimer gameLoop = new AnimationTimer() {

			@Override
			public void handle(long now) {

				
				players.forEach(sprite -> sprite.processInput());

				spawnEnemies(true);
				if (ok && count == 0)
					moveSprites(enemies);
				count++;
				
				if (count == 100) {
					relocateSprites(enemies);
					count = 0;
				}

				players.forEach(sprite -> sprite.move());
				enemies.forEach(sprite -> sprite.move());
				powerups.forEach(sprite -> sprite.move());
				players.forEach(sprite -> spawnPrimaryWeaponObjects(sprite));
				SingleplayerBulletList.forEach(sprite -> sprite.move());
				checkCollisions();

				players.forEach(sprite -> sprite.updateUI());
				enemies.forEach(sprite -> sprite.updateUI());
				powerups.forEach(sprite -> sprite.updateUI());
				SingleplayerBulletList.forEach(sprite -> sprite.updateUI());
				enemies.forEach(sprite -> sprite.checkRemovability());

				removeSprites(enemies);
				removePowerups(powerups);

				updateScore();
				if (ok == false)
					this.stop();
//				gameOver();
				checkLife();
			}
		};
		gameLoop.start();
		scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent t) {
				if (t.getCode() == KeyCode.F9) {
					enemies.clear();

				}
			}
		});
	}
	/**
	 * This method just moves back and forth the enemies in this level
	 * @param spriteList
	 */

	public void relocateSprites(List<? extends Sprite> spriteList) {
		Iterator<? extends Sprite> iter = spriteList.iterator();
		while (iter.hasNext()) {
			Sprite sprite = iter.next();
			if (sprite.getDx() != 0) {
				sprite.setDx(-sprite.getDx());
				sprite.setDy(-sprite.getDy());

				// sprite.updateUI();
			}
		}
	}
	/**
	 * This method checks to see if every condition to advance to the next level has been met and also check if the player is dead and issue
	 * a game over screen
	 */
	private void gameOver() {
		if (collision == true) {
			GameOver.setFont(Font.font(null, FontWeight.BOLD, 64));
			GameOver.setStroke(Color.BLACK);
			GameOver.setFill(Color.RED);
			GameOver.setText("GAME OVER");
			GameOver.setVisible(true);
			double x = (Settings.SCENE_WIDTH - GameOver.getBoundsInLocal().getWidth()) / 2;
			double y = (Settings.SCENE_HEIGHT - GameOver.getBoundsInLocal().getHeight()) / 2;
			GameOver.relocate(x, y);
			players.forEach(sprite -> sprite.removeFromLayer());
			enemies.forEach(sprite -> sprite.removeFromLayer());
			SingleplayerBulletList.forEach(sprite -> sprite.removeFromLayer());
			ok = false;

		}
		if (enemies.isEmpty()) {

			GameOver.setFont(Font.font(null, FontWeight.BOLD, 64));
			GameOver.setStroke(Color.BLACK);
			GameOver.setFill(Color.RED);
			GameOver.setText("Level completed!");
			GameOver.setVisible(true);
			double x = (Settings.SCENE_WIDTH - GameOver.getBoundsInLocal().getWidth()) / 2;
			double y = (Settings.SCENE_HEIGHT - GameOver.getBoundsInLocal().getHeight()) / 2;
			GameOver.relocate(x, y);
			players.forEach(sprite -> sprite.removeFromLayer());
			enemies.forEach(sprite -> sprite.removeFromLayer());
			SingleplayerBulletList.forEach(sprite -> sprite.removeFromLayer());
			ok = false;
			NextLevel.setFont(Font.font(null, FontWeight.BOLD, 32));
			NextLevel.setStroke(Color.BLACK);
			NextLevel.setFill(Color.RED);
			NextLevel.setText("Press Enter to continue");
			NextLevel.relocate(x + 75, y + 150);
			NextLevel.setVisible(true);
			scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent t) {
					if (t.getCode() == KeyCode.ENTER) {

						/*
						 * Stage sb = (Stage)label1.getScene().getWindow(); sb.close();
						 */
						LevelTwo start = new LevelTwo();
						try {
							start.LevelTwo(stage, powerScore, score, players.get(0).getLife(), ship);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});

		}

	}
	
	/**
	 * This method is used to set up the parameters for the score text, collision text
	 */

	public void createScoreLayer() {

		collisionText.setFont(Font.font(null, FontWeight.BOLD, 64));
		collisionText.setStroke(Color.BLACK);
		collisionText.setFill(Color.RED);

		Score.setFont(Font.font(null, FontWeight.BOLD, 25));
		Score.setStroke(Color.BLACK);
		Score.setFill(Color.RED);

		scoreLayer.getChildren().addAll(collisionText, Score, GameOver, NextLevel, playerLifeText);

		collisionText.setText("Collision");
		double x = (Settings.SCENE_WIDTH - collisionText.getBoundsInLocal().getWidth()) / 2;
		double y = (Settings.SCENE_HEIGHT - collisionText.getBoundsInLocal().getHeight()) / 2;
		collisionText.relocate(x, y);
		Score.relocate(0, 0);
		collisionText.setText("");

		collisionText.setBoundsType(TextBoundsType.VISUAL);

	}
	
	/**
	 * This method, like the enemies one, spawns a single instance of the player on the screen and adds listeners for keyboard presses
	 */

	public void createSingleplayers() {

		Input input = new Input(scene);

		input.addListeners();

		Image image = SingleplayerImage;

		double x = (Settings.SCENE_WIDTH - image.getWidth()) / 2.0;
		double y = Settings.SCENE_HEIGHT * 0.7;

		Singleplayer player = new Singleplayer(playfieldLayer, image, x, y, 0, 0, 0, 0, Settings.PLAYER_SHIP_HEALTH, 1,
				Settings.PLAYER_SHIP_SPEED, input, x, y);

		player.setLife(2);
		player.setInvincible(false);

		life1 = new MultiPlayerLife(playfieldLayer, Life, 60, Settings.SCENE_HEIGHT - Life.getHeight() - 5);
		life2 = new MultiPlayerLife(playfieldLayer, Life, 95, Settings.SCENE_HEIGHT - Life.getHeight() - 5);

		life1.addToLayer();
		life2.addToLayer();

		players.add(player);
		
		playerLifeText.setFont(Font.font(null, FontWeight.BOLD, 25));
		playerLifeText.setStroke(Color.BLACK);
		playerLifeText.setFill(Color.GOLD);
		playerLifeText.setText("Life: ");
		playerLifeText.setVisible(true);
		playerLifeText.relocate(0, Settings.SCENE_HEIGHT - 30);

	}
	
	/**
	 * This method checks the number of lives the player has and also is responsible for the "taking damage" animation and game over screen
	 */

	public void checkLife() {
		if (players.get(0).getLife() <= -1 || enemies.isEmpty()) {
			gameOver();
		}
		if (players.get(0).getLife() == 0) {
			life1.isVisible(false);
			life2.isVisible(false);
		}
		if (players.get(0).getLife() == 1) {
			life1.isVisible(true);
			life2.isVisible(false);
		}
		if (players.get(0).getLife() == 2) {
			life1.isVisible(true);
			life2.isVisible(true);
		}
		if (collision == true) {
			players.get(0).setLife(players.get(0).getLife() - 1);
			if (players.get(0).getLife() <= -1) {
				gameOver();
			} else {
				collision = false;
				powerScore = 1;
				Image image = SingleplayerImage;
				double x = (Settings.SCENE_WIDTH - image.getWidth()) / 2.0;
				double y = Settings.SCENE_HEIGHT * 0.7;
				players.get(0).setX(x);
				players.get(0).setY(y);
				players.get(0).setInvincible(true);
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(3000);
							players.get(0).setInvincible(false);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
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
									players.get(0).removeFromLayer();
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
									players.get(0).addToLayer();
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
	 * This method is the main way through which enemies spawn. Used in every other classes
	 * @param random determines whether the enemies can spawn or not
	 */

	public void spawnEnemies(boolean random) {

		if (random && k != 0) {
			return;
		}

		Image image = enemyImage;

		double speed = rnd.nextDouble() * 1.0 + 2.0;

		double x = rnd.nextDouble() * (Settings.SCENE_WIDTH - image.getWidth());
		double y = -image.getHeight();

		// Alien enemy = new Alien( playfieldLayer, image, x, y, 0, 0, speed, 0, 1,1);
		enemies = new ArrayList<>();

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 6; j++) {

				var alien = new Alien(playfieldLayer, image, 490 + 50 * j, Commons.ALIEN_INIT_Y + 50 * i, 0, 0, 0, 0, 2,
						1, 410 + 18 * j, Commons.ALIEN_INIT_Y + 18 * i);
				alien.stopMovement();
				enemies.add(alien);
			}
		}
		k = 1;

	}
	/**
	 * This method is used to spawn and determine which type of weapon the player has. It is also used in every single level and is the same.
	 * @param Singleplayer
	 */

	private void spawnPrimaryWeaponObjects(Singleplayer Singleplayer) {

		Singleplayer.chargePrimaryWeapon();

		Shot SingleplayerBullet;

		Image image = SingleplayerBulletImage;

		double x = Singleplayer.getPrimaryWeaponX() - image.getWidth() / 2.0;
		double y = Singleplayer.getPrimaryWeaponY() - 15;

		double spread = Singleplayer.getPrimaryWeaponBulletSpread();
		double count = Singleplayer.getPrimaryWeaponBulletCount();
		double speed = Singleplayer.getPrimaryWeaponBulletSpeed();

		if (Singleplayer.isFirePrimaryWeapon() && powerScore == 1) {

			SingleplayerBullet = new Shot(playfieldLayer, image, x, y, 0, -speed, 0, 0);
			SingleplayerBulletList.add(SingleplayerBullet);

			Singleplayer.unchargePrimaryWeapon();
		}
		if (Singleplayer.isFirePrimaryWeapon() && powerScore == 2) {

			SingleplayerBullet = new Shot(playfieldLayer, image, x - 5, y, 0, -speed, 0, 0);
			SingleplayerBulletList.add(SingleplayerBullet);
			SingleplayerBullet = new Shot(playfieldLayer, image, x + 5, y, 0, -speed, 0, 0);
			SingleplayerBulletList.add(SingleplayerBullet);
			Singleplayer.unchargePrimaryWeapon();
		}
		if (Singleplayer.isFirePrimaryWeapon() && powerScore == 3) {

			SingleplayerBullet = new Shot(playfieldLayer, image, x - 10, y, 0, -speed, 0, 0);
			SingleplayerBulletList.add(SingleplayerBullet);
			SingleplayerBullet = new Shot(playfieldLayer, image, x, y, 0, -speed, 0, 0);
			SingleplayerBulletList.add(SingleplayerBullet);
			SingleplayerBullet = new Shot(playfieldLayer, image, x + 10, y, 0, -speed, 0, 0);
			SingleplayerBulletList.add(SingleplayerBullet);
			Singleplayer.unchargePrimaryWeapon();
		}
		if (Singleplayer.isFirePrimaryWeapon() && powerScore == 4) {

			SingleplayerBullet = new Shot(playfieldLayer, image, x - 10, y, 0, -speed, 0, 0);
			SingleplayerBulletList.add(SingleplayerBullet);
			SingleplayerBullet = new Shot(playfieldLayer, image, x, y, 0, -speed, 0, 0);
			SingleplayerBulletList.add(SingleplayerBullet);
			SingleplayerBullet = new Shot(playfieldLayer, image, x + 10, y, 0, -speed, 0, 0);
			SingleplayerBulletList.add(SingleplayerBullet);
			SingleplayerBullet = new Shot(playfieldLayer, image, x, y - 10, 0, -speed, 0, 0);
			SingleplayerBulletList.add(SingleplayerBullet);
			Singleplayer.unchargePrimaryWeapon();
		}
		if (Singleplayer.isFirePrimaryWeapon() && powerScore >= 5) {

			SingleplayerBullet = new Shot(playfieldLayer, image, x, y, 0, -speed, 0, 0);
			SingleplayerBulletList.add(SingleplayerBullet);

			for (int i = 0; i < count / 2.0; i++) {

				// left
				SingleplayerBullet = new Shot(playfieldLayer, image, x - 5 * i, y, -spread * i, -speed, 0, 0);
				SingleplayerBullet.setR(-5 * i);
				SingleplayerBulletList.add(SingleplayerBullet);

				// right
				SingleplayerBullet = new Shot(playfieldLayer, image, x + 5 * i, y, spread * i, -speed, 0, 0);
				SingleplayerBullet.setR(5 * i);
				SingleplayerBulletList.add(SingleplayerBullet);

			}
			Singleplayer.unchargePrimaryWeapon();
		}
		
	}

	/**
	 * This method receives the list of enemies and checks if they are removable, or having their health below 0. It also is responsible for the 
	 * 10% chance that every enemy can drop a power-up
	 * @param spriteList
	 */
	public void removeSprites(List<? extends Sprite> spriteList) {
		Iterator<? extends Sprite> iter = spriteList.iterator();
		PowerUp powerUp;
		Image pwimg = powerup;
		while (iter.hasNext()) {
			Sprite sprite = iter.next();

			if (sprite.isRemovable()) {
				Random rnd = new Random();
				int plm = 0;
				plm = rnd.nextInt(100);
				if (plm > 90) {
					powerUp = new PowerUp(playfieldLayer, pwimg, sprite.getCenterX(), sprite.getCenterY(), 0, 0, 2.5, 0,
							0, 0, 0, 0);
					powerups.add(powerUp);
				}
				score += 10;
				sprite.removeFromLayer();

				iter.remove();
			}
		}
	}
	/**
	 * Simple method that receives the list of powerups and removes them from the screen if they are not picked up by the player
	 * @param spriteList
	 */

	public void removePowerups(List<? extends Sprite> spriteList) {
		Iterator<? extends Sprite> iter = spriteList.iterator();
		while (iter.hasNext()) {
			Sprite sprite = iter.next();
			if (sprite.isRemovable()) {
				sprite.removeFromLayer();
				iter.remove();
			}
		}
	}
	
	/**
	 * This method receives the enemies list and picks one randomly and start moving it at a random speed 
	 * @param spriteList
	 */

	public void moveSprites(List<? extends Sprite> spriteList) {
		Iterator<? extends Sprite> i = spriteList.iterator();
		Random rnd = new Random();
		int mark = rnd.nextInt(100);
		int temp = 0;
		while (i.hasNext()) {
			Sprite sprite = i.next();

			temp++;
			if (temp == mark) {

				int randomX = 0;
				while (randomX == 0) {

					randomX = ThreadLocalRandom.current().nextInt(-2, 2);
				}
				int randomY = 0;
				while (randomY == 0) {

					randomY = ThreadLocalRandom.current().nextInt(0, 2);
				}

				sprite.setDx(randomX);
				sprite.setDy(randomY);
				sprite.startMovement();

			}
		}
	}
	
	/**
	 * This method runs every frame and is responsible for collisions between the player and enemies, player and enemy projectiles
	 * player and power ups and also player bullets and enemies. Same as the other ones in all the levels
	 */

	public void checkCollisions() {

		collision = false;

		if (players.get(0).getInvincible() == false) {
			if (players.get(0).getInvincible() == false) {
				for (Singleplayer player : players) {
					for (Alien enemy : enemies) {
						if (player.collidesWith(enemy)) {
							collision = true;
						}
					}
				}
			}
		}
			for (Singleplayer player : players) {
				for (Alien enemy : enemies) {
					for (Shot shot : SingleplayerBulletList) {
						if (shot.collidesWith(enemy)) {
							shot.stopMovement();
							shot.removeFromLayer();
							shot.remove();
							shot.setX(0);
							enemy.getDamagedBy(player);
							if (enemy.getHealth() <= 0.0)
								enemy.setRemovable(true);
							
						}
					}
				}
			}
		for (Singleplayer player : players) {
			for (PowerUp powerup : powerups) {
				if (player.collidesWith(powerup)) {
					powerScore++;
					score += 50;
					powerup.remove();
				}
			}
		}
	}

	public void updateScore() {
		Score.setText(Integer.toString(score));

	}
}
