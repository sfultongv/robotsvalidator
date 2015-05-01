package robots.model;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * StreamCloner -
 *
 * @author Sam Griffin
 */
public class StreamCloner {
	private final ByteArrayOutputStream byteArrayOutputStream;

	public StreamCloner(final InputStream inputStream) throws IOException {
		byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		while ((len = inputStream.read(buffer)) > -1 ) {
			byteArrayOutputStream.write(buffer, 0, len);
		}
		byteArrayOutputStream.flush();
	}

	public InputStream getNewInputStream() {
		return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
	}

	public BufferedReader getNewBufferedReader() {
		return new BufferedReader(new InputStreamReader(getNewInputStream()));
	}
}
