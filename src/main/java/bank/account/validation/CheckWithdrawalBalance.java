package bank.account.validation;

import bank.account.model.Money;
import bank.account.model.Statement;
import bank.account.model.Withdrawal;
import bank.account.validation.exception.ValidationException;

public interface CheckWithdrawalBalance {

	default boolean hasEnoughBalance(Statement statement, Money amount) {
		if (statement == null || amount == null)
			return false;
		Money prevision = Withdrawal.apply(statement.getBalance(), amount);
		return prevision.gtZero() || prevision.equals(Money.ZERO);
	}

	default void checkBalance(Statement statement, Money amount) {
		if (!hasEnoughBalance(statement, amount))
			throw new ValidationException("Balance is too low");
	}
}
