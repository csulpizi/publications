package examples;
import publications.Publication;
import java.util.function.Consumer;

public class Example0 {
	private static void newHeadline(Publication<String,String> p, String headline) {
		System.out.println("Breaking news: " + headline);
		p.publish(headline);
		System.out.println();
	}
	public static void main(String [] args) {
		Consumer<String> rosie = msg -> { System.out.println("Rosie understands that " + msg); };
		Consumer<String> ryan = msg -> { System.out.println("Ryan is skeptical that " + msg); };
		Consumer<String> nelly = msg -> { System.out.println("Nelly doesn't know how to read."); };
		
		Publication<String,String> nyTimes = new Publication<String,String>();
		
		System.out.println("Rosie subscribes to NY Times!");
		nyTimes.subscribe("Rosie", rosie);
		newHeadline(nyTimes, "pandas are adorable");
		
		System.out.println("Ryan subscribes to NY Times!");
		nyTimes.subscribe("Ryan", ryan);
		System.out.println("Nelly subscribes to NY Times!");
		nyTimes.subscribe("Nelly", nelly);
		newHeadline(nyTimes, "it's going to rain");
		
		newHeadline(nyTimes, "bleach is poisonous");
		
		System.out.println("Nelly dies from bleach poisoning, her family cancels her subscription to NY times.");
		nyTimes.unsubscribe("Nelly");
		
		newHeadline(nyTimes, "woman dies from bleach poisoning");
	}
}

/*
Output:

	Rosie subscribes to NY Times!
	Breaking news: pandas are adorable
	Rosie understands that pandas are adorable
	
	Ryan subscribes to NY Times!
	Nelly subscribes to NY Times!
	Breaking news: it's going to rain
	Ryan is skeptical that it's going to rain
	Rosie understands that it's going to rain
	Nelly doesn't know how to read.
	
	Breaking news: bleach is poisonous
	Ryan is skeptical that bleach is poisonous
	Rosie understands that bleach is poisonous
	Nelly doesn't know how to read.
	
	Nelly dies from bleach poisoning, her family cancels her subscription to NY times.
	Breaking news: woman dies from bleach poisoning
	Ryan is skeptical that woman dies from bleach poisoning
	Rosie understands that woman dies from bleach poisoning
 */