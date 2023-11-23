package F28DA_CW2;

import java.util.HashSet;
import java.util.Set;

public class Airport implements IAirportPartB, IAirportPartC {
	
	String name;
	String city;
	String airportCode;
	int length;
	Set<Airport> airportConnected = new HashSet<>();
	int directlyConnectedOrderCount = 0;
	
	public Airport(String airportCode, String city, String name) {
		this.name = name;
		this.city = city;
		this.airportCode = airportCode;
		this.length = 1;
	}
	
	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return airportCode;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public void setDicrectlyConnected(Set<Airport> dicrectlyConnected) {
		airportConnected = dicrectlyConnected;

	}

	@Override
	public Set<Airport> getDicrectlyConnected() {
		// TODO Auto-generated method stub
		return airportConnected;
	}


	@Override
	public void setDicrectlyConnectedOrder(int order) {
		this.directlyConnectedOrderCount = order;
	}
	

	@Override
	public int getDirectlyConnectedOrder() {
		// TODO Auto-generated method stub
		return directlyConnectedOrderCount;
	}

}
