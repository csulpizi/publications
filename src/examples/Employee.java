package examples;

import java.util.function.Consumer;

public class Employee {
	String name;
	int salary;
	int bankAccount = 100;
	Employee(String name, int salary){
		this.name = name;
		this.salary = salary;
	}
	Consumer<Integer> pay = hours -> { 
		this.bankAccount += this.salary * hours;
		System.out.println(this.name + " now has a bank account balance of $" + this.bankAccount);
	}; 
}
