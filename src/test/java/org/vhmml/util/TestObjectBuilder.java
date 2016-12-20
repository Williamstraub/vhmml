package org.vhmml.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.elasticsearch.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.vhmml.entity.Creator;
import org.vhmml.entity.Creator.Type;
import org.vhmml.entity.LexiconTerm;
import org.vhmml.entity.ReferenceCreator;
import org.vhmml.entity.ReferenceEntry;
import org.vhmml.entity.readingroom.AlternateSurrogate;
import org.vhmml.entity.readingroom.ArchivalContent;
import org.vhmml.entity.readingroom.ArchivalData;
import org.vhmml.entity.readingroom.ArchivalObject;
import org.vhmml.entity.readingroom.Content;
import org.vhmml.entity.readingroom.ContentContributor;
import org.vhmml.entity.readingroom.Contributor;
import org.vhmml.entity.readingroom.ReadingRoomObject;
import org.vhmml.entity.readingroom.ReadingRoomObject.AccessRestriction;
import org.vhmml.entity.readingroom.ReadingRoomObject.Status;

public class TestObjectBuilder {

	public static LexiconTerm getLexiconTermWithCorruptRelatedTerms() {
		
		LexiconTerm term = new LexiconTerm("Term with 2 corrupt relations");
		
		List<LexiconTerm> relatedTerms = new ArrayList<LexiconTerm>();
		List<LexiconTerm> reverseRelations = new ArrayList<LexiconTerm>();
		reverseRelations.add(term);

		// create 6 relations, 4 valid ones that are mutually related to the term and 2 that aren't		
		relatedTerms.add(new LexiconTerm("Valid Related Term 1"));
		relatedTerms.add(new LexiconTerm("Valid Related Term 2"));		
		relatedTerms.add(new LexiconTerm("Valid Related Term 3"));
		relatedTerms.add(new LexiconTerm("Valid Related Term 4"));
		
		for(int i = 0; i < 4; i++) {
			relatedTerms.get(i).setRelatedTerms(reverseRelations);
		}
		
		relatedTerms.add(new LexiconTerm("Invalid Related Term 1"));
		relatedTerms.add(new LexiconTerm("Invalid Related Term 2"));
		
		term.setRelatedTerms(relatedTerms);
		
		return term;
	}
	
	public static ReferenceEntry getReferenceEntryWithMultipleCreators() {
		ReferenceEntry entry = new ReferenceEntry();
		entry.setTitle("Test reference entry");
		entry.setItemType(ReferenceEntry.Type.BOOK);
		
		List<ReferenceCreator> libCreators = new ArrayList<ReferenceCreator>();
		libCreators.add(getReferenceCreator(entry, Type.AUTHOR, "Columba", "Stewart"));
		libCreators.add(getReferenceCreator(entry, Type.EDITOR, "William", "Straub"));		
		entry.setReferenceCreators(libCreators);
		
		return entry;
	}
	
	public static ReferenceCreator getReferenceCreator(ReferenceEntry entry, Creator.Type type, String firstName, String lastName) {
		ReferenceCreator libCreator = new ReferenceCreator();
		libCreator.setCreatorType(type.getName());
		Creator creator = new Creator();
		creator.setFirstName(firstName);
		creator.setLastName(lastName);
		libCreator.setCreator(creator);
		libCreator.setEntry(entry);
		
		return libCreator;
	}
	
	public static ReadingRoomObject getReadingRoomObject() {
		ReadingRoomObject object = new ReadingRoomObject();
		
		object.setAccessRestriction(AccessRestriction.REGISTERED);
		object.setAcknowledgments("acknowledgements");
		object.setActive(true);
		List<AlternateSurrogate> altSurrogates = new ArrayList<>();
		AlternateSurrogate altSurrogate1 = new AlternateSurrogate();
		altSurrogate1.setName("test alternate surrogate");
		altSurrogates.add(altSurrogate1);
		object.setAlternateSurrogates(altSurrogates);
		object.setBibliography("bibliography");
		object.setBinding("binding");
		object.setBindingDepth(12.2f);
		object.setBindingHeight(22.5f);
		object.setBindingWidth(14.2f);
		object.setCaptureDate(new Date());
		object.setCiteAs("cite as");
		object.setCollation("collation");
		object.setColophon("colophon");
		object.setCommonName("common name");
		object.setConditionNotes("condition notes");
		object.setCurrentStatus(Status.IN_SITU);
		object.setDataSource("data source");
		object.setDownloadOption("download option");
		object.setEditable(true);
		object.setFeaturesImported("features imported");
		
		return  object;
	}
	
	public static Contributor getContributor(String name) {
		Contributor contrib = new Contributor();
		contrib.setName(name);
		contrib.setDisplayName(name + " display");
		contrib.setAuthorityUriLC(name + " LC URI");
		contrib.setAuthorityUriVIAF(name + " VIAF URI");
		
		return contrib;
	}
	
	public static ContentContributor getContentContributor(String name, Content content) {
		ContentContributor contentContrib = new ContentContributor();		
		contentContrib.setContent(content);
		contentContrib.setContributor(getContributor(name));
		
		return contentContrib;
	}
	
	public static Content getContent(String title) {
		Content content = new Content();
		
		content.setAcknowledgments("acknowledgements");
		List<String> altTitles = new ArrayList<>();
		altTitles.add("alt title #1");
		altTitles.add("alt title #2");
		content.setAlternateTitles(altTitles);
		content.setCatalogerTags("cataloger tags");
		content.setColophonText("colophon text");
		List<ContentContributor> contentContributors = new ArrayList<>();		
		contentContributors.add(getContentContributor("Contributor #1", content));
		contentContributors.add(getContentContributor("Contributor #2", content));		
		content.setContentContributors(contentContributors);
		content.setContentsDetail("contents detail");
		content.setDecoration("decoration");
		content.setExplicit("explicit");
		content.setIncipit("incipit");
		content.setItemCondition("item condition");
		content.setItemLocation("item location");
		content.setItemNotes("item notes");
		content.setItemNumber(1);		
		content.setProvisionalTitle(title);
		
		return content;
	}
	
	public static ArchivalObject getArchivalMaterial() {
		ReadingRoomObject object = getReadingRoomObject();
		ArchivalObject archivalObject = new ArchivalObject();
		BeanUtils.copyProperties(object, archivalObject);
		
		ArchivalData archivalData = new ArchivalData();
		archivalData.setCollectionFond("collection fond");
		archivalData.setCustodialHistory("custodial history");
		archivalData.setBeginDate(500);
		archivalData.setEndDate(600);
		archivalData.setDimensions("dimensions");
		archivalData.setLanguageIds(Lists.newArrayList(new Long(1)));
		archivalData.setRecType("record type");
		archivalData.setScopeContent("scope content");
		archivalData.setSeries("series");
		archivalData.setSubSeries("sub series");
		archivalData.setSupport("support");
		
		List<ArchivalContent> contents = new ArrayList<>();
		ArchivalContent archivalContent1 = new ArchivalContent();
		ArchivalContent archivalContent2 = new ArchivalContent();
		Content content1 = getContent("Content #1");
		Content content2 = getContent("Content #2");
		BeanUtils.copyProperties(content1, archivalContent1);
		BeanUtils.copyProperties(content2, archivalContent2);
		archivalContent1.setParentArchivalData(archivalData);
		archivalContent2.setParentArchivalData(archivalData);
		archivalContent1.setContentContributors(null);
		archivalContent2.setContentContributors(null);
		contents.add(archivalContent1);
		contents.add(archivalContent2);
		
		archivalData.setContent(contents);
		
		archivalObject.setArchivalData(archivalData);
				
		return archivalObject;
	}
}
