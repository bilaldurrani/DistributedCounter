package com.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.common.NodesInfoResponse;
import com.common.ServerInfo;
import com.common.UpdateRequest;
import com.interfaces.IConnectionManager;
import com.interfaces.IRouteProvider;

@Service
public class ConnectionManager implements IConnectionManager{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private IRouteProvider routeProvider;
	
	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public ServerInfo GetNodeConnectionInfo(ServerInfo node)
	{
		String url = routeProvider.GetNodeConnectionInfoUrl(node);

		logger.info("Getting connection info using URL: {}", url);
		
		ResponseEntity<ServerInfo> response = restTemplate.getForEntity(url, ServerInfo.class);
		return response.getBody();
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public void SyncWithNode(ServerInfo node, UpdateRequest request) {
	
		try {
			String url = routeProvider.GetUpdateUrl(node);
			
			logger.info("Sending update/sync request using URL: {}", url);
			
			restTemplate.put(url, request);
		}
		catch(RestClientException e)
		{
			logger.error("Unable to update {}", node, e);
		}
	}

	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public NodesInfoResponse GetNodesInfo(ServerInfo node) {
		try {
			String url = routeProvider.GetNodesInfoUrl(node);
			
			logger.info("Getting NodesInfo using URL: {}", url);
			
			NodesInfoResponse response = restTemplate.getForObject(url, NodesInfoResponse.class);
			return response;
		}
		catch(RestClientException e)
		{
			logger.error("Unable to get node info from {}", node, e);
		}
		
		return null;
	}

}
