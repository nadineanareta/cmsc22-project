package application;

import javafx.animation.PauseTransition;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Character extends Sprite{
	//the attributes of the character class
	private String name;
	private Scene scene; 
	private int strength;
	private Text strengthText; //text display for the strength
	private boolean alive;
	private boolean alreadyWon = false;
	
	//images of the character, left, right and hurt
	private final static Image CHAR_IMAGE = new Image("file:src//images/ej.png", 51, 90,false,false);
	private final static Image HURT_IMAGE = new Image("file:src//images/hurt.png", 51, 90,false,false);
	private final static Image CHAR_RIGHT = new Image("file:src//images/ej_right.png", 51, 90,false,false);
	
	public Character(String name, int x, int y, Scene scene){
		//sprite constructor
		super(x,y);
		
		//initializations
		this.scene = scene;
		this.name = name;
		this.setStrength(100);
		this.alive = true;
		this.strengthText = new Text();
		
		// defaults the character image to the left version
		this.chooseImg(2);
		
		//initialization method for the text display of strength
		this.setUpStrengthDisplay();
	}
	
	// method that sets and loads the appropriate image of the character
	public void chooseImg(int type) {
		switch(type) {
		case 0: 
			// if the character is hurt, it loads the image of hurt version
			// only loads the image for only 1 second
			this.loadImage(Character.HURT_IMAGE); 
			PauseTransition p1 = new PauseTransition(Duration.seconds(1));
			p1.setOnFinished(e -> this.loadImage(Character.CHAR_IMAGE));
			p1.play();
			break;
		case 1:
			// if the character is turning to the right
			// it loads the image to right version
			this.loadImage(Character.CHAR_RIGHT);
			break;
		default: this.loadImage(Character.CHAR_IMAGE);
		// the default image of the character (left version)
		}
		
	}

	public boolean isAlive(){ //checker if character is alive
		if (this.alive) return true;
		return false;
	} 
	
	public String getName(){ //name getter
		return this.name;
	}

	public void move() {
		//moves the character based on positional changes
    	this.x += this.dx;
    	this.y += this.dy;
	}

	private void setUpStrengthDisplay() {
		//initializes a font style
    	Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SuperMario256.ttf"), 40);
    	//edits the text display accordingly
    	this.strengthText.setFont(customFont);
    	this.strengthText.setFill(Color.rgb(238, 66, 102));
    	this.strengthText.setStroke(Color.BLACK);
        this.strengthText.setX(this.x - 3); //adjust the X position
        this.strengthText.setY(this.y + 120); //adjust the Y position
        //adds it to the root node
        Group root = (Group) this.scene.getRoot();
        root.getChildren().add(strengthText); 
	}
	
	void moveStrength() {
		//moves the strength display based on the character position
		//also updates its value accordingly
        this.strengthText.setText(Integer.toString(this.getStrength()));
        this.strengthText.setX(this.x - 3);
        this.strengthText.setY(this.y + 120);
	}
	
    void checkWindowBoundaries(GameTimer gt) {
        //check if the character is going out of the left boundary
        if (this.x < 0) {
            this.x = 0; //set the character's position to the left boundary
        }

        //check if the character is going out of the right boundary
        if (this.x > (GameStage.getWindowWidth() - this.width)) {
            this.x = (int) (GameStage.getWindowWidth() - this.width); //set the character's position to the right boundary
        }

        //check if the character is going out of the top boundary
        if (this.y < 0 && !this.alreadyWon) {
            this.y = 0; //set the character's position to the top boundary
        }
        
        //checks if the whole character was out of the downward bounds
        if (this.y >= (GameStage.getWindowHeight() + (this.height/3.5))) {
            gt.getStage().setupGameOver(1);
            
       }
    }
    
    void gainStrength(int increase){ //strength incrementer 
    	this.setStrength(this.getStrength() + increase);
    	System.out.println("STRENGTH: "+ this.getStrength());
    }
    
    void decStrength(int increase){ //strength decrementer 
    	this.setStrength(this.getStrength() - increase);
    	System.out.println("STRENGTH: "+ this.getStrength());
    }

	public void setAlreadyWon(boolean alreadyWon) { //sets the winning status of the character
		this.alreadyWon = alreadyWon;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

}
