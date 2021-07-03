package com.hardik.bojack.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JacksonStdImpl
public class WizardDto implements Serializable {

	private static final long serialVersionUID = 5551973292505478937L;
	private final UUID id;
	private final String firstName;
	private final String middleName;
	private final String lastName;
	private final String gender;
	private final UUID houseId;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;

}
