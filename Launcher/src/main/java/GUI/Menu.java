package gui;

import gui.utils.ResizeHelper;
import gui.localization.LanguageManager;
import gui.screens.addgame.battlenet.StartBattleNETGame;
import gui.screens.misc.initMenuController;
import updater.Updater;
import api.launcher.GameLauncher;
import api.launcher.utils.JsonConfig;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class Menu extends Application{
	
	public Stage stage;
	public menuController mainController;
	public JsonConfig jsonConfig;
	public static String styleSheet = "css/DarkTheme.css";
	public static LanguageManager lang;
	public static StackPane root;
	public static AnchorPane rootAnchor;
	
	private double xOffset = 0;
	private double yOffset = 0;
	private boolean canMove = false;
	
	private Rectangle2D before = new Rectangle2D(50, 50, 50, 50);
	private boolean isMaximized = false;
	private long lastClick = System.currentTimeMillis();
	private GameLauncher launcher;
	
	
	public static String getStyleSheet(){
		File res = new File("resources");
		if(!res.exists())
			res.mkdir();
		File css = new File("resources//DarkTheme.css");
		if(css.exists()) {
			try {
				return css.toURI().toURL().toString();
			} catch(MalformedURLException e) {
				e.printStackTrace();
				return "http://217.79.178.92/launcher/release/resources/DarkTheme.css";
			}
		}
		else
			return "http://217.79.178.92/launcher/release/resources/DarkTheme.css";
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		styleSheet = getStyleSheet();
		System.out.println("[Launcher] Using \""+styleSheet+"\" as StylingSheet");
		
		this.stage = stage;
		this.launcher = new GameLauncher();
		
		this.jsonConfig = new JsonConfig("launcher.json");
		this.jsonConfig.load();
		this.jsonConfig.setDefault("language", "english");
		this.jsonConfig.save();
		
		lang = new LanguageManager(jsonConfig.getConfig().getString("language"));
		
		FXMLLoader loader = new FXMLLoader(Menu.class.getClassLoader().getResource("fxml/menu.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);
		
		root.setOnMousePressed(event -> {
			if(event.getY()<60) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
				canMove = true;
			}
		});
		root.setOnMouseReleased(event -> {
			canMove = false;
			if(event.getScreenY()<=10){
				setFullScreen(true);
			}
			if(lastClick+300>System.currentTimeMillis()&&event.getY()<60){
				if(!isMaximized) {
					setFullScreen(true);
				} else {
					setFullScreen(false);
					Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
					stage.setX(primaryScreenBounds.getWidth()/2-before.getWidth()/2);
					stage.setY(primaryScreenBounds.getHeight()/2-before.getHeight()/2);
				}
			}
			lastClick = System.currentTimeMillis();
		});
		root.setOnMouseDragged(event -> {
			if(canMove) {
				if(isMaximized){
					if(!(event.getScreenY()<=10)){
						setFullScreen(false);
						xOffset = before.getWidth()/2;
					}
				}
				stage.setX(event.getScreenX() - xOffset);
				stage.setY(event.getScreenY() - yOffset);
			}
		});
		scene.getStylesheets().add(Menu.styleSheet);
		
		mainController = loader.getController();
		mainController.init(this);
		this.root = mainController.getRootStack();
		this.rootAnchor = mainController.getRootAnchorPane();
		
		this.stage.setScene(scene);
		this.stage.initStyle(StageStyle.UNDECORATED);
		this.stage.setTitle("UltimateGameLauncher");
		this.stage.getIcons().setAll(new Image("file:resources/Icon.png"));
		
		//ResizeHelper.addResizeListener(stage);
		
		this.stage.show();
		
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), ev -> {
			mainController.onRefresh();
			for(initMenuController controller : mainController.controllers.values()){
				controller.onRefresh();
			}
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ResizeHelper.addResizeListener(stage, 936, 554, Double.MAX_VALUE, Double.MAX_VALUE, !stage.isFullScreen());
				before = new Rectangle2D(0, 0, stage.getWidth(), stage.getHeight());
			}
		});
		
		stage.setHeight(650);
		stage.setWidth(1190);
	}
	
	public void setFullScreen(boolean fullscreen){
		if(fullscreen){
			before = new Rectangle2D(0, 0, stage.getWidth(), stage.getHeight());
			
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			stage.setX(primaryScreenBounds.getMinX());
			stage.setY(primaryScreenBounds.getMinY());
			stage.setWidth(primaryScreenBounds.getWidth()-2);
			stage.setHeight(primaryScreenBounds.getHeight());
			isMaximized = true;
		}else{
			stage.setWidth(before.getWidth());
			stage.setHeight(before.getHeight());
			isMaximized = false;
		}
	}
	
	public static void main(String[] args){
		boolean skipUpdate = false;
		boolean console = false;
		for(int i = 0; i<args.length; i++){
			if(args[i].equalsIgnoreCase("--startBNet") && args.length>=1){
				Application.launch(StartBattleNETGame.class, args[i+1]);
				return;
			}
			if(args[i].equalsIgnoreCase("--noUpdate")){
				skipUpdate = true;
			}
			if(args[i].equalsIgnoreCase("--console")){
				console = true;
			}
		}
		
		if(!skipUpdate){
			if(Updater.needsUpdate()){
				try {
					String jre = new File("jre/bin/java.exe").exists()?"jre/bin/java.exe":"java";
					Runtime.getRuntime().exec("cmd.exe /K start "+jre+" -jar Updater.jar --launcher "+(console?"--console ":"") + "--update");
					System.exit(0);
					return;
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		Application.launch(Menu.class, args);
	}
	
	public void changeScene(String name){
		mainController.loadFXML(name);
	}
	
	public GameLauncher getLauncher() {
		return launcher;
	}
}

