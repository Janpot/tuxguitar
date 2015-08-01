package org.herac.tuxguitar.gui.editors;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.song.models.TGBeat;

public class TGEditorManager {
	
	private List<TGRedrawListener> redrawListeners;
	private List<TGUpdateListener> updateListeners;
	private List<TGExternalBeatViewerListener> beatViewerListeners;
	
	public TGEditorManager(){
		this.redrawListeners = new ArrayList<TGRedrawListener>();
		this.updateListeners = new ArrayList<TGUpdateListener>();
		this.beatViewerListeners = new ArrayList<TGExternalBeatViewerListener>();
	}
	
	public void doRedraw( int type ){
        for (TGRedrawListener listener : this.redrawListeners) {
            listener.doRedraw(type);
        }
	}
	
	public void doUpdate( int type ){
        for (TGUpdateListener listener : this.updateListeners) {
            listener.doUpdate(type);
        }
	}
	
	public void showExternalBeat( TGBeat beat ){
        for (TGExternalBeatViewerListener listener : this.beatViewerListeners) {
            listener.showExternalBeat(beat);
        }
	}
	
	public void hideExternalBeat(){
        for (TGExternalBeatViewerListener listener : this.beatViewerListeners) {
            listener.hideExternalBeat();
        }
	}
	
	public void addRedrawListener( TGRedrawListener listener){
		if(!this.redrawListeners.contains( listener )){
			this.redrawListeners.add( listener );
		}
	}
	
	public void removeRedrawListener( TGRedrawListener listener){
		if(this.redrawListeners.contains( listener )){
			this.redrawListeners.remove( listener );
		}
	}
	
	public void addUpdateListener( TGUpdateListener listener){
		if(!this.updateListeners.contains( listener )){
			this.updateListeners.add( listener );
		}
	}
	
	public void removeUpdateListener( TGUpdateListener listener){
		if(this.updateListeners.contains( listener )){
			this.updateListeners.remove( listener );
		}
	}
	
	public void addBeatViewerListener( TGExternalBeatViewerListener listener){
		if(!this.beatViewerListeners.contains( listener )){
			this.beatViewerListeners.add( listener );
		}
	}
	
	public void removeBeatViewerListener( TGExternalBeatViewerListener listener){
		if(this.beatViewerListeners.contains( listener )){
			this.beatViewerListeners.remove( listener );
		}
	}
}