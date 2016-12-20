package org.vhmml.repository.readingroom;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Component;
import org.vhmml.entity.readingroom.ArchivalData;
import org.vhmml.repository.AbstractBatchUpdateRepository;

@Component
public class ArchivalDataRepositoryImpl extends AbstractBatchUpdateRepository {
 
	private static final String BATCH_INSERT_SQL = "INSERT INTO reading_room_archival_data (id, collection_fond, series, sub_series, container_type, container_count, creator, scope_content, historical_note, rec_type, custodial_history, dimensions, support, date_precise_year, date_precise_month, date_precise_day, begin_date, end_date, native_date, decoration, sub_sub_series, writing_system, century_uncertain, title, reading_room_object_id, century_imported) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";	
	
	public int batchSave(List<ArchivalData> records) {
		int count = 0;
		List<List<ArchivalData>> recordBatches = ListUtils.partition(records, BATCH_SIZE);
		
		for(final List<ArchivalData> batch : recordBatches) {
			// executeBatch resides on AbstractBatchUpdateRepository
			count += executeBatch(BATCH_INSERT_SQL, new BatchPreparedStatementSetter() {
				
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ArchivalData archivalData = batch.get(i);					
					
					ps.setObject(1, archivalData.getId());
					ps.setString(2, archivalData.getCollectionFond());
					ps.setString(3, archivalData.getSeries());
					ps.setString(4, archivalData.getSubSeries());
					ps.setString(5, archivalData.getContainerType());
					ps.setObject(6, archivalData.getContainerCount());
					ps.setString(7, archivalData.getCreator());
					ps.setString(8, archivalData.getScopeContent());
					ps.setString(9, archivalData.getHistoricalNote());
					ps.setString(10, archivalData.getRecType());
					ps.setString(11, archivalData.getCustodialHistory());
					ps.setString(12, archivalData.getDimensions());
					ps.setString(13, archivalData.getSupport());
					ps.setObject(14, archivalData.getDatePreciseYear());
					ps.setObject(15, archivalData.getDatePreciseMonth());
					ps.setObject(16, archivalData.getDatePreciseDay());
					ps.setObject(17, archivalData.getBeginDate());
					ps.setObject(18, archivalData.getEndDate());
					ps.setString(19, archivalData.getNativeDate());
					ps.setString(20, archivalData.getDecoration());
					ps.setString(21, archivalData.getSubSubSeries());
					ps.setString(22, archivalData.getWritingSystem());
					ps.setBoolean(23, archivalData.isCenturyUncertain());
					ps.setString(24, archivalData.getTitle());	
					ps.setLong(25, archivalData.getParentObject().getId());
					ps.setString(26, archivalData.getCenturyImported());
				}
								
				public int getBatchSize() {
					return batch.size();
				}
			});			
		}
		
		return count;
	}
}
