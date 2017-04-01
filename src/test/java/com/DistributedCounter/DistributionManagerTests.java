package com.DistributedCounter;

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

import com.Common.Counter;
import com.Common.NodesInfoResponse;
import com.Common.ServerInfo;
import com.Common.UpdateRequest;
import com.Interfaces.IConnectionManager;
import com.Interfaces.ICountManager;
import com.Interfaces.IDistributionManager;
import com.Interfaces.INodesManager;
import com.Interfaces.IServerInfoProvider;
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
        
		Mockito.when(this.ServerInfoProvider.GetServerInfo()).thenReturn(info);       
        Mockito.when(this.nodesManager.GetAllNodes()).thenReturn(nodes);
		
		Counter counter = new Counter(5, 10);
		
		remoteNode.setServerIpAndPort("1");
		
		distributionManager.PublishToAllNodes(counter);
		
		ArgumentCaptor<ServerInfo> savedRemoteNode = ArgumentCaptor.forClass(ServerInfo.class);
		ArgumentCaptor<UpdateRequest> savedUpdatereq = ArgumentCaptor.forClass(UpdateRequest.class);
		
		Mockito.verify(this.connectionManager).SyncWithNode(
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
        
        Mockito.when(this.connectionManager.GetNodeConnectionInfo(remoteNodeInfo)).thenReturn(remoteNodeInfo);
        Mockito.when(this.ServerInfoProvider.GetServerInfo()).thenReturn(currNodeInfo); 
		Mockito.when(this.connectionManager.GetNodesInfo(remoteNodeInfo)).thenReturn(nodeInfoResponse);     
		
		// Making call.
		distributionManager.Register(remoteNodeInfo);
		
		//Verifications.
		Mockito.verify(this.nodesManager, Mockito.atLeast(2)).UpdateCounter(any(), any());
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
        
        Mockito.when(this.connectionManager.GetNodeConnectionInfo(remoteNodeInfo)).thenReturn(remoteNodeInfo);
        Mockito.when(this.ServerInfoProvider.GetServerInfo()).thenReturn(currNodeInfo); 
		Mockito.when(this.connectionManager.GetNodesInfo(remoteNodeInfo)).thenReturn(nodeInfoResponse);     
		
		// Making call.
		distributionManager.Register(remoteNodeInfo);
		
		//Verifications.
		Mockito.verify(this.nodesManager).UpdateCounter(any(), any());
		Mockito.verify(this.countManager).SetCounter(any());
	}
}
