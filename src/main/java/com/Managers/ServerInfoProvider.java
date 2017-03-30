package com.Managers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.Common.ServerInfo;
import com.Interfaces.IServerInfoProvider;

@Service
public class ServerInfoProvider implements IServerInfoProvider{

	@Value( "${server.port}" )
	String port;

	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public ServerInfo GetServerInfo() throws UnknownHostException
	{
		String ip = InetAddress.getLocalHost().getHostAddress();
		return new ServerInfo(ip, port);
	}
}
