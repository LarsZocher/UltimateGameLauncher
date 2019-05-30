package gui.screens;

import gui.Menu;
import gui.screens.misc.initMenuController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class infosController extends initMenuController {
	
	@FXML
	private Label version1;
	@FXML
	private Label version2;
	@FXML
	private Label version3;
	
	@Override
	public void init(Menu menu) {
		super.init(menu);
		try {
			launcher:{
				File launcher = new File("DotBotLauncher.jar");
				if(!launcher.exists()) {
					version1.setText("INTERNAL BUILD");
					break launcher;
				}
				BasicFileAttributes attrLauncher = Files.readAttributes(launcher.toPath(), BasicFileAttributes.class);
				String version = attrLauncher.lastModifiedTime().toString();
				version = version.split("T")[0];
				version = version.substring(2, version.length()-2).replaceAll("-", "");
				String build = attrLauncher.lastModifiedTime().toString();
				build = build.split("-")[2].replaceAll(":", "");
				build = build.split("\\.")[0].replaceAll("T", ".");
				version1.setText("Version: "+version+" Build: "+build);
			}
			dotbot:
			{
				File dotBot = new File("DotBot.jar");
				if(!dotBot.exists()) {
					version2.setText("INTERNAL BUILD");
					break dotbot;
				}
				BasicFileAttributes attrDotBot = Files.readAttributes(dotBot.toPath(), BasicFileAttributes.class);
				String version = attrDotBot.lastModifiedTime().toString();
				version = version.split("T")[0];
				version = version.substring(2, version.length()-2).replaceAll("-", "");
				String build = attrDotBot.lastModifiedTime().toString();
				build = build.split("-")[2].replaceAll(":", "");
				build = build.split("\\.")[0].replaceAll("T", ".");
				version2.setText("Version: "+version+" Build: "+build);
			}
			gLauncher:{
				File gLauncher = new File("plugins/DotBotGameLauncher.jar");
				if(!gLauncher.exists()) {
					version3.setText("INTERNAL BUILD");
					break gLauncher;
				}
				BasicFileAttributes attrGLauncher = Files.readAttributes(gLauncher.toPath(), BasicFileAttributes.class);
				String version = attrGLauncher.lastModifiedTime().toString();
				version = version.split("T")[0];
				version = version.substring(2, version.length()-2).replaceAll("-", "");
				String build = attrGLauncher.lastModifiedTime().toString();
				build = build.split("-")[2].replaceAll(":", "");
				build = build.split("\\.")[0].replaceAll("T", ".");
				version3.setText("Version: "+version+" Build: "+build);
			}
//			File pluginsFolder = new File("plugins");
//			for(File plugin : pluginsFolder.listFiles()){
//				if(PluginEntry.isPlugin(plugin)){
//					String name = PluginEntry.getPluginName(plugin);
//					if(name.equalsIgnoreCase("gamelauncher")){
//						found1.setText("ERFOLGREICH GELADEN");
//						found1.setStyle("-fx-text-fill: -primary;");
//					}
//					if(name.equalsIgnoreCase("radio")){
//						found2.setText("ERFOLGREICH GELADEN");
//						found2.setStyle("-fx-text-fill: -primary;");
//					}
//				}
//			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onRefresh() {
	
	}
}
