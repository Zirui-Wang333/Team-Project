package MultiplayerEnemyScene;

import GameMenu.MultiPlayerGame;
import GameMenu.MultiPlayerSettings;
import GameMenu.sprite.Life;
import GameMenu.sprite.PowerUp;
import javafx.application.Platform;
import javafx.scene.image.Image;

public class Scene3 {

	int dx;
	int dy;
	int powerupPosition;
	MultiPlayerGame game;
	Image image;
	String player;
	int property;
	Image lifeImage;

	public Scene3(MultiPlayerGame game, String player, int powerupPosition, int dx, int dy, int property) {
		this.powerupPosition = powerupPosition;
		this.dx = dx;
		this.dy = dy;
		this.game = game;
		this.player = player;
		this.image = game.powerup;
		this.property = property;
		this.lifeImage = game.life;

		if (powerupPosition != -1 && property<= 1) {
			if (player.equals("player1")) {
				handlePlayer1Scene3();
			}
			if (player.equals("player2")) {
				handlePlayer2Scene3();
			}
		}
		if(powerupPosition != -1 && property >= 2) {
			if(player.equals("player1")) {
				handlePlayer1Life();
			}
			if(player.equals("player2")) {
				handlePlayer2Life();
			}
		}
	}

	public void handlePlayer1Scene3() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				PowerUp powerUp = new PowerUp(game.playfieldLayer, image, powerupPosition, 0, 0, dx, dy, 0, 1, 0, 0, 0);
				powerUp.setProperty(property);
				game.player1Powerups.add(powerUp);
			}
		});
	}

	public void handlePlayer2Scene3() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				PowerUp powerUp = new PowerUp(game.playfieldLayer, image, powerupPosition+MultiPlayerSettings.SCENE_WIDTH, 0, 0, dx, dy, 0, 1, 0, 0, 0);
				powerUp.setProperty(property);
				game.player2Powerups.add(powerUp);
			}
		});
	}
	
	public void handlePlayer1Life() {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				Life life = new Life(game.playfieldLayer, lifeImage, powerupPosition, 0, 0, dx, dy, 0, 1, 0, 0, 0);
				life.setProperty(property);
				game.player1LifeList.add(life);
			}
		});
		
	}
	
	public void handlePlayer2Life() {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				Life life = new Life(game.playfieldLayer, lifeImage, powerupPosition + MultiPlayerSettings.SCENE_WIDTH, 0, 0, dx, dy, 0, 1, 0, 0, 0);
				life.setProperty(property);
				game.player2LifeList.add(life);
			}
		});
	}

}
