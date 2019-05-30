package gui.screens.misc;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
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

public abstract class BattleNETApplication {
	
	private Label name;
	private HBox box;
	
	public BattleNETApplication(String appname, String iconPath){
		
		name = new Label(appname);
		name.setFont(new Font(15));
		
		Image typeIcon;
		try {
			typeIcon = new Image(iconPath);
		}catch(Exception e){
			typeIcon = new Image("icon/default.png");
		}
		ImageView type = new ImageView(typeIcon);
		type.setFitHeight(22);
		type.setFitWidth(22);
		
		Image editIcon = new Image("icon/edit.png");
		ImageView edit = new ImageView(editIcon);
		edit.setFitHeight(22);
		edit.setFitWidth(22);
		Pane editPane = new Pane(edit);
		editPane.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				onEdit();
			}
		});
		
		Image linkIcon = new Image("icon/link.png");
		ImageView link = new ImageView(linkIcon);
		link.setFitHeight(22);
		link.setFitWidth(22);
		Pane linkPane = new Pane(link);
		linkPane.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				onLink();
			}
		});
		
		JFXButton b = new JFXButton("Spielen");
		b.setFocusTraversable(false);
		b.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				onRun();
			}
		});
		
		HBox.setMargin(linkPane,new Insets(0,-5,0,0));
		
		Pane p = new Pane();
		box = new HBox(type,name,p,b,linkPane, editPane);
		box.setSpacing(10.0);
		HBox.setHgrow(p, Priority.ALWAYS);
		box.setAlignment(Pos.CENTER);
	}
	
	public HBox getBox() {
		return box;
	}
	
	public abstract void onEdit();
	
	public abstract void onLink();
	
	public abstract void onRun();
	
}
