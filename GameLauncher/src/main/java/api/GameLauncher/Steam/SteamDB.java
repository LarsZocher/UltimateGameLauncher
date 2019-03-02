package api.GameLauncher.Steam;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class SteamDB {
	
	public static SteamApp getSteamAppByName(String name) {
		SteamApp app = new SteamApp();
		try {
			String encoding = "UTF-8";
			String searchText = name + " app id";
			
			Document google = Jsoup.connect("https://www.google.de/search?q=" + URLEncoder.encode(searchText, encoding)).userAgent("Mozilla/5.0").get();
			
			String URL = google.getElementsByTag("cite").get(0).text();
			if(!URL.toLowerCase().contains("steamdb")){
				return app;
			}
			
			google = Jsoup.connect(URL).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
			
			app.setAppID(google.getElementsByTag("td").get(1).text());
			app.setAppType(google.getElementsByTag("td").get(3).text());
			app.setName(google.getElementsByTag("td").get(5).text());
			app.setDeveloper(google.getElementsByTag("td").get(7).text());
			app.setPublisher(google.getElementsByTag("td").get(9).text());
			app.setSupportedSystems(google.getElementsByTag("td").get(11).text());
			app.setLastRecordUpdate(google.getElementsByTag("td").get(13).text());
			app.setLastChangeNumber(google.getElementsByTag("td").get(15).text());
			app.setReleaseDate(google.getElementsByTag("td").get(17).text());
			app.setClientIcon((google.getElementsByTag("img").get(1)+"").split("src=")[1].split("\"")[1]);
			app.setPicture("https://steamcdn-a.akamaihd.net/steam/apps/"+app.getAppID()+"/header.jpg");
			app.setCreationDate(System.currentTimeMillis());
			
			for(int i = 0; i<google.getElementsByTag("div").size(); i++){
				Element element = google.getElementsByTag("div").get(i);
				if(element.hasClass("header-wrapper header-app owned")) {
					try {
						app.setBackground((element + "").split("'")[1]);
					} catch(ArrayIndexOutOfBoundsException e) {
						app.setBackground("https://steamcdn-a.akamaihd.net/steam/apps/271590/page_bg_generated_v6b.jpg?t=1546027132");
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return app;
	}
	
	public static List<DBSearchResult> searchByName(String name, int size) {
		List<DBSearchResult> apps = new ArrayList<>();
		try {
			String encoding = "UTF-8";
			String searchText = name;
			
			Document google = Jsoup.connect("https://steamdb.info/search/?a=app&q=" + URLEncoder.encode(searchText, encoding)).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
			
			int i = 0;
			for(Element e : google.getElementsByClass("app")){
				i++;
				DBSearchResult result = new DBSearchResult();
				result.setAppID(e.getElementsByTag("td").get(0).text());
				result.setAppType(e.getElementsByTag("td").get(1).text());
				result.setName(e.getElementsByTag("td").get(2).text());
				apps.add(result);
				if(i==size){
					Collections.sort(apps, new Comparator<DBSearchResult>() {
						@Override
						public int compare(DBSearchResult o1, DBSearchResult o2) {
							return o1.getName().length() - o2.getName().length();
						}
					});
					System.out.println();
					return apps;
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		Collections.sort(apps, new Comparator<DBSearchResult>() {
			@Override
			public int compare(DBSearchResult o1, DBSearchResult o2) {
				return o1.getName().length() - o2.getName().length();
			}
		});
		return apps;
	}
	
	public static SteamApp getSteamAppByID(String AppID) {
		SteamApp app = new SteamApp();
		try {
			String encoding = "UTF-8";
			
			Document google = Jsoup.connect("https://steamdb.info/app/"+URLEncoder.encode(AppID, encoding)+"/").userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
			
			app.setAppID(google.getElementsByTag("td").get(1).text());
			app.setAppType(google.getElementsByTag("td").get(3).text());
			app.setName(google.getElementsByTag("td").get(5).text());
			app.setDeveloper(google.getElementsByTag("td").get(7).text());
			app.setPublisher(google.getElementsByTag("td").get(9).text());
			app.setSupportedSystems(google.getElementsByTag("td").get(11).text());
			app.setLastRecordUpdate(google.getElementsByTag("td").get(13).text());
			app.setLastChangeNumber(google.getElementsByTag("td").get(15).text());
			app.setReleaseDate(google.getElementsByTag("td").get(17).text());
			app.setClientIcon((google.getElementsByTag("img").get(1)+"").split("src=")[1].split("\"")[1]);
			app.setPicture("https://steamcdn-a.akamaihd.net/steam/apps/"+app.getAppID()+"/header.jpg");
			app.setCreationDate(System.currentTimeMillis());
			
			for(int i = 0; i<google.getElementsByTag("div").size(); i++){
				Element element = google.getElementsByTag("div").get(i);
				if(element.hasClass("header-wrapper header-app owned")) {
					try {
						app.setBackground((element + "").split("'")[1]);
					}catch(ArrayIndexOutOfBoundsException e){
						app.setBackground("https://steamcdn-a.akamaihd.net/steam/apps/271590/page_bg_generated_v6b.jpg?t=1546027132");
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return app;
	}
	
	public static String getIcon(String AppID){
		try {
			String encoding = "UTF-8";
			
			Document google = Jsoup.connect("https://steamdb.info/app/" + URLEncoder.encode(AppID, encoding) + "/").userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
			
			return (google.getElementsByTag("img").get(1)+"").split("src=")[1].split("\"")[1];
		}catch(IOException e){
		
		}
		return "";
	}
	
	public static void main(String[] args){
		getSteamAppByName("GTA 5");
	}
}
