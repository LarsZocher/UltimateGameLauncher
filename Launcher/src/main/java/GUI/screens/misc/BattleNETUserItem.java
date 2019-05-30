package gui.screens.misc;

import gui.css.CSSColorHelper;
import gui.css.CSSCustom;
import gui.css.CSSState;
import api.launcher.battlenet.BattleNETUser;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public abstract class BattleNETUserItem {
	
	private HBox hbox;
	private Rectangle rect;
	private ImageView start;
	private Label name;
	public boolean isFocused = false;
	public boolean isDisabled = false;
	private CSSCustom custom = new CSSCustom();
	private BooleanProperty isFocusedProperty = new SimpleBooleanProperty(false);
	private BattleNETUser user;
	private int index;
	
	public BattleNETUserItem(BattleNETUser user, int index){
		this.user = user;
		this.index = index;
		
		this.rect = new Rectangle(7, 60);
		this.name = new Label(user.getName());
		this.name.setFont(new Font(20));
		this.start = new ImageView(new Image("icon/play.png"));
		this.start.setFitWidth(50);
		this.start.setFitHeight(50);
		
		ImageView view = new ImageView(this.start.getImage());
		view.setFitWidth(this.start.getFitWidth());
		view.setFitHeight(this.start.getFitHeight());
		view.setX(start.getX());
		view.setY(start.getY());
		this.start.setClip(view);
		
		colorIcon();
		
		Pane spacer = new Pane();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		hbox = new HBox(rect, name, spacer, start);
		hbox.setSpacing(10);
		hbox.setAlignment(Pos.CENTER);
		
		custom.addNode(this.hbox, "userItem");
		custom.addNode(this.rect, "rect");
		
		custom.refreshAll();
		
		start.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				onClick(user);
			}
		});
		hbox.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				onMouseEntered();
			}
		});
	}
	
	public HBox getBox(){
		return hbox;
	}
	
	public int getIndex() {
		return index;
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
						start.getImage().getWidth()+10,
						start.getImage().getHeight()+10,
						CSSColorHelper.parseColor("-primary")
				)
		);
		
		start.effectProperty().bind(
				Bindings
						.when(isFocusedProperty)
						.then((Effect) blush)
						.otherwise((Effect) null)
		);
		
		start.setCache(true);
		start.setCacheHint(CacheHint.SPEED);
	}
	
	public void focus(){
		if(!isDisabled) {
			custom.setID(rect, CSSState.FOCUSED);
			isFocused = true;
			isFocusedProperty.setValue(isFocused);
		}
	}
	
	public void unfocus(){
		if(!isDisabled) {
			custom.setID(rect);
			isFocused = false;
			isFocusedProperty.setValue(isFocused);
		}
	}
	
	public BattleNETUser getUser() {
		return user;
	}
	
	public void triggerOnClick(){
		onClick(user);
	}
	
	public abstract void onClick(BattleNETUser user);
	
	public abstract void onMouseEntered();
}
