package com.tibco.tct.fom.environment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nuxeo.xforms.ui.UIControl;

import com.tibco.customwizard.validator.ICustomValidator;
import com.tibco.customwizard.validator.IValidationContext;
import com.tibco.customwizard.validator.ValidateResult;
import com.tibco.customwizard.xforms.IXFormValidationContext;

public class UrlValidator implements ICustomValidator {

	public static final String REGEX_PROTOCOL = "(\\btcp://|\\bssl://)";
	public static final String REGEX_HOST = "(?:(?:(?:[a-zA-Z0-9][a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9]?\\.)*[a-zA-Z][a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9]?)|(?:[a-zA-Z][a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9]?))";
	public static final String REGEX_IPv4 = "(?:(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(?:\\.(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})";
	public static final String REGEX_IPv6 = "(?:(?:(?:(?:[0-9A-Fa-f]{1,4}:){7}(?:[0-9A-Fa-f]{1,4}|:))|(?:(?:[0-9A-Fa-f]{1,4}:){6}(?::[0-9A-Fa-f]{1,4}|(?:(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(?:\\.(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(?:(?:[0-9A-Fa-f]{1,4}:){5}(?:(?:(?::[0-9A-Fa-f]{1,4}){1,2})|:(?:(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(?:\\.(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(?:(?:[0-9A-Fa-f]{1,4}:){4}(?:(?:(?::[0-9A-Fa-f]{1,4}){1,3})|(?:(?::[0-9A-Fa-f]{1,4})?:(?:(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(?:\\.(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(?:(?:[0-9A-Fa-f]{1,4}:){3}(?:(?:(?::[0-9A-Fa-f]{1,4}){1,4})|(?:(?::[0-9A-Fa-f]{1,4}){0,2}:(?:(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(?:\\.(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(?:(?:[0-9A-Fa-f]{1,4}:){2}(?:(?:(?::[0-9A-Fa-f]{1,4}){1,5})|(?:(?::[0-9A-Fa-f]{1,4}){0,3}:(?:(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(?:\\.(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(?:(?:[0-9A-Fa-f]{1,4}:){1}(?:(?:(?::[0-9A-Fa-f]{1,4}){1,6})|(?:(?::[0-9A-Fa-f]{1,4}){0,4}:(?:(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(?:\\.(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(?::(?:(?:(?::[0-9A-Fa-f]{1,4}){1,7})|(?:(?::[0-9A-Fa-f]{1,4}){0,5}:(?:(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(?:\\.(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(?:%.+)?)";
	public static final String REGEX_PORT = "(6553[0-5]|655[0-2]\\d|65[0-4]\\d\\d|6[0-4]\\d{3}|[1-5]\\d{4}|[1-9]\\d{0,3}|0)"; // Capturing
																																// group
	public static final String REGEX_HOST_OR_IPv4_OR_IPv6_WITH_SQUARE_BRACKETS = REGEX_HOST
			+ "|" + REGEX_IPv4 + "|" + "(?:\\x5B" + REGEX_IPv6 + "\\x5D)";
	public static final String REGEX_HOST_OR_IPv4_OR_IPv6 = REGEX_HOST + "|"
			+ REGEX_IPv4 + "|" + REGEX_IPv6;
	public static final String REGEX_HOST_PORT_1 = REGEX_PROTOCOL + "("
			+ REGEX_HOST_OR_IPv4_OR_IPv6_WITH_SQUARE_BRACKETS + "):"
			+ REGEX_PORT;
	public static final String REGEX_HOST_PORT_2 = REGEX_PROTOCOL + "("
			+ REGEX_HOST_OR_IPv4_OR_IPv6 + "):" + REGEX_PORT;
	public static final Pattern PATTERN_HOST_PORT_1 = Pattern
			.compile(REGEX_HOST_PORT_1);
	public static final Pattern PATTERN_HOST_PORT_2 = Pattern
			.compile(REGEX_HOST_PORT_2);

	public ValidateResult validate(IValidationContext validationContext) {

		UIControl uc = ((IXFormValidationContext) validationContext).getControl();
		String fieldName = uc.getName();
		String url = uc.getXMLValue();
		
		Matcher matcher1 = PATTERN_HOST_PORT_1.matcher(url);
		Matcher matcher2 = PATTERN_HOST_PORT_2.matcher(url);
		if (!matcher1.matches() && !matcher2.matches()) {
			return new ValidateResult("'" + fieldName + "'" + " The URL: " + url + " is invalid. \n Please Note: the Protocol can only be 'tcp' or 'ssl', and Port can only be an integer from 1 to 65535.");
		} 
		return ValidateResult.VALID;
	}
}
