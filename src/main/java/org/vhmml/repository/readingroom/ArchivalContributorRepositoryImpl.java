package org.vhmml.repository.readingroom;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Component;
import org.vhmml.entity.readingroom.ArchivalContributor;
import org.vhmml.repository.AbstractBatchUpdateRepository;

@Component
public class ArchivalContributorRepositoryImpl extends AbstractBatchUpdateRepository {
 
	private static final String BATCH_INSERT_SQL = "INSERT INTO reading_room_archival_contributors (id, archival_data_id, contributor_id, contributor_type, name_ns) VALUES(?, ?, ?, ?, ?)";	
	
	public int batchSave(List<ArchivalContributor> records) {
		int count = 0;
		List<List<ArchivalContributor>> recordBatches = ListUtils.partition(records, BATCH_SIZE);
		
		for(final List<ArchivalContributor> batch : recordBatches) {
			// executeBatch resides on AbstractBatchUpdateRepository
			count += executeBatch(BATCH_INSERT_SQL, new BatchPreparedStatementSetter() {
				
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ArchivalContributor archivalContributor = batch.get(i);					
					
					ps.setObject(1, archivalContributor.getId());
					ps.setLong(2, archivalContributor.getParentArchivalData().getId());
					ps.setObject(3, archivalContributor.getContributor().getId());
					ps.setString(4, archivalContributor.getType().toString());
					ps.setString(5, archivalContributor.getNameNs());					
				}
								
				public int getBatchSize() {
					return batch.size();
				}
			});			
		}
		
		return count;
	}
}
