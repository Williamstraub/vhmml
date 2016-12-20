package org.vhmml.repository.readingroom;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Component;
import org.vhmml.entity.readingroom.ArchivalDataLanguage;
import org.vhmml.repository.AbstractBatchUpdateRepository;

@Component
public class ArchivalDataLanguageRepositoryImpl extends AbstractBatchUpdateRepository {
 
	private static final String BATCH_INSERT_SQL = "INSERT INTO reading_room_archival_data_languages (id, archival_data_id, language_id) VALUES(?, ?, ?)";	
	
	public int batchSave(List<ArchivalDataLanguage> records) {
		int count = 0;
		List<List<ArchivalDataLanguage>> recordBatches = ListUtils.partition(records, BATCH_SIZE);
		
		for(final List<ArchivalDataLanguage> batch : recordBatches) {
			// executeBatch resides on AbstractBatchUpdateRepository
			count += executeBatch(BATCH_INSERT_SQL, new BatchPreparedStatementSetter() {
				
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ArchivalDataLanguage archivalDataLanguage = batch.get(i);					
					
					ps.setObject(1, archivalDataLanguage .getId());
					ps.setLong(2, archivalDataLanguage.getArchivalDataId());
					ps.setLong(3, archivalDataLanguage.getLanguageId());									
				}
								
				public int getBatchSize() {
					return batch.size();
				}
			});			
		}
		
		return count;
	}
}
