package bank.account.service.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import bank.account.model.AccountId;
import bank.account.model.Client;
import bank.account.model.Money;

@ExtendWith(MockitoExtension.class)
public class TestDepositCommand {

	private Client client;

	private AccountId accountId;

	private Money amount;

	@BeforeEach
	public void setUp() {
		this.client = new Client();
		this.accountId = new AccountId("test");
		this.amount = new Money("1000");
	}

	@Test
	public void testDepositCommand_New() {
		final DepositCommand command = new DepositCommand(client, accountId, amount);

		Assertions.assertEquals(client, command.client());
		Assertions.assertEquals(accountId, command.accountId());
		Assertions.assertEquals(amount, command.amount());
	}

	@Test
	public void testDepositCommand_New_NoClient() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			new DepositCommand(null, accountId, amount);
		});
	}

	@Test
	public void testDepositCommand_New_NoAccountId() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			new DepositCommand(client, null, amount);
		});
	}

	@Test
	public void testDepositCommand_New_NoAmount() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			new DepositCommand(client, accountId, null);
		});
	}

}
