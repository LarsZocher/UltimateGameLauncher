package api.GameLauncher;

import com.google.gson.Gson;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class Application {
	
	private AppTypes type;
	private String name;
	private String displayName;
	private long created = 0;
	private String iconPath = "";
	private boolean defaultIcon = true;
	private String headerPath = "";
	private boolean defaultHeader = true;
	private transient JSONObject content;
	private String uniqueID = "default";
	
	public AppTypes getType() {
		return type;
	}
	
	public void setType(AppTypes type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getCreated() {
		return created;
	}
	
	public void setCreated(long created) {
		this.created = created;
	}
	
	public <T extends Content> T getContent(Class<T> type){
		return new Gson().fromJson(content.getJSONObject("content").toString(), type);
	}
	
	public JSONObject getRawContent(){
		if(this.content != null)
			return this.content;
		else
			return new JSONObject();
	}
	
	public void setContent(JSONObject content) {
		this.content = content;
	}
	
	public String getUniqueID() {
		return uniqueID;
	}
	
	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getNiceName() {
		return getDisplayName().replaceAll("[^a-zA-Z0-9_\\s-]", "");
	}
	
	public String getIconPath() {
		return iconPath;
	}
	
	public String getIconPathAsURL() {
		File file = new File(iconPath);
		if(file.exists()){
			try {
				return file.toURI().toURL().toString();
			} catch(MalformedURLException e) {
				return iconPath;
			}
		}
		return iconPath;
	}
	
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	
	public boolean isDefaultIcon() {
		return defaultIcon;
	}
	
	public void setDefaultIcon(boolean defaultIcon) {
		this.defaultIcon = defaultIcon;
	}
	
	public String getHeaderPath() {
		return headerPath;
	}
	
	public void setHeaderPath(String headerPath) {
		this.headerPath = headerPath;
	}
	
	public boolean isDefaultHeader() {
		return defaultHeader;
	}
	
	public void setDefaultHeader(boolean defaultHeader) {
		this.defaultHeader = defaultHeader;
	}
}
