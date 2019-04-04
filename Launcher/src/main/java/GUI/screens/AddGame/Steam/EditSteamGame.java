package GUI.screens.AddGame.Steam;

import GUI.Menu;
import api.GameLauncher.GameLauncher;
import api.GameLauncher.Steam.SteamApp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public abstract class EditSteamGame extends Application{
	
	private double xOffset = 0;
	private double yOffset = 0;
	private boolean canMove = false;
	
	private Stage stage;
	private EditSteamGameController controller;
	private final GameLauncher launcher;
	
	public EditSteamGame(GameLauncher launcher) {
		this.launcher = launcher;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(EditSteamGame.class.getClassLoader().getResource("fxml/EditSteamGame2.fxml"));
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
		controller.setEditSteamGame(this);
		controller.setLauncher(launcher);
		controller.init(stage);
		
		this.stage.setScene(scene);
		this.stage.initStyle(StageStyle.TRANSPARENT);
		this.stage.getIcons().setAll(new Image("file:materials/Icon.png"));
		this.stage.show();
	}
	
	public abstract void onContinue(api.GameLauncher.Application app);
	
	public void loadSteamApp(api.GameLauncher.Application app){
		controller.loadSteamApp(app);
	}
	
	public static void main(String[] args){
		launch(args);
	}
}
