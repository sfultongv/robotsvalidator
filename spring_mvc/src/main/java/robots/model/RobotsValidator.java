package robots.model;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
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

	public static String findDirective(final String directive, Iterable<String> directives) {
		return Iterables.find(directives, new Predicate<String>() {
			@Override
			public boolean apply(final String s) {
				return s.toLowerCase().equals(directive.toLowerCase());
			}
		});
	}

	public static Optional<LineStructure> tryMatchDirective(final String directive, String line) {
		return tryMatchDirectives(Sets.newHashSet(directive), line);
	}

	public static Optional<LineStructure> tryMatchDirectives(final Set<String> directives, String line) {
		if (line == null) {
			return Optional.absent();
		}

		Pattern pattern = Pattern.compile("^(\\s*)(" + Joiner.on('|').join(directives) + ")(:?\\s*)([^#]*)(#.*)?$", Pattern.CASE_INSENSITIVE);
		final Matcher matcher = pattern.matcher(line);
		if (! matcher.matches()) {
			return Optional.absent();
		}

		LineFragment leadingSpaceFragment = LineFragment.makeFragment(matcher.group(1), new Predicate<String>() {
			@Override
			public boolean apply(final String s) {
				return "".equals(s);
			}
		}, "There shouldn't be any leading space before the directive");
		final String properlyCapitalizedDirective = findDirective(matcher.group(2), directives);
		LineFragment directiveFragment = LineFragment.makeFragment(matcher.group(2), new Predicate<String>() {
			@Override
			public boolean apply(final String s) {
				return properlyCapitalizedDirective.equals(s);
			}
		}, String.format("The proper capitalization for this directive is \"%s\"", properlyCapitalizedDirective));
		LineFragment colonSpaceFragment = LineFragment.makeFragment(matcher.group(3), new Predicate<String>() {
			@Override
			public boolean apply(final String s) {
				return ": ".equals(s);
			}
		}, "There should be a single colon followed by a single space after the directive");
		LineFragment valueFragment = new LineFragment(matcher.group(4));
		LineFragment commentFragment = new LineFragment(matcher.group(5));
		return Optional.of(new LineStructure(leadingSpaceFragment, directiveFragment, colonSpaceFragment, valueFragment, commentFragment));
	}

	public static Optional<LineStructure> tryMatchBlankLine(String line) {
		if (line == null) {
			return Optional.absent();
		}

		Pattern pattern = Pattern.compile(BLANK_LINE);
		Matcher matcher = pattern.matcher(line);
		if (! matcher.matches()) {
			return Optional.absent();
		}

		LineFragment leadingSpaceFragment = new LineFragment(matcher.group(1));
		LineFragment emptyFragment = new LineFragment("");
		LineFragment commentFragment = new LineFragment(matcher.group(2));
		return Optional.of(new LineStructure(leadingSpaceFragment, emptyFragment, emptyFragment, emptyFragment, commentFragment));
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
