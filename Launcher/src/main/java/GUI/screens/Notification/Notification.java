package GUI.screens.Notification;

import GUI.Menu;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class Notification extends Application{
	
	private double xOffset = 0;
	private double yOffset = 0;
	private boolean canMove = false;
	
	public Stage stage;
	private NotificationController controller;
	public int id;
	
	public static HashMap<Integer, String> text = new HashMap<>();
	public static HashMap<Integer, String> title = new HashMap<>();
	public static HashMap<Integer, NotificationIcon> icon = new HashMap<>();
	public static HashMap<Integer, ArrayList<Option>> options = new HashMap<>();
	public static int idCounter = 0;
	
	public Notification() {
		this.id = idCounter;
		idCounter++;
		if(!Notification.icon.containsKey(id))
			Notification.icon.put(id, NotificationIcon.INFO);
		if(!Notification.options.containsKey(id))
			Notification.options.put(id, new ArrayList<>());
	}
	
	public Notification(String text, String title) {
		this.id = idCounter;
		idCounter++;
		if(!Notification.text.containsKey(id))
			Notification.text.put(id, text);
		if(!Notification.title.containsKey(id))
			Notification.title.put(id, title);
		if(!Notification.icon.containsKey(id))
			Notification.icon.put(id, NotificationIcon.INFO);
		if(!Notification.options.containsKey(id))
			Notification.options.put(id, new ArrayList<>());
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		if(text.isEmpty()){
			System.exit(0);
		}
		
		FXMLLoader loader = new FXMLLoader(Notification.class.getClassLoader().getResource("fxml/Notification.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		this.stage = primaryStage;
		
		root.setOnMousePressed(event -> {
			if(event.getY()<40) {
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
		
		scene.getStylesheets().add(Menu.styleSheet);
		
		controller = loader.getController();
		controller.setNotification(this);
		controller.init(stage);
		controller.setOptions();
		
		this.stage.setScene(scene);
		this.stage.initStyle(StageStyle.TRANSPARENT);
		this.stage.setTitle("Bestätigen");
		this.stage.getIcons().setAll(new Image("file:materials/Icon.png"));
		this.stage.show();
		
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), ev -> {
			this.stage.toFront();
		}));
		timeline.play();
		this.stage.toFront();
	}
	
	public void setText(String text){
		Notification.text.put(id, text);
	}
	
	public void setTitle(String title){
		Notification.title.put(id, title);
	}
	
	public void setIcon(NotificationIcon icon) {
		Notification.icon.put(id, icon);
	}
	
	public void addOption(ButtonOption option, ButtonAlignment alignment, ButtonCallback callback){
		addOption(option.text, alignment, callback);
	}
	
	public void addOption(ButtonOption option, ButtonAlignment alignment, ButtonCallback callback, boolean closeOnClick){
		addOption(option.text, alignment, callback, closeOnClick);
	}
	
	public void addOption(String option, ButtonAlignment alignment, ButtonCallback callback){
		ArrayList<Option> options = Notification.options.get(id);
		options.add(new Option(option, alignment, callback));
		Notification.options.put(id, options);
	}
	
	public void addOption(String option, ButtonAlignment alignment, ButtonCallback callback, boolean closeOnClick){
		ArrayList<Option> options = Notification.options.get(id);
		options.add(new Option(option, alignment, callback, closeOnClick));
		Notification.options.put(id, options);
	}
	
	public static void main(String[] args){
		Notification note = new Notification();
		note.setText("Das ist nur ein text um zu testen ob der LineWrap geht, bitte sage mir doch ob dies Funktioniert hat, danke.");
		note.setTitle("Test");
		note.setIcon(NotificationIcon.QUESTION);
		note.addOption(ButtonOption.NO, ButtonAlignment.RIGHT, new ButtonCallback() {
			@Override
			public void onClick() {
				Notification note = new Notification();
				note.setText("Trotzdem danke");
				note.setTitle("Test");
				note.setIcon(NotificationIcon.INFO);
				note.addOption(ButtonOption.CLOSE, ButtonAlignment.RIGHT, new ButtonCallback() {
					@Override
					public void onClick() {
					
					}
				});
				note.show();
			}
		});
		note.addOption(ButtonOption.YES, ButtonAlignment.RIGHT, new ButtonCallback() {
			@Override
			public void onClick() {
				Notification note = new Notification();
				note.setText("Danke fürs testen!");
				note.setTitle("Test");
				note.setIcon(NotificationIcon.ERROR);
				note.addOption(ButtonOption.CLOSE, ButtonAlignment.RIGHT, new ButtonCallback() {
					@Override
					public void onClick() {
					
					}
				});
				note.addOption(ButtonOption.CANCEL, ButtonAlignment.LEFT, new ButtonCallback() {
					@Override
					public void onClick() {
					
					}
				});
				note.show();
			}
		});
		note.show();
	}
	
	public void show(){
		try{
			this.start(new Stage());
		}catch(Exception e){
			idCounter--;
			Application.launch(Notification.class);
		}
	}
}
