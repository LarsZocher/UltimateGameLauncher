package api.GameLauncher;

import api.GameLauncher.Image.IconSize;
import api.GameLauncher.Image.PathType;
import api.GameLauncher.Steam.SteamApp;
import mslinks.ShellLink;
import mslinks.ShellLinkException;

import java.io.File;
import java.io.FileReader;
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
		createShortcut(application, folder, name, launcher.getImageManager().getIconICO(application, IconSize.S_64, PathType.FILE));
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
	
	public void replaceShortcut(Application application, String oldShortcut, boolean useOldIcon){
		replaceShortcut(application, oldShortcut, application.getNiceName(), useOldIcon);
	}
	
	public void replaceShortcut(Application application, String oldShortcut, String name, boolean useOldIcon){
		File oldSC = new File(oldShortcut);
		String image = "";
		if(oldSC.exists()&&useOldIcon) {
			try {
				if(oldSC.getName().toLowerCase().contains("java"))
					image = new ShellLink(oldSC).getIconLocation();
				else
					image = new ShellLink(oldSC).resolveTarget();
			} catch(IOException e) {
				e.printStackTrace();
			} catch(ShellLinkException e) {
				e.printStackTrace();
			}
		}else{
			image = launcher.getImageManager().getIconICO(application, IconSize.S_64, PathType.FILE);
		}
		replaceShortcut(application, oldShortcut, name, image);
	}
	
	public void replaceShortcut(Application application, String oldShortcut, String name, String image){
		File oldSC = new File(oldShortcut);
		String folder = oldSC.getAbsolutePath().replace(oldSC.getName(), "");
		if(oldSC.exists()){
			oldSC.delete();
		}
		createShortcut(application, folder, name, image);
	}
	
	public boolean hasShortcut(Application application){
		return getOldShortcutFile(application)!=null;
	}
	
	public File getOldShortcutFile(Application application) {
		switch(application.getType()) {
			case STEAM: {
				for(File file : new File(getDesktopFolder()).listFiles()) {
					if(file.getPath().contains(".url")) {
						try {
							FileReader reader = new FileReader(file);
							char[] a = new char[500];
							reader.read(a);
							String text = "";
							for(char c : a)
								text += c + "";
							reader.close();
							if(text.contains("rungameid/")) {
								if(text.split("rungameid/")[1].split("IconFile=")[0].replace("\n", "").replace("\r", "").equalsIgnoreCase(application.getContent(SteamApp.class).getAppID()))
									return file;
							}
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
				break;
			}
			case BATTLENET:{
				File sc =  new File(System.getProperty("user.home") + "/Desktop/" + application.getNiceName() + ".lnk");
				if(sc.exists())
					return sc;
				break;
			}
		}
		return null;
	}
	
	public static String getDesktopFolder(){
		return System.getProperty("user.home") + "/Desktop/";
	}
}
