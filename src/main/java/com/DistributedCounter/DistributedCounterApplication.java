package com.distributedcounter;

import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.common.ServerInfo;
import com.interfaces.IDistributionManager;

/**
 * @author durrab01
 *
 */
/**
 * @author durrab01
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = { "com.distributedCounter", "com.interfaces", "com.managers", "com.common", "com.controllers" }) 
public class DistributedCounterApplication {

	private static final Logger logger = LoggerFactory.getLogger("DistributedCounterApplication.Main");

	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = SpringApplication.run(DistributedCounterApplication.class, args);

		if (args.length > 1) {
			// Not checking that args[1] really is an IP. In worst case the
			// Registration logic will do a throw and exception.
			RegisterWithNode(ctx, args[1]);
		}
	}

	/**
	 * Registers the current node with a remote node. TODO: This should be done
	 * in a better way (Running when application started event is fired).
	 * 
	 * @param ctx
	 *            ApplicationContext from Spring. Needed to resolve
	 *            IDistributionManager.
	 * @param node
	 *            The remote node being registered with.
	 * @throws Exception
	 *             Throws an exception if the registration is not successful.
	 */
	public static void RegisterWithNode(ApplicationContext ctx, String node) throws UnknownHostException {
		IDistributionManager distributionManager = ctx.getBean(IDistributionManager.class);

		try {
			logger.info("Received arg to register with node: {}", node);

			ServerInfo serverInfo = new ServerInfo(node);
			distributionManager.register(serverInfo);
		} catch (UnknownHostException e) {
			logger.error("Unable to Register with node {}. Please enter an up and running node.", node);
			throw e;
		}
	}
}
