package org.vhmml.repository.readingroom;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.vhmml.entity.readingroom.ReadingRoomObjectGenre;
import org.vhmml.repository.AbstractBatchUpdateRepository;

public class ReadingRoomObjectGenreRepositoryImpl extends AbstractBatchUpdateRepository {

	private static final String BATCH_INSERT_SQL = "INSERT INTO vhmml.reading_room_object_genres(reading_room_object_id, genre_id)VALUES(?,?)";
	
	public int batchSave(List<ReadingRoomObjectGenre> objectGenres) {
		int count = 0;
		List<List<ReadingRoomObjectGenre>> recordBatches = ListUtils.partition(objectGenres, BATCH_SIZE);
		
		for(final List<ReadingRoomObjectGenre> batch : recordBatches) {
			// executeBatch resides on AbstractBatchUpdateRepository
			count += executeBatch(BATCH_INSERT_SQL, new BatchPreparedStatementSetter() {
				
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ReadingRoomObjectGenre objectGenre = batch.get(i);
					ps.setLong(1, objectGenre.getReadingRoomObjectId()); 
					ps.setLong(2, objectGenre.getGenreId());										
				}
								
				public int getBatchSize() {
					return batch.size();
				}
			});			
		}
		
		return count;
	}
}
