package bank.account.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestStatement {

	private AccountId id;

	private Client client;

	private Account account;

	private LocalDateTime date;

	private List<AbstractOperation> operations;

	@Mock
	private AbstractOperation operation_1, operation_2, operation_3;

	@BeforeEach
	public void setUp() {
		this.id = new AccountId("test");
		this.client = new Client();
		this.account = new Account(id, client);
		this.date = LocalDateTime.now();
		this.operations = new ArrayList<>();
	}

	@Test
	public void testStatement_New3() {

		operations.add(operation_1);
		operations.add(operation_2);
		operations.add(operation_3);

		final Statement statement = new Statement(account, date, operations);

		Assertions.assertEquals(account, statement.getAccount());
		Assertions.assertEquals(date, statement.getDate());
		Assertions.assertEquals(operations.size(), statement.getOperations().size());
		Assertions.assertEquals(operations.get(0), statement.getOperations().get(0));
		Assertions.assertEquals(operations.get(1), statement.getOperations().get(1));
		Assertions.assertEquals(operations.get(2), statement.getOperations().get(2));
	}

	@Test
	public void testStatement_New2() {

		final Statement statement = new Statement(account, date);

		Assertions.assertEquals(account, statement.getAccount());
		Assertions.assertEquals(date, statement.getDate());
		Assertions.assertTrue(statement.getOperations().isEmpty());
	}

	@Test
	public void testStatement_New_NoDate() {
		new Statement(account, null);
	}

	@Test
	public void testStatement_New_NoAccount() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			new Statement(null, date);
		});
	}

	@Test
	public void testStatement_AddOperation() {

		final Statement statement = new Statement(account, null);

		statement.addOperation(operation_1);

		Assertions.assertEquals(1, statement.getOperations().size());
		Assertions.assertEquals(operation_1, statement.getOperations().get(0));
	}

	@Test
	public void testStatement_AddOperation_WhenIsPublished() {

		final Statement statement = new Statement(account, date);

		Assertions.assertThrows(IllegalStateException.class, () -> {
			statement.addOperation(operation_1);
		});

	}

}
