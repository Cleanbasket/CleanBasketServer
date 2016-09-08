package com.bridge4biz.wash.fcm;

import static com.bridge4biz.wash.gcm.Constants.FCM_SEND_ENDPOINT;
import static com.bridge4biz.wash.gcm.Constants.PARAM_COLLAPSE_KEY;
import static com.bridge4biz.wash.gcm.Constants.PARAM_DELAY_WHILE_IDLE;
import static com.bridge4biz.wash.gcm.Constants.PARAM_DRY_RUN;
import static com.bridge4biz.wash.gcm.Constants.PARAM_PAYLOAD_PREFIX;
import static com.bridge4biz.wash.gcm.Constants.PARAM_REGISTRATION_ID;
import static com.bridge4biz.wash.gcm.Constants.PARAM_RESTRICTED_PACKAGE_NAME;
import static com.bridge4biz.wash.gcm.Constants.PARAM_TIME_TO_LIVE;
import static com.bridge4biz.wash.gcm.Constants.TOKEN_CANONICAL_REG_ID;
import static com.bridge4biz.wash.gcm.Constants.TOKEN_ERROR;
import static com.bridge4biz.wash.gcm.Constants.TOKEN_MESSAGE_ID;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.core.io.ClassPathResource;

import com.bridge4biz.wash.gcm.InvalidRequestException;
import com.bridge4biz.wash.gcm.Message;
import com.bridge4biz.wash.gcm.Result;
import com.bridge4biz.wash.gcm.Result.Builder;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class FcmSender {

	private static final String key = "AIzaSyCLPofmWdWaeL_5icJrSE-QCLIHK9gHRgc";
	protected final Random random = new Random();
	
	protected static final Logger logger = Logger.getLogger(FcmSender.class.getName());


	public FcmSender() {
		FirebaseOptions options = new FirebaseOptions.Builder().setServiceAccount(getAccountJSON())
				.setDatabaseUrl("https://project-561258547352407813.firebaseio.com/").build();
		FirebaseApp.initializeApp(options);
	}

	private InputStream getAccountJSON() {
		try {
			return new ClassPathResource("CleanBasketAndroid-77612746bd5d.json").getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Result send(Message message, String registrationId, int retries) throws IOException {
		int attempt = 0;
		Result result = null;
		int backoff = 1000;
		boolean tryAgain;
		do {
			attempt++;
			
			result = sendNoRetry(message, registrationId);
			tryAgain = result == null && attempt <= retries;
			if (tryAgain) {
				int sleepTime = backoff / 2 + random.nextInt(backoff);
				if (2 * backoff < 1024000) {
					backoff *= 2;
				}
			}
		} while (tryAgain);
		if (result == null) {
			throw new IOException("Could not send message after " + attempt + " attempts");
		}
		return result;
	}

	public Result sendNoRetry(Message message, String registrationId) throws IOException {
		StringBuilder body = newBody(PARAM_REGISTRATION_ID, registrationId);
		Boolean delayWhileIdle = message.isDelayWhileIdle();
		if (delayWhileIdle != null) {
			addParameter(body, PARAM_DELAY_WHILE_IDLE, delayWhileIdle ? "1" : "0");
		}
		Boolean dryRun = message.isDryRun();
		if (dryRun != null) {
			addParameter(body, PARAM_DRY_RUN, dryRun ? "1" : "0");
		}
		String collapseKey = message.getCollapseKey();
		if (collapseKey != null) {
			addParameter(body, PARAM_COLLAPSE_KEY, collapseKey);
		}
		String restrictedPackageName = message.getRestrictedPackageName();
		if (restrictedPackageName != null) {
			addParameter(body, PARAM_RESTRICTED_PACKAGE_NAME, restrictedPackageName);
		}
		Integer timeToLive = message.getTimeToLive();
		if (timeToLive != null) {
			addParameter(body, PARAM_TIME_TO_LIVE, Integer.toString(timeToLive));
		}
		for (Entry<String, String> entry : message.getData().entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (key == null || value == null) {
				logger.warning("Ignoring payload entry thas has null: " + entry);
			} else {
				key = PARAM_PAYLOAD_PREFIX + key;
				addParameter(body, key, URLEncoder.encode(value,"UTF-8"));
			}
		}
		String requestBody = body.toString();
		logger.finest("Request body: " + requestBody);
		HttpURLConnection conn;
		int status;
		try {
			conn = post(FCM_SEND_ENDPOINT, requestBody);
			status = conn.getResponseCode();
		} catch (IOException e) {
			logger.log(Level.FINE, "IOException posting to FCM", e);
			return null;
		}
		if (status / 100 == 5) {
			logger.fine("FCM service is unavailable (status " + status + ")");
			return null;
		}
		String responseBody;
		if (status != 200) {
			try {
				responseBody = getAndClose(conn.getErrorStream());
				logger.finest("Plain post error response: " + responseBody);
			} catch (IOException e) {
				// ignore the exception since it will thrown an
				// InvalidRequestException
				// anyways
				responseBody = "N/A";
				logger.log(Level.FINE, "Exception reading response: ", e);
			}
			throw new InvalidRequestException(status, responseBody);
		} else {
			try {
				responseBody = getAndClose(conn.getInputStream());
			} catch (IOException e) {
				logger.log(Level.WARNING, "Exception reading response: ", e);
				// return null so it can retry
				return null;
			}
		}
		String[] lines = responseBody.split("\n");
		if (lines.length == 0 || lines[0].equals("")) {
			throw new IOException("Received empty response from FCM service.");
		}
		String firstLine = lines[0];
		String[] responseParts = split(firstLine);
		String token = responseParts[0];
		String value = responseParts[1];
		if (token.equals(TOKEN_MESSAGE_ID)) {
			Builder builder = new Result.Builder().messageId(value);
			// check for canonical registration id
			if (lines.length > 1) {
				String secondLine = lines[1];
				responseParts = split(secondLine);
				token = responseParts[0];
				value = responseParts[1];
				if (token.equals(TOKEN_CANONICAL_REG_ID)) {
					builder.canonicalRegistrationId(value);
				} else {
					logger.warning("Invalid response from FCM: " + responseBody);
				}
			}
			Result result = builder.build();
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Message created succesfully (" + result + ")");
			}
			return result;
		} else if (token.equals(TOKEN_ERROR)) {
			return new Result.Builder().errorCode(value).build();
		} else {
			throw new IOException("Invalid response from FCM: " + responseBody);
		}
	}
	
	protected static StringBuilder newBody(String name, String value) {
		return new StringBuilder(nonNull(name)).append('=').append(nonNull(value));
	}
	
	protected HttpURLConnection post(String url, String body) throws IOException {
		return post(url, "application/x-www-form-urlencoded;charset=UTF-8", body);
	}

	protected HttpURLConnection post(String url, String contentType, String body) throws IOException {
		if (url == null || body == null) {
			throw new IllegalArgumentException("arguments cannot be null");
		}

		byte[] bytes = body.getBytes("UTF-8");
		HttpURLConnection conn = getConnection(url);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setFixedLengthStreamingMode(bytes.length);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", contentType);
		conn.setRequestProperty("Authorization", "key=" + key);
		OutputStream out = conn.getOutputStream();
		try {
			out.write(bytes);
		} finally {
			close(out);
		}
		return conn;
	}

	protected HttpURLConnection getConnection(String url) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		return conn;
	}

	private static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	static <T> T nonNull(T argument) {
		if (argument == null) {
			throw new IllegalArgumentException("argument cannot be null");
		}
		return argument;
	}
	
	protected static void addParameter(StringBuilder body, String name, String value) {
		nonNull(body).append('&').append(nonNull(name)).append('=').append(nonNull(value));
	}
	
	private String[] split(String line) throws IOException {
		String[] split = line.split("=", 2);
		if (split.length != 2) {
			throw new IOException("Received invalid response line from FCM: " + line);
		}
		return split;
	}

	private static String getAndClose(InputStream stream) throws IOException {
		try {
			return getString(stream);
		} finally {
			if (stream != null) {
				close(stream);
			}
		}
	}
	
	protected static String getString(InputStream stream) throws IOException {
		if (stream == null) {
			return "";
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuilder content = new StringBuilder();
		String newLine;
		do {
			newLine = reader.readLine();
			if (newLine != null) {
				content.append(newLine).append('\n');
			}
		} while (newLine != null);
		if (content.length() > 0) {
			// strip last newline
			content.setLength(content.length() - 1);
		}
		return content.toString();
	}
}