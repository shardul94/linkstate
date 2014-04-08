import java.awt.*;
import java.applet.*;
//<applet code="LinkState.class" height=1000 width=1000> </applet>
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
public class LinkState extends Applet{
	public static ArrayList<Node> nodes;
	public static ArrayList<Edge> edges;
	public void init(){
		try{
			nodes = new ArrayList<Node>();
			edges = new ArrayList<Edge>();
			File input = new File("input.txt");
			Scanner in = new Scanner(input);
			int n = in.nextInt();
			int e = in.nextInt();
			for(int i=1;i<=n;i++)
				nodes.add(new Node(in.nextInt(),in.nextInt()));
			for(int i=1;i<=e;i++)
				edges.add(new Edge(in.nextInt(),in.nextInt(),in.nextInt()));
				
			new Thread(){
				@Override
				public void run(){
					try{
						repaint();
						Thread.sleep(30);
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
			g.drawLine(e.n1.centerx,e.n1.centery,e.n2.centerx,e.n2.centery);
		}
		
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
	float x,y,incrx,incry;
	Packet(int a, int b){
		n1 = LinkState.nodes.get(a-1);
		n2 = LinkState.nodes.get(b-1);
		x = n1.centerx;
		y = n1.centery;
		incrx = (n2.centerx-n1.centerx)/30;
		incry = (n2.centery-n1.centery)/30;
	}
	boolean update(){
		x += incrx;
		y += incry;
		if(Math.abs(x-n1.centerx)<incrx&&Math.abs(y-n1.centery)<incry) return false;
		else return true;
	}
}