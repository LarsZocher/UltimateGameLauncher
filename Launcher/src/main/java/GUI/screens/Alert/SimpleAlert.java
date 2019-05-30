package gui.screens.alert;

import gui.Menu;
import gui.css.CSSUtils;
import gui.screens.notification.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class SimpleAlert {

	private String title;
	private String message;
	private List<Option> options = new ArrayList<>();
	private JFXDialog dialog;
	
	private VBox content = new VBox();
	private AnchorPane textPane = new AnchorPane();
	private VBox textContent = new VBox();
	private HBox buttons = new HBox();
	private HBox left = new HBox();
	private HBox right = new HBox();
	private Pane spacer = new Pane();
	
	private boolean initialized = false;
	
	private void initContent(){
		HBox.setHgrow(spacer, Priority.SOMETIMES);
		
		for(Option option : options) {
			addButton(option.getOption(), option.getAlignment(), option.getCallback(), option.isCloseOnClick(), option.getStyle());
		}
		
		Pane bar = new Pane();
		VBox.setVgrow(bar, Priority.ALWAYS);
		bar.setPrefHeight(3);
		bar.setId("bar-4");
		
		textContent.setSpacing(18);
		
		Label titleLabel = new Label(this.title);
		titleLabel.setFont(new Font(24));
		titleLabel.setStyle("-fx-font-weight: bold");
		Label messageLabel = new Label(this.message);
		messageLabel.setFont(new Font(15));
		
		textContent.getChildren().addAll(titleLabel, messageLabel);
		
		textPane.getChildren().add(textContent);
		AnchorPane.setBottomAnchor(textContent, 24d);
		AnchorPane.setLeftAnchor(textContent, 24d);
		AnchorPane.setRightAnchor(textContent, 24d);
		AnchorPane.setTopAnchor(textContent, 24d);
		
		VBox.setMargin(buttons, new Insets(8, 8, 8, 8));
		left.setSpacing(8);
		right.setSpacing(8);
		buttons.getChildren().addAll(left, spacer, right);
		
		content.getChildren().addAll(bar, textPane, buttons);
		
		CSSUtils.addCSS(content, "alert");
		CSSUtils.addCSS(titleLabel, "alert-title");
		CSSUtils.addCSS(messageLabel, "alert-message");
		
		initialized = true;
	}
	
	public VBox getContent(){
		if(!initialized)
			initContent();
		return content;
	}
	
	private void addButton(String option, ButtonAlignment alignment, ButtonCallback callback, boolean closeOnClick, ButtonStyle style){
		JFXButton button = new JFXButton(option.toUpperCase());
		button.setMinWidth(64);
		button.setMinHeight(36);
		button.setPrefHeight(36);
		button.setMaxHeight(36);
		button.setFocusTraversable(false);
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				switch(style){
					case FLAT:{
						CSSUtils.addCSS(button, Menu.styleSheet, "node-button");
					}
					default:{
						button.setId("alert-button");
					}
				}
			}
		});
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				callback.onClick();
				if(closeOnClick) {
					dialog.close();
				}
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
	
	public void addOption(ButtonOption option, ButtonAlignment alignment, ButtonCallback callback){
		addOption(option.getText(), alignment, callback);
	}
	
	public void addOption(ButtonOption option, ButtonAlignment alignment, ButtonCallback callback, boolean closeOnClick){
		addOption(option.getText(), alignment, callback, closeOnClick);
	}
	
	public void addOption(String option, ButtonAlignment alignment, ButtonCallback callback){
		options.add(new Option(option, alignment, callback));
	}
	
	public void addOption(String option, ButtonAlignment alignment, ButtonCallback callback, boolean closeOnClick){
		options.add(new Option(option, alignment, callback, closeOnClick));
	}
	
	public void addOption(String option, ButtonAlignment alignment, ButtonCallback callback, boolean closeOnClick, ButtonStyle style){
		options.add(new Option(option, alignment, callback, closeOnClick, style));
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public JFXDialog getDialog() {
		return dialog;
	}
	
	public void setDialog(JFXDialog dialog) {
		this.dialog = dialog;
	}
}
