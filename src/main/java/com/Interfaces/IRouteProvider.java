package com.interfaces;

import com.common.ServerInfo;

public interface IRouteProvider {
	
	/**
	 * Get the update URL.
	 * @param node	Uses this node info to prepend the update URL.
	 * @return		The update URL.
	 */
	public String GetUpdateUrl(ServerInfo node);
	
	/**
	 * Get the Nodes Info URL.
	 * @param node	Uses this node info to prepend the Nodes Info URL.
	 * @return		The Nodes Info URL.
	 */
	public String GetNodesInfoUrl(ServerInfo node);
	
	/**
	 * Get the Nodes Connection Info URL.
	 * @param node	Uses this node info to prepend the Nodes Connection Info URL.
	 * @return		The Nodes Connection Info URL.
	 */
	public String GetNodeConnectionInfoUrl(ServerInfo node);
}