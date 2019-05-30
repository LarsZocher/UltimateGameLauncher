package api.launcher.steam;

import api.launcher.Content;
import org.json.JSONObject;

import java.util.ArrayList;
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
	private String user = "";
	private String configName = "";
	private String clientIcon = "";
	private String icon = "";
	private String args = "";
	private String franchise = "";
	private long releaseDate = 0;
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
	
	public void setWebData(JSONObject json){
		name = json.getString("displayName");
		type = json.getString("appType");
		appID = json.getString("appId");
		if(!json.getString("releaseDate").isEmpty())
			releaseDate = Long.valueOf(json.getString("releaseDate"));
		developer = json.getString("developer");
		publisher = json.getString("publisher");
		franchise = json.getString("franchise");
		clientIcon = json.getString("iconName");
		icon = json.getString("iconName");
		creationDate = System.currentTimeMillis();
		setConfigName(name);
		
		ArrayList<String> nts = new ArrayList<>();
		nts.add(getNiceName());
		namesToSay = nts;
	}
	
	public long getReleaseDate() {
		return releaseDate;
	}
	
	public void setReleaseDate(long releaseDate) {
		this.releaseDate = releaseDate;
	}
}
