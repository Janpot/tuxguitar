package org.herac.tuxguitar.gui.undo.undoables.custom;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.editors.tab.Caret;
import org.herac.tuxguitar.gui.editors.tab.TGMeasureImpl;
import org.herac.tuxguitar.gui.undo.CannotRedoException;
import org.herac.tuxguitar.gui.undo.CannotUndoException;
import org.herac.tuxguitar.gui.undo.UndoableEdit;
import org.herac.tuxguitar.gui.undo.undoables.UndoableCaretHelper;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGTrack;

public class UndoableChangeKeySignature implements UndoableEdit{
	private int doAction;
	private UndoableCaretHelper undoCaret;
	private UndoableCaretHelper redoCaret;
	private long position;
	private int redoableKeySignature;
	private int undoableKeySignature;
	private List<KeySignaturePosition> nextKeySignaturePositions;
	private boolean toEnd;
	private TGTrack track;
	
	private UndoableChangeKeySignature(){
		super();
	}
	
	public void redo() throws CannotRedoException {
		if(!canRedo()){
			throw new CannotRedoException();
		}
		TuxGuitar.instance().getSongManager().getTrackManager().changeKeySignature(this.track,this.position,this.redoableKeySignature,this.toEnd);
		TuxGuitar.instance().fireUpdate();
		this.redoCaret.update();
		
		this.doAction = UNDO_ACTION;
	}
	
	public void undo() throws CannotUndoException {
		if(!canUndo()){
			throw new CannotUndoException();
		}
		TuxGuitar.instance().getSongManager().getTrackManager().changeKeySignature(this.track,this.position,this.undoableKeySignature,this.toEnd);
		if(this.toEnd){
            for (KeySignaturePosition ksp : this.nextKeySignaturePositions) {
                TuxGuitar.instance().getSongManager().getTrackManager().changeKeySignature(this.track, ksp.getPosition(), ksp.getKeySignature(), true);
            }
		}
		TuxGuitar.instance().fireUpdate();
		this.undoCaret.update();
		
		this.doAction = REDO_ACTION;
	}
	
	public boolean canRedo() {
		return (this.doAction == REDO_ACTION);
	}
	
	public boolean canUndo() {
		return (this.doAction == UNDO_ACTION);
	}
	
	public static UndoableChangeKeySignature startUndo(){
		UndoableChangeKeySignature undoable = new UndoableChangeKeySignature();
		Caret caret = getCaret();
		undoable.doAction = UNDO_ACTION;
		undoable.undoCaret = new UndoableCaretHelper();
		undoable.position = caret.getPosition();
		undoable.undoableKeySignature = caret.getMeasure().getKeySignature();
		undoable.track = caret.getTrack();
		undoable.nextKeySignaturePositions = new ArrayList<KeySignaturePosition>();
		
		int prevKeySignature = undoable.undoableKeySignature;
        for (TGMeasure tgMeasure : caret.getTrack().getMeasures()) {
            TGMeasureImpl measure = (TGMeasureImpl) tgMeasure;
            if (measure.getStart() > undoable.position) {
                int currKeySignature = measure.getKeySignature();
                if (prevKeySignature != currKeySignature) {
                    KeySignaturePosition tsp = undoable.new KeySignaturePosition(measure.getStart(), currKeySignature);
                    undoable.nextKeySignaturePositions.add(tsp);
                }
                prevKeySignature = currKeySignature;
            }
        }
		
		return undoable;
	}
	
	public UndoableChangeKeySignature endUndo(int keySignature,boolean toEnd){
		this.redoCaret = new UndoableCaretHelper();
		this.redoableKeySignature = keySignature;
		this.toEnd = toEnd;
		return this;
	}
	
	private static Caret getCaret(){
		return TuxGuitar.instance().getTablatureEditor().getTablature().getCaret();
	}
	
	private class KeySignaturePosition{
		private long position;
		private int keySignature;
		
		public KeySignaturePosition(long position,int keySignature) {
			this.position = position;
			this.keySignature = keySignature;
		}
		
		public long getPosition() {
			return this.position;
		}
		
		public int getKeySignature() {
			return this.keySignature;
		}
	}
}
