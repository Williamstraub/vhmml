package org.vhmml.entity.readingroom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.vhmml.entity.Identifiable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "reading_room_images")
public class Image implements Identifiable {

	@Id
	@GeneratedValue
	private Long id;
	
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name = "reading_room_content_id")
    private Content parentContent;

	@Column(name = "img_id")
	private Integer imgId;

	@Column(name = "folio_number")
	private String folioNumber;

	private String caption;
	@Column(name = "notes_to_photographer")
	private String notesToPhotographer;

	@Column(name = "icon_class")
	private String iconClass;

	private boolean revisit;
	private Integer seq; 
	private String url1;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Content getParentContent() {
		return parentContent;
	}

	public void setParentContent(Content parentContent) {
		this.parentContent = parentContent;
	}

	public Integer getImgId() {
		return imgId;
	}

	public void setImgId(Integer imgId) {
		this.imgId = imgId;
	}

	public String getFolioNumber() {
		return folioNumber;
	}

	public void setFolioNumber(String folioNumber) {
		this.folioNumber = folioNumber;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getNotesToPhotographer() {
		return notesToPhotographer;
	}

	public void setNotesToPhotographer(String notesToPhotographer) {
		this.notesToPhotographer = notesToPhotographer;
	}

	public String getIconClass() {
		return iconClass;
	}

	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}

	public boolean isRevisit() {
		return revisit;
	}

	public void setRevisit(boolean revisit) {
		this.revisit = revisit;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getUrl1() {
		return url1;
	}

	public void setUrl1(String url1) {
		this.url1 = url1;
	}
}
