package org.vhmml.repository.readingroom;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Component;
import org.vhmml.entity.readingroom.ContentLanguage;
import org.vhmml.repository.AbstractBatchUpdateRepository;

@Component
public class ReadingRoomContentLanguageRepositoryImpl extends AbstractBatchUpdateRepository {
	private static final String BATCH_INSERT_SQL = "INSERT INTO reading_room_content_languages (reading_room_content_id, language_id) VALUES(?,?)";
	
	public int batchSave(List<ContentLanguage> records) {
		int count = 0;
		List<List<ContentLanguage>> recordBatches = ListUtils.partition(records, BATCH_SIZE);
		
		for(final List<ContentLanguage> batch : recordBatches) {
			// executeBatch resides on AbstractBatchUpdateRepository
			count += executeBatch(BATCH_INSERT_SQL, new BatchPreparedStatementSetter() {
				
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ContentLanguage contentLanguage = batch.get(i);					
					
					ps.setLong(1, contentLanguage.getReadingRoomContentId());
					ps.setLong(2, contentLanguage.getLanguageId());					
				}
								
				public int getBatchSize() {
					return batch.size();
				}
			});			
		}
		
		return count;
	}
}
