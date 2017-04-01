package com.Controllers;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.Common.NodesInfoResponse;
import com.Common.ServerInfo;
import com.Common.UpdateRequest;
import com.Interfaces.ICountManager;
import com.Interfaces.INodesManager;
import com.Interfaces.IServerInfoProvider;

@RestController
public class DistributionController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	IServerInfoProvider serverInfoProvider;
	
	@Autowired
	INodesManager nodesManager;
	
	@Autowired
	ICountManager countManager;
	
	/**
	 * Gets all the nodes info that this node has.
	 * @return		NodeInfoResponse which encapsulates all the information about all the nodes in the system.
	 * 				This response has their IPs and their Counter states.
	 */
	@RequestMapping(value = "/nodesinfo", method = RequestMethod.GET)
	NodesInfoResponse GetNodesInfo()
	{
		NodesInfoResponse response = new NodesInfoResponse();
		response.setNodesInfo(nodesManager.GetNodesToCounterMapping());
		response.setOwnCounter(countManager.GetCounter());
		
		return response;
	}
	
	/**
	 * Gets the resolved IP:PORT of this node. This is used by other nodes at registration time to verify the node being registered to exists.
	 * Also this is used to get the IP:PORT in a qualified way which this node will understand as well.
	 * eg: http://127.0.0.1:8080 is also the same as http://NETWORKADDRESS:8080. Using this call, the registration node will be in sync with this node on how to call it. 
	 * @return 		The node info is format http://IP:PORT
	 */
	@RequestMapping(value = "/nodeconnectioninfo", method = RequestMethod.GET)
	ResponseEntity<ServerInfo> GetNodeConnectionInfo(HttpServletResponse response) {
		try {
			ServerInfo info = serverInfoProvider.GetServerInfo();
			logger.info("Current server info: {}", info);
			
			return ResponseEntity.ok(info);
		} catch (Exception e) {
			logger.error("Failed to get connection info: ", e);
			
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
	}
    
	/**
	 * Updates the calling node's counter states with the local state of that counter.
	 * @param request	Request from the remote node detailing the node's counter states.
	 * @return			HTTP status to tell whether the state was updated or not (Not being used right now).
	 */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    void Update(@RequestBody UpdateRequest request, HttpServletResponse response) {
    	logger.info(String.format("Received update Request: %s", request));
        boolean isUpdated = nodesManager.UpdateCounter(request.getServerInfo(), request.getCounter());
        
        if(isUpdated) {
        	response.setStatus(HttpServletResponse.SC_CREATED);
        }
        else {
        	// TODO: Check whether this is correct or not.
        	response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        }
    }    
}