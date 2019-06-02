package gui.screens.alert;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.events.JFXDialogEvent;
import gui.Menu;
import gui.css.CSSUtils;
import gui.screens.notification.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class LoadingAlert extends CustomAlert {
	
	private VBox content = new VBox();
	private AnchorPane top = new AnchorPane();
	private VBox topContent = new VBox();
	private HBox left = new HBox();
	private HBox right = new HBox();
	private Pane spacer = new Pane();
	private HBox buttons = new HBox();
	private HBox progressBarContainer = new HBox();
	private Pane progressBar = new Pane();
	
	private boolean initialized = false;
	private String title;
	private List<Option> options = new ArrayList<>();
	private boolean hasButtons = false;
	private int progress = 0;
	private Timeline animation;
	private boolean isClosed = false;
	
	private int MAX_BAR_WIDTH = 500;
	
	public LoadingAlert(String title) {
		this.title = title;
	}
	
	public LoadingAlert() {
	
	}
	
	private void initContent() {
		
		HBox.setHgrow(spacer, Priority.SOMETIMES);
		for(Option option : options) {
			addButton(option.getOption(), option.getAlignment(), option.getCallback(), option.isCloseOnClick(), option.getStyle());
		}
		
		Pane bar = new Pane();
		VBox.setVgrow(bar, Priority.ALWAYS);
		bar.setPrefHeight(3);
		bar.setId("bar-4");
		
		Label titleLabel = new Label(this.title);
		titleLabel.setFont(new Font(20));
		titleLabel.setStyle("-fx-font-weight: bold");
		
		progressBar.setPrefHeight(4);
		progressBar.setId("bar-3");
		
		progressBar.setPrefWidth(0);
		
		progressBarContainer.setStyle("-fx-background-color: #00000040");
		
		progressBarContainer.getChildren().addAll(progressBar);
		progressBarContainer.setPrefWidth(MAX_BAR_WIDTH);
		
		topContent.getChildren().addAll(titleLabel, progressBarContainer);
		topContent.setSpacing(10);
		
		top.getChildren().add(topContent);
		AnchorPane.setBottomAnchor(topContent, 24d);
		AnchorPane.setLeftAnchor(topContent, 24d);
		AnchorPane.setRightAnchor(topContent, 24d);
		AnchorPane.setTopAnchor(topContent, 24d);
		
		VBox.setMargin(buttons, new Insets(8, 8, 8, 8));
		left.setSpacing(8);
		right.setSpacing(8);
		
		buttons.getChildren().addAll(left, spacer, right);
		content.getChildren().addAll(bar, top, buttons);
		
		CSSUtils.addCSS(content, "alert");
		CSSUtils.addCSS(titleLabel, "alert-title");
		
		initialized = true;
	}
	
	public VBox getContent() {
		if(!initialized)
			initContent();
		return content;
	}
	
	public void setProgress(int progress) {
		setProgress(progress, null);
	}
	
	public void setProgress(int progress, AnimationStyle style) {
		setProgress(progress, style, 300);
	}
	
	public void setProgress(int progress, AnimationStyle style, int duration) {
		if(progress > 100)
			progress = 100;
		if(progress < 0)
			progress = 0;
		int oldProgress = this.progress;
		this.progress = progress;
		if(style == null || isClosed) {
			this.progressBar.setMinWidth(MAX_BAR_WIDTH / 100.0 * progress);
			this.progressBar.setPrefWidth(MAX_BAR_WIDTH / 100.0 * progress);
			this.progressBar.setMaxWidth(MAX_BAR_WIDTH / 100.0 * progress);
		} else {
			DoubleProperty value = new SimpleDoubleProperty(oldProgress / 100.0);
			value.addListener((observable, oldV, newV) ->
			{
				switch(style) {
					case LINEAR: {
						this.progressBar.setMinWidth(MAX_BAR_WIDTH * newV.doubleValue());
						this.progressBar.setPrefWidth(MAX_BAR_WIDTH * newV.doubleValue());
						this.progressBar.setMaxWidth(MAX_BAR_WIDTH * newV.doubleValue());
						break;
					}
					case FAST_SLOW: {
						double oldProgressSmall = oldProgress / 100.0;
						this.progressBar.setMinWidth(MAX_BAR_WIDTH * (oldProgressSmall + (Math.abs(oldProgress - this.progress) / 100.0 * fast_slow(Math.abs(newV.doubleValue() - oldProgressSmall) / (Math.abs(oldProgress - this.progress) / 100.0)))));
						this.progressBar.setPrefWidth(MAX_BAR_WIDTH * (oldProgressSmall + (Math.abs(oldProgress - this.progress) / 100.0 * fast_slow(Math.abs(newV.doubleValue() - oldProgressSmall) / (Math.abs(oldProgress - this.progress) / 100.0)))));
						this.progressBar.setMaxWidth(MAX_BAR_WIDTH * (oldProgressSmall + (Math.abs(oldProgress - this.progress) / 100.0 * fast_slow(Math.abs(newV.doubleValue() - oldProgressSmall) / (Math.abs(oldProgress - this.progress) / 100.0)))));
						break;
					}
					case SLOW_FAST: {
						double oldProgressSmall = oldProgress / 100.0;
						this.progressBar.setMinWidth(MAX_BAR_WIDTH * (oldProgressSmall + (Math.abs(oldProgress - this.progress) / 100.0 * slow_fast(Math.abs(newV.doubleValue() - oldProgressSmall) / (Math.abs(oldProgress - this.progress) / 100.0)))));
						this.progressBar.setPrefWidth(MAX_BAR_WIDTH * (oldProgressSmall + (Math.abs(oldProgress - this.progress) / 100.0 * slow_fast(Math.abs(newV.doubleValue() - oldProgressSmall) / (Math.abs(oldProgress - this.progress) / 100.0)))));
						this.progressBar.setMaxWidth(MAX_BAR_WIDTH * (oldProgressSmall + (Math.abs(oldProgress - this.progress) / 100.0 * slow_fast(Math.abs(newV.doubleValue() - oldProgressSmall) / (Math.abs(oldProgress - this.progress) / 100.0)))));
						break;
					}
				}
			});
			
			if(animation != null)
				animation.stop();
			animation = new Timeline();
			final KeyValue kv = new KeyValue(value, progress / 100.0);
			final KeyFrame kf = new KeyFrame(Duration.millis(duration), kv);
			animation.getKeyFrames().add(kf);
			
			animation.playFromStart();
		}
	}
	
	private double fast_slow(double x) {
		x = x * 2;
		return ((-1.0 * x * x) + (4.0 * x)) / 4;
	}
	
	private double slow_fast(double x) {
		x = x * 2;
		return (1.0 * x * x) / 4;
	}
	
	private void addButton(String option, ButtonAlignment alignment, ButtonCallback callback, boolean closeOnClick, ButtonStyle style) {
		JFXButton button = new JFXButton(option.toUpperCase());
		button.setMinWidth(64);
		button.setMinHeight(36);
		button.setPrefHeight(36);
		button.setMaxHeight(36);
		button.setFocusTraversable(false);
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				switch(style) {
					case FLAT: {
						button.getStyleClass().add("note-button");
						break;
					}
					default: {
						button.setId("alert-button");
						break;
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
		
		switch(alignment) {
			case LEFT: {
				left.getChildren().add(button);
				break;
			}
			case RIGHT: {
				right.getChildren().add(button);
				break;
			}
		}
		
	}
	
	@Override
	public void onClose(JFXDialogEvent event) {
		if(animation != null)
			animation.stop();
		isClosed = true;
	}
	
	public void addOption(ButtonOption option, ButtonAlignment alignment, ButtonCallback callback) {
		addOption(option.getText(), alignment, callback);
	}
	
	public void addOption(ButtonOption option, ButtonAlignment alignment, ButtonCallback callback, boolean closeOnClick) {
		addOption(option.getText(), alignment, callback, closeOnClick);
	}
	
	public void addOption(String option, ButtonAlignment alignment, ButtonCallback callback) {
		options.add(new Option(option, alignment, callback));
	}
	
	public void addOption(String option, ButtonAlignment alignment, ButtonCallback callback, boolean closeOnClick) {
		options.add(new Option(option, alignment, callback, closeOnClick));
	}
	
	public void addOption(String option, ButtonAlignment alignment, ButtonCallback callback, boolean closeOnClick, ButtonStyle style) {
		options.add(new Option(option, alignment, callback, closeOnClick, style));
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
}
