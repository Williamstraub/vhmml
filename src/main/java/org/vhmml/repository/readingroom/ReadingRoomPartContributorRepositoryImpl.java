package org.vhmml.repository.readingroom;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.vhmml.entity.readingroom.ReadingRoomPartContributor;
import org.vhmml.repository.AbstractBatchUpdateRepository;

public class ReadingRoomPartContributorRepositoryImpl extends AbstractBatchUpdateRepository {
	private static final String BATCH_INSERT_SQL = "INSERT INTO reading_room_part_contributors(part_id, contributor_id, contributor_type, name_ns) VALUES (?, ?, ?, ?)";	
	
	public int batchSave(List<ReadingRoomPartContributor> records) {
		int count = 0;
		List<List<ReadingRoomPartContributor>> recordBatches = ListUtils.partition(records, BATCH_SIZE);
		
		for(final List<ReadingRoomPartContributor> batch : recordBatches) {
			// executeBatch resides on AbstractBatchUpdateRepository
			count += executeBatch(BATCH_INSERT_SQL, new BatchPreparedStatementSetter() {
				
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ReadingRoomPartContributor partContributor = batch.get(i);
					
					ps.setLong(1, partContributor.getParentPart().getId());
					ps.setLong(2, partContributor.getContributor().getId());
					ps.setString(3, partContributor.getType().name());
					ps.setString(4,  partContributor.getNameNs());
				}
								
				public int getBatchSize() {
					return batch.size();
				}
			});			
		}
		
		return count;
	}
}
