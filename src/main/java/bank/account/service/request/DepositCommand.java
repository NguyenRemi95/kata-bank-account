package bank.account.service.request;

import java.util.Objects;

import bank.account.model.AccountId;
import bank.account.model.Client;
import bank.account.model.Money;
import bank.account.validation.CheckPositiveMoney;

public record DepositCommand(Client client, AccountId accountId, Money amount) implements CheckPositiveMoney {
	public DepositCommand(Client client, AccountId accountId, Money amount) {
		this.client = Objects.requireNonNull(client);
		this.accountId = Objects.requireNonNull(accountId);
		this.amount = Objects.requireNonNull(amount);
		validate();
	}

	public void validate() {
		checkPositiveMoney(amount);
	}
}
