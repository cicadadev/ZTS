package gcp.frontpc.common.wrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.io.output.TeeOutputStream;
import org.apache.log4j.Logger;

public class LoggerResponseWrapper extends HttpServletResponseWrapper {

	private static final Logger	logger	= Logger.getLogger(LoggerResponseWrapper.class);

	TeeServletOutputStream		teeStream;

	PrintWriter					teeWriter;

	ByteArrayOutputStream		bos;

	public LoggerResponseWrapper(HttpServletResponse response) {
		super(response);
	}

	public String getContent() throws IOException {
		if (bos != null) {
			return bos.toString();
		} else {
			return "";
		}

	}

	@Override
	public PrintWriter getWriter() throws IOException {

		if (this.teeWriter == null) {
			this.teeWriter = new PrintWriter(new OutputStreamWriter(getOutputStream()));
		}
		return this.teeWriter;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {

		if (teeStream == null) {
			bos = new ByteArrayOutputStream();
			teeStream = new TeeServletOutputStream(getResponse().getOutputStream(), bos);
		}
		return teeStream;
	}

	@Override
	public void flushBuffer() throws IOException {
		if (teeStream != null) {
			teeStream.flush();
			System.err.println("teeStream flush");
		}
		if (this.teeWriter != null) {
			this.teeWriter.flush();
			System.err.println("teeWriter flush");
		}
	}

	public class TeeServletOutputStream extends ServletOutputStream {

		private final TeeOutputStream targetStream;

		public TeeServletOutputStream(OutputStream one, OutputStream two) {
			targetStream = new TeeOutputStream(one, two);
		}

		@Override
		public void write(int arg0) throws IOException {
			this.targetStream.write(arg0);
		}

		public void flush() throws IOException {
			super.flush();
			this.targetStream.flush();
		}

		public void close() throws IOException {
			super.close();
			this.targetStream.close();
		}

		@Override
		public boolean isReady() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void setWriteListener(WriteListener arg0) {
			// TODO Auto-generated method stub

		}
	}
}
