package robots.model;

/**
 * LineStructure -
 *
 * @author Sam Griffin
 */
public class LineStructure {
	final String leadingSpace;
	final String directive;
	final String spaceSection1;
	final String value;
	final String comment;
	final String invalid;

	public LineStructure(final String leadingSpace, final String directive, final String spaceSection1, final String value, final String comment) {
		this.leadingSpace = leadingSpace;
		this.directive = directive;
		this.spaceSection1 = spaceSection1;
		this.value = value;
		this.comment = comment;
		invalid = null;
	}

	public LineStructure(final String invalid) {
		this.invalid = invalid;
		leadingSpace = null;
		directive = null;
		spaceSection1 = null;
		value = null;
		comment = null;
	}

	public String getLeadingSpace() {
		return leadingSpace;
	}

	public String getDirective() {
		return directive;
	}

	public String getSpaceSection1() {
		return spaceSection1;
	}

	public String getValue() {
		return value;
	}

	public String getComment() {
		return comment;
	}

	public String getInvalid() {
		return invalid;
	}

	public boolean isValid() {
		return invalid == null;
	}

	public boolean isRealDirective() {
		return directive != null;
	}

	// if the directive's capitalization does not match the standard directly, warn
	public boolean isDirectiveInexact() {
		return ! RobotsValidator.DIRECTIVES.contains(directive);
	}
}
