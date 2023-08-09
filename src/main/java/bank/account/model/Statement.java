package bank.account.model;

import java.time.LocalDateTime;
import java.util.List;

public class Statement {

	private Account account;
	
	private LocalDateTime date;
	
	private Money amount;
	
	private Money balance;
	
	private List<AbstractOperation> operations;
}
