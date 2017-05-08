package com.managers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.common.ServerInfo;
import com.interfaces.IServerInfoProvider;

@Service
public class ServerInfoProvider implements IServerInfoProvider{

	@Value( "${server.port}" )
	String port;

	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public ServerInfo getServerInfo() throws UnknownHostException
	{
		String ip = InetAddress.getLocalHost().getHostAddress();
		return new ServerInfo(ip, port);
	}
}
