package api.launcher.battlenet;

import api.launcher.Content;

import java.util.List;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class BattleNETGameConfig extends Content {
	
	private List<String> namesToSay;
	
	public List<String> getNamesToSay() {
		return namesToSay;
	}
	
	public void setNamesToSay(List<String> namesToSay) {
		this.namesToSay = namesToSay;
	}
}
