package bank.account.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bank.account.model.Money;
import bank.account.validation.exception.ValidationException;

public class TestCheckPositiveMoney {

	private CheckPositiveMoney check;

	@BeforeEach
	public void setUp() {
		check = new CheckPositiveMoney() {
		};
	}

	@Test
	void testCheckPositiveMoney_isPositiveMoney() {
		Assertions.assertFalse(check.isPositiveMoney(null));
		Assertions.assertTrue(check.isPositiveMoney(new Money("100")));
		Assertions.assertFalse(check.isPositiveMoney(Money.ZERO));
		Assertions.assertFalse(check.isPositiveMoney(new Money("-100")));
	}

	@Test
	void testCheckPositiveMoney_checkPositiveMoney() {
		check.checkPositiveMoney(new Money("100"));
	}

	@Test
	void testCheckPositiveMoney_checkPositiveMoney_Null() {
		Assertions.assertThrows(ValidationException.class, () -> {
			check.checkPositiveMoney(null);
		});
	}

	@Test
	void testCheckPositiveMoney_checkPositiveMoney_ZERO() {
		Assertions.assertThrows(ValidationException.class, () -> {
			check.checkPositiveMoney(Money.ZERO);
		});
	}

	@Test
	void testCheckPositiveMoney_checkPositiveMoney_Neg() {
		Assertions.assertThrows(ValidationException.class, () -> {
			check.checkPositiveMoney(new Money("-100"));
		});
	}

}
