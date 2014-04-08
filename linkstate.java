import java.awt.*;
import java.applet.*;
//<applet code="linkstate.class" height=1000 width=1000> </applet>
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
public class linkstate extends Applet{
	public static ArrayList<Node> nodes;
	public void init(){
		try{
			nodes = new ArrayList<Node>();
			File input = new File("input.txt");
			Scanner in = new Scanner(input);
			int n = in.nextInt();
			int e = in.nextInt();
			for(int i=1;i<=n;i++)
				nodes.add(new Node(in.nextInt(),in.nextInt()));
				
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
			g.drawOval(n.xpos,n.ypos,40,40);
			g.drawLine(n.xpos+7,n.ypos+7,n.xpos+33,n.ypos+33);
			g.drawLine(n.xpos+7,n.ypos+33,n.xpos+33,n.ypos+7);
		}
		
	}
}
class Node{
	int xpos,ypos;
	Node(int x, int y){
		xpos = x;
		ypos = y;
	}
}
class Edge{
	int n1,n2,cost,status;
	Edge(int a, int b, int c, int s){
		n1 = a;
		n2 = b;
		cost = c;
		status = s;
	}
}