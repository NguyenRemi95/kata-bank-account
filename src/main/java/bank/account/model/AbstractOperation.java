package bank.account.model;

import java.time.LocalDateTime;
import java.util.Objects;

import bank.account.exception.NotYetImplementedException;

public abstract class AbstractOperation {
	
	private LocalDateTime date;
	
	private Statement statement;
	
	private Money amount;
	
	protected AbstractOperation(LocalDateTime date, Statement statement, Money amount) {
		super();
		this.date = Objects.requireNonNull(date);
		//TODO see later if we can inforce not null
		this.statement = statement;
		this.amount = Objects.requireNonNull(amount);
	}

	public abstract OperationType  getType();
	
	public enum OperationType {
		DEPOSIT, WITHDRAWAL
	}

	public LocalDateTime getDate() {
		return date;
	}

	public Statement getStatement() {
		return statement;
	}
	
	protected void setStatement(Statement statement) {
		if (this.statement != null )
			throw new IllegalStateException("operation statement can only be set once");
		this.statement = statement;
	}

	public Money getAmount() {
		return amount;
	}

	public Money getBalance() {
		throw new NotYetImplementedException();
	}
	
}
