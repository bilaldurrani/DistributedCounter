package com.distributedcounter;

import org.springframework.beans.factory.annotation.Autowired;

import com.common.Counter;
import com.common.ServerInfo;
import com.controllers.DistributionController;
import com.interfaces.ICountManager;
import com.interfaces.INodesManager;
import com.interfaces.IServerInfoProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DistributionController.class)
public class DistributionControllerTests {

	@Autowired
    private MockMvc mockMvc;

    @MockBean
	IServerInfoProvider serverInfoProvider;
	
	@MockBean
	INodesManager nodesManager;
	
	@MockBean
	ICountManager countManager;
	
	@Test
    public void GetNodesInfo() throws Exception {
		ServerInfo info = new ServerInfo("TEST", "8080");
		Counter counter = new Counter(5,  10);
		Map<ServerInfo, Counter> map = new HashMap<ServerInfo, Counter>();
		map.put(info, counter);
		
		Mockito.when(this.nodesManager.getNodesToCounterMapping()).thenReturn(map);
		Mockito.when(this.countManager.getCounter()).thenReturn(counter);
		
		this.mockMvc.perform(get("/nodesinfo"))
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("{\"ownCounter\":{\"incrementCounter\":5,\"decrementCounter\":10},\"nodesInfo\":{\"http://TEST:8080\":{\"incrementCounter\":5,\"decrementCounter\":10}}}")));
    }
	
	@Test
    public void GetNodeConnectionInfo() throws Exception {
		ServerInfo info = new ServerInfo("TEST", "8080");
		
		Mockito.when(this.serverInfoProvider.getServerInfo()).thenReturn(info);
		
		this.mockMvc.perform(get("/nodeconnectioninfo"))
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("{\"serverIpAndPort\":\"http://TEST:8080\"}")));
    }
	
	@Test
    public void GetNodeConnectionInfo_ThrowException() throws Exception {
		Mockito.when(this.serverInfoProvider.getServerInfo()).thenThrow(new UnknownHostException());
		
		this.mockMvc.perform(get("/nodeconnectioninfo"))
		.andExpect(status().isNoContent());
    }
	
	@Test
    public void UpdateRequestCreated() throws Exception {
		
		String request = "{  \"serverInfo\": {    \"serverIpAndPort\": \"TEST\"  },  \"counter\": {    \"incrementCounter\": 0,    \"decrementCounter\": 0  }}";
		
		Mockito.when(this.nodesManager.updateCounter(any(), any())).thenReturn(true);
		
		this.mockMvc.perform(put("/update")
				.content(request)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated());
    }
	
	@Test
    public void UpdateRequestFailed() throws Exception {
		
		String request = "{  \"serverInfo\": {    \"serverIpAndPort\": \"TEST\"  },  \"counter\": {    \"incrementCounter\": 0,    \"decrementCounter\": 0  }}";
		
		Mockito.when(this.nodesManager.updateCounter(any(), any())).thenReturn(false);
		
		this.mockMvc.perform(put("/update")
				.content(request)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotModified());
    }
}
