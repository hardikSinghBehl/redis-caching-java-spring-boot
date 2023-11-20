package com.behl.cachetropolis.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Schema(title = "WizardUpdationRequest", accessMode = Schema.AccessMode.WRITE_ONLY)
public class WizardUpdationRequestDto {
	
	@Size(max = 50)
	@Schema(requiredMode = RequiredMode.NOT_REQUIRED, example = "Behl")
	private String lastName;
	
	@Pattern(regexp = "^(Dragon heartstring|Phoenix feather|Unicorn tail hair)$", message = "Invalid WandType")
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "Dragon heartstring", allowableValues = { "Dragon heartstring", "Phoenix feather", "Unicorn tail hair" })
	private String wandType;
	
    @Pattern(regexp = "^(Chaser|Beater|Keeper|Seeker)$", message = "Invalid QuidditchPosition")
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "Chaser", allowableValues = { "Chaser", "Beater", "Keeper", "Seeker" })
	private String quidditchPosition;
	
	@Pattern(regexp = "^(Muggle|Half blood|Pure Blood|Squib|Half breed)$", message = "Invalid BloodStatus")
	@Schema(requiredMode = RequiredMode.NOT_REQUIRED, example = "Muggle", allowableValues = { "Muggle", "Half blood", "Pure Blood", "Squib", "Half breed" })
	private String bloodStatus;
	
	@Size(max = 20)
	@Schema(requiredMode = RequiredMode.NOT_REQUIRED, example = "Phoenix")
	private String patronus;

}
