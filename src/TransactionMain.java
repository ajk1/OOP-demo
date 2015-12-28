import java.io.IOException;

import edu.cmu.heinz.ij95713.Atm.Atm;
import edu.cmu.heinz.ij95713.Bank.BankServer;

/**
 * 
 */

/**
 * @author Alex
 *
 */
public class TransactionMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		new Atm(new BankServer(args[0])).handleTransaction();
	}

}
