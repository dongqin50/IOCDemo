package com.conagra.hardware.model;


/**
 * 2013/5/7 mp3ʵ����
 * 
 * @author wwj
 * 
 */
public class Mp3Info {
	private long id; // ����ID 3
	private String title; // �������� 0
	private String album; // ר�� 7
	private long albumId;//ר��ID 6
	private String displayName; //��ʾ���� 4
	private String artist; // �������� 2
	private long duration; // ����ʱ�� 1
	private long size; // ������С 8
	private String url; // ����·�� 5
	private String lrcTitle; // �������
	private String lrcSize; // ��ʴ�С 

	public Mp3Info() {
		super();
	}

	public Mp3Info(long id, String title, String album, long albumId,
                   String displayName, String artist, long duration, long size,
                   String url, String lrcTitle, String lrcSize) {
		super();
		this.id = id;
		this.title = title;
		this.album = album;
		this.albumId = albumId;
		this.displayName = displayName;
		this.artist = artist;
		this.duration = duration;
		this.size = size;
		this.url = url;
		this.lrcTitle = lrcTitle;
		this.lrcSize = lrcSize;
	}

	@Override
	public String toString() {
		return "Mp3Info [id=" + id + ", title=" + title + ", album=" + album
				+ ", albumId=" + albumId + ", displayName=" + displayName
				+ ", artist=" + artist + ", duration=" + duration + ", size="
				+ size + ", url=" + url + ", lrcTitle=" + lrcTitle
				+ ", lrcSize=" + lrcSize + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	

	public long getAlbumId() {
		return albumId;
	}

	public void setAlbumId(long albumId) {
		this.albumId = albumId;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLrcTitle() {
		return lrcTitle;
	}

	public void setLrcTitle(String lrcTitle) {
		this.lrcTitle = lrcTitle;
	}

	public String getLrcSize() {
		return lrcSize;
	}

	public void setLrcSize(String lrcSize) {
		this.lrcSize = lrcSize;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	

}