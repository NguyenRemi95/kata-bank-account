package bank.account.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {
	
	public static Money ZERO = new Money(BigDecimal.ZERO);
			
	private BigDecimal value;

	public Money(BigDecimal value) {
		this.value = Objects.requireNonNull(value);
	}
	
	
}
