package robots.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * RobotResults -
 *
 * @author Sam Griffin
 */
public class RobotResults {
	private final String site;
	private final String errorString;
	private final List<Section> sections;

	private RobotResults() {
		site = "";
		errorString = "";
		sections = Collections.emptyList();
	}

	public RobotResults(final String error) {
		site = "";
		errorString = error;
		sections = Collections.emptyList();
	}

	public RobotResults(final String site, InputStream inputStream) {
		this.site = site;
		RobotsValidator validator = new RobotsValidator();
		String errorString = "";
		List<Section> sections;
		try {
			PushbackBufferedReader pushbackBufferedReader = new PushbackBufferedReader(new InputStreamReader(inputStream));
			sections = validator.getSections(pushbackBufferedReader);
		} catch (IOException e) {
			errorString = e.getMessage();
			sections = Collections.emptyList();
		}
		this.errorString = errorString;
		this.sections = sections;
	}

	public String getSite() {
		return site;
	}

	public String getErrorString() {
		return errorString;
	}

	public boolean hasError() {
		return ! StringUtils.isBlank(errorString);
	}

	public boolean isError() {
		return hasError();
	}

	public List<Section> getSections() {
		return sections;
	}

	public boolean isReal() {
		return this != NO_RESULTS;
	}

	public static RobotResults NO_RESULTS = new RobotResults();
}
