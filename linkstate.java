import java.awt.*;
import java.applet.*;
//<applet code="linkstate.class" height=1000 width=1000> </applet>
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
public class linkstate extends Applet{
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
			g.drawLine(nodes.get(e.n1-1).centerx,nodes.get(e.n1-1).centery,nodes.get(e.n2-1).centerx,nodes.get(e.n2-1).centery);
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
	int n1,n2,cost,status;
	Edge(int a, int b, int c){
		n1 = a;
		n2 = b;
		cost = c;
		status = 1;
	}
}