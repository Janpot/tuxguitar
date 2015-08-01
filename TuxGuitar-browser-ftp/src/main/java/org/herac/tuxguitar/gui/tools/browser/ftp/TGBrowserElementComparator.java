package org.herac.tuxguitar.gui.tools.browser.ftp;

import java.util.Comparator;

import org.herac.tuxguitar.gui.tools.browser.base.TGBrowserElement;

public class TGBrowserElementComparator implements Comparator<TGBrowserElement> {
	
	private static final int RESULT_LESS = -1;
	
	private static final int RESULT_EQUAL = 0;
	
	private static final int RESULT_GREATER = 1;
	
	private static final int DIRECTION = 1;
	
	private static final int DIRECTION_FOLDER = 1;
	
	public int compare(TGBrowserElement o1, TGBrowserElement o2) {
		if(o1 != null && o2 != null){
            if(o1.isFolder() && !o2.isFolder()){
				return (DIRECTION_FOLDER * RESULT_LESS);
			}
			if(o2.isFolder() && !o1.isFolder()){
				return (DIRECTION_FOLDER * RESULT_GREATER);
			}
			
			return (DIRECTION * (o1.getName().compareTo(o2.getName())));
		}
		return RESULT_EQUAL;
	}
	
}
