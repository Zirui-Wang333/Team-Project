package GameMenu.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MultiPlayer {

	String username;
	int port;
	InetAddress ipAddress;

	public MultiPlayer(InetAddress ipAddress, int port, String username) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.username = username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setIPAddress(String ipAddress) {
		try {
			this.ipAddress = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public InetAddress getIPAddress() {
		return ipAddress;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return port;
	}
	
}
