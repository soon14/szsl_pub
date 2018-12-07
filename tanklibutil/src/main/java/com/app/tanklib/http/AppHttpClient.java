package com.app.tanklib.http;

import com.app.tanklib.util.StringUtil;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class AppHttpClient {

	private final DefaultHttpClient mHttpClient;
	private static final int TIMEOUT = 15;

	public AppHttpClient() {
		SchemeRegistry supportedSchemes = new SchemeRegistry();
		SocketFactory sf = PlainSocketFactory.getSocketFactory();
		supportedSchemes.register(new Scheme("http", sf, 80));
		supportedSchemes.register(new Scheme("https", SSLSocketFactory
				.getSocketFactory(), 443));
		HttpParams httpParams = createHttpParams();
		HttpClientParams.setRedirecting(httpParams, false);
		final ClientConnectionManager ccm = new ThreadSafeClientConnManager(
				httpParams, supportedSchemes);
		mHttpClient = new DefaultHttpClient(ccm, httpParams);
		// mHttpClient.setHttpRequestRetryHandler(requestRetryHandler);
	}

	// /**
	// * 设置重连机制和异常自动恢复处理
	// */
	// private static HttpRequestRetryHandler requestRetryHandler = new
	// HttpRequestRetryHandler() {
	// // 自定义的恢复策略
	// public boolean retryRequest(IOException exception, int executionCount,
	// HttpContext context) {
	// System.out.println("executionCount  " + executionCount);
	// System.out.println("sleep start ... ");
	// try {
	// Thread.sleep(2 * 1000);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// System.out.println("sleep end ... ");
	// // 设置恢复策略，在Http请求发生异常时候将自动重试3次
	// if (executionCount >= 3) {
	// // Do not retry if over max retry count
	// return false;
	// }
	// if (exception instanceof NoHttpResponseException) {
	// // Retry if the server dropped connection on us
	// return true;
	// }
	// if (exception instanceof SSLHandshakeException) {
	// // Do not retry on SSL handshake exception
	// return false;
	// }
	// HttpRequest request = (HttpRequest) context
	// .getAttribute(ExecutionContext.HTTP_REQUEST);
	// boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
	// if (!idempotent) {
	// // Retry if the request is considered idempotent
	// return true;
	// }
	// return false;
	// }
	// };

	public String executeHttpRequests(String url) throws Exception {
		System.out.println("url : " + url);
		HttpGet get = createHttpGet(url);
		return executeHttpRequests(executeHttpRequest(get));
	}

	public String executeHttpRequests(String url,
			NameValuePair... nameValuePairs) throws Exception {
		HttpGet get = null;
		if (null != nameValuePairs) {
			get = createHttpGet(url, nameValuePairs);
		} else {
			get = createHttpGet(url);
		}
		return executeHttpRequests(executeHttpRequest(get));
	}

	public String executeHttpRequests(String url,
			ArrayList<BsoftNameValuePair> nameValuePairs) throws Exception {
		HttpGet get = null;
		if (null != nameValuePairs) {
			get = createHttpGet(url, nameValuePairs);
		} else {
			get = createHttpGet(url);
		}
		return executeHttpRequests(executeHttpRequest(get));
	}

	public String executeHttpRequests(HttpResponse response) throws Exception {
		int statusCode = response.getStatusLine().getStatusCode();
		switch (statusCode) {
		case 200:
			return EntityUtils.toString(response.getEntity(), "UTF-8");

		case 400:
			throw new ApiHttpException(response.getStatusLine().toString(),
					EntityUtils.toString(response.getEntity()));

		case 401:
			response.getEntity().consumeContent();
			throw new ApiHttpException(response.getStatusLine().toString());

		case 404:
			response.getEntity().consumeContent();
			throw new ApiHttpException(response.getStatusLine().toString());

		case 500:
			response.getEntity().consumeContent();
			throw new ApiHttpException("Service is down. Try again later.");

		default:
			response.getEntity().consumeContent();
			throw new ApiHttpException("Error connecting to Service: "
					+ statusCode + ". Try again later.");
		}
	}

	public String doHttpPost(String url, NameValuePair... nameValuePairs)
			throws Exception {
		HttpPost httpPost = createHttpPost(url, nameValuePairs);
		HttpResponse response = executeHttpRequest(httpPost);
		switch (response.getStatusLine().getStatusCode()) {
		case 200:
			try {
				return EntityUtils.toString(response.getEntity());
			} catch (ParseException e) {
				throw new ApiHttpException(e.getMessage());
			}

		case 401:
			response.getEntity().consumeContent();
			throw new ApiHttpException(response.getStatusLine().toString());

		case 404:
			response.getEntity().consumeContent();
			throw new ApiHttpException(response.getStatusLine().toString());

		default:
			response.getEntity().consumeContent();
			throw new ApiHttpException(response.getStatusLine().toString());
		}
	}

	public String doHttpPost(String url,
			ArrayList<BsoftNameValuePair> nameValuePairs) throws Exception {
		HttpPost httpPost = createHttpPost(url, nameValuePairs);
		HttpResponse response = executeHttpRequest(httpPost);
		System.out.println(url);
		System.out.println(nameValuePairs.toString());
		switch (response.getStatusLine().getStatusCode()) {
		case 200:
			try {
				return EntityUtils.toString(response.getEntity());
			} catch (ParseException e) {
				throw new ApiHttpException(e.getMessage());
			}

		case 401:
			response.getEntity().consumeContent();
			throw new ApiHttpException(response.getStatusLine().toString());

		case 404:
			response.getEntity().consumeContent();
			throw new ApiHttpException(response.getStatusLine().toString());

		default:
			response.getEntity().consumeContent();
			throw new ApiHttpException(response.getStatusLine().toString());
		}
	}

	/**
	 * execute() an httpRequest catching exceptions and returning null instead.
	 * 
	 * @param httpRequest
	 * @return
	 * @throws IOException
	 */
	public HttpResponse executeHttpRequest(HttpRequestBase httpRequest)
			throws IOException {
		try {
			mHttpClient.getConnectionManager().closeExpiredConnections();
			return mHttpClient.execute(httpRequest);
		} catch (IOException e) {
			httpRequest.abort();
			throw e;
		}
	}

	public HttpGet createHttpGet(String url) {
		HttpGet httpGet = new HttpGet(url);
		// httpGet.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
		return httpGet;
	}

	public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs) {
		String query = URLEncodedUtils.format(stripNulls(nameValuePairs),
				HTTP.UTF_8);
		HttpGet httpGet = new HttpGet(url + "?" + query);

		System.out.println(url + "?" + query);

		// httpGet.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
		return httpGet;
	}

	public HttpGet createHttpGet(String url,
			ArrayList<BsoftNameValuePair> nameValuePairs) {
		String query = URLEncodedUtils.format(stripNulls(nameValuePairs),
				HTTP.UTF_8);
		HttpGet httpGet = new HttpGet(url + "?" + query);

		System.out.println(url + "?" + query);

		// httpGet.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
		return httpGet;
	}

	public HttpPost createHttpPost(String url, NameValuePair... nameValuePairs) {
		HttpPost httpPost = new HttpPost(url);
		// httpPost.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(
					stripNulls(nameValuePairs), HTTP.UTF_8));
		} catch (UnsupportedEncodingException e1) {
			throw new IllegalArgumentException(
					"Unable to encode http parameters.");
		}
		return httpPost;
	}

	public HttpPost createHttpPost(String url,
			ArrayList<BsoftNameValuePair> nameValuePairs) {
		HttpPost httpPost = new HttpPost(url);
		// httpPost.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
					HTTP.UTF_8));
		} catch (UnsupportedEncodingException e1) {
			throw new IllegalArgumentException(
					"Unable to encode http parameters.");
		}
		return httpPost;
	}

	public HttpPost createHttpFilePost(String url,
			NameValuePair... nameValuePairs) {
		HttpPost httpPost = new HttpPost(url);
		// httpPost.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(
					stripNulls(nameValuePairs), HTTP.UTF_8));
		} catch (UnsupportedEncodingException e1) {
			throw new IllegalArgumentException(
					"Unable to encode http parameters.");
		}
		return httpPost;
	}

	private List<NameValuePair> stripNulls(NameValuePair... nameValuePairs) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (int i = 0; i < nameValuePairs.length; i++) {
			NameValuePair param = nameValuePairs[i];
			if (param.getValue() != null) {
				params.add(param);
			}
		}
		return params;
	}

	private List<NameValuePair> stripNulls(
			ArrayList<BsoftNameValuePair> nameValuePairs) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (int i = 0; i < nameValuePairs.size(); i++) {
			NameValuePair param = nameValuePairs.get(i);
			if (param.getValue() != null) {
				params.add(param);
			}
		}
		return params;
	}

	/**
	 * Create the default HTTP protocol parameters.
	 */
	private static final HttpParams createHttpParams() {
		final HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setStaleCheckingEnabled(params, false);

		HttpConnectionParams.setConnectionTimeout(params, TIMEOUT * 1000);
		HttpConnectionParams.setSoTimeout(params, TIMEOUT * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);

		return params;
	}

	public String doHttpPost(String url, String srcPath,
			NameValuePair... nameValuePairs) throws Exception {
		HttpPost httpPost = new HttpPost(url);

		MultipartEntity multipartEntity = new MultipartEntity();
		multipartEntity.addPart("file", new FileBody(new File(srcPath)));

		if (null != nameValuePairs) {
			for (int i = 0; i < nameValuePairs.length; i++) {
				multipartEntity.addPart(
						nameValuePairs[i].getName(),
						new StringBody(URLEncoder.encode(
								nameValuePairs[i].getValue(), "UTF-8")));
			}
		}

		httpPost.setEntity(multipartEntity);
		HttpResponse response = mHttpClient.execute(httpPost);

		switch (response.getStatusLine().getStatusCode()) {
		case 200:
			try {
				return EntityUtils.toString(response.getEntity());
			} catch (ParseException e) {
				throw new ApiHttpException(e.getMessage());
			}

		case 401:
			response.getEntity().consumeContent();
			throw new ApiHttpException(response.getStatusLine().toString());

		case 404:
			response.getEntity().consumeContent();
			throw new ApiHttpException(response.getStatusLine().toString());

		default:
			response.getEntity().consumeContent();
			throw new ApiHttpException(response.getStatusLine().toString());
		}
	}

	public String doHttpPost(String url, String[] srcPath,
			NameValuePair... nameValuePairs) throws Exception {
		HttpPost httpPost = new HttpPost(url);

		MultipartEntity multipartEntity = new MultipartEntity();
		if (null != srcPath && srcPath.length > 0) {
			for (int i = 0; i < srcPath.length; i++) {
				multipartEntity.addPart("file", new FileBody(new File(
						srcPath[i])));
			}
		}

		if (null != nameValuePairs) {
			for (int i = 0; i < nameValuePairs.length; i++) {
				multipartEntity.addPart(
						nameValuePairs[i].getName(),
						new StringBody(URLEncoder.encode(
								nameValuePairs[i].getValue(), "UTF-8")));
			}
		}

		httpPost.setEntity(multipartEntity);
		HttpResponse response = mHttpClient.execute(httpPost);

		switch (response.getStatusLine().getStatusCode()) {
		case 200:
			try {
				return EntityUtils.toString(response.getEntity());
			} catch (ParseException e) {
				throw new ApiHttpException(e.getMessage());
			}

		case 401:
			response.getEntity().consumeContent();
			throw new ApiHttpException(response.getStatusLine().toString());

		case 404:
			response.getEntity().consumeContent();
			throw new ApiHttpException(response.getStatusLine().toString());

		default:
			response.getEntity().consumeContent();
			throw new ApiHttpException(response.getStatusLine().toString());
		}
	}

	public String doHttpPostUTF8(String url, NameValuePair... nameValuePairs)
			throws Exception {
		// HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);

		MultipartEntity multipartEntity = new MultipartEntity();

		if (null != nameValuePairs) {
			for (int i = 0; i < nameValuePairs.length; i++) {
				multipartEntity.addPart(
						nameValuePairs[i].getName(),
						new StringBody(URLEncoder.encode(
								nameValuePairs[i].getValue(), "UTF-8")));
			}
		}

		httpPost.setEntity(multipartEntity);
		HttpResponse response = mHttpClient.execute(httpPost);

		switch (response.getStatusLine().getStatusCode()) {
		case 200:
			try {
				return EntityUtils.toString(response.getEntity());
			} catch (ParseException e) {
				throw new ApiHttpException(e.getMessage());
			}

		case 401:
			response.getEntity().consumeContent();
			throw new ApiHttpException(response.getStatusLine().toString());

		case 404:
			response.getEntity().consumeContent();
			throw new ApiHttpException(response.getStatusLine().toString());

		default:
			response.getEntity().consumeContent();
			throw new ApiHttpException(response.getStatusLine().toString());
		}
	}

	public String doPostRequestInput(String urlStr, String psotString,
			NameValuePair... nameValuePairs) {
		HttpURLConnection uc = null;
		try {
			for (int i = 0; i < nameValuePairs.length; i++) {
				if (i == 0) {
					urlStr += "?" + nameValuePairs[i].getName() + "=";
					urlStr += nameValuePairs[i].getValue();
				} else {
					urlStr += "&" + nameValuePairs[i].getName() + "=";
					urlStr += nameValuePairs[i].getValue();
				}
			}
			URL url = new URL(urlStr);

			uc = (HttpURLConnection) url.openConnection();
			uc.setRequestProperty("content-type", "application/json");

			uc.setDoInput(true);
			uc.setDoOutput(true);
			uc.setConnectTimeout(1000 * 10);
			uc.setReadTimeout(1000 * 10);
			uc.setRequestMethod("POST");

			uc.getOutputStream().write(psotString.getBytes("UTF-8"));
			uc.getOutputStream().close();

			InputStream content = (InputStream) uc.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					content, "UTF-8"));
			String line = in.readLine();
			if (line != null) {
				return line.trim();
			}
			in.close();
		} catch (FileNotFoundException fe) {
			fe.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (null != uc) {
				uc.disconnect();
			}
		}
		return null;
	}

	public String doHttpPostEhr(String url, String[] recipes, String[] reports,
			ArrayList<BsoftNameValuePair> nameValuePairs) throws Exception {

		System.out.println("doHttpPostEhr recipes : " + recipes);
		System.out.println("doHttpPostEhr reports : " + reports);

		HttpPost httpPost = new HttpPost(url);

		MultipartEntity multipartEntity = new MultipartEntity();
		if (null != recipes && recipes.length > 0) {
			for (int i = 0; i < recipes.length; i++) {
				if (!StringUtil.isEmpty(recipes[i])) {
					multipartEntity.addPart("recipes", new FileBody(new File(
							recipes[i])));
					System.out.println("recipes " + recipes[i]);
				}
			}
		}
		if (null != reports && reports.length > 0) {
			for (int i = 0; i < reports.length; i++) {
				if (!StringUtil.isEmpty(reports[i])) {
					multipartEntity.addPart("reports", new FileBody(new File(
							reports[i])));
					System.out.println("reports " + reports[i]);
				}
			}
		}

		if (null != nameValuePairs) {
			for (int i = 0; i < nameValuePairs.size(); i++) {
				// multipartEntity.addPart(
				// nameValuePairs.get(i).getName(),
				// new StringBody(URLEncoder.encode(nameValuePairs.get(i)
				// .getValue(), "UTF-8")));
				multipartEntity.addPart(nameValuePairs.get(i).getName(),
						new StringBody(nameValuePairs.get(i).getValue(),
								Charset.forName("UTF-8")));
			}
		}

		httpPost.setEntity(multipartEntity);
		HttpResponse response = mHttpClient.execute(httpPost);

		switch (response.getStatusLine().getStatusCode()) {
		case 200:
			try {
				return EntityUtils.toString(response.getEntity());
			} catch (ParseException e) {
				throw new ApiHttpException(e.getMessage());
			}

		case 401:
			response.getEntity().consumeContent();
			throw new ApiHttpException(response.getStatusLine().toString());

		case 404:
			response.getEntity().consumeContent();
			throw new ApiHttpException(response.getStatusLine().toString());

		default:
			response.getEntity().consumeContent();
			throw new ApiHttpException(response.getStatusLine().toString());
		}
	}

	public String doHttpPostHeader(String url, String header,
			ArrayList<BsoftNameValuePair> nameValuePairs) throws Exception {
		HttpPost httpPost = new HttpPost(url);

		MultipartEntity multipartEntity = new MultipartEntity();
		if (!StringUtil.isEmpty(header)) {
			multipartEntity.addPart("file", new FileBody(new File(header)));
		}

		if (null != nameValuePairs) {
			for (int i = 0; i < nameValuePairs.size(); i++) {
				// multipartEntity.addPart(
				// nameValuePairs.get(i).getName(),
				// new StringBody(URLEncoder.encode(nameValuePairs.get(i)
				// .getValue(), "UTF-8")));
				multipartEntity.addPart(nameValuePairs.get(i).getName(),
						new StringBody(nameValuePairs.get(i).getValue(),
								Charset.forName("UTF-8")));
			}
		}

		httpPost.setEntity(multipartEntity);
		HttpResponse response = mHttpClient.execute(httpPost);

		switch (response.getStatusLine().getStatusCode()) {
		case 200:
			try {
				return EntityUtils.toString(response.getEntity());
			} catch (ParseException e) {
				throw new ApiHttpException(e.getMessage());
			}

		case 401:
			response.getEntity().consumeContent();
			throw new ApiHttpException(response.getStatusLine().toString());

		case 404:
			response.getEntity().consumeContent();
			throw new ApiHttpException(response.getStatusLine().toString());

		default:
			response.getEntity().consumeContent();
			throw new ApiHttpException(response.getStatusLine().toString());
		}
	}
}
