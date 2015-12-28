import edu.cmu.heinz.ij95713.SP.ServiceProvider;

public class ProviderMain {

	public static void main(String[] args) {
		ServiceProvider sp = new ServiceProvider("Happy Service Provider");
		sp.loadCustomers("customers.dat");
		sp.printCustomerReport();
	}

}
