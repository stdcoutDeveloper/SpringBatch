/**
 * 
 */
package com.manning.sbia.ch01;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author acogoluegnes
 *
 */
@SpringBootTest
@SpringBatchTest
@ContextConfiguration(locations = { "/import-products-job-context.xml", "/test-context.xml" })
public class ImportProductsIntegrationTest {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	public void setUp() throws Exception {
		jdbcTemplate.update("delete from product");
		jdbcTemplate.update("insert into product (id,name,description,price) values(?,?,?,?)", "PR....214",
				"Nokia 2610 Phone", "", 102.23);
	}

	@Test
	public void importProducts() throws Exception {
		int initial = jdbcTemplate.queryForObject("select count(1) from product", Integer.class);

		jobLauncher.run(job, new JobParametersBuilder().addString("inputResource", "classpath:/input/products.zip")
				.addString("targetDirectory", "./target/importproductsbatch/").addString("targetFile", "products.txt")
				.addLong("timestamp", System.currentTimeMillis()).toJobParameters());
		int nbOfNewProducts = 7;
		assertEquals(initial + nbOfNewProducts,
				jdbcTemplate.queryForObject("select count(1) from product", Integer.class));
	}

	@Test
	public void importProductsWithErrors() throws Exception {
		int initial = jdbcTemplate.queryForObject("select count(1) from product", Integer.class);

		jobLauncher.run(job,
				new JobParametersBuilder().addString("inputResource", "classpath:/input/products_with_errors.zip")
						.addString("targetDirectory", "./target/importproductsbatch/")
						.addString("targetFile", "products.txt").addLong("timestamp", System.currentTimeMillis())
						.toJobParameters());
		int nbOfNewProducts = 6;
		assertEquals(initial + nbOfNewProducts,
				jdbcTemplate.queryForObject("select count(1) from product", Integer.class));
	}

}
