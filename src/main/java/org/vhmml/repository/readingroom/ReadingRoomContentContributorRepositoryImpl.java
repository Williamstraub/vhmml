package org.vhmml.repository.readingroom;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Component;
import org.vhmml.entity.readingroom.ContentContributor;
import org.vhmml.repository.AbstractBatchUpdateRepository;

@Component
public class ReadingRoomContentContributorRepositoryImpl extends AbstractBatchUpdateRepository {
	private static final String BATCH_INSERT_SQL = "INSERT INTO reading_room_content_contributors(reading_room_content_id, reading_room_contributor_id, contributor_type, name_ns) VALUES(?, ?, ?, ?)";	
	
	public int batchSave(List<ContentContributor> records) {
		int count = 0;
		List<List<ContentContributor>> recordBatches = ListUtils.partition(records, BATCH_SIZE);
		
		for(final List<ContentContributor> batch : recordBatches) {
			// executeBatch resides on AbstractBatchUpdateRepository
			count += executeBatch(BATCH_INSERT_SQL, new BatchPreparedStatementSetter() {
				
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ContentContributor contentContributor = batch.get(i);					
					ps.setLong(1, contentContributor.getContent().getId());
					ps.setLong(2, contentContributor.getContributor().getId());		
					ps.setString(3, contentContributor.getType().name());
					ps.setString(4, contentContributor.getNameNs());					
				}
								
				public int getBatchSize() {
					return batch.size();
				}
			});			
		}
		
		return count;
	}
}
