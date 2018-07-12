package uk.gov.cshr.scanner;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.cshr.scanner.controller.ScanningApi;
import uk.gov.cshr.status.CSHRServiceStatus;

@Controller
public class ScanningController implements ScanningApi {
    @Override
    public ResponseEntity<CSHRServiceStatus> scan(String filename, MultipartFile file) {
        return null;
    }
}
