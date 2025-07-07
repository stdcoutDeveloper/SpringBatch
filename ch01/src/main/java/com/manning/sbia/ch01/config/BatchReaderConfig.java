package com.manning.sbia.ch01.config;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.manning.sbia.ch01.batch.ProductFieldSetMapper;
import com.manning.sbia.ch01.domain.Product;

@Configuration
public class BatchReaderConfig {
	
	@Bean
	@StepScope
	public FlatFileItemReader<Product> reader(
			@Value("#{jobParameters['targetDirectory']}") String dir,
			@Value("#{jobParameters['targetFile']}") String file
	) {
		String fullPath = dir + file;
		
		FlatFileItemReader<Product> reader = new FlatFileItemReader<>();
		// the input file
		reader.setResource(new FileSystemResource(fullPath));
		// Skips first line
		reader.setLinesToSkip(1);
		reader.setLineMapper(lineMapper());
		return reader;
	}
	
	@Bean
	public DefaultLineMapper<Product> lineMapper() {
	    DefaultLineMapper<Product> mapper = new DefaultLineMapper<>();
	    mapper.setLineTokenizer(lineTokenizer());
	    mapper.setFieldSetMapper(fieldSetMapper());
	    return mapper;
	}

	@Bean
	public DelimitedLineTokenizer lineTokenizer() {
	    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
	    tokenizer.setNames("PRODUCT_ID", "NAME", "DESCRIPTION", "PRICE");
	    return tokenizer;
	}
	
	@Bean
	public ProductFieldSetMapper fieldSetMapper() {
	    return new ProductFieldSetMapper();
	}
}
