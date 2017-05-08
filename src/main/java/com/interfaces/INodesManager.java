package com.interfaces;

import java.util.Collection;
import java.util.Map;

import com.common.Counter;
import com.common.ServerInfo;

public interface INodesManager {

	/**
	 * Gets all the nodes.
	 * @return	List of all the nodes
	 */
	Collection<ServerInfo> getAllNodes();

	/**
	 * Get all the remote counters that are locally stored. (Doesn't return own counter)
	 * @return	List of all the counters.
	 */
	Collection<Counter> getAllCounters();

	/**
	 * Updates a remote counters locally. 
	 * @param nodeInfo	The node whose counter to update.
	 * @param counter	The new counter value.
	 * @return			If the counter is not newer then it won't update the counter and return false.
	 */
	boolean updateCounter(ServerInfo nodeInfo, Counter counter);

	/**
	 * Get the Node to Counter mapping. The String is the node's IP.
	 * @return			Map of Node to Counter mapping.
	 */
	Map<ServerInfo, Counter> getNodesToCounterMapping();
}