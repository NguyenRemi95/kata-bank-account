package bank.account.model;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestWithdrawal {

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
	public void testWithdrawal_New() {
		final Withdrawal operation = new Withdrawal(date, statement, amount);

		Assertions.assertEquals(date, operation.getDate());
		Assertions.assertEquals(statement, operation.getStatement());
		Assertions.assertEquals(amount, operation.getAmount());
	}

	@Test
	public void testWithdrawal_New_NoDate() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			new Withdrawal(null, statement, amount);
		});
	}

	@Test
	public void testWithdrawal_New_NoAmount() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			new Withdrawal(date, statement, null);
		});
	}

	@Test
	public void testWithdrawal_GetType() {
		final Withdrawal operation = new Withdrawal(date, statement, amount);

		Assertions.assertEquals(AbstractOperation.OperationType.WITHDRAWAL, operation.getType());
	}

	@Test
	public void testWithdrawal_SetStatement() {
		final Withdrawal operation = new Withdrawal(date, null, amount);

		operation.setStatement(statement);
		Assertions.assertEquals(statement, operation.getStatement());
	}

	@Test
	public void testWithdrawal_SetStatement_AlreadySet() {
		final Withdrawal Withdrawal = new Withdrawal(date, statement, amount);

		Assertions.assertThrows(IllegalStateException.class, () -> {
			Withdrawal.setStatement(statement);
		});
	}
}
