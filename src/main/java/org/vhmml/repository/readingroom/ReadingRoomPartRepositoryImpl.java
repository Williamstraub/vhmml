package org.vhmml.repository.readingroom;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.vhmml.entity.readingroom.ReadingRoomObjectPart;
import org.vhmml.repository.AbstractBatchUpdateRepository;

public class ReadingRoomPartRepositoryImpl extends AbstractBatchUpdateRepository {	
	
	private static final String BATCH_INSERT_SQL = "INSERT INTO reading_room_object_parts(id,reading_room_object_id,part_number,support_dimensions_imported,country_of_origin,cardinal_of_origin,region_of_origin,city_of_origin,century_imported,ymd_date_imported,layout,script_imported,number_scribes,scribe,support_imported,artist,part_notes,begin_date,end_date,seq,acknowledgments,writing_system_imported,native_date_precise,catchwords,decoration,colophon_part, artist_uri, country_of_origin_uncertain, city_of_origin_uncertain, century_uncertain, date_precise_year, date_precise_month, date_precise_day, writing_space_imported, writing_space_height, writing_space_width, foliation_imported, signatures, medium) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public int batchSave(List<ReadingRoomObjectPart> records) {
		int count = 0;
		List<List<ReadingRoomObjectPart>> recordBatches = ListUtils.partition(records, BATCH_SIZE);
		
		for(final List<ReadingRoomObjectPart> batch : recordBatches) {
			// executeBatch resides on AbstractBatchUpdateRepository
			count += executeBatch(BATCH_INSERT_SQL, new BatchPreparedStatementSetter() {
				
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ReadingRoomObjectPart part = batch.get(i);
					
					ps.setLong(1, part.getId());
					ps.setLong(2, part.getParentObject().getId());
					ps.setInt(3, part.getPartNumber());
					ps.setString(4, part.getSupportDimensionsImported());					
					ps.setObject(5, part.getCountryOfOrigin());
					ps.setString(6, part.getCardinalOfOrigin());
					ps.setString(7, part.getRegionOfOrigin());
					ps.setObject(8, part.getCityOfOrigin());
					ps.setString(9, part.getCenturyImported());
					ps.setString(10, part.getYmdDateImported());
					ps.setString(11, part.getLayout());
					ps.setString(12, part.getScriptImported());
					ps.setString(13, part.getNumberScribes());
					ps.setString(14, part.getScribe());
					ps.setString(15, part.getSupportImported());
					ps.setString(16, part.getArtist());
					ps.setString(17, part.getPartNotes());
					ps.setObject(18, part.getBeginDate());
					ps.setObject(19, part.getEndDate());					
					ps.setInt(20, part.getSeq());
					ps.setString(21, part.getAcknowledgments());
					ps.setString(22, part.getWritingSystemImported());
					ps.setString(23, part.getNativeDatePrecise());
					ps.setBoolean(24, part.isCatchwords());
					ps.setString(25, part.getDecoration());
					ps.setString(26, part.getColophonPart());
					ps.setString(27, part.getArtistUri());
					ps.setBoolean(28, part.isCountryOfOriginUncertain());
					ps.setBoolean(29, part.isCityOfOriginUncertain());
					ps.setBoolean(30, part.isCenturyUncertain());					
					ps.setObject(31, part.getDatePreciseYear());
					ps.setObject(32, part.getDatePreciseMonth());
					ps.setObject(33, part.getDatePreciseDay());
					ps.setString(34, part.getWritingSpaceImported());
					ps.setObject(35, part.getWritingSpaceHeight());
					ps.setObject(36, part.getWritingSpaceWidth());
					ps.setString(37, part.getFoliationImported());					
					ps.setBoolean(38, part.isSignatures());
					ps.setString(39, part.getMedium());
				}
								
				public int getBatchSize() {
					return batch.size();
				}
			});			
		}
		
		return count;
	}
}
