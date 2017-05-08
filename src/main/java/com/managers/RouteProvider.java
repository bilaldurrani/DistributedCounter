package com.managers;

import org.springframework.stereotype.Service;

import com.common.ServerInfo;
import com.interfaces.IRouteProvider;

@Service
public class RouteProvider implements IRouteProvider {
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public String getUpdateUrl(ServerInfo node)
	{
		return this.appendUrl(node, "update");
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public String getNodesInfoUrl(ServerInfo node)
	{
		return this.appendUrl(node, "nodesinfo");
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public String getNodeConnectionInfoUrl(ServerInfo node)
	{
		return this.appendUrl(node, "nodeconnectioninfo");
	}
	
	private String appendUrl(ServerInfo node, String urlToApend)
	{
		return String.format("%s/%s", node.getServerIpAndPort(), urlToApend);
	}
}
