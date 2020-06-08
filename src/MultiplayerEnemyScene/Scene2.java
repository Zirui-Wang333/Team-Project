package MultiplayerEnemyScene;

import GameMenu.Commons;
import GameMenu.MultiPlayerGame;
import GameMenu.MultiPlayerSettings;
import GameMenu.sprite.Alien;
import GameMenu.sprite.PowerUp;
import javafx.application.Platform;
import javafx.scene.image.Image;

public class Scene2 {

	int x;
	int y;
	int dx;
	int dy;
	int health;
	MultiPlayerGame game;
	String player;
	Image enemyImage;

	public Scene2(MultiPlayerGame game, String player, int x, int y, int dx, int dy, int health) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.health = health;
		this.game = game;
		this.player = player;
		enemyImage = game.enemyImage;

		if (player.equals("player1")) {
			handlePlayer1Scene2();
		}
		if (player.equals("player2")) {
			handlePlayer2Scene2();
		}

	}

	
	private void handlePlayer1Scene2() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				Alien enemy = new Alien(game.playfieldLayer, enemyImage, x, y, 0, dx, dy, 0, health, 1, 410 + 18,
						Commons.ALIEN_INIT_Y + 18);
				game.player1Enemies.add(enemy);
			}
		});
	}

	private void handlePlayer2Scene2() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				Alien enemy = new Alien(game.playfieldLayer, enemyImage, x+MultiPlayerSettings.SCENE_WIDTH, y, 0, dx, dy, 0, health, 1, 410 + 18,
						Commons.ALIEN_INIT_Y + 18);
				game.player2Enemies.add(enemy);
			}
		});
	}

}
