package shop.zip.travel.global.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다"),
	DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이메일이 중복입니다"),
	DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "닉네임이 중복입니다");

	private final HttpStatus status;
	private final String message;

	ErrorCode(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return this.status;
	}

	public String getMessage() {
		return this.message;
	}

	public int getStatusValue() {
		return this.status.value();
	}

}
