package gui.localization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class LanguageManager {
	
	private Language language;
	
	public LanguageManager(String lang){
		File folder = new File("localization");
		if(!folder.exists())
			folder.mkdir();
		
		for(File file : folder.listFiles()) {
			if(file.getName().toLowerCase().contains("lang-") && file.getName().toLowerCase().contains(".json")) {
				updateLanguage(file);
			}
		}
		language = getLanguage(lang);
		if(language.language.equalsIgnoreCase("none")){
			File file = new File("localization/lang-english.json");
			try {
				if(!file.exists()) {
					file.createNewFile();
					updateLanguage(file);
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void updateLanguage(File file){
		try {
			String json = FileUtils.readFileToString(file, "UTF-8");
			Language language = new Language();
			if(!json.isEmpty())
				language = new Gson().fromJson(json, Language.class);
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
			writer.println(gson.toJson(language));
			writer.flush();
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public Language getLanguage(){
		return language;
	}
	
	public Language getLanguage(String lang){
		File folder = new File("localization");
		for(File file : folder.listFiles()) {
			if(file.getName().toLowerCase().contains("lang-")&&file.getName().toLowerCase().contains(".json")){
				try {
					Language language = new Gson().fromJson(FileUtils.readFileToString(file, "UTF-8"), Language.class);
					if(language.language.equalsIgnoreCase(lang)){
						return language;
					}
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		return new Language();
	}
}
