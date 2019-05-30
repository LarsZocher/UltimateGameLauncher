package api.launcher.battlenet;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public enum BattleNETGames {
	OVERWATCH("Overwatch", "Pro", "prometheus"),
	STARCRAFT_2("StarCraft II", "S2", "s2_dede"),
	STARCRAFT("StarCraft", "S1", "s1"),
	HOTS("Heroes of the Storm", "Hero", "heroes"),
	WOW("World of Warcraft", "WoW", "wow_dede"),
	WARCRAFT_3("Warcraft III", "W3", ""),
	DIABLO_3("Diablo III", "D3", "diablo3_dede"),
	HEARTHSTONE("Hearthstone", "WTCG", "hs_beta"),
	DESTINY_2("Destiny 2", "DST2", "destiny2"),
	COD_BO4("Call of Duty: Black Ops 4", "VIPR", "viper");
	
	private String name;
	private String code;
	private String configName;
	
	BattleNETGames(String name, String code, String configName) {
		this.name = name;
		this.code = code;
		this.configName = configName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getBattleNetConfigName(){
		return configName;
	}
	
	public static BattleNETGames getByName(String name){
		for(BattleNETGames games : BattleNETGames.values()) {
			if(games.getName().equalsIgnoreCase(name)){
				return games;
			}
		}
		return OVERWATCH;
	}
	
	public static BattleNETGames getByConfigName(String name){
		for(BattleNETGames games : BattleNETGames.values()) {
			if(games.getConfigName().equalsIgnoreCase(name)){
				return games;
			}
		}
		return OVERWATCH;
	}
	
	public boolean hasConfigName(){
		return !configName.isEmpty();
	}
	
	public String getConfigName(){
		String name = this.name;
		name = name.toLowerCase();
		name = name.replaceAll("\\.", "");
		name = name.replaceAll("/", "");
		name = name.replaceAll(",", "");
		name = name.replaceAll(":", "");
		name = name.replaceAll(" ", "_");
		return name;
	}
}
