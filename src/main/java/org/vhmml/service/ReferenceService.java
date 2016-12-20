package org.vhmml.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.springframework.data.domain.Pageable;
import org.vhmml.dto.ReferenceSearchResult;
import org.vhmml.dto.zotero.ZoteroCollectionImportResult;
import org.vhmml.dto.zotero.ZoteroItemImportResult;
import org.vhmml.dto.zotero.ZoteroReferenceCollection;
import org.vhmml.dto.zotero.ZoteroReferenceGroup;
import org.vhmml.dto.zotero.ZoteroSearchResult;
import org.vhmml.entity.ReferenceEntry;
import org.vhmml.entity.ReferenceListItem;

import com.sun.syndication.io.FeedException;

public interface ReferenceService {
	
	public enum SearchType {
		AUTHOR,
		TITLE,
		KEYWORD
	}
	
	public ReferenceEntry getEntry(Long id);
	public ReferenceEntry saveEntry(ReferenceEntry referenceEntry);
	public ReferenceSearchResult search(SearchType searchType, String searchString, Pageable pageable);
	public List<ZoteroReferenceGroup> getReferenceGroupsForUser(Integer userId, String authKey) throws IOException;
	public List<ZoteroReferenceCollection> getGroupCollections(Integer groupId, String authKey) throws IOException;
	public ZoteroSearchResult getCollectionItems(Integer groupId, String collectionKey, String authKey, Pageable pageable) throws HttpException, IOException, FeedException;
	public ZoteroCollectionImportResult importZoteroCollectionEntries(Integer groupId, String collectionKey, String authKey) throws HttpException, IOException, FeedException;
	public ZoteroItemImportResult importZoteroItem(Integer groupId, String itemKey, String authKey) throws HttpException, IOException, FeedException;
	public void deleteEntry(Long entryId);
	public void deleteByZoteroCollection(Integer zoteroGroupId, String zoteroCollectionKey);
	public ReferenceListItem findListItemById(Long referenceEntryId);
	public List<ReferenceListItem> findAll();
	public void sendEntryToUser(Long entryId, String emailAddress) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;	
}
