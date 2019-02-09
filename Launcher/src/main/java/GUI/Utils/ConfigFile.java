package GUI.Utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class ConfigFile extends YamlConfiguration {
	
	private File file;
	
	public ConfigFile(String path) {
		this.file = new File(path);
	}
	
	public void createFile() {
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void deleteFile() {
		if(this.file != null) {
			if(this.file.exists()) {
				this.file.delete();
			}
		}
	}
	
	public boolean exists() {
		return this.file.exists();
	}
	
	public File getFile() {
		return this.file;
	}
	
	public String getPath() {
		return this.file.getPath();
	}
	
	public void save() {
		if(exists()) {
			try {
				this.save(this.file);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ConfigFile load(){
		if(exists()){
			try {
				this.load(this.file);
			} catch(IOException e) {
				e.printStackTrace();
			} catch(InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
		return this;
	}
	
	public void setDefault(String path, Object value){
		if(!this.contains(path)) {
			this.set(path, value);
		}
	}
}
