package org.vhmml.repository.readingroom;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.vhmml.entity.readingroom.BibliographyUrl;
import org.vhmml.repository.AbstractBatchUpdateRepository;

public class ReadingRoomObjectBibUrlRepositoryImpl extends AbstractBatchUpdateRepository {

	private static final String BATCH_INSERT_SQL = "INSERT INTO vhmml.reading_room_object_bib_urls(reading_room_object_id, url, link_text)VALUES(?, ?, ?)";
	
	public int batchSave(List<BibliographyUrl> bibUrls) {
		int count = 0;
		List<List<BibliographyUrl>> recordBatches = ListUtils.partition(bibUrls, BATCH_SIZE);
		
		for(final List<BibliographyUrl> batch : recordBatches) {
			// executeBatch resides on AbstractBatchUpdateRepository
			count += executeBatch(BATCH_INSERT_SQL, new BatchPreparedStatementSetter() {
				
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					BibliographyUrl bibUrl = batch.get(i);
					ps.setLong(1, bibUrl.getReadingRoomObjectId()); 
					ps.setString(2, bibUrl.getUrl());
					ps.setString(3, bibUrl.getLinkText());
				}
								
				public int getBatchSize() {
					return batch.size();
				}
			});			
		}
		
		return count;
	}
}
