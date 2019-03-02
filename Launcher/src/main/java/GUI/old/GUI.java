//package GUI.old;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.scene.image.Image;
//import javafx.scene.layout.Pane;
//import javafx.stage.Stage;
//import javafx.stage.StageStyle;
//
///**
// * Removing of this disclaimer is forbidden.
// *
// * @author BubbleEgg
// * @verions: 1.0.0
// **/
//
//public class GUI extends Application {
//
//	public Stage stage;
//	public Scene scene;
//
//	private double xOffset = 0;
//	private double yOffset = 0;
//
//	@Override
//	public void start(Stage primaryStage) throws Exception {
//		try{
//			createConfig();
//
//			stage = primaryStage;
//			primaryStage.initStyle(StageStyle.UNDECORATED);
//			primaryStage.getIcons().setAll(new Image("file:materials/Icon.png"));
//			primaryStage.setTitle("DotBot Launcher");
//
//			FXMLLoader loader = new FXMLLoader(GUI.class.getResource("GUI/old/Launcher.fxml"));
//			Pane pane = loader.load();
//
//			pane.setOnMousePressed(event -> {
//				if(event.getY()<60) {
//					xOffset = event.getSceneX();
//					yOffset = event.getSceneY();
//				}
//			});
//			pane.setOnMouseDragged(event -> {
//				if(event.getY()<60) {
//					stage.setX(event.getScreenX() - xOffset);
//					stage.setY(event.getScreenY() - yOffset);
//				}
//			});
//
//			scene = new Scene(pane);
//
//			GUIController controller = loader.getController();
//			controller.setGui(this);
//
//			primaryStage.setScene(scene);
//			primaryStage.show();
//
//			controller.init();
//
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
//
//	public void createConfig(){
//		ConfigFile config = new ConfigFile("launcher.yml");
//		config.createFile();
//		config.load();
//		config.setDefault("Start.cmdConsole", false);
//		config.setDefault("Start.NormalConsole", true);
//
//		config.setDefault("Stats.Started", 0);
//		config.setDefault("Stats.Used", 0);
//		config.setDefault("Stats.Commands", 0);
//		config.save();
//	}
//
//	public static void main(String [] args){
//		launch(args);
//	}
//
//}
