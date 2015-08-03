package org.herac.tuxguitar.io.midi;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.gui.system.Service;
import org.herac.tuxguitar.gui.system.plugins.TGPlugin;
import org.herac.tuxguitar.gui.system.plugins.base.TGPluginList;

@Service
public class MidiPluginList extends TGPluginList{
	
	protected List<TGPlugin> loadPlugins() {
		List<TGPlugin> plugins = new ArrayList<TGPlugin>();
		plugins.add(new MidiPluginImporter());
		plugins.add(new MidiPluginExporter());
		return plugins;
	}
	
	public String getAuthor() {
		return "Julian Casadesus <julian@casadesus.com.ar>";
	}
	
	public String getDescription() {
		return "Midi file format import-export plugin";
	}
	
	public String getName() {
		return "Midi file format import-export plugin";
	}
	
	public String getVersion() {
		return "1.0";
	}
}
