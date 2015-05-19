package robots.model;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;

/**
 * LineFragment -
 *
 * @author Sam Griffin
 */
public class LineFragment {
	private final String fragment;
	private final Optional<String> suggestion;

	public LineFragment(final String fragment, final String suggestion) {
		this.fragment = fragment;
		this.suggestion = Optional.of(suggestion);
	}

	public LineFragment(final String fragment) {
		this.fragment = fragment;
		suggestion = Optional.absent();
	}

	public String getSuggestion () {
		return suggestion.get();
	}

	public String getFragment() {
		return fragment;
	}

	public boolean isSuggesting() {
		return suggestion.isPresent();
	}

	public static LineFragment makeFragment(String fragment, Predicate<String> predicate, String suggestion) {
		if (predicate.apply(fragment)) {
			return new LineFragment(fragment);
		} else {
			return new LineFragment(fragment, suggestion);
		}
	}
}
