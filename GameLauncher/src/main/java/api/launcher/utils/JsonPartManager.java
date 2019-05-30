package api.launcher.utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public abstract class JsonPartManager {
	
	private JSONObject json;
	private String key;
	
	public JsonPartManager(String key) {
		this.key = key;
	}
	
	public abstract JSONObject onLoad(String key);
	
	public abstract void onSave(JSONObject json, String key);
	
	public void load(){
		json = onLoad(key);
	}
	
	public JSONObject get(){
		return json;
	}
	
	public JSONObject getJSONObject(String key){
		if(json.has(key)){
			return json.getJSONObject(key);
		}
		return new JSONObject();
	}
	
	public JSONArray getJSONArray(String key){
		if(json.has(key)){
			return json.getJSONArray(key);
		}
		return new JSONArray();
	}
	
	public void save(){
		onSave(json, key);
	}
}
