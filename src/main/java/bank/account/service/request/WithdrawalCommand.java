package bank.account.service.request;

import java.util.Objects;

import bank.account.model.AccountId;
import bank.account.model.Client;
import bank.account.model.Money;

public record WithdrawalCommand(Client client, AccountId accountId, Money amount) {
	public WithdrawalCommand(Client client, AccountId accountId, Money amount) {
		this.client = Objects.requireNonNull(client);
		this.accountId = Objects.requireNonNull(accountId);
		this.amount = Objects.requireNonNull(amount);
	}
}
