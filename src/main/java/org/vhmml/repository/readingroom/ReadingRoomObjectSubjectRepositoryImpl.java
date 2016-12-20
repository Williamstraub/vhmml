package org.vhmml.repository.readingroom;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Component;
import org.vhmml.entity.readingroom.ReadingRoomObjectSubject;
import org.vhmml.repository.AbstractBatchUpdateRepository;

@Component
public class ReadingRoomObjectSubjectRepositoryImpl extends AbstractBatchUpdateRepository {
	private static final String BATCH_INSERT_SQL = "INSERT INTO reading_room_object_subjects (reading_room_object_id, subject_id) VALUES(?,?)";	
	
	public int batchSave(List<ReadingRoomObjectSubject> records) {
		int count = 0;
		List<List<ReadingRoomObjectSubject>> recordBatches = ListUtils.partition(records, BATCH_SIZE);
		
		for(final List<ReadingRoomObjectSubject> batch : recordBatches) {
			// executeBatch resides on AbstractBatchUpdateRepository
			count += executeBatch(BATCH_INSERT_SQL, new BatchPreparedStatementSetter() {
				
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ReadingRoomObjectSubject contentSubject = batch.get(i);					
					
					ps.setLong(1, contentSubject.getReadingRoomObjectId());
					ps.setLong(2, contentSubject.getSubjectId());					
				}
								
				public int getBatchSize() {
					return batch.size();
				}
			});			
		}
		
		return count;
	}
}
