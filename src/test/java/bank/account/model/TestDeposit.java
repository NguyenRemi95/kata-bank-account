package bank.account.model;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestDeposit {

	private LocalDateTime date;

	@Mock
	private Statement statement;

	private Money amount;

	@BeforeEach
	public void setUp() {
		this.date = LocalDateTime.now();
		this.amount = Money.ZERO;
	}

	@Test
	public void testDeposit_New() {
		final Deposit operation = new Deposit(date, statement, amount);

		Assertions.assertEquals(date, operation.getDate());
		Assertions.assertEquals(statement, operation.getStatement());
		Assertions.assertEquals(amount, operation.getAmount());
	}

	@Test
	public void testDeposit_New_NoDate() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			new Deposit(null, statement, amount);
		});
	}

	@Test
	public void testDeposit_New_NoAmount() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			new Deposit(date, statement, null);
		});
	}

	@Test
	public void testDeposit_GetType() {
		final Deposit operation = new Deposit(date, statement, amount);

		Assertions.assertEquals(AbstractOperation.OperationType.DEPOSIT, operation.getType());
	}

	@Test
	public void testDeposit_SetStatement() {
		final Deposit operation = new Deposit(date, null, amount);

		operation.setStatement(statement);
		Assertions.assertEquals(statement, operation.getStatement());
	}

	@Test
	public void testDeposit_SetStatement_AlreadySet() {
		final Deposit deposit = new Deposit(date, statement, amount);

		Assertions.assertThrows(IllegalStateException.class, () -> {
			deposit.setStatement(statement);
		});
	}
}
