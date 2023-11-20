package com.behl.cachetropolis.dto;

import java.util.UUID;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Schema(title = "WizardCreationSuccessResponse", accessMode = Schema.AccessMode.READ_ONLY)
public class WizardCreationSuccessResponseDto {
	
	private UUID wizardId;

}
