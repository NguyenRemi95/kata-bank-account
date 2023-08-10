package bank.account.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;

import java.math.BigDecimal;

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
	public void testOperationManager_withdrawal_AccountNotFound() {
		final WithdrawalCommand command = new WithdrawalCommand(client, accountId, amount);

		Assertions.assertThrows(AccountNotFound.class, () -> {
			operationManager.withdrawal(command);
		});
	}

	@Test
	public void testOperationManager_withdrawal_balanceCheckFailed() {
		final WithdrawalCommand command = new WithdrawalCommand(client, accountId, amount);
		lenient().when(statementRepository.getCurrentStatement(accountId)).thenReturn(statement);

		Assertions.assertThrows(ValidationException.class, () -> {
			operationManager.withdrawal(command);
		});
	}

	@Test
	public void testOperationManager_withdrawal() {
		final WithdrawalCommand command = new WithdrawalCommand(client, accountId, amount);
		final Statement statement_1 = new Statement(account, null, amount);
		lenient().when(statementRepository.getCurrentStatement(accountId)).thenReturn(statement_1);

		operationManager.withdrawal(command);

		Mockito.verify(statementRepository, times(1)).store(statement_1);
		Assertions.assertEquals(1, statement_1.getOperations().size());
		Assertions.assertEquals(AbstractOperation.OperationType.WITHDRAWAL,
				statement_1.getOperations().get(0).getType());
		Assertions.assertEquals(amount, statement_1.getOperations().get(0).getAmount());
		Assertions.assertEquals(Money.ZERO, statement_1.getBalance());
	}

	@Test
	public void testOperationManager_withdrawal_multiple() {
		final WithdrawalCommand command = new WithdrawalCommand(client, accountId, amount);
		final Money initial = new Money("20000000");
		final Statement statement_1 = new Statement(account, null, initial);
		lenient().when(statementRepository.getCurrentStatement(accountId)).thenReturn(statement_1);

		final int loop = 10;
		Money total = initial;
		for (int i = 0; i < loop; i++) {
			operationManager.withdrawal(command);
			total = total.subtract(amount);
		}

		Mockito.verify(statementRepository, times(loop)).store(statement_1);
		Assertions.assertEquals(loop, statement_1.getOperations().size());
		Assertions.assertEquals(AbstractOperation.OperationType.WITHDRAWAL,
				statement_1.getOperations().get(0).getType());
		Assertions.assertEquals(total.subtract(initial), statement_1.getAmount());
		Assertions.assertEquals(total, statement_1.getBalance());
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
		Assertions.assertEquals(amount, statement.getAmount());
		Assertions.assertEquals(amount, statement.getBalance());
	}

	@Test
	public void testOperationManager_deposit_multiple() {
		final DepositCommand command = new DepositCommand(client, accountId, amount);
		lenient().when(statementRepository.getCurrentStatement(accountId)).thenReturn(statement);
		final int loop = 10;
		Money total = Money.ZERO;
		for (int i = 0; i < loop; i++) {
			operationManager.deposit(command);
			total = total.add(amount);
		}

		Mockito.verify(statementRepository, times(loop)).store(statement);
		Assertions.assertEquals(loop, statement.getOperations().size());
		Assertions.assertEquals(AbstractOperation.OperationType.DEPOSIT, statement.getOperations().get(0).getType());
		Assertions.assertEquals(total, statement.getAmount());
		Assertions.assertEquals(total, statement.getBalance());
	}

	@Test
	public void testOperationManager_deposit_clientAccessFailed() {
		final DepositCommand command = new DepositCommand(new Client(), accountId, amount);
		lenient().when(statementRepository.getCurrentStatement(accountId)).thenReturn(statement);

		Assertions.assertThrows(ValidationException.class, () -> {
			operationManager.deposit(command);
		});
	}

	@Test
	public void testOperationManager_deposit_withdrawal_iterate_mixed() throws InterruptedException {
		final String[] amounts = new String[] { "100.1", "200.2", "300.3", "400.4", "999", "100.1", "200.2", "300.3",
				"400.4", "999" }; // sum = 4000

		final int loop = 10;
		final Money initialBalance = new Money(new BigDecimal(4000 * loop));
		statement = new Statement(account, null, initialBalance);

		lenient().when(statementRepository.getCurrentStatement(any())).thenReturn(statement);

		{
			Thread thread1 = new Thread(() -> {
				for (int i = 0; i < 10 * loop; i++) {
					final WithdrawalCommand command = new WithdrawalCommand(client, accountId,
							new Money(amounts[i % 10]));
					operationManager.withdrawal(command);
				}
			});

			Thread thread2 = new Thread(() -> {
				for (int i = 0; i < 10 * loop; i++) {
					final DepositCommand command = new DepositCommand(client, accountId,
							new Money(amounts[9 - (i % 10)]));
					operationManager.deposit(command);
				}
			});

			thread1.start();
			thread2.start();

			thread1.join();
			thread2.join();
		}
		Assertions.assertEquals(2 * 10 * loop, statement.getOperations().size());
		Assertions.assertEquals(initialBalance, statement.getBalance());

	}

}
