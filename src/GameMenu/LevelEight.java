package GameMenu;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import GameMenu.sprite.Singleplayer;
import GameMenu.sprite.Alien;
import GameMenu.sprite.Input;
import GameMenu.sprite.Sprite;
import GameMenu.Settings;
import GameMenu.sprite.Shot;
import GameMenu.sprite.PowerUp;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LevelEight extends Parent {
	private Group root = new Group();

	
	private Scene scene;
	private Pane playfieldLayer = new Pane();
	private Pane scoreLayer = new Pane();
	private Pane temporar = new Pane();
	private Image playerImage;
	private Image enemyImage;
	private Image background;
	private Image playerBulletImage;
	private Image powerup;
	private Image enemyBullets;
	private Image Life;
	private Stage stage;
	private List<Shot> playerBulletList = new ArrayList<>();
	private List<Singleplayer> players = new ArrayList<>();
	private List<Shot> enemyBulletList = new ArrayList<>();
	public int k = 0;
	public boolean ok = true;
	public int count = 0;
	public int score = 0;
	private int ship = 0;
	private Text collisionText = new Text();
	private Text Score = new Text();
	private Text GameOver = new Text();
	private Text NextLevel = new Text();
	private boolean collision = false;
	private int number = 0;
	private int shootCount = 0;
	private int powerScore;
	private int count1 = 0;
	private int count2 = 0;
	private List<Alien> enemies1 = new ArrayList<>();
	private List<Alien> enemies2 = new ArrayList<>();
	private List<PowerUp> powerups = new ArrayList<>();
	private List<Button> buttonList = new ArrayList<>();
	private List<Button> buttonList2 = new ArrayList<>();
	private boolean spawn1 = true;
	private boolean spawn2 = true;
	private int number2 = 0;
	private Path triangle1 = new Path(new MoveTo(640.0, 100.0), new LineTo(890.0, 450.0), new LineTo(390.0, 450.0),
			new LineTo(640.0, 100.0));
	private Path triangle2 = new Path(new MoveTo(880, 200), new LineTo(640, 550), new LineTo(400, 200),
			new LineTo(880, 200));

	private Label label1 = new Label();

	private Text playerLifeText = new Text();
	private MultiPlayerLife life1;
	private MultiPlayerLife life2;
	private int playerLifes;

	public void LevelEight(final Stage ThirdLevel, int power, int sc, int playerLifes,int spaceshipNumber) throws IOException {
		score = sc;
		this.playerLifes = playerLifes;
		stage = ThirdLevel;
		powerScore = power;
		
		InputStream isbg = Files.newInputStream(Paths.get("resources/space.jpg"));
		background = new Image(isbg);
		isbg.close();
		
		InputStream isshot = Files.newInputStream(Paths.get("resources/shot.png"));
		playerBulletImage = new Image(isshot);
		isshot.close();
		
		InputStream islf = Files.newInputStream(Paths.get("resources/life.png"));
		Life = new Image(islf);
		islf.close();
		
		ImageView imgView = new ImageView(background);
		imgView.setFitWidth(Settings.SCENE_WIDTH);
		imgView.setFitHeight(Settings.SCENE_HEIGHT);
		
		InputStream ispw = Files.newInputStream(Paths.get("resources/PowerUp.gif"));
		powerup = new Image(ispw);
		ispw.close();
		
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

				if (count1 == 0 && spawn1) {
					Button button = new Button();
					button.setLayoutX(0);
					button.setLayoutY(0);
					button.setVisible(true);
					buttonList.add(button);
				
					moveTriangle1(button, spawn1);
					spawnEnemies(spawn1);
					number++;

				}
				if (count2 == 0 && spawn2) {
					Button button = new Button();
					button.setLayoutX(0);
					button.setLayoutY(0);
					buttonList2.add(button);
					moveTriangle2(button, spawn2);
					spawnEnemies2(spawn2);
					number2++;

				}
				count1++;
				count2++;
				getCoords(buttonList);
				getCoords2(buttonList2);
				if (count1 == 28) // 20 -> height of the aliens
					count1 = 0;
				if (number == 21) { // number of aliens to spawn on each side
					spawn1 = false;
				}
				if (count2 == 28)
					count2 = 0;
				if (number2 == 21)
					spawn2 = false;
				shootCount++;
				if (shootCount == 100) {
					SpriteShoot(enemies1);
					SpriteShoot(enemies2);
					// SpriteShoot(enemiesRight);
					shootCount = 0;

				}

				players.forEach(sprite -> sprite.move());
				enemies1.forEach(sprite -> sprite.move());
				enemies2.forEach(sprite -> sprite.move());

			
				powerups.forEach(sprite -> sprite.move());
				players.forEach(sprite -> spawnPrimaryWeaponObjects(sprite));
				playerBulletList.forEach(sprite -> sprite.move());
				enemyBulletList.forEach(sprite -> sprite.move());
				checkCollisions();

				powerups.forEach(sprite -> sprite.updateUI());
				players.forEach(sprite -> sprite.updateUI());
				enemies1.forEach(sprite -> sprite.updateUI());
				enemies2.forEach(sprite -> sprite.updateUI());

			
				playerBulletList.forEach(sprite -> sprite.updateUI());
				enemyBulletList.forEach(sprite -> sprite.updateUI());

				removeSprites(enemies1);
				removeSprites2(enemies2);

				removePowerups(powerups);
				removePowerups(enemyBulletList);
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
					LevelNine start = new LevelNine();
					try {
						start.LevelNine(stage, powerScore, score, 2,ship);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void moveTriangle1(Button button, boolean spawn) {

		if (!spawn)
			return;

		PathTransition transition = new PathTransition();
		transition.setNode(button);

		transition.setDuration(Duration.seconds(10));
		transition.setPath(triangle1);

		
		transition.setCycleCount(PathTransition.INDEFINITE);
		transition.setInterpolator(Interpolator.LINEAR);
		transition.play();

	}

	private void moveTriangle2(Button button, boolean spawn) {

		if (!spawn)
			return;

		PathTransition transition = new PathTransition();
		transition.setNode(button);

		transition.setDuration(Duration.seconds(10));
		transition.setPath(triangle2);

		
		transition.setCycleCount(PathTransition.INDEFINITE);
		transition.setInterpolator(Interpolator.LINEAR);
		transition.play();

	}

	private void getCoords(List<Button> buttons) {
		Iterator<Button> iter = buttons.iterator();
		int index = 0;
		while (iter.hasNext()) {
			Button button = iter.next();
			Bounds boundsInScene = button.localToScene(button.getBoundsInLocal());
			button.setVisible(true);
			if (boundsInScene.getCenterX() != 0 && boundsInScene.getCenterY() != 0) {
				Sprite sprite = enemies1.get(index);
				sprite.setX(boundsInScene.getCenterX());
				sprite.setY(boundsInScene.getCenterY());
				// sprite.setR(sprite.getR()+0.75);
			}
			index++;
		}
	}

	private void getCoords2(List<Button> buttons) {
		Iterator<Button> iter = buttons.iterator();
		int index = 0;
		while (iter.hasNext()) {
			Button button = iter.next();
			Bounds boundsInScene = button.localToScene(button.getBoundsInLocal());
			button.setVisible(true);
			if (boundsInScene.getCenterX() != 0 && boundsInScene.getCenterY() != 0) {
				Sprite sprite = enemies2.get(index);
				sprite.setX(boundsInScene.getCenterX());
				sprite.setY(boundsInScene.getCenterY());
				// sprite.setR(sprite.getR()+0.75);
			}
			index++;
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

		double x = -100;
		double y = -100;

		var alien = new Alien(playfieldLayer, enemyImage, x, y, 0, 0, 1, 0, 20, 1, 0, 0);
		enemies1.add(alien);

	}

	public void spawnEnemies2(boolean random) {

		if (!random) {
			return;
		}

		double x = -100;
		double y = -100;

		var alien = new Alien(playfieldLayer, enemyImage, x, y, 0, 0, 1, 0, 20, 1, 0, 0);
		enemies2.add(alien);

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
		int index = 0;
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
				buttonList.remove(index);
				iter.remove();

			}
			index++;
		}
	}

	public void removeSprites2(List<? extends Sprite> spriteList) {
		Iterator<? extends Sprite> iter = spriteList.iterator();
		PowerUp powerUp;
		Image pwimg = powerup;
		int index = 0;
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
				buttonList2.remove(index);
				iter.remove();

			}
			index++;
		}
	}

	public void checkCollisions() {

		collision = false;

		if (players.get(0).getInvincible() == false) {
			for (Singleplayer player : players) {
				for (Alien enemy : enemies1) {
					if (player.collidesWith(enemy)) {
						collision = true;
					}
				}
			}
			for (Singleplayer player : players) {
				for (Alien enemy : enemies2) {
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
//							gameOver();
							checkLife();
						
					}
				}
			}
		}

		for (Singleplayer player : players) {
			for (Shot shot : playerBulletList) {
				for (Alien enemy : enemies1) {
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
			for (Shot shot : playerBulletList) {
				for (Alien enemy : enemies2) {
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
			enemies1.forEach(sprite -> sprite.removeFromLayer());
			enemies2.forEach(sprite -> sprite.removeFromLayer());
			playerBulletList.forEach(sprite -> sprite.removeFromLayer());
			ok = false;

		}
		if (enemies1.isEmpty() && enemies2.isEmpty()) {
			GameOver.setFont(Font.font(null, FontWeight.BOLD, 64));
			GameOver.setStroke(Color.BLACK);
			GameOver.setFill(Color.RED);
			GameOver.setText("Level completed!");
			GameOver.setVisible(true);
			double x = (Settings.SCENE_WIDTH - GameOver.getBoundsInLocal().getWidth()) / 2;
			double y = (Settings.SCENE_HEIGHT - GameOver.getBoundsInLocal().getHeight()) / 2;
			GameOver.relocate(x, y);
			players.forEach(sprite -> sprite.removeFromLayer());
			enemies1.forEach(sprite -> sprite.removeFromLayer());
			enemies2.forEach(sprite -> sprite.removeFromLayer());
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

						
						LevelNine start = new LevelNine();
						try {
							start.LevelNine(stage, powerScore, score,players.get(0).getLife(),ship);
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
		if (players.get(0).getLife() <= -1 || (enemies1.isEmpty() && enemies2.isEmpty())) {
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
