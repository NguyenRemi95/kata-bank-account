package bank.account.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import bank.account.model.AbstractOperation;
import bank.account.model.Account;
import bank.account.model.AccountId;
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
			StringBuilder builder = new StringBuilder();
			printStatement(builder, statement);
			return builder.toString();
		}

	}

	protected static DateTimeFormatter FORMATER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	static private final String LINE_SEPARATOR = "\n";

	static private final String STATEMENT_HEADER_1 = "***** ACCOUNT : ";

	static private final String STATEMENT_HEADER_2 = " ***** DATE : ";

	static private final String STATEMENT_HEADER_3 = " *****";

	static private final String TABLE_HEADER = "|         DATE     \t| OPERATION\t| AMOUNT\t| BALANCE ";

	static private final String TABLE_START = "|                 \t|         \t|        \t| ";

	static private final String TABLE_END = "|                \t| TOTAL    \t|         \t| ";

	static private final String TABLE_LINE_START = "| ";

	static private final String TABLE_LINE_SEPARATOR = "\t| ";

	static private final String TABLE_LINE_END = " |";

	private StringBuilder printStatement(StringBuilder builder, Statement statement) {

		appendHeader(builder, statement).append(LINE_SEPARATOR);
		builder.append(TABLE_HEADER).append(TABLE_LINE_END).append(LINE_SEPARATOR);
		builder.append(TABLE_START).append(statement.getInitialBalance()).append(TABLE_LINE_END).append(LINE_SEPARATOR);
		for (AbstractOperation operation : statement.getOperations()) {
			append(builder, operation).append(LINE_SEPARATOR);
		}
		builder.append(TABLE_END).append(statement.getBalance()).append(TABLE_LINE_END);
		return builder;

	}

	private StringBuilder appendHeader(StringBuilder builder, Statement statement) {
		builder.append(STATEMENT_HEADER_1);
		append(builder, statement.getAccount());
		builder.append(STATEMENT_HEADER_2);
		if (statement.getDate() == null) {
			builder.append("PENDING");
		} else {
			append(builder, statement.getDate());
		}
		builder.append(STATEMENT_HEADER_3);
		return builder;
	}

	private StringBuilder append(StringBuilder builder, LocalDateTime date) {

		if (date != null)
			builder.append(FORMATER.format(date));
		return builder;
	}

	private StringBuilder append(StringBuilder builder, AccountId accountId) {
		builder.append(accountId.id());
		return builder;

	}

	private StringBuilder append(StringBuilder builder, Account account) {
		append(builder, account.id());
		return builder;

	}

	private StringBuilder append(StringBuilder builder, AbstractOperation operation) {
		builder.append(TABLE_LINE_START);
		append(builder, operation.getDate());
		builder.append(TABLE_LINE_SEPARATOR);
		builder.append(operation.getType());
		builder.append(TABLE_LINE_SEPARATOR);
		builder.append(operation.getAmount());
		builder.append(TABLE_LINE_SEPARATOR);
		builder.append(operation.getBalance());
		builder.append(TABLE_LINE_END);
		return builder;
	}

}
