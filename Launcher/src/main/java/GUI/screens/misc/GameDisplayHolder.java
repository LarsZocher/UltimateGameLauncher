package gui.screens.misc;

import javafx.scene.layout.Pane;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class GameDisplayHolder {
	
	private Pane pane = new Pane();
	private String appName;
	private String name;
	
	public GameDisplayHolder(String appName, String name) {
		this.appName = appName;
		this.name = name;
		this.pane.setPrefWidth(225);
		this.pane.setPrefHeight(103);
		this.pane.setStyle("-fx-background-color: #2d2d2d");
	}
	
	public String getAppName() {
		return appName;
	}
	
	public String getName() {
		return name;
	}
	
	public Pane getPane() {
		return pane;
	}
}
