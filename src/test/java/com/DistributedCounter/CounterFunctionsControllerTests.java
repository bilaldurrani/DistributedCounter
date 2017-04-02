package com.distributedcounter;

import org.springframework.beans.factory.annotation.Autowired;

import com.common.Counter;
import com.controllers.CounterFunctionsController;
import com.interfaces.ICountManager;
import com.interfaces.IDistributionManager;
import com.interfaces.INodesManager;
import com.interfaces.IServerInfoProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CounterFunctionsController.class)
public class CounterFunctionsControllerTests {

	@Autowired
    private MockMvc mockMvc;

    @MockBean
	IServerInfoProvider serverInfoProvider;
	
	@MockBean
	INodesManager nodesManager;
	
	@MockBean
	ICountManager countManager;
	
	@MockBean
	IDistributionManager distributionManager;
	
	@Test
    public void Increment() throws Exception {
		this.mockMvc.perform(post("/increment")).andExpect(status().isOk());
		
		Mockito.verify(this.countManager).Increment();
		Mockito.verify(this.distributionManager).PublishToAllNodes(any());
    }
	
	@Test
    public void Decrement() throws Exception {
		this.mockMvc.perform(post("/decrement")).andExpect(status().isOk());
		
		Mockito.verify(this.countManager).Decrement();
		Mockito.verify(this.distributionManager).PublishToAllNodes(any());
    }
	
	@Test
	public void Count() throws Exception	{
		Collection<Counter> counters = new ArrayList<Counter>();
		
		counters.add(new Counter(10, 1));
		counters.add(new Counter(20, 2));
		counters.add(new Counter(30, 3));
		counters.add(new Counter(40, 4));
		
		int ownCount = 50;
		int expectedCount = ownCount + (10 + 20 + 30 + 40) - (1 + 2 + 3 + 4);
		
		Mockito.when(this.nodesManager.GetAllCounters()).thenReturn(counters);
		Mockito.when(this.countManager.GetCount()).thenReturn(ownCount);
		
		this.mockMvc.perform(get("/counter"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(Integer.toString(expectedCount))));
	}
}
