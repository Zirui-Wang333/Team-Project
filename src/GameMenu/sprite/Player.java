package GameMenu.sprite;

import GameMenu.MultiPlayerSettings;
import GameMenu.Settings;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Player extends Sprite {

	double playerShipMinX;
	double playerShipMaxX;
	double playerShipMinY;
	double playerShipMaxY;

	double player2ShipMinX;
	double player2ShipMaxX;
	double player2ShipMinY;
	double player2ShipMaxY;

	public Input input;

	double speed;

	double cannonChargeTime = 6; // the cannon can fire every n frames
	double cannonChargeCounter = cannonChargeTime; // initially the cannon is charged
	double cannonChargeCounterDelta = 1; // counter is increased by this value each frame

	double cannonBullets = 5; // number of bullets which the cannon can fire in 1 shot (center, left, right)
	double cannonBulletSpread = 0.6; // dx of left and right bullets
	double cannonBulletSpeed = 8.0; // speed of each bullet

	int player;
	
	boolean invincible = false;

	public Player(Pane layer, Image image, double x, double y, double r, double dx, double dy, double dr, double health,
			double damage, double speed, Input input, double ox, double oy, int player) {

		super(layer, image, x, y, r, dx, dy, dr, health, damage, ox, oy);

		this.speed = speed;
		this.input = input;
		this.player = player;

		if (player == 1) {
			initPlayer1();
		} else if (player == 2) {
			initPlayer2();
		}
	}

	private void initPlayer1() {

		// calculate movement bounds of the player ship
		// allow half of the ship to be outside of the screen
		playerShipMinX = 0;
		playerShipMaxX = MultiPlayerSettings.SCENE_WIDTH - image.getWidth();
		playerShipMinY = 0 - image.getHeight() / 2.0;
		playerShipMaxY = MultiPlayerSettings.SCENE_HEIGHT - image.getHeight() / 2.0;

	}

	private void initPlayer2() {
		player2ShipMinX = MultiPlayerSettings.SCENE_WIDTH;
		player2ShipMaxX = (Settings.SCENE_WIDTH - image.getWidth()) + Settings.SCENE_WIDTH;
		player2ShipMinY = 0 - image.getHeight() / 2.0;
		player2ShipMaxY = Settings.SCENE_HEIGHT - image.getHeight() / 2.0;
	}

	public void processInput() {

		// ------------------------------------
		// movement
		// ------------------------------------

		// vertical direction
		if (input.isMoveUp()) {
			dy = -speed;
		} else if (input.isMoveDown()) {
			dy = speed;
		} else {
			dy = 0d;
		}

		// horizontal direction
		if (input.isMoveLeft()) {
			dx = -speed;
		} else if (input.isMoveRight()) {
			dx = speed;
		} else {
			dx = 0d;
		}

	}

	public void processPlayer2Input() {

		// ------------------------------------
		// movement
		// ------------------------------------

		// vertical direction
		if (input.isMoveUp2()) {
			dy = -speed;
		} else if (input.isMoveDown2()) {
			dy = speed;
		} else {
			dy = 0d;
		}

		// horizontal direction
		if (input.isMoveLeft2()) {
			dx = -speed;
		} else if (input.isMoveRight2()) {
			dx = speed;
		} else {
			dx = 0d;
		}

	}

	public void player2ProcessInput() {

		// ------------------------------------
		// movement
		// ------------------------------------

		// vertical direction
		if (input.isMoveUp()) {
			dy = -speed;
		} else if (input.isMoveDown()) {
			dy = speed;
		} else {
			dy = 0d;
		}

		// horizontal direction
		if (input.isMoveLeft()) {
			dx = -speed;
		} else if (input.isMoveRight()) {
			dx = speed;
		} else {
			dx = 0d;
		}

	}

	@Override
	public void move() {

		super.move();

		// ensure the ship can't move outside of the screen
		if (player == 1) {
			checkPlayer1Bounds();
		} else if (player == 2) {
			checkPlayer2Bounds();
		}

	}

	private void checkPlayer1Bounds() {

		// vertical
		if (Double.compare(y, playerShipMinY) < 0) {
			y = playerShipMinY;
		} else if (Double.compare(y, playerShipMaxY) > 0) {
			y = playerShipMaxY;
		}

		// horizontal
		if (Double.compare(x, playerShipMinX) < 0) {
			x = playerShipMinX;
		} else if (Double.compare(x, playerShipMaxX) > 0) {
			x = playerShipMaxX;
		}

	}

	private void checkPlayer2Bounds() {

		// vertical
		if (Double.compare(y, player2ShipMinY) < 0) {
			y = player2ShipMinY;
		} else if (Double.compare(y, player2ShipMaxY) > 0) {
			y = player2ShipMaxY;
		}

		// horizontal
		if (Double.compare(x, player2ShipMinX) < 0) {
			x = player2ShipMinX;
		} else if (Double.compare(x, player2ShipMaxX) > 0) {
			x = player2ShipMaxX;
		}

	}

	@Override
	public void checkRemovability() {
		// TODO will be added later when the player ship explodes
	}

	public boolean isFirePrimaryWeapon() {

		boolean isCannonCharged = cannonChargeCounter >= cannonChargeTime;

		return input.isFirePrimaryWeapon() && isCannonCharged;

	}

	public void chargePrimaryWeapon() {

		// limit weapon fire
		// ---------------------------
		// charge weapon: increase a counter by some delta. once it reaches a limit, the
		// weapon is considered charged
		cannonChargeCounter += cannonChargeCounterDelta;
		if (cannonChargeCounter > cannonChargeTime) {
			cannonChargeCounter = cannonChargeTime;
		}

	}

	public void unchargePrimaryWeapon() {

		// player bullet uncharged
		cannonChargeCounter = 0;
	}

	public double getPrimaryWeaponX() {
		return x + image.getWidth() / 2.0; // center of the ship
	}

	public double getPrimaryWeaponY() {
		return y;
	}

	public double getPrimaryWeaponBulletSpread() {
		return cannonBulletSpread;
	}

	public double getPrimaryWeaponBulletCount() {
		return cannonBullets;
	}

	public double getPrimaryWeaponBulletSpeed() {
		return cannonBulletSpeed;
	}
	
	public boolean getInvincible() {
		return invincible;
	}
	
	public void setInvincible(boolean invincible) {
		this.invincible = invincible;
	}

}