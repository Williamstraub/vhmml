package org.vhmml.repository.readingroom;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.vhmml.entity.readingroom.ReadingRoomObjectFeature;
import org.vhmml.repository.AbstractBatchUpdateRepository;

public class ReadingRoomObjectFeatureRepositoryImpl extends AbstractBatchUpdateRepository {

	private static final String BATCH_INSERT_SQL = "INSERT INTO vhmml.reading_room_object_features(reading_room_object_id, feature_id)VALUES(?,?)";
	
	public int batchSave(List<ReadingRoomObjectFeature> objectFeatures) {
		int count = 0;
		List<List<ReadingRoomObjectFeature>> recordBatches = ListUtils.partition(objectFeatures, BATCH_SIZE);
		
		for(final List<ReadingRoomObjectFeature> batch : recordBatches) {
			// executeBatch resides on AbstractBatchUpdateRepository
			count += executeBatch(BATCH_INSERT_SQL, new BatchPreparedStatementSetter() {
				
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ReadingRoomObjectFeature objectFeature = batch.get(i);
					ps.setLong(1, objectFeature.getReadingRoomObjectId()); 
					ps.setLong(2, objectFeature.getFeatureId());										
				}
								
				public int getBatchSize() {
					return batch.size();
				}
			});			
		}
		
		return count;
	}
}
