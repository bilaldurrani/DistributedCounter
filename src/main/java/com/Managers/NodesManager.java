package com.managers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.common.Counter;
import com.common.ServerInfo;
import com.interfaces.INodesManager;

@Service
public class NodesManager implements INodesManager {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Map<ServerInfo, Counter> serverToCounterMap = new HashMap<ServerInfo, Counter>();

	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public Collection<ServerInfo> GetAllNodes()
	{
		return serverToCounterMap.keySet();
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public Map<ServerInfo, Counter> GetNodesToCounterMapping()
	{
		return serverToCounterMap;
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public Collection<Counter> GetAllCounters()
	{
		return serverToCounterMap.values();
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public synchronized boolean UpdateCounter(ServerInfo node, Counter counter)
	{
		Counter oldCounter = serverToCounterMap.getOrDefault(node, null);
		
		if(oldCounter != null)
		{
			boolean decrementUpdated = true;
			boolean incrementUpdated = true;

			if(!oldCounter.UpdateDecrementCounter(counter.getDecrementCounter()))
			{
				logger.warn("Update to decrement counter failed (Possibly as old value is greater) for %s. Old Value: %d, New Value: %d", 
						node.toString(), 
						oldCounter.getDecrementCounter(), 
						counter.getDecrementCounter());
				decrementUpdated = false;
			}
			
			if(!oldCounter.UpdateIncrementCounter(counter.getIncrementCounter()))
			{
				logger.warn("Update to increment counter failed (Possibly as old value is greater) for {}. Old Value: {}, New Value: {}", 
						node.toString(), 
						oldCounter.getIncrementCounter(), 
						counter.getIncrementCounter());
				incrementUpdated = false;
			}
			
			return incrementUpdated || decrementUpdated;
		}
		else {
			serverToCounterMap.put(node, counter);
			logger.info("Adding new counter to local cached counters: Node: {}, Counter: {}", node, counter);
			return true;
		}
	}
}


