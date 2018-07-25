package uk.gov.cshr.scanner.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.io.ByteStreams;
import fi.solita.clamav.ClamAVClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import uk.gov.cshr.status.CSHRServiceStatus;

/**
 * Tests {@link ScanningService}
 */
@Ignore
public class ScanningServiceTest {
    private static final String CLEAN_FILE = "validFile.txt";
    private static final String DIRTY_FILE = "eicar.com";

    private TestClamAVScanningService service;
    private ClamAVClient mockAvClient;

    @Before
    public void setup() {
        mockAvClient = mock(ClamAVClient.class);

        service = new TestClamAVScanningService();
        service.setClient(mockAvClient);
    }

    @After
    public void tearDown() {
        mockAvClient = null;
        service = null;
    }

    @Test
    public void scan_noFileSupplied() {
        CSHRServiceStatus expected = CSHRServiceStatus.builder()
                .code("CSHR_302")
                .summary("No file was found to scan")
                .detail(Collections.emptyList())
                .build();

        assertThat(service.scan(CLEAN_FILE, null), is(equalTo(expected)));
    }

    @Test(expected = RuntimeException.class)
    public void scan_scannerNotWorking() throws IOException {
        byte[] content = loadFileToScan(CLEAN_FILE);
        when(mockAvClient.scan(content)).thenThrow(IOException.class);

        service.scan(CLEAN_FILE, content);
    }

    private byte[] loadFileToScan(final String filename) throws IOException {
        return ByteStreams.toByteArray(ScanningServiceTest.class.getResourceAsStream("/" + filename));
    }

    @Test
    public void scan_fileIsDirty() throws IOException {
        List<String> statusDetail = new ArrayList<>();
        statusDetail.add("FOUND : a virus");

        CSHRServiceStatus expected = CSHRServiceStatus.builder()
                .code("CSHR_301")
                .summary("A file called '" + DIRTY_FILE + "' failed the virus check and cannot be processed any further")
                .detail(statusDetail)
                .build();

        byte[] content = loadFileToScan(DIRTY_FILE);
        when(mockAvClient.scan(content)).thenReturn("FOUND : a virus".getBytes());
        assertThat(service.scan(DIRTY_FILE, content), is(equalTo(expected)));
    }

    @Test
    public void scan_fileIsClean() throws IOException {
        List<String> statusDetail = new ArrayList<>();
        statusDetail.add("OK");

        CSHRServiceStatus expected = CSHRServiceStatus.builder()
                .code("CSHR_300")
                .summary("A file called '" + CLEAN_FILE + "' passed the virus check and is safe for further processing")
                .detail(statusDetail)
                .build();

        byte[] content = loadFileToScan(CLEAN_FILE);
        when(mockAvClient.scan(content)).thenReturn("OK".getBytes());
        assertThat(service.scan(CLEAN_FILE, content), is(equalTo(expected)));
    }
}
