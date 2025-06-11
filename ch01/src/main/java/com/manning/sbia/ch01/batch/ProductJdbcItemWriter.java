/**
 * 
 */
package com.manning.sbia.ch01.batch;

import javax.sql.DataSource;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.manning.sbia.ch01.domain.Product;

/**
 * @author acogoluegnes
 *
 */
public class ProductJdbcItemWriter implements ItemWriter<Product> {

	private static final String INSERT_PRODUCT = "insert into product (id,name,description,price) values(?,?,?,?)";

	private static final String UPDATE_PRODUCT = "update product set name=?, description=?, price=? where id = ?";

	private JdbcTemplate jdbcTemplate;

	public ProductJdbcItemWriter(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void write(Chunk<? extends Product> chunk) throws Exception {
		for (Product item : chunk) {
			int updated = jdbcTemplate.update(UPDATE_PRODUCT, item.getName(), item.getDescription(), item.getPrice(),
					item.getId());
			if (updated == 0) {
				jdbcTemplate.update(INSERT_PRODUCT, item.getId(), item.getName(), item.getDescription(),
						item.getPrice());
			}
		}
	}

}
