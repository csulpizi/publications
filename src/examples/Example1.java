package examples;
import publications.Publication;

public class Example1 {
	public static void main(String [] args) {
		Publication<String,Integer> payroll = new Publication<String,Integer>();
		payroll.subscribe("Steve",new Employee("Steve",10).pay);
		payroll.subscribe("Jennifer",new Employee("Dave",15).pay);
		payroll.subscribe("CEO Man",new Employee("CEO Man",1500).pay);
		
		System.out.println("\nPaying employees for 3 hours of work.");
		payroll.publish(3);
		System.out.println("\nPaying employees for 4 hours of work.");
		payroll.publish(4);
		System.out.println("\nPaying employees for 1 hours of work.");
		payroll.publish(1);
	}
}