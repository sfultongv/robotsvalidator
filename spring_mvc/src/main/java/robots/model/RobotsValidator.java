package robots.model;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * RobotsValidator -
 *
 * @author Sam Griffin
 */
public class RobotsValidator {
	public static final String BLANK_LINE = "^(\\s*)(#.*)?$";
	// basic directives
	public static final String USER_AGENT = "User-agent";
	public static final String DISALLOW = "Disallow";
	// nonstandard extensions
	public static final String CRAWL_DELAY = "Crawl-delay";
	public static final String ALLOW = "Allow";
	public static final String SITEMAP = "Sitemap";
	public static final String HOST = "Host";
	public static final Set<String> DIRECTIVES = Sets.newHashSet(
		USER_AGENT, DISALLOW, CRAWL_DELAY, ALLOW, SITEMAP, HOST
	);

	public static Optional<LineStructure> tryMatchDirective(String directive, String line) {
		if (line == null) {
			return Optional.absent();
		}

		Pattern pattern = Pattern.compile("^(\\s*)(" + directive + "):(\\s*)([^#]*)(#.*)?$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(line);
		return matcher.matches()
			? Optional.of(new LineStructure(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5)))
			: Optional.<LineStructure>absent();
	}

	public static Optional<LineStructure> tryMatchDirectives(Set<String> directives, String line) {
		if (line == null) {
			return Optional.absent();
		}

		Pattern pattern = Pattern.compile("^(\\s*)(" + Joiner.on('|').join(directives) + "):(\\s*)([^#]*)(#.*)?$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(line);
		return matcher.matches()
			? Optional.of(new LineStructure(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5)))
			: Optional.<LineStructure>absent();

	}

	public static Optional<LineStructure> tryMatchBlankLine(String line) {
		if (line == null) {
			return Optional.absent();
		}

		Pattern pattern = Pattern.compile(BLANK_LINE);
		Matcher matcher = pattern.matcher(line);
		return matcher.matches()
			? Optional.of(new LineStructure(matcher.group(1), null, null, null, matcher.group(2)))
			: Optional.<LineStructure>absent();
	}

	public List<Section> getSections (PushbackBufferedReader pushbackBufferedReader) throws IOException {
		List<Section> sections = Lists.newLinkedList();
		while (! pushbackBufferedReader.done()) {
			Optional<? extends Section> section = UserAgentSection.tryParse(pushbackBufferedReader);

			if (! section.isPresent()) {
				section = SitemapSection.tryParse(pushbackBufferedReader);
			}

			if (! section.isPresent()) {
				section = InvalidSection.tryParse(pushbackBufferedReader);
			}

			if (! section.isPresent()) {
				section = CommentSection.tryParse(pushbackBufferedReader);
			}

			if (section.isPresent()) {
				sections.add(section.get());
			}
		}

		return sections;
	}

}
