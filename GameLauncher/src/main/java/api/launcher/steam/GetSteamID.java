package api.launcher.steam;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class GetSteamID extends Application{
	
	public static String query = "";
	private static String currentQuery = "null";
	public static ArrayList<String> execute = new ArrayList<>();
	public static boolean started = false;
	public static int volume = 30;
	public static GetSteamID getSteamID;
	private WebEngine engine;
	public static boolean isPlaying = false;
	public static boolean showWindow = false;
	boolean add = true;
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		
		WebView myWebView = new WebView();
		engine = myWebView.getEngine();
		
		Button btn = new Button("Load Youtube");
		btn.setOnAction((ActionEvent event) -> engine.load("https://steamcommunity.com/openid/login?openid.ns=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0&openid.mode=checkid_setup&openid.return_to=https%3A%2F%2Fsteamidfinder.com%2Flookup%2F%3Fopenid&openid.realm=https%3A%2F%2Fsteamidfinder.com&openid.ns.sreg=http%3A%2F%2Fopenid.net%2Fextensions%2Fsreg%2F1.1&openid.claimed_id=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select&openid.identity=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select"));
		
		Button btn2 = new Button("Mute");
		btn2.setOnAction((ActionEvent event) -> engine.executeScript("player.mute()"));
		
		Button btn3 = new Button("Unmute");
		btn3.setOnAction((ActionEvent event) -> engine.executeScript("player.unMute()"));
		
		Button btn4 = new Button("+");
		btn4.setOnAction((ActionEvent event) -> {
			if(volume+10>100){
				volume = 100;
			}else{
				volume = volume+10;
			}
		});
		
		Button btn5 = new Button("-");
		btn5.setOnAction((ActionEvent event) -> {
			if(volume-10<0){
				volume = 0;
			}else{
				volume = volume-10;
			}
		});
		
		VBox root = new VBox();
		HBox buttons = new HBox();
		buttons.getChildren().addAll(btn, btn2, btn3, btn4, btn5);
		root.getChildren().addAll(myWebView, buttons);
		
		Scene scene = new Scene(root, 800, 500);
		primaryStage.setScene(scene);
		primaryStage.setTitle("DotBot YouTube");
		primaryStage.show();
	}
	
	
	
//	public static void execute(String execute){
//		WebWindow.execute.add(execute);
//	}
//
//	public static void stopPlayer(){
//		execute("player.stopVideo()");
//		isPlaying = false;
//	}
	
	
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		query = s.nextLine();
		
		launch(args);
	}
}
