package com.manning.sbia.ch01.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchJobConfig {

	@Bean
	public Job importProductsJob(JobRepository jobRepository, Step decompressStep, Step readWriteProductsStep) {
		return new JobBuilder("importProducts", jobRepository)
				.start(decompressStep)
				.next(readWriteProductsStep)
				.build();
	}
}
