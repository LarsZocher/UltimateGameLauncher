package api.GameLauncher.Origin;

import api.GameLauncher.AppTypes;
import api.GameLauncher.Application;
import api.GameLauncher.GameLauncher;
import api.GameLauncher.Utils.JsonConfig;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class Origin {
	
	private GameLauncher launcher;
	
	public Origin(GameLauncher launcher) {
		this.launcher = launcher;
		findGames();
	}
	
	public void findGames() {
		File folder = new File("C:\\ProgramData\\Origin\\LocalContent");
		if(folder.exists()) {
			for(File game : folder.listFiles()) {
				OriginGame originGame = new OriginGame();
				if(game.isDirectory()) {
					originGame.setName(game.getName());
					List<String> names = new ArrayList<>();
					names.add(game.getName()
							.replaceAll("\\(TM\\)", "")
							.replaceAll("\\.", ""));
					originGame.setNamesToSay(names);
					originGame.setConfigName(game.getName()
							.replaceAll("\\(TM\\)", "")
							.toLowerCase()
							.replaceAll("\\.", "")
							.replaceAll("/", "")
							.replaceAll(",", "")
							.replaceAll(":", "")
							.replaceAll(" ", "_")
					
					);
					for(File gameFile : game.listFiles()) {
						if(gameFile.getPath().contains(".dat")) {
							originGame.setProductID(gameFile.getName()
									.replace(".dat", "")
									.replace("EAST", "EAST:")
							);
							break;
						}
					}
					for(String name : launcher.getApplicationsNames()) {
						if(!name.equalsIgnoreCase(originGame.getConfigName())) {
							addGame(originGame);
							break;
						}
					}
				}
			}
		}
	}
	
	public OriginGame getGame(String name) {
		for(Application application : launcher.getApplications()) {
			if(application.getName().equalsIgnoreCase(name) && application.getType() == AppTypes.ORIGIN) {
				return application.getContent(OriginGame.class);
			}
		}
		return null;
	}
	
	public void addGame(OriginGame game) {
		this.launcher.cfg.load();
		
		Application application = null;
		for(Application apps : launcher.getApplications()) {
			if(apps.getName().equalsIgnoreCase(game.getConfigName())) {
				application = apps;
				removeGame(game.getConfigName());
			}
		}
		
		if(application == null) {
			application = new Application();
			application.setName(game.getConfigName());
			application.setType(AppTypes.ORIGIN);
			application.setUniqueID("ORIGIN_"+game.getProductID());
			application.setCreated(System.currentTimeMillis());
		}
		
		JSONArray applications = JsonConfig.getJSONArray(launcher.cfg.getConfig(), "Applications");
		JSONObject appJson = new JSONObject(new Gson().toJson(application));
		appJson.put("content", new JSONObject(new Gson().toJson(game)));
		
		applications.put(appJson);
		
		launcher.cfg.getConfig().put("Applications", applications);
		
		this.launcher.cfg.save();
	}
	
	public void removeGame(String name) {
		this.launcher.cfg.load();
		JSONArray applications = JsonConfig.getJSONArray(launcher.cfg.getConfig(), "Applications");
		int id = 0;
		for(int i = 0; i < applications.length(); i++) {
			if(applications.getJSONObject(i).getString("name").equalsIgnoreCase(name)) {
				if(applications.getJSONObject(i).getString("type").equalsIgnoreCase(AppTypes.ORIGIN.name())) {
					id = i;
					break;
				}
			}
		}
		
		applications.remove(id);
		this.launcher.cfg.getConfig().put("Applications", applications);
		this.launcher.cfg.save();
	}
	
	public void launch(String name) {
		launch(getGame(name));
	}
	
	public void launch(OriginGame game) {
		try {
			Desktop.getDesktop().browse(new URI("origin://launchgame/" + game.getProductID()));
		} catch(IOException e) {
			e.printStackTrace();
		} catch(URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
