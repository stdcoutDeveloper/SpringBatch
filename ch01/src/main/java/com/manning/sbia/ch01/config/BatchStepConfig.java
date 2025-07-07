package com.manning.sbia.ch01.config;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.manning.sbia.ch01.batch.DecompressTasklet;
import com.manning.sbia.ch01.domain.Product;

@Configuration
public class BatchStepConfig {

	@Bean
    public Step decompressStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, DecompressTasklet decompressTasklet) {
        return new StepBuilder("decompress", jobRepository)
                .tasklet(decompressTasklet, transactionManager)
                .build();
    }

	@Bean
	public Step readWriteProductsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			ItemReader<Product> reader, ItemWriter<Product> writer) {

		return new StepBuilder("readWriteProducts", jobRepository)
				.<Product, Product>chunk(100, transactionManager)
				.reader(reader)
				.writer(writer)
				.faultTolerant()
				.skipLimit(5)
				.skip(FlatFileParseException.class)
				.build();
	}
}
