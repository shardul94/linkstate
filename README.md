# Link State Routing Simulation

The program takes an input from a file and tries to simulate Link State Routing for the given scenario. The application is made in JAVA Applets.
To run the program enter these commands in your command prompt or terminal.
`javac LinkState.java
 appletviewer LinkState.java`

***

## Input Format

The input.txt file has the following format:
* Line 1 gives the number of nodes(n), number of edges(e) and number of tasks(t).
* The next n lines gives information about the x and y coordinates of the center of the nodes.
* The next e lines gives information about the starting node, ending node and cost of the edge.
* The next t lines gives information about the tasks.
  + The first value is the type of task viz. 0: Send a Packet, 1: Bring a link down, 2: Pull a link up.
  + The second and the third values give the nodes between which the packet is to be sent or the link which has to be brought up or down.
  + The fourth value allows us to specify the time at which the mentioned task should occur.

***

&copy; 2014 Shardul Mahadik