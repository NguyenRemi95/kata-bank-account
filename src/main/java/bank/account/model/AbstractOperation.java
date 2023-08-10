package bank.account.model;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class AbstractOperation {

	private LocalDateTime date;

	private Statement statement;

	private Money amount;

	// making transient field for cache
	private transient Money balance;

	private AbstractOperation previousOperation;

	protected AbstractOperation(LocalDateTime date, Statement statement, Money amount) {
		super();
		this.date = Objects.requireNonNull(date);
		// TODO see later if we can inforce not null
		this.statement = statement;
		this.amount = Objects.requireNonNull(amount);
		// we could check amount positive here but validation will be done earlier
	}

	public abstract OperationType getType();

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
		Objects.requireNonNull(statement);
		if (this.statement != null)
			throw new IllegalStateException("operation statement can only be set once");
		synchronized (statement) {
			this.statement = statement;
			this.previousOperation = statement.getLastOperation();
			// clear balance
			this.balance = null;
		}

	}

	public Money getAmount() {
		return amount;
	}

	public Money getBalance() {
		if (balance == null) {
			balance = computeBalance();
		}
		return balance;
	}

	protected Money getPreviousBalance() {
		return previousOperation != null ? previousOperation.getBalance()
				: (statement != null ? statement.getInitialBalance() : Money.ZERO);
	}

	protected abstract Money computeBalance();

}
