package GameMenu.sprite;
import GameMenu.Settings;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Singleplayer extends Sprite {

	 double playerShipMinX;
	 double playerShipMaxX;
	 double playerShipMinY;
	 double playerShipMaxY;

	 Input input;
	 
	 double speed;
	 
	 int life;
	 boolean invincible;
	 
	 double cannonChargeTime = 12; // the cannon can fire every n frames 
	 double cannonChargeCounter = cannonChargeTime; // initially the cannon is charged
	 double cannonChargeCounterDelta = 1; // counter is increased by this value each frame 
	 
	 double cannonBullets = 5; // number of bullets which the cannon can fire in 1 shot (center, left, right)
	 double cannonBulletSpread = 0.6; // dx of left and right bullets
	 double cannonBulletSpeed = 8.0; // speed of each bullet

	 public Singleplayer(Pane layer, Image image, double x, double y, double r, double dx, double dy, double dr, double health, double damage, double speed, Input input, double ox, double oy) {

	  super(layer, image, x, y, r, dx, dy, dr, health, damage, ox, oy);

	  this.speed = speed;
	  this.input = input;
	  
	  init();
	 }

	 
	 private void init() {
	  
	  // calculate movement bounds of the player ship
	  // allow half of the ship to be outside of the screen 
	  playerShipMinX = 0 - image.getWidth() / 2.0;
	  playerShipMaxX = Settings.SCENE_WIDTH - image.getWidth() / 2.0;
	  playerShipMinY = 0 - image.getHeight() / 2.0;
	  playerShipMaxY = Settings.SCENE_HEIGHT -image.getHeight() / 2.0;
	  
	 }

	 public void processInput() {
	  
	  // ------------------------------------
	  // movement
	  // ------------------------------------
	  
	     // vertical direction
	     if( input.isMoveUp()) {
	      dy = -speed;
	     } else if( input.isMoveDown()) {
	      dy = speed;
	     } else {
	      dy = 0d;
	     }
	     
	     // horizontal direction
	     if( input.isMoveLeft()) {
	      dx = -speed;
	     } else if( input.isMoveRight()) {
	      dx = speed;
	     } else {
	      dx = 0d;
	     }

	 }
	 
	 @Override
	 public void move() {
	  
	  super.move();
	  
	  // ensure the ship can't move outside of the screen
	  checkBounds();
	  
	     
	 }
	 
	 private void checkBounds() {

	     // vertical
	     if( Double.compare( y, playerShipMinY) < 0) {
	      y = playerShipMinY;
	     } else if( Double.compare(y, playerShipMaxY) > 0) {
	      y = playerShipMaxY;
	     }

	     // horizontal
	     if( Double.compare( x, playerShipMinX) < 0) {
	      x = playerShipMinX;
	     } else if( Double.compare(x, playerShipMaxX) > 0) {
	      x = playerShipMaxX;
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
	     // charge weapon: increase a counter by some delta. once it reaches a limit, the weapon is considered charged
	     cannonChargeCounter += cannonChargeCounterDelta;
	     if( cannonChargeCounter > cannonChargeTime) {
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
	 
	 public void setLife(int life) {
		 this.life = life;
	 }
	 
	 public int getLife() {
		 return life;
	 }
	 
	 public void setInvincible(boolean invincible) {
		 this.invincible = invincible;
	 }
	 
	 public boolean getInvincible() {
		 return invincible;
	 }
	 
	}