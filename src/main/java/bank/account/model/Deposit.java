package bank.account.model;

public class Deposit extends AbstractOperation{

	@Override
	public OperationType getType() {
		return OperationType.DEPOSIT;
	}

}
