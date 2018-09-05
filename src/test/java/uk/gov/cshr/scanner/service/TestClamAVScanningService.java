package uk.gov.cshr.scanner.service;

import fi.solita.clamav.ClamAVClient;

/**
 * Test implementation of ClamAVScanningService.
 *
 * The class overrides the getClient() method so that a mock version of ClamAVClient can be inserted into the service.
 */
public class TestClamAVScanningService extends ClamAvService {
    private ClamAVClient mockClient;

    @Override
    protected ClamAVClient getClient() {
        return mockClient;
    }

    // Used to insert a mock version into the service
    void setClient(ClamAVClient client) {
        mockClient = client;
    }
}
