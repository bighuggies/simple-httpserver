package me.atshughson.softeng325.a01.httpserver;

public enum HTTPStatusCode {
	OK ("200 OK"),
	BAD_REQUEST ("400 Bad Request"),
	NOT_FOUND ("404 Not found"),
	UNSUPPORTED_MEDIA_TYPE ("415 Unsupported Media Type"),
	NOT_IMPLEMENTED ("501 Not Implemented"),
	;
	
	private final String text;
	
	HTTPStatusCode(String text) {
		this.text = text;
    }

    public String toString() {
		return text;
	}
}
