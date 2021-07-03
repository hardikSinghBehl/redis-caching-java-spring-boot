package com.hardik.bojack.dto;

import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JacksonStdImpl
public class WizardCreationRequestDto {

	private final String firstName;
	private final String middleName;
	private final String lastName;
	private final String gender;
	private final UUID houseId;

}
