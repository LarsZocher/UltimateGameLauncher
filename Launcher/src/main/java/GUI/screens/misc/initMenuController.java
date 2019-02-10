package GUI.screens.misc;

import GUI.Menu;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public abstract class initMenuController {
	
	protected Menu menu;
	protected boolean isCurrentlyShown = false;
	
	public void init(Menu menu){
		this.menu = menu;
	}
	
	public void onShow(){}
	
	public boolean isCurrentlyShown() {
		return isCurrentlyShown;
	}
	
	public void setCurrentlyShown(boolean currentlyShown) {
		isCurrentlyShown = currentlyShown;
	}
	
	public abstract void onRefresh();
}
