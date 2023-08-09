package bank.account.service;

import static org.mockito.Mockito.lenient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bank.account.exception.NotYetImplementedException;
import bank.account.model.AbstractOperation;
import bank.account.model.Account;
import bank.account.model.AccountId;
import bank.account.model.Client;
import bank.account.model.Statement;
import bank.account.repository.StatementRepository;
import bank.account.service.exception.StatementNotFound;
import bank.account.service.request.PrintStatementQuery;
import bank.account.validation.exception.ValidationException;

@ExtendWith(MockitoExtension.class)
public class TestStatementManager {

	private Client client;

	private AccountId accountId;

	private Account account;

	private LocalDateTime date;

	private Statement statement;

	private List<AbstractOperation> operations;

	@Mock
	private StatementRepository statementRepository;

	private StatementManager statementManager;

	@BeforeEach
	public void setUp() {
		client = new Client();
		accountId = new AccountId("test");
		account = new Account(accountId, client);
		date = LocalDateTime.now();
		statement = new Statement(account, null);
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
		statement.publish();
		lenient().when(statementRepository.getPastStatement(accountId, date)).thenReturn(statement);

		Assertions.assertThrows(ValidationException.class, () -> {
			statementManager.printStatement(query);
		});
	}

	@Test
	public void testStatementManager_printStatement_NoDate_ClientAccessFailed() {
		final PrintStatementQuery query = new PrintStatementQuery(new Client(), accountId, null);
		lenient().when(statementRepository.getCurrentStatement(accountId)).thenReturn(statement);

		Assertions.assertThrows(ValidationException.class, () -> {
			statementManager.printStatement(query);
		});
	}

	@Test
	public void testStatementManager_printStatement() {
		final PrintStatementQuery query = new PrintStatementQuery(client, accountId, null);
		lenient().when(statementRepository.getCurrentStatement(accountId)).thenReturn(statement);

		// TODO
		Assertions.assertThrows(NotYetImplementedException.class, () -> {
			statementManager.printStatement(query);
		});
	}
}
