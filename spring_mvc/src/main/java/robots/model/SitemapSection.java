package robots.model;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * SitemapSection - this represents the end section of sitemap/host directives
 *
 *
 * @author Sam Griffin
 */
public class SitemapSection implements Section {
	final List<LineStructure> structures;

	public SitemapSection(final List<LineStructure> structures) {
		this.structures = structures;
	}

	public static Optional<? extends Section> tryParse (PushbackBufferedReader pushbackBufferedReader) throws IOException {
		LinkedList<String> lines = Lists.newLinkedList();
		try {
			boolean foundValidDirective = false;
			LinkedList<LineStructure> structures = Lists.newLinkedList();
			String currentLine;
			Optional<LineStructure> currentMatch;

			// handle comment section
			while ((currentMatch = RobotsValidator.tryMatchBlankLine(currentLine = pushbackBufferedReader.readLine())).isPresent()) {
				lines.addLast(currentLine);
				structures.addLast(currentMatch.get());
			}

			// parse any directives that belong at the end of the file
			Set<String> allowedDirectives = Sets.newHashSet(RobotsValidator.SITEMAP, RobotsValidator.HOST);
			while ((currentMatch = RobotsValidator.tryMatchBlankLine(currentLine = pushbackBufferedReader.readLine())
				.or(RobotsValidator.tryMatchDirectives(allowedDirectives, currentLine))).isPresent()) {

				lines.addLast(currentLine);
				structures.addLast(currentMatch.get());

				// if we found a non-blank/comment line, it's a valid directive
				if (currentMatch.get().getDirective() != null) {
					foundValidDirective = true;
				}
			}

			// if we didn't find any non-blank/comment lines return nothing
			if (! foundValidDirective) {
				throw new ParseException();
			}

			// rewind last line and return well-formed section
			pushbackBufferedReader.pushBack(currentLine);
			return Optional.of(new SitemapSection(structures));

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
		return "Miscellaneous Properties Section";
	}
}
