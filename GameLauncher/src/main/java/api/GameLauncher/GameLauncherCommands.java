package api.GameLauncher;

import Bot.Commands.Command;
import Bot.PluginLoader.CommandLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class GameLauncherCommands extends CommandLoader{
	
	private GameLauncher gl;
	
	public GameLauncherCommands(GameLauncher gl){
		this.gl = gl;
	}
	
	@Override
	public void registerCommands() {
		{
			List<String> commands = new ArrayList<>();
			commands.add("starte {args}");
			commands.add("starten {args}");
			commands.add("starter {args}");
			commands.add("öffne {args}");
			commands.add("öffne das programm {args}");
			commands.add("starte das programm {args}");
			getCommandManager().registerCommand(new Command(commands) {
				@Override
				public void onCommand(String command, String usedFilter, String[] cmdArgs, String keyword, String[] arg, String args, int clientID) {
					if(args!=null||!args.equalsIgnoreCase("")){
						gl.launchByNameToSay(args);
					}
				}
			});
		}
		{
			List<String> commands = new ArrayList<>();
			commands.add("starte steam");
			commands.add("starten steam");
			commands.add("starter steam");
			commands.add("öffne steam");
			commands.add("öffne das programm steam");
			commands.add("starte das programm steam");
			getCommandManager().registerCommand(new Command(commands) {
				@Override
				public void onCommand(String command, String usedFilter, String[] cmdArgs, String keyword, String[] arg, String args, int clientID) {
					if(gl.getSteam().getMainAccount()!=null){
						gl.getSteam().changeUser(gl.getSteam().getMainAccount());
					}else{
						try {
							if(!gl.getSteam().getPath().isEmpty())
								Runtime.getRuntime().exec(gl.getSteam().getPath());
						} catch(IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
		}
		{
			List<String> commands = new ArrayList<>();
			commands.add("switche zu meinen main account");
			commands.add("switche zu meinen main steam account");
			commands.add("switche zu meinen haupt account");
			commands.add("switche zu meinen haupt steam account");
			commands.add("wechsle zu meinen main account");
			commands.add("wechsle zu meinen main steam account");
			commands.add("wechsle zu meinen haupt account");
			commands.add("wechsle zu meinen haupt steam account");
			getCommandManager().registerCommand(new Command(commands) {
				@Override
				public void onCommand(String command, String usedFilter, String[] cmdArgs, String keyword, String[] arg, String args, int clientID) {
					if(gl.getSteam().getMainAccount()!=null){
						gl.getSteam().changeUser(gl.getSteam().getMainAccount());
					}
				}
			});
		}
	}
}
