package api.GameLauncher;

import com.google.gson.Gson;
import org.json.JSONObject;

import java.lang.reflect.Type;
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
	private long created = 0;
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
	
	public void setContent(JSONObject content) {
		this.content = content;
	}
	
	public String getUniqueID() {
		return uniqueID;
	}
	
	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}
}
