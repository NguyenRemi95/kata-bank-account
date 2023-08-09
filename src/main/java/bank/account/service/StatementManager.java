package bank.account.service;

import java.util.Objects;

import bank.account.exception.NotYetImplementedException;
import bank.account.model.Client;
import bank.account.model.Statement;
import bank.account.repository.StatementRepository;
import bank.account.service.exception.StatementNotFound;
import bank.account.service.request.PrintStatementQuery;
import bank.account.validation.CheckClientAccess;

public class StatementManager implements StatementService, CheckClientAccess {

	private StatementRepository statementRepository;

	public StatementManager(StatementRepository statementRepository) {
		this.statementRepository = Objects.requireNonNull(statementRepository);
	}

	@Override
	public String printStatement(PrintStatementQuery query) {
		// assemble context
		final Client client = query.client();
		final Statement statement = query.date() == null ? statementRepository.getCurrentStatement(query.accountId())
				: statementRepository.getPastStatement(query.accountId(), query.date());

		if (statement == null)
			throw new StatementNotFound(
					"No statement found for accountId " + query.accountId().id() + " and date " + query.date());

		{ // validate
			checkClientAccess(client, statement.getAccount());
		}
		{ // process
			throw new NotYetImplementedException();
		}

	}

}
