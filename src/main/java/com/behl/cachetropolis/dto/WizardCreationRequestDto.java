package com.behl.cachetropolis.dto;

import java.util.UUID;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Schema(title = "WizardCreationRequest", accessMode = Schema.AccessMode.WRITE_ONLY)
public class WizardCreationRequestDto {

	@Size(max = 50)
	@NotBlank(message = "FirstName must not be empty")
	@Schema(requiredMode = RequiredMode.REQUIRED, example = "Hardik")
	private String firstName;
	
	@Size(max = 50)
	@Schema(requiredMode = RequiredMode.NOT_REQUIRED, example = "Behl")
	private String lastName;
	
	@NotBlank(message = "Gender must not be empty")
	@Schema(requiredMode = RequiredMode.REQUIRED, example = "Male")
    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be either Male, Female, or Other")
	private String gender;
	
	@NotNull(message = "HouseId must not be empty")
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private UUID houseId;

}
