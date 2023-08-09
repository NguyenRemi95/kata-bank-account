package bank.account.model;

import java.time.LocalDateTime;

public class Withdrawal extends AbstractOperation {

	protected Withdrawal(LocalDateTime date, Statement statement, Money amount) {
		super(date, statement, amount);
	}

	@Override
	public OperationType getType() {
		return OperationType.WITHDRAWAL;
	}

}
