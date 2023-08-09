package bank.account.service.request;

import java.time.LocalDateTime;
import java.util.Objects;

import bank.account.model.AccountId;
import bank.account.model.Client;

public record PrintStatementQuery(Client client, AccountId accountId, LocalDateTime date) {
	public PrintStatementQuery(Client client, AccountId accountId, LocalDateTime date) {
		this.client = Objects.requireNonNull(client);
		this.accountId = Objects.requireNonNull(accountId);
		this.date = date;
	}
}