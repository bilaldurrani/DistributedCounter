package com.DistributedCounter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.Common.Counter;
import com.Common.ServerInfo;
import com.Common.UpdateRequest;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateRequestTests {

	@Test
	public void Getters()
	{
		Counter counter = new Counter(5, 10);
		ServerInfo serverInfo = new ServerInfo("Test");
		
		UpdateRequest request = new UpdateRequest(serverInfo, counter);
		
		assertThat(request.getCounter()).isEqualTo(counter);
		assertThat(request.getServerInfo()).isEqualTo(serverInfo);
	}
	
	@Test
	public void ToString()
	{
		Counter counter = new Counter(5, 10);
		ServerInfo serverInfo = new ServerInfo("Test");
		
		UpdateRequest request = new UpdateRequest(serverInfo, counter);
		String expected = String.format("ServerAddress:%s, Counter:%s", serverInfo.toString(), counter.toString());
		
		assertThat(request.toString()).isEqualTo(expected);
	}
}
