package main;

import mslinks.ShellLink;
import mslinks.ShellLinkException;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class main {
	
	public static void main(String[] args){
//		if(args[0].equalsIgnoreCase("--start")){
//			try {
//				Runtime.getRuntime().exec("taskkill /F /IM steam.exe");
//				Thread.sleep(500);
//				Runtime.getRuntime().exec("D:/Program Files (x86)/Steam/Steam.exe -login mindestorm LZms11.? -dev -console" + " -applaunch " + args[1]);
//			} catch(IOException e) {
//				e.printStackTrace();
//			} catch(InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
			try {
				ShellLink.createLink("C:\\Users\\Lars\\Desktop\\Jar\\DotBot\\plugins\\DotBotPlugin_GameLauncher.jar", "C:\\Users\\Lars\\Desktop\\test.lnk");
				ShellLink link = new ShellLink(new File("C:\\Users\\Lars\\Desktop\\test.lnk"));
				link.setCMDArgs("--start 70");
				
				FileReader reader = new FileReader(new File("C:\\Users\\Lars\\Desktop\\Half-Life.url"));
				char [] a = new char[500];
				reader.read(a);
				String text = "";
				for(char c : a)
					text+=c+"";
				reader.close();

				text = text.split("IconFile=")[1].split("\\.ico")[0]+".ico";
				System.out.println("\""+text+"\"");
				link.setIconLocation(text);
				link.getHeader().setIconIndex(0);
				
				link.saveTo("C:\\Users\\Lars\\Desktop\\test.lnk");
				
			} catch(IOException e) {
				e.printStackTrace();
			} catch(ShellLinkException e) {
				e.printStackTrace();
			}
	}
}
