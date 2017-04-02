package com.distributedcounter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.common.ServerInfo;
import com.interfaces.IRouteProvider;
import com.managers.RouteProvider;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RouteProviderTests {
	
	@Test
	public void CheckGets()
	{
		String uri = "http://1.1.1.1:8080";
		ServerInfo serverInfo = new ServerInfo(uri);
		IRouteProvider provider = new RouteProvider();
		
		assertThat(provider.GetNodeConnectionInfoUrl(serverInfo)).isEqualTo(String.format("%s/%s", "http://1.1.1.1:8080", "nodeconnectioninfo"));
		assertThat(provider.GetNodesInfoUrl(serverInfo)).isEqualTo(String.format("%s/%s", "http://1.1.1.1:8080", "nodesinfo"));
		assertThat(provider.GetUpdateUrl(serverInfo)).isEqualTo(String.format("%s/%s", "http://1.1.1.1:8080", "update"));
	}
}
