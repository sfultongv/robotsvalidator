package robots.model;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

/**
 * CommentSection -
 *
 * @author Sam Griffin
 */
public class CommentSection implements Section {
	final List<LineStructure> structures;

	public CommentSection(final List<LineStructure> structures) {
		this.structures = structures;
	}

	public static Optional<? extends Section> tryParse (PushbackBufferedReader pushbackBufferedReader) throws IOException {
		LinkedList<String> lines = Lists.newLinkedList();
		try {
			boolean foundComments = false;
			LinkedList<LineStructure> structures = Lists.newLinkedList();
			String currentLine;
			Optional<LineStructure> currentMatch;

			// handle comment section
			while ((currentMatch = RobotsValidator.tryMatchBlankLine(currentLine = pushbackBufferedReader.readLine())).isPresent()) {
				lines.addLast(currentLine);
				structures.addLast(currentMatch.get());
				foundComments = true;
			}

			if (! foundComments) {
				throw new ParseException();
			}

			// rewind last line and return well-formed section
			pushbackBufferedReader.pushBack(currentLine);
			return Optional.of(new CommentSection(structures));

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
		return "Comments Section";
	}
}
