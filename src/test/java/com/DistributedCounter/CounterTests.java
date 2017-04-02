package com.distributedcounter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.common.Counter;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CounterTests {
	
	
	@Test
	public void OverloadedConstuctor()
	{
		int incCounter = 5;
		int decCounter = 10;
		Counter counter = new Counter(incCounter,  decCounter);
		
		assertThat(counter.getIncrementCounter()).isEqualTo(incCounter);
		assertThat(counter.getDecrementCounter()).isEqualTo(decCounter);
	}
	
	@Test
	public void CounterCheckGetterSetter()
	{
		int incCounter = 5;
		int decCounter = 10;
		Counter counter = new Counter();
		counter.setIncrementCounter(incCounter);
		counter.setDecrementCounter(decCounter);
		
		assertThat(counter.getIncrementCounter()).isEqualTo(incCounter);
		assertThat(counter.getDecrementCounter()).isEqualTo(decCounter);
	}
	
	@Test
	public void CounterUpdateCounter_IncLarger()
	{
		Counter oldCounter = new Counter(5,10);
		
		assertThat(oldCounter.UpdateIncrementCounter(20)).isTrue();
	}
	
	@Test
	public void CounterUpdateCounter_IncSmaller()
	{
		Counter oldCounter = new Counter(5,10);
		
		assertThat(oldCounter.UpdateIncrementCounter(1)).isFalse();
	}
	
	@Test
	public void CounterUpdateCounter_DecLarger()
	{
		Counter oldCounter = new Counter(5,10);
		
		assertThat(oldCounter.UpdateDecrementCounter(20)).isTrue();
	}
	
	@Test
	public void CounterUpdateCounter_DecSmaller()
	{
		Counter oldCounter = new Counter(5,10);
		
		assertThat(oldCounter.UpdateDecrementCounter(1)).isFalse();
	}
	
	@Test
	public void CounterToString()
	{
		Counter oldCounter = new Counter(5,10);
		String expected = "IncrementCounter: 5 DecrementCount: 10";
		
		assertThat(oldCounter.toString()).isEqualTo(expected);
	}
}
