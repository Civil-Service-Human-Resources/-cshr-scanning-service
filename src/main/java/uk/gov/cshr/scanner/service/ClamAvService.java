package uk.gov.cshr.scanner.service;

import java.util.ArrayList;
import java.util.List;

import fi.solita.clamav.ClamAVClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.cshr.status.CSHRServiceStatus;
import uk.gov.cshr.status.StatusCode;

@Service
@Slf4j
public class ClamAvService implements ScanningService {
    @Value("${av.service.hostname}")
    private String hostname;

    @Value("${av.service.port}")
    private int port;

    @Value("${av.service.timeout}")
    private int timeout;

    @Override
    public CSHRServiceStatus scan(final String filename, final byte[] content) {
        StatusCode code;
        String summary;
        List<String> detail = new ArrayList<>();

        if (content != null) {
            log.debug("Obtaining instance of clamav client");
            ClamAVClient avClient = getClient();
            byte[] response;

            try {
                response = avClient.scan(content);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }

            log.debug("scan complete and testing the outcome of the resopnse");
            if (ClamAVClient.isCleanReply(response)) {
                log.debug(filename + " is clean");
                code = StatusCode.FILE_IS_CLEAN;
                summary = "A file called '"
                        + filename
                        + "' passed the virus check and is safe for further processing";
            } else {
                log.debug(filename + " is dirty");
                code = StatusCode.FILE_IS_DIRTY;
                summary = "A file called '"
                        + filename
                        + "' failed the virus check and cannot be processed any further";
            }

            detail.add(new  String(response));

        } else {
            code = StatusCode.FILE_NOT_SCANNED;
            summary = "No file was found to scan";
        }

        return CSHRServiceStatus.builder().code(code.getCode()).summary(summary).detail(detail).build();
    }

    protected ClamAVClient getClient() {
        return new ClamAVClient(hostname, port, timeout);
    }
}
