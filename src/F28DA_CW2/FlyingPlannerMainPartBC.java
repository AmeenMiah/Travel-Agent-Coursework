package F28DA_CW2;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class FlyingPlannerMainPartBC {

	public static void main(String[] args) {

		// Your implementation should be in FlyingPlanner.java, this class is only to
		// run the user interface of your programme.

		FlyingPlanner fi;
		fi = new FlyingPlanner();
		try {
			Scanner scan = new Scanner(System.in);
	        System.out.println("Please enter the depature airport (use the three digit code for the airport)");
	        String destination = scan.nextLine().toUpperCase();  // Read user input
	        
	        System.out.println("Please enter the destination airport (use the three digit code for the airport)");
	        String depature = scan.nextLine().toUpperCase();  // Read user input
			fi.populate(new FlightsReader());
			Airport from = fi.airport(destination);
			Airport to = fi.airport(depature);
			Journey i = fi.leastCost(destination, depature);
			System.out.println("Journey for " + from.city + "(" + from.airportCode + ") to " + to.city + " (" + to.airportCode + ")");
			String s1 = String.format("%1$s %2$s %3$s %4$s %5$s %6$s", "Leg", "Leave", "At", "On", "Arrive", "At");
			System.out.println(s1);
			Flight flight;
			for (int x = 0; x < i.getFlights().size(); x++) {
				flight = fi.flight(i.flights.get(x));
				s1 = String.format("%d: %1$s %2$s (%3$s) %4$s %5$s %6$s (%7$s) %8$s", x, flight.getFrom().city, flight.getFrom().airportCode, flight.getFromGMTime(), i.flights.get(x) , flight.getTo().city, flight.getTo().airportCode, flight.getToGMTime());
				System.out.println(s1);
			}
			System.out.println("Total Journey Cost = £" + i.cost);
			// Implement here your user interface using the methods of Part B. You could
			// optionally expand it to use the methods of Part C.
			scan.close();
		} catch (FileNotFoundException | FlyingPlannerException e) {
			e.printStackTrace();
			
		}

	}

}
