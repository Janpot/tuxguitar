package org.herac.tuxguitar.community.browser;

import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

import org.herac.tuxguitar.community.auth.TGCommunityAuth;
import org.herac.tuxguitar.community.utils.TGCommunityWeb;

public class TGBrowserRequest {
	
	private static final String REMOTE_URL = (TGCommunityWeb.HOME_URL + "/rd.php/sharing/tuxguitar/browser.do");
	
	private String request;
	
	public TGBrowserRequest(TGCommunityAuth auth, TGBrowserElementImpl element) throws Throwable {
		this.initialize(auth, element);
	}
	
	public TGBrowserRequest(TGCommunityAuth auth) throws Throwable {
		this(auth , null);
	}
	
	private void initialize( TGCommunityAuth auth, TGBrowserElementImpl element ) throws Throwable {
		this.request = "";
		this.request += URLEncoder.encode( "auth" , "UTF-8" );
		this.request += ("=");
		this.request += URLEncoder.encode( auth.getAuthCode() , "UTF-8" );
		
		if( element != null ){
            for (Map.Entry<String, String> property : element.getProperties()) {
                this.request += "&";
                this.request += URLEncoder.encode(property.getKey(), "UTF-8");
                this.request += "=";
                this.request += URLEncoder.encode(property.getValue(), "UTF-8");
            }
		}
	}
	
	public TGBrowserResponse getResponse() throws Throwable {
		URL url = new URL(REMOTE_URL);
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);
		
		OutputStreamWriter outputStream = new OutputStreamWriter(conn.getOutputStream());
		outputStream.write(this.request);
		outputStream.flush();
		outputStream.close();
		
		return new TGBrowserResponse( conn.getInputStream() ) ;
	}
}
