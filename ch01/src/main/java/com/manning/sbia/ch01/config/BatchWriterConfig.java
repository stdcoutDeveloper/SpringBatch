package com.manning.sbia.ch01.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.manning.sbia.ch01.batch.ProductJdbcItemWriter;

@Configuration
public class BatchWriterConfig {

	@Bean
    public ProductJdbcItemWriter writer(DataSource dataSource) {
        return new ProductJdbcItemWriter(dataSource);
    }
}
