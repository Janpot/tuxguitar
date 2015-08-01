package org.herac.tuxguitar.io.base;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.io.tg.TGInputStream;
import org.herac.tuxguitar.io.tg.TGOutputStream;
import org.herac.tuxguitar.io.tg.TGStream;

public class TGFileFormatManager {
	
	public static final String DEFAULT_EXTENSION = TGStream.TG_FORMAT_EXTENSION;
	
	private static TGFileFormatManager instance;
	
	private TGSongLoader loader;
	private TGSongWriter writer;
	private List<TGInputStreamBase> inputStreams;
	private List<TGOutputStreamBase> outputStreams;
	private List<TGRawExporter> exporters;
	private List<TGRawImporter> importers;
	
	private TGFileFormatManager(){
		this.loader = new TGSongLoader();
		this.writer = new TGSongWriter();
		this.inputStreams = new ArrayList<TGInputStreamBase>();
		this.outputStreams = new ArrayList<TGOutputStreamBase>();
		this.exporters = new ArrayList<TGRawExporter>();
		this.importers = new ArrayList<TGRawImporter>();
		this.addDefaultStreams();
	}
	
	public static TGFileFormatManager instance(){
		if(instance == null){
			instance = new TGFileFormatManager();
		}
		return instance;
	}
	
	public TGSongLoader getLoader(){
		return this.loader;
	}
	
	public TGSongWriter getWriter(){
		return this.writer;
	}
	
	public void addInputStream(TGInputStreamBase stream){
		this.inputStreams.add(stream);
	}
	
	public void removeInputStream(TGInputStreamBase stream){
		this.inputStreams.remove(stream);
	}
	
	public int countInputStreams(){
		return this.inputStreams.size();
	}
	
	public void addOutputStream(TGOutputStreamBase stream){
		this.outputStreams.add(stream);
	}
	
	public void removeOutputStream(TGOutputStreamBase stream){
		this.outputStreams.remove(stream);
	}
	
	public int countOutputStreams(){
		return this.outputStreams.size();
	}
	
	public void addImporter(TGRawImporter importer){
		this.importers.add(importer);
	}
	
	public void removeImporter(TGRawImporter importer){
		this.importers.remove(importer);
	}
	
	public int countImporters(){
		return this.importers.size();
	}
	
	public void addExporter(TGRawExporter exporter){
		this.exporters.add(exporter);
	}
	
	public void removeExporter(TGRawExporter exporter){
		this.exporters.remove(exporter);
	}
	
	public int countExporters(){
		return this.exporters.size();
	}

    public List<TGInputStreamBase> getInputStreams() {
        return this.inputStreams;
    }

    public List<TGOutputStreamBase> getOutputStreams() {
        return this.outputStreams;
    }

    public List<TGRawImporter> getImporters() {
        return this.importers;
    }

    public List<TGRawExporter> getExporters() {
        return this.exporters;
    }

    public List<TGFileFormat> getInputFormats(){
		List<TGFileFormat> formats = new ArrayList<TGFileFormat>();
        for (TGInputStreamBase stream : getInputStreams()) {
            TGFileFormat format = stream.getFileFormat();
            if (!existsFormat(format, formats)) {
                formats.add(format);
            }
        }
		return formats;
	}
	
	public List<TGFileFormat> getOutputFormats(){
		List<TGFileFormat> formats = new ArrayList<TGFileFormat>();
        for (TGOutputStreamBase stream : getOutputStreams()) {
            TGFileFormat format = stream.getFileFormat();
            if (!existsFormat(format, formats)) {
                formats.add(format);
            }
        }
		return formats;
	}
	
	private boolean existsFormat(TGFileFormat format,List<TGFileFormat> formats){
        for (TGFileFormat comparator : formats) {
            if (comparator.getName().equals(format.getName()) || comparator.getSupportedFormats().equals(format.getSupportedFormats())) {
                return true;
            }
        }
		return false;
	}
	
	private void addDefaultStreams(){
		this.addInputStream(new TGInputStream());
		this.addOutputStream(new TGOutputStream());
	}
}
