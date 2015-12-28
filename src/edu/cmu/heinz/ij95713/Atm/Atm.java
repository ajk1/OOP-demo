package edu.cmu.heinz.ij95713.Atm;
import java.util.*;

import edu.cmu.heinz.ij95713.Bank.*;

/**
 * @author Alex
 * contains all the methods for handling an atm transaction
 */
public class Atm {
	private ReceiptPrinter rp = new ReceiptPrinter();
	private CardReader cr = new CardReader();
	private CashDispenser cd = new CashDispenser();
	private IOUnit io = new IOUnit();
	private DepositUnit du = new DepositUnit();
	private BankServer bank;
	
	private static interface TransactionHandler {
		void handleTransaction(Atm atm, String cardNumber);
	}
	private static class DepositTHandler implements TransactionHandler {
		public void handleTransaction(Atm atm, String cardNumber) {
			double amount = atm.io.obtainAmount();
			atm.du.getDepositEnvelope();
			atm.bank.doDeposit(cardNumber, amount);
			atm.rp.printReceipt("Deposit of $" + amount + " is made to account " + cardNumber);
			atm.rp.printReceipt("New balance on account is " + atm.bank.doQuery(cardNumber));
		}
	}
//	other handlers here
	private static class WithdrawTHandler implements TransactionHandler {
		public void handleTransaction(Atm atm, String cardNumber) {
			int amount = (int)atm.io.obtainAmount();
			while (!atm.bank.verifyFunds(cardNumber, amount)) {
				System.out.println("Insufficient funds. \nCurrent balance: "
						+atm.bank.doQuery(cardNumber));
				amount = (int)atm.io.obtainAmount();
			}
			atm.bank.doWithdraw(cardNumber, amount);
			atm.cd.dispenseCash(amount);
			atm.rp.printReceipt("Withdrawal of $" + amount + " is made from account " + cardNumber);
			atm.rp.printReceipt("New balance on account is " + atm.bank.doQuery(cardNumber));
		}
	}
	
	private static class QueryTHandler implements TransactionHandler {
		public void handleTransaction(Atm atm, String cardNumber) {
			double queryAmount = atm.bank.doQuery(cardNumber);
			atm.rp.printReceipt("Balance on account " + cardNumber
						+ " is " + queryAmount);
		}
	}
	
	private static class CancelTHandler implements TransactionHandler {
		public void handleTransaction(Atm atm, String cardNumber) {
			atm.rp.printReceipt("Your transaction has been canceled");
			atm.handleTransaction();
		}
	}
	
	private static class QuitTHandler implements TransactionHandler {
		public void handleTransaction(Atm atm, String cardNumber) {
			try 
			{
			   Thread.sleep(4000);
			} 
			catch (InterruptedException e) 
			{
			   e.printStackTrace();
			}
			finally {
				System.exit(0);
			}
		}
	}
	
	private static class TransactionHandlerMapEntry {
		TransactionHandlerMapEntry(IOUnit.Transaction transaction, TransactionHandler handler) {
			this.transaction = transaction;
			transactionHandler = handler;
		}
		IOUnit.Transaction transaction;
		TransactionHandler transactionHandler;
	}
	private static TransactionHandlerMapEntry[] thmap = {
		new TransactionHandlerMapEntry(IOUnit.Transaction.DEPOSIT, new DepositTHandler()),
		new TransactionHandlerMapEntry(IOUnit.Transaction.WITHDRAW, new WithdrawTHandler()),
		new TransactionHandlerMapEntry(IOUnit.Transaction.QUERY, new QueryTHandler()),
		new TransactionHandlerMapEntry(IOUnit.Transaction.CANCEL, new CancelTHandler()),
		new TransactionHandlerMapEntry(IOUnit.Transaction.QUIT, new QuitTHandler())
	};
	private static TransactionHandler findTHandler(IOUnit.Transaction t) {
		for (TransactionHandlerMapEntry handler : thmap) {
			if (handler.transaction == t)
				return handler.transactionHandler;
		}
		return null;
	}
	public Atm(BankServer b) {
		bank = b;
	}
	public void handleTransaction() {
		String cardNumber = cr.readCard();
		boolean verified = false;
		for (int trial = 1; trial <= 3; trial++) {
			String pin = io.obtainCustomerPin();
			verified = bank.verifyPin(cardNumber, pin);
			if (verified) {
				break;
			}
		}
		if (!verified) {
			cr.confiscateCard();
			return;
		}
		
		IOUnit.Transaction t;
		do {
			t = io.obtainTransaction();
			TransactionHandler handler = findTHandler(t);
			if (handler != null) {
				handler.handleTransaction(this, cardNumber);
			}
		} while (t != IOUnit.Transaction.QUIT);
		
		cr.releaseCard();
		System.exit(0);
	}
}

class ReceiptPrinter {
	public void printReceipt(String str) {
		System.out.println(str);
	}
}

class CardReader {
	public String readCard() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter your card number: ");
		String cardNumber = sc.nextLine();
		return cardNumber;
	}
	public void confiscateCard() {
		System.out.println("Your card is confiscated.");
		System.exit(0);
	}
	public void releaseCard() {
		System.out.println("Your card is released.");
	}
}

class CashDispenser {
	public void dispenseCash(int amount) {
		System.out.println("$"+ amount +" is dispensed.");
	}
}

class DepositUnit {
	public void getDepositEnvelope() {
		System.out.println("Envelope is successfully received.");
	}
}

class IOUnit {
	private Scanner sc;
	enum Transaction {
		DEPOSIT, WITHDRAW, QUERY, CANCEL, QUIT
	}

	
	public IOUnit() {
		sc = new Scanner(System.in);
	}
	public String obtainCustomerPin() {
		System.out.println("Enter your PIN number: ");
		String pin = sc.nextLine();
		return pin;
	}
	public Transaction obtainTransaction() {
		Transaction transaction = Transaction.CANCEL;
		int choice;
		do {
			System.out.println("Enter the transaction type: "
					+ "\n<1> DEPOSIT" + "\n<2> WITHDRAW" 
					+ "\n<3> QUERY" + "\n<4> CANCEL" + "\n<5> QUIT");
			choice = sc.nextInt();
			switch(choice) {
			case 1: transaction = Transaction.DEPOSIT;
				break;
			case 2: transaction = Transaction.WITHDRAW;
				break;
			case 3: transaction = Transaction.QUERY;
				break;
			case 4: transaction = Transaction.CANCEL;
				break;
			case 5: transaction = Transaction.QUIT;
				break;
			default: choice = 0;
			}
		} while (choice == 0);
		return transaction;
	}
	public double obtainAmount() {
		System.out.println("Enter the amount: ");
		double amount = sc.nextDouble();
		return amount;
	}
}
