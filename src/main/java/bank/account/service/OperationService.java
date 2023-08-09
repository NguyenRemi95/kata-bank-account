package bank.account.service;

import bank.account.service.request.DepositCommand;
import bank.account.service.request.WithdrawalCommand;

public interface OperationService {

	public void deposit(DepositCommand command);

	public void withdrawal(WithdrawalCommand command);
}
