package GUI.screens.misc;

import codebehind.steam.mobileauthentication.model.Confirmation;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class SteamMobileConfirmation {
	
	private Pane pane;
	
	public SteamMobileConfirmation(Confirmation conf){
		Image image = new javafx.scene.image.Image(conf.getPicture(), 40, 40, false, true, true);
		ImageView icon = new ImageView(image);
		
		Label name = new Label(conf.getDescription());
		name.setFont(new Font(15));
		Label price = new Label(conf.getPrice());
		price.setFont(new Font(15));
		Label date = new Label(conf.getDate());
		date.setFont(new Font(15));
		
		Pane space = new Pane();
		space.setMaxSize(99999, 200);
		
		JFXButton accept = new JFXButton("ANNEHMEN");
		accept.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: -primary");
		JFXButton deny = new JFXButton("ABLEHNEN");
		deny.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #d2d2d2");
		
		VBox text = new VBox(name, price, date);
		text.setSpacing(5);
		HBox box = new HBox(icon, text, space, accept, deny);
		box.setMaxSize(99999, 200);
		box.setSpacing(10);
		pane = new Pane(box);
		pane.setStyle("-fx-background-color: #2d2d2d");
		HBox.setHgrow(pane, Priority.ALWAYS);
		HBox.setHgrow(space, Priority.ALWAYS);
	}
	
	public Pane getPane() {
		return pane;
	}
}
