package F28DA_CW2;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.SingleSourcePaths;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.AsUnweightedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

public class FlyingPlanner implements IFlyingPlannerPartB<Airport,Flight>, IFlyingPlannerPartC<Airport,Flight> {

	Airport airporthead;
	Flight flighthead;
	String[][] airport;
	List<Airport> airportList = new ArrayList<Airport>();
	Map<String, Airport> airportMap = new HashMap<>();
	List<Flight> flightList = new ArrayList<Flight>();
	Graph<String, NamedWeightedEdge> g = new SimpleDirectedWeightedGraph<>(NamedWeightedEdge.class);
	Graph<String, NamedWeightedEdge> dag = new DirectedAcyclicGraph<>(NamedWeightedEdge.class);
	
	
	@Override
	public boolean populate(FlightsReader fr) {
		populate(fr.getAirports(), fr.getFlights());
		return true;
	}
	
	
	@Override
	public boolean populate(HashSet<String[]> airports, HashSet<String[]> flights) {
		// TODO Auto-generated method stub
		
		String[][] airport = airports.toArray(new String[airports.size()][3]);
		
		for (int i = 0; i < airport.length; i++) {
			Airport temp = new Airport(airport[i][0], airport[i][1], airport[i][2]); 
			airportList.add(temp);
			g.addVertex(airport[i][0]);
			airportMap.put(airport[i][0], temp);
		}
		
		String[][] flight = flights.toArray(new String[flights.size()][5]);
		

		for (int i = 0; i < flight.length; i++) {
			Flight temp = new Flight(flight[i][0], airport(flight[i][1]), flight[i][2], airport(flight[i][3]), flight[i][4], Integer.parseInt(flight[i][5])); 
			flightList.add(temp);
			NamedWeightedEdge e = g.addEdge(flight[i][1], flight[i][3]);
			g.setEdgeWeight(e, Integer.parseInt(flight[i][5]));
			e.setName(flight[i][0]);
		}
		
		return true;
	}



	@Override
	public Flight flight(String code) {
		// TODO Auto-generated method stub
		for (int i = 0; i < flightList.size(); i++)
		{
			if (flightList.get(i).getFlightCode().compareTo(code) == 0) {
				return flightList.get(i);
			}
		}
		return null;
	}
	
	@Override
	public Airport airport(String code) {
		// TODO Auto-generated method stub
		return airportMap.get(code);
	}

	@Override
	public Journey leastCost(String from, String to) throws FlyingPlannerException {
		// TODO Auto-generated method stub
		DijkstraShortestPath<String, NamedWeightedEdge> d1 = new DijkstraShortestPath<String,NamedWeightedEdge>(g);
		GraphPath<String, NamedWeightedEdge> path = d1.getPath(from, to);
		
		List<NamedWeightedEdge> edgeList = path.getEdgeList();
        List<String> edgeNames = new ArrayList<>();
        int connectingTime = 0;
		int totalTime = 0;
        int airTime = 0;
        String tempTime = null;
        for (NamedWeightedEdge e : edgeList) {
            edgeNames.add(e.getName());
            Flight temp = flight(e.getName());
            airTime += timeCalculator(temp.getFromGMTime(), temp.getToGMTime());
            if (tempTime != null) {
            	connectingTime +=  timeCalculator(tempTime, temp.getFromGMTime());
            }
            tempTime = temp.getToGMTime();
        }
        totalTime = connectingTime + airTime;
        

		Journey cheapest = new Journey((int) d1.getPath(from, to).getWeight(), d1.getPath(from, to).getLength(), d1.getPath(from, to).getVertexList(), edgeNames, airTime, connectingTime, totalTime);
		return cheapest;
	}

	@Override
	public Journey leastHop(String from, String to) throws FlyingPlannerException {
		Graph<String, NamedWeightedEdge> unweightedGraph = new AsUnweightedGraph<>(g);
		DijkstraShortestPath<String, NamedWeightedEdge> d1 = new DijkstraShortestPath<String,NamedWeightedEdge>(unweightedGraph);
		GraphPath<String, NamedWeightedEdge> path = d1.getPath(from, to);
        List<String> edgeNames = new ArrayList<>();
        int connectingTime = 0;
		int totalTime = 0;
        int airTime = 0;
        int cost = 0;
        String tempTime = null;
        for (NamedWeightedEdge e : path.getEdgeList()) {
            edgeNames.add(e.getName());
            Flight temp = flight(e.getName());
            cost += temp.getCost();
            airTime += timeCalculator(temp.getFromGMTime(), temp.getToGMTime());
            if (tempTime != null) {
            	connectingTime +=  timeCalculator(tempTime, temp.getFromGMTime());
            }
            tempTime = temp.getToGMTime();
            
        }
        totalTime = connectingTime + airTime;
		Journey hops = new Journey(cost, d1.getPath(from, to).getLength(), d1.getPath(from, to).getVertexList(), edgeNames, airTime, connectingTime, totalTime);
		
		return hops;
	}

	@Override
	public Journey leastCost(String from, String to, List<String> excluding) throws FlyingPlannerException {
		Graph<String, NamedWeightedEdge> g2 = new SimpleDirectedWeightedGraph<>(NamedWeightedEdge.class);
		
		for (String vertex : g.vertexSet()) {
			g2.addVertex(vertex);
	    }
	        
	        // Copy all edges from the original graph to the new graph
	    for (NamedWeightedEdge edge : g.edgeSet()) {
	    	String source = g.getEdgeSource(edge);
	    	String target = g.getEdgeTarget(edge);
	    	g2.addEdge(source, target);
	    }
	    g2.removeAllVertices(excluding);
		DijkstraShortestPath<String, NamedWeightedEdge> d1 = new DijkstraShortestPath<String,NamedWeightedEdge>(g2);
		GraphPath<String, NamedWeightedEdge> path = d1.getPath(from, to);
		if (path != null) {
			List<NamedWeightedEdge> edgeList = path.getEdgeList();
	        List<String> edgeNames = new ArrayList<>();
	        for (NamedWeightedEdge e : edgeList) {
	            edgeNames.add(e.getName());
	        }
			Journey cheapest = new Journey((int) d1.getPath(from, to).getWeight(), d1.getPath(from, to).getLength(), d1.getPath(from, to).getVertexList(), edgeNames, 0, 0, 0);
			return cheapest;
		}
		else {
			throw new FlyingPlannerException("No Flight Paths");
		}
	}

	@Override
	public Journey leastHop(String from, String to, List<String> excluding) throws FlyingPlannerException {
		// TODO Auto-generated method stub
		Graph<String, NamedWeightedEdge> unweightedGraph = new AsUnweightedGraph<>(g);
		unweightedGraph.removeAllVertices(excluding);
		DijkstraShortestPath<String, NamedWeightedEdge> d1 = new DijkstraShortestPath<String,NamedWeightedEdge>(unweightedGraph);
		GraphPath<String, NamedWeightedEdge> path = d1.getPath(from, to);
		List<NamedWeightedEdge> edgeList = path.getEdgeList();
        List<String> edgeNames = new ArrayList<>();
        int connectingTime = 0;
		int totalTime = 0;
        int airTime = 0;
        String tempTime = null;
        for (NamedWeightedEdge e : edgeList) {
            edgeNames.add(e.getName());
            Flight temp = flight(e.getName());
            airTime += timeCalculator(temp.getFromGMTime(), temp.getToGMTime());
            if (tempTime != null) {
            	connectingTime +=  timeCalculator(tempTime, temp.getFromGMTime());
            }
            tempTime = temp.getToGMTime();
        }
        totalTime = connectingTime + airTime;
		Journey hops = new Journey(0, d1.getPath(from, to).getLength(), d1.getPath(from, to).getVertexList(), edgeNames, airTime, connectingTime, totalTime);
		
		return hops;
	}

	public int timeCalculator(String from, String to)
	{
		//Set up local variables
		int hour1 = Integer.parseInt(from.substring(0, 2));
		int hour2 = Integer.parseInt(to.substring(0, 2));
		int minute1 = Integer.parseInt(from.substring(2, 4));
		int minute2 = Integer.parseInt(to.substring(2, 4));
		int timeInMinutes = 0;
		int totalMinutes = 0;
		
		if (hour2 == 0)
		{
			hour2 = 24;
		}
		if (hour1 == 0)
		{
			hour1 = 24;
		}
		
		if (minute1 != 0) {
			totalMinutes += 60 - minute1;
			hour1 += 1;
		}
		if (minute2 != 0) {
			totalMinutes += minute2;
		}
		

		
		if (hour2 == 25)
		{
			hour2 = 1;
		}
		if (hour1 == 25)
		{
			hour1 = 1;
		}

		if (hour1 > hour2) {
			//120 + 21 + 35 + 34 + 120 + 37 + 19 + 50
			timeInMinutes = hour1 - hour2;
			timeInMinutes = (24 - timeInMinutes)*60;		
		}
		else if (hour1 < hour2) {
			timeInMinutes = (hour2 - hour1)*60;
		}
		timeInMinutes += totalMinutes;
		return timeInMinutes;
	}
	@Override
	public Set<Airport> directlyConnected(Airport airport) {
		// TODO Auto-generated method stub
		Set<String> set = Graphs.neighborSetOf(g, airport.getCode());
		Set<Airport> airportSet = new HashSet<>();
		for (String vertex : set) {
	            Boolean temp = g.containsEdge(vertex, airport.getCode());
	            Boolean temp2 = g.containsEdge(airport.getCode(), vertex);
	            if (temp && temp2) {
	            	airportSet.add(airport(vertex));
	            }
	     }
		 
		
		 
		return airportSet;
	}

	@Override
	public int setDirectlyConnected() {
		// TODO Auto-generated method stub
		int size = 0;
		
		for (String vertex : g.vertexSet()) {
			Airport temp = airport(vertex);
			Set<Airport> airportSetC = directlyConnected(temp);
			size += airportSetC.size();
			airportMap.get(vertex).setDicrectlyConnected(airportSetC);
		}
		return size;
	}

	@Override
	public int setDirectlyConnectedOrder() {
		// TODO Auto-generated method stub
		
		for (String vertex : g.vertexSet()) {
			dag.addVertex(vertex);
		}
		for (String vertex : g.vertexSet()) {
			Airport temp = airport(vertex);
			int count = 0;
			
			Set<Airport> airportsConnected = temp.getDicrectlyConnected();
			
			for (Airport vertex2 : airportsConnected)
			{
				if (vertex2.getDicrectlyConnected().size() > airportsConnected.size()) {
					dag.addEdge(vertex, vertex2.getCode());
					count+=1;
				}
			}
			
			
			airportMap.get(vertex).setDicrectlyConnectedOrder(count);
		}
		return dag.edgeSet().size();
	}

	@Override
	public Set<Airport> getBetterConnectedInOrder(Airport airport) {
		// TODO Auto-generated method stub
		Set<Airport> list = new HashSet<>();
		DijkstraShortestPath<String, NamedWeightedEdge> d1 = new DijkstraShortestPath<String,NamedWeightedEdge>(dag);
		for (String vertex : dag.vertexSet()) {
//			DefaultEdge e = dag.getEdge( airport.getCode(), vertex);
//			if (e != null) {
//				list.add(airport(vertex));
//			}
//			Boolean temp = dag.containsEdge(airport.getCode(), vertex);
//			if (temp) {
//				list.add(airport(vertex));
//			}
			
			
			GraphPath<String, NamedWeightedEdge> path = d1.getPath(airport.getCode(), vertex);
			
			if (path != null && vertex.compareTo(airport.getCode()) != 0) {
				list.add(airport(vertex));
				
			}
		}
		return list;
	}

	@Override
	public String leastCostMeetUp(String at1, String at2) throws FlyingPlannerException {
		// TODO Auto-generated method stub
		
		 // Compute the shortest path from vertex A to every other vertex in the graph
        DijkstraShortestPath<String, NamedWeightedEdge> dijkstra1 = new DijkstraShortestPath<>(g);
        ShortestPathAlgorithm.SingleSourcePaths<String, NamedWeightedEdge> shortestPaths1 = dijkstra1.getPaths(at1);
        
        // Compute the shortest path from vertex D to every other vertex in the graph
        DijkstraShortestPath<String, NamedWeightedEdge> dijkstra2 = new DijkstraShortestPath<>(g);
        ShortestPathAlgorithm.SingleSourcePaths<String, NamedWeightedEdge> shortestPaths2 = dijkstra2.getPaths(at2);
        
        // Find the cheapest meetup point
        double minDistance = Double.POSITIVE_INFINITY;
        String meetupPoint = null;
        for (String vertex : g.vertexSet()) {
            GraphPath<String, NamedWeightedEdge> path1 = shortestPaths1.getPath(vertex);
            GraphPath<String, NamedWeightedEdge> path2 = shortestPaths2.getPath(vertex);
            if (path1 != null && path2 != null) {
                double distance = path1.getWeight() + path2.getWeight();
                if (distance < minDistance) {
                    minDistance = distance;
                    meetupPoint = vertex;
                }
            }
        }
		return meetupPoint;
	}

	@Override
	public String leastHopMeetUp(String at1, String at2) throws FlyingPlannerException {
		 // Compute the shortest path from vertex A to every other vertex in the graph
        DijkstraShortestPath<String, NamedWeightedEdge> dijkstra1 = new DijkstraShortestPath<>(g);
        ShortestPathAlgorithm.SingleSourcePaths<String, NamedWeightedEdge> shortestPaths1 = dijkstra1.getPaths(at1);
        
        // Compute the shortest path from vertex D to every other vertex in the graph
        DijkstraShortestPath<String, NamedWeightedEdge> dijkstra2 = new DijkstraShortestPath<>(g);
        ShortestPathAlgorithm.SingleSourcePaths<String, NamedWeightedEdge> shortestPaths2 = dijkstra2.getPaths(at2);
        
        // Find the cheapest meetup point
        double minDistance = Double.POSITIVE_INFINITY;
        String meetupPoint = null;
        for (String vertex : g.vertexSet()) {
            GraphPath<String, NamedWeightedEdge> path1 = shortestPaths1.getPath(vertex);
            GraphPath<String, NamedWeightedEdge> path2 = shortestPaths2.getPath(vertex);
            if (path1 != null && path2 != null) {
                double distance = path1.getWeight() + path2.getWeight();
                if (distance < minDistance) {
                    minDistance = distance;
                    meetupPoint = vertex;
                }
            }
        }
		return meetupPoint;
	}
	public int leastTime(String a1, String a2) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> predecessors = new HashMap<>();
        Set<String> unvisited = new HashSet<>();

        for (String vertex : g.vertexSet()) {
            distances.put(vertex, Integer.MAX_VALUE);
            predecessors.put(vertex, null);
            unvisited.add(vertex);
        }

        distances.put(a1, 0);
        int airTime = 0;
        int connectingTime = 0;
        String tempTime = null;
        int minDistance = Integer.MAX_VALUE;
        while (!unvisited.isEmpty()) {
            String current = null;
            minDistance = Integer.MAX_VALUE;

            for (String vertex : unvisited) {
                int distance = distances.get(vertex);
                if (distance < minDistance) {
                    current = vertex;
                    minDistance = distance;
                }
            }

            unvisited.remove(current);

            if (minDistance == Integer.MAX_VALUE) {
                break;
            }

            for (NamedWeightedEdge edge : g.outgoingEdgesOf(current)) {
                String neighbor = g.getEdgeTarget(edge);
                Flight temp = flight(edge.getName());
                
                airTime += timeCalculator(temp.getFromGMTime(), temp.getToGMTime());
                if (tempTime != null) {
                	connectingTime +=  timeCalculator(tempTime, temp.getFromGMTime());
                }
                tempTime = temp.getToGMTime();
            
                int weight = connectingTime + airTime;
                int alternative = distances.get(current) + weight;

                if (alternative < distances.get(neighbor)) {
                    distances.put(neighbor, alternative);
                    predecessors.put(neighbor, current);
                }
            }
        }

        List<String> path = new ArrayList<>();
        String current = a2;

        while (predecessors.get(current) != null) {
            path.add(current);
            current = predecessors.get(current);
        }

        path.add(current);
        Collections.reverse(path);

        return minDistance;
	}
	
	@Override
	public String leastTimeMeetUp(String at1, String at2, String startTime) throws FlyingPlannerException {
		// TODO Auto-generated method stub
		 // Compute the shortest path from vertex A to every other vertex in the graph
        DijkstraShortestPath<String, NamedWeightedEdge> dijkstra1 = new DijkstraShortestPath<>(g);
        ShortestPathAlgorithm.SingleSourcePaths<String, NamedWeightedEdge> shortestPaths1 = dijkstra1.getPaths(at1);
        
        // Compute the shortest path from vertex D to every other vertex in the graph
        DijkstraShortestPath<String, NamedWeightedEdge> d2 = new DijkstraShortestPath<>(g);
        ShortestPathAlgorithm.SingleSourcePaths<String, NamedWeightedEdge> shortestPaths2 = d2.getPaths(at2);
        
    	
		int totalTime = 0;
		int totalTime2 = 0;
        int airTime = 0;
        int connectingTime2 = 0;
        int connectingTime = 0;
        int airTime2 = 0;
        
        AllDirectedPaths<String, NamedWeightedEdge> d1 = new AllDirectedPaths<>(g);
        // Find the cheapest meetup point
        double minDistance = Double.POSITIVE_INFINITY;
        String meetupPoint = null;
        int shortestTime = Integer.MAX_VALUE;
        for (String vertex : g.vertexSet()) {
        	System.out.println(vertex);
            airTime = 0;
            connectingTime2 = 0;
            connectingTime = 0;
            airTime2 = 0;
            GraphPath<String, NamedWeightedEdge> path1 = shortestPaths1.getPath(vertex);
            GraphPath<String, NamedWeightedEdge> path2 = shortestPaths2.getPath(vertex);
            
            
            if (path1 != null && path2 != null) {

                Flight temp = null;
                Flight temp2 = null;
                if (path1.getStartVertex().compareTo(vertex) != 0)
                	temp = flight(path1.getEdgeList().get(0).getName());
                else
                	continue;
                
                if (path2.getStartVertex().compareTo(vertex) != 0)
                	temp2 = flight(path2.getEdgeList().get(0).getName());
                else
                	continue;
                
                int hour1 = Integer.parseInt(temp.getFromGMTime().substring(0, 2));
        		int hour2 = Integer.parseInt(temp2.getFromGMTime().substring(0, 2));
        		int hour3 = Integer.parseInt(startTime.substring(0, 2));
 

                if (hour1 >= hour3 && hour2 >= hour3) {
	                String tempTime = null;
	                
	                for (NamedWeightedEdge e : path1.getEdgeList()) {
	
	                    Flight temp3 = flight(e.getName());
	                    airTime += timeCalculator(temp3.getFromGMTime(), temp3.getToGMTime());
	                    if (tempTime != null) {
	                    	connectingTime +=  timeCalculator(tempTime, temp3.getFromGMTime());
	                    }
	                    tempTime = temp3.getToGMTime();
	                }
	                
	                if(airTime > 0 && connectingTime > 0)
	                	totalTime = airTime + connectingTime;
	                
	                for (NamedWeightedEdge e : path2.getEdgeList()) {
	                	 
	                    Flight temp4 = flight(e.getName());
	                    airTime2 += timeCalculator(temp4.getFromGMTime(), temp4.getToGMTime());
	                    if (tempTime != null) {
	                    	connectingTime2 +=  timeCalculator(tempTime, temp4.getFromGMTime());
	                    }
	                    tempTime = temp4.getToGMTime();
	                }
	                if(airTime > 0 && connectingTime > 0)
	                	totalTime2 = airTime2 + connectingTime2;
	                if (totalTime > 0 && totalTime2 > 0) {
		                double distance = totalTime + totalTime2;
		                if (distance < minDistance) {
		                	
		                    minDistance = distance;
		                    meetupPoint = vertex;
		                }
	                }
                }
            }

        }
  
		return meetupPoint;
	}
	




}

