package bank.account.service;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import bank.account.model.AbstractOperation;
import bank.account.model.Account;
import bank.account.model.AccountId;
import bank.account.model.Client;
import bank.account.model.Money;
import bank.account.model.Statement;
import bank.account.repository.StatementRepository;
import bank.account.service.exception.AccountNotFound;
import bank.account.service.request.DepositCommand;
import bank.account.service.request.WithdrawalCommand;
import bank.account.validation.exception.ValidationException;

@ExtendWith(MockitoExtension.class)
public class TestOperationManager {

	private Client client;

	private AccountId accountId;

	private Account account;

	private Money amount;

	private Statement statement;

	@Mock
	private StatementRepository statementRepository;

	private OperationManager operationManager;

	@BeforeEach
	public void setUp() {
		client = new Client();
		accountId = new AccountId("test");
		account = new Account(accountId, client);
		amount = new Money("100");
		statement = new Statement(account, null);
		operationManager = new OperationManager(statementRepository);
	}

	@Test
	public void testOperationManager_withdrawalAccountNotFound() {
		final WithdrawalCommand command = new WithdrawalCommand(client, accountId, amount);

		Assertions.assertThrows(AccountNotFound.class, () -> {
			operationManager.withdrawal(command);
		});
	}

	@Test
	public void testOperationManager_withdrawal() {
		final WithdrawalCommand command = new WithdrawalCommand(client, accountId, amount);
		lenient().when(statementRepository.getCurrentStatement(accountId)).thenReturn(statement);

		operationManager.withdrawal(command);

		Mockito.verify(statementRepository, times(1)).store(statement);
		Assertions.assertEquals(1, statement.getOperations().size());
		Assertions.assertEquals(AbstractOperation.OperationType.WITHDRAWAL, statement.getOperations().get(0).getType());
		Assertions.assertEquals(amount, statement.getOperations().get(0).getAmount());
		// TODO check balance

	}

	@Test
	public void testOperationManager_witdrawal_clientAccessFailed() {
		final WithdrawalCommand command = new WithdrawalCommand(new Client(), accountId, amount);
		lenient().when(statementRepository.getCurrentStatement(accountId)).thenReturn(statement);

		Assertions.assertThrows(ValidationException.class, () -> {
			operationManager.withdrawal(command);
		});
	}

	@Test
	public void testOperationManager_depositAccountNotFound() {
		final DepositCommand command = new DepositCommand(client, accountId, amount);

		Assertions.assertThrows(AccountNotFound.class, () -> {
			operationManager.deposit(command);
		});
	}

	@Test
	public void testOperationManager_deposit() {
		final DepositCommand command = new DepositCommand(client, accountId, amount);
		lenient().when(statementRepository.getCurrentStatement(accountId)).thenReturn(statement);

		operationManager.deposit(command);

		Mockito.verify(statementRepository, times(1)).store(statement);
		Assertions.assertEquals(1, statement.getOperations().size());
		Assertions.assertEquals(AbstractOperation.OperationType.DEPOSIT, statement.getOperations().get(0).getType());
		Assertions.assertEquals(amount, statement.getOperations().get(0).getAmount());
		// TODO check balance
	}

	@Test
	public void testOperationManager_deposit_clientAccessFailed() {
		final DepositCommand command = new DepositCommand(new Client(), accountId, amount);
		lenient().when(statementRepository.getCurrentStatement(accountId)).thenReturn(statement);

		Assertions.assertThrows(ValidationException.class, () -> {
			operationManager.deposit(command);
		});
	}

}
