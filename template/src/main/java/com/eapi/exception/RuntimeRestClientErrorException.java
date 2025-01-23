package com.eapi.exception;

import com.eapi.response.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RuntimeRestClientErrorException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private ErrorResponse errorResponse;
}
