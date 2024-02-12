package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

// public class for GAmeTimer that extends the AnimationTImer
public class GameTimer extends AnimationTimer{
	
	// attributes
	private GraphicsContext gc;
	private GameStage stage; 
	private Scene theScene;
	private Background bg;
	
	// attribute for the y-attribute of the background
	private double backgroundY;
	
	// character form Character class
	private Character myCharacter;
	
	// arraylists of walls, the wall values, and items/collectibles
	private ArrayList<Walls> walls;
	private ArrayList<Integer> wallValuesBasis;
	private ArrayList<Collect> collect;
	private ArrayList<Avoid> avoid;
	
	// attributes for spawning/time
	private long startSpawnWalls;
	private long startSpawnItems;
	private long Time;
	private int secondCounter;
	private int gameCounter;
	private String gameCounterText;
	
	// boolean attributes/checkers
	private boolean disableKey = false;
	
	// final (static) attributes to be used throughout the code
	private static int CHARACTER_SPEED;
	private final static int BG_SPEED = 3; 
	
	private final static double WALL_SPAWN_DELAY = 5;
	private static double ITEM_SPAWN_DELAY;
	
	private final static int WALLS_INITIAL_YPOS = -100;
	private final static int POINTS_INITIAL_YPOS = 0;
	private final static int BOMBS_INITIAL_YPOS = 10;
	
	// final attribute for the increase gif to be presented during the game
	private final static Image INCREASE = new Image("file:src/images/increase.gif",GameStage.getWindowWidth(),GameStage.getWindowHeight(),false,false);
	
	// custom font that will be used in the game
	private final Font CUSTOM_FONT = Font.loadFont(getClass().getResourceAsStream("/fonts/SuperMario256.ttf"), 40);
	
	// constructor of the GameTimer
	GameTimer(GameStage stage, GraphicsContext gc, Scene theScene){
		// declarations/initializations/instantiations
		this.stage = stage; 
		this.gc = gc;
		this.theScene = theScene;
		GameTimer.CHARACTER_SPEED = 3;
		GameTimer.ITEM_SPAWN_DELAY = 1.25;
		
		this.startSpawnWalls = System.nanoTime();
		this.startSpawnItems = System.nanoTime();
		
		this.myCharacter = new Character("Eljohn",190,600, theScene);
		this.bg = new Background(0, 0);
		this.walls = new ArrayList<Walls>();
		this.wallValuesBasis = new ArrayList<Integer>();
		this.collect = new ArrayList<Collect>();
		this.avoid = new ArrayList<Avoid>();
		
		this.gameCounter = 60;
		this.gameCounterText = "1:00";
		
		//call method to handle mouse click event
		this.handleKeyPressEvent();
		
		// sets the background movement
		this.setBG();
		
	}

	@Override
	public void handle(long currentNanoTime) {
		// calls method for the timer in the game
		seconds(currentNanoTime);
		
		// methods for the background
		this.gc.clearRect(0, 0, GameStage.getWindowWidth(),GameStage.getWindowHeight());
		this.redrawBackgroundImage();
		
		// methods that calls the spawn method for items/walls
		this.autoSpawnWalls(currentNanoTime);
		this.autoSpawnItems(currentNanoTime);
		
		// method that has all the move method for all sprite objects
		this.moveSprites();	
		
		// method that renders all sprite objects
		this.renderSprites();
		
		// method that handle collisions with walls
		this.handleWallCollisions();
		
		// method that controls/moves the strength text that is displayed with the character
		this.myCharacter.moveStrength();
		
		// method that checks the position of the character within the window boundary
		this.myCharacter.checkWindowBoundaries(this);
		
		// method that displays the timer
		this.textTime();
		
		// if the timer reaches 0
		// checks if game counter runs out
        if(this.gameCounter == 0) {
        	// if the character is alive
        	// the game stops and displays the finish scene
        	if (this.myCharacter.isAlive()) {
        		this.stop();
        		this.stage.setupFinish();
        	}else { // else, the game stops but displays the gameover scene
            	this.stop();
            	this.stage.setupGameOver(0);
        	}
        }
        
        // if the timer reaches to a specific time
        // it increments the spawn of the items and the speed of the character
        // it also displays a text that says "INCREASED SPEED"
        if (this.gameCounter == 45) { // when reaches to 45 seconds
        	GameTimer.ITEM_SPAWN_DELAY = 1.05;
        	GameTimer.CHARACTER_SPEED = 4;
        	this.gc.drawImage(GameTimer.INCREASE, 0,0);
        }
        else if (this.gameCounter == 30) { // when reaches to 30 seconds
        	GameTimer.ITEM_SPAWN_DELAY = .80;
        	GameTimer.CHARACTER_SPEED = 5;
        	this.gc.drawImage(GameTimer.INCREASE, 0,0);
        }
        else if (this.gameCounter == 10) { // when reaches to 10 seconds
        	GameTimer.ITEM_SPAWN_DELAY = .55;
        	GameTimer.CHARACTER_SPEED = 6;
        	this.gc.drawImage(GameTimer.INCREASE, 0,0);
        } else if (this.gameCounter == 2) { // if the timer reaches to 2 seconds
        	this.myCharacter.setDY(GameTimer.getBgSpeed() * -1); // it sets the character to move upward
        	this.myCharacter.setAlreadyWon(true); // the character already has won
        	this.disableKey = true;
        }
	}
	
    // logic from one of the examples, "Viper Shooting Game"
	private void seconds(long currentTime){
		// Condition that checks if the time converted from nanoseconds to seconds elapses 1.00s
		if (((currentTime-this.Time) / 1000000000.0) >= 1){ 
			this.secondCounter++; // increments the seconds
			this.gameCounter = 60 - this.secondCounter; // count down or main timer
			if (gameCounter <= 9) { // updates game counter text (single digit / double digit count)
				this.gameCounterText = "0:0" + gameCounter; // single digit
			} else {
				this.gameCounterText = "0:" + gameCounter; // double digit
			}
			this.Time = System.nanoTime(); // updates time or resets back to 0
		}
	}
	
	// displays the text of Timer
	private void textTime(){
		// sets the style of the text
		this.gc.setFont(this.CUSTOM_FONT); // uses the custom font
		
		// if the character is alive, it sets the text of the timer
		if (myCharacter.isAlive()) {
			this.gc.fillText("Time: " + gameCounterText, 20, 40);
			this.gc.setFill(Color.rgb(48, 42, 32));
		}
	}
	
	// method that handles/checks the collision between the character and walls
	private void handleWallCollisions() {
		// iterates over the walls in ArrayList
	    for (Walls wall : walls) {
	    	// if collided
	        if (myCharacter.collidesWith(wall)) {
	        	// checks if the wall value is greater than 0
	        	if (wall.getValue() > 0) {
	        		// checks if the strength is negative or has no enough strength
	        		if (this.myCharacter.getStrength() < 0) {
	        			// set-ups the game over scene 
	        			getStage().setupGameOver(1);
	        		} 
	        		// disables any key when collided
	        		// locks the character to the wall
	        		// the character cannot move when collided with a wall
		        	this.disableKey = true; 
		        	
		        	// brings the character down with the wall
		        	this.myCharacter.setDY(GameTimer.getBgSpeed());
		        	 
		        	// if the wall value is 100-ish
		        	// decrements both the character's strength and wall value by 3
		        	if (wall.getValue() > 99) {
		        		this.myCharacter.setStrength(this.myCharacter.getStrength() - 3); 
			        	wall.setValue(wall.getValue() - 3);
		        	} else {
		        		this.myCharacter.setStrength(this.myCharacter.getStrength() - 2);  
		        		wall.setValue(wall.getValue() - 2);
		        	}
	        		
	        	} else {
	        		// prints in the console the current strength of the character
	        		System.out.println("STRENGTH: " + this.myCharacter.getStrength());
	        		
	        		// removes or clears the displayed wall values
	        		this.clearValues();
	        		
	        		// removes or clears the walls in the ArrayList
	        		this.walls.clear();
	        		
	        		// makes the wall and wall value vanish or invisible
	        		wall.visible = false;
	        		wall.getValueText().setVisible(false);
	        		
	        		// able the keys or the character can now
	        		// move after collision with a wall
	        		this.disableKey = false; 
	        		
	        		// the character can freely move after passing through a wall
	        		this.myCharacter.setDY(0);
	        		break;
	        	}
	        }
	    }
	}
	
	// method that clears the values of the walls
	private void clearValues() {
		// iterates over the ArrayList of walls
		for (Walls w : this.walls) {
			w.getValueText().setVisible(false); // sets the visible attribute to false
		}
	}
	
	// method that handles press movement key event
	private void handleKeyPressEvent() {
		
		// when a key is pressed, it instantiate a KeyCode and moves the character
		// according to the key pressed
		theScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
			public void handle(KeyEvent e){
            	KeyCode code = e.getCode();
                moveCharacter(code);
			}
			
		});
		
		// when a key is released (not pressed), it instantiates a KeyCode
		// and stops the character movement
		theScene.setOnKeyReleased(new EventHandler<KeyEvent>(){
		            public void handle(KeyEvent e){
		            	KeyCode code = e.getCode();
		                stopMoving(code);
		            }
		        });
    }
	
	// method that calls or move all sprite objects
	private void moveSprites() {
		this.moveWalls(); // walls
		this.bg.moveBG(); // background
		this.myCharacter.move(); // character
		this.moveCollect(); // collectibles
		this.moveAvoid(); // items to avoid
	}
	
	// method that calls or renders all sprite objects
	private void renderSprites() {
		this.bg.render(this.gc); // background
		this.renderCollect(); // collectibles
		this.renderAvoid(); // items to avoid
		this.renderWalls(); // walls
		this.myCharacter.render(this.gc); // character
		this.renderWallValueDisplay(); // walls values
	}
	
	// moves character
	private void moveCharacter(KeyCode key) {
		// keys are disabled, returns or does not proceed
		if(this.disableKey) return;
			
		// moves up
		if(key==KeyCode.UP) this.myCharacter.setDY(-1 * GameTimer.CHARACTER_SPEED);              

		// moves to the left
		if(key==KeyCode.LEFT) {
			this.myCharacter.setDX(-1 * GameTimer.CHARACTER_SPEED);
			this.myCharacter.chooseImg(2); // loads image of the left version of character
		}

		// moves down
		if(key==KeyCode.DOWN) this.myCharacter.setDY(GameTimer.CHARACTER_SPEED);
		
		// moves to the right
		if(key==KeyCode.RIGHT) {
			this.myCharacter.setDX(GameTimer.CHARACTER_SPEED);
			this.myCharacter.chooseImg(1); // loads image of the right verison of character
			
		}
		
   	}
	
	//method that will stop the character's movement; set the character's DX and DY to 0
	private void stopMoving(KeyCode ke){
		this.myCharacter.setDX(0);
		this.myCharacter.setDY(0);
	}

	// method that moves the walls
	private void moveWalls() {
		// iterates over the number of walls in ArrayList
		for(int i = 0; i < this.walls.size(); i++){
			Walls w = this.walls.get(i);
			if (w.isVisible()){ // if visible, calls the move method or makes the walls move
				w.move();
			}
			else this.walls.remove(i); // removes the wall from the ArrayList
		}
	}
	
	// method that moves the collectibles
	private void moveCollect() {
		// iterates over the number of collectibles in ArrayList
		for(int i = 0; i < this.collect.size(); i++){
			Collect c = this.collect.get(i);
			if (c.isVisible()){ // if visible, calls the move method or makes the collectibles move
				c.move();
				c.checkCollision(this.myCharacter); // checks collision with the character
			}
			else this.collect.remove(i); // else, removes the collectilble from the ArrayList
		}
	}
	
	// method that moves the items to avoid
	private void moveAvoid() {
		// iterates over the number of items to avoid in ArrayList
		for(int i = 0; i < this.avoid.size(); i++){
			Avoid b = this.avoid.get(i);
			if (b.isVisible()){ // if visible, calls the method to move the items
				b.move();
				b.checkCollision(this.myCharacter); // checks collision with character
				if (this.myCharacter.getStrength() < 0) { //if the character's strength is negative due to items to avoid
					getStage().setupGameOver(0); //sets up game over scene
				}
			}
			else this.avoid.remove(i); //removes the avoid element from arraylist
		}
	}
	
	//renders wall in arraylist
	private void renderWalls() {
        for (Walls wall : this.walls) {
        	wall.render(this.gc);
        } 
	}
	
	//renders the current display of wall values
	private void renderWallValueDisplay() {
		for (Walls i : this.walls) {
			i.moveWallValueDisplay();
		}
	}
	
	//renders collectible items
	private void renderCollect() {
		for (Collect collect : this.collect) {
			collect.render(this.gc);
		}
	}
	
	//renders items to avoid
	private void renderAvoid() {
		for (Avoid avoid : this.avoid) {
			avoid.render(this.gc);
		}
	}

	//constantly moves the bg at constant speed
	private void setBG() {
		this.bg.setDY(GameTimer.getBgSpeed());
	}
	
	//logic from one of the examples (EVERWING GAME)
    void redrawBackgroundImage() {
		// clear the canvas
        this.gc.clearRect(0, 0, GameStage.getWindowWidth(),GameStage.getWindowHeight());

        // redraw background image (moving effect)
        this.backgroundY += GameTimer.getBgSpeed();

        this.gc.drawImage(Background.getBgImage(), 0, this.backgroundY-Background.getBgImage().getHeight() );
        this.gc.drawImage(Background.getBgImage(), 0, this.backgroundY );
        
        if (this.backgroundY >= GameStage.getWindowHeight()) {
        	this.backgroundY = GameStage.getWindowHeight()-Background.getBgImage().getHeight();
        }
    }

    //wall spawn logic
	private void wallsSpawn() {
	    int yPos = GameTimer.WALLS_INITIAL_YPOS; //initial y position saved

	    this.wallValuesBasis.clear(); //clears the arraylist of wall value basis
	    
	    this.wallValuesBasis.add(this.myCharacter.getStrength());
	    this.wallValuesBasis.add(this.myCharacter.getStrength());
	    this.wallValuesBasis.add(this.myCharacter.getStrength());
	    this.wallValuesBasis.add(this.myCharacter.getStrength()/2); //ensures that 1 tile will be passable by the character
	    
	    Collections.shuffle(wallValuesBasis); //shuffles the position
	    
	    if ((System.nanoTime() - this.startSpawnWalls) / 1000000000.0 > GameTimer.WALL_SPAWN_DELAY) {
	    	this.disableKey = false; //ensures that keys are enabled again
	    	this.walls.clear(); //clears the previous walls created
	    	//instantiates new walls and adds them to the array list
		    this.walls.add(new Walls(0, -5, yPos, this.wallValuesBasis.get(0), this.theScene));
		    this.walls.add(new Walls(1, 116, yPos, this.wallValuesBasis.get(1), this.theScene));
		    this.walls.add(new Walls(2, 238, yPos, this.wallValuesBasis.get(2), this.theScene));
		    this.walls.add(new Walls(3, 360, yPos, this.wallValuesBasis.get(3), this.theScene));
		    this.startSpawnWalls = System.nanoTime(); //resets the start spawn time to current time
	    }
	}
	
	// method that instantiates the spawning of collectibles
	private void collectSpawn() {
		//initializations of positions and type
		int yPos = GameTimer.POINTS_INITIAL_YPOS, xPos, type;
		//random
		Random r = new Random();
		//randoms a type and x position since it falls vertically only
		type = r.nextInt(3);
		xPos = r.nextInt(400);
		
		//adds new collectible to arraylist
		this.collect.add(new Collect(type, xPos, yPos));
	}
	
	//same logic as collectSpawn()
	private void avoidSpawn() {
		// initialization of the positions and types
		int yPos = GameTimer.BOMBS_INITIAL_YPOS, xPos, type;
		
		// randomizes the type of avoidable items and x position
		Random r = new Random();
		
		type = r.nextInt(2);
		xPos = r.nextInt(400);
		
		// adds new item to avoid to ArrayList
		this.avoid.add(new Avoid(type, xPos, yPos));
	}
	
	// spawn walls according to time delay
	private void autoSpawnWalls(long currentNanoTime) {
    	double spawnElapsedTime = (currentNanoTime - this.startSpawnWalls) / 1000000000.0;
        // spawn walls
    	if (this.gameCounter > 3) { // spawn walls until 3 seconds left
            if(spawnElapsedTime > GameTimer.WALL_SPAWN_DELAY) {
            	this.wallsSpawn(); // calls method to spawn walls
            	this.startSpawnWalls = System.nanoTime();
            }
    	}
	}
	
	// spawn items according to time delay
	private void autoSpawnItems(long currentNanoTime) {
	    double spawnElapsedTime = (currentNanoTime - this.startSpawnItems) / 1000000000.0;
        // spawn items
	    if (this.gameCounter > 3) { // spawn items until 3 seconds left
		    if (spawnElapsedTime > GameTimer.ITEM_SPAWN_DELAY) {
	            this.collectSpawn(); // spawn collectibles
	            this.avoidSpawn(); // spawn items to avoid
	            this.startSpawnItems = System.nanoTime();
	        }
	    }
	}
	
	//stage getter
	public GameStage getStage() {
		return stage;
	}

	public static int getBgSpeed() {
		return BG_SPEED;
	}
	
	
}
