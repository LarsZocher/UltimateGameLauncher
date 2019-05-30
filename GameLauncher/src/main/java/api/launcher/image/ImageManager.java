package api.launcher.image;

import api.launcher.Application;
import api.launcher.GameLauncher;
import api.launcher.steam.SteamApp;
import net.sf.image4j.codec.ico.ICODecoder;
import net.sf.image4j.codec.ico.ICOEncoder;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class ImageManager {
	
	private GameLauncher launcher;
	
	public ImageManager(GameLauncher launcher){
		this.launcher = launcher;
		
		File headerFolder = new File(launcher.folderPath+"Games\\Header");
		if(!headerFolder.exists()) headerFolder.mkdir();
		
		File iconFolder = new File(launcher.folderPath+"Games\\Icon");
		if(!iconFolder.exists()) iconFolder.mkdir();
		
		File tempFolder = new File(launcher.folderPath+"Games\\Temp");
		if(!tempFolder.exists())
			tempFolder.mkdir();
		else{
			for(File file : tempFolder.listFiles()) {
				file.delete();
			}
		}
	}
	
	public String getImage(String path, PathType type){
		File original = new File(path);
		if(original.exists()){
			switch(type){
				case FILE:{
					return original.getAbsolutePath();
				}
				case URL:{
					try {
						return original.toURI().toURL().toString();
					} catch(MalformedURLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}
	
	public String getIconPNG(Application app, IconSize size, PathType type){
		switch(type){
			case URL:{
				try {
					return new File(getIconPNG(app, size)).toURI().toURL().toString();
				} catch(MalformedURLException e) {
					e.printStackTrace();
				}
			}
			case FILE:{
				return getIconPNG(app, size);
			}
		}
		return null;
	}
	
	public String getIconICO(Application app, IconSize size, PathType type){
		switch(type){
			case URL:{
				try {
					return new File(getIconICO(app, size)).toURI().toURL().toString();
				} catch(MalformedURLException e) {
					e.printStackTrace();
				}
			}
			case FILE:{
				return getIconICO(app, size);
			}
		}
		return null;
	}
	
	public String getIconICO(Application app, IconSize size) {
		try {
			String path = getIconPNG(app, size);
			File icon = new File(path.replaceAll("\\.png", ".ico"));
			ICOEncoder.write(ImageIO.read(new File(path)), icon);
			return icon.getAbsolutePath();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getIconPNG(Application app, IconSize size) {
		try {
			System.out.println(app);
			String path;
			if(app.isDefaultIcon())
				path = getDefaultIconPNG(app);
			else
				path = getCustomIconPNG(app);
			
			System.out.println(path);
			
			BufferedImage bi = ImageIO.read(new File(path));
			switch(size){
				case S_32:{
					File newFile = new File(path.replace(".png", "_32.png"));
					ImageIO.write(resize(bi, 32, 32), "png", newFile);
					return newFile.getAbsolutePath();
				}
				case S_64:{
					File newFile = new File(path.replace(".png", "_64.png"));
					ImageIO.write(resize(bi, 64, 64), "png", newFile);
					return newFile.getAbsolutePath();
				}
				case S_128:{
					File newFile = new File(path.replace(".png", "_128.png"));
					ImageIO.write(resize(bi, 128, 128), "png", newFile);
					return newFile.getAbsolutePath();
				}
				default:{
					return path;
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		
		return dimg;
	}
	
	public String getCustomIconPNG(Application app){
		String customFile = app.getIconPath();
		File pngFile = new File(launcher.folderPath+"Games\\Icon\\"+app.getName()+".png");
		File newPngFile = new File(customFile);
		
		String filteredLink = customFile;
		if(filteredLink.contains("?")){
			filteredLink = filteredLink.split("\\?")[0];
		}
		
		boolean isWebLink = customFile.toLowerCase().contains("http://")||customFile.toLowerCase().contains("https://");
		boolean isValidType = filteredLink.endsWith(".png")||filteredLink.endsWith(".ico");
		if(!isWebLink&&newPngFile.exists()){
			pngFile.delete();
			try {
				FileUtils.copyFile(newPngFile, pngFile);
			} catch(IOException e) {
				e.printStackTrace();
			}
			return pngFile.getAbsolutePath();
		}
		if(isWebLink&&isValidType){
			try {
				URLConnection con = new URL(customFile).openConnection();
				con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
				InputStream in = con.getInputStream();
				
				String icoPath = "";
				if(filteredLink.endsWith(".png"))
					icoPath = launcher.folderPath+"Games\\Icon\\"+app.getName()+".png";
				if(filteredLink.endsWith(".ico"))
					icoPath = launcher.folderPath+"Games\\Icon\\"+app.getName()+".ico";
				File icoFile = new File(icoPath);
				if(icoFile.exists())
					icoFile.delete();
				
				Files.copy(in, Paths.get(icoPath));
				
				if(customFile.endsWith(".ico")) {
					List<BufferedImage> images = ICODecoder.read(icoFile);
					images.sort(new Comparator<BufferedImage>() {
						@Override
						public int compare(BufferedImage o1, BufferedImage o2) {
							return Integer.compare(o2.getWidth(), o1.getWidth());
						}
					});
					ImageIO.write(images.get(0), "png", pngFile);
				}
				return pngFile.getAbsolutePath();
			} catch(IOException e) {
				e.printStackTrace();
				System.out.println("Failed to download icon!");
			}
		}
		return null;
	}
	
	public String getDefaultIconPNG(Application app){
		File pngFile = new File(launcher.folderPath+"Games\\Icon\\"+app.getName()+".png");
		downloadDefaultIconPNG(app);
		return pngFile.getAbsolutePath();
	}
	
	private void downloadDefaultIconPNG(Application app){
		String filePath = launcher.folderPath+"Games\\Icon\\"+app.getName()+".png";
		switch(app.getType()){
			case STEAM:{
				try(InputStream in = new URL("http://media.steampowered.com/steamcommunity/public/images/apps/"+app.getContent(SteamApp.class).getAppID()+"/"+app.getContent(SteamApp.class).getClientIcon()+".ico").openStream()) {
					String icoPath = launcher.folderPath+"Games\\Icon\\"+app.getName()+".ico";
					File icoFile = new File(icoPath);
					if(icoFile.exists())
						icoFile.delete();
					
					Files.copy(in, Paths.get(icoPath));
					
					List<BufferedImage> images = ICODecoder.read(icoFile);
					images.sort(new Comparator<BufferedImage>() {
						@Override
						public int compare(BufferedImage o1, BufferedImage o2) {
							return Integer.compare(o2.getWidth(), o1.getWidth());
						}
					});
					ImageIO.write(images.get(0), "png", new File(filePath));
				} catch(IOException e) {
					e.printStackTrace();
					System.out.println("Failed to download steam icon!");
				}
				break;
			}
			case ORIGIN:
			case BATTLENET:{
				try(InputStream in = new URL("http://217.79.178.92/games/icon/"+app.getUniqueID()+".png").openStream()) {
					File pngFile = new File(filePath);
					if(pngFile.exists())
						pngFile.delete();
					
					Files.copy(in, Paths.get(filePath));
				} catch(IOException e) {
					System.out.println("Failed to download origin/battlenet icon!");
				}
				break;
			}
		}
	}
	
	public String getHeaderURL(Application app){
		if(app.isDefaultHeader()){
			return getDefaultHeaderURL(app);
		}
		try {
			return new File(getHeaderFile(app)).toURI().toURL().toString();
		} catch(MalformedURLException e) {
			return null;
		}
	}
	
	public String getHeaderFile(Application app){
		String link;
		if(app.isDefaultHeader())
			link = getDefaultHeaderURL(app);
		else
			link = app.getHeaderPath();
		
		File jpgFile = new File(launcher.folderPath+"Games\\Header\\"+app.getName()+".jpg");
		File newJpgFile = new File(link);
		
		String filteredLink = link;
		if(filteredLink.contains("?")){
			filteredLink = filteredLink.split("\\?")[0];
		}
		
		boolean isWebLink = link.toLowerCase().contains("http://")||link.toLowerCase().contains("https://");
		boolean isValidType = filteredLink.endsWith(".jpg");
		if(!isWebLink&&newJpgFile.exists()&&(!newJpgFile.getAbsolutePath().equalsIgnoreCase(jpgFile.getAbsolutePath()))){
			jpgFile.delete();
			try {
				FileUtils.copyFile(newJpgFile, jpgFile);
			} catch(IOException e) {
				e.printStackTrace();
			}
			return jpgFile.getAbsolutePath();
		}
		if(isWebLink&&isValidType){
			try(InputStream in = new URL(link).openStream()) {
				String icoPath = launcher.folderPath+"Games\\Header\\"+app.getName()+".jpg";
				File icoFile = new File(icoPath);
				if(icoFile.exists())
					icoFile.delete();
				
				Files.copy(in, Paths.get(icoPath));
				return jpgFile.getAbsolutePath();
			} catch(IOException e) {
				System.out.println("Failed to download header!");
			}
		}
		return null;
	}
	
	public String getDefaultHeaderURL(Application app){
		switch(app.getType()){
			case STEAM:{
				return "https://steamcdn-a.akamaihd.net/steam/apps/"+app.getContent(SteamApp.class).getAppID()+"/header.jpg";
			}
			case ORIGIN:
			case BATTLENET:{
				return "http://217.79.178.92/games/header/"+app.getUniqueID()+".jpg";
			}
		}
		return null;
	}
}
