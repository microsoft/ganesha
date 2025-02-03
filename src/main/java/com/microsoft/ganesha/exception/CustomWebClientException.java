package com.microsoft.ganesha.exception;

import com.microsoft.ganesha.response.ErrorResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CustomWebClientException thrown for unknown application exception in catch block
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomWebClientException extends RuntimeException {

    private static final long serialVersionUID = 5790475272220783214L;

    private ErrorResponse errorResponse;
}