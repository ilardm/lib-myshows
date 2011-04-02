/*
Copyright (c) 2011, Ilya Arefiev <arefiev.id@gmail.com>
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:
 * Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the
   distribution.
 * Neither the name of the author nor the names of its
   contributors may be used to endorse or promote products derived
   from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package aid.lib.myshows;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * @author Ilya Arefiev (arefiev.id@gmail.com)
 *
 */
public class MyshowsClient {
	protected MyshowsAPI api=null;
	
	public MyshowsClient() {
		api=new MyshowsAPI();
		
		System.out.println("+++ MyshowsClient()");
		
		// TODO: check if offline
	}
	
	public boolean login(String _username, String _password) {
		System.out.println("+++ login(String "+_username+", String "+_password+")");
		
		return api.login(_username, _password);
	}
	
	public boolean logout() {
		System.out.println("+++ logout()");
		
		return false;
	}
	
	public JSONArray getShows() {
		System.out.println("+++ getShows()");
		
		JSONObject shows=null;
		JSONArray ret=null;
		String result=api.getShows();
		
		if ( result!=null ) {
			try {
				// put shows in jsonobject{ "showid":{"info"} }
				shows=new JSONObject(result);
				
				System.out.println(shows.toString(2));
				// get all "showid"
				Iterator<String> iter = shows.keys();
				
				ret=new JSONArray();
				
				// add info into jsonarray
				while ( iter.hasNext() ) {
					ret.put(
							shows.getJSONObject( iter.next() )
							);
				}
				
			} catch (Exception e) {
				System.err.println("--- oops: "+e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.err.println("--- null from API call");
		}
		
		return ret;
	}
	
	public JSONArray getUnwatchedEpisodes(int _show) {
		System.out.println("+++ getUnwatchedEpisodes("+_show+")");
		
		JSONObject unwatched=null;
		JSONArray ret=null;
		String result=api.getUnwatchedEpisodes();
		
		if ( result!=null ) {
			try {
				// put episodes in jsonobject{ "showid":{"info"} }
				unwatched=new JSONObject(result);
				
				System.out.println(unwatched.toString(2));
				// get all "showid"
				Iterator<String> iter = unwatched.keys();
				
				ret=new JSONArray();
				
				JSONObject episode=null;
				
				// add info into jsonarray
				while ( iter.hasNext() ) {
					episode=unwatched.getJSONObject( iter.next() );
					
					// filter by _show if exists
					if ( _show>0 && episode.getInt("showId")==_show ) {
						ret.put( episode );
					} else if ( _show<0 ) {
						ret.put( episode );
					}
				}
				
			} catch (Exception e) {
				System.err.println("--- oops: "+e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.err.println("--- null from API call");
		}
		
		return ret;
	}
	
	public JSONArray getNextEpisodes(int _show) {
		System.out.println("+++ getNextEpisodes("+_show+")");
		
		JSONObject next=null;
		JSONArray ret=null;
		String result=api.getNextEpisodes();
		
		if ( result!=null ) {
			try {
				// put episodes in jsonobject{ "showid":{"info"} }
				next=new JSONObject(result);
				
				System.out.println(next.toString(2));
				// get all "showid"
				Iterator<String> iter = next.keys();
				
				ret=new JSONArray();
				
				JSONObject episode=null;
				
				// add info into jsonarray
				while ( iter.hasNext() ) {
					episode=next.getJSONObject( iter.next() );
					
					// filter by _show if exists
					if ( _show>0 && episode.getInt("showId")==_show ) {
						ret.put( episode );
					} else if ( _show<0 ) {
						ret.put( episode );
					} else {
						continue;
					}
				}
				
			} catch (Exception e) {
				System.err.println("--- oops: "+e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.err.println("--- null from API call");
		}
		
		return ret;
	}
	
	public JSONArray getSeenEpisodes(int _show) {
		System.out.println("+++ getSeenEpisodes(int "+_show+")");
		
		JSONObject seen=null;
		JSONArray ret=null;
		String result=api.getSeenEpisodes(_show);
		
		if ( result!=null ) {
			try {
				// put episodes in jsonobject{ "showid":{"info"} }
				seen=new JSONObject(result);
				
				System.out.println(seen.toString(2));
				// get all "showid"
				Iterator<String> iter = seen.keys();
				
				ret=new JSONArray();
				
				// add info into jsonarray
				while ( iter.hasNext() ) {
					ret.put(
							seen.getJSONObject( iter.next() )
							);
				}
				
			} catch (Exception e) {
				System.err.println("--- oops: "+e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.err.println("--- null from API call");
		}
		
		return ret;
	}
	
	public boolean checkEpisode(int _episode) {
		System.out.println("+++ checkEpisode(int "+_episode+")");
		
		return api.checkEpisode(_episode);
	}
	
	public boolean checkEpisode(int _episode, int _ratio) {
		System.out.println("+++ checkEpisode(int "+_episode+", int "+_ratio+")");
		
		return api.checkEpisode(_episode, _ratio);
	}
	
	public boolean unCheckEpisode(int _episode) {
		System.out.println("+++ unCheckEpisode(int "+_episode+")");
		
		return api.unCheckEpisode(_episode);
	}
}
