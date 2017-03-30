package com.Interfaces;

import java.net.UnknownHostException;

import com.Common.ServerInfo;

public interface IServerInfoProvider {

	/**
	 * Get the current node's info.
	 * @return							Current node's info in a http://IP:PORT format.
	 * @throws UnknownHostException		If the IP cannot be found then this will throw and exception.
	 */
	ServerInfo GetServerInfo() throws UnknownHostException;

}
