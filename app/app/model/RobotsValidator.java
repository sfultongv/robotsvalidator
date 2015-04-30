package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

/**
 * RobotsValidator -
 *
 * @author Sam Griffin
 */
public class RobotsValidator {
	private String errorMessage;
	private final static List<Pattern> VALID_PATTERNS = Lists.newArrayList(
		// basic directives
		Pattern.compile("^\\s*(#.*)?$"),                                //blank line
		Pattern.compile("^user-agent:.*$", Pattern.CASE_INSENSITIVE),   //user agent
		Pattern.compile("^disallow:.*$", Pattern.CASE_INSENSITIVE),     //disallow

		// nonstandard extensions
		Pattern.compile("^crawl-delay:.*$", Pattern.CASE_INSENSITIVE),
		Pattern.compile("^allow:.*$", Pattern.CASE_INSENSITIVE),
		Pattern.compile("^sitemap:.*$", Pattern.CASE_INSENSITIVE),
		Pattern.compile("^host:.*$", Pattern.CASE_INSENSITIVE)
	);


	// parse robots.txt from inputstream and return a copied inputstream for further processing
	public void check (BufferedReader bufferedReader) throws IOException {
		// test every line
		String line = "";
		int lineNumber = 0;
		while ((line = bufferedReader.readLine()) != null) {
			lineNumber++;
			boolean matched = false;
			for (Pattern pattern : VALID_PATTERNS) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.matches()) {
					matched = true;
					break;
				}
			}
			if (!matched) {
				errorMessage = String.format("Error on line %d: %s", lineNumber, line);
				break;
			}
		}
	}

	public boolean hasError() {
		return StringUtils.isNotBlank(errorMessage);
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
