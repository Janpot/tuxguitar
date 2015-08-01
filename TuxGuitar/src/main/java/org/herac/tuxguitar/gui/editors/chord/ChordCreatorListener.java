package org.herac.tuxguitar.gui.editors.chord;

import org.herac.tuxguitar.song.models.TGChord;

import java.util.List;

public interface ChordCreatorListener {
	
	public void notifyChords(ChordCreatorUtil process, List<TGChord> chords);
	
}
