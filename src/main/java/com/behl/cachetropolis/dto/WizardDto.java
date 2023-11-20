package com.behl.cachetropolis.dto;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Schema(title = "Wizard", accessMode = Schema.AccessMode.READ_ONLY)
public class WizardDto implements Serializable {

	private static final long serialVersionUID = -248856302229246668L;
	
	private UUID id;
	private String firstName;
	private String lastName;
	private String gender;
	private String wandType;
	private String quidditchPosition;
	private String bloodStatus;
	private String patronus;
	private UUID houseId;

}
