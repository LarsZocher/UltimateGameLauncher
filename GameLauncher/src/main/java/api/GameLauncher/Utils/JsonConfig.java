package api.GameLauncher.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class JsonConfig {
	
	private String filePath;
	private JSONObject config = new JSONObject();
	
	public JsonConfig(String path) {
		filePath = path;
	}
	
	public boolean exists() {
		return new File(filePath).exists();
	}
	
	public void createFile() {
		try {
			getFile().createNewFile();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public File getFile() {
		return new File(filePath);
	}
	
	public void load() {
		try {
			if(!exists())
				createFile();
			config = new JSONObject(FileUtils.readFileToString(getFile(), "UTF-8"));
		} catch(JSONException e) {
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		try {
			if(!exists())
				createFile();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(config.toString());
			String prettyJsonString = gson.toJson(je);
			
			PrintWriter writer = new PrintWriter(getFile());
			writer.println(prettyJsonString);
			writer.flush();
			writer.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public JSONObject getConfig() {
		return config;
	}
	
	public void setDefault(String key, Object object) {
		if(!getConfig().has(key)) {
			getConfig().put(key, object);
		}
	}
	
	public static void setDefault(JSONObject json, String key, Object object) {
		if(!json.has(key)) {
			json.put(key, object);
		}
	}
	
	public static JSONObject getJSONObject(JSONObject object, String key){
		if(object.has(key)){
			return object.getJSONObject(key);
		}
		return new JSONObject();
	}
	
	public static JSONArray getJSONArray(JSONObject object, String key){
		if(object.has(key)){
			return object.getJSONArray(key);
		}
		return new JSONArray();
	}
}
