package com.Managers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Common.Counter;
import com.Common.ServerInfo;
import com.Interfaces.INodesManager;
import com.Interfaces.IServerInfoProvider;

@Service
public class NodesManager implements INodesManager {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	IServerInfoProvider serverInfoProvider;
		
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
		boolean retVal = true;;
		Counter oldCounter = serverToCounterMap.getOrDefault(node, null);
		
		if(oldCounter != null)
		{
			if(!oldCounter.UpdateDecrementCounter(counter.getDecrementCounter()));
			{
				logger.warn("Update to decrement counter failed (Possibly as old value is greater) for {}. Old Value: {}, New Value: {}", 
						node.toString(), 
						oldCounter.getDecrementCounter(), 
						counter.getDecrementCounter());
				retVal = false;
			}
			
			if(!oldCounter.UpdateIncrementCounter(counter.getIncrementCounter()));
			{
				logger.warn("Update to increment counter failed (Possibly as old value is greater) for {}. Old Value: {}, New Value: {}", 
						node.toString(), 
						oldCounter.getIncrementCounter(), 
						counter.getIncrementCounter());
				retVal = false;
			}
		}
		else {
			serverToCounterMap.put(node, counter);
			logger.info("Adding new counter to local cached counters: Node: {}, Counter: {}", node, counter);
		}

		return retVal;
	}
}


