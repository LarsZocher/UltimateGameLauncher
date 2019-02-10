package api.GameLauncher.Steam;

import api.GameLauncher.AppTypes;
import api.GameLauncher.Application;
import api.GameLauncher.GameLauncher;
import api.GameLauncher.Utils.JsonConfig;
import api.GameLauncher.Utils.JsonPartManager;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class Steam {
	
	private GameLauncher launcher;
	private SteamGuard steamGuard;
	private JsonPartManager steamManager;
	
	public Steam(GameLauncher launcher) {
		this.launcher = launcher;
		this.steamGuard = new SteamGuard(launcher);
		
		
		this.steamManager = new JsonPartManager("Steam") {
			@Override
			public JSONObject onLoad(String key) {
				launcher.cfg.load();
				return JsonConfig.getJSONObject(launcher.cfg.getConfig(),key);
			}
			
			@Override
			public void onSave(JSONObject json, String key) {
				launcher.cfg.getConfig().put(key, json);
				launcher.cfg.save();
			}
		};
		
		steamManager.load();
		JsonConfig.setDefault(steamManager.get(), "path", findDirectory());
		JsonConfig.setDefault(steamManager.get(), "developer", false);
		JsonConfig.setDefault(steamManager.get(), "lastUsedAccount", "none");
		steamManager.save();
		
		loadAccounts();
	}
	
	public List<SteamConfigUser> loadAccounts() {
		List<SteamConfigUser> users = new ArrayList<>();
		if(!getPath().isEmpty()) {
			try {
				File file = new File(getPath() + "config/config.vdf");
				String data = FileUtils.readFileToString(file, "UTF-8");
				data = data.split("\"Accounts\"")[1];
				String[] dataParts = data.split("\"");
				for(int i = 1; i < dataParts.length; i++) {
					String username = dataParts[i];
					if(!dataParts[i + 2].equalsIgnoreCase("SteamID")){
						break;
					}
					String id = dataParts[i + 4];
					i += 5;
					
					SteamConfigUser user = new SteamConfigUser();
					user.setSteam64id(id);
					user.setUsername(username);
					users.add(user);
				}
			}catch(Exception e){
				System.out.println("Failed to load Steam users");
			}
		}
		return users;
	}
	
	public List<SteamApp> getAppsFromUser(SteamUser user, boolean free){
		return getAppsFromUser(user.getID(), free);
	}
	
	public List<SteamApp> getAppsFromUser(String steam64id, boolean free){
		System.out.println("Searching games from: "+steam64id);
		List<SteamApp> apps = new ArrayList<>();
		try {
			HttpURLConnection con = (HttpURLConnection) new URL("https://api.steampowered.com/IPlayerService/GetOwnedGames/v1/?key=ECF61DC7A97863B4871287BF468A51D0&steamid="+steam64id+"&include_appinfo=0&include_played_free_games="+(free?1:0)).openConnection();
			con.setRequestMethod("GET");
			
			con.setDoOutput(true);
			
			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			
			while((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			JSONObject responseJSON = new JSONObject(response.toString());
			System.out.println(response.toString());
			if(responseJSON.getJSONObject("response").has("games")) {
				JSONArray games = responseJSON.getJSONObject("response").getJSONArray("games");
				for(int i = 0; i < games.length(); i++) {
					apps.add(SteamDB.getSteamAppByID(games.getJSONObject(i).getInt("appid") + ""));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return apps;
	}
	
	public void createUser(String username, String password) {
		SteamUser user = new SteamUser(launcher, username);
		for(SteamConfigUser scu : loadAccounts()){
			if(scu.getUsername().equalsIgnoreCase(username)){
				user.createUser(password, scu.getSteam64id());
			}
		}
	}
	
	public void createUser(String username, String password, String steam64id) {
		SteamUser user = new SteamUser(launcher, username);
		user.createUser(password, steam64id);
	}
	
	public void deleteUser(String username) {
		SteamUser user = new SteamUser(launcher, username);
		user.delete();
	}
	
	public SteamUser getUser(String username) {
		return new SteamUser(launcher, username);
	}
	
	public List<String> getUsernames() {
		steamManager.load();
		List<String> users = new ArrayList<>();
		for(int i = 0; i<steamManager.getJSONArray("Users").length(); i++){
			users.add(steamManager.getJSONArray("Users").getJSONObject(i).getString("username"));
		}
		return users;
	}
	
	public void addApp(SteamApp app) {
		this.launcher.cfg.load();
		
		Application application = null;
		for(Application apps : launcher.getApplications()){
			if(apps.getName().equalsIgnoreCase(app.getConfigName())){
				application = apps;
				removeApp(app.getConfigName());
			}
		}
		
		if(application==null) {
			application = new Application();
			application.setName(app.getConfigName());
			application.setType(AppTypes.STEAM);
			application.setCreated(System.currentTimeMillis());
			application.setUniqueID("STEAM_"+app.getAppID());
		}
		
		JSONArray applications = JsonConfig.getJSONArray(launcher.cfg.getConfig(),"Applications");
		JSONObject appJson = new JSONObject(new Gson().toJson(application));
		appJson.put("content", new JSONObject(new Gson().toJson(app)));
		
		applications.put(appJson);
		
		launcher.cfg.getConfig().put("Applications", applications);
		
		this.launcher.cfg.save();
	}
	
	public SteamApp getApp(String name) {
		this.launcher.cfg.load();
		
		JSONArray applications = JsonConfig.getJSONArray(launcher.cfg.getConfig(),"Applications");
		int id = 0;
		for(int i = 0; i < applications.length(); i++) {
			if(applications.getJSONObject(i).getString("name").equalsIgnoreCase(name)) {
				if(applications.getJSONObject(i).getString("type").equalsIgnoreCase(AppTypes.STEAM.name())) {
					id = i;
					break;
				}
			}
		}
		
		return new Gson().fromJson(applications.getJSONObject(id).getJSONObject("content").toString(), SteamApp.class);
	}
	
	public void checkCompatibility(SteamApp app) {
		SteamApp newApp = SteamDB.getSteamAppByID(app.getAppID());
		app.setPathToPicture(newApp.getPathToPicture());
		app.setPathToBackground(newApp.getPathToBackground());
		addApp(app);
	}
	
	public void removeApp(String name) {
		this.launcher.cfg.load();
		JSONArray applications = JsonConfig.getJSONArray(launcher.cfg.getConfig(),"Applications");
		int id = 0;
		for(int i = 0; i < applications.length(); i++) {
			if(applications.getJSONObject(i).getString("name").equalsIgnoreCase(name)) {
				if(applications.getJSONObject(i).getString("type").equalsIgnoreCase(AppTypes.STEAM.name())) {
					id = i;
					break;
				}
			}
		}
		
		applications.remove(id);
		this.launcher.cfg.getConfig().put("Applications", applications);
		this.launcher.cfg.save();
	}
	
	public List<String> getApps() {
		this.launcher.cfg.load();
		List<String> apps = new ArrayList<>();
		JSONArray applications = JsonConfig.getJSONArray(launcher.cfg.getConfig(),"Applications");
		for(int i = 0; i < applications.length(); i++) {
			if(applications.getJSONObject(i).getString("type").equalsIgnoreCase(AppTypes.STEAM.name())) {
				apps.add(applications.getJSONObject(i).getString("name"));
			}
		}
		return apps;
	}
	
	public void setPath(String path) {
		steamManager.load();
		steamManager.get().put("path", path);
		steamManager.save();
	}
	
	public String getPath() {
		steamManager.load();
		return steamManager.get().getString("path");
	}
	
	public String getSteamExecutable(){
		return getPath()+"Steam.exe";
	}
	
	public void launchByNameToSay(String name) {
		for(String appName : getApps()) {
			SteamApp app = getApp(appName);
			for(String nameToSay : app.getNamesToSay()) {
				if(nameToSay.equalsIgnoreCase(name)) {
					launch(app);
					return;
				}
			}
		}
	}
	
	public void launch(String name) {
		launch(getApp(name));
	}
	
	public void launch(SteamApp app) {
		if(app.getAppID().isEmpty()) {
			System.out.println("AppID is empty!");
		}
		if(!app.getUser().isEmpty()) {
			try {
				SteamUser user = getUser(app.getUser());
				
				steamManager.load();
				if(!steamManager.get().getString("lastUsedAccount").equalsIgnoreCase(user.getUsername())) {
					Runtime.getRuntime().exec("taskkill /F /IM steam.exe");
					Thread.sleep(1000);
				}
				boolean isSteamRunning = isRunning();
				
				Runtime.getRuntime().exec(getSteamExecutable() + " -login " + user.getUsername() + " " + user.getPassword() + " " + (getDeveloperMode() ? "-dev -console" : "") + " -applaunch " + app.getAppID() + " " + app.getArgs());
				steamManager.get().put("lastUsedAccount", user.getUsername());
				steamManager.save();
				if(user.hasSteamGuard() && !isSteamRunning) {
					Thread.sleep(4000);
					steamGuard.enterCode(user);
				}
			} catch(InterruptedException e) {
				e.printStackTrace();
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Runtime.getRuntime().exec(getSteamExecutable() + " -applaunch " + app.getAppID());
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setDeveloperMode(boolean enabled) {
		steamManager.load();
		steamManager.get().put("developer", enabled);
		steamManager.save();
	}
	
	public boolean getDeveloperMode() {
		steamManager.load();
		return steamManager.get().getBoolean("developer");
	}
	
	public void changeUser(SteamUser user) {
		if(user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
			return;
		}
		try {
			steamManager.load();
			if(!steamManager.get().getString("lastUsedAccount").equalsIgnoreCase(user.getUsername())) {
				Runtime.getRuntime().exec("taskkill /F /IM steam.exe");
				Thread.sleep(1000);
			}
			
			boolean isSteamRunning = isRunning();
			
			Runtime.getRuntime().exec(getSteamExecutable() + " -login " + user.getUsername() + " " + user.getPassword() + " " + (getDeveloperMode() ? "-dev -console" : ""));
			steamManager.get().put("lastUsedAccount", user.getUsername());
			steamManager.save();
			if(user.hasSteamGuard() && !isSteamRunning) {
				Thread.sleep(4000);
				steamGuard.enterCode(user);
			}
		} catch(InterruptedException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void forceChangeUser(SteamUser user) {
		if(user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
			return;
		}
		try {
			Runtime.getRuntime().exec("taskkill /F /IM steam.exe");
			Thread.sleep(500);
			Runtime.getRuntime().exec(getSteamExecutable() + " -login " + user.getUsername() + " " + user.getPassword() + " " + (getDeveloperMode() ? "-dev -console" : ""));
			
			steamManager.load();
			steamManager.get().put("lastUsedAccount", user.getUsername());
			steamManager.save();
			
			if(user.hasSteamGuard()) {
				Thread.sleep(4000);
				steamGuard.enterCode(user);
			}
		} catch(InterruptedException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(Throwable throwable) {
			throwable.printStackTrace();
		}
	}
	
	public boolean isRunning() {
		try {
			String line;
			String pidInfo = "";
			
			Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
			
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			while((line = input.readLine()) != null) {
				pidInfo += line;
			}
			
			input.close();
			
			if(pidInfo.contains("Steam.exe")) {
				return true;
			}
			return false;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String getLastUser() {
		steamManager.load();
		return steamManager.get().getString("lastUsedAccount");
	}
	
	public SteamUser getMainAccount() {
		steamManager.load();
		SteamUser user = getUser(steamManager.get().getString("mainUser"));
		if(user.exists())
			return getUser(steamManager.get().getString("mainUser"));
		else {
			try {
				getUser(getUsernames().get(0)).setAsMainAccount();
				return getUser(getUsernames().get(0));
			} catch(Exception e) {
				return null;
			}
		}
	}
	
	public String findDirectory(){
		List<String> defaults = new ArrayList<>();
		defaults.add("Program Files (x86)\\Steam\\");
		defaults.add("Program Files\\Steam\\");
		String mustContain = "Steam.exe";
		for(File drive : File.listRoots()){
			for(String path : defaults){
				File file = new File(drive.getPath()+path+mustContain);
				if(file.exists()){
					return drive.getPath()+path;
				}
			}
		}
		return "";
	}
	
	public SteamGuard getSteamGuard() {
		return steamGuard;
	}
	
	public JsonPartManager getSteamManager() {
		return steamManager;
	}
}
