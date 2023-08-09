package bank.account.model;

import java.util.Objects;

// AccountId is need to reference an Account without exposing Account structure
public record AccountId(String id) {
	public AccountId(String id) {
		this.id = Objects.requireNonNull(id);
	}
}
