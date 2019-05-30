package gui.screens.notification;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public enum  NotificationIcon {
	ERROR("error.png"),
	QUESTION("question.png"),
	INFO("info.png");
	
	String icon;
	
	NotificationIcon(String icon) {
		this.icon = icon;
	}
}
