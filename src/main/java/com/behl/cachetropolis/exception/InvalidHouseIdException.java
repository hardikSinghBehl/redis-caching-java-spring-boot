package com.behl.cachetropolis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.NonNull;

public class InvalidHouseIdException extends ResponseStatusException {

	private static final long serialVersionUID = -4587327576098330280L;

	private static final String DEFAULT_MESSAGE = "No house record found corresponding to provided Id.";

	public InvalidHouseIdException() {
		super(HttpStatus.NOT_FOUND, DEFAULT_MESSAGE);
	}

	public InvalidHouseIdException(@NonNull String message) {
		super(HttpStatus.NOT_FOUND, message);
	}
}
