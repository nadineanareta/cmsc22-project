package application;

import javafx.scene.image.Image;

//background class
public class Background extends Sprite {
	//loads image for bg
	private final static Image BG_IMAGE = new Image("file:src//images/background.png",480,800,false,false);
	//holder of initial y position to check the bounds
	private int initialY;
	
	public Background(int xPos, int yPos) {
		//sprite constructor
		super(xPos, yPos);
		
		//initialization
		this.initialY = yPos; 
		
		//image for rendering
		this.loadImage(Background.getBgImage());
	}

	public void moveBG() {
		//updates based on change in positions.
    	this.x += this.dx;
    	this.y += this.dy;
    	
        if (this.y >= GameStage.getWindowHeight()) {
            // Reset the background position to its initial position
            this.y = initialY;
        }
	}

	public static Image getBgImage() {
		return BG_IMAGE;
	}
}
