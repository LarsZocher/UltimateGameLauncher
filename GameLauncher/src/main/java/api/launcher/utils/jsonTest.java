package api.launcher.utils;

import org.json.JSONObject;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class jsonTest {
	
	public static void main(String[] args){
		JsonConfig config = new JsonConfig("test.json");
		JSONObject steam = new JSONObject();
		JsonConfig.setDefault(steam, "Path", "");
		JsonConfig.setDefault(steam, "Steam.Developer", false);
		JsonConfig.setDefault(steam, "Steam.LastUsedAccount", "none");
		config.load();
		config.getConfig().put("Steam", steam);
		config.save();
	}
}
