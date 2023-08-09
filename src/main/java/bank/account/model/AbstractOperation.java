package bank.account.model;

import java.time.LocalDateTime;

public abstract class AbstractOperation {
	
	private LocalDateTime date;
	
	private Money amount;
	
	private Money balance;
	
	public abstract OperationType  getType();
	
	public enum OperationType {
		DEPOSIT, WITHDRAWAL
	}
	
}
