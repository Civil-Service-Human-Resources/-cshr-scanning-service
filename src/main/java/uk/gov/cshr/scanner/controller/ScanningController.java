package uk.gov.cshr.scanner.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.google.common.io.ByteStreams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.cshr.exception.CSHRServiceException;
import uk.gov.cshr.scanner.service.ScanningService;
import uk.gov.cshr.status.CSHRServiceStatus;
import uk.gov.cshr.status.StatusCode;

@Controller
@Slf4j
public class ScanningController implements ScanningApi {
    @Inject
    ScanningService scanningService;

    @Override
    public ResponseEntity<CSHRServiceStatus> scan(String filename, MultipartFile file) {
        log.info("Starting to scan a file called " + filename);

        CSHRServiceStatus status;

        try {
            status = scanningService.scan(filename, ByteStreams.toByteArray(file.getInputStream()));
        } catch (NullPointerException e) {
            List<String> detail = new ArrayList<>();
            detail.add(e.getMessage());

            throw new CSHRServiceException(CSHRServiceStatus.builder()
                    .code(StatusCode.FILE_NOT_SCANNED.getCode())
                    .summary("A file called '" + filename + "' was requested to be scanned but no file was found.")
                    .detail(detail)
                    .build());
        } catch (IOException e) {
            List<String> detail = new ArrayList<>();
            detail.add(e.getMessage());

            throw new CSHRServiceException(CSHRServiceStatus.builder()
                    .code(StatusCode.FILE_NOT_SCANNED.getCode())
                    .summary("A file called '" + filename + "' was requested to be scanned and an unexpected error was encountered.")
                    .detail(detail)
                    .build());
        }

        log.info("Scan of file called " + filename + " is complete and the result is: " + status.getSummary());

        return ResponseEntity.ok(status);
    }
}
