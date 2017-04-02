# DistributedCounter
Distributed Counter implementation using CRDT (https://en.wikipedia.org/wiki/Conflict-free_replicated_data_type#State-based_CRDTs)

# Startup
Build: `mvn install`

Start 1 node on 8080:
`java -jar .\target\DistributedCounter-0.0.1-SNAPSHOT.jar --server.port=8080`

Start 2nd node on 8081 and register with cluster using 8080
`java -jar .\target\DistributedCounter-0.0.1-SNAPSHOT.jar --server.port=8081 http://localhost:8080`

Start 3rd node on 8082 and register with cluster using 8080 or 8081
`java -jar .\target\DistributedCounter-0.0.1-SNAPSHOT.jar --server.port=8082 http://localhost:8081`

# Supported Commands

**Commands for consumers to use**

- **POST /increment** : Increments the local increment counter and publishes the states to all connected nodes
- **POST /decrement** : Increments the local decrement counter and publishes the states to all connected nodes

**NOTES:**
1. If increment of decrement cannot publish to all nodes, or some connection fails, then there is no retry. The system is AP and will eventually be consisten when another increment/decrement is done as it will cause another publish to happen. This can be fixed by
 1. Having an internal queue of failed network commands and have a scheduled tasks which retries all the failed commands.
 2. Add a new endpoint /Sync. And have a cron job which calls /Sync every X amount of time. This will remove the need to have thread    handling within the service. (Cassandra also has a Update command which needs to be called routinely to keep all the nodes in sync).

- **GET /counter** : Each node has all the counter information of all the nodes. Running this request will do: Total Count = (Sum all Incrment Counters) - (Sum all Iecrement Counters).



**Commands for node-to-node communication**

- **GET /nodeconnectioninfo**   : Gets the node's connections info (IP:PORT).
- **GET /nodesinfo**            : Gets all the registered nodes and their counters saved in the current node. 
- **PUT /update**               : This is the receive states updates (Counter information) from other nodes. 

# Known issues:
1. If node a & b are registered to node c at the same time. Then node a & b might not have info about each other. This can be fixed by changing the "Update" endpoint to return back nodes information. This will make the system self heal and eventually consistant.

2. If a node goes down, then the remaining nodes will still try to keep sending it their state information. This can be fixed using a circtuit breaker.

3.
