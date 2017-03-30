package com.DistributedCounter;

import java.net.UnknownHostException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.Common.ServerInfo;
import com.Interfaces.IDistributionManager;

/**
 * @author durrab01
 *
 */
/**
 * @author durrab01
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.DistributedCounter", "com.Interfaces", "com.Managers", "com.Common", "com.Controllers"} )// basePackageClasses = {TestController.class, CountManager.class})
public class DistributedCounterApplication {
	
	private static final Logger logger = LoggerFactory.getLogger("DistributedCounterApplication.Main");
	
	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = SpringApplication.run(DistributedCounterApplication.class, args);
		
		System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
        
        System.out.println(ctx.getEnvironment());
        
        if(args.length > 1)
        {
        	// Not checking that args[1] really is an IP. In worst case the Registration logic will do a throw and exception. 
        	RegisterWithNode(ctx, args[1]);
        }
	}
	
	/**
	 * Registers the current node with a remote node.
	 * @param ctx			ApplicationContext from Spring. Needed to resolve IDistributionManager.
	 * @param node			The remote node being registered with.
	 * @throws Exception	Throws an exception if the registration is not successful.
	 */
	private static void RegisterWithNode(ApplicationContext ctx, String node) throws Exception
	{
		IDistributionManager distributionManager = ctx.getBean(IDistributionManager.class);
		
    	try {
    		logger.info("Received arg to register with node: {}", node);
    		
    		ServerInfo serverInfo = new ServerInfo(node);
			distributionManager.Register(serverInfo);
		} catch (UnknownHostException e) {
			logger.error("Unable to Register with node {}. Please enter an up and running node.", node);
			throw e;
		}
	}
}
