package shop.zip.travel.global.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import shop.zip.travel.global.error.exception.CustomNotFoundException;
import shop.zip.travel.global.error.exception.DuplicatedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
		log.info("IllegalArgumentException : ", e);
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(CustomNotFoundException.class)
	public ResponseEntity<String> handleCustomNotFoundException(CustomNotFoundException e) {
		log.info("CustomNotFoundException : ", e);
		return ResponseEntity.status(e.getErrorCode().getStatusValue()).body(e.getMessage());
	}

	@ExceptionHandler(DuplicatedException.class)
	public ResponseEntity<String> handleDuplicatedException(DuplicatedException e) {
		log.info("DuplicatedException : ", e);
		return ResponseEntity.status(e.getErrorCode().getStatusValue()).body(e.getMessage());
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
		log.error("RuntimeException : ", e);
		return ResponseEntity.internalServerError().body(e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception e) {
		log.error("Exception : ", e);
		return ResponseEntity.internalServerError().body(e.getMessage());
	}

}
