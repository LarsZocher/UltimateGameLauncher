package GUI.screens;

import GUI.Menu;
import GUI.localization.Language;
import GUI.screens.AddGame.ProgramManager;
import GUI.screens.AddGame.Steam.EditSteamUser;
import GUI.screens.AddGame.Steam.EditSteamUserMode;
import GUI.screens.AddGame.Steam.NewSteamUserController;
import GUI.screens.AddGame.Steam.SteamGuard.AuthenticatorMenu;
import GUI.screens.Alert.Alert;
import GUI.screens.Alert.SimpleAlert;
import GUI.screens.Notification.*;
import GUI.screens.misc.initMenuController;
import api.GameLauncher.GameLauncher;
import api.GameLauncher.Steam.SteamUser;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class steamUserController extends initMenuController {
	
	private String name;
	private SteamUser user;
	private GameLauncher launcher;
	private steamController controller;
	
	@FXML
	private ImageView usericon;
	@FXML
	private Label username;
	@FXML
	private Pane bar;
	@FXML
	private ImageView deleteUser;
	@FXML
	private ImageView editUser;
	@FXML
	private ImageView linkUser;
	@FXML
	private ImageView star;
	@FXML
	private ImageView steamGuard;
	@FXML
	private JFXButton change;
	@FXML
	private JFXButton code;
	
	@Override
	public void init(Menu menu) {
		super.init(menu);
		user = launcher.getSteam().getUser(name);
		username.setText(user.getCurrentUsername());
		if(user.getImageMedium() == null || user.getImageMedium().isEmpty())
			usericon.setImage(new Image("icon/loading.png"));
		else
			usericon.setImage(new Image(user.getImageMedium()));
		if(launcher.getSteam().getLastUser().equalsIgnoreCase(name))
			setSelected(true);
		else
			setSelected(false);
		
		if(user.isMainAccount()) {
			star.setImage(new Image("icon/star.png"));
		} else {
			star.setImage(new Image("icon/star_border.png"));
		}
		
		if(!user.hasSteamGuard()) {
			steamGuard.setImage(new Image("icon/phone_delete.png"));
		}
		
		Tooltip deleteUserTip = new Tooltip();
		deleteUserTip.setText(Language.format(Menu.lang.getLanguage().SteamUserToolTipDeleteUser));
		deleteUserTip.setFont(new Font(12));
		Tooltip editUserTip = new Tooltip();
		editUserTip.setText(Language.format(Menu.lang.getLanguage().SteamUserToolTipEditUser));
		editUserTip.setFont(new Font(12));
		Tooltip linkUserTip = new Tooltip();
		linkUserTip.setText(Language.format(Menu.lang.getLanguage().SteamUserToolTipLinkUser));
		linkUserTip.setFont(new Font(12));
		Tooltip starTip = new Tooltip();
		starTip.setText(Language.format(Menu.lang.getLanguage().SteamUserToolTipMainUser));
		starTip.setFont(new Font(12));
		Tooltip authTip = new Tooltip();
		authTip.setText(Language.format(Menu.lang.getLanguage().SteamUserToolTipAuthenticator));
		authTip.setFont(new Font(12));
		Tooltip.install(deleteUser, deleteUserTip);
		Tooltip.install(editUser, editUserTip);
		Tooltip.install(linkUser, linkUserTip);
		Tooltip.install(star, starTip);
		Tooltip.install(steamGuard, authTip);
		
		
		change.setText(Language.format(Menu.lang.getLanguage().ButtonSwitch));
		code.setText(Language.format(Menu.lang.getLanguage().ButtonCode));
		
		
		bar.setId("bar-2");
		
		showIcons(false);
	}
	
	public void setUser(String name) {
		this.name = name;
	}
	
	public void setLauncher(GameLauncher launcher) {
		this.launcher = launcher;
	}
	
	public void setSteamController(steamController controller) {
		this.controller = controller;
	}
	
	public void setSelected(boolean selected) {
		if(selected) {
			bar.setId("bar-2");
		} else {
			bar.setId("lkljö");
		}
	}
	
	@Override
	public void onRefresh() {
	
	}
	
	@FXML
	void onEdit() {
		try {
			try {
				FXMLLoader userLoader = new FXMLLoader(ProgramManager.class.getClassLoader().getResource("fxml/NewSteamUser.fxml"));
				Parent userRoot = userLoader.load();
				Alert userAlert = new Alert(Menu.root);
				
				NewSteamUserController userController = userLoader.getController();
				userController.init(userAlert, EditSteamUserMode.EDIT);
				userController.setUserData(user);
				userController.setLauncher(launcher);
				userController.setCallback(() -> {
					controller.forceRefreshList();
				});
				
				userAlert.setContent((Region)userRoot);
				userAlert.setBackground(Menu.rootAnchor);
				userAlert.setBackgroundBlur(10);
				userAlert.setBackgroundColorAdjust(0, 0, -0.3, 0);
				userAlert.show();
			}catch(IOException e){
				e.printStackTrace();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	void onLink() {
		try {
			launcher.getSteam().getUser(name).createShortcutOnDesktop();
			
			SimpleAlert sa = new SimpleAlert();
			sa.setTitle(Language.format(Menu.lang.getLanguage().AlertSteamUserLinkTitle));
			sa.setMessage(Language.format(Menu.lang.getLanguage().AlertSteamUserLinkMessage, user.getCurrentUsername()));
			sa.addOption(Language.format(Menu.lang.getLanguage().ButtonOK), ButtonAlignment.RIGHT, new ButtonCallback() {
				@Override
				public void onClick() {
				
				}
			}, true, ButtonStyle.GHOST);
			
			Alert alert = new Alert(Menu.root);
			alert.setContent(sa);
			alert.setBackground(Menu.rootAnchor);
			alert.setBackgroundBlur(10);
			alert.setBackgroundColorAdjust(0, 0, -0.3, 0);
			alert.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	void onDelete() {
		try {
			SimpleAlert sa = new SimpleAlert();
			sa.setTitle(Language.format(Menu.lang.getLanguage().AlertSteamUserDeleteTitle));
			sa.setMessage(Language.format(Menu.lang.getLanguage().AlertSteamUserDeleteMessage, user.getCurrentUsername()));
			sa.addOption(Language.format(Menu.lang.getLanguage().ButtonDelete), ButtonAlignment.RIGHT, new ButtonCallback() {
				@Override
				public void onClick() {
					launcher.getSteam().deleteUser(name);
				}
			}, true, ButtonStyle.GHOST);
			sa.addOption(Language.format(Menu.lang.getLanguage().ButtonCancel), ButtonAlignment.RIGHT, new ButtonCallback() {
				@Override
				public void onClick() {
				}
			}, true, ButtonStyle.GHOST);
			
			Alert alert = new Alert(Menu.root);
			alert.setContent(sa);
			alert.setBackground(Menu.rootAnchor);
			alert.setBackgroundBlur(10);
			alert.setBackgroundColorAdjust(0, 0, -0.3, 0);
			alert.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	void onChange() {
		try {
			launcher.getSteam().forceChangeUser(user);
			controller.forceRefreshList();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	void onCode() {
		try {
			if(user.hasSteamGuard()) {
				String code = launcher.getSteam().getSteamGuard().getCode(user);
				launcher.getSteam().getSteamGuard().getConfirmations(user);
				controller.setCode(code, user);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} catch(Throwable throwable) {
			throwable.printStackTrace();
		}
	}
	
	@FXML
	void onFav() {
		if(!user.isMainAccount()) {
			user.setAsMainAccount();
			for(steamUserController userController : controller.getController()) {
				userController.star.setImage(new Image("icon/star_border.png"));
				userController.star.setVisible(false);
			}
			star.setImage(new Image("icon/star.png"));
			star.setVisible(true);
		}
	}
	
	@FXML
	void onSteamGuard() {
		AuthenticatorMenu menu = new AuthenticatorMenu() {
			@Override
			public void onSuccessfullyAdded() {
				controller.forceRefreshList();
				try {
					launcher.getSteam().getSteamGuard().doLogin(launcher.getSteam().getUser(user));
				} catch(Throwable throwable) {
					throwable.printStackTrace();
				}
			}
			
			@Override
			public void onSuccessfullyRemoved() {
				controller.forceRefreshList();
			}
		};
		menu.setUser(user.getUsername());
		try {
			menu.start(new Stage());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showIcons(boolean show) {
		deleteUser.setVisible(show);
		editUser.setVisible(show);
		linkUser.setVisible(show);
		if(!user.hasSteamGuard())
			steamGuard.setVisible(show);
		
		if(user.isMainAccount())
			star.setVisible(true);
		else
			star.setVisible(show);
	}
	
}
