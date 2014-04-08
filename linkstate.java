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
	public void init(){
		try{
			nodes = new ArrayList<Node>();
			edges = new ArrayList<Edge>();
			packets = new ArrayList<Packet>();
			tasks = new ArrayList<Task>();
			File input = new File("input.txt");
			Scanner in = new Scanner(input);
			int n = in.nextInt();
			int e = in.nextInt();
			int t = in.nextInt();
			for(int i=1;i<=n;i++)
				nodes.add(new Node(in.nextInt(),in.nextInt()));
			for(int i=1;i<=e;i++)
				edges.add(new Edge(in.nextInt(),in.nextInt(),in.nextInt()));
			for(int i=1;i<=t;i++)
				tasks.add(new Task(in.nextInt(),in.nextInt(),in.nextLong()));
				
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
}
class Node{
	int centerx,centery;
	Node(int x, int y){
		centerx = x;
		centery = y;
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