package com.behl.cachetropolis.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.behl.cachetropolis.dto.ExceptionResponseDto;
import com.behl.cachetropolis.dto.HouseDto;
import com.behl.cachetropolis.dto.WizardDto;
import com.behl.cachetropolis.service.MasterHouseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/houses")
@Tag(name = "Houses Registry", description = "Endpoints for retrieving house information")
public class MasterHouseController {

	private final MasterHouseService masterHouseService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Retrieves all house records", description = "Retrieves details of all houses.")
	@ApiResponse(responseCode = "200", description = "House records retrieved successfully.")
	public ResponseEntity<List<HouseDto>> retrieveAll() {
		final var houses = masterHouseService.retrieve();
		return ResponseEntity.ok(houses);
	}

	@GetMapping(value = "/{houseId}/wizards", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Retrieves wizard records by house", description = "Retrieves wizards corresponding to the specified houseId.")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Wizard records retrieved successfully."),
			@ApiResponse(responseCode = "404", description = "Invalid HouseId provided.",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))) })
	public ResponseEntity<List<WizardDto>> retrieveWizardsByHouse(@PathVariable final UUID houseId) {
		final var wizards = masterHouseService.retrieveWizardsByHouseId(houseId);
		return ResponseEntity.ok(wizards);
	}

}
