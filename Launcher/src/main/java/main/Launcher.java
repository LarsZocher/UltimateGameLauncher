package main;

import java.awt.*;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Timer;
import java.util.TimerTask;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class Launcher {
	
	public static void main(String[] args){
		File batchConsole = new File("DotBotConsole.bat");
		File batchConsoleOnly = new File("DotBotConsoleOnly.bat");
		File batchLauncherConsole = new File("DotBotLauncherConsole.bat");
		File batchLauncherConsoleOnly = new File("DotBotLauncherConsoleOnly.bat");
		Console console = System.console();
		if(console == null && !GraphicsEnvironment.isHeadless()) {
			String filename = Launcher.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
			try {
				File batch = new File("DotBotLauncher.bat");
				if(!batch.exists()){
					batch.createNewFile();
					PrintWriter writer = new PrintWriter(batch);
					writer.println("@echo off");
					writer.println("java -jar "+filename);
					writer.println("exit");
					writer.flush();
				}
				if(!batchConsole.exists()){
					batchConsole.createNewFile();
					PrintWriter writer = new PrintWriter(batchConsole);
					writer.println("@echo off");
					writer.println("java -jar DotBot.jar");
					writer.println("pause");
					writer.println("exit");
					writer.flush();
				}
				if(!batchConsoleOnly.exists()){
					batchConsoleOnly.createNewFile();
					PrintWriter writer = new PrintWriter(batchConsoleOnly);
					writer.println("@echo off");
					writer.println("java -jar DotBot.jar --console");
					writer.println("pause");
					writer.println("exit");
					writer.flush();
				}
				if(!batchLauncherConsole.exists()){
					batchLauncherConsole.createNewFile();
					PrintWriter writer = new PrintWriter(batchLauncherConsole);
					writer.println("@echo off");
					writer.println("java -jar "+filename+" --both");
					writer.println("exit");
					writer.flush();
				}
				if(!batchLauncherConsoleOnly.exists()){
					batchLauncherConsoleOnly.createNewFile();
					PrintWriter writer = new PrintWriter(batchLauncherConsoleOnly);
					writer.println("@echo off");
					writer.println("java -jar "+filename+" --console");
					writer.println("exit");
					writer.flush();
				}
				Runtime.getRuntime().exec("cmd /c start \"\" "+batch.getPath());
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Starting Launcher...");
			fileCheck();
			checkForUpdates();
			startDotBot(args, batchConsole, batchConsoleOnly);
		}
	}
	
	private static void fileCheck(){
		System.out.println("Scanning Files...");
		File DotBot = new File("DotBot.jar");
		File libs = new File("libs");
		System.out.println("  - DotBot.jar   : "+(DotBot.exists()?"FOUND":"MISSING"));
		System.out.println("  - 'libs' Folder: "+(libs.exists()?"FOUND":"MISSING"));
		System.out.println("Scanning Files Complete!");
	}
	
	
	private static void checkForUpdates(){
		System.out.println("Checking for updates...");
		File mainPluginFolder = new File("D://Users//Lars//IdeaProjects//RunnableJars//plugins");
		File PluginFolder = new File("plugins");
		int counter = 0;
		if(mainPluginFolder.exists()){
			for(File file : mainPluginFolder.listFiles()) {
				if(file.getName().contains("DotBotPlugin_")){
					File oldFile = new File(PluginFolder.toPath()+"//"+file.getName());
					if(oldFile.exists()){
						try {
							BasicFileAttributes attrold = Files.readAttributes(oldFile.toPath(), BasicFileAttributes.class);
							BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
							if(attrold.lastModifiedTime().equals(attr.lastModifiedTime())){
								continue;
							}
						} catch(IOException e) {
							e.printStackTrace();
						}
					}
					try {
						System.out.println("  updating "+file.getName()+"...");
						counter++;
						Files.copy(file.toPath(), new File(PluginFolder.toPath()+"//"+file.getName()).toPath(), REPLACE_EXISTING);
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		System.out.println("Updating finished, "+counter+" files were updated!");
	}
	
	private static void startDotBot(String[] args, File consoleFile, File onlyConsoleFile){
		File DotBot = new File("DotBot.jar");
		if(!DotBot.exists()){
			System.out.println("DotBot.jar was not found! Waiting 20sec. for it!");
			for(int i = 0; i<20; i++){
				if(DotBot.exists())
					break;
				try {
					Thread.sleep(1000);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(!DotBot.exists()){
				System.out.println("DotBot.jar was still not found! Shutting down!");
				System.exit(0);
			}
		}
		try {
			if(args[0].equalsIgnoreCase("--console")){
				try {
					Runtime.getRuntime().exec("cmd /c start \"\" "+onlyConsoleFile.getPath());
				} catch(IOException e1) {
					e1.printStackTrace();
				}
			}
			if(args[0].equalsIgnoreCase("--both")){
				try {
					Runtime.getRuntime().exec("cmd /c start \"\" "+consoleFile.getPath());
				} catch(IOException e1) {
					e1.printStackTrace();
				}
			}
		}catch(Exception e){
			try {
				Runtime.getRuntime().exec("java -jar DotBot.jar");
			} catch(IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
