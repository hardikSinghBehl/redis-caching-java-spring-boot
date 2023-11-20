package com.behl.cachetropolis.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.behl.cachetropolis.dto.ExceptionResponseDto;
import com.behl.cachetropolis.dto.WizardCreationRequestDto;
import com.behl.cachetropolis.dto.WizardCreationSuccessResponseDto;
import com.behl.cachetropolis.dto.WizardDto;
import com.behl.cachetropolis.dto.WizardUpdationRequestDto;
import com.behl.cachetropolis.service.WizardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/wizards")
@Tag(name = "Wizards Management", description = "Endpoints for managing wizard records")
public class WizardController {

	private final WizardService wizardService;

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Creates a wizard record", description = "Registers a unique wizard record in the system corresponding to the provided information.")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Wizard record created successfully."),
			@ApiResponse(responseCode = "404", description = "Invalid HouseId provided.",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "Invalid request payload.",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))) })
	public ResponseEntity<WizardCreationSuccessResponseDto> create(
			@Valid @RequestBody final WizardCreationRequestDto wizardCreationRequest) {
		final var wizardId = wizardService.create(wizardCreationRequest);
		final var response = new WizardCreationSuccessResponseDto();
		response.setWizardId(wizardId);
		return ResponseEntity.ok(response);
	}

	@PatchMapping(value = "/{wizardId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Updates wizard details", description = "Updates details corresponding to the specified wizard.")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Wizard details updated successfully.",
					content = @Content(schema = @Schema(implementation = Void.class))),
			@ApiResponse(responseCode = "404", description = "Invalid WizardId provided.",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "Invalid request payload.",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))) })
	public ResponseEntity<HttpStatus> update(@PathVariable final UUID wizardId,
			@RequestBody final WizardUpdationRequestDto WizardUpdationRequest) {
		wizardService.update(wizardId, WizardUpdationRequest);
		return ResponseEntity.ok().build();
	}

	@GetMapping(value = "/{wizardId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Retrieves wizard details", description = "Retrieves details corresponding to the specified wizard.")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Wizard details retrieved successfully."),
			@ApiResponse(responseCode = "404", description = "Invalid WizardId provided.",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))) })
	public ResponseEntity<WizardDto> retrieve(@PathVariable final UUID wizardId) {
		final var response = wizardService.retrieveById(wizardId);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping(value = "/{wizardId}")
	@Operation(summary = "Deletes wizard record", description = "Deletes wizard record corresponding to the specified wizardId.")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "204", description = "Wizard deleted successfully.",
					content = @Content(schema = @Schema(implementation = Void.class))),
			@ApiResponse(responseCode = "404", description = "Invalid WizardId provided.",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))) })
	public ResponseEntity<HttpStatus> delete(@PathVariable final UUID wizardId) {
		wizardService.delete(wizardId);
		return ResponseEntity.noContent().build();
	}
	
}
