package bank.account.model;

import java.time.LocalDateTime;

public class Deposit extends AbstractOperation {

	public Deposit(LocalDateTime date, Statement statement, Money amount) {
		super(date, statement, amount);
	}

	public Deposit(Statement statement, Money amount) {
		this(LocalDateTime.now(), statement, amount);
	}

	public Deposit(Money amount) {
		this(null, amount);
	}

	@Override
	public OperationType getType() {
		return OperationType.DEPOSIT;
	}

	// we may need to make calculation without an instance
	public static Money apply(Money previousBalance, Money amount) {
		return previousBalance.add(amount);
	}

	@Override
	public Money computeBalance() {
		return apply(getPreviousBalance(), getAmount());
	}

}
