package gui.screens.addgame.battlenet;

import gui.Menu;
import api.launcher.battlenet.BattleNETGames;
import api.launcher.GameLauncher;
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

public class EditBattleNETGame extends Application{
	
	private double xOffset = 0;
	private double yOffset = 0;
	
	private Stage stage;
	private EditBattleNETGameController controller;
	private GameLauncher launcher;
	
	public EditBattleNETGame(GameLauncher launcher) {
		this.launcher = launcher;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(EditBattleNETGame.class.getClassLoader().getResource("fxml/EditBattleNETGame.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		this.stage = primaryStage;
		
		root.setOnMousePressed(event -> {
			if(event.getY()<40) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		root.setOnMouseDragged(event -> {
			if(event.getY()<40) {
				stage.setX(event.getScreenX() - xOffset);
				stage.setY(event.getScreenY() - yOffset);
			}
		});
		
		scene.getStylesheets().add(Menu.styleSheet);
		
		controller = loader.getController();
		controller.setEditBattleNETGame(this);
		controller.setLauncher(launcher);
		controller.init(stage);
		
		this.stage.setScene(scene);
		this.stage.initStyle(StageStyle.TRANSPARENT);
		this.stage.getIcons().setAll(new Image("file:materials/Icon.png"));
		this.stage.show();
	}
	
	public void loadApp(BattleNETGames app){
		controller.loadApp(app);
	}
	
	public static void main(String[] args){
		launch(args);
	}
}
