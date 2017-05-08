package com.interfaces;

import com.common.Counter;

public interface ICountManager {

	/**
	 * Increment the local increment counter.
	 * @return		Current increment counter value.
	 */
	public int increment();
	
	
	/**
	 * Increment the local decrement counter.
	 * @return		Current decrement counter value.
	 */
	public int decrement();
	
	/**
	 * Gets the current local count.
	 * @return		The current count.
	 */
	public int getCount();
	
	/**
	 * Gets the counter object.
	 * @return		The counter object.
	 */
	public Counter getCounter();
	
	/**
	 * Sets the counter object.
	 * @return		The counter object to set to.
	 */
	public void setCounter(Counter counter);
}
