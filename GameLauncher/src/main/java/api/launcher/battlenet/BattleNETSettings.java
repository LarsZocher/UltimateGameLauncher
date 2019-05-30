package api.launcher.battlenet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class BattleNETSettings {
	
	private final File CONFIG = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\Battle.net\\Battle.net.config");
	
	public boolean exists(){
		return CONFIG.exists();
	}
	
	public String loadConfig(){
		if(exists()){
			try {
				return FileUtils.readFileToString(CONFIG, "UTF-8");
			} catch(IOException e) {
			
			}
		}
		System.out.println("[BATTLENET] Config not found!");
		return "";
	}
	
	public void saveConfig(JSONObject object){
		saveConfig(object.toString());
	}
	
	public void saveConfig(String config){
		if(exists()){
			try {
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				JsonParser jp = new JsonParser();
				JsonElement je = jp.parse(config);
				String prettyJsonString = gson.toJson(je);
				
				PrintWriter writer = new PrintWriter(CONFIG);
				writer.println(prettyJsonString);
				writer.flush();
				writer.close();
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}
		}
	}
	
	public void setAutoLogin(boolean autoLogin){
		JSONObject json = new JSONObject(loadConfig());
		json.getJSONObject("Client").put("AutoLogin", autoLogin);
		saveConfig(json);
	}
	
	public boolean getAutoLogin(){
		JSONObject json = new JSONObject(loadConfig());
		return json.getJSONObject("Client").getBoolean("AutoLogin");
	}
	
	public String getInstallPath(){
		JSONObject json = new JSONObject(loadConfig());
		return json.getJSONObject("Client").getJSONObject("Install").getString("DefaultInstallPath");
	}
	
	public void setGameLaunchWindowBehavior(GameLaunchWindowBehavior behavior){
		JSONObject json = new JSONObject(loadConfig());
		json.getJSONObject("Client").put("GameLaunchWindowBehavior", behavior.getId());
		saveConfig(json);
	}
	
	public GameLaunchWindowBehavior getGameLaunchWindowBehavior(){
		JSONObject json = new JSONObject(loadConfig());
		return GameLaunchWindowBehavior.getByID(json.getJSONObject("Client").getInt("GameLaunchWindowBehavior"));
	}
	
	public void setRestoreWindowOnGameEnd(boolean restore){
		JSONObject json = new JSONObject(loadConfig());
		json.getJSONObject("Client").put("RestoreWindowOnGameEnd", restore);
		saveConfig(json);
	}
	
	public boolean getRestoreWindowOnGameEnd(){
		JSONObject json = new JSONObject(loadConfig());
		return json.getJSONObject("Client").getBoolean("RestoreWindowOnGameEnd");
	}
	
	public String getLauncherPath(){
		JSONObject json = new JSONObject(loadConfig());
		for(String key : json.keySet()) {
			JSONObject clientJson = json.getJSONObject(key);
			if(clientJson.has("Path")){
				return clientJson.getString("Path");
			}
		}
		return "";
	}
	
	public List<BattleNETGames> getOwnedGames(){
		JSONObject games = new JSONObject(loadConfig()).getJSONObject("Games");
		List<BattleNETGames> ownedGames = new ArrayList<>();
		for(String key : games.keySet()){
			JSONObject game = games.getJSONObject(key);
			for(BattleNETGames battleNetGame : BattleNETGames.values()){
				if(battleNetGame.getBattleNetConfigName().equalsIgnoreCase(game.getString("ServerUid"))){
					ownedGames.add(battleNetGame);
				}
			}
		}
		return ownedGames;
	}
	
	public List<String> getSavedAccountNames(){
		JSONObject json = new JSONObject(loadConfig());
		List<String> users = new ArrayList<>();
		users.addAll(Arrays.asList(json.getJSONObject("Client").getString("SavedAccountNames").split(",")));
		return users;
	}
	
	public void setSavedAccountNames(List<String> users){
		JSONObject json = new JSONObject(loadConfig());
		String userString = String.join(",", users);
		json.getJSONObject("Client").put("SavedAccountNames", userString);
		saveConfig(json);
	}
}
