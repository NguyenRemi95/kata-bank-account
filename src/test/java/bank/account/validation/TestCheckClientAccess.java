package bank.account.validation;

import static org.mockito.Mockito.lenient;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bank.account.model.Account;
import bank.account.model.Client;
import bank.account.validation.exception.ValidationException;

@ExtendWith(MockitoExtension.class)
public class TestCheckClientAccess {

	private CheckClientAccess check;

	private Client client;

	@Mock
	private Account account;

	@BeforeEach
	public void setUp() {
		check = new CheckClientAccess() {
		};

		client = new Client();
	}

	@Test
	void testCheckClientAcces_allow() {

		Assertions.assertFalse(check.allowClientAccess(client, null));
		Assertions.assertFalse(check.allowClientAccess(null, account));

		lenient().when(account.client()).thenReturn(client);
		Assertions.assertTrue(check.allowClientAccess(client, account));

		lenient().when(account.client()).thenReturn(new Client());
		Assertions.assertFalse(check.allowClientAccess(client, account));
	}

	@Test
	void testCheckClientAcces_check() {

		lenient().when(account.client()).thenReturn(client);
		check.checkClientAccess(client, account);
	}

	@Test
	void testCheckClientAcces_checkClientNull() {
		Assertions.assertThrows(ValidationException.class, () -> {
			check.checkClientAccess(null, account);
		});
	}

	@Test
	void testCheckClientAcces_checkAccountNull() {
		Assertions.assertThrows(ValidationException.class, () -> {
			check.checkClientAccess(client, null);
		});
	}

	@Test
	void testCheckClientAcces_check_Failed() {
		lenient().when(account.client()).thenReturn(new Client());
		Assertions.assertThrows(ValidationException.class, () -> {
			check.checkClientAccess(client, null);
		});
	}

}
