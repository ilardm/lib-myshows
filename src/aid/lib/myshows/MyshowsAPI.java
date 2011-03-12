package aid.lib.myshows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.security.MessageDigest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class MyshowsAPI {
	final protected String URL_API="http://api.myshows.ru/profile/";
	
	protected String user=null;
	protected String password=null;
	
	protected HttpClient httpClient=null;
	
	public MyshowsAPI(String _user, String _password) {
		user=_user;
		
		// get md5 hash of password
		// http://www.spiration.co.uk/post/1199/Java%20md5%20example%20with%20MessageDigest
		try {
			MessageDigest algorithm=MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(_password.getBytes());
			
			byte[] hashDigest=algorithm.digest();
			
			StringBuffer hexString=new StringBuffer();
			for ( int i=0; i<hashDigest.length; i++ ) {
				String  hex=Integer.toHexString( 0xFF & hashDigest[i] );
				if ( hex.length()==1 ) {
					hex="0"+hex;
				}
				hexString.append(hex);
			}
			
			password=hexString.toString();
			
			// debug
			System.out.println("password: "+password);
			
		} catch (Exception e) {
			System.err.println("--- oops: "+e.toString());
			e.printStackTrace();
			
			password=null;
		}
	}
	
	public boolean login() {
		
		if ( password==null ) {
			// debug
			System.err.println("--- password=null");
			return false;
		}
		
		String URLs=String.format(URL_API+"login?login=%1$s&password=%2$s", user, password);
    	
		try {
			httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(URLs);
			
			HttpResponse responce = httpClient.execute(httpGet);
			
			// TODO: rewrite checking logged in (?)
			if ( responce.getStatusLine().getStatusCode()==HttpURLConnection.HTTP_OK ) {
				return true;
			} else {
				HttpEntity entity=responce.getEntity();
				if ( entity!=null ) {
					BufferedReader inputStream = new BufferedReader(
							new InputStreamReader( entity.getContent() )
							);
					String answer = "";
					String line;
					while ( (line = inputStream.readLine()) != null ) {
						answer += (line + "\n");
					}
					System.out.println("answer: >>>\n" + answer + "<<<");
				}
			}
			
		} catch (Exception e) {
			System.err.println("--- oops: "+e.getMessage());
			e.printStackTrace();
		}
		
		return false;
	}
}
