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
	public static int costs_copy[][];
	public static boolean visited[];
	public static int dist[];
	public static int pi[];
	Graphics bufferGraphics;
    Image offscreen;
    Dimension dim;
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
			costs_copy = new int[n+1][n+1];
			visited = new boolean[n+1];
			dist = new int[n+1];
			pi = new int[n+1];
			int e = in.nextInt();
			int t = in.nextInt();
			for(int i=1;i<=n;i++)
				nodes.add(new Node(in.nextInt(),in.nextInt(),n,i));
			for(int i=1;i<=e;i++)
				edges.add(new Edge(in.nextInt(),in.nextInt(),in.nextInt()));
			for(int i=1;i<=t;i++)
				tasks.add(new Task(in.nextInt(),in.nextInt(),in.nextInt(),in.nextLong()));
			calculateRoutingTables();
			for(int x=1;x<=n;x++){
				System.out.println("Node: "+x);
				for(int y=1;y<=n;y++)
					System.out.println(y+" "+nodes.get(x-1).route[y][0]+" "+nodes.get(x-1).route[y][1]);
			}
			dim = getSize();
          	offscreen = createImage(dim.width,dim.height);
          	bufferGraphics = offscreen.getGraphics(); 
			new Thread(){
				@Override
				public void run(){
					try{
						while(!done){
							repaint();
							Thread.sleep(40);
						}
					}catch(Exception e){
					}
				}
			}.start();
		}catch(Exception e){
		}
	}
	public void paint(Graphics g){
		bufferGraphics.clearRect(0,0,dim.width,dim.width); 
		for(Node n:nodes){
			bufferGraphics.drawOval(n.centerx-20,n.centery-20,40,40);
			bufferGraphics.drawLine(n.centerx-13,n.centery-13,n.centerx+13,n.centery+13);
			bufferGraphics.drawLine(n.centerx-13,n.centery+13,n.centerx+13,n.centery-13);
		}
		for(Edge e:edges){
			if(e.status==0) bufferGraphics.setColor(Color.RED);
			else bufferGraphics.setColor(Color.GREEN);
			bufferGraphics.drawLine(e.n1.centerx,e.n1.centery,e.n2.centerx,e.n2.centery);
			bufferGraphics.setColor(Color.BLACK);
			bufferGraphics.drawString(e.cost+"",e.n1.centerx+(e.n2.centerx-e.n1.centerx)/2+10,e.n1.centery+(e.n2.centery-e.n1.centery)/2+10);
		}
		ArrayList<Task> toSend = getTasks(counter);
		for(Task t:toSend){
			if(t.type==0){
				if(t.n1.route[t.n2.index][1]==t.n1.index){
					packets.add(new Packet(t.n1,t.n2));
				}else{
					int next = t.n1.route[t.n2.index][1];
					packets.add(new Packet(t.n1,nodes.get(next-1)));
					tasks.add(new Task(0,next,t.n2.index,counter+20));
				}
			}else if(t.type==1){
				down(t);
			}
			else if(t.type==2){
				up(t);
			}
		}
		for(Packet p:packets){
			if(p.update())
				bufferGraphics.fillOval((int)p.x,(int)p.y,10,10);
			else packets.remove(p);
		}
		g.drawImage(offscreen,0,0,this); 
		counter++;	
	}
	public void update(Graphics g){
          paint(g);
    } 
	public static void down(Task t){
		costs[t.n1.index][t.n2.index] = 32767;
		costs[t.n2.index][t.n1.index] = 32767;
		setStatus(t.n1,t.n2,0);
		calculateRoutingTables();
		for(int x=1;x<=nodes.size();x++){
			System.out.println("Node: "+x);
			for(int y=1;y<=nodes.size();y++)
				System.out.println(y+" "+nodes.get(x-1).route[y][0]+" "+nodes.get(x-1).route[y][1]);
		}
	}
	public static void up(Task t){
		costs[t.n1.index][t.n2.index] = costs_copy[t.n1.index][t.n2.index];
		costs[t.n2.index][t.n1.index] = costs_copy[t.n2.index][t.n1.index];
		setStatus(t.n1,t.n2,1);
		calculateRoutingTables();
		for(int x=1;x<=nodes.size();x++){
			System.out.println("Node: "+x);
			for(int y=1;y<=nodes.size();y++)
				System.out.println(y+" "+nodes.get(x-1).route[y][0]+" "+nodes.get(x-1).route[y][1]);
		}
	}
	public static int getStatus(Node n1,Node n2){
		for(Edge e:edges){
			if(e.n1==n1&&e.n2==n2||e.n2==n1&&e.n1==n2) return e.status;
		}
		return 0;
	}
	public static void setStatus(Node n1,Node n2,int status){
		for(Edge e:edges){
			if(e.n1==n1&&e.n2==n2||e.n2==n1&&e.n1==n2) e.status=status;
		}
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
					dist[i]=32767;
					pi[i]=0;
				}
				int c,current;
				current=x;
				visited[current]=true;
				dist[current]=0;
				while(current!=y){
					int dc = dist[current];
					for(int i=1;i<=nodes.size();i++){
						if(costs[current][i]!=0&&visited[i]!=true)
							if(costs[current][i]+dc<dist[i]){
								dist[i]=costs[current][i]+dc;
								pi[i]=current;
							}
					}
					
					int min=Integer.MAX_VALUE;
					for(int i=1;i<=nodes.size();i++){
						if(visited[i]!=true&&dist[i]<min){
							min=dist[i];
							current = i;
						}
					}
					visited[current] = true;
				}
				int temp=pi[y];
				if(pi[y]==x||pi[y]==0) nodes.get(x-1).route[y][1] = x;
				else while(temp!=x){
					nodes.get(x-1).route[y][1] = temp;
					temp = pi[temp];
				}
				System.out.println();
				nodes.get(x-1).route[y][0] = dist[y];
			}
		}
	}
}
class Node{
	int index,centerx,centery;
	int route[][];
	Node(int x, int y,int n,int i){
		centerx = x;
		centery = y;
		route = new int[n+1][3];
		index=i;
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
		LinkState.costs_copy[a][b] = c;
		LinkState.costs_copy[b][a] = c;
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
		incrx = (n2.centerx-n1.centerx)/20.0;
		incry = (n2.centery-n1.centery)/20.0;
	}
	boolean update(){
		x += incrx;
		y += incry;
		if(Math.abs(x-n2.centerx)-10<=incrx&&Math.abs(y-n2.centery)-10<=incry) return false;
		else return true;
	}
}
class Task{
	Node n1,n2;
	Long time;
	int type;
	Task(int type,int a,int b,long t){
		n1 = LinkState.nodes.get(a-1);
		n2 = LinkState.nodes.get(b-1);
		time = t;
		this.type=type;
	}
}