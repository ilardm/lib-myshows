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
	final protected String URL_API_LOGIN="http://api.myshows.ru/profile/login?login=%1$s&password=%2$s";
	final protected String URL_API_SHOWS="http://api.myshows.ru/profile/shows/";
	final protected String URL_API_EPISODES_UNWATCHED="http://api.myshows.ru/profile/episodes/unwatched/";
	final protected String URL_API_EPISODES_NEXT="http://api.myshows.ru/profile/episodes/next/";
	final protected String URL_API_EPISODES_SEEN="http://api.myshows.ru/profile/shows/%1$d/";
	
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
//			password="57da8c667d17b1b98b6c203cb1fc3d62";
			
			// debug
			System.out.println("password: "+password);
			
			httpClient = new DefaultHttpClient();
			
		} catch (Exception e) {
			System.err.println("--- oops: "+e.toString());
			e.printStackTrace();
			
			password=null;
		}
	}
	
	public boolean login() {
		
		if ( httpClient==null ) {
			// debug
			System.err.println("--- password=null");
			return false;
		}
		
		String URLs=String.format(URL_API_LOGIN, user, password);
    	
		try {
			HttpGet request = new HttpGet(URLs);
			
			HttpResponse response = httpClient.execute(request);
			
			// TODO: rewrite checking logged in (?)
			if ( response.getStatusLine().getStatusCode()==HttpURLConnection.HTTP_OK ) {
				request.abort();	// ~ close connection (?)
				return true;
			} else {
				HttpEntity entity=response.getEntity();
				if ( entity!=null ) {
					BufferedReader inputStream = new BufferedReader(
							new InputStreamReader( entity.getContent() )
							);
					String answer = "";
					String line;
					while ( (line = inputStream.readLine()) != null ) {
						answer += (line + "\n");
					}
					request.abort();	// ~ close connection (?)
					
					System.out.println("answer: >>>\n" + answer + "<<<");
				}
			}
			
		} catch (Exception e) {
			System.err.println("--- oops: "+e.getMessage());
			e.printStackTrace();
		}
		
		return false;
	}
	
	public String getShows() {
		if ( httpClient==null ) {
			return null;
		}
		
		try {
			HttpGet request=new HttpGet(URL_API_SHOWS);
			
			HttpResponse response=httpClient.execute(request);
			
			HttpEntity entity=response.getEntity();
			if ( entity!=null ) {
				BufferedReader inputStream = new BufferedReader(
						new InputStreamReader( entity.getContent() )
						);
				String answer = "";
				String line;
				while ( (line = inputStream.readLine()) != null ) {
					answer += (line + "\n");
				}
				request.abort();	// ~ close connection (?)
				
				// debug
				System.out.println("answer: >>>\n" + answer + "<<<");
				
				if ( response.getStatusLine().getStatusCode()==HttpURLConnection.HTTP_OK ) {
					return answer;
				} else {
					return null;
				}
			}
		} catch (Exception e) {
			System.err.println("--- oops: "+e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getUnwatchedEpisodes() {
		if ( httpClient==null ) {
			return null;
		}
		
		try {
			HttpGet request=new HttpGet(URL_API_EPISODES_UNWATCHED);
			
			HttpResponse response=httpClient.execute(request);
			
			HttpEntity entity=response.getEntity();
			if ( entity!=null ) {
				BufferedReader inputStream = new BufferedReader(
						new InputStreamReader( entity.getContent() )
						);
				String answer = "";
				String line;
				while ( (line = inputStream.readLine()) != null ) {
					answer += (line + "\n");
				}
				request.abort();	// ~ close connection (?)
				
				// debug
				System.out.println("answer: >>>\n" + answer + "<<<");
				
				if ( response.getStatusLine().getStatusCode()==HttpURLConnection.HTTP_OK ) {
					return answer;
				} else {
					return null;
				}
			}
		} catch (Exception e) {
			System.err.println("--- oops: "+e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getNextEpisodes() {
		if ( httpClient==null ) {
			return null;
		}
		
		try {
			HttpGet request=new HttpGet(URL_API_EPISODES_NEXT);
			
			HttpResponse response=httpClient.execute(request);
			
			HttpEntity entity=response.getEntity();
			if ( entity!=null ) {
				BufferedReader inputStream = new BufferedReader(
						new InputStreamReader( entity.getContent() )
						);
				String answer = "";
				String line;
				while ( (line = inputStream.readLine()) != null ) {
					answer += (line + "\n");
				}
				request.abort();	// ~ close connection (?)
				
				// debug
				System.out.println("answer: >>>\n" + answer + "<<<");
				
				if ( response.getStatusLine().getStatusCode()==HttpURLConnection.HTTP_OK ) {
					return answer;
				} else {
					return null;
				}
			}
		} catch (Exception e) {
			System.err.println("--- oops: "+e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getSeenEpisodes(int _show) {
		if ( httpClient==null || _show<0 ) {
			return null;
		}
		
		try {
			String URLs=String.format(URL_API_EPISODES_SEEN, _show);
			HttpGet request=new HttpGet(URLs);
			
			HttpResponse response=httpClient.execute(request);
			
			HttpEntity entity=response.getEntity();
			if ( entity!=null ) {
				BufferedReader inputStream = new BufferedReader(
						new InputStreamReader( entity.getContent() )
						);
				String answer = "";
				String line;
				while ( (line = inputStream.readLine()) != null ) {
					answer += (line + "\n");
				}
				request.abort();	// ~ close connection (?)
				
				// debug
				System.out.println("answer: >>>\n" + answer + "<<<");
				
				if ( response.getStatusLine().getStatusCode()==HttpURLConnection.HTTP_OK ) {
					return answer;
				} else {
					return null;
				}
			}
		} catch (Exception e) {
			System.err.println("--- oops: "+e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
}
