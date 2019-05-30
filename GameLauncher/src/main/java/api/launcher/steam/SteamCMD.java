package api.launcher.steam;

import net.platinumdigitalgroup.jvdf.VDFNode;
import net.platinumdigitalgroup.jvdf.VDFParser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class SteamCMD {
	
	private BufferedReader reader;
	private BufferedReader readerError;
	private PrintWriter writer;
	private Process process;
	
	public static void main(String[] args) throws Exception {
		SteamCMD cmd = new SteamCMD();
		cmd.test();
//		boolean free = true;
//		String steam64id = "76561198242115578";
//		HttpURLConnection con = (HttpURLConnection) new URL("https://api.steampowered.com/IPlayerService/GetOwnedGames/v1/?key=ECF61DC7A97863B4871287BF468A51D0&steamid="+steam64id+"&include_appinfo=0&include_played_free_games="+(free?1:0)).openConnection();
//		con.setRequestMethod("GET");
//
//		con.setDoOutput(true);
//
//		BufferedReader in = new BufferedReader(
//				new InputStreamReader(con.getInputStream()));
//		String inputLine;
//		StringBuffer response = new StringBuffer();
//
//		while((inputLine = in.readLine()) != null) {
//			response.append(inputLine);
//		}
//		in.close();
//
//		JSONObject responseJSON = new JSONObject(response.toString());
//		System.out.println(response.toString());
//		List<Integer> ids = new ArrayList<>();
//		if(responseJSON.getJSONObject("response").has("games")) {
//			JSONArray games = responseJSON.getJSONObject("response").getJSONArray("games");
//			for(int i = 0; i < games.length(); i++) {
//				ids.add(games.getJSONObject(i).getInt("appid"));
//			}
//		}
//		HashMap<Integer, SteamApp> apps = cmd.getSteamApps(toIntArray(ids));
//		int i = 0;
//		for(SteamApp value : apps.values()) {
//			System.out.println((i+1)+"-"+ids.size()+" "+value.toString());
//			i++;
//		}
	}
	
	static int[] toIntArray(List<Integer> list){
		int[] ret = new int[list.size()];
		for(int i = 0;i < ret.length;i++)
			ret[i] = list.get(i);
		return ret;
	}
	
	public HashMap<Integer, SteamApp> getSteamApps(List<Integer> ids){
		return getSteamApps(toIntArray(ids));
	}
	
	public HashMap<Integer, SteamApp> getSteamApps(int... ids){
		HashMap<Integer, SteamApp> apps = new HashMap<>();
		HashMap<Integer, String> content = null;
		try {
			content = getAppInfoPrint(ids);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		for(int id : ids){
			if(content.get(id)==null||content.get(id).isEmpty()||content.get(id).equalsIgnoreCase("null")) {
				apps.put(id, null);
				continue;
			}
			if(!content.get(id).contains("common"))
				continue;
			
			VDFNode node = new VDFParser().parse(content.get(id));
			VDFNode common = node.getSubNode(id+"").getSubNode("common");
			SteamApp app = new SteamApp();
			app.setAppID(id+"");
			app.setType(common.getString("type"));
			app.setName(common.getString("name"));
			if(common.containsKey("associations"))
				for(int i = 0; i<common.getSubNode("associations").size(); i++){
					String type = common.getSubNode("associations").getSubNode(i+"").getString("type");
					switch(type){
						case "developer":{
							app.setDeveloper(app.getDeveloper().isEmpty()?common.getSubNode("associations").getSubNode(i+"").getString("name"):app.getDeveloper()+", "+common.getSubNode("associations").getSubNode(i+"").getString("name"));
							break;
						}
						case "publisher":{
							app.setPublisher(app.getPublisher().isEmpty()?common.getSubNode("associations").getSubNode(i+"").getString("name"):app.getPublisher()+", "+common.getSubNode("associations").getSubNode(i+"").getString("name"));
							break;
						}
						case "franchise":{
							app.setFranchise(app.getFranchise().isEmpty()?common.getSubNode("associations").getSubNode(i+"").getString("name"):app.getFranchise()+", "+common.getSubNode("associations").getSubNode(i+"").getString("name"));
							break;
						}
					}
				}
			if(common.containsKey("icon"))
				app.setIcon(common.getString("icon"));
			if(common.containsKey("clienticon"))
				app.setClientIcon(common.getString("clienticon"));
			else
				app.setClientIcon(app.getIcon());
			app.setCreationDate(System.currentTimeMillis());
			app.setConfigName(app.getName());
			apps.put(id, app);
		}
		return apps;
	}
	
	public HashMap<Integer, String> getAppInfoPrint(int... ids) throws Exception {
		File steamcmd = new File("steam/steamcmd.exe");
		if(!steamcmd.exists()){
			System.out.println("[SteamCMD] ERROR: steamcmd.exe not found!");
			return new HashMap<>();
		}
		
		HashMap<Integer, String> apps = new HashMap<>();
		List<String> startArgs = new ArrayList<>();
		
		startArgs.add("steam/steamcmd.exe");
		startArgs.add("+login anonymous");
		
		final int rounds = 2;
		for(int i = 0; i<rounds; i++) {
			for(int id : ids) {
				startArgs.add("+app_info_print " + id);
				startArgs.add("+a");
			}
			startArgs.add("+force_install_dir ./4");
			startArgs.add("+app_update 4");
		}
		startArgs.add("+exit");
		
		ProcessBuilder builder = new ProcessBuilder(startArgs);
		builder.directory(new File("steam"));
		builder.redirectErrorStream(true);
		process = builder.start();
		
		reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		readerError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		writer = new PrintWriter(process.getOutputStream());
		
		
		new Thread(() -> {
			int currentIndex = 0;
			String sCurrentLine;
			String currentAppInfo = "";
			int round = 1;
			boolean isNext = false;
			try {
				while((sCurrentLine = reader.readLine()) != null) {
					
					if(sCurrentLine.startsWith("No app info")){
						currentAppInfo = "";
						apps.put(ids[currentIndex], "null");
						currentIndex++;
						if(currentIndex==ids.length) {
							currentIndex = 0;
							round++;
						}
						isNext = false;
						if(round==rounds)
							System.out.println("[SteamCMD] "+ids[currentIndex] + " not found");
					}
					if(isNext&&!sCurrentLine.equalsIgnoreCase("Command not found: a")){
						currentAppInfo += sCurrentLine+"\n";
					}
					if(sCurrentLine.startsWith("AppID : ")){
						currentAppInfo = "";
						isNext = true;
					}
					if(sCurrentLine.equalsIgnoreCase("Command not found: a")&&isNext){
						apps.put(ids[currentIndex], currentAppInfo);
						currentIndex++;
						if(currentIndex==ids.length) {
							currentIndex = 0;
							round++;
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}).start();
		process.waitFor();
		System.out.println("end!");
		return apps;
	}
	
	public void test() throws Exception{
		List<String> startArgs = new ArrayList<>();
		
		startArgs.add("steam/steamcmd.exe");
		
		ProcessBuilder builder = new ProcessBuilder(startArgs);
		builder.directory(new File("steam"));
		builder.redirectErrorStream(true);
		process = builder.start();
		
		reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		readerError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		writer = new PrintWriter(process.getOutputStream());
		
		
		new Thread(() -> {
			String sCurrentLine;
			try {
				while((sCurrentLine = reader.readLine()) != null) {
					System.out.println(sCurrentLine);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}).start();
		new Thread(() -> {
			String sCurrentLine;
			try {
				while((sCurrentLine = readerError.readLine()) != null) {
					System.out.println(sCurrentLine);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}).start();
		
		Thread.sleep(2000);
		System.out.println("login");
		writer.println("login anonymous");
		writer.flush();
	}
}
