package com.DistributedCounter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.Common.Counter;
import com.Common.NodesInfoResponse;
import com.Common.ServerInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodesInfoResponseTests {

	@Test
	public void NodesInfoResponse_GetterSetters()
	{
		NodesInfoResponse response = new NodesInfoResponse();
		ServerInfo info = new ServerInfo("TEST", "8080");
		Counter counter = new Counter(5,  10);
		Counter ownCounter = new Counter(20,  30);
		
		response.setOwnCounter(new Counter(5,10));
		
		Map<ServerInfo, Counter> map = new HashMap<ServerInfo, Counter>();
		map.put(info, counter);
		
		// Act.
		response.setNodesInfo(map);
		response.setOwnCounter(ownCounter);

		// Verify.
		Map<ServerInfo, Counter> getNodesInfo = response.getNodesInfo();
		Counter getOwnCounter = response.getOwnCounter();
		
		assertThat(getNodesInfo.get(info)).isEqualTo(counter);
		assertThat(getOwnCounter).isEqualTo(ownCounter);
	}
}
