package com.Common;

import java.util.HashMap;
import java.util.Map;

public class NodesInfoResponse {

	private Counter ownCounter;

	private Map<ServerInfo, Counter> nodesInfo;

	public NodesInfoResponse() {
		nodesInfo = new HashMap<ServerInfo, Counter>();
		setOwnCounter(new Counter());
	}

	public Map<ServerInfo, Counter> getNodesInfo() {
		return nodesInfo;
	}

	public void setNodesInfo(Map<ServerInfo, Counter> nodesInfo) {
		this.nodesInfo = nodesInfo;
	}

	public Counter getOwnCounter() {
		return ownCounter;
	}

	public void setOwnCounter(Counter ownCounter) {
		this.ownCounter = ownCounter;
	}
}
