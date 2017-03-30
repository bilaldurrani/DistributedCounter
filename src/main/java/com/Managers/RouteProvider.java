package com.Managers;

import org.springframework.stereotype.Service;

import com.Common.ServerInfo;
import com.Interfaces.IRouteProvider;

@Service
public class RouteProvider implements IRouteProvider {
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public String GetUpdateUrl(ServerInfo node)
	{
		return AppendUrl(node, "update");
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public String GetNodesInfoUrl(ServerInfo node)
	{
		return AppendUrl(node, "nodesinfo");
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public String GetNodeConnectionInfoUrl(ServerInfo node)
	{
		return AppendUrl(node, "nodeconnectioninfo");
	}
	
	private String AppendUrl(ServerInfo node, String urlToApend)
	{
		return String.format("%s/%s", node.getServerIpAndPort(), urlToApend);
	}
}
