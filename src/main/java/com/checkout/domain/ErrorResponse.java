package com.checkout.domain;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ErrorResponse {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

	private String timestamp;
	private Integer status;
	private String error;
	private String message;
	private String path;
	
	public ErrorResponse(Integer status, String error, String message, String path) {
		super();
		this.setTimestamp(sdf.format(new Timestamp(System.currentTimeMillis())));
		this.setStatus(status);
		this.setError(error);
		this.setMessage(message);
		this.setPath(path);
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
