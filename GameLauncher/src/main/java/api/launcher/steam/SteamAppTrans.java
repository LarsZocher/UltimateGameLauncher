package api.launcher.steam;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class SteamAppTrans extends Application {
	
	boolean showButton = false;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Button btn = new Button("Start");
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("click");
			}
		});
		Image backimg = new Image("https://steamcdn-a.akamaihd.net/steam/apps/271590/page_bg_generated_v6b.jpg?t=1546027132");
		ImageView back = new ImageView(backimg);
		back.setFitWidth(220);
		back.setFitHeight(100);
		
		Image img = new Image("https://steamcdn-a.akamaihd.net/steam/apps/271590/header.jpg?t=1546027132");
		ImageView rect = new ImageView(img);
		rect.setFitWidth(220);
		rect.setFitHeight(100);
		
		StackPane root = new StackPane(back, btn, rect);
		
		final FadeTransition fadeIn = new FadeTransition(Duration.millis(200));
		fadeIn.setNode(rect);
		fadeIn.setToValue(0);
		fadeIn.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(!showButton)
					return;
				rect.toBack();
				back.toBack();
			}
		});
		rect.setOnMouseEntered(e -> {
			fadeIn.playFromStart();
			showButton = true;
		});
		
		final FadeTransition fadeOut = new FadeTransition(Duration.millis(200));
		fadeOut.setNode(rect);
		fadeOut.setToValue(1);
		rect.setOnMouseExited(e -> {
			if((e.getX()>0&&e.getX()<rect.getFitWidth())&&(e.getY()>0&&e.getY()<rect.getFitHeight()))
				return;
			fadeOut.playFromStart();
			showButton = false;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					rect.toFront();
				}
			});
		});
		
		rect.setOpacity(1);
		
		Scene scene = new Scene(root, 300, 250);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
