# DistributedCounter
Distributed Counter implementation using CRDT

mvn install

Start an instance on port 8080:
java -jar .\target\DistributedCounter-0.0.1-SNAPSHOT.jar --server.port=8080

Join a new node on port 8081 to the cluster - Register to cluster using node http://localhost:8080
java -jar .\target\DistributedCounter-0.0.1-SNAPSHOT.jar --server.port=8081 http://localhost:8080

Join another node. Can use any running note to join the cluster. Using http://localhost:8081 for registering.
java -jar .\target\DistributedCounter-0.0.1-SNAPSHOT.jar --server.port=8082 http://localhost:8081

Known issues:
1. If node a & b are registered to node c at the same time. Then node a & b might not have info about each other. This can be fixed by changing the "Update" endpoint to return back nodes information. This will make the system self heal and eventually consistant.
