package com.github.cecchisandrone.vc.audio;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MicrophoneInputStream extends FilterInputStream {

	protected MicrophoneInputStream(InputStream in) {
		super(in);
	}

	@Override
	public int read() throws IOException {
		int read = super.read();
		if (read == 0) {
			return -1;
		} else {
			return read;
		}
	}

	@Override
	public int read(byte[] b) throws IOException {
		int read = super.read(b);
		if (read == 0) {
			return -1;
		} else {
			return read;
		}
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int read = super.read(b, off, len);
		if (read == 0) {
			return -1;
		} else {
			return read;
		}
	}
}
