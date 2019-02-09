package GUI.screens.misc;

import GUI.Menu;
import GUI.css.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javax.swing.text.html.CSS;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public abstract class MenuButton {
	
	private Group group;
	private HBox button;
	private Rectangle rect;
	private ImageView icon;
	private Label name;
	public boolean isFocused = false;
	public boolean isDisabled = false;
	private String FXMLname;
	private CSSCustom custom = new CSSCustom();
	private BooleanProperty isFocusedProperty = new SimpleBooleanProperty(false);
	
	public MenuButton(HBox button, String FXMLname){
		this.button = button;
		this.rect = (Rectangle) button.getChildren().get(0);
		this.icon = (ImageView) button.getChildren().get(1);
		this.name = (Label) button.getChildren().get(2);
		this.FXMLname = FXMLname;
		
		ImageView view = new ImageView(this.icon.getImage());
		view.setFitWidth(this.icon.getFitWidth());
		view.setFitHeight(this.icon.getFitHeight());
		this.icon.setClip(view);
		
		custom.addNode(this.button, "menuButton");
		custom.addNode(this.rect, "rect");
		custom.addNode(this.name, "text");
		custom.addNode(this.icon, "pic");
		
		custom.refreshAll();
		
		colorIcon();
		
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
					custom.setID(button, CSSState.HOVER);
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
	
	private void colorIcon(){
		ColorAdjust monochrome = new ColorAdjust();
		monochrome.setSaturation(-1.0);
		
		Blend blush = new Blend(
				BlendMode.MULTIPLY,
				monochrome,
				new ColorInput(
						0,
						0,
						icon.getImage().getWidth(),
						icon.getImage().getHeight(),
						CSSColorHelper.parseColor("-primary")
				)
		);
		
		icon.effectProperty().bind(
				Bindings
						.when(isFocusedProperty)
						.then((Effect) blush)
						.otherwise((Effect) null)
		);
		
		icon.setCache(true);
		icon.setCacheHint(CacheHint.SPEED);
	}
	
	public void enable(){
		custom.setID(icon);
		if(isFocused)
			custom.setID(name, CSSState.FOCUSED);
		else
			custom.setID(name);
		isDisabled = false;
	}
	
	public void disable(){
		unfocus();
		custom.setID(icon, CSSState.DISABLED);
		custom.setID(name, CSSState.DISABLED);
		isDisabled = true;
	}
	
	public void focus(){
		if(!isDisabled) {
			custom.setID(button, CSSState.FOCUSED);
			custom.setID(rect, CSSState.FOCUSED);
			custom.setID(name, CSSState.FOCUSED);
			isFocused = true;
			isFocusedProperty.setValue(isFocused);
		}
	}
	
	public void unfocus(){
		if(!isDisabled) {
			custom.setID(button);
			custom.setID(rect);
			custom.setID(name);
			isFocused = false;
			isFocusedProperty.setValue(isFocused);
		}
	}
	
	public String getFXMLname() {
		return FXMLname;
	}
	
	public abstract void onClick();
}
