package org.herac.tuxguitar.community.browser;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.herac.tuxguitar.gui.tools.browser.TGBrowserException;
import org.herac.tuxguitar.gui.tools.browser.base.TGBrowserElement;

public class TGBrowserElementImpl extends TGBrowserElement {
	
	private TGBrowserElementImpl parent;
	private Map<String, String> properties;
	private String url;
	
	public TGBrowserElementImpl(String name) {
		super(name);
		this.url = null;
		this.properties = new HashMap<String, String>();
	}
	
	public TGBrowserElementImpl getParent() {
		return this.parent;
	}
	
	public void setParent(TGBrowserElementImpl parent) {
		this.parent = parent;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void addProperty( String key, String value ){
		this.properties.put( key, value );
	}
	
	public String getProperty( String key ){
		return this.properties.get( key );
	}

    public Set<Map.Entry<String, String>> getProperties() {
        return this.properties.entrySet();
    }

    public boolean isFolder() {
		return (this.url == null || this.url.length() == 0 );
	}
	
	public InputStream getInputStream() throws TGBrowserException {
		try {
			if( ! this.isFolder() ){
				URL url = new URL( this.url );
				InputStream stream = url.openStream();
				
				return stream;
			}
		} catch ( Throwable throwable ){
			throw new TGBrowserException( throwable );
		}
		return null;
	}
}
