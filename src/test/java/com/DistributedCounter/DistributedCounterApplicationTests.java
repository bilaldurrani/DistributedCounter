package com.distributedcounter;

import org.springframework.context.ApplicationContext;

import static org.mockito.Matchers.any;

import java.net.UnknownHostException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.common.ServerInfo;
import com.distributedcounter.DistributedCounterApplication;
import com.interfaces.IDistributionManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DistributedCounterApplicationTests {

	@Mock 
	ApplicationContext ctx;
	
	@Mock 
	IDistributionManager manager;
	
	@Test
	public void contextLoads() throws Exception {
		String[] args = new String[1];
		args[0] = "TestArg0";
		
		DistributedCounterApplication.main(args);
	}
	
	@Test
	public void Registeration() throws Exception {
		Mockito.when(ctx.getBean(IDistributionManager.class)).thenReturn(manager);
		
		DistributedCounterApplication.registerWithNode(ctx, "TEST");
		
		Mockito.verify(manager).register(new ServerInfo("TEST"));
	}
	
	@Test(expected = UnknownHostException.class)
	public void RegisterationException() throws Exception {
		Mockito.when(ctx.getBean(IDistributionManager.class)).thenReturn(manager);
		Mockito.doThrow(UnknownHostException.class).when(this.manager).register(any());
		
		DistributedCounterApplication.registerWithNode(ctx, "TEST");
	}
}
