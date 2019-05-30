package main;

import api.launcher.utils.WinRegistry;

import java.lang.reflect.InvocationTargetException;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class main {
	
	public static void main(String[] args){
		try {
			String user = WinRegistry.readString(WinRegistry.HKEY_CURRENT_USER, "Software\\Valve\\Steam", "AutoLoginUser");
			String path = WinRegistry.readString(WinRegistry.HKEY_CURRENT_USER, "Software\\Valve\\Steam", "SteamPath");
			String exe = WinRegistry.readString(WinRegistry.HKEY_CURRENT_USER, "Software\\Valve\\Steam", "SteamExe");
			System.out.println("Current User: "+user);
			System.out.println("Steam path: "+path);
			System.out.println("Steam executable: "+exe);
		} catch(IllegalAccessException e) {
			e.printStackTrace();
		} catch(InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
