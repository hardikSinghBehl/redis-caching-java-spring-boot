package com.behl.cachetropolis.dto;

import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Schema(title = "Error", accessMode = Schema.AccessMode.READ_ONLY)
public class ExceptionResponseDto<T> {

	private String status;
	private T description;
	
	public void setStatus(@NonNull final HttpStatusCode httpStatusCode) {
		this.status = String.valueOf(httpStatusCode);
	}

}