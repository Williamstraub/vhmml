package org.vhmml.repository.readingroom;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.vhmml.entity.readingroom.Extent;
import org.vhmml.entity.readingroom.ReadingRoomObjectContributor;
import org.vhmml.repository.AbstractBatchUpdateRepository;

public class ReadingRoomObjectContributorRepositoryImpl extends AbstractBatchUpdateRepository {
	
	private static final String BATCH_INSERT_SQL = "INSERT INTO reading_room_object_contributors(reading_room_object_id, contributor_id, type, name_ns) VALUES (?,?,?,?)";	
	
	public int batchSave(List<ReadingRoomObjectContributor> objectContributors) {
		int count = 0;
		List<List<ReadingRoomObjectContributor>> recordBatches = ListUtils.partition(objectContributors, BATCH_SIZE);
		
		for(final List<ReadingRoomObjectContributor> batch : recordBatches) {
			// executeBatch resides on AbstractBatchUpdateRepository
			count += executeBatch(BATCH_INSERT_SQL, new BatchPreparedStatementSetter() {
				
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ReadingRoomObjectContributor objectContributor = batch.get(i);
					ps.setLong(1, objectContributor.getReadingRoomObject().getId()); 
					ps.setLong(2, objectContributor.getContributor().getId());
					String type = objectContributor.getType() != null ? objectContributor.getType().name() : null;
					ps.setObject(3, type);
					ps.setString(4, objectContributor.getNameNs());
				}
								
				public int getBatchSize() {
					return batch.size();
				}
			});			
		}
		
		return count;
	}
}
