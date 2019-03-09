package api.GameLauncher.Steam;

import api.GameLauncher.Content;
import api.GameLauncher.GameLauncher;
import mslinks.ShellLink;
import net.sf.image4j.codec.ico.ICODecoder;
import net.sf.image4j.codec.ico.ICOEncoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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

public class SteamApp extends Content {
	
	private String appID = "";
	private String appType = "";
	private String name = "";
	private String developer = "";
	private String publisher = "";
	private String supportedSystems = "";
	private String lastRecordUpdate = "";
	private String lastChangeNumber = "";
	private String releaseDate = "";
	private String user = "";
	private String configName = "";
	private String picture = "";
	private String background = "";
	private String clientIcon = "";
	private String icon = "";
	private String pathToIconICO = "";
	private String pathToIconPNG = "";
	private String args = "";
	private String franchise = "";
	private List<String> namesToSay;
	private long creationDate = 0;
	
	public String getAppID() {
		return appID;
	}
	
	public void setAppID(String appID) {
		this.appID = appID;
	}
	
	public String getAppType() {
		return appType;
	}
	
	public void setAppType(String appType) {
		this.appType = appType;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDeveloper() {
		return developer;
	}
	
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	
	public String getPublisher() {
		return publisher;
	}
	
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
	public String getSupportedSystems() {
		return supportedSystems;
	}
	
	public void setSupportedSystems(String supportedSystems) {
		this.supportedSystems = supportedSystems;
	}
	
	public String getLastRecordUpdate() {
		return lastRecordUpdate;
	}
	
	public void setLastRecordUpdate(String lastRecordUpdate) {
		this.lastRecordUpdate = lastRecordUpdate;
	}
	
	public String getLastChangeNumber() {
		return lastChangeNumber;
	}
	
	public void setLastChangeNumber(String lastChangeNumber) {
		this.lastChangeNumber = lastChangeNumber;
	}
	
	public String getReleaseDate() {
		return releaseDate;
	}
	
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public List<String> getNamesToSay() {
		return namesToSay;
	}
	
	public void setNamesToSay(List<String> namesToSay) {
		this.namesToSay = namesToSay;
	}
	
	public String getClientIcon() {
		return clientIcon;
	}
	
	public void setClientIcon(String pathToIcon) {
		this.clientIcon = pathToIcon;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public String getIconICOPath() {
		return pathToIconICO;
	}
	
	public String getIconICOPath(GameLauncher launcher, boolean update) {
		if(pathToIconICO.isEmpty()){
			savePicture(launcher, update);
		}
		return pathToIconICO;
	}
	
	public void setIconICOPath(String pathToIconICO) {
		this.pathToIconICO = pathToIconICO;
	}
	
	public long getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}
	
	public String getPicture() {
		return picture;
	}
	
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public String getBackground() {
		return background;
	}
	
	public void setBackground(String pathToBackground) {
		this.background = pathToBackground;
	}
	
	public String getArgs() {
		return args;
	}
	
	public void setArgs(String args) {
		this.args = args;
	}
	
	public String getFranchise() {
		return franchise;
	}
	
	public void setFranchise(String franchise) {
		this.franchise = franchise;
	}
	
	public String getIconPNGPath() {
			return pathToIconPNG;
	}
	
	public String getIconPNGPath(GameLauncher launcher, boolean update) {
		if(pathToIconPNG.isEmpty()){
			savePicturePNG(launcher, update);
		}
		return pathToIconPNG;
	}
	
	public void setIconPNGPath(String pathToIconPNG) {
		this.pathToIconPNG = pathToIconPNG;
	}
	
	public String getConfigName() {
		String name = configName;
		
		name = name.replaceAll(" ", "_");
		name = name.toLowerCase().replaceAll("[^a-zA-Z0-9_]", "");
		
		
		configName = name;
		
		return name;
	}
	
	private String getNiceName() {
		String name = getName();
		
		name = name.replaceAll("[^a-zA-Z0-9_\\s]", "");
		
		return name;
	}
	
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	
	public void createShortcutOnDesktop(GameLauncher launcher) {
		createShortcut(System.getProperty("user.home") + "/Desktop/", launcher);
	}
	
	public void createShortcut(String path, GameLauncher launcher) {
		if(hasAlreadyAShortcut()) {
			createShortcutWithOldIcon(path, getOldShortcutFile().getPath(), launcher);
			return;
		}
		savePicture(launcher, true);
		ShellLink sl;
		if(!launcher.jrePath.isEmpty()) {
			sl = ShellLink.createLink(launcher.jrePath+"javaw.exe");
			sl.setCMDArgs("-jar "+launcher.folderPath.replaceAll("GameLauncher/", "") + new File(launcher.gameLauncherName).getPath()+" --start " + getConfigName());
			sl.setWorkingDir(launcher.folderPath.replaceAll("GameLauncher/", ""));
		}else{
			sl = ShellLink.createLink(launcher.folderPath.replaceAll("GameLauncher/", "") + new File(launcher.gameLauncherName).getPath());
			sl.setCMDArgs("--start " + getConfigName());
		}
		sl.setIconLocation(getIconICOPath());
		
		try {
			sl.saveTo(path + getNiceName() + ".lnk");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void replaceShortcut(String oldLinkPath, GameLauncher launcher) {
		createShortcutWithOldIcon(System.getProperty("user.home") + "/Desktop/", oldLinkPath, launcher);
		File old = new File(oldLinkPath);
		if(old.exists())
			old.delete();
	}
	
	public void createShortcutWithOldIcon(String path, String oldLinkPath, GameLauncher launcher) {
		try {
			ShellLink link;
			if(!launcher.jrePath.isEmpty()) {
				link = ShellLink.createLink(launcher.jrePath+"javaw.exe");
				link.setCMDArgs("-jar "+launcher.folderPath.replaceAll("GameLauncher/", "") + new File(launcher.gameLauncherName).getPath()+" --start " + getConfigName());
				link.setWorkingDir(launcher.folderPath.replaceAll("GameLauncher/", ""));
			}else{
				link = ShellLink.createLink(launcher.folderPath.replaceAll("GameLauncher/", "") + new File(launcher.gameLauncherName).getPath());
				link.setCMDArgs("--start " + getConfigName());
			}
			
			FileReader reader = new FileReader(new File(oldLinkPath));
			char[] a = new char[500];
			reader.read(a);
			String text = "";
			for(char c : a)
				text += c + "";
			reader.close();
			
			text = text.split("IconFile=")[1].split("\\.ico")[0] + ".ico";
			link.setIconLocation(text);
			link.getHeader().setIconIndex(0);
			
			setIconICOPath(text);
			
			link.saveTo(path + getNiceName() + ".lnk");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean hasAlreadyAShortcut() {
		for(File file : new File(System.getProperty("user.home") + "/Desktop").listFiles()) {
			if(file.getPath().contains(".url")) {
				try {
					FileReader reader = new FileReader(file);
					char[] a = new char[500];
					reader.read(a);
					String text = "";
					for(char c : a)
						text += c + "";
					reader.close();
					if(text.contains("rungameid/")) {
						if(text.split("rungameid/")[1].split("IconFile=")[0].replace("\n", "").replace("\r", "").equalsIgnoreCase(getAppID()))
							return true;
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	public File getOldShortcutFile() {
		for(File file : new File(System.getProperty("user.home") + "/Desktop").listFiles()) {
			if(file.getPath().contains(".url")) {
				try {
					FileReader reader = new FileReader(file);
					char[] a = new char[500];
					reader.read(a);
					String text = "";
					for(char c : a)
						text += c + "";
					reader.close();
					if(text.contains("rungameid/")) {
						if(text.split("rungameid/")[1].split("IconFile=")[0].replace("\n", "").replace("\r", "").equalsIgnoreCase(getAppID()))
							return file;
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public void savePicture(GameLauncher launcher, boolean update) {
		try(InputStream in = new URL(getClientIcon()).openStream()) {
			File folder = new File(launcher.folderPath + "Games/Steam");
			if(!folder.exists())
				folder.mkdir();
			String path = launcher.folderPath + "Games/Steam/" + getConfigName() + ".ico";
			File file = new File(path);
			if(file.exists())
				file.delete();
			Files.copy(in, Paths.get(path));
			
			if(getIconICOPath().isEmpty())
				setIconICOPath(file.getAbsolutePath());
			if(update)
				launcher.getSteam().addApp(this);
			return;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return;
	}
	
	public void savePicturePNG(GameLauncher launcher, boolean update) {
		savePicture(launcher, update);
		try {
			List<BufferedImage> images = ICODecoder.read(new File(getIconICOPath()));
			
			String path = launcher.folderPath + "Games/Steam/" + getConfigName() + ".png";
			ImageIO.write(images.get(images.size()-1), "png", new File(path));
			
			if(getIconPNGPath().isEmpty())
				setIconPNGPath(new File(path).getAbsolutePath());
			
			if(update)
				launcher.getSteam().addApp(this);
			return;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return;
	}
	
	@Override
	public String toString() {
		return "SteamApp{" +
				"appID='" + appID + '\'' +
				", appType='" + appType + '\'' +
				", name='" + name + '\'' +
				", developer='" + developer + '\'' +
				", publisher='" + publisher + '\'' +
				", supportedSystems='" + supportedSystems + '\'' +
				", lastRecordUpdate='" + lastRecordUpdate + '\'' +
				", lastChangeNumber='" + lastChangeNumber + '\'' +
				", releaseDate='" + releaseDate + '\'' +
				", user='" + user + '\'' +
				", configName='" + configName + '\'' +
				", picture='" + picture + '\'' +
				", background='" + background + '\'' +
				", clientIcon='" + clientIcon + '\'' +
				", icon='" + icon + '\'' +
				", pathToIconICO='" + pathToIconICO + '\'' +
				", args='" + args + '\'' +
				", franchise='" + franchise + '\'' +
				", namesToSay=" + namesToSay +
				", creationDate=" + creationDate +
				'}';
	}
}
