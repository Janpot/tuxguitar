package org.herac.tuxguitar.gui.system.plugins.base;

import org.herac.tuxguitar.gui.system.plugins.TGPlugin;

public abstract class TGPluginAdapter implements TGPlugin{
	
	public String getName() {
		return null;
	}
	
	public String getAuthor() {
		return "Unavailable";
	}
	
	public String getDescription() {
		return "Unavailable";
	}
	
	public String getVersion() {
		return "Unavailable";
	}
}
