package org.herac.tuxguitar.gui.system.plugins;

import java.util.*;

import com.google.common.base.Throwables;
import org.herac.tuxguitar.gui.util.MessageDialog;
import org.herac.tuxguitar.util.TGServiceReader;

public class TGPluginManager {
    public static final Comparator<TGPlugin> PLUGIN_BY_NAME_COMPARATOR = new Comparator<TGPlugin>() {
        @Override
        public int compare(TGPlugin p1, TGPlugin p2) {
            return getNameSafe(p1).compareTo(getNameSafe(p2));
        }
    };

    private List<TGPlugin> plugins;
    /**
     * This map holds a status of each plugin. If a plugin failed to initialize we will keep track of it.
     * It would be displayed in plugin list, but with a special mark 'MALFUNCTION' and the app will not try
     * to invoke any of its methods.
     * Identity hash map is used to prevent invocation of hashCode and equals of malfunctioning plugins
     */
    private IdentityHashMap<TGPlugin, Status> pluginStatus;
    private IdentityHashMap<TGPlugin, String> pluginErrors;

    public TGPluginManager() {
        this.plugins = new ArrayList<TGPlugin>();
        this.pluginStatus = new IdentityHashMap<TGPlugin, Status>();
        this.pluginErrors = new IdentityHashMap<TGPlugin, String>();
        this.initPlugins();
    }

    public List<TGPlugin> getPlugins() {
        return this.plugins;
    }

    public void initPlugins() {
        try {
            this.plugins.addAll(TGServiceReader.getServices(TGPlugin.class));
            Collections.sort(this.plugins, PLUGIN_BY_NAME_COMPARATOR);
        } catch (Throwable throwable) {
            MessageDialog.errorMessage(new TGPluginException("An error ocurred when trying to init plugin", throwable));
        }
    }

    private void setMalfunctioningStatus(TGPlugin plugin, Throwable exception) {
        pluginErrors.put(plugin, Throwables.getStackTraceAsString(exception));
        pluginStatus.put(plugin, Status.MALFUNCTION);
    }

    private static String getNameSafe(TGPlugin plugin) {
        try {
            String name = plugin.getName();
            return name == null ? plugin.getClass().getSimpleName() : name;
        } catch (Throwable e) {
            return plugin.getClass().getSimpleName();
        }
    }

    private void closeSafe(TGPlugin plugin) {
        try {
            plugin.close();
        } catch (Throwable t) {
            // ignore exception, it was just the last try to free resources
        }
    }

    public void closePlugins() {
        for (TGPlugin plugin : this.plugins) {
            if (getStatus(plugin) == Status.OK) {
                try {
                    plugin.close();
                } catch (TGPluginException exception) {
                    MessageDialog.errorMessage(exception);
                } catch (Throwable throwable) {
                    MessageDialog.errorMessage(new TGPluginException("An error ocurred when trying to close plugin " + getNameSafe(plugin), throwable));
                }
            }
        }
    }

    public void openPlugins() {
        List<String> failedPlugins = new ArrayList<String>();
        try {
            for (TGPlugin plugin : this.plugins) {
                try {
                    plugin.init();
                    pluginStatus.put(plugin, Status.OK);
                    plugin.setEnabled(isEnabled(plugin));
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    setMalfunctioningStatus(plugin, throwable);
                    closeSafe(plugin);
                    failedPlugins.add(getNameSafe(plugin));
                }
            }
        } catch (Throwable throwable) {
            MessageDialog.errorMessage(new TGPluginException("An error ocurred when trying to init plugin", throwable));
        }
        if (!failedPlugins.isEmpty()) {
            MessageDialog.errorMessage(new TGPluginException("Some plugins failed to initialize: " + failedPlugins));
        }
    }

    public Status getStatus(TGPlugin plugin) {
        Status r = pluginStatus.get(plugin);
        return r == null ? Status.UNKNOWN : r;
    }

    public String getError(TGPlugin plugin) {
        return pluginErrors.get(plugin);
    }

    public void setEnabled(TGPlugin plugin, boolean enabled) {
        try {
            TGPluginProperties.instance().setProperty(getEnabledProperty(plugin), enabled);
            TGPluginProperties.instance().save();
            if (getStatus(plugin) == Status.OK) {
                plugin.setEnabled(enabled);
            }
        } catch (TGPluginException exception) {
            MessageDialog.errorMessage(exception);
        } catch (Throwable throwable) {
            MessageDialog.errorMessage(new TGPluginException("An error ocurred when trying to set plugin status", throwable));
        }
    }

    public boolean isEnabled(TGPlugin plugin) {
        try {
            return TGPluginProperties.instance().getBooleanConfigValue(getEnabledProperty(plugin), true);
        } catch (Throwable throwable) {
            MessageDialog.errorMessage(new TGPluginException("An error ocurred when trying to get plugin status", throwable));
        }
        return false;
    }

    public String getEnabledProperty(TGPlugin plugin) {
        return (plugin.getClass().getName() + ".enabled");
    }

    public enum Status {
        OK, MALFUNCTION, INCOMPATIBLE, UNKNOWN
    }
}
