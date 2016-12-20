package org.vhmml.dto.iiif;

import java.util.List;

import org.vhmml.dto.iiif.json.deserialization.CanvasDeserializer;
import org.vhmml.dto.iiif.json.serialization.CanvasSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = CanvasSerializer.class)
@JsonDeserialize(using = CanvasDeserializer.class)
public class Canvas extends IIIFTypedMetaData {
	
	private String label;
	private Integer height;
	private Integer width;
	private List<Image> images;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}
}
