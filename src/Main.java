import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.*;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String URLs="http://api.myshows.ru/profile/login?login=demo&password=fe01ce2a7fbac8fafaed7c982a04e229";
    	
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(URLs);
			
			HttpResponse responce = httpClient.execute(httpGet);
			
			HttpEntity entity=responce.getEntity();
			if ( entity!=null ) {
				BufferedReader inputStream=new BufferedReader(
						new InputStreamReader(
								entity.getContent()
								)
						);
				String answer="";
				String line;
				while ( (line=inputStream.readLine())!=null ) {
					answer+=( line+"\n" );
				}
				System.out.println("answer: >>>\n"+answer+"<<<");
			}
			
			// get shows
			System.out.println("\n\tget shows");
			
			httpGet=new HttpGet("http://api.myshows.ru/profile/shows/");
			responce=httpClient.execute(httpGet);
			entity=responce.getEntity();
			if ( entity!=null ) {
				BufferedReader inputStream=new BufferedReader(
						new InputStreamReader(
								entity.getContent()
								)
						);
				String answer="";
				String line;
				while ( (line=inputStream.readLine())!=null ) {
					answer+=( line+"\n" );
				}
				System.out.println("answer: >>>\n"+answer+"<<<");
				
				if ( answer.startsWith("{") ) {
					JSONObject jo=new JSONObject(answer);
					System.out.println( jo.toString(2) );
				}
			}
			
		} catch (Exception e) {
			System.err.println("--- oops (main): " + e);
			e.printStackTrace();
		}
	}

}
