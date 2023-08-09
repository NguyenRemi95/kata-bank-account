package bank.account.service;

import java.util.Objects;

import bank.account.model.Client;
import bank.account.model.Deposit;
import bank.account.model.Money;
import bank.account.model.Statement;
import bank.account.model.Withdrawal;
import bank.account.repository.StatementRepository;
import bank.account.service.exception.AccountNotFound;
import bank.account.service.request.DepositCommand;
import bank.account.service.request.WithdrawalCommand;
import bank.account.validation.CheckClientAccess;
import bank.account.validation.CheckPositiveMoney;

public class OperationManager implements OperationService, CheckPositiveMoney, CheckClientAccess {

	private StatementRepository statementRepository;

	public OperationManager(StatementRepository statementRepository) {
		this.statementRepository = Objects.requireNonNull(statementRepository);
	}

	@Override
	public void deposit(DepositCommand command) {
		// assemble context
		final Client client = command.client();
		final Statement statement = statementRepository.getCurrentStatement(command.accountId());
		if (statement == null)
			throw new AccountNotFound("No statement found for accountId " + command.accountId().id());
		final Money amount = command.amount();
		{ // validate
			checkPositiveMoney(amount);
			checkClientAccess(client, statement.getAccount());
		}
		{ // process
			final Deposit operation = new Deposit(amount);
			statement.addOperation(operation);
			statementRepository.store(statement);
		}
	}

	@Override
	public void withdrawal(WithdrawalCommand command) {
		// assemble context
		final Client client = command.client();
		final Statement statement = statementRepository.getCurrentStatement(command.accountId());
		if (statement == null)
			throw new AccountNotFound("No statement found for accountId " + command.accountId().id());
		final Money amount = command.amount();
		{ // validate
			checkPositiveMoney(amount);
			checkClientAccess(client, statement.getAccount());
			// TODO add balanceCheck
		}
		{ // process
			final Withdrawal operation = new Withdrawal(amount);
			statement.addOperation(operation);
			statementRepository.store(statement);
		}
	}

}
