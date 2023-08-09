package bank.account.model;

public class Withdrawal extends AbstractOperation {

	@Override
	public OperationType getType() {
		return OperationType.WITHDRAWAL;
	}

}
