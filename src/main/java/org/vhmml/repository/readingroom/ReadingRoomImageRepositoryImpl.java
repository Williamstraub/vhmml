package org.vhmml.repository.readingroom;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.vhmml.entity.readingroom.Image;
import org.vhmml.repository.AbstractBatchUpdateRepository;

public class ReadingRoomImageRepositoryImpl extends AbstractBatchUpdateRepository {
	private static final String BATCH_INSERT_SQL = "INSERT INTO vhmml.reading_room_images(id,reading_room_content_id,img_id,folio_number,caption,notes_to_photographer,icon_class,revisit,seq,url1)VALUES(?,?,?,?,?,?,?,?,?,?)";	
	
	public int batchSave(List<Image> records) {
		int count = 0;
		List<List<Image>> recordBatches = ListUtils.partition(records, BATCH_SIZE);
		
		for(final List<Image> batch : recordBatches) {
			// executeBatch resides on AbstractBatchUpdateRepository
			count += executeBatch(BATCH_INSERT_SQL, new BatchPreparedStatementSetter() {
				
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Image image = batch.get(i);
					ps.setLong(1, image.getId());
					ps.setLong(2, image.getParentContent().getId());
					ps.setInt(3, image.getImgId());
					ps.setString(4, image.getFolioNumber());
					ps.setString(5, image.getCaption());
					ps.setString(6, image.getNotesToPhotographer());
					ps.setString(7, image.getIconClass());
					ps.setBoolean(8, image.isRevisit());
					ps.setInt(9, image.getSeq());
					ps.setString(10, image.getUrl1());
				}
								
				public int getBatchSize() {
					return batch.size();
				}
			});			
		}
		
		return count;
	}
}
