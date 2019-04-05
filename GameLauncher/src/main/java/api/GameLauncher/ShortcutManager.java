package api.GameLauncher;

import api.GameLauncher.Image.IconSize;
import api.GameLauncher.Image.PathType;
import mslinks.ShellLink;

import java.io.File;
import java.io.IOException;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class ShortcutManager {
	
	private GameLauncher launcher;
	
	public ShortcutManager(GameLauncher launcher){
		this.launcher = launcher;
	}
	
	public void createShortcut(Application application, String folder){
		createShortcut(application, folder, application.getNiceName());
	}
	
	public void createShortcut(Application application, String folder, String name){
		createShortcut(application, folder, name, launcher.getImageManager().getIconICO(application, IconSize.S_DEFAULT, PathType.FILE));
	}
	
	public void createShortcut(Application application, String folder, String name, String image){
		ShellLink sl;
		if(!launcher.jrePath.isEmpty()) {
			sl = ShellLink.createLink(launcher.jrePath+"javaw.exe");
			sl.setCMDArgs("-jar "+launcher.folderPath.replaceAll("GameLauncher/", "") + new File(launcher.gameLauncherName).getPath()+" --start " + application.getName());
			sl.setWorkingDir(launcher.folderPath.replaceAll("GameLauncher/", ""));
		}else{
			sl = ShellLink.createLink(launcher.folderPath.replaceAll("GameLauncher/", "") + new File(launcher.gameLauncherName).getPath());
			sl.setCMDArgs("--start " + application.getName());
		}
		sl.setIconLocation(image);
		
		try {
			sl.saveTo(folder + name + ".lnk");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getDesktopFolder(){
		return System.getProperty("user.home") + "/Desktop/";
	}
}
