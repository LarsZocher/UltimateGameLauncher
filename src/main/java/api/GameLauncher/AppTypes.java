package api.GameLauncher;

import api.GameLauncher.BattleNET.BattleNETGameConfig;
import api.GameLauncher.Origin.OriginGame;
import api.GameLauncher.Steam.SteamApp;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author CodingAir
 * @verions: 1.0.0
 **/

public enum AppTypes {
	
	ALL("Alle", "ALLE", false, Content.class),
	STEAM("Steam", "STEAM", true, SteamApp.class),
	BATTLENET("BattleNET", "BattleNET", false, BattleNETGameConfig.class),
	ORIGIN("Origin", "Origin", false, OriginGame.class),
	UPLAY("UPlay", "UPlay", true, Content.class);
	
	private String name;
	private String GUIName;
	private boolean isCreateable;
	private Class content;
	
	AppTypes(String name, String GUIName, boolean isCreateable, Class content){
		this.name = name;
		this.GUIName = GUIName;
		this.isCreateable = isCreateable;
		this.content = content;
	}
	
	public String getName() {
		return name;
	}
	
	public String getGUIName() {
		return GUIName;
	}
	
	public boolean isCreateable() {
		return isCreateable;
	}
	
	public static AppTypes getByName(String name){
		for(AppTypes type : AppTypes.values()) {
			if(type.getName().equalsIgnoreCase(name))
				return type;
		}
		return STEAM;
	}
	public static AppTypes getByGUIName(String name){
		for(AppTypes type : AppTypes.values()) {
			if(type.getGUIName().equalsIgnoreCase(name))
				return type;
		}
		return STEAM;
	}
	
	public Class getContent() {
		return content;
	}
}
