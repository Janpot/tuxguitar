/*
 * Created on 19-dic-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.io.base;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.TGSong;

/**
 * @author julian
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TGSongWriter {
	
	public TGSongWriter(){
		super();
	}
	
	public void write(TGFactory factory,TGSong song,String path) throws TGFileFormatException{
		try {
            for (TGOutputStreamBase writer : TGFileFormatManager.instance().getOutputStreams()) {
                if (isSupportedExtension(writer, path)) {
                    writer.init(factory, new BufferedOutputStream(new FileOutputStream(new File(path))));
                    writer.writeSong(song);
                    return;
                }
            }
		} catch (Throwable t) {
			throw new TGFileFormatException(t);
		}
		throw new TGFileFormatException("Unsupported file format");
	}
	
	private boolean isSupportedExtension(TGOutputStreamBase writer,String path){
		int index = path.lastIndexOf(".");
        return index > 0 && writer.isSupportedExtension(path.substring(index));
    }
	
}
