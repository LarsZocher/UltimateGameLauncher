package api.launcher.battlenet;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public enum GameLaunchWindowBehavior {
	KEEP_BATTLENET_OPEN(0),
	MINIMIZE(1),
	MINIMIZE_TO_SYSTEM_TRAY(3),
	EXIT_BATTLENET(2);
	
	int id;
	
	GameLaunchWindowBehavior(int id) {
		this.id = id;
	}
	
	public static GameLaunchWindowBehavior getByID(int id){
		for(GameLaunchWindowBehavior value : GameLaunchWindowBehavior.values()) {
			if(value.getId()==id)
				return value;
		}
		return MINIMIZE;
	}
	
	public int getId() {
		return id;
	}
}
