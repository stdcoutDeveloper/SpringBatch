package com.manning.sbia.ch01.config;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.manning.sbia.ch01.batch.DecompressTasklet;

@Configuration
public class BatchTaskletConfig {

	@Bean
    @StepScope
    public DecompressTasklet decompressTasklet(
            @Value("#{jobParameters['inputResource']}") Resource inputResource,
            @Value("#{jobParameters['targetDirectory']}") String targetDirectory,
            @Value("#{jobParameters['targetFile']}") String targetFile
    ) {
        DecompressTasklet tasklet = new DecompressTasklet();
        tasklet.setInputResource(inputResource);
        tasklet.setTargetDirectory(targetDirectory);
        tasklet.setTargetFile(targetFile);
        return tasklet;
    }
}
