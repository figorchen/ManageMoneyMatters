package com.uuzz.android.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpURLConnectionHelper {

//	public static final String URL_PREFIX = "http://211.137.203.113:11003/agent_client/agt_tc_android/normal/";
    public static final String URL_PREFIX = "http://124.128.16.98:22002/agent_client/agt_tc_android/normal/";

//    public static final String URL_PREFIX = "http://192.168.2.23:8888/agent_client/agt_tc_android/normal/";

	public static class ReturnClass {
		public int httpResponseCode;
		public String httpResponseMessage;
		public String httpReturnMessage; // 200的时候有效

		public ReturnClass(int c, String m, String m1) {
			httpResponseCode = c;
			httpResponseMessage = m;
			httpReturnMessage = m1;
		}
	}

	public static String sendGet(String url, String params, String cookie, String csrfToken) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlName = url + "?" + params;
			URL realUrl = new URL(urlName);
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setConnectTimeout(60000);
			conn.setReadTimeout(60000);
			if (null != cookie && !"".equals(cookie)) {
				conn.setRequestProperty("Cookie", cookie);
			}
			if (null != csrfToken && !"".equals(csrfToken)) {
				conn.setRequestProperty("X-CSRF-Token", csrfToken);
			}
			conn.connect();
			Map<String, List<String>> map = conn.getHeaderFields();
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}

			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line + "\n";
			}
		} catch (Exception e) {
			System.out.println("Http Get 异常" + e);
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

    public static ReturnClass sendPostUrlEncoded(String url, String params) throws IOException {
        return sendPostUrlEncoded(url, params, "", "");
    }

	// 通过ReturnClass来汇报发生IOException的情况，屏蔽掉IOException即可，当returnCode非200时，会发生这种情况
	public static ReturnClass sendPostUrlEncoded(String url, String params, String cookie, String csrfToken) throws IOException {
		String contentTypeStr = "application/x-www-form-urlencoded";

		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		HttpURLConnection conn = null;
		try {
			// System.setProperty("http.keepAlive", "false");
			URL realUrl = new URL(url);
			conn = (HttpURLConnection) realUrl.openConnection();

			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setConnectTimeout(60000);
			conn.setReadTimeout(60000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Content-Type", contentTypeStr);
			if (null != cookie && !"".equals(cookie)) {
				conn.setRequestProperty("Cookie", cookie);
			}
			if (null != csrfToken && !"".equals(csrfToken)) {
				conn.setRequestProperty("X-CSRF-Token", csrfToken);
			}
			conn.connect();
			out = new PrintWriter(conn.getOutputStream());
			out.print(params);
			out.flush();
//			InputStream ins = conn.getInputStream();
//			System.out.println(ins);
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line + "\n";
			}
		} catch (IOException e) {
			// http code != 200时，会到此分支
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return new ReturnClass(conn.getResponseCode(), conn.getResponseMessage(), result);
	}

	// POST /haoketang/?q=app/user/login.json HTTP/1.1
	// Host: 192.168.1.105
	// Cache-Control: no-cache
	//
	// ----WebKitFormBoundaryE19zNvXGzXaLvS5C
	// Content-Disposition: form-data; name="username"
	//
	// tiancheng
	// ----WebKitFormBoundaryE19zNvXGzXaLvS5C
	// Content-Disposition: form-data; name="password"
	//
	// tiancheng
	// ----WebKitFormBoundaryE19zNvXGzXaLvS5C
	public static ReturnClass sendPostMultiPartFromdata(String url, Map<String, String> mPostMap, String cookie, String csrfToken) throws IOException {
		String BASE_BOUNDARYSTR = "WebKitFormBoundaryKCtFPtDxcU9j4UBA";
		String contentTypeStr = "multipart/form-data" + "; boundary=----" + BASE_BOUNDARYSTR;

		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		HttpURLConnection conn = null;
		try {
			URL realUrl = new URL(url);
			conn = (HttpURLConnection) realUrl.openConnection();
			conn.setRequestProperty("Accept", "*/*");
			// conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setConnectTimeout(60000);
			conn.setReadTimeout(60000);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Cache-Control", "no-cache");
			conn.setRequestProperty("Content-Type", contentTypeStr);
			if (null != cookie && !"".equals(cookie)) {
				conn.setRequestProperty("Cookie", cookie);
			}
			if (null != csrfToken && !"".equals(csrfToken)) {
				conn.setRequestProperty("X-CSRF-Token", csrfToken);
			}
			conn.connect();
			out = new PrintWriter(conn.getOutputStream());
			StringBuilder sb = new StringBuilder();
			Iterator<String> it = mPostMap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				sb.append("------" + BASE_BOUNDARYSTR);
				sb.append("\r\n");
				sb.append("Content-Disposition: form-data; name=\"");
				sb.append(key);
				sb.append("\"\r\n\r\n");
				sb.append(mPostMap.get(key));
				sb.append("\r\n");
			}
			sb.append("------" + BASE_BOUNDARYSTR + "--");

			// post the string data.
			out.print(sb.toString());
			// 结束符
			out.flush();
			if (200 != conn.getResponseCode()) {

			}
//			InputStream ins = conn.getInputStream();
//			System.out.println(ins);
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line + "\n";
			}
			// out.close();
		} catch (IOException e) {
			// http code != 200时，会到此分支
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return new ReturnClass(conn.getResponseCode(), conn.getResponseMessage(), result);
	}

	public static ReturnClass sendPostJson(String url, String jsonStr) throws IOException {
		String contentTypeStr = "application/json";

		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		HttpURLConnection conn = null;
		try {
			URL realUrl = new URL(url);
			conn = (HttpURLConnection) realUrl.openConnection();
			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setConnectTimeout(60000);
			conn.setReadTimeout(60000);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Content-Type", contentTypeStr);
			out = new PrintWriter(conn.getOutputStream());

			// post the string data.
			out.print(jsonStr);
			out.flush();
//			InputStream ins = conn.getInputStream();
//			System.out.println(ins);
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line + "\n";
			}
		} catch (IOException e) {
			// http code != 200时，会到此分支
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return new ReturnClass(conn.getResponseCode(), conn.getResponseMessage(), result);
	}
}
