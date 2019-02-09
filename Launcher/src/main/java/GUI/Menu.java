package GUI;

import GUI.Utils.ConfigFile;
import GUI.Utils.ResizeHelper;
import GUI.localization.LanguageManager;
import GUI.screens.misc.initMenuController;
import api.GameLauncher.Utils.JsonConfig;
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
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class Menu extends Application{
	
	public Stage stage;
	public menuController mainController;
	public ConfigFile config;
	public JsonConfig jsonConfig;
	public static final String configFile = "Launcher.yml";
	public static String styleSheet = "css/DarkTheme.css";
	public static LanguageManager lang;
	
	private double xOffset = 0;
	private double yOffset = 0;
	private boolean canMove = false;
	
	private Rectangle2D before = new Rectangle2D(50, 50, 50, 50);
	private boolean isMaximized = false;
	private long lastClick = System.currentTimeMillis();
	
	
	
	@Override
	public void start(Stage stage) throws Exception {
		File res = new File("resources");
		if(!res.exists())
			res.mkdir();
//		File css = new File("resources//DarkTheme.css");
//		if(css.exists())
//			styleSheet = "http://217.79.178.92/launcher/release/resources/DarkTheme.css");
		
		styleSheet = "http://217.79.178.92/launcher/release/resources/DarkTheme.css";
		this.config = new ConfigFile(configFile);
		this.config.createFile();
		this.stage = stage;
		
		this.jsonConfig = new JsonConfig("launcher.json");
		this.jsonConfig.load();
		JsonConfig.setDefault(this.jsonConfig.getConfig(), "language", "english");
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
				ResizeHelper.addResizeListener(stage, scene.getWidth(), scene.getHeight(), Double.MAX_VALUE, Double.MAX_VALUE, !stage.isFullScreen());
				before = new Rectangle2D(0, 0, stage.getWidth(), stage.getHeight());
			}
		});
	}
	
	public void setFullScreen(boolean fullscreen){
		if(fullscreen){
			before = new Rectangle2D(0, 0, stage.getWidth(), stage.getHeight());
			
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			stage.setX(primaryScreenBounds.getMinX());
			stage.setY(primaryScreenBounds.getMinY());
			stage.setWidth(primaryScreenBounds.getWidth());
			stage.setHeight(primaryScreenBounds.getHeight());
			
			isMaximized = true;
		}else{
			stage.setWidth(before.getWidth());
			stage.setHeight(before.getHeight());
			isMaximized = false;
		}
	}
	
	public static void main(String[] args){
		Application.launch(Menu.class, args);
	}
	
	public void changeScene(String name){
		mainController.loadFXML(name);
	}
}
