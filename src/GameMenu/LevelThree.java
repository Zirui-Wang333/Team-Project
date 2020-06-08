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

public class LevelThree extends Parent {
	private Group root = new Group();

	boolean finalPlace = false;
	private Scene scene;
	private Pane playfieldLayer = new Pane();
	private Pane scoreLayer = new Pane();
	private Pane temporar = new Pane();
	private Image playerImage;
	private Image enemyImage;
	private Image background;
	private Image playerBulletImage;
	private Image enemyBullets;
	private Image Life;
	private Image powerup;
	private Stage stage;
	private List<Shot> playerBulletList = new ArrayList<>();
	private List<Singleplayer> players = new ArrayList<>();
	private List<Alien> enemies = new ArrayList<>();
	private List<Shot> enemyBulletList = new ArrayList<>();
	private boolean ok = true;
	private int count = 0;
	private int score = 0;
	private Text collisionText = new Text();
	private Text Score = new Text();
	private Text GameOver = new Text();
	private Text NextLevel = new Text();
	private boolean collision = false;
	private boolean spawn = true;
	private int number = 0;
	private int shootCount = 0;
	private int powerScore;
	private int stopLeft = 350;
	private int stopRight = 930;
	private int numberofEnemies = 0;
	private int numberofEnemies1 = 0;
	private int leftY = 150;
	private int rightY = 150;
	private int lineLeft = 0;
	private int lineRight = 0;
	private int ship = 0;
	private List<Sprite> tempListLeft = new ArrayList<>();
	private List<Sprite> tempListRight = new ArrayList<>();
	private List<PowerUp> powerups = new ArrayList<>();
	private List<Sprite> leftmoveDown = new ArrayList<>();
	private List<Sprite> rightmoveDown = new ArrayList<>();
	private List<Sprite> leftmoveRight = new ArrayList<>();
	private List<Sprite> rightmoveLeft = new ArrayList<>();
	private List<Sprite> leftmoveUp = new ArrayList<>();
	private List<Sprite> rightmoveUp = new ArrayList<>();
	private List<Sprite> leftStop = new ArrayList<>();
	private List<Sprite> rightStop = new ArrayList<>();
	private boolean permLeft = true;
	private boolean permRight = true;
	private Label label1 = new Label();

	private Text playerLifeText = new Text();
	private MultiPlayerLife life1;
	private MultiPlayerLife life2;
	private int playerLifes;

	public void LevelThree(final Stage ThirdLevel, int power, int sc, int playerLifes, int spaceshipNumber) throws IOException {
		stage = ThirdLevel;
		score = sc;
		powerScore = power;
		this.playerLifes = playerLifes;

		InputStream isbg = Files.newInputStream(Paths.get("resources/space.jpg"));
		background = new Image(isbg);
		isbg.close();
		InputStream isshot = Files.newInputStream(Paths.get("resources/shot.png"));
		playerBulletImage = new Image(isshot);
		isshot.close();
		ImageView imgView = new ImageView(background);
		imgView.setFitWidth(Settings.SCENE_WIDTH);
		imgView.setFitHeight(Settings.SCENE_HEIGHT);
		InputStream ispw = Files.newInputStream(Paths.get("resources/PowerUp.gif"));
		powerup = new Image(ispw);
		ispw.close();
		InputStream islf = Files.newInputStream(Paths.get("resources/life.png"));
		Life = new Image(islf);
		islf.close();
		root.getChildren().addAll(temporar, imgView, playfieldLayer, scoreLayer, label1);
		scene = new Scene(root, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
		InputStream isgb = Files.newInputStream(Paths.get("resources/goatbullet.png"));
		enemyBullets = new Image(isgb);
		isgb.close();
		ThirdLevel.setScene(scene);
		ship = spaceshipNumber;
		if(spaceshipNumber == 0) {
		InputStream is = Files.newInputStream(Paths.get("resources/spaceship1.png"));
		playerImage = new Image(is);
		is.close();
		}
		if(spaceshipNumber == 1) {
			InputStream is = Files.newInputStream(Paths.get("resources/ship1.png"));
			playerImage = new Image(is);
			is.close();
		}
		if(spaceshipNumber == 2) {
			InputStream is = Files.newInputStream(Paths.get("resources/ship2.png"));
			playerImage = new Image(is);
			is.close();
		}
		if(spaceshipNumber == 3) {
			InputStream is = Files.newInputStream(Paths.get("resources/ship3.png"));
			playerImage = new Image(is);
			is.close();
		}
		if(spaceshipNumber == 4) {
			InputStream is = Files.newInputStream(Paths.get("resources/ship4.png"));
			playerImage = new Image(is);
			is.close();
		}
		if(spaceshipNumber == 5) {
			InputStream is = Files.newInputStream(Paths.get("resources/ship5.png"));
			playerImage = new Image(is);
			is.close();
		}
		
		InputStream is1 = Files.newInputStream(Paths.get("resources/goat.gif"));
		enemyImage = new Image(is1);

		is1.close();
		createScoreLayer();
		createPlayers();

		AnimationTimer gameLoop = new AnimationTimer() {

			@Override
			public void handle(long now) {

			

				players.forEach(sprite -> sprite.processInput());

				if (count == 0) {
					spawnEnemies(spawn);
					number++;

				}
				count++;
				if (count == 30) // 20 -> height of the aliens
					count = 0;
				if (number == 25) { // number of aliens to spawn on each side
					spawn = false;
				}
				shootCount++;
				if (shootCount == 100) {
					SpriteShoot(enemies);
				
					shootCount = 0;

				}
				moveRight(leftmoveDown);
				moveLeft(rightmoveDown);
				moveUpLeft(leftmoveRight);
				moveUpRight(rightmoveLeft);

				leftColumn(leftmoveUp);
				rightColumn(rightmoveUp);

				players.forEach(sprite -> sprite.move());
				enemies.forEach(sprite -> sprite.move());

				powerups.forEach(sprite -> sprite.move());
				players.forEach(sprite -> spawnPrimaryWeaponObjects(sprite));
				playerBulletList.forEach(sprite -> sprite.move());
				enemyBulletList.forEach(sprite -> sprite.move());
				checkCollisions();

				powerups.forEach(sprite -> sprite.updateUI());
				players.forEach(sprite -> sprite.updateUI());
				enemies.forEach(sprite -> sprite.updateUI());

				
				playerBulletList.forEach(sprite -> sprite.updateUI());
				enemyBulletList.forEach(sprite -> sprite.updateUI());

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

	public void moveRight(List<Sprite> spriteList) {
		Iterator<Sprite> iter = spriteList.iterator();
		while (iter.hasNext()) {
			Sprite sprite = iter.next();

			if (sprite.getY() == 400) {

				sprite.setDy(0);
				sprite.setDx(2);
				leftmoveRight.add(sprite);
				iter.remove();

			}
		}
	}

	public void moveUpLeft(List<Sprite> spriteList) {

		Iterator<Sprite> iter = spriteList.iterator();

		while (iter.hasNext()) {
			Sprite sprite = iter.next();

			if (sprite.getX() == 590) {

				sprite.setDy(-2);
				sprite.setDx(0);
				leftmoveUp.add(sprite);
				iter.remove();

			}
		}
	}

	public void moveUpRight(List<Sprite> spriteList) {

		Iterator<Sprite> iter = spriteList.iterator();

		while (iter.hasNext()) {
			Sprite sprite = iter.next();

			if (sprite.getX() == 690) {
				sprite.setDx(0);
				sprite.setDy(-2);
				rightmoveUp.add(sprite);
				iter.remove();

			}
		}
	}

	public void moveLeft(List<Sprite> spriteList) {
		Iterator<Sprite> iter = spriteList.iterator();

		while (iter.hasNext()) {

			Sprite sprite = iter.next();

			if (sprite.getY() == 400) {

				sprite.setDy(0);
				sprite.setDx(-2);
				rightmoveLeft.add(sprite);
				iter.remove();

			}
		}
	}

	public void leftColumn(List<Sprite> spriteList) {
		Iterator<Sprite> iter = spriteList.iterator();

		// double y = 150.0;
		while (iter.hasNext()) {
			Sprite sprite = iter.next();
			if (sprite.getY() == leftY) {
				sprite.setDy(0);
				sprite.setDx(-2);
				if (permLeft) {
					tempListLeft.add(sprite);
					permLeft = false;
				}

				leftStop.add(sprite);
				numberofEnemies++;
				if (tempListLeft.get(lineLeft).getX() == stopLeft) {
					tempListLeft.get(lineLeft).stopMovement();
				}

				if (numberofEnemies >= 2) {
					if (leftStop.get(numberofEnemies - 1).collidesWith(leftStop.get(numberofEnemies - 2)))
						leftStop.get(numberofEnemies - 1).stopMovement();
				}

				if (numberofEnemies == 5) {
					numberofEnemies = 0;
					leftStop.forEach(sprite1 -> sprite1.stopMovement());
					leftStop.clear();
					leftY += 50;
					permLeft = true;
					lineLeft++;
				}
				iter.remove();

			}
		}
		if (!leftStop.isEmpty() && lineLeft != 0) {
			if (leftStop.get(0).getX() == 350)
				leftStop.forEach(sprite -> sprite.stopMovement());
		}

	}

	public void rightColumn(List<Sprite> spriteList) {
		Iterator<Sprite> iter = spriteList.iterator();

		while (iter.hasNext()) {
			Sprite sprite = iter.next();
			if (sprite.getY() == rightY) {
				sprite.setDy(0);
				sprite.setDx(2);
				if (permRight) {
					tempListRight.add(sprite);
					permRight = false;
				}

				rightStop.add(sprite);
				numberofEnemies1++;
				if (tempListRight.get(lineRight).getX() == stopRight) {
					tempListRight.get(lineRight).stopMovement();
				}

				if (numberofEnemies1 >= 2) {
					if (rightStop.get(numberofEnemies1 - 1).collidesWith(rightStop.get(numberofEnemies1 - 2)))
						rightStop.get(numberofEnemies1 - 1).stopMovement();
				}

				if (numberofEnemies1 == 5) {
					numberofEnemies1 = 0;
					rightStop.forEach(sprite1 -> sprite1.stopMovement());
					rightStop.clear();
					rightY += 50;
					permRight = true;
					lineRight++;
				}
				iter.remove();

			}
		}
		if (!rightStop.isEmpty() && lineRight != 0) {
			if (rightStop.get(0).getX() == 930)
				rightStop.forEach(sprite -> sprite.stopMovement());
		}
	}

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

	public void createPlayers() {

		Input input = new Input(scene);

		input.addListeners();

		Image image = playerImage;

		double x = (Settings.SCENE_WIDTH - image.getWidth()) / 2.0;
		double y = Settings.SCENE_HEIGHT * 0.7;

		Singleplayer player = new Singleplayer(playfieldLayer, image, x, y, 0, 0, 0, 0, Settings.PLAYER_SHIP_HEALTH, 1,
				Settings.PLAYER_SHIP_SPEED, input, x, y);

		player.setLife(playerLifes);
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

	public void spawnEnemies(boolean random) {

		if (!random) {
			return;
		}

		Image image = enemyImage;


		double xleft = 200.0;
		double yleft = 0;
		double xright = 1080.0;
		double yright = 0;

		var alienleft = new Alien(playfieldLayer, image, xleft, yleft, 0, 0, 2, 0, 12.0, 1, 0, 0);
		var alienright = new Alien(playfieldLayer, image, xright, yright, 0, 0, 2, 0, 12.0, 1, 0, 0);
		leftmoveDown.add(alienleft);
		rightmoveDown.add(alienright);
		enemies.add(alienleft);
		enemies.add(alienright);

	}

	private void spawnPrimaryWeaponObjects(Singleplayer player) {

		player.chargePrimaryWeapon();
		Shot playerBullet;

		Image image = playerBulletImage;

		double x = player.getPrimaryWeaponX() - image.getWidth() / 2.0;
		double y = player.getPrimaryWeaponY() - 15;

		double spread = player.getPrimaryWeaponBulletSpread();
		double count = player.getPrimaryWeaponBulletCount();
		double speed = player.getPrimaryWeaponBulletSpeed();

		if (player.isFirePrimaryWeapon() && powerScore == 1) {

			playerBullet = new Shot(playfieldLayer, image, x, y, 0, -speed, 0, 0);
			playerBulletList.add(playerBullet);

			player.unchargePrimaryWeapon();
		}
		if (player.isFirePrimaryWeapon() && powerScore == 2) {

			playerBullet = new Shot(playfieldLayer, image, x - 5, y, 0, -speed, 0, 0);
			playerBulletList.add(playerBullet);
			playerBullet = new Shot(playfieldLayer, image, x + 5, y, 0, -speed, 0, 0);
			playerBulletList.add(playerBullet);
			player.unchargePrimaryWeapon();
		}
		if (player.isFirePrimaryWeapon() && powerScore == 3) {

			playerBullet = new Shot(playfieldLayer, image, x - 10, y, 0, -speed, 0, 0);
			playerBulletList.add(playerBullet);
			playerBullet = new Shot(playfieldLayer, image, x, y, 0, -speed, 0, 0);
			playerBulletList.add(playerBullet);
			playerBullet = new Shot(playfieldLayer, image, x + 10, y, 0, -speed, 0, 0);
			playerBulletList.add(playerBullet);
			player.unchargePrimaryWeapon();
		}
		if (player.isFirePrimaryWeapon() && powerScore == 4) {

			playerBullet = new Shot(playfieldLayer, image, x - 10, y, 0, -speed, 0, 0);
			playerBulletList.add(playerBullet);
			playerBullet = new Shot(playfieldLayer, image, x, y, 0, -speed, 0, 0);
			playerBulletList.add(playerBullet);
			playerBullet = new Shot(playfieldLayer, image, x + 10, y, 0, -speed, 0, 0);
			playerBulletList.add(playerBullet);
			playerBullet = new Shot(playfieldLayer, image, x, y - 10, 0, -speed, 0, 0);
			playerBulletList.add(playerBullet);
			player.unchargePrimaryWeapon();
		}
		if (player.isFirePrimaryWeapon() && powerScore >= 5) {

			playerBullet = new Shot(playfieldLayer, image, x, y, 0, -speed, 0, 0);
			playerBulletList.add(playerBullet);

			for (int i = 0; i < count / 2.0; i++) {

				// left
				playerBullet = new Shot(playfieldLayer, image, x - 5 * i, y, -spread * i, -speed, 0, 0);
				playerBullet.setR(-5 * i);
				playerBulletList.add(playerBullet);

				// right
				playerBullet = new Shot(playfieldLayer, image, x + 5 * i, y, spread * i, -speed, 0, 0);
				playerBullet.setR(5 * i);
				playerBulletList.add(playerBullet);

			}
			player.unchargePrimaryWeapon();
		}
		
	}

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

	public void checkCollisions() {

		collision = false;

		if (players.get(0).getInvincible() == false) {
			for (Singleplayer player : players) {
				for (Alien enemy : enemies) {
					if (player.collidesWith(enemy)) {
						collision = true;
					}
				}
			}

			for (Singleplayer player : players) {
				for (Shot shot : enemyBulletList) {
					if (shot.collidesWith(player)) {
						shot.stopMovement();
						shot.removeFromLayer();
						shot.remove();
						shot.setX(0);
						
							collision = true;
							checkLife();
						
					}
				}
			}
		}

		for (Singleplayer player : players) {
			for (Shot shot : playerBulletList) {
				for (Alien enemy : enemies) {
					if (shot.collidesWith(enemy)) {
						shot.stopMovement();
						shot.removeFromLayer();
						shot.remove();
						shot.setX(0);
						enemy.getDamagedBy(player);
						if (enemy.getHealth() <= 0.0) {
							enemy.setRemovable(true);

						}

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
		for (Alien alien : enemies) {
			if (alien.getX() >= 1280 || alien.getX() <= -20)
				alien.setRemovable(true);
			
		}
	}

	public void updateScore() {
		Score.setText(Integer.toString(score));

	}

	public void gameOver() {
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
			playerBulletList.forEach(sprite -> sprite.removeFromLayer());
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
			playerBulletList.forEach(sprite -> sprite.removeFromLayer());
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

						// Stage sb = (Stage)label1.getScene().getWindow();
						// sb.close();
						LevelFour start = new LevelFour();
						try {
							start.LevelFour(stage, powerScore, score, players.get(0).getLife(), ship);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
		}

	}

	public void SpriteShoot(List<? extends Sprite> spriteList) {
		Iterator<? extends Sprite> i = spriteList.iterator();
		Random rnd = new Random();
		int mark = rnd.nextInt(25);
		int temp = 0;
		while (i.hasNext()) {
			Sprite sprite = i.next();

			temp++;
			if (temp == mark) {

				Shot enemyBullet;

				Image image = enemyBullets;

				double x = sprite.getCenterX() - 10;
				double y = sprite.getY() + 25;
				double speed = 4.0;

				enemyBullet = new Shot(playfieldLayer, image, x, y, 0, speed, 0, 0);
				enemyBulletList.add(enemyBullet);

			}
		}
	}

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
				Image image = playerImage;
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

}
