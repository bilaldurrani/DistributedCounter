package com.interfaces;

import com.common.Counter;

public interface ICountManager {

	/**
	 * Increment the local increment counter.
	 * @return		Current increment counter value.
	 */
	public int Increment();
	
	
	/**
	 * Increment the local decrement counter.
	 * @return		Current decrement counter value.
	 */
	public int Decrement();
	
	/**
	 * Gets the current local count.
	 * @return		The current count.
	 */
	public int GetCount();
	
	/**
	 * Gets the counter object.
	 * @return		The counter object.
	 */
	public Counter GetCounter();
	
	/**
	 * Sets the counter object.
	 * @return		The counter object to set to.
	 */
	public void SetCounter(Counter counter);
}
