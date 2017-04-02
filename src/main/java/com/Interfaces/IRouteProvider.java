package com.interfaces;

import com.common.ServerInfo;

public interface IRouteProvider {
	
	/**
	 * Get the update URL.
	 * @param node	Uses this node info to prepend the update URL.
	 * @return		The update URL.
	 */
	public String getUpdateUrl(ServerInfo node);
	
	/**
	 * Get the Nodes Info URL.
	 * @param node	Uses this node info to prepend the Nodes Info URL.
	 * @return		The Nodes Info URL.
	 */
	public String getNodesInfoUrl(ServerInfo node);
	
	/**
	 * Get the Nodes Connection Info URL.
	 * @param node	Uses this node info to prepend the Nodes Connection Info URL.
	 * @return		The Nodes Connection Info URL.
	 */
	public String getNodeConnectionInfoUrl(ServerInfo node);
}