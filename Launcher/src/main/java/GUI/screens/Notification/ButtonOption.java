package GUI.screens.Notification;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author CodingAir
 * @verions: 1.0.0
 **/

public enum ButtonOption {
	YES("Ja"),
	NO("Nein"),
	CANCEL("Abbrechen"),
	OK("OK"),
	TRY_AGAIN("Erneut versuchen"),
	CLOSE("Schließen"),
	CONTINUE("Weiter"),
	DELETE("Löschen"),
	BROWSE("Suchen"),
	BROWSE_DOTS("Suchen..."),
	APPLY("Anwenden"),
	ADD("Hinzufügen"),
	CUSTOM("Custom");
	
	String text;
	
	ButtonOption(String text) {
		this.text = text;
	}
	
	public ButtonOption setText(String text) {
		this.text = text;
		return this;
	}
}
