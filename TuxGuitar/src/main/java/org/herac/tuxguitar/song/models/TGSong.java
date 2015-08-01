/*
 * Created on 23-nov-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.song.models;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.song.factory.TGFactory;

/**
 * @author julian
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class TGSong {
	
	private String name;
	private String artist;
	private String album;
	private String author;
	private String date;
	private String copyright;
	private String writer;
	private String transcriber;
	private String comments;
	private List<TGTrack> tracks;
	private List<TGMeasureHeader> measureHeaders;
	
	public TGSong() {
		this.name = "";
		this.artist = "";
		this.album = "";
		this.author = "";
		this.date = "";
		this.copyright = "";
		this.writer = "";
		this.transcriber = "";
		this.comments = "";
		this.tracks = new ArrayList<TGTrack>();
		this.measureHeaders = new ArrayList<TGMeasureHeader>();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAlbum() {
		return this.album;
	}
	
	public void setAlbum(String album) {
		this.album = album;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getArtist() {
		return this.artist;
	}
	
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public String getDate() {
		return this.date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getCopyright() {
		return this.copyright;
	}
	
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
	
	public String getWriter() {
		return this.writer;
	}
	
	public void setWriter(String writer) {
		this.writer = writer;
	}
	
	public String getTranscriber() {
		return this.transcriber;
	}
	
	public void setTranscriber(String transcriber) {
		this.transcriber = transcriber;
	}
	
	public String getComments() {
		return this.comments;
	}
	
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public int countMeasureHeaders(){
		return this.measureHeaders.size();
	}
	
	public void addMeasureHeader(TGMeasureHeader measureHeader){
		this.addMeasureHeader(countMeasureHeaders(),measureHeader);
	}
	
	public void addMeasureHeader(int index,TGMeasureHeader measureHeader){
		measureHeader.setSong(this);
		this.measureHeaders.add(index,measureHeader);
	}
	
	public void removeMeasureHeader(int index){
		this.measureHeaders.remove(index);
	}
	
	public void removeMeasureHeader(TGMeasureHeader measureHeader){
		this.measureHeaders.remove(measureHeader);
	}
	
	public TGMeasureHeader getMeasureHeader(int index){
		return this.measureHeaders.get(index);
	}
	
	public Iterable<TGMeasureHeader> getMeasureHeaders() {
		return this.measureHeaders;
	}
	
	public int countTracks(){
		return this.tracks.size();
	}
	
	public void addTrack(TGTrack track){
		this.addTrack(countTracks(),track);
	}
	
	public void addTrack(int index,TGTrack track){
		track.setSong(this);
		this.tracks.add(index,track);
	}
	
	public void moveTrack(int index,TGTrack track){
		this.tracks.remove(track);
		this.tracks.add(index,track);
	}
	
	public void removeTrack(TGTrack track){
		this.tracks.remove(track);
		track.clear();
	}
	
	public TGTrack getTrack(int index){
		return this.tracks.get(index);
	}
	
	public List<TGTrack> getTracks() {
		return this.tracks;
	}

	public boolean isEmpty(){
		return (countMeasureHeaders() == 0 || countTracks() == 0);
	}
	
	public void clear(){
        for (TGTrack track : getTracks()) {
            track.clear();
        }
		this.tracks.clear();
		this.measureHeaders.clear();
	}
	
	public TGSong clone(TGFactory factory){
		TGSong song = factory.newSong();
		copy(factory,song);
		return song;
	}
	
	public void copy(TGFactory factory,TGSong song){
		song.clear();
		song.setName(getName());
		song.setArtist(getArtist());
		song.setAlbum(getAlbum());
		song.setAuthor(getAuthor());
		song.setDate(getDate());
		song.setCopyright(getCopyright());
		song.setWriter(getWriter());
		song.setTranscriber(getTranscriber());
		song.setComments(getComments());
        for (TGMeasureHeader header : getMeasureHeaders()) {
            song.addMeasureHeader(header.clone(factory));
        }
        for (TGTrack track : getTracks()) {
            song.addTrack(track.clone(factory, song));
        }
	}
}
