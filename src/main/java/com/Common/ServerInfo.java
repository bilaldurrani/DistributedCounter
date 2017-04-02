package com.common;

public class ServerInfo {
	private String serverIpAndPort;
	
	public ServerInfo(){
		serverIpAndPort = "";
	}
	
	public ServerInfo(String serverInfo){
		setServerIpAndPort(serverInfo);
	}
	
	public ServerInfo(String ip, String port) {
		setServerIpAndPort(String.format("http://%s:%s", ip, port));
	}

	
	@Override
	public String toString() {
		return getServerIpAndPort();
	}

	public String getServerIpAndPort() {
		return serverIpAndPort;
	}

	public void setServerIpAndPort(String serverIpAndPort) {
		this.serverIpAndPort = serverIpAndPort;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && this.getClass().isInstance(obj)) {
			return ((ServerInfo)obj).getServerIpAndPort().equals(serverIpAndPort);
		}
		
		// Otherwise always return false.
		return false;
	}
	
	@Override
	public int hashCode() {
		// We only need the server ip and port hash for using maps properly.
		return serverIpAndPort.hashCode();
	}
}
