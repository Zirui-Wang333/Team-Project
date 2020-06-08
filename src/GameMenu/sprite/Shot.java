package GameMenu.sprite;


import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Shot extends Sprite {

	 public Shot(Pane layer, Image image, double x, double y, double dx, double dy, double ox, double oy) {
	  super(layer, image, x, y, 0, dx, dy, 0, 1, 1, 0, 0); // TODO: health/damage
	 }

	 public Shot(Pane layer, Image image, double x, double y, double r, double dx, double dy, double dr, double health, double damage, double ox, double oy) {
	  super(layer, image, x, y, r, dx, dy, dr, health, damage, 0, 0);
	 }

	 @Override
	 public void checkRemovability() {
	  
	  // upper bounds exceeded
	  if( ( getY() + getHeight()) < 0) {
	   setRemovable(true);
	  }
	  
	 }

	}
   

   
