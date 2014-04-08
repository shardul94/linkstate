import java.awt.*;
import java.applet.*;
//<applet code="LinkState.class" height=1000 width=1000> </applet>
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
public class LinkState extends Applet{
	public static ArrayList<Node> nodes;
	public static ArrayList<Edge> edges;
	public static ArrayList<Packet> packets;
	public static ArrayList<Task> tasks;
	public static boolean done = false;
	public static long counter = 0;
	public static int costs[][];
	public static boolean visited[];
	public static int dist[];
	public void init(){
		try{
			nodes = new ArrayList<Node>();
			edges = new ArrayList<Edge>();
			packets = new ArrayList<Packet>();
			tasks = new ArrayList<Task>();
			File input = new File("input.txt");
			Scanner in = new Scanner(input);
			int n = in.nextInt();
			costs = new int[n+1][n+1];
			visited = new boolean[n+1];
			dist = new int[n+1];
			int e = in.nextInt();
			int t = in.nextInt();
			for(int i=1;i<=n;i++)
				nodes.add(new Node(in.nextInt(),in.nextInt(),n));
			for(int i=1;i<=e;i++)
				edges.add(new Edge(in.nextInt(),in.nextInt(),in.nextInt()));
			for(int i=1;i<=t;i++)
				tasks.add(new Task(in.nextInt(),in.nextInt(),in.nextLong()));
			calculateRoutingTables();
			new Thread(){
				@Override
				public void run(){
					try{
						while(!done){
							repaint();
							Thread.sleep(40);
						}
					}catch(Exception e){
						System.out.println(e);
					}
				}
			}.start();
		}catch(Exception e){
			System.out.println(e);
		}
	}
	public void paint(Graphics g){
		for(Node n:nodes){
			g.drawOval(n.centerx-20,n.centery-20,40,40);
			g.drawLine(n.centerx-13,n.centery-13,n.centerx+13,n.centery+13);
			g.drawLine(n.centerx-13,n.centery+13,n.centerx+13,n.centery-13);
		}
		for(Edge e:edges){
			if(e.status==0) g.setColor(Color.RED);
			g.drawLine(e.n1.centerx,e.n1.centery,e.n2.centerx,e.n2.centery);
			g.setColor(Color.BLACK);
		}
		ArrayList<Task> toSend = getTasks(counter);
		for(Task t:toSend){
			packets.add(new Packet(t.n1,t.n2));
		}
		for(Packet p:packets){
			if(p.update())
				g.fillOval((int)p.x,(int)p.y,10,10);
			else packets.remove(p);
		}
		counter++;	
	}
	public static ArrayList<Task> getTasks(long time){
		ArrayList<Task> l = new ArrayList<Task>();
		for(Task t:tasks){
			if(t.time==time) l.add(t);
		}
		return l;
	}
	public static void calculateRoutingTables(){
		for(int x=1;x<=nodes.size();x++){
			for(int y=1;y<=nodes.size();y++){
				for(int i=0;i<=nodes.size();i++){
					visited[i]=false;
					dist[i]=Integer.MAX_VALUE;
				}
				int c,current;
				current=x;
				visited[current]=true;
				dist[current]=0;
				int step=0;
				while(current!=y){
					int dc = dist[current];
					for(int i=1;i<=nodes.size();i++){
						if(costs[current][i]!=0&&visited[i]!=true)
							if(costs[current][i]+dc<dist[i]){
								dist[i]=costs[current][i]+dc;
							}
					}
					
					int min=Integer.MAX_VALUE;
					for(int i=1;i<=nodes.size();i++){
						if(visited[i]!=true&&dist[i]<min){
							min=dist[i];
							current = i;
						}
					}
					if(step++==0) nodes.get(x-1).route[y][1] = current;
					visited[current] = true;
				}
				nodes.get(x-1).route[y][0] = dist[y];
			}
		}
	}
}
class Node{
	int centerx,centery;
	int route[][];
	Node(int x, int y,int n){
		centerx = x;
		centery = y;
		route = new int[n+1][3];
	}
}
class Edge{
	Node n1,n2;
	int cost,status;
	Edge(int a, int b, int c){
		n1 = LinkState.nodes.get(a-1);
		n2 = LinkState.nodes.get(b-1);
		cost = c;
		status = 1;
		LinkState.costs[a][b] = c;
		LinkState.costs[b][a] = c;
		
	}
}
class Packet{
	Node n1,n2;
	double x,y,incrx,incry;
	Packet(Node a, Node b){
		n1 = a;
		n2 = b;
		x = n1.centerx;
		y = n1.centery;
		incrx = (n2.centerx-n1.centerx)/30.0;
		incry = (n2.centery-n1.centery)/30.0;
	}
	boolean update(){
		x += incrx;
		y += incry;
		if(Math.abs(x-n2.centerx)-5<=incrx&&Math.abs(y-n2.centery)-5<=incry) return false;
		else return true;
	}
}
class Task{
	Node n1,n2;
	Long time;
	Task(int a,int b,long t){
		n1 = LinkState.nodes.get(a-1);
		n2 = LinkState.nodes.get(b-1);
		time = t;
	}
}