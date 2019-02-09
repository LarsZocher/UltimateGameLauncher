package GUI.screens.Notification;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class Option {
	
	private String option;
	private ButtonAlignment alignment;
	private ButtonCallback callback;
	private boolean closeOnClick = true;
	
	public Option(String option, ButtonAlignment alignment, ButtonCallback callback) {
		this.option = option;
		this.alignment = alignment;
		this.callback = callback;
	}
	
	public Option(String option, ButtonAlignment alignment, ButtonCallback callback, boolean closeOnClick) {
		this.option = option;
		this.alignment = alignment;
		this.callback = callback;
		this.closeOnClick = closeOnClick;
	}
	
	public String getOption() {
		return option;
	}
	
	public void setOption(String option) {
		this.option = option;
	}
	
	public ButtonAlignment getAlignment() {
		return alignment;
	}
	
	public void setAlignment(ButtonAlignment alignment) {
		this.alignment = alignment;
	}
	
	public ButtonCallback getCallback() {
		return callback;
	}
	
	public void setCallback(ButtonCallback callback) {
		this.callback = callback;
	}
	
	public boolean isCloseOnClick() {
		return closeOnClick;
	}
	
	public void setCloseOnClick(boolean closeOnClick) {
		this.closeOnClick = closeOnClick;
	}
}
