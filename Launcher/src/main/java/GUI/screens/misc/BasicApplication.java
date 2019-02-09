package GUI.screens.misc;

import GUI.Menu;
import GUI.screens.Notification.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public abstract class BasicApplication extends Application{
	
	private double xOffset = 0;
	private double yOffset = 0;
	private boolean canMove = false;
	private boolean hasMoveFunktion = false;
	private int titleBarSize = 40;
	
	public Stage stage;
	
	private String title;
	private boolean isTransperant = false;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.stage = primaryStage;
		
		if(isTransperant) this.stage.initStyle(StageStyle.TRANSPARENT);
		if(title!=null) this.stage.setTitle(title);
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				onClose();
			}
		});
		
		onStart();
	}
	
	public void addMoveFunction(Parent root){
		hasMoveFunktion = true;
		
		root.setOnMousePressed(event -> {
			if(event.getY()<titleBarSize) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
				canMove = true;
			}
		});
		root.setOnMouseReleased(event -> {
			canMove = false;
		});
		root.setOnMouseDragged(event -> {
			if(canMove) {
				stage.setX(event.getScreenX() - xOffset);
				stage.setY(event.getScreenY() - yOffset);
			}
		});
	}
	
	public boolean hasMoveFunktion() {
		return hasMoveFunktion;
	}
	
	public void toFront(){
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), ev -> {
			this.stage.toFront();
		}));
		timeline.play();
		this.stage.toFront();
	}
	
	public void show(){
		try{
			this.start(new Stage());
		}catch(Exception e){
			Application.launch(Notification.class);
		}
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public boolean isTransperant() {
		return isTransperant;
	}
	
	public void setTransperant(boolean transperant) {
		isTransperant = transperant;
	}
	
	public int getTitleBarSize() {
		return titleBarSize;
	}
	
	public void setTitleBarSize(int titleBarSize) {
		this.titleBarSize = titleBarSize;
	}
	
	public void onClose(){
	
	}
	
	public abstract void onStart();
}
