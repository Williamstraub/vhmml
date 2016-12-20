package org.vhmml.repository;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class AbstractBatchUpdateRepository {
	
	public static final Integer BATCH_SIZE = 1000;
	
	private JdbcTemplate jdbcTemplate;  
	
	@Autowired
	private EntityManager entityManager;

	protected int executeBatch(String sql, BatchPreparedStatementSetter batchPreparedStatementSetter) {
		Validate.notNull(jdbcTemplate, "Datasource not set for batch statement.");
		int[] rows = jdbcTemplate.batchUpdate(sql, batchPreparedStatementSetter);
		entityManager.flush();
		entityManager.clear();
		
		return sumRows(rows);
	}

	private int sumRows(int[] rows) {
		int rowCount = 0;
		for (int row : rows) {
			rowCount += row;
		}
		return rowCount;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}
