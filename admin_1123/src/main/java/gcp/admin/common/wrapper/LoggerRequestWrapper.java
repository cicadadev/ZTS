package gcp.admin.common.wrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;

public class LoggerRequestWrapper extends HttpServletRequestWrapper {

	private static final Logger	logger	= Logger.getLogger(LoggerRequestWrapper.class);

	public final String			payload;

	public LoggerRequestWrapper(HttpServletRequest request) throws AuthenticationException {
		super(request);

		// read the original payload into the payload variable
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			// read the payload into the StringBuilder
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				// make an empty string since there is no payload
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			logger.error("Error reading the request payload", ex);
			throw new AuthenticationException("Error reading the request payload", ex) { };
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException iox) {
					// ignore
				}
			}
		}
		payload = stringBuilder.toString();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(payload.getBytes());
		ServletInputStream inputStream = new ServletInputStream() {
			public int read() throws IOException {
				return byteArrayInputStream.read();
			}

			@Override
			public boolean isFinished() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isReady() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setReadListener(ReadListener arg0) {
				// TODO Auto-generated method stub

			}
		};
		return inputStream;
	}
}
