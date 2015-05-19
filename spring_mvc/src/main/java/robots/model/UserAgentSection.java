package robots.model;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * UserAgentSection -
 *
 * @author Sam Griffin
 */
public class UserAgentSection implements Section {
	public static final Predicate<Section> IS_USER_AGENT = new Predicate<Section>() {
		@Override
		public boolean apply(final Section section) {
			return section.getSectionType().equals(SectionType.UserAgent);
		}
	};
	final List<LineStructure> structures;
	final String userAgent;

	public UserAgentSection(final List<LineStructure> structures, final String userAgent) {
		this.structures = structures;
		this.userAgent = userAgent;
	}

	public static Optional<? extends Section> tryParse (PushbackBufferedReader pushbackBufferedReader) throws IOException {
		LinkedList<String> lines = Lists.newLinkedList();
		try {
			LinkedList<LineStructure> structures = Lists.newLinkedList();
			String currentLine;
			Optional<LineStructure> currentMatch;
			String userAgent;

			// handle comment section
			while ((currentMatch = RobotsValidator.tryMatchBlankLine(currentLine = pushbackBufferedReader.readLine())).isPresent()) {
				lines.addLast(currentLine);
				structures.addLast(currentMatch.get());
			}

			// try to get user agent directive
			currentMatch = RobotsValidator.tryMatchDirective(RobotsValidator.USER_AGENT, currentLine);
			if (! currentMatch.isPresent()) {
				throw new ParseException();
			}
			lines.addLast(currentLine);
			structures.addLast(currentMatch.get());
			userAgent = currentMatch.get().getValue();

			// parse until we come across a directive that doesn't go with the user agent section
			Set<String> allowedDirectives = Sets.newHashSet(RobotsValidator.ALLOW, RobotsValidator.DISALLOW, RobotsValidator.CRAWL_DELAY);
			while ((currentMatch = RobotsValidator.tryMatchBlankLine(currentLine = pushbackBufferedReader.readLine())
								.or(RobotsValidator.tryMatchDirectives(allowedDirectives, currentLine))).isPresent()) {

				lines.addLast(currentLine);
				structures.addLast(currentMatch.get());
			}

			// rewind last line and return well-formed user agent section
			pushbackBufferedReader.pushBack(currentLine);
			return Optional.of(new UserAgentSection(structures, userAgent));

		} catch (ParseException e) {
			// rewind the buffer so another parser can attempt to parse
			pushbackBufferedReader.pushBack(lines);
			return Optional.absent();
		}
	}

	@Override
	public List<LineStructure> getLines() {
		return structures;
	}

	@Override
	public String getSectionHeading() {
		return "User Agent Section for: " + userAgent;
	}

	@Override
	public SectionType getSectionType() {
		return SectionType.UserAgent;
	}

	public String getUserAgent() {
		return userAgent;
	}
}
