package bank.account.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import bank.account.exception.NotYetImplementedException;

// aka AccountStatement
// use to paginate Operation History related to an Account
// - at any given time for each "open" account have a single current Statement ( !isPublished() aka  date == null)
// - when a statement isPublished() no operation can be add
public class Statement {

	private Account account;

	private LocalDateTime date;

	private List<AbstractOperation> operations;

	protected Statement(Account account, LocalDateTime date, List<AbstractOperation> operations) {
		this.account = Objects.requireNonNull(account);
		this.date = date;
		this.operations = Objects.requireNonNull(operations);
	}

	public Statement(Account account, LocalDateTime date) {
		this(account, date, new ArrayList<>());
	}

	public Account getAccount() {
		return account;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public Money getAmount() {
		throw new NotYetImplementedException();
	}

	public Money getBalance() {
		throw new NotYetImplementedException();
	}

	public List<AbstractOperation> getOperations() {
		return Collections.unmodifiableList(operations);
	}

	protected void addOperation(AbstractOperation operation) {
		if (isPublished())
			throw new IllegalStateException("no operation can be added to a published statement");
		operation.setStatement(this);
		this.operations.add(operation);
	}

	public boolean isPublished() {
		return date != null;
	}

	public void publish() {
		date = LocalDateTime.now();
	}

}
