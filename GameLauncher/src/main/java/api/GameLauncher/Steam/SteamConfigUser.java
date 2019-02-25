package api.GameLauncher.Steam;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class SteamConfigUser {
	private String username = "";
	private String password = "";
	private String steam64id = "";
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getSteam64id() {
		return steam64id;
	}
	
	public void setSteam64id(String steam64id) {
		this.steam64id = steam64id;
	}
}
