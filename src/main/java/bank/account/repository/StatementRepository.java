package bank.account.repository;

import java.time.LocalDateTime;

import bank.account.model.AccountId;
import bank.account.model.Statement;

public interface StatementRepository {

	Statement getCurrentStatement(AccountId id);

	Statement getPastStatement(AccountId id, LocalDateTime date);

	// store operation by cascade
	void store(Statement statement);

}
