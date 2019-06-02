package gui.screens.misc;


import api.launcher.Application;
import api.launcher.GameLauncher;
import javafx.scene.layout.Pane;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class EmptyGameDisplay extends GameDisplay {
	
	@Override
	public Pane getPane() {
		return new Pane();
	}
	
	@Override
	public void onLink() {
	
	}
	
	@Override
	public void onEdit() {
	
	}
	
	@Override
	public void onDelete() {
	
	}
	
	@Override
	public void onRun() {
	
	}
}
