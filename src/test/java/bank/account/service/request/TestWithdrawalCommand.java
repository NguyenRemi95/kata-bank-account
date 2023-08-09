package bank.account.service.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import bank.account.model.AccountId;
import bank.account.model.Client;
import bank.account.model.Money;
import bank.account.validation.exception.ValidationException;

@ExtendWith(MockitoExtension.class)
public class TestWithdrawalCommand {

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
	public void testWithdrawalCommand_New() {
		final WithdrawalCommand command = new WithdrawalCommand(client, accountId, amount);

		Assertions.assertEquals(client, command.client());
		Assertions.assertEquals(accountId, command.accountId());
		Assertions.assertEquals(amount, command.amount());
	}

	@Test
	public void testWithdrawalCommand_New_NoClient() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			new WithdrawalCommand(null, accountId, amount);
		});
	}

	@Test
	public void testWithdrawalCommand_New_NoAccountId() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			new WithdrawalCommand(client, null, amount);
		});
	}

	@Test
	public void testWithdrawalCommand_New_NoAmount() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			new WithdrawalCommand(client, accountId, null);
		});
	}

	@Test
	public void testWithdrawalCommand_New_ZeroAmount() {
		Assertions.assertThrows(ValidationException.class, () -> {
			new WithdrawalCommand(client, accountId, Money.ZERO);
		});
	}

	@Test
	public void testWithdrawalCommand_New_NegativeAmount() {
		Assertions.assertThrows(ValidationException.class, () -> {
			new WithdrawalCommand(client, accountId, new Money("-55.55"));
		});
	}

}
