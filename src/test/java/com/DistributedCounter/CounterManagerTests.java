package com.distributedcounter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

import com.common.Counter;
import com.managers.CounterManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CounterManagerTests {

	@Test
	public void Increment()
	{
		CounterManager manager = new CounterManager();
		
		manager.increment();
		Counter counter = manager.getCounter();
		
		assertThat(counter.getIncrementCounter()).isEqualTo(1);
		assertThat(counter.getDecrementCounter()).isEqualTo(0);
	}
	
	@Test
	public void Decrement()
	{
		CounterManager manager = new CounterManager();
		
		manager.decrement();
		Counter counter = manager.getCounter();
		
		assertThat(counter.getIncrementCounter()).isEqualTo(0);
		assertThat(counter.getDecrementCounter()).isEqualTo(1);
	}
	
	@Test
	public void Count()
	{
		CounterManager manager = new CounterManager();
		
		manager.increment();
		manager.increment();
		manager.increment();
		
		manager.decrement();
		
		assertThat(manager.getCount()).isEqualTo(2);
	}
	
	@Test
	public void SetCounter()
	{
		CounterManager manager = new CounterManager();

		manager.setCounter(new Counter(5, 10));
		
		Counter counter = manager.getCounter();
		
		assertThat(counter.getIncrementCounter()).isEqualTo(5);
		assertThat(counter.getDecrementCounter()).isEqualTo(10);
	}
}


