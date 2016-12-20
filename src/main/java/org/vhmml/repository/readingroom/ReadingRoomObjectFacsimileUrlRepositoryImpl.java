package org.vhmml.repository.readingroom;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.vhmml.entity.readingroom.FacsimileUrl;
import org.vhmml.repository.AbstractBatchUpdateRepository;

public class ReadingRoomObjectFacsimileUrlRepositoryImpl extends AbstractBatchUpdateRepository {

	private static final String BATCH_INSERT_SQL = "INSERT INTO vhmml.reading_room_facsimile_urls(reading_room_object_id, url)VALUES(?,?)";
	
	public int batchSave(List<FacsimileUrl> bibUrls) {
		int count = 0;
		List<List<FacsimileUrl>> recordBatches = ListUtils.partition(bibUrls, BATCH_SIZE);
		
		for(final List<FacsimileUrl> batch : recordBatches) {
			// executeBatch resides on AbstractBatchUpdateRepository
			count += executeBatch(BATCH_INSERT_SQL, new BatchPreparedStatementSetter() {
				
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					FacsimileUrl bibUrl = batch.get(i);
					ps.setLong(1, bibUrl.getReadingRoomObjectId()); 
					ps.setString(2, bibUrl.getUrl());										
				}
								
				public int getBatchSize() {
					return batch.size();
				}
			});			
		}
		
		return count;
	}
}
