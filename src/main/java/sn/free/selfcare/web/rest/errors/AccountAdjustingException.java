package sn.free.selfcare.web.rest.errors;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class AccountAdjustingException extends AbstractThrowableProblem {

	private static final long serialVersionUID = 1L;

	private final String errorKey;

	public AccountAdjustingException(String defaultMessage, String errorKey) {
		this(ErrorConstants.DEFAULT_TYPE, defaultMessage, errorKey);
	}

	public AccountAdjustingException(URI type, String defaultMessage, String errorKey) {
		super(type, defaultMessage, Status.PRECONDITION_FAILED, null, null, null, getAlertParameters(errorKey));
		this.errorKey = errorKey;
	}

	public String getErrorKey() {
		return errorKey;
	}

	private static Map<String, Object> getAlertParameters(String errorKey) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("message", "error." + errorKey);
		return parameters;
	}
}
