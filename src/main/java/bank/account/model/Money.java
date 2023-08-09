package bank.account.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@SuppressWarnings("rawtypes")
public class Money implements Comparable {

	public static final Money ZERO = new Money("0");

	private final BigDecimal amount;

	public Money(String val) {
		this.amount = new BigDecimal(val);
		if (amount.scale() > 2)
			throw new IllegalArgumentException("Money can't have scale > 2");
		amount.setScale(2);
	}

	public Money(BigDecimal value) {
		this(value.toString());
	}

	public Money add(Money money) {
		return new Money(amount.add(money.amount));
	}

	public Money subtract(Money money) {
		return new Money(amount.subtract(money.amount));
	}

	@Override
	public int compareTo(Object o) {
		return compareTo((Money) o);
	}

	public int compareTo(Money anotherMoney) {
		return this.amount.compareTo(anotherMoney.amount);
	}

	public boolean gt(Money money) {
		return compareTo(money) > 0;
	}

	public boolean gtZero() {
		return gt(Money.ZERO);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Money))
			return false;
		Money brother = (Money) o;
		return (amount.compareTo(brother.amount) == 0);
	}

	@Override
	public int hashCode() {
		return amount.hashCode();
	}

	/**
	 * Prints money with two decimal.
	 */
	@Override
	public String toString() {

		int realScale = amount.scale();
		if (realScale > 2)
			throw new RuntimeException("Scale of money object is > 2, should never happen, Money object is faulty.");

		DecimalFormat format = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
		format.setGroupingUsed(false);

		return format.format(amount);

	}

}
