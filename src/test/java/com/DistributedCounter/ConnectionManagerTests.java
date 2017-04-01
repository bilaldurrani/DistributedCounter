package com.DistributedCounter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.any;

import com.Common.NodesInfoResponse;
import com.Common.ServerInfo;
import com.Common.UpdateRequest;
import com.Interfaces.IConnectionManager;
import com.Interfaces.IRouteProvider;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConnectionManagerTests {

	@MockBean
	private IRouteProvider routeProvider;
	
	@MockBean
	private RestTemplate restTemplate;
	
	@Autowired
	private IConnectionManager manager;
	
	@Test
	public void GetNodeConnectionInfo()
	{
		ServerInfo info = new ServerInfo("TEST");
		ResponseEntity<ServerInfo> response = new ResponseEntity<ServerInfo>(info, HttpStatus.OK);
		Mockito.when(this.routeProvider.GetNodeConnectionInfoUrl(any())).thenReturn("TESTURL");       
        Mockito.when(this.restTemplate.getForEntity("TESTURL", ServerInfo.class)).thenReturn(response);
        
        ServerInfo received = manager.GetNodeConnectionInfo(new ServerInfo("Request"));
        
        assertThat(received).isEqualTo(info);
	}
	
	@Test
	public void SyncWithNode()
	{
		UpdateRequest request = new UpdateRequest();
		Mockito.when(this.routeProvider.GetUpdateUrl(any())).thenReturn("TESTURL");       
        
        manager.SyncWithNode(new ServerInfo("Request"), request);
        
        Mockito.verify(this.restTemplate).put("TESTURL", request);
	}
	
	@Test
	public void SyncWithNode_Exception()
	{
		UpdateRequest request = new UpdateRequest();
		Mockito.when(this.routeProvider.GetUpdateUrl(any())).thenReturn("TESTURL");
		Mockito.doThrow(new RestClientException("TestException")).when(this.restTemplate).put(any(String.class), any());
        
        manager.SyncWithNode(new ServerInfo("Request"), request);
	}
	
	
	@Test
	public void GetNodesInfo()
	{
		NodesInfoResponse response = new NodesInfoResponse();
		Mockito.when(this.routeProvider.GetNodesInfoUrl(any())).thenReturn("TESTURL");
		Mockito.when(this.restTemplate.getForObject("TESTURL", NodesInfoResponse.class)).thenReturn(response);
        
		NodesInfoResponse received = manager.GetNodesInfo(new ServerInfo("Request"));
        
		assertThat(received).isEqualTo(response);
	}
	
	@Test
	public void GetNodesInfo_Exception()
	{
		Mockito.when(this.routeProvider.GetUpdateUrl(any())).thenReturn("TESTURL");
		Mockito.doThrow(RestClientException.class).when(this.restTemplate).getForObject(any(String.class), any());
        
		NodesInfoResponse received = manager.GetNodesInfo(new ServerInfo("Request"));
		assertThat(received).isEqualTo(null);
	}
}
