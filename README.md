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
- **GET /counter** : Each node has all the counter information of all the nodes. Running this request will do: Total Count = (Sum all Incrment Counters) - (Sum all Iecrement Counters).

**NOTES:**
  - If increment of decrement cannot publish to all nodes, or some connection fails, then there is no retry. The system is AP and will eventually be consisten when another increment/decrement is done as it will cause another publish to happen. This can be fixed by
    1. Having an internal queue of failed network commands and have a scheduled tasks which retries all the failed commands.
    2. Add a new endpoint /Sync. And have a cron job which calls /Sync every X amount of time. This will remove the need to have thread    handling within the service. (Cassandra also has a Update command which needs to be called routinely to keep all the nodes in sync).

**Commands for node-to-node communication**

- **GET /nodeconnectioninfo**   : Gets the node's connections info (IP:PORT).
- **GET /nodesinfo**            : Gets all the registered nodes and their counters saved in the current node. 
- **PUT /update**               : This is the receive states updates (Counter information) from other nodes. 

# Limitations:

1. The max counter is an Int, so can be -2,147,483,648 to 2,147,483, 647. This can be fixed by using big number. But that would increase the serialization/deserialization complexity.

2. There is no file storage being done; but data persistance will remain as long as there is at least 1 node still up. eg: If node a and node b are working and node a goes down, then node b will still work and not lose any info. As soon as node a comes back online and registers with node b; node b will return to it the last counter states it had of node a and node a will initialize using those states.

3. Registration needs to be done of a node one at a time (cannot start multiple nodes at the same time).If node C is online and A & B simultaneuously try to register to C, then the node whose registeration starts first with C will not get the info about the other node (as C might be still processing the other node's info).
This can be fixed by changing the "/update" endpoint to return back nodes information. EG: if registration is partial and A didn't get B's info. On the next /increment operation to A, A will do /update to C (not to B as it doesn't have B's info). In response C will return all it's node's info, which will put A in sync and A will then have all the cluster's info.

# Known issues:

2. If a node goes down, then the remaining nodes will still try to keep sending it their state information. This will decrease performance as a Timout has to happen. This can be fixed using a circtuit breaker and stop sending updates to the offline node. 

3. If all node's counters count to be larged then int.max, then an overflow exception will be thrown. Can possible use big integer just for calculating the count.

**Test Coverage: 97.6%** (But some tests don't verify 100% each property used)
