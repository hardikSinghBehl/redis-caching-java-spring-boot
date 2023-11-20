package com.behl.cachetropolis.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.behl.cachetropolis.dto.HouseDto;
import com.behl.cachetropolis.service.MasterHouseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/health-check")
@Tag(name = "Health Check", description = "Endpoint for performing health checks")
public class SortingHatController {

	private final MasterHouseService masterHouseService;

	@GetMapping(value = "/sorting-hat", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Performs house assignment spell", description = "Performs house assignment spell to randomly assign a house.")
	@ApiResponse(responseCode = "200", description = "House assignment spell performed successfully.")
	public ResponseEntity<HouseDto> performHouseAssignmentSpell() {
		final var response = masterHouseService.retrieveRandom();
		return ResponseEntity.ok(response);
	}

}
