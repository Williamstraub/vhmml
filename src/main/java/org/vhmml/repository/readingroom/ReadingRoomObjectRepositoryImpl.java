package org.vhmml.repository.readingroom;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Component;
import org.vhmml.entity.readingroom.ReadingRoomObject;
import org.vhmml.entity.readingroom.ReadingRoomObject.AccessRestriction;
import org.vhmml.entity.readingroom.ReadingRoomObject.Status;
import org.vhmml.repository.AbstractBatchUpdateRepository;

@Component
public class ReadingRoomObjectRepositoryImpl extends AbstractBatchUpdateRepository {
 
	private static final String BATCH_INSERT_SQL = "INSERT INTO reading_room_objects (id, processed_by, repository_id, shelf_mark, common_name, physical_notes, binding, provenance, bibliography, notes, hmml_project_number, inputter, input_date, reproduction_notes, acknowledgments, city_id, country_id, object_type, capture_date, access_restriction, viewable_online, collation, download_option, icon_name, cite_as, data_source, medium, current_status, condition_notes, summary, holding_institution_id, surrogate_format_id, features_imported, binding_height, binding_width, binding_depth, binding_dimensions_imported, right_to_left, foliation) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";	
	
	public int batchSave(List<ReadingRoomObject> records) {
		int count = 0;
		List<List<ReadingRoomObject>> recordBatches = ListUtils.partition(records, BATCH_SIZE);
		
		for(final List<ReadingRoomObject> batch : recordBatches) {
			// executeBatch resides on AbstractBatchUpdateRepository
			count += executeBatch(BATCH_INSERT_SQL, new BatchPreparedStatementSetter() {
				
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ReadingRoomObject readingRoomObject = batch.get(i);
					
					ps.setLong(1, readingRoomObject.getId());
					ps.setString(2, readingRoomObject.getProcessedBy());
					Long repoId = readingRoomObject.getRepository() != null ? readingRoomObject.getRepository().getId() : null;					
					ps.setObject(3, repoId);
					ps.setString(4, readingRoomObject.getShelfMark());
					ps.setString(5, readingRoomObject.getCommonName());
					ps.setString(6, readingRoomObject.getPhysicalNotes());
					ps.setString(7, readingRoomObject.getBinding());
					ps.setString(8, readingRoomObject.getProvenance());
					ps.setString(9, readingRoomObject.getBibliography());
					ps.setString(10, readingRoomObject.getNotes());
					ps.setString(11, readingRoomObject.getHmmlProjectNumber());
					ps.setString(12, readingRoomObject.getInputter());
					ps.setString(13, readingRoomObject.getInputDate());					
					ps.setString(14, readingRoomObject.getReproductionNotes());
					ps.setString(15, readingRoomObject.getAcknowledgments());					

					Long cityId = readingRoomObject.getCity() != null ? readingRoomObject.getCity().getId() : null;
					ps.setObject(16, cityId);				
					
					Long countryId = readingRoomObject.getCountry() != null ? readingRoomObject.getCountry().getId() : null;
					ps.setObject(17, countryId);					
					ps.setString(18, readingRoomObject.getType().name());
					
					if(readingRoomObject.getCaptureDate() != null) {
						ps.setDate(19, new java.sql.Date(readingRoomObject.getCaptureDate().getTime()));
					} else {
						ps.setDate(19, null);
					}
					
					String accessRestriction = readingRoomObject.getAccessRestriction() != null ? readingRoomObject.getAccessRestriction().name() : AccessRestriction.REGISTERED.name();					
					ps.setString(20, accessRestriction);
					ps.setBoolean(21, readingRoomObject.isViewableOnline());
					ps.setString(22, readingRoomObject.getCollation());
					ps.setString(23, readingRoomObject.getDownloadOption());
					ps.setString(24, readingRoomObject.getIconName());
					ps.setString(25, readingRoomObject.getCiteAs());
					ps.setString(26, readingRoomObject.getDataSource());
					ps.setString(27, readingRoomObject.getMedium());					
					ps.setString(28, readingRoomObject.getCurrentStatus() != null ? readingRoomObject.getCurrentStatus().name() : Status.UNKNOWN.name());
					ps.setString(29, readingRoomObject.getConditionNotes());			
					ps.setString(30, readingRoomObject.getSummary());					
					ps.setObject(31, readingRoomObject.getHoldingInstitution() != null ? readingRoomObject.getHoldingInstitution().getId() : null);
					ps.setObject(32, readingRoomObject.getSurrogateFormat() != null ? readingRoomObject.getSurrogateFormat().getId() : null);
					ps.setString(33, readingRoomObject.getFeaturesImported());
					ps.setObject(34, readingRoomObject.getBindingHeight() != null ? readingRoomObject.getBindingHeight() : null);
					ps.setObject(35, readingRoomObject.getBindingWidth() != null ? readingRoomObject.getBindingWidth() : null);
					ps.setObject(36, readingRoomObject.getBindingDepth() != null ? readingRoomObject.getBindingDepth() : null);					
					ps.setString(37, readingRoomObject.getBindingDimensionsImported());
					ps.setBoolean(38, readingRoomObject.isRightToLeft());					
					ps.setString(39, readingRoomObject.getFoliation());
				}
								
				public int getBatchSize() {
					return batch.size();
				}
			});			
		}
		
		return count;
	}
}
