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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author acogoluegnes
 *
 */
@SpringBootTest
public class ImportProductsIntegrationTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.update("DELETE FROM product");

        jdbcTemplate.update(
                "INSERT INTO product (id, name, description, price) VALUES (?, ?, ?, ?)",
                "PR....214", "Nokia 2610 Phone", "", 102.23
        );
    }

    @Test
    public void importProducts() throws Exception {
        int initial = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM product", Integer.class);

        jobLauncher.run(job, new JobParametersBuilder()
                .addString("inputResource", "classpath:/input/products.zip")
                .addString("targetDirectory", "./target/importproductsbatch/")
                .addString("targetFile", "products.txt")
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters()
        );

        int nbOfNewProducts = 7;
        int actual = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM product", Integer.class);
        assertEquals(initial + nbOfNewProducts, actual, "Should import 7 new products");
    }

    @Test
    public void importProductsWithErrors() throws Exception {
        int initial = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM product", Integer.class);

        jobLauncher.run(job, new JobParametersBuilder()
                .addString("inputResource", "classpath:/input/products_with_errors.zip")
                .addString("targetDirectory", "./target/importproductsbatch/")
                .addString("targetFile", "products.txt")
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters()
        );

        int nbOfNewProducts = 6;
        int actual = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM product", Integer.class);
        assertEquals(initial + nbOfNewProducts, actual, "Should import 6 new products (1 error skipped)");
    }
}
