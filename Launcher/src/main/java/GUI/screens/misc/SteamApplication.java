package gui.screens.misc;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
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

public abstract class SteamApplication {
	
	private JFXListView<HBox> list;
	private Label name;
	private HBox box;
	
	public SteamApplication(JFXListView list, String appname, String iconPath){
		this.list = list;
		
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
		
		Image deleteIcon = new Image("icon/close.png");
		ImageView delete = new ImageView(deleteIcon);
		delete.setFitHeight(22);
		delete.setFitWidth(22);
		Pane deletePane = new Pane(delete);
		deletePane.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				onDelete();
			}
		});
		
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
		
		HBox.setMargin(editPane,new Insets(0,-5,0,0));
		HBox.setMargin(linkPane,new Insets(0,-5,0,0));
		
		
		JFXButton b = new JFXButton("Spielen");
		b.setStyle("-fx-background-color: -primary; -fx-text-fill: #ffffff");
		b.setFocusTraversable(false);
		b.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				onRun();
			}
		});
		
		Pane p = new Pane();
		box = new HBox(type,name,p,b,linkPane,editPane,deletePane);
		box.setSpacing(10.0);
		HBox.setHgrow(p, Priority.ALWAYS);
		box.setAlignment(Pos.CENTER);
	}
	
	public HBox getBox() {
		return box;
	}
	
	public abstract void onDelete();
	
	public abstract void onEdit();
	
	public abstract void onLink();
	
	public abstract void onRun();
	
}
