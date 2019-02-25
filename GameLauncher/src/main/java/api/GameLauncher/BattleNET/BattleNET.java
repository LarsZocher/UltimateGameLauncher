package api.GameLauncher.BattleNET;

import api.GameLauncher.AppTypes;
import api.GameLauncher.Application;
import api.GameLauncher.GameLauncher;
import api.GameLauncher.Utils.JsonConfig;
import api.GameLauncher.Utils.JsonPartManager;
import com.google.gson.Gson;
import mslinks.ShellLink;
import net.sf.image4j.codec.ico.ICOEncoder;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
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
	
	public BattleNET(GameLauncher launcher) {
		this.launcher = launcher;
		this.settings = new BattleNETSettings();
		
		this.launcher.cfg.load();
		
		battleCfg = new JsonPartManager("BattleNet") {
			@Override
			public JSONObject onLoad(String key) {
				launcher.cfg.load();
				return JsonConfig.getJSONObject(launcher.cfg.getConfig(), key);
			}
			
			@Override
			public void onSave(JSONObject json, String key) {
				launcher.cfg.getConfig().put(key, json);
				launcher.cfg.save();
			}
		};
		battleCfg.load();
		JsonConfig.setDefault(battleCfg.get(), "path", settings.getLauncherPath()+"\\\\");
		battleCfg.save();
		
		vbs = new File(launcher.folderPath + "CheckBattleNETScript.vbs");
		if(!vbs.exists()){
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
			application.setType(AppTypes.BATTLENET);
			application.setUniqueID("BATTLENET_" + game.getCode());
			application.setCreated(System.currentTimeMillis());
			
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
	
	public void launch(BattleNETGames game) {
		launchGame(game.getCode());
	}
	
	public void launch(String game) {
		launchGame(BattleNETGames.getByConfigName(game).getCode());
	}
	
	private void launchGame(String code) {
		new Thread(() -> {
			try {
				if(!isRunning()) {
					ProcessBuilder pb = new ProcessBuilder(getDirectory() + "Battle.net.exe", "--exec=\"launch " + code + "\"");
					pb.directory(new File(getDirectory()));
					pb.start();
					
					ProcessBuilder start = new ProcessBuilder(System.getenv("WINDIR") + "\\system32\\wscript.exe",vbs.getAbsolutePath());
					Process p = start.start();
					while(p.isAlive()){
						Thread.sleep(500);
					}
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
		battleCfg.load();
		return battleCfg.get().getString("path");
	}
	
	public void setDirectory(String path) {
		battleCfg.load();
		battleCfg.get().put("path", path);
		battleCfg.save();
	}
	
	public void setNamesToSay(BattleNETGames game, List<String> names) {
		launcher.cfg.load();
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
		launcher.cfg.load();
		JSONArray applications = JsonConfig.getJSONArray(launcher.cfg.getConfig(), "Applications");
		for(int i = 0; i < applications.length(); i++) {
			Application app = new Gson().fromJson(applications.getJSONObject(i).toString(), Application.class);
			if(app.getName().equalsIgnoreCase(game.getConfigName())) {
				
				JSONObject appJson = new JSONObject(new Gson().toJson(app));
				
				BattleNETGameConfig bgc = new Gson().fromJson(appJson.getJSONObject("content").toString(), BattleNETGameConfig.class);
				return bgc.getNamesToSay();
			}
		}
		return null;
	}
	
	public BattleNETUser getUser(String email){
		battleCfg.load();
		for(int i = 0; i<battleCfg.getJSONArray("Users").length(); i++){
			BattleNETUser user = new Gson().fromJson(battleCfg.getJSONArray("Users").getJSONObject(i).toString(), BattleNETUser.class);
			if(user.getEmail().equalsIgnoreCase(email)){
				return user;
			}
		}
		return new BattleNETUser();
	}
	
	public List<BattleNETUser> getUsers(){
		battleCfg.load();
		List<BattleNETUser> users = new ArrayList<>();
		for(int i = 0; i<battleCfg.getJSONArray("Users").length(); i++){
			users.add(new Gson().fromJson(battleCfg.getJSONArray("Users").getJSONObject(i).toString(), BattleNETUser.class));
		}
		return users;
	}
	
	public void addUser(String email, String name){
		BattleNETUser user = new BattleNETUser();
		user.setEmail(email);
		user.setName(name);
		addUser(user);
	}
	
	public void addUser(BattleNETUser user){
		user.setLastLogIn(0);
		battleCfg.load();
		JSONArray users = battleCfg.getJSONArray("Users");
		for(int i = 0; i<users.length(); i++){
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
	
	public void removeUser(BattleNETUser user){
		removeUser(user.getEmail());
	}
	
	public void removeUser(String email){
		battleCfg.load();
		int id = -1;
		for(int i = 0; i<battleCfg.getJSONArray("Users").length(); i++){
			if(battleCfg.getJSONArray("Users").getJSONObject(i).getString("email").equalsIgnoreCase(email)){
				id = i;
				break;
			}
		}
		if(id!=-1){
			battleCfg.getJSONArray("Users").remove(id);
			battleCfg.save();
		}
		
		List<String> accounts = settings.getSavedAccountNames();
		if(accounts.contains(email))
			accounts.remove(email);
		settings.setSavedAccountNames(accounts);
	}
	
	public void forceChangeUser(BattleNETUser user) {
		if(user.getEmail().isEmpty())
			return;
		
		try {
			List<String> newUserList = new ArrayList<>();
			newUserList.add(user.getEmail());
			for(String users : settings.getSavedAccountNames()){
				if(!users.equalsIgnoreCase(user.getEmail()))
					newUserList.add(users);
			}
			settings.setSavedAccountNames(newUserList);
			
			Runtime.getRuntime().exec("taskkill /F /IM Battle.net.exe");
			Thread.sleep(500);
			ProcessBuilder pb = new ProcessBuilder(getDirectory() + "Battle.net.exe");
			pb.directory(new File(getDirectory()));
			pb.start();
			
			battleCfg.load();
			battleCfg.get().put("lastUsedAccount", user.getEmail());
			battleCfg.save();
			
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
	
	public String getIcon(BattleNETGames game) {
		List<Application> applications = launcher.getApplications(AppTypes.BATTLENET);
		for(int i = 0; i < applications.size(); i++) {
			Application app = applications.get(i);
			if(app.getName().equalsIgnoreCase(game.getConfigName())) {
				if(app.getIconPath().equalsIgnoreCase("default")) {
					saveIcon(game);
				}
				return launcher.getApplication(app.getName()).getIconPath();
			}
		}
		return null;
	}
	
	public void createShortcutOnDesktop(GameLauncher launcher, BattleNETGames game) {
		createShortcut(System.getProperty("user.home") + "/Desktop/", launcher, game);
	}
	
	public void createShortcut(String path, GameLauncher launcher, BattleNETGames game) {
		if(hasAlreadyAShortcut(game)) {
			createShortcutWithOldIcon(path, launcher, game);
			return;
		}
		ShellLink sl;
		if(!launcher.jrePath.isEmpty()) {
			sl = ShellLink.createLink(launcher.jrePath+"javaw.exe");
			sl.setCMDArgs("-jar "+launcher.folderPath.replaceAll("GameLauncher/", "") + new File(launcher.gameLauncherName).getPath()+" --start " + game.getConfigName());
			sl.setWorkingDir(launcher.folderPath.replaceAll("GameLauncher/", ""));
		}else{
			sl = ShellLink.createLink(launcher.folderPath.replaceAll("GameLauncher/", "") + new File(launcher.gameLauncherName).getPath());
			sl.setCMDArgs("--start " + game.getConfigName());
		}
		sl.setIconLocation(getIcon(game));
		try {
			sl.saveTo(path + game.getName() + ".lnk");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean hasAlreadyAShortcut(BattleNETGames game) {
		return new File(System.getProperty("user.home") + "/Desktop/" + game.getName() + ".lnk").exists();
	}
	
	public void replaceShortcut(GameLauncher launcher, BattleNETGames game) {
		createShortcutWithOldIcon(System.getProperty("user.home") + "/Desktop/" + game.getName() + ".lnk", launcher, game);
	}
	
	public void createShortcutWithOldIcon(String path, GameLauncher launcher, BattleNETGames game) {
		try {
			String oldIcon = new ShellLink(new File(path)).resolveTarget();
			
			ShellLink link;
			if(!launcher.jrePath.isEmpty()) {
				link = ShellLink.createLink(launcher.jrePath+"javaw.exe");
				link.setCMDArgs("-jar "+launcher.folderPath.replaceAll("GameLauncher/", "") + new File(launcher.gameLauncherName).getPath()+" --start " + game.getConfigName());
				link.setWorkingDir(launcher.folderPath.replaceAll("GameLauncher/", ""));
			}else{
				link = ShellLink.createLink(launcher.folderPath.replaceAll("GameLauncher/", "") + new File(launcher.gameLauncherName).getPath());
				link.setCMDArgs("--start " + game.getConfigName());
			}
			
			if(oldIcon.contains(".jar"))
				link.setIconLocation(getIcon(game));
			else
				link.setIconLocation(oldIcon);
			link.saveTo(path);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void saveIcon(BattleNETGames game) {
		try {
			String path = null;
			String UID = "";
			for(Application application : launcher.getApplications(AppTypes.BATTLENET)) {
				if(application.getName().equalsIgnoreCase(game.getConfigName())){
					path = "http://217.79.178.92/games/icon/" + application.getUniqueID() + ".png";
					UID = application.getUniqueID();
				}
			}
			String newPath = launcher.folderPath+"Games/BattleNET/"+UID+".png";
			FileUtils.copyURLToFile(new URL(path), new File(newPath));
			BufferedImage bi = ImageIO.read(new File(newPath));
			
			File icon = new File(newPath.replaceAll("\\.png", ".ico"));
			if(icon.exists())
				icon.delete();
			ICOEncoder.write(bi, icon);
			return;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return;
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
	
	public static void main(String[] args) {
		GameLauncher launcher = new GameLauncher();
		new BattleNET(launcher).launch(BattleNETGames.OVERWATCH);
	}
	
}
