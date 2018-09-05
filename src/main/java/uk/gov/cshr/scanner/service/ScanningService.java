package uk.gov.cshr.scanner.service;

import uk.gov.cshr.status.CSHRServiceStatus;

/**
 * Defines methods to be implemented by concrete scanning service implementations.
 */
public interface ScanningService {
    /**
     * This method is responsible for scanning content from a file as an input stream.  The method calls an external
     * scanning facility to perform the actual scan.
     *
     * @param filename the name of the file being scanned
     * @param content the content of the file to be scanned
     * @return CSHRServiceStatus status of the request to scan the file.
     */
    CSHRServiceStatus scan(final String filename, byte[] content);
}
