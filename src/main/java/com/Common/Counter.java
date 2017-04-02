package com.common;

public class Counter {
	private int incrementCounter;
	private int decrementCounter;
	
	public Counter() {
		this.incrementCounter = 0;
		this.decrementCounter = 0;
	}
	
	public Counter(int incrementCounter, int decrementCounter) {
		this.incrementCounter = incrementCounter;
		this.decrementCounter = decrementCounter;
	}
	
	public int getIncrementCounter() {
		return incrementCounter;
	}
	
	public void setIncrementCounter(int incrementCounter) {
		this.incrementCounter = incrementCounter;
	}
	
	public int getDecrementCounter() {
		return decrementCounter;
	}
	
	public void setDecrementCounter(int decrementCounter) {
		this.decrementCounter = decrementCounter;
	}
	
	public boolean UpdateDecrementCounter(int decrementCounter) {
		if(this.getDecrementCounter() < decrementCounter) {
			this.setDecrementCounter(decrementCounter);
			return true;
		}
		
		return false;
	}
	
	public boolean UpdateIncrementCounter(int incrementCounter) {
		if(this.getIncrementCounter() < incrementCounter) {
			this.setIncrementCounter(incrementCounter);
			return true;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return String.format("IncrementCounter: %d DecrementCount: %d", incrementCounter, decrementCounter);
	}
}
