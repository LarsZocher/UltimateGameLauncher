package gui.screens.misc;

import api.launcher.GameLauncher;
import api.launcher.image.UserIconSize;
import gui.css.CSSColorHelper;
import api.launcher.Application;
import api.launcher.steam.SteamUser;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public abstract class GameDisplay {
	
	private Pane pane = new Pane();
	private HBox topBar;
	private String name;
	private Application app;
	private String picture;
	private GameLauncher launcher;
	private boolean showEdit;
	private boolean showLink;
	private boolean showDelete;
	private boolean isFocused = false;
	private boolean isShown = false;
	private int width = 225;
	private int height = 102;
	
	public GameDisplay(String name, Application app, String picture, GameLauncher launcher, boolean showEdit, boolean showLink, boolean showDelete) {
		this.launcher = launcher;
		long start = System.currentTimeMillis();
		this.name = name;
		this.app = app;
		this.picture = picture;
		this.showEdit = showEdit;
		this.showLink = showLink;
		this.showDelete = showDelete;
		
		this.pane.setPrefWidth(width);
		this.pane.setPrefHeight(height);
		this.pane.setStyle("-fx-background-color: #2d2d2d");
		System.out.println("-.." + (System.currentTimeMillis() - start));
		
		show();
		System.out.println("-.." + (System.currentTimeMillis() - start));
	}
	
	public GameDisplay() {
	}
	
	public void hide() {
		if(!isShown)
			return;
		this.pane.getChildren().clear();
		isShown = false;
	}
	
	public void show() {
		if(isShown)
			return;
		
		System.out.println("\"" + picture + "\"");
		
		isShown = true;
		Label label = new Label(name);
		label.setFont(new Font(13));
		label.setPrefWidth(width);
		label.setMinWidth(width);
		label.setStyle("-fx-text-fill: WHITE");
		
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
		
		Image runIcon = new Image("icon/play.png");
		ImageView run = new ImageView(runIcon);
		run.setFitHeight(50);
		run.setFitWidth(50);
		HBox runBox = new HBox(run);
		runBox.setAlignment(Pos.TOP_CENTER);
		runBox.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				onRun();
			}
		});
		colorIcon(run);
		
		topBar = new HBox();
		if(showLink)
			topBar.getChildren().add(linkPane);
		if(showEdit)
			topBar.getChildren().add(editPane);
		if(showDelete)
			topBar.getChildren().add(deletePane);
		HBox nameBox = new HBox(label);
		nameBox.setAlignment(Pos.BOTTOM_LEFT);
		topBar.setAlignment(Pos.TOP_RIGHT);
		topBar.setSpacing(5);
		VBox buttons = new VBox(topBar, runBox, nameBox);
		buttons.setSpacing(5);
		buttons.setAlignment(Pos.TOP_CENTER);
		
		ImageView rect = new ImageView();
		Task<Void> async = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					if(picture == null || picture.isEmpty()) {
						Image img = new Image("icon/gd_default.png", 225, 102, false, true, true);
						rect.setImage(img);
					} else {
						Image img = new Image(picture, 225, 102, false, true, true);
						rect.setImage(img);
					}
				} catch(Exception e) {
					Image img = new Image("icon/gd_default.png", 225, 102, false, true, true);
					rect.setImage(img);
					e.printStackTrace();
				}
				return null;
			}
		};
		new Thread(async).start();
		
		nameBox.setMaxWidth(rect.getFitWidth());
		
		StackPane stackPane = new StackPane(buttons, rect);
		stackPane.setStyle("-fx-background-color: #2d2d2d");
		pane.getChildren().add(stackPane);
		
		GaussianBlur blur = new GaussianBlur(0);
		rect.setEffect(blur);
		DoubleProperty value = new SimpleDoubleProperty(0);
		value.addListener((observable, oldV, newV) ->
		{
			blur.setRadius(newV.doubleValue());
		});
		
		Timeline timeline = new Timeline();
		final KeyValue kv = new KeyValue(value, 10);
		final KeyFrame kf = new KeyFrame(Duration.millis(200), kv);
		timeline.getKeyFrames().add(kf);
		
		Timeline timeline2 = new Timeline();
		final KeyValue kv2 = new KeyValue(value, 0);
		final KeyFrame kf2 = new KeyFrame(Duration.millis(200), kv2);
		timeline2.getKeyFrames().add(kf2);
		
		
		FadeTransition fadeIn = new FadeTransition(Duration.millis(200));
		fadeIn.setNode(rect);
		fadeIn.setToValue(0.5);
		stackPane.setOnMouseEntered(e -> {
			fadeIn.playFromStart();
			timeline.playFromStart();
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					rect.toBack();
				}
			});
			isFocused = true;
		});
		
		FadeTransition fadeOut = new FadeTransition(Duration.millis(200));
		fadeOut.setNode(rect);
		fadeOut.setToValue(1);
		stackPane.setOnMouseExited(e -> {
			if((e.getX() > 0 && e.getX() < rect.getFitWidth()) && (e.getY() > 0 && e.getY() < rect.getFitHeight()))
				return;
			fadeOut.playFromStart();
			if(fadeIn.getStatus() == Animation.Status.RUNNING) {
				fadeIn.stop();
				timeline.stop();
			}
			timeline2.playFromStart();
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					rect.toFront();
				}
			});
			isFocused = false;
		});
		rect.setOpacity(1);
	}
	
	
	public void setUser(SteamUser user) {
		if(!user.exists())
			return;
		Pane p = new Pane();
		HBox.setHgrow(p, Priority.SOMETIMES);
		Image img = new Image(launcher.getImageManager().getUserIconURL(user.getID(), UserIconSize.ICON), 22, 22, false, true, true);
		ImageView view = new ImageView(img);
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				topBar.getChildren().add(0, p);
				topBar.getChildren().add(0, view);
			}
		});
	}
	
	public void unfocus() {
		if(!isFocused)
			return;
//		fadeOut.playFromStart();
//		if(fadeIn.getStatus() == Animation.Status.RUNNING) {
//			fadeIn.stop();
//			timeline.stop();
//		}
//		timeline2.playFromStart();
//		Platform.runLater(new Runnable() {
//			@Override
//			public void run() {
//				rect.toFront();
//			}
//		});
		isFocused = false;
	}
	
	private void colorIcon(ImageView icon) {
		ColorAdjust monochrome = new ColorAdjust();
		monochrome.setSaturation(-1.0);
		
		Blend blush = new Blend(
				BlendMode.MULTIPLY,
				monochrome,
				new ColorInput(
						0,
						0,
						icon.getImage().getWidth() + 10,
						icon.getImage().getHeight() + 10,
						CSSColorHelper.parseColor("-primary")
				)
		);
		
		icon.effectProperty().bind(
				Bindings
						.when(icon.visibleProperty())
						.then((Effect) blush)
						.otherwise((Effect) null)
		);
		
		icon.setCache(true);
		icon.setCacheHint(CacheHint.SPEED);
		ImageView view = new ImageView(icon.getImage());
		view.setFitWidth(icon.getFitWidth());
		view.setFitHeight(icon.getFitHeight());
		icon.setClip(view);
	}
	
	public abstract void onLink();
	
	public abstract void onEdit();
	
	public abstract void onDelete();
	
	public abstract void onRun();
	
	public boolean isShown() {
		return isShown;
	}
	
	public String getName() {
		return name;
	}
	
	public Pane getPane() {
		return pane;
	}
	
	public Application getApp() {
		return app;
	}
}
