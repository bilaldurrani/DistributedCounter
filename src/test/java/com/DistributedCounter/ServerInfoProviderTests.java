package com.DistributedCounter;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.Common.ServerInfo;
import com.Interfaces.IServerInfoProvider;
import com.Managers.ServerInfoProvider;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ServerInfoProvider.class)
public class ServerInfoProviderTests {

    @Mock
    InetAddress inetAddress;
	
	@Test
	public void CheckServerInfo() throws UnknownHostException
	{
		String mockIp = "1.1.1.1";
		PowerMockito.mockStatic(InetAddress.class);
		PowerMockito.when(InetAddress.getLocalHost()).thenReturn(inetAddress);
		PowerMockito.when(inetAddress.getHostAddress()).thenReturn(mockIp);
		
		IServerInfoProvider provider = new ServerInfoProvider();
		ServerInfo serverInfo = provider.GetServerInfo();
		
		ServerInfo expected = new ServerInfo(mockIp, null);
		
		assertThat(serverInfo).isEqualTo(expected);
	}
}
