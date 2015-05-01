package robots.model;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.archive.modules.net.Robotstxt;

/**
 * RobotResults -
 *
 * @author Sam Griffin
 */
public class RobotResults {
	private final String site;
	private final String agent;
	private final String path;
	private final String errorString;
	private final boolean allowsPath;
	private final boolean authenticResults;

	private RobotResults() {
		site = "";
		agent = "";
		path = "";
		errorString = "";
		allowsPath = false;
		authenticResults = false;
	}

	public RobotResults(final String error) {
		site = "";
		agent = "";
		path = "";
		allowsPath = false;
		authenticResults = false;
		errorString = error;
	}

	public RobotResults(final String site, final String agent, final String path, InputStream inputStream) {
		this.site = site;
		this.agent = agent;
		this.path = path;
		RobotsValidator validator = new RobotsValidator();
		String errorString = "";
		boolean allowsPath = false;
		boolean authenticResults = false;
		try {
			StreamCloner streamCloner = new StreamCloner(inputStream);
			validator.check(streamCloner.getNewBufferedReader());
			errorString = validator.getErrorMessage();
			if (StringUtils.isBlank(errorString)) {
				Robotstxt robotstxt = new Robotstxt(streamCloner.getNewBufferedReader());
				allowsPath = robotstxt.getDirectivesFor(agent).allows(path);
				authenticResults = true;
			}
		} catch (IOException e) {
			errorString = e.getMessage();
			authenticResults = false;
		}
		this.errorString = errorString;
		this.allowsPath = allowsPath;
		this.authenticResults = authenticResults;
	}

	public String getSite() {
		return site;
	}

	public String getAgent() {
		return agent;
	}

	public String getPath() {
		return path;
	}

	public boolean isAuthenticResults() {
		return authenticResults;
	}

	public String getPathAllowed() {
		return  allowsPath ? "allowed" : "disallowed";
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

	public static RobotResults NO_RESULTS = new RobotResults();
}
