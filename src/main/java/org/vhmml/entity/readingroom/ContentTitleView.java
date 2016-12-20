package org.vhmml.entity.readingroom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reading_room_content_titles_view")
public class ContentTitleView {

	@Id
	@Column(name = "content_id")
	private Long contentId;

	@Column(name = "hmml_project_number")
	private String hmmlProjectNumber;

	private String title;

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public String getHmmlProjectNumber() {
		return hmmlProjectNumber;
	}

	public void setHmmlProjectNumber(String hmmlProjectNumber) {
		this.hmmlProjectNumber = hmmlProjectNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
