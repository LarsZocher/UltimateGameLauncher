package GUI.screens.AddGame.BattleNET;

import GUI.Menu;
import api.GameLauncher.BattleNET.BattleNETGames;
import api.GameLauncher.GameLauncher;
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

public class StartBattleNETGame extends Application {
	
	
	private double xOffset = 0;
	private double yOffset = 0;
	
	private Stage stage;
	private StartBattleNETGameController controller;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		String game = getParameters().getRaw().get(0);
		
		FXMLLoader loader = new FXMLLoader(EditBattleNETGame.class.getClassLoader().getResource("fxml/StartBattleNETGame.fxml"));
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
		controller.init(stage);
		controller.setApp(BattleNETGames.getByConfigName(game));
		
		this.stage.setScene(scene);
		
		scene.getStylesheets().add(Menu.getStyleSheet());
		
		this.stage.initStyle(StageStyle.TRANSPARENT);
		this.stage.getIcons().setAll(new Image("file:materials/Icon.png"));
		this.stage.show();
		this.stage.requestFocus();
	}
	
	public void setApp(BattleNETGames app){
	
	}
	
	public static void main(String[] args){
		launch(args);
	}

}
