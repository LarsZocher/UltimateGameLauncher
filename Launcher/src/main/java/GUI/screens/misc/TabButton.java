package GUI.screens.misc;

import GUI.Menu;
import GUI.css.CSSCustom;
import GUI.css.CSSState;
import GUI.css.CSSUtils;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public abstract class TabButton {
	
	private VBox button;
	private Rectangle rect;
	private Label name;
	public boolean isFocused = false;
	public boolean isDisabled = false;
	private CSSCustom custom = new CSSCustom();
	
	public TabButton(VBox button, String buttonName){
		this.button = button;
		this.name = (Label) button.getChildren().get(0);
		this.name.setText(buttonName);
		this.rect = (Rectangle) button.getChildren().get(1);
		
		custom.addNode(this.button, "menuButton");
		custom.addNode(rect, "rect");
		custom.addNode(name, "text");
		
		button.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(!isDisabled)
					onClick();
			}
		});
		button.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(!isDisabled&&!isFocused) {
					//custom.setID(button, CSSState.HOVER);
					custom.setID(name, CSSState.FOCUSED);
				}
			}
		});
		button.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(!isFocused&&!isDisabled) {
					custom.setID(button);
					custom.setID(name);
				}
			}
		});
	}
	
	public void enable(){
		if(isFocused)
			custom.setID(name, CSSState.FOCUSED);
		else
			custom.setID(name);
		isDisabled = false;
	}
	
	public void disable(){
		unfocus();
		custom.setID(name, CSSState.DISABLED);
		isDisabled = true;
	}
	
	public void focus(){
		if(!isDisabled) {
			//custom.setID(button, CSSState.FOCUSED);
			custom.setID(rect, CSSState.FOCUSED);
			custom.setID(name, CSSState.FOCUSED);
			isFocused = true;
		}
	}
	
	public void unfocus(){
		if(!isDisabled) {
			custom.setID(button);
			custom.setID(rect);
			custom.setID(name);
			isFocused = false;
		}
	}
	
	public abstract void onClick();
}
