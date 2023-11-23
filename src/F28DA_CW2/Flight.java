package F28DA_CW2;

public class Flight implements IFlight {
	String flightCode;
	Airport to;
	Airport from;
	int cost;
	String fromTime;
	String toTime;


	public Flight (String flightCode, Airport from, String fromTime, Airport to, String toTime, int cost)
	{
		this.flightCode = flightCode;
		this.to = to;
		this.from = from;
		this.cost = cost;
		this.fromTime = fromTime;
		this.toTime = toTime;
	}
	
	@Override
	public String getFlightCode() {
		// TODO Auto-generated method stub
		return flightCode;
	}

	@Override
	public Airport getTo() {
		// TODO Auto-generated method stub
		return to;
	}

	@Override
	public Airport getFrom() {
		// TODO Auto-generated method stub
		return from;
	}

	@Override
	public String getFromGMTime() {
		// TODO Auto-generated method stub
		return fromTime;
	}

	@Override
	public String getToGMTime() {
		// TODO Auto-generated method stub
		return toTime;
	}

	@Override
	public int getCost() {
		// TODO Auto-generated method stub
		return cost;
	}


}
