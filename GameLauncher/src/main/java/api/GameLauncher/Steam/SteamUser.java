package api.GameLauncher.Steam;

import Utils.Encoding.Base64Encoding;
import api.GameLauncher.GameLauncher;
import api.GameLauncher.Utils.JsonPartManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import mslinks.ShellLink;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class SteamUser {
	
	private GameLauncher launcher;
	private String username;
	private JsonPartManager steamManager;
	
	public SteamUser(GameLauncher launcher, String username) {
		this.launcher = launcher;
		this.username = username;
		steamManager = launcher.getSteam().getSteamManager();
		steamManager.load();
		
		if(!new File(launcher.folderPath+"Users/SteamGuard_"+username.toLowerCase()+".json").exists()){
			addSteamGuard(new SteamGuardInformation());
		}
	}
	
	public void createUser(String password, String steam64id) {
		try {
			Base64Encoding codec = new Base64Encoding();
			String encypted = codec.encrypt(password);
			
			SteamConfigUser user = new SteamConfigUser();
			user.setUsername(username);
			user.setPassword(encypted);
			user.setSteam64id(steam64id);
			steamManager.load();
			JSONArray users = steamManager.getJSONArray("Users").put(new JSONObject(new Gson().toJson(user)));
			steamManager.get().put("Users", users);
			steamManager.save();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addSteamGuard(SteamGuardInformation info){
		File userData = new File(launcher.folderPath+"Users/SteamGuard_"+username.toLowerCase()+".json");
		if(userData.exists()){
			userData.delete();
		}
		try {
			userData.createNewFile();
		} catch(IOException e) {
			e.printStackTrace();
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(gson.toJson(info, SteamGuardInformation.class));
		String prettyJsonString = gson.toJson(je);
		try {
			PrintWriter writer = new PrintWriter(userData);
			writer.println(prettyJsonString);
			writer.flush();
			writer.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void disableCredentialsFile(){
		File userData = new File(launcher.folderPath+"Users/SteamGuard_"+username.toLowerCase()+".json");
		if(userData.exists()){
			userData.renameTo(new File(launcher.folderPath+"Users/SteamGuard_D_"+username.toLowerCase()+"_"+System.currentTimeMillis()+".json"));
		}
	}
	
	public SteamGuardInformation getSteamGuard(){
		File userData = new File(launcher.folderPath+"Users/SteamGuard_"+username.toLowerCase()+".json");
		if(userData.exists()){
			try {
				return new Gson().fromJson(FileUtils.readFileToString(userData, "UTF-8"), SteamGuardInformation.class);
			} catch(IOException e) {
				e.printStackTrace();
				return new SteamGuardInformation();
			}
		}else
			return new SteamGuardInformation();
	}
	
	public boolean hasSteamGuard() {
		return !getSteamGuard().getShared_secret().isEmpty();
	}
	
	public String getPassword() {
		if(exists()) {
			try {
				String encypted = getConfigUser().getPassword();
				Base64Encoding codec = new Base64Encoding();
				return codec.decrypt(encypted);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	public SteamConfigUser getConfigUser(){
		steamManager.load();
		for(int i = 0; i<steamManager.getJSONArray("Users").length(); i++){
			if(steamManager.getJSONArray("Users").getJSONObject(i).getString("username").equalsIgnoreCase(username)){
				return new Gson().fromJson(steamManager.getJSONArray("Users").getJSONObject(i).toString(), SteamConfigUser.class);
			}
		}
		return null;
	}
	
	public void addGames(boolean free){
		List<SteamApp> apps = launcher.getSteam().getAppsFromUser(getID(), free);
		games : for(SteamApp app : apps) {
			for(String cfgname : launcher.getSteam().getApps()){
				if(app.getConfigName().equalsIgnoreCase(cfgname))
					continue games;
			}
			System.out.println("Adding: "+app.getName()+"...");
			app.setUser(username);
			app.setConfigName(app.getName());
			app.setConfigName(app.getConfigName());
			launcher.getSteam().addApp(app);
		}
	}
	
	public String getUsername() {
		return username;
	}
	
	public boolean exists() {
		return getConfigUser()!=null;
	}
	
	public void delete() {
		if(exists()) {
			steamManager.load();
			int id = -1;
			for(int i = 0; i<steamManager.getJSONArray("Users").length(); i++){
				if(steamManager.getJSONArray("Users").getJSONObject(i).getString("username").equalsIgnoreCase(username)){
					id = i;
					break;
				}
			}
			if(id!=-1){
				steamManager.getJSONArray("Users").remove(id);
				steamManager.save();
			}
		}
	}
	
	public String getID() {
		String id = getConfigUser().getSteam64id();
		if(!id.isEmpty())
			return getConfigUser().getSteam64id();
		return "76561197960287930";
	}
	
	public String getImageIcon() {
		try {
			String link = "https://steamcommunity.com/profiles/" + getID() + "?xml=1";
			
			BufferedReader in = new BufferedReader(new InputStreamReader(new URL(link).openStream()));
			
			String inputLine;
			while((inputLine = in.readLine()) != null) {
				if(inputLine.contains("<avatarIcon>")){
					String image = inputLine.split("\\[")[2].split("]")[0];
					return image;
				}
			}
			in.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	public String getImageMedium() {
		try {
			String link = "https://steamcommunity.com/profiles/" + getID() + "?xml=1";
			
			BufferedReader in = new BufferedReader(new InputStreamReader(new URL(link).openStream()));
			
			String inputLine;
			while((inputLine = in.readLine()) != null) {
				if(inputLine.contains("<avatarMedium>")){
					String image = inputLine.split("\\[")[2].split("]")[0];
					return image;
				}
			}
			in.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	public String getImageFull() {
		try {
			String link = "https://steamcommunity.com/profiles/" + getID() + "?xml=1";
			
			BufferedReader in = new BufferedReader(new InputStreamReader(new URL(link).openStream()));
			
			String inputLine;
			while((inputLine = in.readLine()) != null) {
				if(inputLine.contains("<avatarFull>")){
					String image = inputLine.split("\\[")[2].split("]")[0];
					return image;
				}
			}
			in.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public File savePictureIcon(){
		try(InputStream in = new URL(getImageIcon()).openStream()){
			String path = launcher.folderPath+"Users/"+getUsername().toLowerCase()+"_icon.jpg";
			File file = new File(path);
			if(file.exists())
				file.delete();
			Files.copy(in, Paths.get(path));
			return new File(path);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public File savePictureMedium(){
		try(InputStream in = new URL(getImageMedium()).openStream()){
			String path = launcher.folderPath+"Users/"+getUsername().toLowerCase()+"_medium.jpg";
			File file = new File(path);
			if(file.exists())
				file.delete();
			Files.copy(in, Paths.get(path));
			return new File(path);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public File savePictureFull(){
		try(InputStream in = new URL(getImageFull()).openStream()){
			String path = launcher.folderPath+"Users/"+getUsername().toLowerCase()+"_full.jpg";
			File file = new File(path);
			if(file.exists())
				file.delete();
			Files.copy(in, Paths.get(path));
			return new File(path);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void createShortcutOnDesktop(){
		createShortcut(System.getProperty("user.home") + "/Desktop/");
	}
	
	public void createShortcut(String path){
		ShellLink sl;
		if(!launcher.jrePath.isEmpty()) {
			sl = ShellLink.createLink(launcher.jrePath+"javaw.exe");
			sl.setCMDArgs("-jar "+launcher.folderPath.replaceAll("GameLauncher/", "") + new File(launcher.gameLauncherName).getPath()+" --user "+getUsername());
			sl.setWorkingDir(launcher.folderPath.replaceAll("GameLauncher/", ""));
		}else{
			sl = ShellLink.createLink(launcher.folderPath.replaceAll("GameLauncher/", "") + new File(launcher.gameLauncherName).getPath());
			sl.setCMDArgs("--user "+getUsername());
		}
		sl.setIconLocation(launcher.steam.getSteamExecutable());
		
		try {
			sl.saveTo(path+getUsername()+".lnk");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getCurrentUsername(){
		try {
			String link = "https://steamcommunity.com/profiles/" + getID() + "?xml=1";
			
			BufferedReader in = new BufferedReader(new InputStreamReader(new URL(link).openStream()));
			
			String inputLine;
			while((inputLine = in.readLine()) != null) {
				if(inputLine.contains("<steamID>")){
					String image = inputLine.split("\\[")[2].split("]")[0];
					return image;
				}
			}
			in.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return username;
	}
	
	public boolean isMainAccount(){
		steamManager.load();
		try {
			return steamManager.get().getString("mainUser").equalsIgnoreCase(getUsername());
		}catch(Exception e){
			return false;
		}
	}
	
	public void setAsMainAccount(){
		steamManager.load();
		steamManager.get().put("mainUser", getUsername().toLowerCase());
		steamManager.save();
	}
}
