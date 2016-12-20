package org.vhmml.dto.iiif;

import java.util.List;

import org.vhmml.dto.iiif.json.deserialization.SequenceDeserializer;
import org.vhmml.dto.iiif.json.serialization.SequenceSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = SequenceSerializer.class)
@JsonDeserialize(using = SequenceDeserializer.class)
public class Sequence extends IIIFTypedMetaData {

	private String label;
	private List<Canvas> canvases;
	
	public Sequence() {
		super();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<Canvas> getCanvases() {
		return canvases;
	}

	public void setCanvases(List<Canvas> canvases) {
		this.canvases = canvases;
	}
}
