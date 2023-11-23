package F28DA_CW2;

import java.util.List;

public class Journey implements IJourneyPartB<Airport, Flight>, IJourneyPartC<Airport, Flight> {

	int cost;
	int hops;
	List<String> stops;
	List<String> flights;
	int airTime;
	int totalTime;
	int connectingTime;
	int airMiles;
	
	public Journey(int cost, int hops, List<String> stops, List<String> flights, int airTime, int connectingTime, int totalTime) {
		this.cost = cost;
		this.hops = hops;
		this.stops = stops;
		this.flights = flights;
		this.airTime = airTime;
		this.totalTime = totalTime;
		this.connectingTime = connectingTime;
		this.airMiles = (int) ((int) this.airTime * (0.03 * cost));
	}
	@Override
	public List<String> getStops() {
		// TODO Auto-generated method stub
		return stops;
	}

	@Override
	public List<String> getFlights() {
		// TODO Auto-generated method stub
		return flights;
	}

	@Override
	public int totalHop() {
		// TODO Auto-generated method stub
		return hops;
	}

	@Override
	public int totalCost() {
		// TODO Auto-generated method stub
		return cost;
	}

	@Override
	public int airTime() {
		// TODO Auto-generated method stub
		return airTime;
	}

	@Override
	public int connectingTime() {
		// TODO Auto-generated method stub
		return connectingTime;
	}

	@Override
	public int totalTime() {
		// TODO Auto-generated method stub
		return totalTime;
	}

	@Override
	public int totalAirmiles() {
		// TODO Auto-generated method stub
		return airMiles;
	}

}
