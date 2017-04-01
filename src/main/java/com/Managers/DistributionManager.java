package com.Managers;

import java.net.UnknownHostException;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Common.Counter;
import com.Common.NodesInfoResponse;
import com.Common.ServerInfo;
import com.Common.UpdateRequest;
import com.Interfaces.*;

@Service
public class DistributionManager implements IDistributionManager {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	IServerInfoProvider ServerInfoProvider;
	
	@Autowired
	IConnectionManager connectionManager;
	
	@Autowired
	INodesManager nodesManager;
	
	@Autowired
	ICountManager countManager;
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public void PublishToAllNodes(Counter counter) throws UnknownHostException {
		
		Collection<ServerInfo> nodes = nodesManager.GetAllNodes();
		ServerInfo serverInfo = ServerInfoProvider.GetServerInfo();

		for(ServerInfo node: nodes)
		{
			UpdateRequest request = new UpdateRequest(serverInfo, counter);
			connectionManager.SyncWithNode(node, request);
		}
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public void Register(ServerInfo nodeToRegisterWith) throws UnknownHostException	{
		
		ServerInfo nodeAddress = connectionManager.GetNodeConnectionInfo(nodeToRegisterWith);
		ServerInfo thisServerInfo = ServerInfoProvider.GetServerInfo();
		NodesInfoResponse nodesInfo = connectionManager.GetNodesInfo(nodeAddress);
		
		nodesInfo.getNodesInfo().forEach((node, counter) -> {
			if(node.equals(thisServerInfo)) {
				// If the node we are registering with has a node which is the current server itself, then it's safe to assume the current node is restarting.
				// As only a single IP:PORT can exist, we take the value from the remote node and update the local counter to that value.
				
				logger.info("Using Remote Node's info for '{}'. Updating Counter to {}" , thisServerInfo, counter);
				countManager.SetCounter(counter);
			}
			else {
				logger.info("Updating local nodes info using: Node: {}, Counter: {}", node, counter.toString());
				nodesManager.UpdateCounter(node, counter);
			}
		});
			
		// Adding the remote node's own counter to the list of counters it has reported. 
		// Better design Might be to have the node's own counter be part of the list of counters. 
		nodesManager.UpdateCounter(nodeAddress, nodesInfo.getOwnCounter());
		
		// Now that info of all nodes is update, publish own info to all the nodes.
		PublishToAllNodes(countManager.GetCounter());
	}	
}
