package bank.account.service;

import static org.mockito.Mockito.lenient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bank.account.model.AbstractOperation;
import bank.account.model.AbstractOperation.OperationType;
import bank.account.model.Account;
import bank.account.model.AccountId;
import bank.account.model.Client;
import bank.account.model.Money;
import bank.account.model.Statement;
import bank.account.repository.StatementRepository;
import bank.account.service.exception.StatementNotFound;
import bank.account.service.request.DepositCommand;
import bank.account.service.request.PrintStatementQuery;
import bank.account.service.request.WithdrawalCommand;
import bank.account.validation.exception.ValidationException;

@ExtendWith(MockitoExtension.class)
public class TestStatementManager {

	private Client client;

	private AccountId accountId;

	private Account account;

	private LocalDateTime date;

	@Mock
	private Statement statement;

	private List<AbstractOperation> operations;

	@Mock
	private StatementRepository statementRepository;

	@Mock
	private AbstractOperation operation_1, operation_2, operation_3;

	private StatementManager statementManager;

	@BeforeEach
	public void setUp() {
		client = new Client();
		accountId = new AccountId("test");
		account = new Account(accountId, client);
		date = LocalDateTime.now();
		operations = new ArrayList<>();
		statementManager = new StatementManager(statementRepository);
	}

	@Test
	public void testStatementManager_printStatement_StatementNotFound() {
		final PrintStatementQuery query = new PrintStatementQuery(client, accountId, date);

		Assertions.assertThrows(StatementNotFound.class, () -> {
			statementManager.printStatement(query);
		});
	}

	@Test
	public void testStatementManager_printStatement_NoDate_StatementNotFound() {
		final PrintStatementQuery query = new PrintStatementQuery(client, accountId, null);

		Assertions.assertThrows(StatementNotFound.class, () -> {
			statementManager.printStatement(query);
		});
	}

	@Test
	public void testStatementManager_printStatement_ClientAccessFailed() {
		final PrintStatementQuery query = new PrintStatementQuery(new Client(), accountId, date);
		lenient().when(statement.isPublished()).thenReturn(true);
		lenient().when(statement.getDate()).thenReturn(date);
		lenient().when(statement.getAccount()).thenReturn(account);
		lenient().when(statementRepository.getPastStatement(accountId, date)).thenReturn(statement);

		Assertions.assertThrows(ValidationException.class, () -> {
			statementManager.printStatement(query);
		});
	}

	@Test
	public void testStatementManager_printStatement_NoDate_ClientAccessFailed() {
		final PrintStatementQuery query = new PrintStatementQuery(new Client(), accountId, null);
		lenient().when(statement.getAccount()).thenReturn(account);
		lenient().when(statementRepository.getCurrentStatement(accountId)).thenReturn(statement);

		Assertions.assertThrows(ValidationException.class, () -> {
			statementManager.printStatement(query);
		});
	}

	@Test
	public void testStatementManager_printStatement_Empty() {
		final PrintStatementQuery query = new PrintStatementQuery(client, accountId, null);
		lenient().when(statement.getAccount()).thenReturn(account);
		lenient().when(statementRepository.getCurrentStatement(accountId)).thenReturn(statement);
		lenient().when(statement.getInitialBalance()).thenReturn(Money.ZERO);
		lenient().when(statement.getBalance()).thenReturn(Money.ZERO);

		String result = statementManager.printStatement(query);
		String expected = "***** ACCOUNT : test ***** DATE : PENDING *****\n"
				+ "|         DATE     	| OPERATION	| AMOUNT	| BALANCE  |\n"
				+ "|                 	|         	|        	| 0.00 |\n"
				+ "|                	| TOTAL    	|         	| 0.00 |";
		Assertions.assertEquals(expected, result);

	}

	@Test
	public void testStatementManager_printStatement_Published_Empty() {

		lenient().when(statementRepository.getPastStatement(accountId, date)).thenReturn(statement);
		lenient().when(statement.getAccount()).thenReturn(account);
		lenient().when(statement.getInitialBalance()).thenReturn(Money.ZERO);
		lenient().when(statement.getBalance()).thenReturn(Money.ZERO);
		lenient().when(statement.getDate()).thenReturn(date);

		final PrintStatementQuery query = new PrintStatementQuery(client, accountId, date);
		final String result = statementManager.printStatement(query);

		final String template = "***** ACCOUNT : test ***** DATE : %s *****\n"
				+ "|         DATE     	| OPERATION	| AMOUNT	| BALANCE  |\n"
				+ "|                 	|         	|        	| 0.00 |\n"
				+ "|                	| TOTAL    	|         	| 0.00 |";

		final String expected = String.format(template, StatementManager.FORMATER.format(date));

		Assertions.assertEquals(expected, result);

	}

	@Test
	public void testStatementManager_printStatement_OneOperation() {

		final Money value = new Money("100");
		lenient().when(operation_1.getDate()).thenReturn(date);
		lenient().when(operation_1.getType()).thenReturn(OperationType.DEPOSIT);
		lenient().when(operation_1.getAmount()).thenReturn(value);
		lenient().when(operation_1.getBalance()).thenReturn(value);

		lenient().when(statementRepository.getPastStatement(accountId, date)).thenReturn(statement);
		lenient().when(statement.getAccount()).thenReturn(account);
		lenient().when(statement.getInitialBalance()).thenReturn(Money.ZERO);
		lenient().when(statement.getBalance()).thenReturn(value);
		lenient().when(statement.getDate()).thenReturn(date);
		lenient().when(statement.getOperations()).thenReturn(operations);

		operations.add(operation_1);

		final PrintStatementQuery query = new PrintStatementQuery(client, accountId, date);
		final String result = statementManager.printStatement(query);

		final String template = "***** ACCOUNT : test ***** DATE : %s *****\n"
				+ "|         DATE     	| OPERATION	| AMOUNT	| BALANCE  |\n"
				+ "|                 	|         	|        	| 0.00 |\n" + "| %s\t| DEPOSIT\t| 100.00\t| 100.00 |\n"
				+ "|                	| TOTAL    	|         	| 100.00 |";
		final String sdate = StatementManager.FORMATER.format(date);
		final String expected = String.format(template, sdate, sdate);

		Assertions.assertEquals(expected, result);

	}

	@Test
	public void testStatementManager_printStatement_SomeOperations() {

		final Money value = new Money("100");
		Money balance = Money.ZERO;
		lenient().when(operation_1.getDate()).thenReturn(date);
		lenient().when(operation_1.getType()).thenReturn(OperationType.DEPOSIT);
		lenient().when(operation_1.getAmount()).thenReturn(value);
		lenient().when(operation_1.getBalance()).thenReturn(balance = balance.add(value));

		lenient().when(operation_2.getDate()).thenReturn(date);
		lenient().when(operation_2.getType()).thenReturn(OperationType.DEPOSIT);
		lenient().when(operation_2.getAmount()).thenReturn(value);
		lenient().when(operation_2.getBalance()).thenReturn(balance = balance.add(value));

		lenient().when(operation_3.getDate()).thenReturn(date);
		lenient().when(operation_3.getType()).thenReturn(OperationType.WITHDRAWAL);
		lenient().when(operation_3.getAmount()).thenReturn(value);
		lenient().when(operation_3.getBalance()).thenReturn(balance = balance.subtract(value));

		operations.add(operation_1);
		operations.add(operation_2);
		operations.add(operation_3);

		lenient().when(statementRepository.getPastStatement(accountId, date)).thenReturn(statement);
		lenient().when(statement.getAccount()).thenReturn(account);
		lenient().when(statement.getInitialBalance()).thenReturn(Money.ZERO);
		lenient().when(statement.getBalance()).thenReturn(balance);
		lenient().when(statement.getDate()).thenReturn(date);
		lenient().when(statement.getOperations()).thenReturn(operations);

		final PrintStatementQuery query = new PrintStatementQuery(client, accountId, date);
		final String result = statementManager.printStatement(query);

		final String template = "***** ACCOUNT : test ***** DATE : %s *****\n"
				+ "|         DATE     	| OPERATION	| AMOUNT	| BALANCE  |\n"
				+ "|                 	|         	|        	| 0.00 |\n" //
				+ "| %s\t| DEPOSIT\t| 100.00\t| 100.00 |\n" //
				+ "| %s\t| DEPOSIT\t| 100.00\t| 200.00 |\n" //
				+ "| %s\t| WITHDRAWAL\t| 100.00\t| 100.00 |\n" //
				+ "|                	| TOTAL    	|         	| 100.00 |";

		final String sdate = StatementManager.FORMATER.format(date);
		final String expected = String.format(template, sdate, sdate, sdate, sdate);

		Assertions.assertEquals(expected, result);

	}

	@Test
	public void testStatementManager_printStatement_GeneratedComplexStatement() throws InterruptedException {

		final String[] amounts = new String[] { "100.1", "200.2", "300.3", "400.4", "999", "100.1", "200.2", "300.3",
				"400.4", "999" }; // sum = 4000
		// increase the loop for bigger statement
		final int loop = 100;
		final Money initialBalance = new Money(new BigDecimal(4000 * loop));
		final Statement statement = new Statement(account, null, initialBalance);

		lenient().when(statementRepository.getCurrentStatement(accountId)).thenReturn(statement);
		{ // build a complex statement
			final OperationManager operationManager = new OperationManager(statementRepository);

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

		String result = statementManager.printStatement(new PrintStatementQuery(client, accountId, null));

		// checking lines count
		Assertions.assertEquals((10 * loop * 2) + 4, result.split("\n").length);

		// System.out.println(result);

	}
}
