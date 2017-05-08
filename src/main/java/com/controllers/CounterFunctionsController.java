package com.controllers;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	 * @throws UnknownHostException
	 */
	// Using PUT and not POST as a resource (increment counter) is being Updated
    @PutMapping("/increment")
    void increment() throws UnknownHostException {
        int c = countManager.increment();
        logger.info("Incrementing counter. New value: {}", c);
        
        // If publish to all nodes fail, we do nothing. A subsequent increment will publish the current node's state again.
        // The system is designed as AP and will eventually be consistent. 
        // This can be fixed by having a queue of all the failed requests and a thread which periodically retries them.
        distributionManager.publishToAllNodes(countManager.getCounter());
    }

	/**
	 * Decrements the local counter by 1 and publishes the counter state to all the nodes.
	 * @throws UnknownHostException
	 */
    // Using PUT and not POST as a resource (decrement counter) is being Updated
    @PutMapping("/decrement")
    void decrement() throws UnknownHostException {
    	int c = countManager.decrement();
    	logger.info("Decrementing counter. New value: {}", c);
    	
    	// If publish to all nodes fail, we do nothing. A subsequent increment will publish the current node's state again.
        // The system is designed as AP and will eventually be consistent. 
        // This can be fixed by having a queue of all the failed requests and a thread which periodically retries them.
    	distributionManager.publishToAllNodes(countManager.getCounter());
    }
    
    /**
     * Gets the current count. Takes the global count by taking sum of all Increment Counter and subtracting the sum of all Decrement Counters.
     * After that it adds the local counter count to generate the total count value. 
     * @return	The current Count of the distributed counter.
     */
    @GetMapping("/counter")
    int getCount() throws ArithmeticException 
    {
    	logger.info("Getting Count");
    	
    	Collection<Counter> counters = nodesManager.getAllCounters();
    	
    	// If the total sum of the nodes is greater then int.max then an exception will be thrown. This can be fixed by using something like big integer.
    	Optional<Integer> allIncrements = counters.stream().map(c -> c.getIncrementCounter()).reduce((i,k) -> Math.addExact(i, k));
    	Optional<Integer> allDecrements = counters.stream().map(c -> c.getDecrementCounter()).reduce((i,k) -> Math.addExact(i, k));
    	
    	int globalCount = 
    			(allIncrements.isPresent() ? allIncrements.get() : 0) 
    			- 
    			(allDecrements.isPresent() ? allDecrements.get() : 0);
    	
    	// NOTE: Possibly the global count can be a Big Integer to reduce the chances of overflow.
    	return countManager.getCount() + globalCount;
    }
}