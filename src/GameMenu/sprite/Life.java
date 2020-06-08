package GameMenu.sprite;

import GameMenu.Settings;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Life extends Sprite {

	/**
	 * 2: increase player life 
	 * 3: decrease player life
	 */
	int property;

	public Life(Pane layer, Image image, double x, double y, double r, double dx, double dy, double dr, double health,
			double damage, double ox, double oy) {
		super(layer, image, x, y, r, dx, dy, dr, health, damage, ox, oy);
	}

	@Override
	public void checkRemovability() {
		if (Double.compare(getY(), Settings.SCENE_HEIGHT) > 0) {
			setRemovable(true);
		}
	}

	public void setProperty(int property) {
		this.property = property;
	}

	public int getProperty() {
		return property;
	}
}
