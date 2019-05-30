package api.launcher.battlenet;

import api.launcher.AppTypes;
import api.launcher.Application;
import api.launcher.GameLauncher;
import api.launcher.utils.JsonConfig;
import api.launcher.utils.JsonPartManager;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class BattleNET {
	
	private GameLauncher launcher;
	private JsonPartManager battleCfg;
	private BattleNETSettings settings;
	private File vbs;
	private boolean disabled = false;
	
	public BattleNET(GameLauncher launcher) {
		this.launcher = launcher;
		this.settings = new BattleNETSettings();
		if(!settings.exists()){
			disabled = true;
			return;
		}
		
		battleCfg = new JsonPartManager("BattleNet") {
			@Override
			public JSONObject onLoad(String key) {
				return JsonConfig.getJSONObject(launcher.cfg.getConfig(), key);
			}
			
			@Override
			public void onSave(JSONObject json, String key) {
				launcher.cfg.getConfig().put(key, json);
				launcher.cfg.save();
			}
		};
		battleCfg.load();
		JsonConfig.setDefault(battleCfg.get(), "path", settings.getLauncherPath() + "\\");
		battleCfg.save();
		
		users:
		for(String email : settings.getSavedAccountNames()) {
			for(BattleNETUser user : getUsers()) {
				if(user.getEmail().equalsIgnoreCase(email)) {
					continue users;
				}
			}
			addUser(email, email);
		}
		
		vbs = new File(launcher.folderPath + "CheckBattleNETScript.vbs");
		if(!vbs.exists()) {
			try {
				vbs.createNewFile();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		try {
			
			PrintWriter writer = new PrintWriter(vbs);
			writer.println("Set WshShell = WScript.CreateObject(\"WScript.Shell\")");
			writer.println("Dim counter");
			writer.println("counter = 30");
			writer.println("do");
			writer.println("counter = counter - 1");
			writer.println("If counter = 0 Then ");
			writer.println("    Wscript.Quit");
			writer.println("End If");
			writer.println("ret = wshShell.AppActivate(\"Blizzard Battle.net\")");
			writer.println("If ret = True Then ");
			writer.println("    Wscript.Quit");
			writer.println("End If");
			writer.println("WScript.Sleep 500 ");
			writer.println("Loop");
			writer.flush();
			writer.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		games:
		for(BattleNETGames game : BattleNETGames.values()) {
			JSONArray applications = JsonConfig.getJSONArray(launcher.cfg.getConfig(), "Applications");
			for(int i = 0; i < applications.length(); i++) {
				Application app = new Gson().fromJson(applications.getJSONObject(i).toString(), Application.class);
				if(app.getName().equalsIgnoreCase(game.getConfigName())) {
					continue games;
				}
			}
			
			Application application = new Application();
			application.setName(game.getConfigName());
			application.setDisplayName(game.getName());
			application.setType(AppTypes.BATTLENET);
			application.setUniqueID("BATTLENET_" + game.getCode());
			application.setCreated(System.currentTimeMillis());
			application.setIconPath("default");
			application.setHeaderPath("default");
			application.setDefaultIcon(true);
			application.setDefaultHeader(true);
			
			List<String> names = new ArrayList<>();
			names.add(game.getName());
			
			BattleNETGameConfig bgc = new BattleNETGameConfig();
			bgc.setNamesToSay(names);
			
			JSONObject appJson = new JSONObject(new Gson().toJson(application));
			appJson.put("content", new JSONObject(new Gson().toJson(bgc)));
			
			applications.put(appJson);
			launcher.cfg.getConfig().put("Applications", applications);
		}
		this.launcher.cfg.save();
	}
	
	public void launchWithUserSelection(BattleNETGames game, boolean forceSelectionWindow){
		if(disabled)
			return;
		if(getUsers().size()<2 && !forceSelectionWindow){
			launch(game);
			return;
		}
		if(new File("Launcher.jar").exists()){
			if(!launcher.jrePath.isEmpty()) {
				try {
					Runtime.getRuntime().exec("cmd.exe /K start "+launcher.jrePath+"javaw.exe -jar Launcher.jar --startBNet " + game.getConfigName());
				} catch(IOException e) {
					e.printStackTrace();
				}
			}else{
				try {
					Runtime.getRuntime().exec("cmd.exe /K java -jar Launcher.jar --startBNet " + game.getConfigName());
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void launch(BattleNETGames game) {
		launchGame(game.getCode());
	}
	
	public void launch(String game) {
		launchGame(BattleNETGames.getByConfigName(game).getCode());
	}
	
	private void launchGame(String code) {
		if(disabled)
			return;
		new Thread(() -> {
			try {
				if(isRunning()) {
					Runtime.getRuntime().exec("taskkill /F /IM Battle.net.exe");
				}
				Thread.sleep(500);
				ProcessBuilder pb = new ProcessBuilder(getDirectory() + "Battle.net.exe", "--exec=\"launch " + code + "\"");
				pb.directory(new File(getDirectory()));
				pb.start();
				
				ProcessBuilder start = new ProcessBuilder(System.getenv("WINDIR") + "\\system32\\wscript.exe", vbs.getAbsolutePath());
				Process p = start.start();
				while(p.isAlive()) {
					Thread.sleep(500);
				}
				
				Runtime.getRuntime().exec(getDirectory() + "Battle.net.exe --exec=\"launch " + code + "\"");
			} catch(IOException e) {
				e.printStackTrace();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		
	}
	
	public String getDirectory() {
		if(disabled)
			return "";
		battleCfg.load();
		return battleCfg.get().getString("path");
	}
	
	public void setDirectory(String path) {
		battleCfg.load();
		battleCfg.get().put("path", path);
		battleCfg.save();
	}
	
	public void setNamesToSay(BattleNETGames game, List<String> names) {
		JSONArray applications = JsonConfig.getJSONArray(launcher.cfg.getConfig(), "Applications");
		for(int i = 0; i < applications.length(); i++) {
			Application app = new Gson().fromJson(applications.getJSONObject(i).toString(), Application.class);
			if(app.getName().equalsIgnoreCase(game.getConfigName())) {
				
				JSONObject appJson = new JSONObject(new Gson().toJson(app));
				
				BattleNETGameConfig bgc = new Gson().fromJson(appJson.getJSONObject("content").toString(), BattleNETGameConfig.class);
				bgc.setNamesToSay(names);
				
				appJson.put("content", new JSONObject(new Gson().toJson(bgc)));
				
				applications.put(appJson);
				launcher.cfg.getConfig().put("Applications", applications);
				this.launcher.cfg.save();
				break;
			}
		}
	}
	
	public List<String> getNamesToSay(BattleNETGames game) {
		return launcher.getApplication(game.getConfigName()).getContent(BattleNETGameConfig.class).getNamesToSay();
	}
	
	public BattleNETUser getUser(String email) {
		battleCfg.load();
		for(int i = 0; i < battleCfg.getJSONArray("Users").length(); i++) {
			BattleNETUser user = new Gson().fromJson(battleCfg.getJSONArray("Users").getJSONObject(i).toString(), BattleNETUser.class);
			if(user.getEmail().equalsIgnoreCase(email)) {
				return user;
			}
		}
		return new BattleNETUser();
	}
	
	public List<BattleNETUser> getUsers() {
		battleCfg.load();
		List<BattleNETUser> users = new ArrayList<>();
		for(int i = 0; i < battleCfg.getJSONArray("Users").length(); i++) {
			users.add(new Gson().fromJson(battleCfg.getJSONArray("Users").getJSONObject(i).toString(), BattleNETUser.class));
		}
		return users;
	}
	
	public void addUser(String email, String name) {
		BattleNETUser user = new BattleNETUser();
		user.setEmail(email);
		user.setName(name);
		addUser(user);
	}
	
	public void addUser(BattleNETUser user) {
		if(disabled)
			return;
		user.setLastLogIn(0);
		battleCfg.load();
		JSONArray users = battleCfg.getJSONArray("Users");
		for(int i = 0; i < users.length(); i++) {
			if(users.getJSONObject(i).getString("email").equalsIgnoreCase(user.getEmail()))
				return;
		}
		users.put(new JSONObject(new Gson().toJson(user)));
		battleCfg.get().put("Users", users);
		battleCfg.save();
		
		List<String> accounts = settings.getSavedAccountNames();
		if(!accounts.contains(user.getEmail()))
			accounts.add(user.getEmail());
		settings.setSavedAccountNames(accounts);
	}
	
	public void removeUser(BattleNETUser user) {
		removeUser(user.getEmail());
	}
	
	public void removeUser(String email) {
		if(disabled)
			return;
		battleCfg.load();
		int id = -1;
		for(int i = 0; i < battleCfg.getJSONArray("Users").length(); i++) {
			if(battleCfg.getJSONArray("Users").getJSONObject(i).getString("email").equalsIgnoreCase(email)) {
				id = i;
				break;
			}
		}
		if(id != -1) {
			battleCfg.getJSONArray("Users").remove(id);
			battleCfg.save();
		}
		
		List<String> accounts = settings.getSavedAccountNames();
		if(accounts.contains(email))
			accounts.remove(email);
		settings.setSavedAccountNames(accounts);
	}
	
	public void changeUser(BattleNETUser user) {
		if(disabled)
			return;
		List<String> newUserList = new ArrayList<>();
		newUserList.add(user.getEmail());
		for(String users : settings.getSavedAccountNames()) {
			if(!users.equalsIgnoreCase(user.getEmail()))
				newUserList.add(users);
		}
		settings.setSavedAccountNames(newUserList);
		
		battleCfg.load();
		battleCfg.get().put("lastUsedAccount", user.getEmail());
		battleCfg.save();
	}
	
	public void forceChangeUser(BattleNETUser user) {
		if(disabled)
			return;
		if(user.getEmail().isEmpty())
			return;
		
		try {
			changeUser(user);
			
			Runtime.getRuntime().exec("taskkill /F /IM Battle.net.exe");
			Thread.sleep(500);
			ProcessBuilder pb = new ProcessBuilder(getDirectory() + "Battle.net.exe");
			pb.directory(new File(getDirectory()));
			pb.start();
			
		} catch(InterruptedException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(Throwable throwable) {
			throwable.printStackTrace();
		}
	}
	
	public String getLastUser() {
		battleCfg.load();
		return battleCfg.get().getString("lastUsedAccount");
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
			
			if(pidInfo.contains("Battle.net.exe")) {
				return true;
			}
			return false;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public BattleNETSettings getSettings() {
		return settings;
	}
	
	public boolean isDisabled() {
		return disabled;
	}
	
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	public static void main(String[] args) {
		GameLauncher launcher = new GameLauncher();
		new BattleNET(launcher).launch(BattleNETGames.OVERWATCH);
	}
	
}
