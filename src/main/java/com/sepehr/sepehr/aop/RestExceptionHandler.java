package com.sepehr.sepehr.aop;


import com.sepehr.sepehr.common.dto.CommonResponseDto;
import com.sepehr.sepehr.common.exceptions.ErrorCodes;
import com.sepehr.sepehr.common.exceptions.ServiceException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<CommonResponseDto> exception(Exception exception, WebRequest request) {

        CommonResponseDto response = new CommonResponseDto();
        response.setResult(CommonResponseDto.RESULT.FAILURE);
//        response.setData(exception.getMessage());
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        exception.printStackTrace();

        if (exception instanceof HttpMessageNotReadableException) {
            response.setData(exception.getMessage());
            response.setErrorCode(com.sepehr.sepehr.common.exceptions.ErrorCodes.BAD_INPUT.getCode());

        } else if (exception instanceof ServiceException) {
            int ErrorCode = ((ServiceException) exception).getErrorCode();

            response.setData(exception.getMessage());
            response.setErrorCode(ErrorCode);


            if (ErrorCode == ErrorCodes.ACCESS_DENIED.getCode()
                    || ErrorCode == ErrorCodes.INACTIVE_USER.getCode()
                    || ErrorCode == ErrorCodes.NO_USER.getCode()) {

                status = HttpStatus.FORBIDDEN;

            } else if (ErrorCode == ErrorCodes.NO_ENTITY.getCode()) {
                status = HttpStatus.NOT_FOUND;
            }

        } else if (exception instanceof AccessDeniedException) {
            response.setData("Access denied");
            response.setErrorCode(ErrorCodes.ACCESS_DENIED.getCode());
            status = HttpStatus.FORBIDDEN;

        } else if (exception instanceof org.springframework.web.bind.MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) exception).getBindingResult();
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> message = new ArrayList<>();
            for (FieldError e : errors) {
                message.add("@" + e.getField().toUpperCase() + ":" + e.getDefaultMessage());
            }
            response.setData("input data is not valid. " + message);
            response.setErrorCode(ErrorCodes.VALIDATION_ERROR.getCode());
            status = HttpStatus.BAD_REQUEST;

        } else if (exception instanceof org.springframework.dao.DataIntegrityViolationException) {
            response.setData("input data integrity is not correct, may be some essential data does not entered.");
            response.setErrorCode(ErrorCodes.BAD_INPUT.getCode());
            status = HttpStatus.BAD_REQUEST;

        } else if (exception instanceof MissingServletRequestParameterException) {
            String error = ((MissingServletRequestParameterException) exception).getParameterName() + " parameter is missing";
            response.setData(error);
            response.setErrorCode(ErrorCodes.MISSING_PARAM.getCode());
            status = HttpStatus.BAD_REQUEST;

        }
        else if (exception instanceof HttpRequestMethodNotSupportedException) {
            response.setData("Method not allow");
            response.setErrorCode(ErrorCodes.Method_Not_Allowed.getCode());
            status = HttpStatus.METHOD_NOT_ALLOWED;

        }
        else if (exception instanceof org.springframework.orm.jpa.JpaSystemException) {
            response.setData("error that do not match any concrete dao exception");
            response.setErrorCode(ErrorCodes.Jpa_System.getCode());
            status = HttpStatus.BAD_REQUEST;

        }
        else if (exception instanceof IOException) {
            response.setData("IOException handler executed");
            response.setErrorCode(ErrorCodes.IOException.getCode());
            status = HttpStatus.NOT_FOUND;

        }

        else if (exception instanceof InvalidDataAccessResourceUsageException) {
            response.setData("SQLException handler executed");
            response.setErrorCode(ErrorCodes.SQLEXCEPTION.getCode());
            status = HttpStatus.BAD_REQUEST;

        }
        else if (exception instanceof NullPointerException) {
            response.setData("object property is empty");
            response.setErrorCode(ErrorCodes.NULL_MESSAGE.getCode());
            status = HttpStatus.BAD_REQUEST;

        }
        else if (exception instanceof RuntimeException) {
            response.setData("Internal error");
            response.setErrorCode(ErrorCodes.INTERNAL_ERROR.getCode());
            status = HttpStatus.INTERNAL_SERVER_ERROR;

        }
        return new ResponseEntity<>(response, status);
    }

}