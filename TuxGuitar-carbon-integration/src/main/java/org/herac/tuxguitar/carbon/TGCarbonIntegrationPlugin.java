package org.herac.tuxguitar.carbon;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.carbon.menu.MacMenuPlugin;
import org.herac.tuxguitar.carbon.opendoc.OpenDocPlugin;
import org.herac.tuxguitar.gui.system.Service;
import org.herac.tuxguitar.gui.system.plugins.TGPlugin;
import org.herac.tuxguitar.gui.system.plugins.base.TGPluginList;

@Service
public class TGCarbonIntegrationPlugin extends TGPluginList {

	private List<TGPlugin> plugins;

    public String getName() {
        return "Carbon Integration Plugin";
    }

    protected List<TGPlugin> loadPlugins() {
		if( this.plugins == null ){
			this.plugins = new ArrayList<TGPlugin>();
			
			this.plugins.add(new OpenDocPlugin());
			this.plugins.add(new MacMenuPlugin());
		}
		return this.plugins;
	}
	
	

}
