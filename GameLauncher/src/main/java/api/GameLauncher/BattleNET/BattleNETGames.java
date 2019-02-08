package api.GameLauncher.BattleNET;

import api.GameLauncher.GameLauncher;
import net.sf.image4j.codec.ico.ICOEncoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author CodingAir
 * @verions: 1.0.0
 **/

public enum BattleNETGames {
	OVERWATCH("Overwatch", "Pro", "Overwatch.png", "Overwatch_header.jpg"),
	STARCRAFT_2("StarCraft II", "S2", "StarCraft2.png", "StarCraft2_header.jpg"),
	STARCRAFT("StarCraft", "S1", "StarCraft.png", "StarCraft_header.jpg"),
	HOTS("Heroes of the Storm", "Hero", "HOTS.png", "HOTS_header.jpg"),
	WOW("World of Warcraft", "WoW", "WoW.png", "WoW_header.jpg"),
	WARCRAFT_3("Warcraft III", "W3", "Warcraft.png", "Warcraft_header.jpg"),
	DIABLO_3("Diablo III", "D3", "Diablo.png", "Diablo_header.jpg"),
	HEARTHSTONE("Hearthstone", "WTCG", "Hearthstone.png", "Hearthstone_header.jpg"),
	DESTINY_2("Destiny 2", "DST2", "Destiny.png", "Destiny_header.jpg"),
	COD_BO4("Call of Duty: Black Ops 4", "VIPR", "COD_BO4.png", "COD_BO4_header.jpg");
	
	private String name;
	private String code;
	private String iconFile;
	private String headerFile;
	
	BattleNETGames(String name, String code, String iconFile, String headerFile) {
		this.name = name;
		this.code = code;
		this.iconFile = iconFile;
		this.headerFile = headerFile;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getIconFile() {
		return iconFile;
	}
	
	public String getHeaderFile() {
		return headerFile;
	}
	
	public static BattleNETGames getByName(String name){
		for(BattleNETGames games : BattleNETGames.values()) {
			if(games.getName().equalsIgnoreCase(name)){
				return games;
			}
		}
		return OVERWATCH;
	}
	
	public static BattleNETGames getByConfigName(String name){
		for(BattleNETGames games : BattleNETGames.values()) {
			if(games.getConfigName().equalsIgnoreCase(name)){
				return games;
			}
		}
		return OVERWATCH;
	}
	
	public String getConfigName(){
		String name = this.name;
		name = name.toLowerCase();
		name = name.replaceAll("\\.", "");
		name = name.replaceAll("/", "");
		name = name.replaceAll(",", "");
		name = name.replaceAll(":", "");
		name = name.replaceAll(" ", "_");
		return name;
	}
}
