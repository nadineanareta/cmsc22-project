package application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameStage {
	//window dimensions
	private static final int WINDOW_HEIGHT = 800;
	private static final int WINDOW_WIDTH = 480;
	
	//main elements of the stage
	private Scene scene;
	private Stage stage;
	private Group root;
	private Canvas canvas;
	private GraphicsContext gc;
	private GameTimer gametimer;
	
	//buttons
	private Button play;
	private Button about;
	private Button how;
	private Button back;
	private Button backFromGame; 
	private Button references;
	
	//images
	private Image died = new Image("file:src/images/dead.gif",GameStage.getWindowWidth(),GameStage.getWindowHeight(),false,false);
	private Image stuck = new Image("file:src/images/stuck.gif",GameStage.getWindowWidth(),GameStage.getWindowHeight(),false,false);
	private Image mainMenu = new Image("file:src/images/main.gif",GameStage.getWindowWidth(),GameStage.getWindowHeight(),false,false);
	private Image aboutDev = new Image("file:src/images/about.gif",GameStage.getWindowWidth(),GameStage.getWindowHeight(),false,false);
	private Image howToPlay = new Image("file:src/images/how.gif",GameStage.getWindowWidth(),GameStage.getWindowHeight(),false,false);
	private Image finish = new Image("file:src/images/win.gif",GameStage.getWindowWidth(),GameStage.getWindowHeight(),false,false);
	private Image refs = new Image("file:src/images/refs.png",GameStage.getWindowWidth(),GameStage.getWindowHeight(),false,false);
	
	//media files (sounds)
	private Media mediaMain = new Media(getClass().getResource("/music/bgm.mp3").toExternalForm());
	private Media mediaGame = new Media(getClass().getResource("/music/gamemusic.mp3").toExternalForm());
	private Media mediaOver = new Media(getClass().getResource("/music/gameover.mp3").toExternalForm());
	private Media mediaButton = new Media(getClass().getResource("/music/button.mp3").toExternalForm());
	private Media mediaFinish = new Media(getClass().getResource("/music/finish.mp3").toExternalForm());
	private Media mediaAbout = new Media(getClass().getResource("/music/yuyuyu.mp3").toExternalForm());
	
	//mediaPlayers (for sounds)
	private MediaPlayer mediaPlayerMain;
	private MediaPlayer mediaPlayerGame;
	private MediaPlayer mediaPlayerOver;
	private MediaPlayer mediaPlayerButton;
	private MediaPlayer mediaPlayerFinish;
	private MediaPlayer mediaPlayerAbout;
	
	//checker if a sound is playing, before switching to about scene
	private boolean isPlaying;
	
	public GameStage () {
		//stage main elements
		this.root = new Group();
		this.canvas = new Canvas(GameStage.getWindowWidth(),GameStage.getWindowHeight());	
		this.gc = canvas.getGraphicsContext2D();
		
		//scenes
		this.scene = new Scene(root, GameStage.getWindowWidth(),GameStage.getWindowHeight(),Color.CADETBLUE);
		
		//buttons
		this.play = new Button();
		this.about = new Button();
		this.how = new Button();
		this.backFromGame = new Button(); 
		this.back = new Button();
		this.references = new Button();
		
		//setups the main menu music
		this.musicMain();
	}
	
	//functions that sets up the invisible buttons
	private void setupButton(Button button, int sX, int sY, int pX, int pY) {
		button.setPrefSize(sX, sY);
		button.setLayoutX(pX);
		button.setLayoutY(pY);
		button.setOpacity(0); 
	}
	
	//setups the main menu scene
	private void setupMain() {
		//adds the images and buttons
		this.root.getChildren().add(new ImageView(mainMenu)); 
		this.root.getChildren().add(this.play);
		this.root.getChildren().add(this.about);
		this.root.getChildren().add(this.how);
		this.root.getChildren().add(this.references);
		
		//setups the invisible buttons
		setupButton(this.references, 97, 29, 11, 10);
		setupButton(this.play, 300, 100, 90, 515);
		setupButton(this.about, 201, 70, 27, 631);
		setupButton(this.how, 201, 70, 245, 631);
		setupButton(this.back, 97, 29, 11, 10);
	}
	
	//stage setup
	public void setStage(Stage stage) {
		//initializes the stage
		this.stage = stage;
		
		//stage elements initializations	     
		this.root.getChildren().add(canvas);
		this.stage.setTitle("Barrier Basher");	
		this.stage.setScene(this.scene);
		this.stage.setResizable(false);
		
		//calls the main menu scene
		this.setupMain();
		//calls the main menu logic
		this.mainMenu();
	}
	
	//game over scene
	public void setupGameOver(int type) {
		this.musicOver(); //starts game over sound
		this.mediaPlayerGame.stop(); //stops the game music
		this.gametimer.stop(); //stops the game timer
		this.clearScene(); //clears the scene
		
		switch (type) { //showcases the corresponding type based on type of death
			case 0: this.switchScene(this.died); break; // bombed
			default: this.switchScene(this.stuck); // got stuck
		}
		
		//adds and removes buttons accordingly
		this.root.getChildren().add(this.backFromGame);
		this.setupButton(this.backFromGame, 300, 100, 90, 515);
		this.root.getChildren().remove(this.back);
	}
	
	//winning scene
	public void setupFinish() {
		this.musicFinish(); //starts music
		this.mediaPlayerGame.stop(); //stops game music
		this.gametimer.stop(); //stops the game timer
		this.clearScene(); //clears the current scene 
		this.switchScene(this.finish);	//shows finish line scene
		
		//adds and removes buttons accordingly
		this.root.getChildren().add(this.backFromGame); 
		this.setupButton(this.backFromGame, 300, 100, 90, 515);
		this.root.getChildren().remove(this.back);
	}
	
	//main menu logic
	public void mainMenu() {
		//presents the current stage
		this.stage.show();
		
		//if play is pressed
		this.play.setOnMouseClicked(event -> {
			this.clickingSound(); //shows the clicking sound
			this.clearScene(); //clears current scene
			this.gametimer = new GameTimer(this,this.gc,this.scene); //creates a new game timer
			this.musicGame(); //sets up music
			this.mediaPlayerMain.pause(); //pauses the main menu sound
			this.gametimer.start(); //starts the game
		});
		this.about.setOnMouseClicked(event -> {
			this.clickingSound(); 
			this.musicAbout(); //about music call
			this.mediaPlayerMain.pause(); // pauses main music
			this.switchScene(this.aboutDev); //switches scene to about the devs
		});
		this.how.setOnMouseClicked(event -> {
			this.clickingSound(); 
			this.switchScene(this.howToPlay); //switches scene to instructions
		});
		this.back.setOnMouseClicked(event -> { //switches back to the main menu
			this.clickingSound();
			this.clearScene();
			this.setupMain();
			this.mediaPlayerMain.play();
			if (this.isPlaying) { //if from about, stops the sound from about
				this.mediaPlayerAbout.stop();
			}
			
		});
		this.backFromGame.setOnMouseClicked(event -> { //back button for game over and winning scene
			this.clickingSound(); //also returns to the main menu
			this.clearScene();
			this.setupMain();
			this.mediaPlayerMain.play();
		});
		this.references.setOnMouseClicked(event -> { //references button
			this.clickingSound(); //switches and shows refs scene
			this.clearScene();
			this.switchScene(this.refs);
		});
	}
	
	
	//MUSIC / SOUND EFFECTS METHODS
	//initializations and setup
	void musicMain() {
	    this.mediaPlayerMain = new MediaPlayer(this.mediaMain);
	    this.mediaPlayerMain.setVolume(0.70);
        this.mediaPlayerMain.setCycleCount(MediaPlayer.INDEFINITE); // Play the music indefinitely
        this.mediaPlayerMain.play();
	}
	
	void musicGame() {
	    this.mediaPlayerGame = new MediaPlayer(this.mediaGame);
	    this.mediaPlayerGame.setVolume(0.65);
        this.mediaPlayerGame.setCycleCount(MediaPlayer.INDEFINITE); // Play the music indefinitely
        this.mediaPlayerGame.play();
	}
	
	void musicOver() {
	    this.mediaPlayerOver = new MediaPlayer(this.mediaOver);
	    this.mediaPlayerOver.setVolume(0.50);
        this.mediaPlayerOver.play();	
	}
	
	void musicFinish() {
	    this.mediaPlayerFinish = new MediaPlayer(this.mediaFinish);
	    this.mediaPlayerFinish.setVolume(0.50);
        this.mediaPlayerFinish.play();	
	}
	
	void musicAbout() {
	    this.mediaPlayerAbout = new MediaPlayer(this.mediaAbout);
	    this.mediaPlayerAbout.setVolume(0.65);
        this.mediaPlayerAbout.setCycleCount(MediaPlayer.INDEFINITE); // Play the music indefinitely
        this.mediaPlayerAbout.play();
        this.isPlaying = true;
	}
	
	void clickingSound() {
	    this.mediaPlayerButton = new MediaPlayer(this.mediaButton);
	    this.mediaPlayerButton.setVolume(0.20);
        this.mediaPlayerButton.play();
	}
	
	private void clearScene() {
	    //clear the root group
	    this.root.getChildren().clear();
	    
	    //add the canvas back to the root
	    this.root.getChildren().add(canvas);
	}
	
	private void switchScene(Image image) {
		gc.clearRect(0, 0, GameStage.getWindowWidth(),GameStage.getWindowHeight()); //clears the canvas
		this.root.getChildren().add(new ImageView(image)); //adds the image view to the scene
		this.root.getChildren().add(this.back); //adds an invisible back button
	}

	public static int getWindowHeight() {
		return WINDOW_HEIGHT;
	}

	public static int getWindowWidth() {
		return WINDOW_WIDTH;
	}
}
