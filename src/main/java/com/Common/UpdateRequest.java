package com.Common;

/**
 * 
 * @author Bilal A. Durrani
 * Class which encapsulates the update request.
 * Current node's Counter is sent over to the other nodes so the state is in sync.
 */
public class UpdateRequest {
	private ServerInfo serverInfo;
	
	private Counter counter;

	public UpdateRequest() {
	}
 
	public UpdateRequest(ServerInfo serverInfo, Counter counter) {
		this.serverInfo = serverInfo;
		this.counter = counter;
	}

	public ServerInfo getServerInfo() {
		return serverInfo;
	}

	public Counter getCounter() {
		return counter;
	}
	
	@Override
	public String toString() {
		return String.format("ServerAddress:%s, Counter:%s", serverInfo.toString(), counter.toString());
	}
}
