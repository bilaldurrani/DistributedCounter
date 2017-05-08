package com.distributedcounter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.common.Counter;
import com.common.ServerInfo;
import com.interfaces.INodesManager;
import com.managers.NodesManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodesManagerTests {

	@Test
	public void UpdateCounter_IncrementIncrease()
	{
		INodesManager nodesManager = new NodesManager();
		Counter counter = new Counter(5, 10);
		ServerInfo info = new ServerInfo("TEST");
		
		assertThat(nodesManager.updateCounter(info, counter)).isTrue();
		counter = new Counter(50, 10);
		assertThat(nodesManager.updateCounter(info, counter)).isTrue();
		
		Collection<Counter> counters = nodesManager.getAllCounters();
		
		Counter counterCheck = counters.iterator().next();
		assertThat(counters.size()).isEqualTo(1);
		assertThat(counterCheck.getIncrementCounter()).isEqualTo(50);
		assertThat(nodesManager.getNodesToCounterMapping().keySet()).isEqualTo(nodesManager.getAllNodes());
	}
	
	@Test
	public void UpdateCounter_IncrementDecrease()
	{
		INodesManager nodesManager = new NodesManager();
		Counter counter = new Counter(5, 10);
		ServerInfo info = new ServerInfo("TEST");
		
		assertThat(nodesManager.updateCounter(info, counter)).isTrue();
		counter = new Counter(1, 10);
		assertThat(nodesManager.updateCounter(info, counter)).isFalse();
		
		Collection<Counter> counters = nodesManager.getAllCounters();
		
		Counter counterCheck = counters.iterator().next();
		assertThat(counters.size()).isEqualTo(1);
		assertThat(counterCheck.getIncrementCounter()).isEqualTo(5);
		assertThat(nodesManager.getNodesToCounterMapping().keySet()).isEqualTo(nodesManager.getAllNodes());
	}
	
}
