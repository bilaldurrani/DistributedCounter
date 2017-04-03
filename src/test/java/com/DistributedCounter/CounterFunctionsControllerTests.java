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
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
		this.mockMvc.perform(put("/increment")).andExpect(status().isOk());
		
		Mockito.verify(this.countManager).increment();
		Mockito.verify(this.distributionManager).publishToAllNodes(any());
    }
	
	@Test
    public void Decrement() throws Exception {
		this.mockMvc.perform(put("/decrement")).andExpect(status().isOk());
		
		Mockito.verify(this.countManager).decrement();
		Mockito.verify(this.distributionManager).publishToAllNodes(any());
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
		
		Mockito.when(this.nodesManager.getAllCounters()).thenReturn(counters);
		Mockito.when(this.countManager.getCount()).thenReturn(ownCount);
		
		this.mockMvc.perform(get("/counter"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(Integer.toString(expectedCount))));
	}
	
	@Test(expected = NestedServletException.class)
	public void Count_CheckIntOverFlow() throws Exception	{
		Collection<Counter> counters = new ArrayList<Counter>();
		
		counters.add(new Counter(Integer.MAX_VALUE, 1));
		counters.add(new Counter(Integer.MAX_VALUE, 2));
		
		int ownCount = 50;
		
		Mockito.when(this.nodesManager.getAllCounters()).thenReturn(counters);
		Mockito.when(this.countManager.getCount()).thenReturn(ownCount);
		
		try {
			this.mockMvc.perform(get("/counter"));
		}
		catch(NestedServletException e) {
			assertThat(e.getCause()).isInstanceOf(ArithmeticException.class);
			throw e;
		}
	}
}
