package bank.account.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestAccount {

	private AccountId id;
	private Client client;

	@BeforeEach
	public void setUp() {
		this.id = new AccountId("test");
		this.client = new Client();
	}

	@Test
	public void testAccount_New() {
		final Account account = new Account(id, client);

		Assertions.assertEquals(id, account.id());
		Assertions.assertEquals(client, account.client());
	}

	@Test
	public void testAccount_New_NoId() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			new Account(null, client);
		});
	}

	@Test
	public void testAccount_New_NoClient() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			new Account(id, null);
		});
	}
}
