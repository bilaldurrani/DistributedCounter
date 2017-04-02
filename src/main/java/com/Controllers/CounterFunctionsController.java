package com.controllers;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.Counter;
import com.interfaces.*;

@RestController
public class CounterFunctionsController {
	   
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ICountManager countManager;
	
	@Autowired
	INodesManager nodesManager;
	
	@Autowired
	IDistributionManager distributionManager;
	
	/**
	 * Increments the local counter by 1 and publishes the counter state to all the nodes.
	 * @throws Exception
	 */
    @PostMapping("/increment")
    void Increment() throws UnknownHostException {
        int c = countManager.Increment();
        logger.info("Incrementing counter. New value: {}", c);
        
        distributionManager.PublishToAllNodes(countManager.GetCounter());
    }

	/**
	 * Decrements the local counter by 1 and publishes the counter state to all the nodes.
	 * @throws Exception
	 */
    @PostMapping("/decrement")
    void Decrement() throws UnknownHostException {
    	int c = countManager.Decrement();
    	logger.info("Decrementing counter. New value: {}", c);
    	
    	distributionManager.PublishToAllNodes(countManager.GetCounter());
    }
    
    /**
     * Gets the current count. Takes the global count by taking sum of all Increment Counter and subtracting the sum of all Decrement Counters.
     * After that it adds the local counter count to generate the total count value. 
     * @return	The current Count of the distributed counter.
     */
    @GetMapping("/counter")
    int GetCount()
    {
    	logger.info("Getting Count");
    	
    	Collection<Counter> counters = nodesManager.GetAllCounters();
    	
    	Optional<Integer> allIncrements = counters.stream().map(c -> c.getIncrementCounter()).reduce((i,k) -> i+k);
    	Optional<Integer> allDecrements = counters.stream().map(c -> c.getDecrementCounter()).reduce((i,k) -> i+k);
    	
    	
    	int globalCount = 
    			(allIncrements.isPresent() ? allIncrements.get() : 0) 
    			- 
    			(allDecrements.isPresent() ? allDecrements.get() : 0);
    	
    	// TODO: Possibly the global count can be a Big Integer to reduce the chances of overflow.
    	return countManager.GetCount() + globalCount;
    }
}