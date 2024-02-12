package application;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

// a public class that extends Sprite
// object variable for items to avoid or decreases the character's strength
public class Avoid extends Sprite{
	// initializations
	private int value;
	private final static double AVOID_SPEED = GameTimer.getBgSpeed()*3;
	private final static int IMAGE_SIZE = 50;
	
	// images used for the items
	private final static Image AVOID_IMAGE_10 = new Image("file:src//images/spider.png", Avoid.IMAGE_SIZE, Avoid.IMAGE_SIZE, false, false);
	private final static Image AVOID_IMAGE_15 = new Image("file:src//images/snake.png", Avoid.IMAGE_SIZE, Avoid.IMAGE_SIZE, false, false);
	
	// sound effect
	private MediaPlayer mediaPlayer;
	private Media media = new Media(getClass().getResource("/music/powerdown.mp3").toExternalForm());
	
	// constructor for class Avoid
	public Avoid(int type, int xPos, int yPos) {
		// superclass from Sprite
		super(xPos, yPos);
		
		// loads image and sets value depending on the type
		switch (type) {
		case 0: this.loadImage(Avoid.AVOID_IMAGE_10); this.value = 10; break;
		default: this.loadImage(Avoid.AVOID_IMAGE_15); this.value = 15;
		}
	}
	
	// method that moves the object vertically
	// vanishes if out of bounds
	void move(){
		this.y += Avoid.AVOID_SPEED;
		if(this.y >= GameStage.getWindowHeight()){
			this.vanish();
		}
	}
	
	// checks collision with the character
	void checkCollision(Character character) {
		// if collided with the character, it vanishes from the stage and
		// decreases the character's strength based on the type of item
		// calls the sound effect
		// changes the image of the character to hurt version
		if (this.collidesWith(character)) {
			System.out.println("Oh naur! " + character.getName() + " is hurt!");
			this.vanish();
			character.decStrength(this.value);
			this.music();
			character.chooseImg(0);
		}
	}

	// method for the power down sound effect
	void music() {
	    this.mediaPlayer = new MediaPlayer(this.media);
	    this.mediaPlayer.setVolume(0.5);
        this.mediaPlayer.play();	
	}
}
