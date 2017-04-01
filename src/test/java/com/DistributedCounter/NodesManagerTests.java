package com.DistributedCounter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.Common.Counter;
import com.Common.ServerInfo;
import com.Interfaces.INodesManager;
import com.Managers.NodesManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodesManagerTests {

	@Test
	public void UpdateCounter_IncrementIncrease()
	{
		INodesManager nodesManager = new NodesManager();
		Counter counter = new Counter(5, 10);
		ServerInfo info = new ServerInfo("TEST");
		
		assertThat(nodesManager.UpdateCounter(info, counter)).isTrue();
		counter = new Counter(50, 10);
		assertThat(nodesManager.UpdateCounter(info, counter)).isTrue();
		
		Collection<Counter> counters = nodesManager.GetAllCounters();
		
		Counter counterCheck = counters.iterator().next();
		assertThat(counters.size()).isEqualTo(1);
		assertThat(counterCheck.getIncrementCounter()).isEqualTo(50);
		assertThat(nodesManager.GetNodesToCounterMapping().keySet()).isEqualTo(nodesManager.GetAllNodes());
	}
	
	@Test
	public void UpdateCounter_IncrementDecrease()
	{
		INodesManager nodesManager = new NodesManager();
		Counter counter = new Counter(5, 10);
		ServerInfo info = new ServerInfo("TEST");
		
		assertThat(nodesManager.UpdateCounter(info, counter)).isTrue();
		counter = new Counter(1, 10);
		assertThat(nodesManager.UpdateCounter(info, counter)).isFalse();
		
		Collection<Counter> counters = nodesManager.GetAllCounters();
		
		Counter counterCheck = counters.iterator().next();
		assertThat(counters.size()).isEqualTo(1);
		assertThat(counterCheck.getIncrementCounter()).isEqualTo(5);
		assertThat(nodesManager.GetNodesToCounterMapping().keySet()).isEqualTo(nodesManager.GetAllNodes());
	}
	
}
