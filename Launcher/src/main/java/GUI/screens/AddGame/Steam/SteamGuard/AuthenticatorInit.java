package GUI.screens.AddGame.Steam.SteamGuard;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public abstract class AuthenticatorInit{
	
	protected AuthenticatorMenu menu;
	protected boolean isCurrentlyShown = false;
	
	public void init(AuthenticatorMenu menu){
		this.menu = menu;
	}
	
	public void onShow(){}
	
	public boolean isCurrentlyShown() {
		return isCurrentlyShown;
	}
	
	public void setCurrentlyShown(boolean currentlyShown) {
		isCurrentlyShown = currentlyShown;
	}
}
