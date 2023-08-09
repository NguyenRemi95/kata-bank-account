package bank.account.service.exception;

public class StatementNotFound extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7162742764275705562L;

	public StatementNotFound(String message) {
		super(message);
	}

}
