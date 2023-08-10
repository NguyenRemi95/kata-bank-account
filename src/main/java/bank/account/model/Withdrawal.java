package bank.account.model;

import java.time.LocalDateTime;

public class Withdrawal extends AbstractOperation {

	public Withdrawal(LocalDateTime date, Statement statement, Money amount) {
		super(date, statement, amount);
	}

	public Withdrawal(Statement statement, Money amount) {
		this(LocalDateTime.now(), statement, amount);
	}

	public Withdrawal(Money amount) {
		this(null, amount);
	}

	@Override
	public OperationType getType() {
		return OperationType.WITHDRAWAL;
	}

	// we may need to make calculation without an instance
	public static Money apply(Money previousBalance, Money amount) {
		return previousBalance.subtract(amount);
	}

	@Override
	public Money computeBalance() {
		return apply(getPreviousBalance(), getAmount());
	}

}
