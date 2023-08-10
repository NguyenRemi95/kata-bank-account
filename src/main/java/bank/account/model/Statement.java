package bank.account.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

// aka AccountStatement
// use to paginate Operation History related to an Account
// - at any given time for each "open" account have a single current Statement ( !isPublished() aka  date == null)
// - when a statement isPublished() no operation can be add
public class Statement {

	private Account account;

	private LocalDateTime date;

	private List<AbstractOperation> operations;

	private Money initialBalance;

	protected Statement(Account account, LocalDateTime date, List<AbstractOperation> operations, Money initialBalance) {
		this.account = Objects.requireNonNull(account);
		this.date = date;
		this.operations = Objects.requireNonNull(operations);
		this.initialBalance = Objects.requireNonNull(initialBalance);
	}

	protected Statement(Account account, LocalDateTime date, List<AbstractOperation> operations) {
		this(account, date, operations, Money.ZERO);
	}

	public Statement(Account account, LocalDateTime date) {
		this(account, date, new ArrayList<>());
	}

	public Statement(Account account, LocalDateTime date, Money initialBalance) {
		this(account, date, new ArrayList<>(), initialBalance);
	}

	public Account getAccount() {
		return account;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public Money getAmount() {
		return getBalance().subtract(initialBalance);
	}

	public Money getInitialBalance() {
		return initialBalance;
	}

	public Money getBalance() {
		AbstractOperation operation = getLastOperation();
		return operation == null ? initialBalance : operation.getBalance();
	}

	public List<AbstractOperation> getOperations() {
		return Collections.unmodifiableList(operations);
	}

	public synchronized void addOperation(AbstractOperation operation) {
		if (isPublished())
			throw new IllegalStateException("no operation can be added to a published statement");
		operation.setStatement(this);
		this.operations.add(operation);
	}

	public boolean isPublished() {
		return date != null;
	}

	public synchronized void publish() {
		date = LocalDateTime.now();
	}

	protected synchronized AbstractOperation getLastOperation() {
		return operations.size() == 0 ? null : operations.get(operations.size() - 1);
	}

}
