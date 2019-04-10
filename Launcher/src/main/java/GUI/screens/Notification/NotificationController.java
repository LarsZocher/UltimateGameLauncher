package GUI.screens.Notification;

import GUI.Menu;
import GUI.css.CSSUtils;
import GUI.screens.misc.initController;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class NotificationController extends initController {
	
	@FXML
	private Pane bar;
	@FXML
	private Label msg;
	@FXML
	private Label title;
	@FXML
	private HBox left;
	@FXML
	private HBox right;
	@FXML
	private ImageView icon;
	
	private Notification note;
	
	public void setNotification(Notification note){
		this.note = note;
	}
	
	@Override
	public void init(Stage stage) {
		super.init(stage);
		msg.setText(Notification.text.get(note.id));
		msg.setWrapText(true);
		title.setText(Notification.title.get(note.id));
		icon.setImage(new Image("icon/"+Notification.icon.get(note.id).icon));
		
		bar.setId("bar");
	}
	
	public void setOptions(){
		for(Option option : Notification.options.get(note.id)) {
			addOption(option.getOption(), option.getAlignment(), option.getCallback(), option.isCloseOnClick());
		}
	}
	
	@FXML
	void onExit() {
		stage.close();
	}
	@FXML
	void onMinimize() {
		stage.setIconified(true);
	}
	
	public void addOption(String option, ButtonAlignment alignment, ButtonCallback callback, boolean closeOnClick){
		JFXButton button = new JFXButton(option.toUpperCase());
		button.setMinWidth(100);
		button.setMinHeight(29);
		button.setPrefHeight(29);
		button.setMaxHeight(29);
		button.setFocusTraversable(false);
		CSSUtils.addCSS(button, Menu.styleSheet, "note-button");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				callback.onClick();
				if(closeOnClick)
					stage.close();
			}
		});
		switch(alignment){
			case LEFT:{
				left.getChildren().add(button);
				break;
			}
			case RIGHT:{
				right.getChildren().add(button);
				break;
			}
		}
	}
}
