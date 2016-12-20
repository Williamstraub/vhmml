package org.vhmml.dto.iiif;

import java.util.List;

import org.vhmml.dto.iiif.json.deserialization.ManifestDeserializer;
import org.vhmml.dto.iiif.json.serialization.ManifestSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = ManifestSerializer.class)
@JsonDeserialize(using = ManifestDeserializer.class)
public class Manifest extends IIIFTypedMetaData {

	private String context;
	private String label;
	private String logo;
	private String description;
	private String attribution;
	private SeeAlso seeAlso;
	private List<Sequence> sequences;
	private String dcTermsWithin;
	private IIIFTypedMetaData scMetaData;	

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAttribution() {
		return attribution;
	}

	public void setAttribution(String attribution) {
		this.attribution = attribution;
	}

	public SeeAlso getSeeAlso() {
		return seeAlso;
	}

	public void setSeeAlso(SeeAlso seeAlso) {
		this.seeAlso = seeAlso;
	}

	public List<Sequence> getSequences() {
		return sequences;
	}

	public void setSequences(List<Sequence> sequences) {
		this.sequences = sequences;
	}

	public String getDcTermsWithin() {
		return dcTermsWithin;
	}

	public void setDcTermsWithin(String dcTermsWithin) {
		this.dcTermsWithin = dcTermsWithin;
	}

	public IIIFTypedMetaData getScMetaData() {
		return scMetaData;
	}

	public void setScMetaData(IIIFTypedMetaData scMetaData) {
		this.scMetaData = scMetaData;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
}
