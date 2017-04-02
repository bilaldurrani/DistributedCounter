package com.interfaces;

import java.net.UnknownHostException;

import com.common.Counter;
import com.common.ServerInfo;

public interface IDistributionManager {

	/**
	 * Publishes the Counter state to all the nodes.
	 * 
	 * @param counter				The counter the publish.
	 * @throws UnknownHostException	Can throw an unknown host exception if this node's IP address cannot be resolved.
	 */
	void PublishToAllNodes(Counter counter) throws UnknownHostException;
	
	/**
	 * Registers with a node. As part of registration:
	 * 1. Gets the remote's nodes IP address as qualified by the remote node. This also verified the node is up.
	 * 2. Get all the nodes information from the remote node.
	 * 3. Updates the remote node states locally.
	 * 4. Published it's state to all the remote nodes. This publish signals the nodes that this node is ready to receive updates.
	 * 
	 * @param nodeToRegisterWith	The remote node to initiate the registration with.
	 * @throws UnknownHostException	Can throw an unknown host exception if this node's IP address cannot be resolved.
	 */
	void Register(ServerInfo nodeToRegisterWith) throws UnknownHostException;
}
