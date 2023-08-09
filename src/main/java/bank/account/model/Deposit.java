package bank.account.model;

import java.time.LocalDateTime;

public class Deposit extends AbstractOperation{

	protected Deposit(LocalDateTime date, Statement statement, Money amount) {
		super(date, statement, amount);
	}

	@Override
	public OperationType getType() {
		return OperationType.DEPOSIT;
	}

}
