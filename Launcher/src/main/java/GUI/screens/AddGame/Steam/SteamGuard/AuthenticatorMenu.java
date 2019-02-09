package GUI.screens.AddGame.Steam.SteamGuard;

import GUI.Menu;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public abstract class AuthenticatorMenu extends Application{
	
	private double xOffset = 0;
	private double yOffset = 0;
	
	private Stage stage;
	private AuthenticatorMenuController controller;
	private boolean canMove = false;
	
	public String user;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println("lol");
		FXMLLoader loader = new FXMLLoader(AuthenticatorMenu.class.getClassLoader().getResource("fxml/SteamGuardSettingsMenu.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		this.stage = primaryStage;
		
		root.setOnMousePressed(event -> {
			if(event.getY()<60) {
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
		
		scene.getStylesheets().add("http://217.79.178.92/launcher/release/resources/DarkTheme.css");
		
		controller = loader.getController();
		controller.setAuthenticatorMenu(this);
		controller.init(stage);
		controller.loadFXML("SteamGuardSettingsStart");
		
		this.stage.setScene(scene);
		this.stage.initStyle(StageStyle.TRANSPARENT);
		this.stage.setTitle("Steam Guard");
		this.stage.getIcons().setAll(new Image("file:materials/Icon.png"));
		this.stage.show();
	}
	
	public void setScene(String name, boolean save){
		controller.loadFXML(name, save);
	}
	
	public void setUser(String username){
		user = username;
	}
	
	public abstract void onSuccessfullyAdded();
	
	public abstract void onSuccessfullyRemoved();
	
	public Stage getStage() {
		return stage;
	}
	
	public String getUser() {
		return user;
	}
	
	public static void main(String[] args){
		launch();
	}
}
