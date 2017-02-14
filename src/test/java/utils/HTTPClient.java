package utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HTTPClient {
	URL url;
	String user;
	String pasword;
	
	public HTTPClient(String user,String password, URL url){
		this.url = url;
		this.user = user;
		this.pasword = password;
	}
	
	
	public String PostRequest(String data, Map<String,String> flags) throws IOException{
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		conn.setRequestMethod("POST");
		
		Set set = flags.entrySet();
		Iterator i = set.iterator();
		while(i.hasNext()){
			Map.Entry me = (Map.Entry)i.next();
			conn.setRequestProperty(me.getKey().toString(),me.getValue().toString());
		}
		
		final String pw = this.pasword;
		if(user != null && pw != null){
			Authenticator.setDefault (new Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			        return new PasswordAuthentication (user, pw.toCharArray());
			    }
			});
		}
		
		
			conn.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			if(data != null){
				wr.writeBytes(data);
			}
			else{
				wr.writeBytes("");
			}
			wr.flush();
			wr.close();
			
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Response code: " + conn.getResponseCode());
			
		
		
		
		try{
			BufferedReader in = new BufferedReader( new InputStreamReader(conn.getInputStream() ) );
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			conn.disconnect();
			return response.toString();

			
		}
		catch(IOException e){
			conn.disconnect();
			return "No response was received due to: " + e;
			
		}
	}


	public URL getUrl() {
		return url;
	}


	public void setUrl(URL url) {
		this.url = url;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public String getPasword() {
		return pasword;
	}


	public void setPasword(String pasword) {
		this.pasword = pasword;
	}


	@Override
	public String toString() {
		return "HTTPClient [url=" + url + ", user=" + user + ", pasword=" + pasword + "]";
	}
	
	

}
