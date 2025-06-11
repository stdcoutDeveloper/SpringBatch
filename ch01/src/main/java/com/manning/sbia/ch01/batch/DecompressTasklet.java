/**
 * 
 */
package com.manning.sbia.ch01.batch;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

/**
 * @author acogoluegnes
 *
 */
public class DecompressTasklet implements Tasklet {

	// the archive file
	private Resource inputResource;

	private String targetDirectory;

	private String targetFile;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// Opens archive
		try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(inputResource.getInputStream()))) {
			// Creates target directory if absent
			File targetDirectoryAsFile = new File(targetDirectory);
			if (!targetDirectoryAsFile.exists()) {
				FileUtils.forceMkdir(targetDirectoryAsFile);
			}

			// Decompresses archive
			File target = new File(targetDirectory, targetFile);

			while (zis.getNextEntry() != null) {
				if (!target.exists()) {
					target.createNewFile();
				}

				try (FileOutputStream fos = new FileOutputStream(target);
					 BufferedOutputStream dest = new BufferedOutputStream(fos)) {
					IOUtils.copy(zis, dest);
//					dest.flush();
				}

			}

			if (!target.exists()) {
				throw new IllegalStateException("Could not decompress anything from the archive!");
			}

			// Tasklet finishes
			return RepeatStatus.FINISHED;
		}
	}

	/* setters */
	public void setInputResource(Resource inputResource) {
		this.inputResource = inputResource;
	}

	public void setTargetDirectory(String targetDirectory) {
		this.targetDirectory = targetDirectory;
	}

	public void setTargetFile(String targetFile) {
		this.targetFile = targetFile;
	}

}
