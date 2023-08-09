package bank.account.validation;

import bank.account.model.Money;
import bank.account.validation.exception.ValidationException;

public interface CheckPositiveMoney {

	default boolean isPositiveMoney(Money amount) {
		return amount != null && amount.gtZero();
	}

	default void checkPositiveMoney(Money amount) {
		if (!isPositiveMoney(amount))
			throw new ValidationException("amout must be positive, found : " + amount);
	}

}
