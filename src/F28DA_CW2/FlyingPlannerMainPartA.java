package F28DA_CW2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.SingleSourcePaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleGraph;

public class FlyingPlannerMainPartA {

	
	public static void printOut(List<String> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			System.out.println((i+1) + ". " + list.get(i) + " -> " + list.get(i+1));
		}
	}
	
	public static void main(String[] args) {
		
		// The following code is from HelloJGraphT.java of the org.jgrapth.demo package
		
		System.err.println("Note: Airports are edinburgh, heathrow, sydney, dubai and kuala lumpur");
		// Code is from HelloJGraphT.java of the org.jgrapth.demo package (start)
        //Graph<String, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
        Graph<String, DefaultWeightedEdge> g = new DirectedWeightedMultigraph<>(DefaultWeightedEdge.class);

        String ed = "edinburgh";
        String ht = "heathrow";
        String sy = "sydney";
        String di = "dubai";
        String kl = "kuala lumpur";

        // add the vertices
        g.addVertex(ed);
        g.addVertex(ht);
        g.addVertex(sy);
        g.addVertex(di);
        g.addVertex(kl);

        // add edges to create a circuit
        
        g.setEdgeWeight((g.addEdge(ed, ht)), 80);

        g.setEdgeWeight(g.addEdge(ht, ed), 80);
        g.setEdgeWeight(g.addEdge(ht, di), 130);
        g.setEdgeWeight(g.addEdge(di, ht), 130);
        g.setEdgeWeight(g.addEdge(ht, sy), 570);
        g.setEdgeWeight(g.addEdge(sy, ht), 570);
        g.setEdgeWeight(g.addEdge(ed, di), 150);
        g.setEdgeWeight(g.addEdge(di, ed), 150);
        g.setEdgeWeight(g.addEdge(di, kl), 170);
        g.setEdgeWeight(g.addEdge(kl, di), 170);
        g.setEdgeWeight(g.addEdge(kl, sy), 150);
        g.setEdgeWeight(g.addEdge(sy, kl), 150);
        
        DijkstraShortestPath<String, DefaultWeightedEdge> d1 = new DijkstraShortestPath<String, DefaultWeightedEdge>(g);
        
        Scanner scan = new Scanner(System.in);
        
        System.out.println("Please enter the depature airport");
        String destination = scan.nextLine().toLowerCase();  // Read user input
        
        System.out.println("Please enter the destination airport");
        String depature = scan.nextLine().toLowerCase();  // Read user input
        
        System.out.println("Cheapest path: ");
        
        //System.out.println(d1.getPath(destination, departure).getVertexList());
        printOut(d1.getPath(destination, depature).getVertexList());
        System.out.println("Number of plane of changes: " + (d1.getPath(destination, depature).getLength()-1));
        System.out.println("Cost = £" + d1.getPathWeight(destination, depature));



        // note undirected edges are printed as: {<v1>,<v2>}
        System.out.println("-- toString output");
        // @example:toString:begin
        System.out.println(g.toString());
        // @example:toString:end
        System.out.println();
		// Code is from HelloJGraphT.java of the org.jgrapth.demo package (start)
        
        scan.close();
        
	}
	
	

}
