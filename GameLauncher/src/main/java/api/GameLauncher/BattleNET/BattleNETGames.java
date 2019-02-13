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
	OVERWATCH("Overwatch", "Pro"),
	STARCRAFT_2("StarCraft II", "S2"),
	STARCRAFT("StarCraft", "S1"),
	HOTS("Heroes of the Storm", "Hero"),
	WOW("World of Warcraft", "WoW"),
	WARCRAFT_3("Warcraft III", "W3"),
	DIABLO_3("Diablo III", "D3"),
	HEARTHSTONE("Hearthstone", "WTCG"),
	DESTINY_2("Destiny 2", "DST2"),
	COD_BO4("Call of Duty: Black Ops 4", "VIPR");
	
	private String name;
	private String code;
	
	BattleNETGames(String name, String code) {
		this.name = name;
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCode() {
		return code;
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
