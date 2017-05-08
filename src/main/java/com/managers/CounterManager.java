package com.managers;

import org.springframework.stereotype.Service;

import com.common.Counter;
import com.interfaces.*;

@Service
public class CounterManager implements ICountManager {

	private Counter counter;

	public CounterManager()
	{
		counter = new Counter();
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public synchronized int increment()
	{
		int count = counter.getIncrementCounter();
		count++;
		
		counter.setIncrementCounter(count);
		
		return count;
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public synchronized int decrement()
	{
		int count = counter.getDecrementCounter();
		count++;
		
		counter.setDecrementCounter(count);
		
		return count;
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public int getCount()
	{
		int incrementCounter = counter.getIncrementCounter();
		int decrementCounter = counter.getDecrementCounter();
		
		return incrementCounter - decrementCounter;
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public Counter getCounter()
	{
		return counter;
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public void setCounter(Counter counter)
	{
		this.counter = counter;
	}
}
