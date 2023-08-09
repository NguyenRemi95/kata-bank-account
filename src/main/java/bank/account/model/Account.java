package bank.account.model;

import java.util.Objects;

public record Account(AccountId id, Client client) {

	// possible : Statement currentStatement;

	// possible : List<Statement> statements;

	public Account(AccountId id, Client client) {
		this.id = Objects.requireNonNull(id);
		this.client = Objects.requireNonNull(client);
	}
}
