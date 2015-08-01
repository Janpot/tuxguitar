package org.herac.tuxguitar.gui.system.plugins.base;

import java.util.List;

import org.herac.tuxguitar.gui.system.plugins.TGPlugin;
import org.herac.tuxguitar.gui.system.plugins.TGPluginException;

public abstract class TGPluginList extends TGPluginAdapter {
    private List<TGPlugin> plugins;

    public TGPluginList() {
        super();
    }

    public void init() throws TGPluginException {
        for (TGPlugin plugin : getPlugins()) {
            plugin.init();
        }
    }

    public void close() throws TGPluginException {
        for (TGPlugin plugin : getPlugins()) {
            plugin.close();
        }
    }

    public void setEnabled(boolean enabled) throws TGPluginException {
        for (TGPlugin plugin : getPlugins()) {
            plugin.setEnabled(enabled);
        }
    }

    private List<TGPlugin> getPlugins() throws TGPluginException {
        if (this.plugins == null) {
            this.plugins = loadPlugins();
        }
        return this.plugins;
    }

    protected abstract List<TGPlugin> loadPlugins() throws TGPluginException;
}
