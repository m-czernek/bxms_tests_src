package utils;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class AbstractJUnitTest {
	public HTTPClient client;
	URL url;
	
	public AbstractJUnitTest() throws MalformedURLException {
		this("kieserver","kieserver1!",new URL("http://localhost:8080/business-central/rest/"));
	}
	
	public AbstractJUnitTest(String user, String password, URL url) {
		client = new HTTPClient(user,password,url);
		this.url = url;
	}
	
	public String setClientURL(String url){
		String newUrl = client.getUrl().toString()+url;
		try {
			client.setUrl(new URL(newUrl));
			return "0";
		} catch (MalformedURLException e) {
			return e.toString();
		}
	}
	
	public void resetClientURL(){
		client.setUrl(url);
	}

}
