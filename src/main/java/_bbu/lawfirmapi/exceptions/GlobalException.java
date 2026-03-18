package  _bbu.lawfirmapi.exceptions;


import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.Map;


import _bbu.lawfirmapi.models.DTO.shared.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
//import org.springframework.web.server.ResponseStatusException;


@RestControllerAdvice
public class GlobalException extends BaseResponse {
    // private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> methodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        Map<String, String> error = new HashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            error.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return problemDetailResponseEntity(error);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ProblemDetail> handlerMethodValidationException(
            HandlerMethodValidationException e) {
        Map<String, String> errors = new HashMap<>();
        // Loop through each invalid parameter validation result
        e.getParameterValidationResults().forEach(parameterError -> {
            String paramName = parameterError.getMethodParameter().getParameterName();

            // Loop through each validation error message for this parameter
            for (var messageError : parameterError.getResolvableErrors()) {
                errors.put(paramName, messageError.getDefaultMessage()); // Store error message
            }
        });

        // Create structured ProblemDetail response
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Method Parameter Validation Failed");
        problemDetail.setProperties(Map.of("timestamp", LocalDateTime.now(), "errors", errors));
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
        return problemDetailResponseEntityCustom(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", 400);
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String , Object>> handleResponseStatus(
            ResponseStatusException ex) {
       Map<String , Object> body = new HashMap<>();
       body.put("status" , 409);
       body.put("error" , "Conflict Request");
       body.put("message" , ex.getMessage());
       body.put("success" , false);

       return new ResponseEntity<>(body , HttpStatus.CONFLICT);
    }


//    @ExceptionHandler(com.kshrd.lumnov.exception.WrongInputException.class)
//    public ResponseEntity<?> wrongInputException(com.kshrd.lumnov.exception.WrongInputException e) {
//        return problemDetailResponseEntityCustom(e.getMessage(), HttpStatus.NOT_FOUND);
//    }

//    @ExceptionHandler(com.kshrd.lumnov.exception.EmailAlreadyExistException.class)
//    public ResponseEntity<?> handleEmailAlreadyExistException(com.kshrd.lumnov.exception.EmailAlreadyExistException e) {
//        return problemDetailResponseEntityCustom(e.getMessage(), HttpStatus.BAD_REQUEST);
//    }

//    @ExceptionHandler(ExpireOTPCodeException.class)
//    public ResponseEntity<?> handleExpireOTPCodeException(ExpireOTPCodeException e) {
//        return problemDetailResponseEntityCustom(e.getMessage(), HttpStatus.BAD_REQUEST);
//    }

//    @ExceptionHandler(EmailNotVerifiedException.class)
//    public ResponseEntity<?> handleEmailNotRegisterException(EmailNotVerifiedException e) {
//        return problemDetailResponseEntityCustom(e.getMessage(), HttpStatus.UNAUTHORIZED);
//    }

    @ExceptionHandler(InvalidException.class)
    public ResponseEntity<?> handleInvalidException(InvalidException e) {
        return problemDetailResponseEntityCustom(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<?> handleInvalidDate(InvalidDateException e) {
        return problemDetailResponseEntityCustom(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpireOTPCodeException.class)
    public ResponseEntity<?> handleExpiredOTP(ExpireOTPCodeException e) {
        return problemDetailResponseEntityCustom(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // @ExceptionHandler(SQLException.class)
    // public ResponseEntity<?> handleSQLException(SQLException e) {
    // return problemDetailResponseEntityCustom(e.getMessage(),
    // HttpStatus.BAD_REQUEST);
    // }

//    @ExceptionHandler(com.kshrd.lumnov.exception.BadRequestException.class)
//    public ResponseEntity<?> handleBadRequestException(com.kshrd.lumnov.exception.BadRequestException e) {
//        return problemDetailResponseEntityCustom(e.getMessage(), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(com.kshrd.lumnov.exception.ForbiddenException.class)
//    public ResponseEntity<?> handleAccessDeniedException(com.kshrd.lumnov.exception.ForbiddenException e) {
//        return problemDetailResponseEntityCustom(e.getMessage(), HttpStatus.FORBIDDEN);
//    }
//
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<?> handleInvalidJson(HttpMessageNotReadableException ex) {
//
//        String msg = ex.getMostSpecificCause().getMessage().toLowerCase();
//
//        // Invalid gender enum
//        if (msg.contains("gender")) {
//            return problemDetailResponseEntityCustom(
//                    "Invalid gender. Accepted values: MALE, FEMALE.", HttpStatus.BAD_REQUEST);
//        }
//
//        // Add this for invalid LocalDate:
//        if (msg.contains("invalid date format or value")) {
//            return problemDetailResponseEntityCustom(
//                    "Invalid date format: " + ex.getMostSpecificCause().getMessage(),
//                    HttpStatus.BAD_REQUEST);
//        }
//
//        if (msg.contains(
//                "accepted for enum class: [booked, available, under_maintenance, rented]")) {
//            return problemDetailResponseEntityCustom(
//                    "Invalid room status. Only accept " + Arrays.toString(RoomStatus.values()),
//                    HttpStatus.BAD_REQUEST);
//        }
//
//        return problemDetailResponseEntityCustom(msg, HttpStatus.BAD_REQUEST);
//    }
//
//    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);
//
//    // Handle all unhandled exceptions
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleAllUnhandledExceptions(Exception e) {
//        logger.error("Unhandled exception occurred", e);
//        StackTraceElement[] stack = e.getStackTrace();
//        String errorMsg = "";
//        if (stack.length > 0) {
//            StackTraceElement origin = stack[0];
//            errorMsg = e.getClass().getSimpleName() + " at " + origin.getClassName() + "."
//                    + origin.getMethodName() + "(" + origin.getFileName() + ":"
//                    + origin.getLineNumber() + ")";
//        } else {
//            errorMsg = e.getClass().getSimpleName();
//        }
//
//        return problemDetailResponseEntityCustom(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
