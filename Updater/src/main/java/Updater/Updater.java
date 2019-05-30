package updater;

import Utils.JsonConfig;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class Updater {
	
	public static final String FILE_URL = "http://217.79.178.92/launcher/release/";
	public static boolean jre = true;
	public static String[] args = new String[0];
	
	static{
		File file = new File("ApplyUpdate.bat");
		if(file.exists()){
			file.delete();
		}
	}
	
	public static void main(String[] args) {
		Updater.args = args;
		
		boolean update = false;
		boolean build = false;
		boolean launcher = false;
		boolean console = false;
		boolean localJRE;
		for(String arg : args) {
			if(arg.equalsIgnoreCase("--noJRE")) {
				jre = false;
			}
			if(arg.equalsIgnoreCase("--build")) {
				build = true;
			}
			if(arg.equalsIgnoreCase("--update")) {
				update = true;
			}
			if(arg.equalsIgnoreCase("--launcher")) {
				launcher = true;
			}
			if(arg.equalsIgnoreCase("--console")) {
				console = true;
			}
		}
		
		if(update) {
			try {
				if(!new File("updater.json").exists())
					setJRE();
				loadJRE();
				update();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		if(build)
			buildPatch();
		
		if(launcher) {
			localJRE = new File("jre/bin/java.exe").exists();
			try {
				if(console) {
					if(localJRE)
						Runtime.getRuntime().exec("cmd.exe /K start jre/bin/java.exe -jar Launcher.jar --noUpdate");
					else
						Runtime.getRuntime().exec("cmd.exe /K start java -jar Launcher.jar --noUpdate");
				} else {
					if(localJRE)
						Runtime.getRuntime().exec("cmd.exe /K start jre/bin/javaw.exe -jar Launcher.jar --noUpdate");
					else
						Runtime.getRuntime().exec("cmd.exe /K start javaw -jar Launcher.jar --noUpdate");
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void setJRE(){
		JsonConfig config = new JsonConfig("updater.json");
		config.load();
		config.getConfig().put("jre", jre);
		config.save();
	}
	
	public static void loadJRE(){
		JsonConfig config = new JsonConfig("updater.json");
		config.load();
		jre = config.getConfig().getBoolean("jre");
	}
	
	public static boolean needsUpdate() {
		System.out.println("[Updater] Checking for updates...");
		
		File update = new File("update");
		if(!update.exists()) {
			update.mkdir();
		}
		System.out.println("[Updater] Downloading build file...");
		try {
			FileUtils.copyURLToFile(
					new URL(FILE_URL + "build.json"),
					new File("update/build.json"),
					1000000,
					1000000);
		} catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println("[Updater] Downloaded build file!");
		
		Utils.JsonConfig patch = new Utils.JsonConfig("update/build.json");
		patch.load();
		List<FileInfo> fileInfoList = new ArrayList<>();
		for(int i = 0; i < patch.getConfig().getJSONArray("Files").length(); i++) {
			fileInfoList.add(new Gson().fromJson(patch.getConfig().getJSONArray("Files").getJSONObject(i).toString(), FileInfo.class));
		}
		System.out.println("[Updater] " + fileInfoList.size() + " entries found!");
		
		for(FileInfo info : fileInfoList) {
			File local = new File(info.path);
			if(!jre) {
				if(info.path.startsWith("jre"))
					continue;
			}
			if(local.exists()) {
				if(info.isLibary)
					continue;
				int result = info.date.compareTo(getFileDate(local));
				if(result > 0) {
					return true;
				}
				continue;
			}
			return true;
		}
		return false;
	}
	
	public static void update() throws Exception {
		
		System.out.println("[Updater] Updating...");
		
		File update = new File("update");
		if(!update.exists()) {
			update.mkdir();
		}
		System.out.println("[Updater] Downloading build file...");
		FileUtils.copyURLToFile(
				new URL(FILE_URL + "build.json"),
				new File("update/build.json"),
				1000000,
				1000000);
		System.out.println("[Updater] Downloaded build file!");
		
		JsonConfig patch = new JsonConfig("update/build.json");
		patch.load();
		List<FileInfo> fileInfoList = new ArrayList<>();
		for(int i = 0; i < patch.getConfig().getJSONArray("Files").length(); i++) {
			fileInfoList.add(new Gson().fromJson(patch.getConfig().getJSONArray("Files").getJSONObject(i).toString(), FileInfo.class));
		}
		System.out.println("[Updater] " + fileInfoList.size() + " entries found!");
		
		for(FileInfo info : fileInfoList) {
			if(info.path.equalsIgnoreCase("Updater.jar")) {
				int result = info.date.compareTo(getFileDate(new File(info.path)));
				if(result > 0) {
					System.out.println("[Updater] Updating updater!");
					
					FileInfo localInfo = new FileInfo();
					localInfo.path = "update/Updater.jar";
					localInfo.name = info.name;
					localInfo.isLibary = info.isLibary;
					localInfo.date = info.date;
					
					downloadFile(info, localInfo);
					createUpdateFile();
					System.exit(0);
					return;
				}
			}
		}
		
		for(FileInfo info : fileInfoList) {
			if(info.path.equalsIgnoreCase("Updater.jar"))
				continue;
			File local = new File(info.path);
			if(!jre) {
				if(info.path.startsWith("jre"))
					continue;
			}
			if(local.exists()) {
				if(info.isLibary)
					continue;
				int result = info.date.compareTo(getFileDate(local));
				if(result > 0) {
					downloadFile(info);
				}
				continue;
			}
			downloadFile(info);
		}
		
		System.out.println("[Updater] Updates finished!");
	}
	
	public static void createUpdateFile() {
		File batch = new File("ApplyUpdate.bat");
		if(batch.exists()) {
			batch.delete();
		}
		try {
			batch.createNewFile();
			
			PrintWriter writer = new PrintWriter(batch);
			writer.println("@echo off");
			writer.println("timeout /t 2");
			writer.println("del Updater.jar /f /q");
			writer.println("move update\\Updater.jar Updater.jar");
			
			String arguments = "";
			for(String arg : args) {
				arguments+=" "+arg;
			}
			
			boolean localJRE = new File("jre/bin/java.exe").exists();
			try {
				if(localJRE)
					writer.println("start jre/bin/java.exe -jar Updater.jar"+arguments);
				else
					writer.println("start java -jar Updater.jar"+arguments);
			} catch(Exception e) {
				e.printStackTrace();
			}
			writer.println("");
			
			writer.flush();
			writer.close();
			
			Runtime.getRuntime().exec("cmd /c ApplyUpdate.bat", null, new File(batch.getAbsolutePath().replace("ApplyUpdate.bat", "")));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void downloadFile(FileInfo fileInfo) {
		downloadFile(fileInfo, fileInfo);
	}
	
	public static void downloadFile(FileInfo fileInfo, FileInfo localInfo) {
		System.out.println("[Updater] Downloading " + fileInfo.name + "...");
		if(fileInfo.path.contains("\\")) {
			String[] path = fileInfo.path.split("\\\\");
			String current = path[0];
			for(int i = 0; i < path.length - 1; i++) {
				File folder = new File(current);
				if(!folder.exists()) {
					folder.mkdir();
				}
				current += "\\" + path[i + 1];
			}
		}
		try {
			if(new File(localInfo.path).exists()) {
				new File(localInfo.path).delete();
				System.out.println("[Updater] Deleting " + fileInfo.name + "...");
				Thread.sleep(200);
			}
			FileUtils.copyURLToFile(
					new URL(FILE_URL + fileInfo.path.replaceAll("\\\\", "/")),
					new File(localInfo.path),
					1000000,
					1000000);
		} catch(IOException e) {
			e.printStackTrace();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("[Updater] Downloaded " + fileInfo.name + "!");
	}
	
	public static void buildPatch() {
		JsonConfig patch = new JsonConfig("build.json");
		patch.createFile();
		patch.load();
		patch.getConfig().put("patch", System.currentTimeMillis());
		List<FileInfo> fileInfoList = new ArrayList<>();
		File[] files = new File(System.getProperty("user.dir")).listFiles();
		for(File file : files) {
			String path = file.getPath().replace(System.getProperty("user.dir") + "\\", "");
			if(path.equalsIgnoreCase("build.json"))
				continue;
			if(path.equalsIgnoreCase("update.bat"))
				continue;
			if(path.equalsIgnoreCase("updateNoJRE.bat"))
				continue;
			if(path.equalsIgnoreCase("build.bat"))
				continue;
			if(path.equalsIgnoreCase("buildNoJRE.bat"))
				continue;
			if(path.equalsIgnoreCase("GameLauncher"))
				continue;
			if(path.equalsIgnoreCase("update"))
				continue;
			if(path.equalsIgnoreCase("jre")) {
				if(!jre)
					continue;
			}
			if(!file.isDirectory()) {
				fileInfoList.add(getFileInfo(file));
			} else
				fileInfoList.addAll(getFilesFromFolder(file));
		}
		System.out.println("PATCH: " + fileInfoList.size() + " files added to patch.json!");
		patch.getConfig().put("Files", new JSONArray(new Gson().toJson(fileInfoList)));
		patch.save();
	}
	
	public static List<FileInfo> getFilesFromFolder(File folder) {
		List<FileInfo> fileInfoList = new ArrayList<>();
		for(File file : folder.listFiles()) {
			if(!file.isDirectory())
				fileInfoList.add(getFileInfo(file));
			else
				fileInfoList.addAll(getFilesFromFolder(file));
		}
		return fileInfoList;
	}
	
	public static FileInfo getFileInfo(File file) {
		FileInfo info = new FileInfo();
		info.name = file.getName();
		info.path = file.getPath().replace(System.getProperty("user.dir") + "\\", "");
		info.isLibary = isLibary(file);
		info.date = getFileDate(file);
		return info;
	}
	
	public static boolean isLibary(File file) {
		return file.getPath().replace("\\" + file.getName(), "").split("\\\\")[file.getPath().split("\\\\").length - 2].equalsIgnoreCase("libs");
	}
	
	public static String getFileDate(File file) {
		BasicFileAttributes attrLauncher = null;
		try {
			attrLauncher = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			return attrLauncher.lastModifiedTime().toString();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return "none";
	}
}

class FileInfo {
	public String name = "";
	public String path = "";
	public String date = "";
	public boolean isLibary = false;
}
