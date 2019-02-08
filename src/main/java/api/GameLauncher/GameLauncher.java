package api.GameLauncher;

import Utils.FileManager.ConfigFile;
import api.GameLauncher.BattleNET.BattleNET;
import api.GameLauncher.BattleNET.BattleNETGames;
import api.GameLauncher.Origin.Origin;
import api.GameLauncher.Steam.Steam;
import api.GameLauncher.Steam.SteamApp;
import api.GameLauncher.Steam.SteamDB;
import api.GameLauncher.Steam.SteamUser;
import api.GameLauncher.Utils.JsonConfig;
import com.google.gson.Gson;
import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class GameLauncher {
	
	
	public Steam steam;
	public BattleNET battleNET;
	public Origin origin;
	public String folderPath = "GameLauncher/";
	public String gameLauncherName = "GameLauncher.jar";
	public String jrePath = "jre/bin/";
	public JsonConfig cfg;
	
	public GameLauncher() {
		File dotbot = new File("DotBot.jar");
		if(dotbot.exists()) {
			folderPath = "plugins/GameLauncher/";
			gameLauncherName = "DotBotGameLauncher.jar";
			jrePath = "";
			File folder = new File("plugins/GameLauncher");
			if(!folder.exists()) {
				folder.mkdir();
			}
			cfg = new JsonConfig(folderPath + "config.json");
		} else {
			folderPath = "GameLauncher/";
			File folder = new File("GameLauncher");
			if(!folder.exists()) {
				folder.mkdir();
			}
			cfg = new JsonConfig(folderPath + "config.json");
		}
		cfg.createFile();
		
		File userFolder = new File(folderPath + "Users");
		if(!userFolder.exists()) {
			userFolder.mkdir();
		}
		
		File gamesFolder = new File(folderPath + "Games");
		if(!gamesFolder.exists()) {
			gamesFolder.mkdir();
		}
		
		steam = new Steam(this);
		battleNET = new BattleNET(this);
		origin = new Origin(this);
	}
	
	public void launch(SteamApp app){
		getSteam().launch(app);
	}
	
	public void launch(BattleNETGames game){
		getBattleNET().launch(game);
	}
	
	public void launchByConfigName(String name){
		for(Application application : getApplications()) {
			if(application.getName().equalsIgnoreCase(name)){
				switch(application.getType()){
					case STEAM:{
						steam.launch(name);
						break;
					}
					case BATTLENET:{
						battleNET.launch(name);
						break;
					}
					case ORIGIN:{
						origin.launch(name);
						break;
					}
				}
				break;
			}
		}
	}
	
	public void launchByNameToSay(String name){
		List<nameInformation> names = new ArrayList<>();
		for(String s : getSteam().getApps()) {
			names.add(new nameInformation(s, getSteam().getApp(s).getNamesToSay(), AppTypes.STEAM));
		}
		for(BattleNETGames battleNETGames : BattleNETGames.values()) {
			names.add(new nameInformation(battleNETGames.getConfigName(), getBattleNET().getNamesToSay(battleNETGames), AppTypes.BATTLENET));
		}
		
		loop:for(nameInformation info : names){
			for(String namesToSay : info.getNamesToSay()){
				if(namesToSay.equalsIgnoreCase(name)){
					switch(info.getType()){
						case STEAM:{
							launchByConfigName(info.getName());
							break;
						}
						case BATTLENET:{
							launchByConfigName(info.getName());
							break;
						}
					}
					break loop;
				}
			}
		}
		return;
	}
	
	public List<Application> getApplications(){
		cfg.load();
		JSONArray applications = JsonConfig.getJSONArray(cfg.getConfig(),"Applications");
		
		List<Application> apps = new ArrayList<>();
		for(int i = 0; i < applications.length(); i++) {
			Application app = new Gson().fromJson(applications.getJSONObject(i).toString(), Application.class);
			app.setContent(applications.getJSONObject(i));
			apps.add(app);
		}
		return apps;
	}
	
	public List<Application> getApplications(AppTypes type){
		cfg.load();
		JSONArray applications = JsonConfig.getJSONArray(cfg.getConfig(),"Applications");
		
		List<Application> apps = new ArrayList<>();
		for(int i = 0; i < applications.length(); i++) {
			Application app = new Gson().fromJson(applications.getJSONObject(i).toString(), Application.class);
			if(app.getType()!=type)
				continue;
			app.setContent(applications.getJSONObject(i));
			apps.add(app);
		}
		return apps;
	}
	
	public Application getApplication(String name){
		cfg.load();
		JSONArray applications = JsonConfig.getJSONArray(cfg.getConfig(),"Applications");
		
		for(int i = 0; i < applications.length(); i++) {
			Application app = new Gson().fromJson(applications.getJSONObject(i).toString(), Application.class);
			if(app.getName().equalsIgnoreCase(name))
				return app;
		}
		return null;
	}
	
	public List<String> getApplicationsNames(){
		cfg.load();
		JSONArray applications = JsonConfig.getJSONArray(cfg.getConfig(),"Applications");
		
		List<String> apps = new ArrayList<>();
		for(int i = 0; i < applications.length(); i++) {
			apps.add(applications.getJSONObject(i).getString("name"));
		}
		return apps;
	}
	
	public static void main(String[] args) {
		GameLauncher launcher = new GameLauncher();
		
		boolean usedArgs = false;
		for(int i = 0; i < args.length; i++) {
			if(args[i].equalsIgnoreCase("--user")) {
				usedArgs = true;
				SteamUser user = new SteamUser(launcher, args[i + 1]);
				launcher.steam.forceChangeUser(user);
			}
			if(args[i].equalsIgnoreCase("--start")) {
				usedArgs = true;
				launcher.launchByConfigName(args[i+1]);
			}
		}
		if(usedArgs)
			return;
		
		Scanner s = new Scanner(System.in);
		
		while(true) {
			String command = s.nextLine();
			switch(command) {
				case "create user": {
					System.out.print("Username: ");
					String username = s.nextLine();
					System.out.print("Password: ");
					String password = s.nextLine();
					
					launcher.steam.createUser(username, password, "");
					break;
				}
				case "delete user": {
					System.out.print("Username: ");
					String username = s.nextLine();
					launcher.steam.deleteUser(username);
					break;
				}
				case "create": {
					System.out.print("Name: ");
					String name = s.nextLine();
					System.out.print("Username: ");
					String user = s.nextLine();
					List<String> names = new ArrayList<>();
					names.add(name);
					
					SteamApp app = SteamDB.getSteamAppByName(name);
					app.setUser(user);
					app.setNamesToSay(names);
					app.setConfigName(name);
					
					launcher.steam.addApp(app);
					break;
				}
				case "delete": {
					System.out.print("Name: ");
					String name = s.nextLine();
					launcher.steam.removeApp(name);
					break;
				}
				case "start": {
					System.out.print("Name: ");
					String name = s.nextLine();
					launcher.steam.launchByNameToSay(name);
					break;
				}
				case "test": {
					System.out.print("Name: ");
					String name = s.nextLine();
					SteamUser user = new SteamUser(launcher, name);
					user.createShortcutOnDesktop();
					break;
				}
				case "search": {
					System.out.print("Name: ");
					String name = s.nextLine();
					System.out.println(SteamDB.getSteamAppByName(name).hasAlreadyAShortcut());
					break;
				}
			}
		}
	}
	
	public Steam getSteam() {
		return this.steam;
	}
	
	public BattleNET getBattleNET() {
		return battleNET;
	}
	
	public Origin getOrigin() {
		return origin;
	}
	
	public void start(String name) {
	
	}
	
}
class nameInformation{
	private String name;
	private List<String> namesToSay;
	private AppTypes type;
	
	public nameInformation(String name, List<String> namesToSay, AppTypes type) {
		this.name = name;
		this.namesToSay = namesToSay;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public AppTypes getType() {
		return type;
	}
	
	public void setType(AppTypes type) {
		this.type = type;
	}
	
	public List<String> getNamesToSay() {
		return namesToSay;
	}
	
	public void setNamesToSay(List<String> namesToSay) {
		this.namesToSay = namesToSay;
	}
}
