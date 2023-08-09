package bank.account.service.request;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import bank.account.model.AccountId;
import bank.account.model.Client;

@ExtendWith(MockitoExtension.class)
public class TestPrintStatementQuery {

	private Client client;

	private AccountId accountId;

	private LocalDateTime date;

	@BeforeEach
	public void setUp() {
		this.client = new Client();
		this.accountId = new AccountId("test");
		this.date = LocalDateTime.now();
	}

	@Test
	public void testPrintStatementQuery_New() {
		final PrintStatementQuery query = new PrintStatementQuery(client, accountId, date);

		Assertions.assertEquals(client, query.client());
		Assertions.assertEquals(accountId, query.accountId());
		Assertions.assertEquals(date, query.date());
	}

	@Test
	public void testPrintStatementQuery_New_NoClient() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			new PrintStatementQuery(null, accountId, date);
		});
	}

	@Test
	public void testPrintStatementQuery_New_NoAccountId() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			new PrintStatementQuery(client, null, date);
		});
	}

	@Test
	public void testPrintStatementQuery_New_NoDate() {
		final PrintStatementQuery query = new PrintStatementQuery(client, accountId, null);

		Assertions.assertEquals(client, query.client());
		Assertions.assertEquals(accountId, query.accountId());
		Assertions.assertEquals(null, query.date());
	}

}
