package com.Managers;

import org.springframework.stereotype.Service;

import com.Common.Counter;
import com.Interfaces.*;

@Service
public class CounterManager implements ICountManager {

	private Counter counter;

	CounterManager()
	{
		counter = new Counter();
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public synchronized int Increment()
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
	public synchronized int Decrement()
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
	public int GetCount()
	{
		int incrementCounter = counter.getIncrementCounter();
		int decrementCounter = counter.getDecrementCounter();
		
		return incrementCounter - decrementCounter;
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public Counter GetCounter()
	{
		return counter;
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public void SetCounter(Counter counter)
	{
		this.counter = counter;
	}
}
