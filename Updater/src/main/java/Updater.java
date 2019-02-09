import Utils.JsonConfig;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
	
	public static void main(String[] args){
		for(String arg : args){
			if(arg.equalsIgnoreCase("--build")){
				buildPatch();
			}
			if(arg.equalsIgnoreCase("--update")){
				try {
					update();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void update() throws Exception {
		
		System.out.println("[INFO] updating...");
		
		File update = new File("update");
		if(!update.exists()){
			update.mkdir();
		}
		System.out.println("[INFO] downloading build file...");
		FileUtils.copyURLToFile(
				new URL(FILE_URL+"build.json"),
				new File("update/build.json"),
				1000000,
				1000000);
		System.out.println("[INFO] downloaded build file!");
		
		JsonConfig patch = new JsonConfig("update/build.json");
		patch.load();
		List<FileInfo> fileInfoList = new ArrayList<>();
		for(int i = 0; i<patch.getConfig().getJSONArray("Files").length(); i++){
			fileInfoList.add(new Gson().fromJson(patch.getConfig().getJSONArray("Files").getJSONObject(i).toString(), FileInfo.class));
		}
		System.out.println("[INFO] "+fileInfoList.size()+" entries found!");
		
		for(FileInfo info : fileInfoList) {
			File local = new File(info.path);
			if(local.exists()){
				if(info.isLibary)
					continue;
				int result = info.date.compareTo(getFileDate(local));
				if(result>0){
					downloadFile(info);
				}
				continue;
			}
			downloadFile(info);
		}
	}
	
	public static void downloadFile(FileInfo fileInfo){
		System.out.println("[INFO] downloading "+fileInfo.name+"...");
		if(fileInfo.path.contains("\\")){
			String[] path = fileInfo.path.split("\\\\");
			String current = path[0];
			for(int i = 0; i<path.length-1; i++){
				File folder = new File(current);
				if(!folder.exists()){
					folder.mkdir();
				}
				current+="\\"+path[i+1];
			}
		}
		try {
			if(new File(fileInfo.path).exists()) {
				new File(fileInfo.path).delete();
				System.out.println("[INFO] deleting "+fileInfo.name+"...");
				Thread.sleep(200);
			}
			FileUtils.copyURLToFile(
					new URL(FILE_URL+fileInfo.path.replaceAll("\\\\", "/")),
					new File(fileInfo.path),
					1000000,
					1000000);
		} catch(IOException e) {
			e.printStackTrace();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("[INFO] downloaded "+fileInfo.name+"!");
	}
	
	public static void buildPatch(){
		JsonConfig patch = new JsonConfig("build.json");
		patch.createFile();
		patch.load();
		patch.getConfig().put("patch", System.currentTimeMillis());
		List<FileInfo> fileInfoList = new ArrayList<>();
		File[] files = new File(System.getProperty("user.dir")).listFiles();
		for(File file : files){
			if(!file.isDirectory()) {
				String path = file.getPath().replace(System.getProperty("user.dir")+"\\","");
				System.out.println(path);
				if(path.equalsIgnoreCase("build.json"))
					continue;
				if(path.equalsIgnoreCase("Updater.jar"))
					continue;
				if(path.equalsIgnoreCase("update.bat"))
					continue;
				if(path.equalsIgnoreCase("build.bat"))
					continue;
				fileInfoList.add(getFileInfo(file));
			}else
				fileInfoList.addAll(getFilesFromFolder(file));
		}
		patch.getConfig().put("Files", new JSONArray(new Gson().toJson(fileInfoList)));
		patch.save();
	}
	
	public static List<FileInfo> getFilesFromFolder(File folder){
		List<FileInfo> fileInfoList = new ArrayList<>();
		for(File file : folder.listFiles()){
			if(!file.isDirectory())
				fileInfoList.add(getFileInfo(file));
			else
				fileInfoList.addAll(getFilesFromFolder(file));
		}
		return fileInfoList;
	}
	
	public static FileInfo getFileInfo(File file){
		FileInfo info = new FileInfo();
		info.name = file.getName();
		info.path = file.getPath().replace(System.getProperty("user.dir")+"\\", "");
		info.isLibary = isLibary(file);
		info.date = getFileDate(file);
		return info;
	}
	
	public static boolean isLibary(File file){
		return file.getPath().replace("\\"+file.getName(), "").split("\\\\")[file.getPath().split("\\\\").length-2].equalsIgnoreCase("libs");
	}
	
	public static String getFileDate(File file){
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
