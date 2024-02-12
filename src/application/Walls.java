package application;

import java.util.Random;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

// public class Walls that extends the parent class Sprite
public class Walls extends Sprite{
	// attributes
	private int value;
	private Text valueText;
	private Scene scene;
	
	// wall speed is the same as the background speed
	private final static double WALLS_SPEED = GameTimer.getBgSpeed();
	private final static int IMAGE_SIZE = 120;
	
	// instantiates the images to load to each wall
	private final static Image WALLS_IMAGE_RED = new Image("file:src//images/wall1.png", Walls.IMAGE_SIZE, Walls.IMAGE_SIZE, false, false);
	private final static Image WALLS_IMAGE_VIOLET = new Image("file:src//images/wall2.png", Walls.IMAGE_SIZE, Walls.IMAGE_SIZE, false, false);
	private final static Image WALLS_IMAGE_YELLOW = new Image("file:src//images/wall3.png", Walls.IMAGE_SIZE, Walls.IMAGE_SIZE, false, false);
	private final static Image WALLS_IMAGE_BLUE = new Image("file:src//images/wall4.png", Walls.IMAGE_SIZE, Walls.IMAGE_SIZE, false, false);
	
	// constructor
	public Walls(int type, int xPos, int yPos, int currentStrength, Scene scene) {
		// from parent class Sprite
		super(xPos, yPos);
		
		// randomizes the values of each wall based on the character's strength
		// displays the value to text
		this.value = Walls.getRandom(currentStrength);
		this.valueText = new Text();
		
		this.scene = scene;
		
		// method that sets up the wall values
		this.setUpWallValueDisplay();
		
		// different types loads different images
		switch (type) {
		case 0: this.loadImage(Walls.WALLS_IMAGE_RED); break;
		case 1: this.loadImage(Walls.WALLS_IMAGE_VIOLET); break;
		case 2: this.loadImage(Walls.WALLS_IMAGE_YELLOW); break;
		default: this.loadImage(Walls.WALLS_IMAGE_BLUE);
		}
	}
	
	// method that makes the wall object moves
	void move() {
		// increments the y position of walls according to speed
		this.y += Walls.WALLS_SPEED;
		if (this.y >= GameStage.getWindowHeight()){	// if this monster passes through the bottom of the scene, set visible to false
			this.vanish(); // vanishes the wall
			this.getValueText().setVisible(false); // vanishes the text
		}
	}
	
	// method that displays the text of wall values
	private void setUpWallValueDisplay() {
		// instantiates a custom font
		Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SuperMario256.ttf"), 40);
		
		// sets style of the wall value texts
    	this.getValueText().setFont(customFont);
    	this.getValueText().setFill(Color.rgb(238, 66, 102));
    	this.getValueText().setStroke(Color.BLACK);
    	
    	// initializes the positions of wall value texts
    	this.getValueText().setX(this.x);
    	this.getValueText().setY(this.y);
    	
    	// adds the text to the group
        Group root = (Group) this.scene.getRoot(); // Assuming root is a Group, change the type if needed
    	root.getChildren().add(this.getValueText());
	}
	
	// makes the wall value texts move
	public void moveWallValueDisplay() {
		// sets texts
        this.getValueText().setText(Integer.toString(this.getValue()));
        
        // changes the positions every time to make it move
        this.getValueText().setX(this.x);
        this.getValueText().setY(this.y);
	}
	
	// gets random values for walls
	public static int getRandom(int strength) {
		// random integers
		  Random random = new Random();
		  
		  // the minimum ranges from 1 to the half of character's strength
		  int min = Math.max(1, strength/2);
		  // the maximum is the current strength of character plus 5
		  int max = strength + 5;
		  
		  // randomizes the wall value and returns
		  int temp = random.nextInt(max - min + 1) + min;
		  return (temp);
	}

	public int getValue() {
		return value;
	}
	
	public int setValue(int value) {
		return this.value = value;
	}

	public Text getValueText() {
		return valueText;
	}
}
