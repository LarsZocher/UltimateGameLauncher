package api.GameLauncher.Steam;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class SteamGuardInformation {
	
	private String shared_secret = "";
	private String serial_number = "";
	private String revocation_code = "";
	private String uri = "";
	private String server_time = "";
	private String account_name = "";
	private String token_gid = "";
	private String identity_secret = "";
	private String secret_1 = "";
	private int status = 1;
	private String device_id = "";
	private String steamguard_scheme = "";
	private String steamid = "";
	
	public String getShared_secret() {
		return shared_secret;
	}
	
	public void setShared_secret(String shared_secret) {
		this.shared_secret = shared_secret;
	}
	
	public String getSerial_number() {
		return serial_number;
	}
	
	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}
	
	public String getRevocation_code() {
		return revocation_code;
	}
	
	public void setRevocation_code(String revocation_code) {
		this.revocation_code = revocation_code;
	}
	
	public String getUri() {
		return uri;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public String getServer_time() {
		return server_time;
	}
	
	public void setServer_time(String server_time) {
		this.server_time = server_time;
	}
	
	public String getAccount_name() {
		return account_name;
	}
	
	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}
	
	public String getToken_gid() {
		return token_gid;
	}
	
	public void setToken_gid(String token_gid) {
		this.token_gid = token_gid;
	}
	
	public String getIdentity_secret() {
		return identity_secret;
	}
	
	public void setIdentity_secret(String identity_secret) {
		this.identity_secret = identity_secret;
	}
	
	public String getSecret_1() {
		return secret_1;
	}
	
	public void setSecret_1(String secret_1) {
		this.secret_1 = secret_1;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getDevice_id() {
		return device_id;
	}
	
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	
	public String getSteamguard_scheme() {
		return steamguard_scheme;
	}
	
	public void setSteamguard_scheme(String steamguard_scheme) {
		this.steamguard_scheme = steamguard_scheme;
	}
	
	public String getSteamid() {
		return steamid;
	}
	
	public void setSteamid(String steamid) {
		this.steamid = steamid;
	}
}
