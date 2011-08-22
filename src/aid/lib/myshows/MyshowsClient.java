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
 * MyShows API public Client
 * @see <a href="http://api.myshows.ru/">http://api.myshows.ru/</a>
 * @author Ilya Arefiev (arefiev.id@gmail.com)
 *
 */
public class MyshowsClient {
	protected MyshowsAPI api=null;
	protected boolean loggedIn=false;
	
	/**
	 * dummy constructor<br>
	 * just creates {@link MyshowsAPI}
	 */
	public MyshowsClient() {
		api=new MyshowsAPI();
		
//		System.out.println("+++ MyshowsClient()");
		
		// TODO: check if offline
	}

	/**
	 * checks if logged in<br>
	 * flag is set to true on successful login
	 * @return <code>true</code> if logged in<br>
	 * 			<code>false</code> otherwise
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * login into username's account<br>
	 * calls <code>MyshowsAPI.login()</code>
	 * @param _username
	 * @param _password
	 * @return <code>true</code> if success<br>
	 * 			<code>false</code> otherwise 
	 */
	public boolean login(String _username, String _password) {
//		System.out.println("+++ login(String "+_username+", String "+_password+")");

		if ( api.login(_username, _password) )
		{
			loggedIn=true;
		}

		return loggedIn;
	}
	
	/**
	 * logout from user's account with clearing auth info<br>
	 * <b>currently does nothing</b>
	 * @return currently <code>false</code>
	 */
	public boolean logout() {
		// TODO: implement logout @ API && client
//		System.out.println("+++ logout()");
		if ( loggedIn ) {
			boolean result=api.logout();

			if ( result ) {
				loggedIn=false;
				return result;
			}
		}

		return false; // TODO: check if correct
	}

	/**
	 * get all shows (watching, canceled, etc) of user<br>
	 * calls <code>MyshowsAPI.getShows()</code><br>
	 * 
	 * {@link JSONArray} format:
		<pre>[
  {
    "rating": 0,
    "ruTitle": "$translated_title",
    "runtime": $episode_duration,
    "showId": $showId,
    "showStatus": "$show_status", // Canceled/Ended || Returning Series
    "title": "$original_title",
    "totalEpisodes": $num_of_totlat_episodes,
    "watchStatus": "$user's_watching_status",	// watching || cancelled
    "watchedEpisodes": $num_of_watched_episodes
  }
]
		</pre>
	 * @return {@link JSONArray} with shows if success<br>
	 * 			<code>null</code> otherwise
	 */
	public JSONArray getShows() {
//		System.out.println("+++ getShows()");

		if ( !loggedIn ) {
			return null;
		}

		JSONObject shows=null;
		JSONArray ret=null;
		String result=api.getShows();
		
		if ( result!=null ) {
			try {
				// put shows in jsonobject{ "showid":{"info"} }
				shows=new JSONObject(result);
				
//				System.out.println(shows.toString(2));
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
	
	/**
	 * get all unwatched episodes of all user's shows or filtered by
	 * <code>_show</code><br>
	 * calls <code>MyshowsAPI.getUnwatchedEpisodes()</code> and filter result by
	 * <code>_show</code> (if <code>_show</code>>0)<br>
	 * {@link JSONArray} format:
		<pre>[
  {
    "airDate": "$dd.mm.yyyy",
    "episodeId": $episodeId,
    "episodeNumber": $episode_number,
    "seasonNumber": $season_number,
    "showId": $showId,
    "title": "$original_episode_title"
  }
]
		</pre>
	 * @param _show $showId to filter unwatched episodes
	 * @return {@link JSONArray} with episodes if success<br>
	 * 			<code>null</code> otherwise
	 */
	public JSONArray getUnwatchedEpisodes(int _show) {
//		System.out.println("+++ getUnwatchedEpisodes("+_show+")");

		if ( !loggedIn ) {
			return null;
		}

		JSONObject unwatched=null;
		JSONArray ret=null;
		String result=api.getUnwatchedEpisodes();

		if ( result!=null ) {
			try {
				// put episodes in jsonobject{ "showid":{"info"} }
				unwatched=new JSONObject(result);
				
//				System.out.println(unwatched.toString(2));
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
	
	/**
	 * get next (future) episodes of all user's shows or filtered by
	 * <code>_show</code><br>
	 * calls <code>MyshowsAPI.getNextEpisodes()</code> and filter result by
	 * <code>_show</code> (if <code>_show</code>>0)<br>
	 * {@link JSONArray} format:
		<pre>[
  {
    "airDate": "$dd.mm.yyyy",
    "episodeId": $episodeId,
    "episodeNumber": $episode_number,
    "seasonNumber": $season_number,
    "showId": $showId,
    "title": "$original_episode_title"
  }
]
		</pre>
	 * @param _show $showId to filter next episodes
	 * @return {@link JSONArray} with episodes if success<br>
	 * 			<code>null</code> otherwise
	 */
	public JSONArray getNextEpisodes(int _show) {
//		System.out.println("+++ getNextEpisodes("+_show+")");

		if ( !loggedIn ) {
			return null;
		}

		JSONObject next=null;
		JSONArray ret=null;
		String result=api.getNextEpisodes();
		
		if ( result!=null ) {
			try {
				// put episodes in jsonobject{ "showid":{"info"} }
				next=new JSONObject(result);
				
//				System.out.println(next.toString(2));
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

	/**
	 * get {@link JSONArray} with ignored episodes<br>
	 * structure is:
	 * <pre>["$eisodeId","$episodeId",...]</pre>
	 * @return {@link JSONArray} if success<br>
	 * 			<code>null</code> otherwise
	 */
	public JSONArray getIgnoredEpisodes() {

		if ( !loggedIn ) {
			return null;
		}

		JSONArray ret=null;

		String result=api.getIgnoredEpisodes();

		if ( result!=null ) {
			try {
				ret=new JSONArray(result);
			} catch (Exception e) {
				System.err.println("--- oops: "+e.getMessage());
				e.printStackTrace();
			}
		}

		return ret;
	}

	/**
	 * add/remove episode to/from ignored list
	 * @param _episode $episodeId
	 * @param _add <code>true</code> if add<br>
	 * 				<code>false</code> if remove
	 * @return <code>true</code> if success<br>
	 * 			<code>false</code> otherwise
	 */
	public boolean ignoreEpisode(int _episode,  boolean _add) {
//		System.out.println( (_add ? "add" : "remove") + " ignored episode #"+_episode);

		if ( !loggedIn ) {
			return false;
		}

		return api.ignoreEpisode(_episode, _add);
	}
	
	/**
	 * get seen episodes of user's show (given by <code>_show</code>)<br>
	 * calls <code>MyshowsAPI.getSeenEpisodes()</code><br>
	 * {@link JSONArray} format:
		<pre>[
  {
    "id": $episodeId,
    "watchDate": "$dd.mm.yyyy"
  }
]
		</pre>
	 * @param _show $showId to filter next episodes
	 * @return {@link JSONArray} with episodes if success<br>
	 * 			<code>null</code> otherwise
	 */
	public JSONArray getSeenEpisodes(int _show) {
//		System.out.println("+++ getSeenEpisodes(int "+_show+")");

		if ( !loggedIn ) {
			return null;
		}

		JSONObject seen=null;
		JSONArray ret=null;
		String result=api.getSeenEpisodes(_show);
		
		if ( result!=null ) {
			try {
				// put episodes in jsonobject{ "showid":{"info"} }
				seen=new JSONObject(result);
				
//				System.out.println(seen.toString(2));
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
	
	/**
	 * mark episode as watched<br>
	 * calls <code>MyshowsAPI.checkEpisode(_episode)</code><br>
	 * @param _episode $episodeId
	 * @return <code>true</code> if success<br>
	 * 			<code>false</code> otherwise (likely unauthorized)
	 */
	public boolean checkEpisode(int _episode) {
//		System.out.println("+++ checkEpisode(int "+_episode+")");

		if ( !loggedIn ) {
			return false;
		}

		return api.checkEpisode(_episode);
	}
	
	
	/**
	 * mark episode as watched with ratio<br>
	 * calls <code>MyshowsAPI.checkEpisode(_episode, _ratio)</code>
	 * @param _episode $episodeId
	 * @param _ratio $ratio // <b>currently useless</b>
	 * @return <code>true</code> if success<br>
	 * 			<code>false</code> otherwise (likely unauthorized)
	 */
	public boolean checkEpisode(int _episode, int _ratio) {
//		System.out.println("+++ checkEpisode(int "+_episode+", int "+_ratio+")");

		if ( !loggedIn ) {
			return false;
		}

		return api.checkEpisode(_episode, _ratio);
	}

	/**
	 * sets episode ratio
	 * @param _episode $episodeId
	 * @param _ratio ratio to set (between 0 and 5)
	 * @return <code>true</code> if success<br>
	 * 			<code>false</code> otherwise
	 */
	public boolean setEpisodeRatio(int _episode, int _ratio) {
//		System.out.println("+++ setEpisodeRatio("+_episode+", "+_ratio+")");

		if ( !loggedIn ) {
			return false;
		}

		return api.setEpisodeRatio(_episode, _ratio);
	}
	
	/**
	 * mark episode as unwatched<br>
	 * calls <code>MyshowsAPI.unCheckEpisode(_episode)</code>
	 * @param _episode $episodeId
	 * @return <code>true</code> if success<br>
	 * 			<code>false</code> otherwise (likely unauthorized)
	 */
	public boolean unCheckEpisode(int _episode) {
//		System.out.println("+++ unCheckEpisode(int "+_episode+")");

		if ( !loggedIn ) {
			return false;
		}

		return api.unCheckEpisode(_episode);
	}
	
	/**
	 * sets show status (ie. watching, cancelled, ..)
	 * @param _show $showId
	 * @param _status chararter. one of following:
	 * 			<ul>
	 * 			<li> w -- watching
	 * 			<li> c -- cancelled
	 * 			<li> l -- later
	 * 			<li> r -- remove
	 * 			</ul>
	 * @return <code>true</code> if success<br>
	 * 			<code>false</code> otherwise
	 */
	public boolean setShowStatus(int _show, char _status) {
//		System.out.println("client.setShowStatus: "+_show+":"+_status);

		if ( !loggedIn ) {
			return false;
		}

		MyshowsAPI.SHOW_STATUS st=MyshowsAPI.SHOW_STATUS.watching;
		
		if ( _status=='w') {
			st=MyshowsAPI.SHOW_STATUS.watching;
		} else if ( _status=='c' ) {
			st=MyshowsAPI.SHOW_STATUS.cancelled;
		} else if ( _status=='l' ) {
			st=MyshowsAPI.SHOW_STATUS.later;
		} else if ( _status=='r' ) {
			st=MyshowsAPI.SHOW_STATUS.remove;
		}
		
		return api.setShowStatus(_show, st);
	}

	/**
	 * sets show ratio
	 * @param _show $showId
	 * @param _ratio ratio to set (between 0 and 5)
	 * @return <code>true</code> if success<br>
	 * 			<code>false</code> otherwise
	 */
	public boolean setShowRatio(int _show, int _ratio) {
//		System.out.println("+++ setShowRatio("+_show+", "+_ratio+")");

		if ( !loggedIn ) {
			return false;
		}

		return api.setShowRatio(_show, _ratio);
	}

	/**
	 * add/remove show to/from favorites
	 * @param _show $showId
	 * @param _add <code>true</code> if add<br>
	 * 				<code>false</code> if remove
	 * @return <code>true</code> if success<br>
	 * 			<code>false</code> otherwise
	 */
	public boolean favoriteShow(int _show,  boolean _add) {
//		System.out.println( (_add ? "add" : "remove") + " favorite show #"+_show);

		if ( !loggedIn ) {
			return false;
		}

		return api.favoriteShow(_show, _add);
	}

	/**
	 * get friends updates<br>
	 * structure is:
	 * <pre>
	 * {
  "$date": [{ // dd.MM.yyyy
    "action": "$action", // watch, // TODO: other actions?
    "episode": "s03e14",
    "episodeId": $episodeId,
    "episodes": $number_of_episodes, // if >1 {episodeId=null, title=null, episode=":"}
    "gender": "$user's_gender", // m, f
    "login": "$username",
    "show": "$original_show_title",
    "showId": $showId,
    "title": "$original_episode_title"
  },
  ...],
  ...
  }
	 * </pre>
	 * @return {@link JSONObject} with updates if success<br>
	 * 			<code>null</code> otherwise
	 */
	public JSONObject getFriendsUpdates() {

		if ( !loggedIn ) {
			return null;
		}

		JSONObject ret=null;

		String result=api.getFriendsUpdates();

		if ( result!=null ) {
			try {
				ret=new JSONObject(result);
			} catch (Exception e) {
				System.err.println("--- oops: "+e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.err.println("--- oops: NULL from API call");
		}

		return ret;
	}

	// TODO: docs
	public JSONObject search(String _keyword) {

		if ( !loggedIn || _keyword==null || _keyword.isEmpty() ) {
			return null;
		}

		JSONObject ret=null;

		String result=api.search(_keyword);

		if ( result!=null ) {
			try {
				ret=new JSONObject(result);
			} catch (Exception e) {
				System.err.println("--- oops: "+e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.err.println("--- oops: NULL from API call");
		}

		return ret;
	}

	// TODO: docs
	public JSONObject searchByFile(String _filename) {

		if ( !loggedIn || _filename==null || _filename.isEmpty() ) {
			return null;
		}

		JSONObject ret=null;

		String result=api.searchByFile(_filename);

		if ( result!=null ) {
			try {
				ret=new JSONObject(result);
				ret=ret.getJSONObject("show");	// return show object only
			} catch (Exception e) {
				System.err.println("--- oops: "+e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.err.println("--- oops: NULL from API call");
		}

		return ret;
	}

	// TODO: docs
	public JSONObject getShowInfo(int _show) {

		if ( !loggedIn || _show<0 ) {
			return null;
		}

		JSONObject ret=null;

		String result=api.getShowInfo(_show);

		if ( result!=null ) {
			try {
				ret=new JSONObject(result);
			} catch (Exception e) {
				System.err.println("--- oops: "+e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.err.println("--- oops: NULL from API call");
		}

		return ret;
	}
}
