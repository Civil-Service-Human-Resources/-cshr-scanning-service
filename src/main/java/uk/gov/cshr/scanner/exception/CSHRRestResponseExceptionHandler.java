package uk.gov.cshr.scanner.exception;

import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uk.gov.cshr.exception.CSHRServiceException;
import uk.gov.cshr.status.CSHRServiceStatus;
import uk.gov.cshr.status.StatusCode;

/**
 * This class provides global exception handling capability for this microservice
 */
@ControllerAdvice
@Slf4j
public class CSHRRestResponseExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Handle exceptions that represent internal server errors
     *
     * @param ex exception that was raised
     * @return CSHRServiceStatus details of the error
     */
    @ExceptionHandler({CSHRServiceException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CSHRServiceStatus handleException(CSHRServiceException ex) {
        log.error(ex.getMessage(), ex);

        return ex.getCshrServiceStatus();
    }

    /**
     * Handle Exception.
     * <p>
     * <p>These are raised for any any unexpected errors.
     *
     * @param ex exception that was raised
     * @return CSHRServiceStatus details of the error
     */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CSHRServiceStatus handleInvalidPathException(InvalidPathException ex) {
        List<String> detail = new ArrayList<>();
        detail.add(ex.getMessage());

        CSHRServiceStatus status =
                CSHRServiceStatus.builder()
                        .code(StatusCode.INTERNAL_SERVICE_ERROR.toString())
                        .summary("An unexpected error occurred trying to scan a file.")
                        .detail(detail)
                        .build();
        log.error(ex.getMessage(), ex);

        return status;
    }
}
