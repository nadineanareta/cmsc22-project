package application;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

// public class Collect or the collectibles that extends the Sprite
public class Collect extends Sprite {
	// the value of the collectibles
	private int value;
	
	// the speed of the items
	private final static double POINTS_SPEED = GameTimer.getBgSpeed()*2;
	
	// initializations of the images
	private final static int IMAGE_SIZE = 50;
	private final static Image POINTS_IMAGE_5 = new Image("file:src//images/heart.png", Collect.IMAGE_SIZE, Collect.IMAGE_SIZE, false, false);
	private final static Image POINTS_IMAGE_10 = new Image("file:src//images/apple.png", Collect.IMAGE_SIZE, Collect.IMAGE_SIZE, false, false);
	private final static Image POINTS_IMAGE_15 = new Image("file:src//images/diamond.png", Collect.IMAGE_SIZE, Collect.IMAGE_SIZE, false, false);
	
	// loads the music/sound effects for the power up
	private MediaPlayer mediaPlayer;
	private Media media = new Media(getClass().getResource("/music/powerup.mp3").toExternalForm());
	
	// constructor
	public Collect(int type, int xPos, int yPos) {
		// Sprite constructor
		super(xPos, yPos);
		
		// loads and initializes the corresponding image and value of the collectible
		switch (type) {
		case 0: this.loadImage(Collect.POINTS_IMAGE_5); this.value = 5; break;
		case 1: this.loadImage(Collect.POINTS_IMAGE_10); this.value = 10; break;
		default: this.loadImage(Collect.POINTS_IMAGE_15); this.value = 15;
		}
	}
	
	// moves the items vertically
	// vanishes if out of bounds
	void move(){
		this.y += Collect.POINTS_SPEED;
		if(this.y >= GameStage.getWindowHeight()){
			this.vanish();
		}
	}
	
	// checks the collision with the character
	void checkCollision(Character character) {
		// if collides with the character
		// the item vanishes and the character gains strength
		// calls the method of sound effects
		if (this.collidesWith(character)) {
			System.out.println(character.getName() + " increased his strength!");
			this.vanish();
			character.gainStrength(Collect.this.value);
			this.music();
		}
	}
	
	// method that plays the sound effects
	void music() {
	    this.mediaPlayer = new MediaPlayer(this.media);
	    this.mediaPlayer.setVolume(0.5);
        this.mediaPlayer.play();	
	}
}
