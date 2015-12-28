package edu.cmu.heinz.ij95713.SP;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

class Customer {

	private ArrayList<CustomerAccount> accounts = new ArrayList<CustomerAccount>();
	private Date dateOpened = new Date();
	String id, name;
	Address address;
	
	public Customer(String id, String name, Address address) {
		this.id = id;
		this.name = name;
	}
	
	// TODO define other Customer methods here
	public void addAccount(CustomerAccount account) {
		accounts.add(account);
	}
	
	public Address getAddress() {
		return address;
	}
	
	public String toString() {
		String s = id + ": " + name + "\nAddress - " + address;
		for (CustomerAccount i : accounts) {
			s = s + "\nAccount - " + i.toString();
		}
		return s;
	}
	
	/**
	 * @param s Service to be added to customer account
	 * Note: Add the service to the last account created for the customer
	 */
	public void addService(Service s) {
		int n = accounts.size();
		if (n == 0) {
			return;
		}
		accounts.get(n - 1).setService(s);
	}
}

class Address {
	private String street;
	private String city;
	private String state;
	private String zip;
	
	public Address(String street, String city, String state, String zip) {
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}
	
	public String toString() {
		return street +", "+ city +", "+ state +" "+ zip;
	}
}

/**
 * An abstract class representing a service that is offered by a service provider.
 * It has an abstract method getCharge().
 */
abstract class Service {
	Service(String n, String d) {
		name = n;
		description = d;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	public void start() {
		System.out.println(name + " started");
	}

	public void stop() {
		System.out.println(name + " stopped");
	}

	public void suspend() {
		System.out.println(name + " suspended");
	}

	public void resume() {
		System.out.println(name + " resumed");
	}

	/**
	 * @return The charge of the service
	 * An abstract method
	 */
	public abstract double getCharge();

	public String toString() {
		return "\tService - " + name + " - " + description;
	}

	private String name;

	private String description;
}

/**
 * Abstraction of a web hosting service that is offered by a service provider
 */
class WebHostingService extends Service {
	private double monthlyCharge;
	private double chargeForOverUse;
	private double maxMonthlyDataTransfer;
	private double totalTransfer;
	private double maxStorage;
	
	WebHostingService(String n, String desc, double monthlyCharge,
			double chargeForOverUse, double maxMonthlyDataTransfer,
			double totalTransfer, double maxStorage) {
		super(n, desc);
		this.monthlyCharge = monthlyCharge;
		this.chargeForOverUse = chargeForOverUse;
		this.maxMonthlyDataTransfer = maxMonthlyDataTransfer;
		this.totalTransfer = totalTransfer;
		this.maxStorage = maxStorage;
	}
	
	/* (non-Javadoc)
	 * @see edu.cmu.heinz.ij95713.SP.Service#getCharge()
	 * Calculate charge as monthlyCharge + any additional charge due to overuse
	 */
	public double getCharge() {
		double charge = monthlyCharge;
		if (totalTransfer > maxMonthlyDataTransfer) {
			charge += chargeForOverUse
					* (totalTransfer - maxMonthlyDataTransfer);
		}
		return charge;
	}
	
	/**
	 * @return Returns the chargeForOverUse.
	 */
	public double getChargeForOverUse() {
		return chargeForOverUse;
	}

	/**
	 * @param chargeForOverUse The chargeForOverUse to set.
	 */
	public void setChargeForOverUse(double chargeForOverUse) {
		this.chargeForOverUse = chargeForOverUse;
	}

	/**
	 * @return Returns the maxMonthlyDataTransfer.
	 */
	public double getMaxMonthlyDataTransfer() {
		return maxMonthlyDataTransfer;
	}

	/**
	 * @param maxMonthlyDataTransfer The maxMonthlyDataTransfer to set.
	 */
	public void setMaxMonthlyDataTransfer(double maxMonthlyDataTransfer) {
		this.maxMonthlyDataTransfer = maxMonthlyDataTransfer;
	}

	/**
	 * @return Returns the maxStorage.
	 */
	public double getMaxStorage() {
		return maxStorage;
	}

	/**
	 * @param maxStorage The maxStorage to set.
	 */
	public void setMaxStorage(double maxStorage) {
		this.maxStorage = maxStorage;
	}

	/**
	 * @return Returns the monthlyCharge.
	 */
	public double getMonthlyCharge() {
		return monthlyCharge;
	}

	/**
	 * @param monthlyCharge The monthlyCharge to set.
	 */
	public void setMonthlyCharge(double monthlyCharge) {
		this.monthlyCharge = monthlyCharge;
	}

	/**
	 * @return Returns the totalTransfer.
	 */
	public double getTotalTransfer() {
		return totalTransfer;
	}

	/**
	 * @param totalTransfer The totalTransfer to set.
	 */
	public void setTotalTransfer(double totalTransfer) {
		this.totalTransfer = totalTransfer;
	}
}

// TODO define other service classes here
class DialUpService extends Service {
	private double monthlyCharge;
	
	DialUpService(String n, String desc, double monthlyCharge) {
		super(n, desc);
		this.monthlyCharge = monthlyCharge;
	}
	
	/* (non-Javadoc)
	 * @see edu.cmu.heinz.ij95713.SP.Service#getCharge()
	 * Calculate charge as monthlyCharge + any additional charge due to overuse
	 */
	public double getCharge() {
		return monthlyCharge;
	}
	

	/**
	 * @return Returns the monthlyCharge.
	 */
	public double getMonthlyCharge() {
		return monthlyCharge;
	}

	/**
	 * @param monthlyCharge The monthlyCharge to set.
	 */
	public void setMonthlyCharge(double monthlyCharge) {
		this.monthlyCharge = monthlyCharge;
	}
}

class DslService extends Service {
	private double monthlyCharge;
	private double rate;
	DslService(String n, String desc, double monthlyCharge, double rate) {
		super(n, desc);
		this.monthlyCharge = monthlyCharge;
		this.rate = rate;
	}
	
	/* (non-Javadoc)
	 * @see edu.cmu.heinz.ij95713.SP.Service#getCharge()
	 * Calculate charge as monthlyCharge + any additional charge due to overuse
	 */
	public double getCharge() {
		return monthlyCharge;
	}
	

	/**
	 * @return Returns the monthlyCharge.
	 */
	public double getMonthlyCharge() {
		return monthlyCharge;
	}

	/**
	 * @param monthlyCharge The monthlyCharge to set.
	 */
	public void setMonthlyCharge(double monthlyCharge) {
		this.monthlyCharge = monthlyCharge;
	}
	

	/**
	 * @return Returns the rate.
	 */
	public double getRate() {
		return monthlyCharge;
	}

	/**
	 * @param rate the Rate to set.
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}
}

/**
 * Interface for service factory classes. createService() method should obtain the parameters needed 
 * for creating a service object from a string tokenizer object and should create one.
 */
interface ServiceFactory {
	public Service createService(StringTokenizer t);
}

/**
 * The factory class for creating a web hosting service object
 */
class WebHostingServiceFactory implements ServiceFactory {
	public Service createService(StringTokenizer t) {
		return new WebHostingService(t.nextToken(), // name
				t.nextToken(), // desc
				Double.parseDouble(t.nextToken()), // monthlyCharge,
				Double.parseDouble(t.nextToken()), // chargeForOverUse
				Double.parseDouble(t.nextToken()), // maxMonthlyDataTransfer
				Double.parseDouble(t.nextToken()), // totalTransfer
				Double.parseDouble(t.nextToken())); // double maxStorage
	}
}

// TODO define other service factory objects here
class DialUpServiceFactory implements ServiceFactory {
	public Service createService(StringTokenizer t) {
		return new DialUpService(t.nextToken(), t.nextToken(), 
				Double.parseDouble(t.nextToken()));
	}
}

class DslServiceFactory implements ServiceFactory {
	public Service createService(StringTokenizer t) {
		return new DslService(t.nextToken(), t.nextToken(), 
				Double.parseDouble(t.nextToken()), 
				Double.parseDouble(t.nextToken()));
	}
}

// TODO define Customer class here
class CustomerAccount {
	private Date dateOpened = new Date();
	private Billable billable;
	private Service service;
	
	CustomerAccount(Billable b)  {
		billable = b;
	}
	
	public double getTotalCharge() {
		return service.getCharge();
	}
	
	public void SubmitPayment(double amount) {
		billable.submitPayment(amount);
	}
	
	public void setService(Service s) {
		service = s;
	}
	
	public String toString() {
		return dateOpened.toString() + "\n\t"
				+ service.toString();
	}
}

/**
 * Interface for all Billable objects
 * verify() is meant to verify the correctness of the billable object such as verifying a checking account.
 * submitPayment() is meant to submit a payment for example the monthly payment to the billable.
 */
interface Billable {
	boolean verify();

	void submitPayment(double amount);
}

/**
 * Billable class that facilitates paying via a credit card.
 */
class PayWithCreditCard implements Billable {
	private CreditCard creditCard;
	
	PayWithCreditCard(CreditCard cc) {
		creditCard = cc;
	}

	public boolean verify() {
		System.out.println("Credit card verified.");
		return true;
	}

	public void submitPayment(double amount) {
		System.out.println("$"+ amount +" charged to credit card.");
	}

	public String toString() {
		return "Credit card: " + creditCard;
	}

	/**
	 * @return Returns the creditCard.
	 */
	public CreditCard getCreditCard() {
		return creditCard;
	}

	/**
	 * @param creditCard The creditCard to set.
	 */
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}
}

//TODO define other Billable classes here
class PayWithCheckingAccount implements Billable {
	private String bankName;
	private String routingNumber;
	
	PayWithCheckingAccount(String bn, String rn) {
		bankName = bn;
		routingNumber = rn;
	}

	public boolean verify() {
		System.out.println("Checking account verified.");
		return true;
	}

	public void submitPayment(double amount) {
		System.out.println("$"+ amount +" charged to checking account.");
	}
}

class BillToAddress implements Billable {
	private Address address;
	
	BillToAddress(Address address) {
		this.address = address;
	}
	public boolean verify() {
		System.out.println("Address verified.");
		return true;
	}

	public void submitPayment(double amount) {
		System.out.println("$"+ amount +" billed to address.");
	}
	
}

class CreditCard {
	private String id;
	private int expMonth;
	private int expYear;
	private Address address;
	
	public CreditCard(String id, int expMonth, int expYear, Address address) {
		this.id = id;
		this.expMonth = expMonth;
		this.expYear = expYear;
		this.address = address;
	}
}


/**
 * Interface for the factory classes to create billable objects.
 * createBillable() should obtain the parameters needed to create a billable object 
 * from a string tokenizer and create one 
 */
interface BillableFactory {
	public Billable createBillable(StringTokenizer t, Customer c);
}

/**
 * The factory class for creating a pay with credit card billable object
 */
class PayWithCreditCardFactory implements BillableFactory {
	public Billable createBillable(StringTokenizer t, Customer customer) {
		String id = t.nextToken();
		int expMonth = Integer.parseInt(t.nextToken());
		int expYear = Integer.parseInt(t.nextToken());
		return new PayWithCreditCard(new CreditCard(id, expMonth, expYear,
				customer.getAddress()));
	}
}

// TODO define other billable factory classes here
class PayWithCheckingAccountFactory implements BillableFactory {
	public Billable createBillable(StringTokenizer t, Customer customer) {
		String bankName = t.nextToken();
		String routingNumber = t.nextToken();
		return new PayWithCheckingAccount(bankName, routingNumber);
	}
}

class BillToAddressFactory implements BillableFactory {
	public Billable createBillable(StringTokenizer t, Customer customer) {
		return new BillToAddress(customer.getAddress());
	}
}
/**
 * Abstraction for a service provider that can provide various Internet access services to its customers. 
 */
public class ServiceProvider {

	public ServiceProvider(String name) {
		this.name = name;
	}

	/**
	 * @param filename The name of the input file
	 * Read the input file that contains customer related information and 
	 * create all customer objects
	 */
	public void loadCustomers(String filename) {
		BufferedReader instream = null;
		try {
			instream = new BufferedReader(new FileReader(
					filename));
			String line;

			// read the input stream line by line.
			while ((line = instream.readLine()) != null) {
				StringTokenizer tokenizer = new StringTokenizer(line, "|");

				String id = tokenizer.nextToken();
				String name = tokenizer.nextToken();
				String street = tokenizer.nextToken();
				String city = tokenizer.nextToken();
				String state = tokenizer.nextToken();
				String zip = tokenizer.nextToken();
				Customer customer = new Customer(id, name, new Address(street,
						city, state, zip));
				while (tokenizer.hasMoreTokens()) {
					String factorystr = tokenizer.nextToken();
					factorystr = "edu.cmu.heinz.ij95713.SP." + factorystr;
					BillableFactory bfactory = (BillableFactory) Class.forName(
							factorystr).newInstance();
					customer.addAccount(new CustomerAccount(bfactory
							.createBillable(tokenizer, customer)));
					factorystr = tokenizer.nextToken();
					factorystr = "edu.cmu.heinz.ij95713.SP." + factorystr;
					ServiceFactory sfactory = (ServiceFactory) Class.forName(
							factorystr).newInstance();
					customer.addService(sfactory.createService(tokenizer));
				}
				customers.add(customer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				instream.close();
			}
			catch(IOException e) {}
		}
	}

	/**
	 * Print out customer report onto the console
	 */
	public void printCustomerReport() {
		for (Customer customer : customers) {
			System.out.println(customer);
		}
	}

	private ArrayList<Customer> customers = new ArrayList<Customer>();

	private String name;
}
