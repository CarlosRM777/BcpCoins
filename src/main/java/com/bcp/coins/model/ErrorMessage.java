package com.bcp.coins.model;

public class ErrorMessage {
	private String message;
	private String path;
	private String exception;
	public ErrorMessage(Exception exception, String path) {
		this.message = exception.getMessage();
		this.path = path;
		this.exception = exception.getClass().getSimpleName();
		// TODO Auto-generated constructor stub
	}
	public String getMessage() {
		return message;
	}
	public String getException() {
		return exception;
	}
	public String getPath() {
		return path;
	}
	
	@Override
	public String toString() {
		return 	"ErrorMessage {" + 
				"exception='"+ exception + "', " + 
				"message='" + message + "', " + 
				"path='" + path + 
				"}";
	}


}
