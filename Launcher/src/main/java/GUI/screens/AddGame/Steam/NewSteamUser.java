package GUI.screens.AddGame.Steam;

import GUI.Menu;
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

public abstract class NewSteamUser extends Application{
	
	private double xOffset = 0;
	private double yOffset = 0;
	
	private Stage stage;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(NewSteamUser.class.getClassLoader().getResource("fxml/NewSteamUser.fxml"));
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
		
		NewSteamUserController controller = loader.getController();
		
		this.stage.setScene(scene);
		this.stage.initStyle(StageStyle.TRANSPARENT);
		this.stage.setTitle("Steam Benutzer");
		this.stage.getIcons().setAll(new Image("file:materials/Icon.png"));
		this.stage.show();
	}
	
	public abstract void onFinish();
	
	public abstract void onCancel();
	
	public static void main(String[] args){
		launch(args);
	}
}
