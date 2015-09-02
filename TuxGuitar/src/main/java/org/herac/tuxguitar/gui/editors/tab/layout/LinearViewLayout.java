/*
 * Created on 04-ene-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.gui.editors.tab.layout;

import org.eclipse.swt.graphics.Rectangle;
import org.herac.tuxguitar.gui.editors.TGPainter;
import org.herac.tuxguitar.gui.editors.tab.TGLyricImpl;
import org.herac.tuxguitar.gui.editors.tab.TGMeasureImpl;
import org.herac.tuxguitar.gui.editors.tab.TGTrackImpl;
import org.herac.tuxguitar.gui.editors.tab.TGTrackSpacing;
import org.herac.tuxguitar.gui.editors.tab.Tablature;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGTrack;

/**
 * @author julian
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LinearViewLayout extends ViewLayout{
	
	public LinearViewLayout(Tablature tablature,int style){
		super(tablature,style);
	}
	
	public int getMode(){
		return MODE_LINEAR;
	}
	
	public void paintSong(TGPainter painter,Rectangle clientArea,int fromX,int fromY) {
		this.setWidth(0);
		this.setHeight(0);
		this.clearTrackPositions();
		
		int style = getStyle();
		int number = ((style & ViewLayout.DISPLAY_MULTITRACK) == 0?getTablature().getCaret().getTrack().getNumber():-1);
		int posY = fromY + getFirstTrackSpacing();
		int height = getFirstTrackSpacing();
		int trackHeight;
        for (TGTrack tgTrack : getSongManager().getSong().getTracks()) {
            TGTrackImpl track = (TGTrackImpl) tgTrack;
            if (number < 0 || track.getNumber() == number) {

                TGTrackSpacing ts = new TGTrackSpacing(this);
                ts.setSize(TGTrackSpacing.POSITION_SCORE_MIDDLE_LINES, ((style & DISPLAY_SCORE) != 0 ? ((getScoreLineSpacing() * 5)) : 0));
                ((TGLyricImpl) track.getLyrics()).start();

                //------AUTO_SPACING---------------------------------------
                int maxY = 0;
                int minY = 0;
                // Need to score extra-lines in edition mode
                if ((style & DISPLAY_TABLATURE) == 0 && (style & DISPLAY_SCORE) != 0) {
                    maxY = ((getScoreLineSpacing() * 4) + (getScoreLineSpacing() * 4));
                    minY = -(getScoreLineSpacing() * 3);
                }


                for (TGMeasure tgMeasure : track.getMeasures()) {
                    TGMeasureImpl measure = (TGMeasureImpl) tgMeasure;
                    maxY = (measure.getMaxY() > maxY) ? measure.getMaxY() : maxY;
                    minY = (measure.getMinY() < minY) ? measure.getMinY() : minY;
                    measure.registerSpacing(this, ts);
                }
                ts.setSize(TGTrackSpacing.POSITION_SCORE_UP_LINES, ((style & DISPLAY_SCORE) != 0 ? Math.abs(minY) : 0));
                if ((style & DISPLAY_SCORE) != 0 && maxY + getMinScoreTabSpacing() > getScoreSpacing()) {
                    ts.setSize(TGTrackSpacing.POSITION_SCORE_DOWN_LINES, (maxY - (getScoreLineSpacing() * 4)));
                }

                if ((style & DISPLAY_TABLATURE) != 0) {
                    ts.setSize(TGTrackSpacing.POSITION_TABLATURE_TOP_SEPARATOR, ((style & DISPLAY_SCORE) != 0 ? getMinScoreTabSpacing() : Math.max(Math.abs(minY), getStringSpacing())));
                    ts.setSize(TGTrackSpacing.POSITION_TABLATURE, ((style & DISPLAY_SCORE) != 0 ? track.getTabHeight() + getStringSpacing() + 1 : Math.max(maxY, track.getTabHeight() + getStringSpacing() + 1)));
                }
                ts.setSize(TGTrackSpacing.POSITION_LYRIC, 10);
                checkDefaultSpacing(ts);

                //----------------------------------------------------
                paintMeasures(track, painter, fromX, posY, ts, clientArea);
                paintLines(track, ts, painter, fromX + (getWidth() + 2), posY, (clientArea.width - (fromX + getWidth())));

                trackHeight = ts.getSize();
                addTrackPosition(track.getNumber(), posY, trackHeight);

                posY += trackHeight + getTrackSpacing();
                height += trackHeight + getTrackSpacing();
            }
        }
		if(getWidth() > clientArea.width){
			// solo para dar un espacio.
			this.setWidth( getWidth() + getFirstMeasureSpacing());
		}
		this.setHeight(height);
		this.paintCaret(painter);
	}
	
	public void paintMeasures(TGTrackImpl track,TGPainter painter,int fromX, int fromY,TGTrackSpacing ts,Rectangle clientArea) {
		int posX = (fromX + getFirstMeasureSpacing());
		int posY = fromY;
		int width = getFirstMeasureSpacing();

        for (TGMeasure tgMeasure : track.getMeasures()) {
            TGMeasureImpl measure = (TGMeasureImpl) tgMeasure;

            //asigno la posicion dentro del compas
            measure.setPosX(posX);
            measure.setPosY(posY);
            measure.setTs(ts);

            ((TGLyricImpl) track.getLyrics()).setCurrentMeasure(measure);

            //Solo pinto lo que entre en pantalla
            boolean isAtX = ((posX + measure.getWidth(this)) > clientArea.x - 100 && posX < clientArea.x + clientArea.width + measure.getWidth(this) + 100);
            boolean isAtY = (posY + ts.getSize() > clientArea.y && posY < clientArea.y + clientArea.height + 80);
            if (isAtX && isAtY) {
                paintMeasure(measure, painter, 0);
                ((TGLyricImpl) track.getLyrics()).paintCurrentNoteBeats(painter, this, measure, posX, posY);
            } else {
                measure.setOutOfBounds(true);
            }

            int measureWidth = measure.getWidth(this);
            posX += measureWidth;
            width += measureWidth;
        }
		this.setWidth(Math.max(getWidth(),width));
	}
}
