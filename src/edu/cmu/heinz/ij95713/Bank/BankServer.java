/**
 * 
 */
package edu.cmu.heinz.ij95713.Bank;
import java.util.*;
import java.io.*;

/**
 * @author Alex
 *
 */
public class BankServer {
	private ArrayList<Account> accounts; //perhaps use HashMap with accountID as key and account value
	
	public BankServer(String filename) {
		accounts = new ArrayList<Account>();
		try {
			this.loadAcccounts(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		new InterestApplier().start();
	}
	
	private volatile boolean running = true;
	
	class InterestApplier extends Thread {
		synchronized void applyMonthlyInterest() {
			for (Account i : accounts) {
				if (i.getType() == "S") {
					SavingsAccount j = (SavingsAccount)i;
					j.monthlyInterest();
					System.out.println(j.getAccountId() +": "+ j.getBalance());
				}
			}
		}
		public void run() {
			while (running) {
				try {
					Thread.sleep(30*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				applyMonthlyInterest();
			}
		}
		
	}
	
	public void shutdown() {
		running = false;
	}
	
	private void loadAcccounts(String filename) throws FileNotFoundException, IOException {
		File file = new File(filename);
		System.out.println(file.toPath().toAbsolutePath());
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	String[] info = line.split("\\|");
		    	if (info[0].equals("S")) {
		    		if (info.length < 6) System.out.println("Bad line: "+ line);
		    		else {
		    			SavingsAccount savings = new SavingsAccount(info[1], info[2], info[3], 
		    				Double.parseDouble(info[4]), Double.parseDouble(info[5]));
		    			accounts.add(savings);
		    		}
		    	}
		    	else if (info[0].equals("C")) {
		    		if (info.length < 11) System.out.println("Bad line: "+ line);
		    		else {
		    			CheckingAccount checking = new CheckingAccount(info[1], info[2], info[3], 
		    				Double.parseDouble(info[4]), Double.parseDouble(info[5]),
		    				Integer.parseInt(info[6]), Integer.parseInt(info[7]), 
		    				Integer.parseInt(info[8]), Integer.parseInt(info[9]), 
		    				Integer.parseInt(info[10]));
		    			accounts.add(checking);
		    		}
		    	}
		    	else System.out.println("Bad line: "+ line);
		    }
		} catch (FileNotFoundException e) {
			
		}
	}
	
	public boolean verifyFunds(String cardNumber, double amount) {
		for (int i=0; i<accounts.size(); i++) {
			if (accounts.get(i).getAccountId().equals(cardNumber)) {
				if (accounts.get(i).getBalance() > amount) return true;
			}
		}
		return false;
	}
	
	public boolean verifyPin(String cardNumber, String pin) {
		for (int i=0; i<accounts.size(); i++) {
			if (accounts.get(i).getAccountId().equals(cardNumber)) {
				if (accounts.get(i).getPin().equals(pin)) return true;
			}
		}
		return false;
	}
	
	public void doWithdraw(String accNumber, int amount) {
		for (int i=0; i<accounts.size(); i++) {
			if (accounts.get(i).getAccountId().equals(accNumber)) {
				accounts.get(i).withdraw(amount);
				break;
			}
		}
	}
	
	public void doDeposit(String accNumber, double amount) {
		for (int i=0; i<accounts.size(); i++) {
			if (accounts.get(i).getAccountId().equals(accNumber)) {
				accounts.get(i).deposit(amount);
				break;
			}
		}
	}
	
	public double doQuery(String accNumber) {
		for (int i=0; i<accounts.size(); i++) {
			if (accounts.get(i).getAccountId().equals(accNumber)) {
				return accounts.get(i).getBalance();
			}
		}
		return 0;
	}
}

class Account {
	protected String accountId;
	protected String customerId;
	protected String pin;
	protected double balance;
	protected String type;
	
	public Account(String accountId, String customerId, String pin, double balance) {
		this.accountId = accountId;
		this.customerId = customerId;
		this.pin = pin;
		this.balance = balance;
	}
	
	public synchronized void deposit(double amount) {
		balance += amount;
	}
	
	public synchronized void withdraw(double amount) {
		balance -= amount;
	}
	
	public boolean equals(Account account) {
		if (this.accountId == account.getAccountId()) return true;
		else return false;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public synchronized double getBalance() {
		return balance;
	}

	public synchronized void setBalance(double balance) {
		this.balance = balance;
	}
	
	public String getType() {
		return type;
	}
}

class SavingsAccount extends Account {
	private double interestRate;
	
	public SavingsAccount(String accountId, String customerId, String pin, double balance,
			double interestRate) {
		super(accountId, customerId, pin, balance);
		this.interestRate = interestRate;
		type = "S";
	}
	public synchronized void monthlyInterest() {
		balance = balance*(1.0+(interestRate/100.0));
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
}

class CheckingAccount extends Account {
	private Date lastDepositDate;
	private double lastDepositAmount;
	
	@SuppressWarnings("deprecation")
	public CheckingAccount(String accountId, String customerId, String pin, double balance,
			double lastDepositAmount, int year, int month, int day, int hour, int minute) {
		super(accountId, customerId, pin, balance);
		this.lastDepositDate = new Date(year, month, day);
		lastDepositDate.setHours(hour);
		lastDepositDate.setMinutes(minute);
		this.lastDepositAmount = lastDepositAmount;
		type = "C";
	}
	
	public void deposit(double amount) {
		balance += amount;
		lastDepositAmount = amount;
		lastDepositDate = new Date();
	}
}