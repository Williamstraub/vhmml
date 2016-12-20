package org.vhmml.dto.zotero;

import java.util.ArrayList;
import java.util.List;

import org.vhmml.util.ZoteroReferenceGroupDeserializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = ZoteroReferenceGroupDeserializer.class)
public class ZoteroReferenceGroup {

	private Integer id;		
	private Integer version;
	private String name;
	private Integer owner;
	private String type;
	private String description;
	private String url;
	private String libraryEditing;
	private String libraryReading;
	private String fileEditing;
	private List<Integer> members = new ArrayList<Integer>();
	private List<ZoteroReferenceCollection> collections = new ArrayList<ZoteroReferenceCollection>();
	
	public ZoteroReferenceGroup() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOwner() {
		return owner;
	}

	public void setOwner(Integer owner) {
		this.owner = owner;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLibraryEditing() {
		return libraryEditing;
	}

	public void setLibraryEditing(String libraryEditing) {
		this.libraryEditing = libraryEditing;
	}

	public String getLibraryReading() {
		return libraryReading;
	}

	public void setLibraryReading(String libraryReading) {
		this.libraryReading = libraryReading;
	}

	public String getFileEditing() {
		return fileEditing;
	}

	public void setFileEditing(String fileEditing) {
		this.fileEditing = fileEditing;
	}
	
	public List<Integer> getMembers() {
		return members;
	}

	public void setMembers(List<Integer> members) {
		this.members = members;
	}

	public List<ZoteroReferenceCollection> getCollections() {
		return collections;
	}

	public void setCollections(List<ZoteroReferenceCollection> collections) {
		this.collections = collections;
	}
}
