package bank.account.model;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMoney {

	@Test
	public void testMoneyCreationString() {
		new Money("123.45");
		new Money("-123.45");
		new Money("12");
		new Money("-34");
		new Money("0.5");
		new Money("-0.89");

	}

	@Test
	public void testMoneyCreationBigDecimal() {
		new Money(new BigDecimal("135.79"));
	}

	@Test
	public void testMoneyCreationOutScale1() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Money("3.1416");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Money("-3.141");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Money("0.123");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Money("-0.123");
		});
	}

	@Test
	public void testMoneyEquals() {
		final String S_123_7 = "123.7";
		final Money amount_1 = new Money(S_123_7);
		final Money amount_2 = new Money(S_123_7);
		final Money amount_3 = new Money("123.7");
		final Money amount_4 = new Money("123.70");

		Assertions.assertEquals(amount_1, amount_1);
		Assertions.assertEquals(amount_1, amount_2);
		Assertions.assertEquals(amount_1, amount_3);
		Assertions.assertEquals(amount_1, amount_4);
		Assertions.assertEquals(amount_2, amount_3);
	}

	@Test
	public void testMoneyNotEquals() {
		final String S_123_7 = "123.7";
		final Money amount = new Money(S_123_7);
		final Money amount_1 = new Money("123.45");

		Assertions.assertNotEquals(amount, amount_1);
	}

	@Test
	public void testMoneyAdd() {
		final Money amount_1 = new Money("1000.1");
		final Money amount_2 = new Money("500.05");
		final Money sum = new Money("1500.15");

		Assertions.assertEquals(sum, amount_1.add(amount_2));
		Assertions.assertEquals(sum, amount_2.add(amount_1));
	}

	@Test
	public void testMoneySubtract() {
		final Money amount_1 = new Money("1000.1");
		final Money amount_2 = new Money("500.05");
		final Money result_1 = amount_2;
		final Money result_2 = new Money("-500.05");

		Assertions.assertEquals(result_1, amount_1.subtract(amount_2));
		Assertions.assertEquals(result_2, amount_2.subtract(amount_1));
	}

	@Test
	public void testMoneyGt() {
		final Money amount_1 = new Money("1000.1");
		final Money amount_2 = new Money("500.05");
		final Money amount_3 = new Money("-7");

		Assertions.assertTrue(amount_1.gt(amount_2));
		Assertions.assertFalse(amount_2.gt(amount_1));
		Assertions.assertFalse(amount_1.gt(amount_1));
		Assertions.assertTrue(amount_1.gt(amount_3));
		Assertions.assertTrue(amount_2.gt(amount_3));
		Assertions.assertFalse(amount_3.gt(amount_1));
	}

	@Test
	public void testMoneyGtZero() {
		final Money amount_1 = new Money("1000.1");
		final Money amount_2 = new Money("500.05");
		final Money amount_3 = new Money("-7");

		Assertions.assertTrue(amount_1.gtZero());
		Assertions.assertTrue(amount_2.gtZero());
		Assertions.assertFalse(amount_3.gtZero());
	}

	@Test
	public void testMoneyToString() {
		final Money amount_1 = new Money("1000.1");
		final Money amount_2 = new Money("500.05");
		final Money amount_3 = new Money("-7");
		final Money amount_4 = new Money("-1000007.90");
		final Money amount_5 = new Money("0000.01");
		final Money amount_6 = new Money("0.05");
		final Money amount_7 = new Money("-0.75");

		Assertions.assertEquals("1000.10", amount_1.toString());
		Assertions.assertEquals("500.05", amount_2.toString());
		Assertions.assertEquals("-7.00", amount_3.toString());
		Assertions.assertEquals("-1000007.90", amount_4.toString());
		Assertions.assertEquals("0.01", amount_5.toString());
		Assertions.assertEquals("0.05", amount_6.toString());
		Assertions.assertEquals("-0.75", amount_7.toString());
	}

}
