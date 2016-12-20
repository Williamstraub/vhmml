package org.vhmml.repository.readingroom;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.vhmml.entity.readingroom.Extent;
import org.vhmml.repository.AbstractBatchUpdateRepository;

public class ReadingRoomObjectExtentRepositoryImpl extends AbstractBatchUpdateRepository {
	
	private static final String BATCH_INSERT_SQL = "INSERT INTO vhmml.reading_room_object_extents(reading_room_object_id, extent_count, extent_type, folio_imported)VALUES(?,?,?,?)";	
	
	public int batchSave(List<Extent> extents) {
		int count = 0;
		List<List<Extent>> recordBatches = ListUtils.partition(extents, BATCH_SIZE);
		
		for(final List<Extent> batch : recordBatches) {
			// executeBatch resides on AbstractBatchUpdateRepository
			count += executeBatch(BATCH_INSERT_SQL, new BatchPreparedStatementSetter() {
				
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Extent extent = batch.get(i);
					ps.setLong(1, extent.getReadingRoomObjectId()); 
					ps.setObject(2, extent.getCount());
					String type = extent.getType() != null ? extent.getType().name() : null;
					ps.setObject(3, type);
					ps.setString(4, extent.getFolioImported());					
				}
								
				public int getBatchSize() {
					return batch.size();
				}
			});			
		}
		
		return count;
	}
}
