package GameMenu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class MultiPlayerLife {
	
	Pane layer;
	ImageView imageView;
	double x;
	double y;
	
	public MultiPlayerLife(Pane layer, Image image, double x, double y) {
		this.layer = layer;
		this.imageView = new ImageView(image);
		this.x = x;
		this.y = y;
		imageView.relocate(x, y);
	}
	
	public void addToLayer() {
		this.layer.getChildren().add(imageView);
	}
	
	public void removeFromLayer() {
		this.layer.getChildren().remove(imageView);
	}
	
	public void isVisible(boolean visible) {
		imageView.setVisible(visible);
	}
	
}
