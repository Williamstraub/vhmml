package org.vhmml.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.vhmml.dto.ReadingRoomSearchResult;
import org.vhmml.entity.readingroom.AlternateSurrogate;
import org.vhmml.entity.readingroom.ArchivalContributor;
import org.vhmml.entity.readingroom.ArchivalData;
import org.vhmml.entity.readingroom.ArchivalDataLanguage;
import org.vhmml.entity.readingroom.BibliographyUrl;
import org.vhmml.entity.readingroom.Content;
import org.vhmml.entity.readingroom.ContentContributor;
import org.vhmml.entity.readingroom.ContentLanguage;
import org.vhmml.entity.readingroom.Extent;
import org.vhmml.entity.readingroom.FacsimileUrl;
import org.vhmml.entity.readingroom.Image;
import org.vhmml.entity.readingroom.ReadingRoomObject;
import org.vhmml.entity.readingroom.ReadingRoomObjectContributor;
import org.vhmml.entity.readingroom.ReadingRoomObjectFeature;
import org.vhmml.entity.readingroom.ReadingRoomObjectGenre;
import org.vhmml.entity.readingroom.ReadingRoomObjectOverview;
import org.vhmml.entity.readingroom.ReadingRoomObjectPart;
import org.vhmml.entity.readingroom.ReadingRoomObjectSubject;
import org.vhmml.entity.readingroom.ReadingRoomPartContributor;
import org.vhmml.exception.ObjectLockedException;

public interface ReadingRoomService {
	
	public List<ReadingRoomObjectOverview> getAllOverviews();
	public List<ReadingRoomObject> getAllObjects();
	public ReadingRoomObject findById(Long id);
	public ReadingRoomObject findByHmmlProject(String hmmlProjectNumber);
	public List<ReadingRoomObject> findByCollection(String collectionName);
	public boolean currentUserCanEdit(ReadingRoomObject object);
	public ReadingRoomSearchResult search(Map<String, String> searchTerms, Pageable pageable);	
	public void bulkInsertReadingRoomObjects(List<ReadingRoomObject> readingRoomObjects);	
	public void bulkInsertReadingRoomObjectExtents(List<Extent> extents);	
	public void bulkInsertReadingRoomObjectContributors(List<ReadingRoomObjectContributor> objectContributors);
	public void bulkInsertReadingRoomObjectParts(List<ReadingRoomObjectPart> readingRoomObjectParts);
	public void bulkInsertReadingRoomPartContributors(List<ReadingRoomPartContributor> readingRoomPartContributors);
	public void bulkInsertReadingRoomContent(List<Content> contents);
	public void bulkInsertReadingRoomImage(List<Image> images);
	public void bulkInsertReadingRoomContentLanguages(List<ContentLanguage> readingRoomContentLangs);	
	public void bulkInsertReadingRoomContentSubjects(List<ReadingRoomObjectSubject> readingRoomObjectSubjects);
	public void bulkInsertReadingRoomContentContributors(List<ContentContributor> contentContributors);
	public void bulkInsertReadingRoomObjectAltSurrogates(List<AlternateSurrogate> altSurrogates);
	public void bulkInsertReadingRoomObjectSubjects(List<ReadingRoomObjectSubject> objectSubjects);
	public void bulkInsertReadingRoomObjectGenres(List<ReadingRoomObjectGenre> objectGenres);
	public void bulkInsertReadingRoomObjectFeatures(List<ReadingRoomObjectFeature> objectFeatures);
	public void bulkInsertReadingRoomObjectBibUrls(List<BibliographyUrl> bibUrls);	
	public void bulkInsertReadingRoomObjectFacsimileUrl(List<FacsimileUrl> facsimileUrl);
	public void bulkInsertArchivalData(List<ArchivalData> archivalData);
	public void bulkInsertArchivalContributors(List<ArchivalContributor> archivalContributors);
	public void bulkInsertArchivalLanguages(List<ArchivalDataLanguage> archivalLanguages);
	public ReadingRoomObject saveReadingRoomObject(ReadingRoomObject object) throws IOException;
	public void setLocked(Long readingRoomObjectId, boolean locked) throws IllegalAccessException, IOException, ObjectLockedException;
	public void clearLockedRecords() throws IOException;
	public void clearLockedRecordsForUser(String username) throws IOException;
	public void deleteReadingRoomObject(Long readingRoomObjectId) throws IllegalAccessException;
	public void deleteReadingRoomObjectPart(Long partId) throws IllegalAccessException, IOException;
	public void deleteReadingRoomContent(Long contentItemId) throws IllegalAccessException;
	public boolean checkImageAccess(Long projectId);
	public boolean checkImageAccess(String hmmlProjectNumber);
	public boolean mustAcceptUsageAgreement(Long objectId);
	public void updateObjectType(Long objectId, ReadingRoomObject.Type type);
}
