package com.distributedcounter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.common.Counter;
import com.common.NodesInfoResponse;
import com.common.ServerInfo;
import com.common.UpdateRequest;
import com.interfaces.IConnectionManager;
import com.interfaces.ICountManager;
import com.interfaces.IDistributionManager;
import com.interfaces.INodesManager;
import com.interfaces.IServerInfoProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DistributionManagerTests {

	@MockBean
	IServerInfoProvider ServerInfoProvider;
	
	@MockBean
	IConnectionManager connectionManager;
	
	@MockBean
	INodesManager nodesManager;
	
	@MockBean
	ICountManager countManager;
	
	@Autowired
	IDistributionManager distributionManager;
	
	String thisServerInfo = "1.1.1.1:8080";
	
	@Test
	public void PublishToAllNodes() throws UnknownHostException
	{
		ServerInfo info = new ServerInfo(thisServerInfo);
		ServerInfo remoteNode = new ServerInfo("RemoteNode1");
		Collection<ServerInfo> nodes = new ArrayList<ServerInfo>();
        nodes.add(remoteNode);
        
		Mockito.when(this.ServerInfoProvider.getServerInfo()).thenReturn(info);       
        Mockito.when(this.nodesManager.getAllNodes()).thenReturn(nodes);
		
		Counter counter = new Counter(5, 10);
		
		remoteNode.setServerIpAndPort("1");
		
		distributionManager.publishToAllNodes(counter);
		
		ArgumentCaptor<ServerInfo> savedRemoteNode = ArgumentCaptor.forClass(ServerInfo.class);
		ArgumentCaptor<UpdateRequest> savedUpdatereq = ArgumentCaptor.forClass(UpdateRequest.class);
		
		Mockito.verify(this.connectionManager).syncWithNode(
				savedRemoteNode.capture(),
				savedUpdatereq.capture());
		
		assertThat(savedRemoteNode.getValue().equals(remoteNode)).isTrue();
		assertThat(savedUpdatereq.getValue().getCounter()).isEqualTo(counter);
		assertThat(savedUpdatereq.getValue().getServerInfo()).isEqualTo(info);
	}
	
	@Test
	public void Register() throws UnknownHostException
	{
		// Setting mocks.
		ServerInfo currNodeInfo = new ServerInfo(thisServerInfo);
		ServerInfo remoteNodeInfo = new ServerInfo("RemoteNode1");
        Counter counter = new Counter(5, 10);
		
        Map<ServerInfo, Counter> mockNodesInfo = new HashMap<ServerInfo, Counter>();
        mockNodesInfo.put(remoteNodeInfo, new Counter(5,  10));
        
        NodesInfoResponse nodeInfoResponse = new NodesInfoResponse();
        nodeInfoResponse.setNodesInfo(mockNodesInfo);
        nodeInfoResponse.setOwnCounter(counter);
        
        Mockito.when(this.connectionManager.getNodeConnectionInfo(remoteNodeInfo)).thenReturn(remoteNodeInfo);
        Mockito.when(this.ServerInfoProvider.getServerInfo()).thenReturn(currNodeInfo); 
		Mockito.when(this.connectionManager.getNodesInfo(remoteNodeInfo)).thenReturn(nodeInfoResponse);     
		
		// Making call.
		distributionManager.register(remoteNodeInfo);
		
		//Verifications.
		Mockito.verify(this.nodesManager, Mockito.atLeast(2)).updateCounter(any(), any());
	}
	
	@Test
	public void Register_OwnCounterExists() throws UnknownHostException
	{
		// Setting mocks.
		ServerInfo currNodeInfo = new ServerInfo(thisServerInfo);
		ServerInfo remoteNodeInfo = new ServerInfo("RemoteNode1");
        Counter counter = new Counter(5, 10);
		
        Map<ServerInfo, Counter> mockNodesInfo = new HashMap<ServerInfo, Counter>();
        mockNodesInfo.put(currNodeInfo, new Counter(5,  10));
        
        NodesInfoResponse nodeInfoResponse = new NodesInfoResponse();
        nodeInfoResponse.setNodesInfo(mockNodesInfo);
        nodeInfoResponse.setOwnCounter(counter);
        
        Mockito.when(this.connectionManager.getNodeConnectionInfo(remoteNodeInfo)).thenReturn(remoteNodeInfo);
        Mockito.when(this.ServerInfoProvider.getServerInfo()).thenReturn(currNodeInfo); 
		Mockito.when(this.connectionManager.getNodesInfo(remoteNodeInfo)).thenReturn(nodeInfoResponse);     
		
		// Making call.
		distributionManager.register(remoteNodeInfo);
		
		//Verifications.
		Mockito.verify(this.nodesManager).updateCounter(any(), any());
		Mockito.verify(this.countManager).setCounter(any());
	}
}
