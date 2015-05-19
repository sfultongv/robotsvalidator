package robots.model;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * LineStructure -
 *
 * @author Sam Griffin
 */
public class LineStructure {
	final LineFragment leadingSpace;
	final LineFragment directive;
	final LineFragment colonSpace;
	final LineFragment value;
	final LineFragment comment;
	final String invalid;

	public LineStructure(final LineFragment leadingSpace, final LineFragment directive, final LineFragment colonSpace, final LineFragment value, final LineFragment comment) {
		this.leadingSpace = leadingSpace;
		this.directive = directive;
		this.colonSpace = colonSpace;
		this.value = value;
		this.comment = comment;
		invalid = null;
	}

	public LineStructure(final String invalid) {
		this.invalid = invalid;
		leadingSpace = null;
		directive = null;
		colonSpace = null;
		value = null;
		comment = null;
	}

	public String getLeadingSpace() {
		return leadingSpace.getFragment();
	}

	public String getDirective() {
		return directive.getFragment();
	}

	public String getSpaceSection1() {
		return colonSpace.getFragment();
	}

	public String getValue() {
		return value.getFragment();
	}

	public String getComment() {
		return comment.getFragment();
	}

	public String getInvalid() {
		return invalid;
	}

	public boolean isValid() {
		return invalid == null;
	}

	public boolean isRealDirective() {
		return getDirective() != null;
	}

	public List<LineFragment> getFragments () {
		return Lists.asList(leadingSpace, new LineFragment[] {directive, colonSpace, value, comment});
	}

	// get rid of this!!
	// if the directive's capitalization does not match the standard directly, warn
	public boolean isDirectiveInexact() {
		return ! RobotsValidator.DIRECTIVES.contains(getDirective());
	}
}
