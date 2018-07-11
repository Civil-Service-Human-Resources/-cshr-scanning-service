package uk.gov.cshr.scanner.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.cshr.status.CSHRServiceStatus;

/** Defines the REST end points available in this service. */
@RequestMapping(value = "/scanner", produces = MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
@Api(value = "scanner")
public interface ScanningApi {
    /**
     * This method is responsible for getting the incoming file scanned for viruses and report the
     * result of the scan.
     *
     * An Http Status of 200 will be returned for all successful requests to scan a file regardless of whether or not a
     * file itself has or has no virus. Check the detail of the response body for further information - a status code of
     * CSHR_300 means the file was clean and not found to contain a known virus or malware.  A status code of CSHR_301
     * means the file was found to contain a known virus or malware.
     *
     * @param filename the name of the file to be scanned
     * @param file the file itself to be scanned
     * @return overall status of the request for the file to be scanned
     */
    @RequestMapping(method = RequestMethod.POST, value = "/scan")
    @ApiOperation(value = "scan file",
            nickname = "scan",
            notes = "An Http Status of 200 will be returned for all successful requests to scan a file regardless of " +
                    "whether or not a file itself has or has no virus. Check the detail of the response body for " +
                    "further information - a status code of CSHR_300 means the file was clean and not found to " +
                    "contain a known virus or malware.  A status code of CSHR_301 means the file was found to " +
                    "contain a known virus or malware.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "Request to scan a file was processed successfully",
                            response = CSHRServiceStatus.class),
                    @ApiResponse(
                            code = 400,
                            message = "The request was invalid.",
                            response = CSHRServiceStatus.class
                    ),
                    @ApiResponse(
                            code = 401,
                            message = "You are not authorised to use this service. Please supply the correct " +
                                    "credentials or contact the system administrator if you believe they are " +
                                    "correct.",
                            response = CSHRServiceStatus.class
                    ),
                    @ApiResponse(
                            code = 500,
                            message =
                                    "An unexpected error occurred processing your request. Please contact the system " +
                                            "administrator.",
                            response = CSHRServiceStatus.class
                    ),
                    @ApiResponse(
                            code = 503,
                            message =
                                    "The service is currently unavailable and your request cannot be processed at " +
                                            "this time. This may be a temporary condition and if it persists please " +
                                            "contact the system administrator",
                            response = CSHRServiceStatus.class
                    )
                }
            )
    ResponseEntity<CSHRServiceStatus> scan(@ApiParam(name = "filename", required = true, value = "set of login parameters in JSON format", example = "myFile.txt")
                                           @RequestParam("filename") String filename,
                                           @ApiParam(name = "file", required = true, value = "MultipartFile object representing the actual file itself")
                                           @RequestParam("file") MultipartFile file);
}
