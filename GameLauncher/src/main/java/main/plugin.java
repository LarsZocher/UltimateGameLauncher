package main;

import Bot.PluginLoader.DotBotPlugin;
import api.GameLauncher.GameLauncher;
import api.GameLauncher.GameLauncherCommands;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class plugin extends DotBotPlugin {
	
	@Override
	public void onEnable() {
		GameLauncher gl = new GameLauncher();
		this.registerCommandLoader(new GameLauncherCommands(gl));
		
	}
	
	@Override
	public void onDisable() {
	
	}
}
