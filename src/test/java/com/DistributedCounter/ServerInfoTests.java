package com.DistributedCounter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.Common.ServerInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServerInfoTests {

	@Test
	public void EqualsTest_Positive()
	{
		String info = "Test";
		ServerInfo serverInfo = new ServerInfo(info);
		ServerInfo serverInfoToCheck = new ServerInfo(info);
		
		assertThat(serverInfo.equals(serverInfoToCheck)).isTrue();
	}
	
	@Test
	public void EqualsTest_Negative()
	{
		String info = "Test";
		String wrongInfo = "TestWrong";
		
		ServerInfo serverInfo = new ServerInfo(info);
		ServerInfo serverInfoToCheck = new ServerInfo(wrongInfo);
		
		assertThat(serverInfo.equals(serverInfoToCheck)).isFalse();
	}
	
	@Test
	public void EqualsTest_OtherClass()
	{
		String info = "Test";
		
		ServerInfo serverInfo = new ServerInfo(info);
		
		assertThat(serverInfo.equals(info)).isFalse();
	}
	
	@Test
	public void Getter()
	{
		String info = "Test";
		
		ServerInfo serverInfo = new ServerInfo(info);
		
		assertThat(serverInfo.getServerIpAndPort()).isEqualTo(info);
	}
	
	@Test
	public void ToString()
	{
		String info = "Test";
		
		ServerInfo serverInfo = new ServerInfo(info);
		
		assertThat(serverInfo.toString()).isEqualTo(info);
	}
	
	@Test
	public void OverloadedContructor()
	{
		String ip = "ip";
		String port = "port";
		
		ServerInfo serverInfo = new ServerInfo(ip, port);
		
		assertThat(serverInfo.getServerIpAndPort()).isEqualTo("http://ip:port");
	}
	
	@Test
	public void Contructor()
	{
		ServerInfo serverInfo = new ServerInfo();
		
		assertThat(serverInfo.getServerIpAndPort()).isEqualTo(null);
	}
}
