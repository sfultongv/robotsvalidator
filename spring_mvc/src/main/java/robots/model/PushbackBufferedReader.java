package robots.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * PushbackBufferedReader -
 *
 * @author Sam Griffin
 */
public class PushbackBufferedReader extends BufferedReader {
	final LinkedList<String> rebuffer;

	public PushbackBufferedReader(final Reader in) {
		super(in);
		rebuffer = Lists.newLinkedList();
	}

	public PushbackBufferedReader(final Reader in, final int sz) {
		super(in, sz);
		rebuffer = Lists.newLinkedList();
	}

	public void pushBack(String s) {
		rebuffer.addLast(s);
	}

	public void pushBack(List<String> l) {
		for (String s : l) {
			pushBack(s);
		}
	}

	@Override
	public String readLine() throws IOException {
		if (! rebuffer.isEmpty()) {
			return rebuffer.removeFirst();
		}
		return super.readLine();
	}

	public boolean done() {
		return rebuffer.size() > 0 && rebuffer.getFirst() == null;
	}
}
