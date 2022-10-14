package com.kyobo.platform.recipe.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {
	private static final long serialVersionUID = 1L;

	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ResponseEntity<?> handleConflict(Exception e) {
		System.out.println("====================================================");
		System.out.println(HttpStatus.INTERNAL_SERVER_ERROR);
		System.out.println(e.getMessage());
		System.out.println("====================================================");
		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
	}
}
