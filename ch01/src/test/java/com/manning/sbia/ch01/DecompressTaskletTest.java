/**
 * 
 */
package com.manning.sbia.ch01;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import com.manning.sbia.ch01.batch.DecompressTasklet;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * @author acogoluegnes
 *
 */
@SpringBootTest
@SpringBatchTest
public class DecompressTaskletTest {

	private static final String[] EXPECTED_CONTENT = new String[] { "PRODUCT_ID,NAME,DESCRIPTION,PRICE",
			"PR....210,BlackBerry 8100 Pearl,,124.60", "PR....211,Sony Ericsson W810i,,139.45",
			"PR....212,Samsung MM-A900M Ace,,97.80", "PR....213,Toshiba M285-E 14,,166.20",
			"PR....214,Nokia 2610 Phone,,145.50", "PR....215,CN Clogs Beach/Garden Clog,,190.70",
			"PR....216,AT&T 8525 PDA,,289.20", "PR....217,Canon Digital Rebel XT 8MP Digital SLR Camera,,13.70", };
	
	private File tempDir;
    private String targetFileName = "output.txt";
    
    @BeforeEach
    void setUp() throws IOException {
        // Create temporary folder
        tempDir = new File(System.getProperty("java.io.tmpdir"), "test-decompress-" + System.nanoTime());
        FileUtils.forceMkdir(tempDir);
    }
    
    @AfterEach
    void tearDown() throws IOException {
        FileUtils.deleteDirectory(tempDir);
    }

	@Test
	public void execute() throws Exception {
		DecompressTasklet tasklet = new DecompressTasklet();
		tasklet.setInputResource(new ClassPathResource("/input/products.zip"));
		File outputDir = new File("./target/decompresstasklet");
		if (outputDir.exists()) {
			FileUtils.deleteDirectory(outputDir);
		}
		tasklet.setTargetDirectory(outputDir.getAbsolutePath());
		tasklet.setTargetFile("products.txt");

		tasklet.execute(null, null);

		File output = new File(outputDir, "products.txt");
		assertTrue(output.exists());

		assertArrayEquals(EXPECTED_CONTENT, FileUtils.readLines(output).toArray());

	}

	@Test
	public void corruptedArchive() throws Exception {
		DecompressTasklet tasklet = new DecompressTasklet();
		tasklet.setInputResource(new ClassPathResource("/input/products_corrupted.zip"));
		File outputDir = new File("./target/decompresstasklet");
		if (outputDir.exists()) {
			FileUtils.deleteDirectory(outputDir);
		}
		tasklet.setTargetDirectory(outputDir.getAbsolutePath());
		tasklet.setTargetFile("products.txt");

		try {
			tasklet.execute(null, null);
			fail("corrupted archive, the tasklet should have thrown an exception");
		} catch (Exception e) {
			// OK
		}
	}

}
