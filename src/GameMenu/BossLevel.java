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
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

public class BossLevel extends Parent {
	private Group root = new Group();

	
	private Scene scene;
	private Pane playfieldLayer = new Pane();
	private Pane scoreLayer = new Pane();

	private Image playerImage;
	private Image enemyImage;
	private Image background;
	private Image playerBulletImage;
	private Image powerup;
	private Image bossImage;
	private Image enemyBullets;
	private Image Life;

	private List<Shot> playerBulletList = new ArrayList<>();
	private List<Singleplayer> players = new ArrayList<>();
	private List<Alien> enemies = new ArrayList<>();
	private List<Shot> enemyBulletList = new ArrayList<>();
	public int k = 0;
	public boolean ok = true;
	public int count = 0;
	public int count2 = 0;
	public int score = 0;

	private Text collisionText = new Text();
	private Text Score = new Text();
	private Text GameOver = new Text();
	private Text NextLevel = new Text();
	private Text Health = new Text();
	private boolean collision = false;
	private boolean spawn = true;
	
	private int shootCount = 0;
	private int powerScore;
	
	private int enemiesSpawn = 0;
	private int targetCount = 0;
	private double percent = 1.0;
	
	private List<PowerUp> powerups = new ArrayList<>();
	
	private List<Sprite> enemiesSpawned = new ArrayList<>();
	private Button button = new Button();
	
	private boolean target = false;
	private double width = 880;
	private int maxhealth = 2000;
	Rectangle healthbar = new Rectangle();
	private Path bossPath = new Path(new MoveTo(640.0, 100.0), new LineTo(1100.0, 300.0), new LineTo(640.0, 400.0),
			new LineTo(300.0, 200.0), new LineTo(400, 600), new LineTo(900, 600), new LineTo(640, 360),
			new LineTo(640, 100));
	
	private Label label1 = new Label();

	private Text playerLifeText = new Text();
	private MultiPlayerLife life1;
	private MultiPlayerLife life2;
	private int playerLifes;

	public void BossLevel(final Stage ThirdLevel, int power, int sc, int playerLifes,int spaceshipNumber) throws IOException {
		score = sc;
		powerScore = power;
		this.playerLifes = playerLifes;
		
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
		
		InputStream isboss = Files.newInputStream(Paths.get("resources/goatboss.gif"));
		bossImage = new Image(isboss);
		isboss.close();
		
		InputStream isgb = Files.newInputStream(Paths.get("resources/goatbullet.png"));
		enemyBullets = new Image(isgb);
		isgb.close();
		
		root.getChildren().addAll(imgView, playfieldLayer, scoreLayer, label1);
		scene = new Scene(root, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
		
		healthbar.setX(200);
		healthbar.setY(50);
		healthbar.setWidth(width);
		healthbar.setHeight(20);
		healthbar.setArcWidth(20);
		healthbar.setArcHeight(20);
		healthbar.setFill(Color.RED);
		
		
		scoreLayer.getChildren().add(healthbar);
		ThirdLevel.setScene(scene);
		
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

				
				healthbar.setWidth(width);

				players.forEach(sprite -> sprite.processInput());
				if (enemiesSpawn < 300)
					enemiesSpawn++;
				if (count == 0 && spawn) {

					button.setLayoutX(0);
					button.setLayoutY(0);

					bossMove(button);
					spawnEnemies(spawn);
					spawn = false;

				}
				if (target && targetCount < 180) {
					targetCount++;
				}
				if (targetCount == 180) {
					target = false;
					homingEnemies(enemiesSpawned);
					targetCount = 0;
				}
				shootCount++;
				if (shootCount == 100) {
					SpriteShoot(enemies);
					
					shootCount = 0;

				}
				getCoords(button);
				if (enemiesSpawn == 300 && enemiesSpawned.isEmpty()) {
					spawnEnemies();
					enemiesSpawn = 0;
					target = true;
				}
				players.forEach(sprite -> sprite.move());
				enemies.forEach(sprite -> sprite.move());

			
				powerups.forEach(sprite -> sprite.move());
				players.forEach(sprite -> spawnPrimaryWeaponObjects(sprite));
				playerBulletList.forEach(sprite -> sprite.move());
				enemiesSpawned.forEach(sprite -> sprite.move());
				enemyBulletList.forEach(sprite -> sprite.move());
				checkCollisions();

				powerups.forEach(sprite -> sprite.updateUI());
				players.forEach(sprite -> sprite.updateUI());
				enemies.forEach(sprite -> sprite.updateUI());
				enemiesSpawned.forEach(sprite -> sprite.updateUI());

				
				playerBulletList.forEach(sprite -> sprite.updateUI());
				enemyBulletList.forEach(sprite -> sprite.updateUI());

				removeSprites(enemies);
				removeSprites(enemiesSpawned);
				
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

	private void bossMove(Button button) {

		PathTransition transition = new PathTransition();
		transition.setNode(button);

		transition.setDuration(Duration.seconds(20));
		transition.setPath(bossPath);

		
		transition.setCycleCount(PathTransition.INDEFINITE);
		transition.setInterpolator(Interpolator.LINEAR);
		transition.play();

	}

	private void getCoords(Button button) {

		Bounds boundsInScene = button.localToScene(button.getBoundsInLocal());
		button.setVisible(true);
		if (boundsInScene.getCenterX() != 0 && boundsInScene.getCenterY() != 0) {
			Sprite sprite = enemies.get(0);
			sprite.setX(boundsInScene.getCenterX() - 100);
			sprite.setY(boundsInScene.getCenterY() - 100);
			

		}
	}

	private void homingEnemies(List<? extends Sprite> spriteList) {
		Iterator<? extends Sprite> iter = spriteList.iterator();
		double turnRate = 0.6;
		Singleplayer player = players.get(0);
		double speed = 4;
		while (iter.hasNext()) {
			Sprite sprite = iter.next();
			double distanceX = player.getCenterX() - sprite.getCenterX();
			double distanceY = player.getCenterY() - sprite.getCenterY();
			double distanceTotal = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
			double moveDistanceX = turnRate * distanceX / distanceTotal;
			double moveDistanceY = turnRate * distanceY / distanceTotal;
			sprite.setDx(sprite.getDx() + moveDistanceX);
			sprite.setDy(sprite.getDy() + moveDistanceY);
			double totalmove = Math.sqrt(sprite.getDx() * sprite.getDx() + sprite.getDy() * sprite.getDy());
			sprite.setDx(speed * sprite.getDx() / totalmove);
			sprite.setDy(speed * sprite.getDy() / totalmove);

		}

	}

	private void spawnEnemies() {
		Sprite sprite = enemies.get(0);
		double x = sprite.getX();
		double y = sprite.getY();
		var alien1 = new Alien(playfieldLayer, enemyImage, x + 100, y, 0, 0, 0, 0, 6, 1, 0, 0);
		var alien2 = new Alien(playfieldLayer, enemyImage, x - 100, y, 0, 0, 0, 0, 6, 1, 0, 0);
		var alien3 = new Alien(playfieldLayer, enemyImage, x, y + 100, 0, 0, 0, 0, 6, 1, 0, 0);
		var alien4 = new Alien(playfieldLayer, enemyImage, x, y - 100, 0, 0, 0, 0, 6, 1, 0, 0);
		enemiesSpawned.add(alien1);
		enemiesSpawned.add(alien2);
		enemiesSpawned.add(alien3);
		enemiesSpawned.add(alien4);
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

		scoreLayer.getChildren().addAll(collisionText, Score, GameOver, NextLevel, Health, playerLifeText);

		collisionText.setText("Collision");
		double x = (Settings.SCENE_WIDTH - collisionText.getBoundsInLocal().getWidth()) / 2;
		double y = (Settings.SCENE_HEIGHT - collisionText.getBoundsInLocal().getHeight()) / 2;
		collisionText.relocate(x, y);
		Score.relocate(0, 0);
		collisionText.setText("");
		Health.setFont(Font.font(null, FontWeight.BOLD, 32));
		Health.setStroke(Color.BLACK);
		Health.setFill(Color.RED);
		Health.setText("Health");
		Health.setVisible(true);
		Health.relocate(580, 0);
		collisionText.setBoundsType(TextBoundsType.VISUAL);

	}

	public void createPlayers() {

		Input input = new Input(scene);

		input.addListeners();

		Image image = playerImage;

		double x = (Settings.SCENE_WIDTH - image.getWidth()) / 2.0;
		double y = Settings.SCENE_HEIGHT * 0.8;

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

		Image image = bossImage;

		var alien = new Alien(playfieldLayer, image, 250, 200, 0, 0, 0, 0, 2000.0, 1, 0, 0);
		enemies.add(alien);
		System.out.println(enemies.size());

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
				if (sprite.getHealth() <= 0)
					score += 10;

				sprite.removeFromLayer();

				iter.remove();
			}
		}
	}

	public void checkCollisions() {

		collision = false;

		for (Singleplayer player : players) {
			for (Shot shot : playerBulletList) {
				for (Alien enemy : enemies) {
					if (shot.collidesWith(enemy)) {
						shot.stopMovement();
						shot.removeFromLayer();
						shot.remove();
						shot.setX(0);
						enemy.getDamagedBy(player);
						percent = enemy.getHealth() / maxhealth;
						width = percent * 880.0;
						if (enemy.getHealth() <= 0.0) {
							enemy.setRemovable(true);

						}

					}
				}
			}
		}
		for (Singleplayer player : players) {
			for (Shot shot : playerBulletList) {
				for (Sprite enemy : enemiesSpawned) {
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

		if (players.get(0).getInvincible() == false) {
			for (Singleplayer player : players) {
				for (Sprite enemy : enemiesSpawned) {
					if (player.collidesWith(enemy)) {
						collision = true;
					}
				}
			}
			
			for (Singleplayer player : players) {
				for (Sprite enemy : enemies) {
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
			for (PowerUp powerup : powerups) {
				if (player.collidesWith(powerup)) {
					powerScore++;
					score += 50;
					powerup.remove();
				}
			}
		}
		for (Sprite alien : enemiesSpawned) {
			if (alien.getX() >= 1280 || alien.getX() <= -20)
				alien.setRemovable(true);
			if (alien.getY() >= 720 || alien.getY() <= 0)
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
			GameOver.setText("You win!");
			GameOver.setVisible(true);
			double x = (Settings.SCENE_WIDTH - GameOver.getBoundsInLocal().getWidth()) / 2;
			double y = (Settings.SCENE_HEIGHT - GameOver.getBoundsInLocal().getHeight()) / 2;
			GameOver.relocate(x, y);
			players.forEach(sprite -> sprite.removeFromLayer());
			enemies.forEach(sprite -> sprite.removeFromLayer());
			playerBulletList.forEach(sprite -> sprite.removeFromLayer());
			ok = false;

			scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent t) {
					if (t.getCode() == KeyCode.ESCAPE) {
						Window wd = playfieldLayer.getScene().getWindow();
						wd.hide();
					}
				}
			});
		}
	}

	public void SpriteShoot(List<? extends Sprite> spriteList) {
		Iterator<? extends Sprite> i = spriteList.iterator();
		double turnRate = 0.6;

		while (i.hasNext()) {
			Sprite sprite = i.next();

			Shot enemyBullet;
			Singleplayer player = players.get(0);
			Image image = enemyBullets;

			double x = sprite.getCenterX();
			double y = sprite.getY() + 170;
			double speed = 4.0;

			enemyBullet = new Shot(playfieldLayer, image, x, y, 0, 0, 0, 0);
			double distanceX = player.getCenterX() - enemyBullet.getCenterX();
			double distanceY = player.getCenterY() - enemyBullet.getCenterY();
			double distanceTotal = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
			double moveDistanceX = turnRate * distanceX / distanceTotal;
			double moveDistanceY = turnRate * distanceY / distanceTotal;
			enemyBullet.setDx(enemyBullet.getDx() + moveDistanceX);
			enemyBullet.setDy(enemyBullet.getDy() + moveDistanceY);
			double totalmove = Math
					.sqrt(enemyBullet.getDx() * enemyBullet.getDx() + enemyBullet.getDy() * enemyBullet.getDy());
			enemyBullet.setDx(speed * enemyBullet.getDx() / totalmove);
			enemyBullet.setDy(speed * enemyBullet.getDy() / totalmove);
			enemyBulletList.add(enemyBullet);

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
