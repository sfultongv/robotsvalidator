package robots.model;

/**
 * RobotsFile -
 *
 * @author Sam Griffin
 */
public class RobotsFile {
	private static class Section {
		private final UserAgentSection userAgentSection;
		private final SitemapSection sitemapSection;
		private final InvalidSection invalidSection;

		public Section (UserAgentSection userAgentSection) {
			this.userAgentSection = userAgentSection;
			sitemapSection = null;
			invalidSection = null;
		}

		public Section (SitemapSection sitemapSection) {
			this.sitemapSection = sitemapSection;
			userAgentSection = null;
			invalidSection = null;
		}

		public Section (InvalidSection invalidSection) {
			this.invalidSection = invalidSection;
			userAgentSection = null;
			sitemapSection = null;
		}

		public boolean isUserAgentSection() {
			return userAgentSection != null;
		}

		public boolean isSitemapSection() {
			return sitemapSection != null;
		}

		public boolean isInvalidSection() {
			return invalidSection != null;
		}

		public UserAgentSection getUserAgentSection() {
			return userAgentSection;
		}

		public SitemapSection getSitemapSection() {
			return sitemapSection;
		}

		public InvalidSection getInvalidSection() {
			return invalidSection;
		}
	}


}
