package GUI.screens.misc;

import GUI.Utils.ConfigFile;
import GUI.css.CSSColorHelper;
import GUI.css.CSSUtils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXToggleNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public abstract class PluginEntry {
	
	private JFXListView<HBox> list;
	private Label name;
	private JFXToggleNode enabled;
	private HBox box;
	private final int id;
	private boolean isDisabled = true;
	private String pluginName;
	
	public PluginEntry(JFXListView list, File plugin){
		this.list = list;
		
		try{
			pluginName = getPluginName(plugin)==null?"null":getPluginName(plugin);
		}catch(Exception e){
			pluginName = "null";
		}
		if(pluginName.equalsIgnoreCase("null"))
			name = new Label("Name missing! - "+plugin.getName());
		else
			name = new Label(pluginName);
		name.setFont(new Font(15));
		
		Label enabledLabel = new Label("Deaktiviert");
		enabledLabel.setMinWidth(70);
		enabledLabel.setPrefWidth(70);
		enabledLabel.setMaxWidth(70);
		
		JFXButton b = new JFXButton("Aktualisieren");
		b.setStyle("-fx-background-color: #2d2d2d; -fx-text-fill: #ffffff");
		b.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				onRefresh();
			}
		});
		b.setFocusTraversable(false);
		
		enabled = new JFXToggleNode();
		enabled.setGraphic(enabledLabel);
		enabled.setStyle("-fx-background-color: #2d2d2d; -fx-text-fill: #ffffff");
		enabled.setFocusTraversable(false);
		
		id = list.getItems().size();
		enabled.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(enabled.isSelected()) {
					enable();
					onEnable();
				}else {
					disable();
					onDisable();
				}
				list.getSelectionModel().select(id);
			}
		});
		
		Pane p = new Pane();
		box = new HBox(name,p,b,enabled);
		box.setSpacing(10.0);
		HBox.setHgrow(p, Priority.ALWAYS);
		box.setAlignment(Pos.CENTER);
	}
	
	public HBox getBox() {
		return box;
	}
	
	public void enable(){
		isDisabled = false;
		enabled.setSelected(true);
		enabled.setStyle("-fx-background-color: -primary; -fx-text-fill: #ffffff");
		((Label)enabled.getGraphic()).setText("Aktiviert");
		enablePlugin(this);
	}
	
	public void disable(){
		isDisabled = true;
		enabled.setSelected(false);
		enabled.setStyle("-fx-background-color: #2d2d2d; -fx-text-fill: #ffffff");
		((Label)enabled.getGraphic()).setText("Deaktiviert");
		disablePlugin(this);
	}
	
	public void toggle(){
		if(isDisabled)
			enable();
		else
			disable();
	}
	
	public boolean isDisabled() {
		return isDisabled;
	}
	
	public String getName(){
		return pluginName;
	}
	
	public abstract void onRefresh();
	
	public abstract void onEnable();
	
	public abstract void onDisable();
	
	public static boolean isPlugin(File jarFile){
		if(!jarFile.getName().contains(".jar")){
			return false;
		}
		try {
			JarFile file = new JarFile(jarFile);
			Manifest manifest = file.getManifest();
			Attributes attrib = manifest.getMainAttributes();
			boolean isPlugin = true;
			Class cl;
			if(attrib.getValue("Plugin-Class")==null||attrib.getValue("Plugin-Class").isEmpty()||attrib.getValue("Plugin-Class").equalsIgnoreCase("")) {
				String main = attrib.getValue(Attributes.Name.MAIN_CLASS);
				if(main.split("\\.")[0].equals("main") && main.split("\\.")[1].equals("main"))
					main = main.split("\\.")[1];
				//DotBot.debugger.print("[Bot.PluginLoader] main class: "+main);
				cl = new URLClassLoader(new URL[]{jarFile.toURL()}).loadClass(main);
				
				Class[] interfaces = cl.getInterfaces();
				for(int y = 0; y < interfaces.length && !isPlugin; y++)
					if(interfaces[y].getName().equals("Bot.PluginLoader.DotBotPlugin"))
						isPlugin = true;
			}else{
				String main = attrib.getValue("Plugin-Class");
				if(main.split("\\.")[0].equals("main") && main.split("\\.")[1].equals("main"))
					main = main.split("\\.")[1];
				//DotBot.debugger.print("[Bot.PluginLoader] main class: "+main);
				cl = new URLClassLoader(new URL[]{jarFile.toURL()}).loadClass(main);
				
				Class[] interfaces = cl.getInterfaces();
				for(int y = 0; y < interfaces.length && !isPlugin; y++)
					if(interfaces[y].getName().equals("Bot.PluginLoader.DotBotPlugin"))
						isPlugin = true;
			}
			return isPlugin;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static String getPluginName(File jarFile){
		try {
			JarFile file = new JarFile(jarFile);
			Manifest manifest = file.getManifest();
			Attributes attrib = manifest.getMainAttributes();
			return attrib.getValue("Plugin-Name");
		} catch(Exception e) {
			return "null";
		}
	}
	
	public static boolean isDisabled(PluginEntry plugin){
		ConfigFile pluginInfo = new ConfigFile("plugins/plugins.yml");
		pluginInfo.createFile();
		pluginInfo.load();
		List<String> disabledPlugins = pluginInfo.getStringList("DisabledPlugins");
		return disabledPlugins.contains(plugin.getName().toLowerCase());
	}
	
	public static void disablePlugin(PluginEntry plugin){
		ConfigFile pluginInfo = new ConfigFile("plugins/plugins.yml");
		pluginInfo.createFile();
		pluginInfo.load();
		List<String> disabledPlugins = pluginInfo.getStringList("DisabledPlugins");
		disabledPlugins.add(plugin.getName().toLowerCase());
		pluginInfo.set("DisabledPlugins", disabledPlugins);
		pluginInfo.save();
	}
	public static void enablePlugin(PluginEntry plugin){
		ConfigFile pluginInfo = new ConfigFile("plugins/plugins.yml");
		pluginInfo.createFile();
		pluginInfo.load();
		List<String> disabledPlugins = pluginInfo.getStringList("DisabledPlugins");
		if(isDisabled(plugin)){
			disabledPlugins.remove(plugin.getName().toLowerCase());
		}
		pluginInfo.set("DisabledPlugins", disabledPlugins);
		pluginInfo.save();
	}
}
