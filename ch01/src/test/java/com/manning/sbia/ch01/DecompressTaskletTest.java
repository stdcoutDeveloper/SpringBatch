/**
 * 
 */
package com.manning.sbia.ch01;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import com.manning.sbia.ch01.batch.DecompressTasklet;

/**
 * @author acogoluegnes
 *
 */
public class DecompressTaskletTest {

	private static final String[] EXPECTED_CONTENT = new String[] { 
			"PRODUCT_ID,NAME,DESCRIPTION,PRICE",
			"PR....210,BlackBerry 8100 Pearl,,124.60",
			"PR....211,Sony Ericsson W810i,,139.45",
			"PR....212,Samsung MM-A900M Ace,,97.80",
			"PR....213,Toshiba M285-E 14,,166.20",
			"PR....214,Nokia 2610 Phone,,145.50",
			"PR....215,CN Clogs Beach/Garden Clog,,190.70",
			"PR....216,AT&T 8525 PDA,,289.20",
			"PR....217,Canon Digital Rebel XT 8MP Digital SLR Camera,,13.70" 
	};

	private File outputDir;

	@BeforeEach
	public void setUp() throws Exception {
		outputDir = new File("./target/decompresstasklet");
		if (outputDir.exists()) {
			FileUtils.deleteDirectory(outputDir);
		}
	}

	@Test
	public void testExecute() throws Exception {
		DecompressTasklet tasklet = new DecompressTasklet();
		tasklet.setInputResource(new ClassPathResource("/input/products.zip"));
		tasklet.setTargetDirectory(outputDir.getAbsolutePath());
		tasklet.setTargetFile("products.txt");

		tasklet.execute(null, null);

		File output = new File(outputDir, "products.txt");
		assertTrue(output.exists(), "Output file should exist");

		List<String> lines = FileUtils.readLines(output, StandardCharsets.UTF_8);
		assertArrayEquals(EXPECTED_CONTENT, lines.toArray(), "File content should match expected content");
	}

	@Test
	public void testCorruptedArchive() throws Exception {
		DecompressTasklet tasklet = new DecompressTasklet();
		tasklet.setInputResource(new ClassPathResource("/input/products_corrupted.zip"));
		tasklet.setTargetDirectory(outputDir.getAbsolutePath());
		tasklet.setTargetFile("products.txt");

		Exception exception = assertThrows(Exception.class, () -> {
			tasklet.execute(null, null);
		});

		assertNotNull(exception, "corrupted archive, the tasklet should have thrown an exception");
	}
}
