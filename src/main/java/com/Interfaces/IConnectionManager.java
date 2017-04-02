package com.interfaces;

import com.common.NodesInfoResponse;
import com.common.ServerInfo;
import com.common.UpdateRequest;

/**
 * Interface dealing with node-to-node communication.
 * @author durrab01
 *
 */
public interface IConnectionManager {

	/**
	 * Sync own counter state with a remote node. 
	 * @param node		Connection info of the remote node.
	 * @param request	Request detailing this node's states.
	 */
	public void SyncWithNode(ServerInfo node, UpdateRequest request);

	/**
	 * Gets remote node's connection info (IP and PORT).
	 * @param node		Connection info of the remote node.
	 * @return			Remote nodes connection info. (This can be different from the param node).
	 */
	public ServerInfo GetNodeConnectionInfo(ServerInfo node);
	
	/**
	 * Gets all the nodes info from a remote node.
	 * @param node		Connection info of the remote node.
	 * @return			NodeInfoResponse which encapsulates all the information about all the nodes in the system.
	 * 					This response has their IPs and their Counter states.
	 */
	public NodesInfoResponse GetNodesInfo(ServerInfo node);
}
