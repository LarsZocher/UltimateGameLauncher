package gui.screens.addgame.steam;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public abstract class SteamNameItem {
	
	private Label name;
	private HBox box;
	
	public SteamNameItem(String nameToSay){
		
		name = new Label(nameToSay);
		name.setFont(new Font(10));
		
		Image removeIcon = new Image("icon/close.png");
		ImageView remove = new ImageView(removeIcon);
		remove.setFitHeight(22);
		remove.setFitWidth(22);
		Pane removePane = new Pane(remove);
		removePane.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				onRemove();
			}
		});
		
		HBox.setMargin(remove,new Insets(0,-5,0,0));
		
		Pane p = new Pane();
		box = new HBox(name,p,removePane);
		box.setSpacing(5.0);
		HBox.setHgrow(p, Priority.ALWAYS);
		box.setAlignment(Pos.CENTER);
	}
	
	public HBox getBox() {
		return box;
	}
	
	public abstract void onRemove();
}
