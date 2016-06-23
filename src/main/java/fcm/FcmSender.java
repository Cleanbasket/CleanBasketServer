package fcm;

import static com.bridge4biz.wash.gcm.Constants.*;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.bridge4biz.wash.gcm.Constants;
import com.bridge4biz.wash.gcm.InvalidRequestException;
import com.bridge4biz.wash.gcm.Message;
import com.bridge4biz.wash.gcm.MulticastResult;
import com.bridge4biz.wash.gcm.Result;
import com.bridge4biz.wash.gcm.Result.Builder;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class FcmSender {
	
	protected static final String UTF8 = "UTF-8";
	protected static final int BACKOFF_INITIAL_DELAY = 1000;
	protected static final int MAX_BACKOFF_DELAY = 1024000;

	protected final Random random = new Random();
	protected static final Logger logger = Logger.getLogger(FcmSender.class.getName());

	private static final String key ="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCh58GOqYYDtebr\nbQUMknZReIfDUpt4JkQ+h/wQsrwipRTDaldoRT1EPduu3wczmCQXsjitsGHXsFEm\nVG+iTx9EOlXXQCxr0jGpVFjiMGFqxxaLzKkiQVF6pSj2Je1Y7ZNRQ/s84Wfv+EWX\nLRfusN/vcUPf6VNhb68bhDBIcwd3rtWnWZUjHYecDfKrJtGpp1+QcJhcCKXKKkdx\nrhlW/YFj7YR0zKOIzaN1ACnLwrT72ZZM5hWlXs4ghCV1wK4ONB54mdwXBQKEmDBS\nlbi1vkzXuhROdTdqTWLF1ShBaEWWrxLm8VC5cWvWBJx7uRBsVMx9EvECCWqpspWB\nkUvVQsdtAgMBAAECggEBAIoSCxWvahMmAAyLR0AGcxJdWGHww5a+A3rUGtjeo+Rj\npYZU3L+WNH5Kxlql5g1Q1I7EtOMiRP5cZYfrf9wJ358epG/RUVCNyz4dKUOTgLA/\nofGMkjwdOe/+gyUPCQ7KY9RsxgeQLkQOFMKsePlcK0yV6g08fJfeV5V+sHJIbnjX\n5P0K+zs2CVEtjWMBpu39jTbtAGnagZhxmhoL8oyMFoNutO5PsDek3H2zvHFNe7bo\nu2OGYI5PYIW7wAWG/uv0Z1KUrTe+plgs3ZKsGGYJgGtBl5mZKMzOwW1A4E3ouKcb\nzyE/zMHILDi9D4CeGL2t9fp6ksSzcM5CjgXRnwTfRQECgYEA+XE8FWpvJP2UzR86\nchfldHN1qi87zvshPH+0N3gm91YX9a1f9dg8z6efCv5SdcBkDuVZSQ1gqgqmBpFz\nx6js/DTHVek5qliNxcBCNtCQcr5fADgltYKdbiatX7Dk3CLckZtMhmIDw8NQBl1P\n+RjuVOg8KFlB2nNsy20DMgy+R8ECgYEApilkBhNzrwCqik+D8ntMDBmOvLqlJZ3Z\nUzosgNuNB/AQgdfz5tyxqOmSRLzy8B7aDlOmFU5e2/EheBBYsoMuuRnjR9KPdDEZ\n53YUaRwH+UCPbzAgnpogq+7/MWnO09EyynNUXaXiM/UlZzhvKJ0XxDKSInamRkLV\n1fznKQeOyq0CgYEAyqlDRasJziXwxY7/rz7W//vmt3RUxV+nqz2eMAsAradXJBXO\nhzE+hwAK6aWjXWQIZ0nTdtTVmpG4PlHinW8Ty/0djyFD5rC3ztjbcymUkoU1Ljpv\n+L68JYhrB/nylyAD0JE6ZVww7tY6qFEb7qhgyr88URPjuxZYaBaTqBBOcIECgYAY\niPIRB5xEUffclmMoUdZnzvpJmdG63TTy2hsqJ8EKVANL+OQ1yY6eH2cOqUvB8vxF\ns0pJyRmupktH3DoMmdwzTsRFnay6/mkRyVi4MIBo6ISFaXjXknCSkqax2CrHEhPK\n2v6xGUZuX5tXQ3j+aTvSJ5l2Z0ikBUhn4YEMiOYnvQKBgGA0uYy/Hcc/NwxGMqzt\nNQgr7QjKTW4nXVp0CbTOdC3gtExCy4mE3vxOntj17r0fRfvXBr7T4De6CH6GvjDV\n3OV69cRTIiA0E/eHr4TnS8vCj3UI2+Oyyz31/SVtVwzNGtqMuK51YXo6I6nM4GEp\nNJwnxrYLLObst6Dx9Fwr7xlg\n";
	
	public FcmSender() {
		FirebaseOptions options = new FirebaseOptions.Builder()
				  .setServiceAccount(getAccountJSON())
				  .setDatabaseUrl("https://cleanbasketpush.firebaseio.com/")
				  .build();
		FirebaseApp.initializeApp(options);
	}
	
	private FileInputStream getAccountJSON() {
		try {
			FileInputStream fis = new FileInputStream("path/to/serviceAccountCredentials.json");
			return fis;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Result send(Message message, String registrationId, int retries) throws IOException {
		int attempt = 0;
		Result result = null;
		int backoff = BACKOFF_INITIAL_DELAY;
		boolean tryAgain;
		do {
			attempt++;
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Attempt #" + attempt + " to send message " + message + " to regIds " + registrationId);
			}
			result = sendNoRetry(message, registrationId);
			tryAgain = result == null && attempt <= retries;
			if (tryAgain) {
				int sleepTime = backoff / 2 + random.nextInt(backoff);
				sleep(sleepTime);
				if (2 * backoff < MAX_BACKOFF_DELAY) {
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
				addParameter(body, key, URLEncoder.encode(value, UTF8));
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
			logger.log(Level.FINE, "IOException posting to GCM", e);
			return null;
		}
		if (status / 100 == 5) {
			logger.fine("GCM service is unavailable (status " + status + ")");
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
			throw new IOException("Received empty response from GCM service.");
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
					logger.warning("Invalid response from GCM: " + responseBody);
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
			throw new IOException("Invalid response from GCM: " + responseBody);
		}
	}

	public MulticastResult send(Message message, List<String> regIds, int retries) throws IOException {
		int attempt = 0;
		MulticastResult multicastResult;
		int backoff = BACKOFF_INITIAL_DELAY;
		// Map of results by registration id, it will be updated after each
		// attempt
		// to send the messages
		Map<String, Result> results = new HashMap<String, Result>();
		List<String> unsentRegIds = new ArrayList<String>(regIds);
		boolean tryAgain;
		List<Long> multicastIds = new ArrayList<Long>();
		do {
			multicastResult = null;
			attempt++;
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Attempt #" + attempt + " to send message " + message + " to regIds " + unsentRegIds);
			}
			try {
				multicastResult = sendNoRetry(message, unsentRegIds);
			} catch (IOException e) {
				// no need for WARNING since exception might be already logged
				logger.log(Level.FINEST, "IOException on attempt " + attempt, e);
			}
			if (multicastResult != null) {
				long multicastId = multicastResult.getMulticastId();
				logger.fine("multicast_id on attempt # " + attempt + ": " + multicastId);
				multicastIds.add(multicastId);
				unsentRegIds = updateStatus(unsentRegIds, results, multicastResult);
				tryAgain = !unsentRegIds.isEmpty() && attempt <= retries;
			} else {
				tryAgain = attempt <= retries;
			}
			if (tryAgain) {
				int sleepTime = backoff / 2 + random.nextInt(backoff);
				sleep(sleepTime);
				if (2 * backoff < MAX_BACKOFF_DELAY) {
					backoff *= 2;
				}
			}
		} while (tryAgain);
		if (multicastIds.isEmpty()) {
			// all JSON posts failed due to GCM unavailability
			throw new IOException("Could not post JSON requests to GCM after " + attempt + " attempts");
		}
		// calculate summary
		int success = 0, failure = 0, canonicalIds = 0;
		for (Result result : results.values()) {
			if (result.getMessageId() != null) {
				success++;
				if (result.getCanonicalRegistrationId() != null) {
					canonicalIds++;
				}
			} else {
				failure++;
			}
		}
		// build a new object with the overall result
		long multicastId = multicastIds.remove(0);
		MulticastResult.Builder builder = new MulticastResult.Builder(success, failure, canonicalIds, multicastId).retryMulticastIds(multicastIds);
		// add results, in the same order as the input
		for (String regId : regIds) {
			Result result = results.get(regId);
			builder.addResult(result);
		}
		return builder.build();
	}

	private List<String> updateStatus(List<String> unsentRegIds, Map<String, Result> allResults, MulticastResult multicastResult) {
		List<Result> results = multicastResult.getResults();
		if (results.size() != unsentRegIds.size()) {
			// should never happen, unless there is a flaw in the algorithm
			throw new RuntimeException("Internal error: sizes do not match. " + "currentResults: " + results + "; unsentRegIds: " + unsentRegIds);
		}
		List<String> newUnsentRegIds = new ArrayList<String>();
		for (int i = 0; i < unsentRegIds.size(); i++) {
			String regId = unsentRegIds.get(i);
			Result result = results.get(i);
			allResults.put(regId, result);
			String error = result.getErrorCodeName();
			if (error != null && (error.equals(Constants.ERROR_UNAVAILABLE) || error.equals(Constants.ERROR_INTERNAL_SERVER_ERROR))) {
				newUnsentRegIds.add(regId);
			}
		}
		return newUnsentRegIds;
	}

	public MulticastResult sendNoRetry(Message message, List<String> registrationIds) throws IOException {
		if (nonNull(registrationIds).isEmpty()) {
			throw new IllegalArgumentException("registrationIds cannot be empty");
		}
		Map<Object, Object> jsonRequest = new HashMap<Object, Object>();
		setJsonField(jsonRequest, PARAM_TIME_TO_LIVE, message.getTimeToLive());
		setJsonField(jsonRequest, PARAM_COLLAPSE_KEY, message.getCollapseKey());
		setJsonField(jsonRequest, PARAM_RESTRICTED_PACKAGE_NAME, message.getRestrictedPackageName());
		setJsonField(jsonRequest, PARAM_DELAY_WHILE_IDLE, message.isDelayWhileIdle());
		setJsonField(jsonRequest, PARAM_DRY_RUN, message.isDryRun());
		jsonRequest.put(JSON_REGISTRATION_IDS, registrationIds);
		Map<String, String> payload = message.getData();
		if (!payload.isEmpty()) {
			jsonRequest.put(JSON_PAYLOAD, payload);
		}
		String requestBody = JSONValue.toJSONString(jsonRequest);
		logger.finest("JSON request: " + requestBody);
		HttpURLConnection conn;
		int status;
		try {
			conn = post(FCM_SEND_ENDPOINT, "application/json;charset=UTF-8", requestBody);
			status = conn.getResponseCode();
		} catch (IOException e) {
			logger.log(Level.FINE, "IOException posting to GCM", e);
			return null;
		}
		String responseBody;
		if (status != 200) {
			try {
				responseBody = getAndClose(conn.getErrorStream());
				logger.finest("JSON error response: " + responseBody);
			} catch (IOException e) {
				// ignore the exception since it will thrown an
				// InvalidRequestException
				// anyways
				responseBody = "N/A";
				logger.log(Level.FINE, "Exception reading response: ", e);
			}
			throw new InvalidRequestException(status, responseBody);
		}
		try {
			responseBody = getAndClose(conn.getInputStream());
		} catch (IOException e) {
			logger.log(Level.WARNING, "IOException reading response", e);
			return null;
		}
		logger.finest("JSON response: " + responseBody);
		JSONParser parser = new JSONParser();
		JSONObject jsonResponse;
		try {
			jsonResponse = (JSONObject) parser.parse(responseBody);
			int success = getNumber(jsonResponse, JSON_SUCCESS).intValue();
			int failure = getNumber(jsonResponse, JSON_FAILURE).intValue();
			int canonicalIds = getNumber(jsonResponse, JSON_CANONICAL_IDS).intValue();
			long multicastId = getNumber(jsonResponse, JSON_MULTICAST_ID).longValue();
			MulticastResult.Builder builder = new MulticastResult.Builder(success, failure, canonicalIds, multicastId);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> results = (List<Map<String, Object>>) jsonResponse.get(JSON_RESULTS);
			if (results != null) {
				for (Map<String, Object> jsonResult : results) {
					String messageId = (String) jsonResult.get(JSON_MESSAGE_ID);
					String canonicalRegId = (String) jsonResult.get(TOKEN_CANONICAL_REG_ID);
					String error = (String) jsonResult.get(JSON_ERROR);
					Result result = new Result.Builder().messageId(messageId).canonicalRegistrationId(canonicalRegId).errorCode(error).build();
					builder.addResult(result);
				}
			}
			MulticastResult multicastResult = builder.build();
			return multicastResult;
		} catch (ParseException e) {
			throw newIoException(responseBody, e);
		} catch (CustomParserException e) {
			throw newIoException(responseBody, e);
		}
	}

	private IOException newIoException(String responseBody, Exception e) {
		// log exception, as IOException constructor that takes a message and
		// cause
		// is only available on Java 6
		String msg = "Error parsing JSON response (" + responseBody + ")";
		logger.log(Level.WARNING, msg, e);
		return new IOException(msg + ":" + e);
	}

	private static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				// ignore error
				logger.log(Level.FINEST, "IOException closing stream", e);
			}
		}
	}

	/**
	 * Sets a JSON field, but only if the value is not {@literal null}.
	 */
	private void setJsonField(Map<Object, Object> json, String field, Object value) {
		if (value != null) {
			json.put(field, value);
		}
	}

	private Number getNumber(Map<?, ?> json, String field) {
		Object value = json.get(field);
		if (value == null) {
			throw new CustomParserException("Missing field: " + field);
		}
		if (!(value instanceof Number)) {
			throw new CustomParserException("Field " + field + " does not contain a number: " + value);
		}
		return (Number) value;
	}

	class CustomParserException extends RuntimeException {

		private static final long serialVersionUID = -4281960286368366724L;

		CustomParserException(String message) {
			super(message);
		}
	}

	private String[] split(String line) throws IOException {
		String[] split = line.split("=", 2);
		if (split.length != 2) {
			throw new IOException("Received invalid response line from GCM: " + line);
		}
		return split;
	}

	/**
	 * Make an HTTP post to a given URL.
	 * 
	 * @return HTTP response.
	 */
	protected HttpURLConnection post(String url, String body) throws IOException {
		return post(url, "application/x-www-form-urlencoded;charset=UTF-8", body);
	}

	/**
	 * Makes an HTTP POST request to a given endpoint.
	 * 
	 * <p>
	 * <strong>Note: </strong> the returned connected should not be
	 * disconnected, otherwise it would kill persistent connections made using
	 * Keep-Alive.
	 * 
	 * @param url
	 *            endpoint to post the request.
	 * @param contentType
	 *            type of request.
	 * @param body
	 *            body of the request.
	 * 
	 * @return the underlying connection.
	 * 
	 * @throws IOException
	 *             propagated from underlying methods.
	 */
	protected HttpURLConnection post(String url, String contentType, String body) throws IOException {
		if (url == null || body == null) {
			throw new IllegalArgumentException("arguments cannot be null");
		}
		if (!url.startsWith("https://")) {
			logger.warning("URL does not use https: " + url);
		}
		logger.fine("Sending POST to " + url);
		logger.finest("POST body: " + body);
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

	/**
	 * Creates a map with just one key-value pair.
	 */
	protected static final Map<String, String> newKeyValues(String key, String value) {
		Map<String, String> keyValues = new HashMap<String, String>(1);
		keyValues.put(nonNull(key), nonNull(value));
		return keyValues;
	}

	/**
	 * Creates a {@link StringBuilder} to be used as the body of an HTTP POST.
	 * 
	 * @param name
	 *            initial parameter for the POST.
	 * @param value
	 *            initial value for that parameter.
	 * @return StringBuilder to be used an HTTP POST body.
	 */
	protected static StringBuilder newBody(String name, String value) {
		return new StringBuilder(nonNull(name)).append('=').append(nonNull(value));
	}

	/**
	 * Adds a new parameter to the HTTP POST body.
	 * 
	 * @param body
	 *            HTTP POST body.
	 * @param name
	 *            parameter's name.
	 * @param value
	 *            parameter's value.
	 */
	protected static void addParameter(StringBuilder body, String name, String value) {
		nonNull(body).append('&').append(nonNull(name)).append('=').append(nonNull(value));
	}

	/**
	 * Gets an {@link HttpURLConnection} given an URL.
	 */
	protected HttpURLConnection getConnection(String url) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		return conn;
	}

	/**
	 * Convenience method to convert an InputStream to a String.
	 * <p>
	 * If the stream ends in a newline character, it will be stripped.
	 * <p>
	 * If the stream is {@literal null}, returns an empty string.
	 */
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

	private static String getAndClose(InputStream stream) throws IOException {
		try {
			return getString(stream);
		} finally {
			if (stream != null) {
				close(stream);
			}
		}
	}

	static <T> T nonNull(T argument) {
		if (argument == null) {
			throw new IllegalArgumentException("argument cannot be null");
		}
		return argument;
	}

	void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

}