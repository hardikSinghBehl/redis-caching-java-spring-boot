package com.behl.cachetropolis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidWizardIdException extends ResponseStatusException {

	private static final long serialVersionUID = -8610744043097731319L;
	
	private static final String DEFAULT_MESSAGE = "No wizard record found corresponding to provided Id.";

	public InvalidWizardIdException() {
		super(HttpStatus.NOT_FOUND, DEFAULT_MESSAGE);
	}

}