package org.vhmml.repository.readingroom;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.vhmml.entity.readingroom.Content;
import org.vhmml.repository.AbstractBatchUpdateRepository;

public class ReadingRoomContentRepositoryImpl extends AbstractBatchUpdateRepository {
	private static final String BATCH_INSERT_SQL = "INSERT INTO vhmml.reading_room_content(id,reading_room_object_part_id,item_location,title_in_ms,provisional_title,title_ns,item_condition,rubric,incipit,explicit,item_notes,revisit,item_number,acknowledgments,colophon_text,text_bibliography, cataloger_tags, uniform_title_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";	
	
	public int batchSave(List<Content> records) {
		int count = 0;
		List<List<Content>> recordBatches = ListUtils.partition(records, BATCH_SIZE);
		
		for(final List<Content> batch : recordBatches) {
			// executeBatch resides on AbstractBatchUpdateRepository
			count += executeBatch(BATCH_INSERT_SQL, new BatchPreparedStatementSetter() {
				
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Content content = batch.get(i);
					ps.setLong(1, content.getId());
					ps.setLong(2, content.getParentPart().getId());
					ps.setString(3, content.getItemLocation());					
					ps.setString(4, content.getTitleInMs());
					ps.setString(5, content.getProvisionalTitle());
					ps.setString(6, content.getTitleNs());
					ps.setString(7, content.getItemCondition());
					ps.setString(8, content.getRubric());
					ps.setString(9, content.getIncipit());
					ps.setString(10, content.getExplicit());
					ps.setString(11, content.getItemNotes());
					ps.setBoolean(12, content.isRevisit());
					ps.setInt(13, content.getItemNumber());
					ps.setString(14, content.getAcknowledgments());					
					ps.setString(15, content.getColophonText());
					ps.setString(16, content.getTextBibliography());					
					ps.setString(17, content.getCatalogerTags());
					
					if(content.getUniformTitle() != null) {
						ps.setLong(18, content.getUniformTitle().getId());
					} else {
						ps.setObject(18, null);
					}
				}
								
				public int getBatchSize() {
					return batch.size();
				}
			});			
		}
		
		return count;
	}
}
