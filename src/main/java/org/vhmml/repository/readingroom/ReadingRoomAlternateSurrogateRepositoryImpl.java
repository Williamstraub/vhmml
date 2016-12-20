package org.vhmml.repository.readingroom;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.vhmml.entity.readingroom.AlternateSurrogate;
import org.vhmml.repository.AbstractBatchUpdateRepository;

public class ReadingRoomAlternateSurrogateRepositoryImpl extends AbstractBatchUpdateRepository {

	private static final String BATCH_INSERT_SQL = "INSERT INTO vhmml.reading_room_object_alternate_surrogates(reading_room_object_id, name)VALUES(?,?)";
	
	public int batchSave(List<AlternateSurrogate> altSurrogates) {
		int count = 0;
		List<List<AlternateSurrogate>> recordBatches = ListUtils.partition(altSurrogates, BATCH_SIZE);
		
		for(final List<AlternateSurrogate> batch : recordBatches) {
			// executeBatch resides on AbstractBatchUpdateRepository
			count += executeBatch(BATCH_INSERT_SQL, new BatchPreparedStatementSetter() {
				
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					AlternateSurrogate altSurrogate = batch.get(i);
					ps.setLong(1, altSurrogate.getReadingRoomObjectId()); 
					ps.setString(2, altSurrogate.getName());										
				}
								
				public int getBatchSize() {
					return batch.size();
				}
			});			
		}
		
		return count;
	}
}
