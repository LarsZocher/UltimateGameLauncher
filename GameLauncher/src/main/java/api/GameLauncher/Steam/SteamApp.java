package api.GameLauncher.Steam;

import api.GameLauncher.Content;
import api.GameLauncher.GameLauncher;
import api.GameLauncher.Image.IconSize;
import api.GameLauncher.Image.PathType;
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
	private String type = "";
	private String name = "";
	private String developer = "";
	private String publisher = "";
	private String supportedSystems = "";
	private String user = "";
	private String configName = "";
	private String clientIcon = "";
	private String icon = "";
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
	
	public long getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
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
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getConfigName() {
		return configName;
	}
	
	private String getNiceName() {
		String name = getName();
		
		name = name.replaceAll("[^a-zA-Z0-9_\\s-]", "");
		
		return name;
	}
	
	public void setConfigName(String configName) {
		configName = configName.replaceAll(" ", "_");
		configName = configName.toLowerCase().replaceAll("[^a-zA-Z0-9_]", "");
		
		this.configName = configName;
	}
	
	@Override
	public String toString() {
		return "SteamApp{" +
				"appID='" + appID + '\'' +
				", name='" + name + '\'' +
				", developer='" + developer + '\'' +
				", publisher='" + publisher + '\'' +
				", supportedSystems='" + supportedSystems + '\'' +
				", user='" + user + '\'' +
				", configName='" + configName + '\'' +
				", clientIcon='" + clientIcon + '\'' +
				", icon='" + icon + '\'' +
				", args='" + args + '\'' +
				", franchise='" + franchise + '\'' +
				", namesToSay=" + namesToSay +
				", creationDate=" + creationDate +
				'}';
	}
}
