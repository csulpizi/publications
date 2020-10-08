package examples;
import java.util.function.Consumer;

import publications.Publication;

public class Example2 {
	private static void newHeadline(Publication<String,String> p, String headline) {
		System.out.println("Breaking news: " + headline);
		p.publish(headline);
		System.out.println();
	}
	public static void main(String [] args) {
		Consumer<String> rosie = msg -> { System.out.println("Rosie understands that " + msg); };
		Consumer<String> ryan = msg -> { System.out.println("Ryan is skeptical that " + msg); };
		// note that nelly is still dead from drinking too much bleach
		
		Publication<String,String> nyTimes = new Publication<String,String>();
		
		System.out.println("Rosie subscribes to NY Times!");
		nyTimes.subscribe("Rosie", rosie);
		nyTimes.setSubscriberExpiryCount("Rosie",3);
		newHeadline(nyTimes,"tacos are tasty");
		
		System.out.println("Ryan subscribes to NY Times!");
		nyTimes.subscribe("Ryan", ryan);
		nyTimes.setSubscriberExpiryCount("Ryan",4);
		newHeadline(nyTimes,"seafood is slimy");
		newHeadline(nyTimes,"burgers are burgeois");
		
		System.out.println("Rosie's free trial is over, so she's no longer going to be subscribed to NY Times.");
		newHeadline(nyTimes,"zebras are zesty");
		newHeadline(nyTimes,"ghosts are grimy");
		
		System.out.println("Now Ryan's free trial is over.");
		newHeadline(nyTimes,"crabs are crabby");
	}
}