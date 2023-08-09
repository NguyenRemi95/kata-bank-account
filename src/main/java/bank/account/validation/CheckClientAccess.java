package bank.account.validation;

import bank.account.model.Account;
import bank.account.model.Client;
import bank.account.validation.exception.ValidationException;

public interface CheckClientAccess {

	default boolean allowClientAccess(Client client, Account account) {
		return client != null && account != null && client.equals(account.client());
	}

	default void checkClientAccess(Client client, Account account) {
		if (!allowClientAccess(client, account))
			throw new ValidationException("client can not access account");
	}
}
