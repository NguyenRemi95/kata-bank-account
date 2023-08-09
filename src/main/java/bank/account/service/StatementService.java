package bank.account.service;

import bank.account.service.request.PrintStatementQuery;

public interface StatementService {

	public String printStatement(PrintStatementQuery query);
}
