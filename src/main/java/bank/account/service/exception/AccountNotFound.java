package bank.account.service.exception;

public class AccountNotFound extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6049741279552590052L;

	public AccountNotFound(String message) {
		super(message);
	}

}
