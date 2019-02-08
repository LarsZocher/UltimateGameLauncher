package api.GameLauncher.BattleNET;

import api.GameLauncher.Content;

import java.util.List;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class BattleNETGameConfig extends Content {
	
	private List<String> namesToSay;
	private String icon = "default";
	
	public List<String> getNamesToSay() {
		return namesToSay;
	}
	
	public void setNamesToSay(List<String> namesToSay) {
		this.namesToSay = namesToSay;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
}
