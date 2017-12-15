package Authentication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.*;

public class Auth {
	
	String userName;
	String password;
	String scope;
	String token;
	String tokenType;
	String refreshToken;
	String organizationGuid;
	String managerURL;
	String developerURL;
	String auditorURL;
	long expiryTime;
	
	Auth(){
		this.scope = "";
		this.token = "";
		this.tokenType = "";
		this.expiryTime = 0;
		this.refreshToken = "";
	}
	
	Auth(String userName, String password){
		this.userName = userName;
		this.password = password;
		this.scope = "";
		this.token = "";
		this.tokenType = "";
		this.expiryTime = 0;
		this.refreshToken = "";
	}
	
	public void setCertificate() throws NoSuchAlgorithmException, KeyManagementException {
		/*Managing the Certificate Issue */
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
		      public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		        return null;
		      }

		      public void checkClientTrusted(X509Certificate[] certs, String authType) {
		      }

		      public void checkServerTrusted(X509Certificate[] certs, String authType) {
		      }
		    } };

		    SSLContext sc = SSLContext.getInstance("SSL");
		    sc.init(null, trustAllCerts, new java.security.SecureRandom());
		    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		    
	}
	
	/*
	 * Post Body
	 * 
	 * */
	public String buildRequestBody() {
		StringBuilder p = new StringBuilder();
		p.append("username=");
		p.append(this.userName);
		p.append("&password=");
		p.append(this.password);
		p.append("&grant_type=password&scope=");
		return p.toString();
	}
	
	
	public void extractInfo(String response) throws JSONException {
		JSONObject j = new JSONObject(response);
		this.token = j.getString("access_token");
		this.scope = j.getString("scope");
		this.expiryTime = j.getLong("expires_in");
		this.refreshToken = j.getString("refresh_token");
		this.tokenType = j.getString("token_type");
	}
	
	public void extractOrginfo(String response) throws JSONException{
		JSONObject j = new JSONObject(response);
		JSONArray l = j.getJSONArray("resources");
		JSONObject m = l.getJSONObject(1);//("entity");
		JSONObject p = m.getJSONObject("entity");
		this.organizationGuid = p.getString("organization_guid");
		this.managerURL = p.getString("managers_url");
		this.developerURL = p.getString("developers_url");
		this.auditorURL = p.getString("auditors_url");
	}
	
	public String convertResponseToString(InputStream response) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(response));
		String output;
		StringBuffer res = new StringBuffer();
		
		while((output= in.readLine()) != null) {
			res.append(output);
		}
		in.close();
		System.out.println("Response Message : " + res.toString());
		return res.toString();
	}
	
	public void getToken() throws IOException, NoSuchAlgorithmException, KeyManagementException, JSONException  {
		
		URL url = new URL("https://uaa.bosh-lite.com/oauth/token");
		
		setCertificate();   
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		//Setting the Headers	
		con.setRequestMethod("POST");
		con.setRequestProperty("Accept", "application/json" );
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("Authorization", "Basic Y2Y6");
		con.setRequestProperty("X-UAA-Endpoint" , "UAA_Endpoint");
		
		//Setting the body parameters
		
		String param = buildRequestBody();
		byte[] postData = param.getBytes(StandardCharsets.UTF_8);
		int postDataLength = postData.length;
		
		//send post request
		
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(postData);
		wr.close();
		
		//Sending Post Request for Token
		int responseCode = con.getResponseCode();
		String response = convertResponseToString(con.getInputStream());
		
		System.out.println("URL :  " + url.toString() );
		System.out.println("response Code : " + con.getResponseCode());
		
		extractInfo(response);
		
	
	} 
	
	public void extractManagerInfo(String response){
		JSONObject j = new JSONObject(response);
		JSONArray l = j.getJSONArray("resources");
		JSONObject p = l.getJSONObject(0);
		JSONObject m = p.getJSONObject("entity");
		System.out.println("$DATE " + m );
	//	JSONObject m = l.getJSONObject(1);//("entity");
	//	JSONObject p = m.getJSONObject("entity");
	}
	
	public void getManager() throws IOException, KeyManagementException, NoSuchAlgorithmException {
		URL url = new URL("https://api.bosh-lite.com"+this.managerURL);
		setCertificate();
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		//send get request
		con.setRequestMethod("GET");
		
		//adding request header
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", "bearer "+this.token);
		
		
		System.out.println();
		int responseCode = con.getResponseCode();
		String response = convertResponseToString(con.getInputStream());
	//	this.extractManagerInfo(response);
		System.out.println("URL : " + url);
		System.out.println("response code for v2"+this.managerURL + " "  + responseCode);
	
	}
	
	public void getAuditor() throws IOException, KeyManagementException, NoSuchAlgorithmException {
		URL url = new URL("https://api.bosh-lite.com"+this.auditorURL);
		setCertificate();
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		//send get request
		con.setRequestMethod("GET");
		
		//adding request header
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", "bearer "+this.token);
		
		
		System.out.println();
		int responseCode = con.getResponseCode();
		String response = convertResponseToString(con.getInputStream());
		this.extractManagerInfo(response);
		System.out.println("URL : " + url);
		System.out.println("response code for v2"+this.auditorURL + " "  + responseCode);
	
	}
	
	public void getDeveloper() throws IOException, KeyManagementException, NoSuchAlgorithmException {
		URL url = new URL("https://api.bosh-lite.com"+this.auditorURL);
		setCertificate();
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		//send get request
		con.setRequestMethod("GET");
		
		//adding request header
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", "bearer "+this.token);
		
		
		System.out.println();
		int responseCode = con.getResponseCode();
		String response = convertResponseToString(con.getInputStream());
	//	this.extractManagerInfo(response);
		System.out.println("URL : " + url);
		System.out.println("response code for v2"+this.developerURL + " "  + responseCode);
	
	}
	
	public void getSpaces() throws KeyManagementException, NoSuchAlgorithmException, IOException {
		
		URL url = new URL("https://api.bosh-lite.com/v2/spaces");
		setCertificate();
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		//send get request
		con.setRequestMethod("GET");
		
		//adding request header
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", "bearer "+this.token);
		
		
		System.out.println();
		int responseCode = con.getResponseCode();
		String response = convertResponseToString(con.getInputStream());
		this.extractOrginfo(response);
		System.out.println("URL : " + url);
		System.out.println("response code for v2 " + responseCode);
		System.out.println("Organisation GUID " + this.organizationGuid);
		
	}
	/*
	public void getSpaces() throws KeyManagementException, NoSuchAlgorithmException, IOException {
		
		URL url = new URL("https://api.bosh-lite.com/v2/spaces");
		setCertificate();
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		//send get request
		con.setRequestMethod("GET");
		
		//adding request header
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", "bearer "+this.token);
		
		
		System.out.println();
		int responseCode = con.getResponseCode();
		String response = convertResponseToString(con.getInputStream());
		this.extractOrginfo(response);
		System.out.println("URL : " + url);
		System.out.println("response code for v2 " + responseCode);
		System.out.println("Organisation GUID " + this.organizationGuid);
		
	}
	public void getRoles() {
		
	}
	*/
	public void print() {
		System.out.println("Printing Details \n");
		System.out.println("Token : " + this.token);
		System.out.println("Scope : " + this.scope);
		System.out.println("Refresh Token : " + this.refreshToken);
		System.out.println("Token Type : " + this.tokenType);
		System.out.println("Expiry Time : " + this.expiryTime);
	}

	public static void main(String[] args) throws IOException, KeyManagementException, NoSuchAlgorithmException, JSONException {
		
		Auth u = new Auth("a","1234");
		u.getToken();
		u.print();
		u.getSpaces();
		u.getManager();
		u.getAuditor();
		u.getDeveloper();
	}
}