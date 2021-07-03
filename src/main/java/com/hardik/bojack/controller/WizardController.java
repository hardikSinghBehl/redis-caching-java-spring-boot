package com.hardik.bojack.controller;

import java.util.UUID;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hardik.bojack.dto.WizardCreationRequestDto;
import com.hardik.bojack.dto.WizardDto;
import com.hardik.bojack.service.WizardService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/wizards")
@AllArgsConstructor
public class WizardController {

	private final WizardService wizardService;

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Creates a wizard record in the system")
	public ResponseEntity<?> wizardCreationRequestHandler(
			@RequestBody(required = true) final WizardCreationRequestDto wizardCreationRequestDto)
			throws JSONException {
		final var response = new JSONObject();
		response.put("Wizard-ID", wizardService.create(wizardCreationRequestDto));
		return ResponseEntity.ok(response.toString());

	}

	@PutMapping(value = "/{wizardId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Updates a wizard record in the system")
	public ResponseEntity<?> wizardUpdationRequestHandler(
			@PathVariable(name = "wizardId", required = true) final UUID wizardId,
			@RequestBody(required = true) final WizardCreationRequestDto wizardCreationRequestDto)
			throws JSONException {
		final var response = new JSONObject();
		response.put("Wizard-ID", wizardService.update(wizardId, wizardCreationRequestDto));
		return ResponseEntity.ok(response.toString());
	}

	@GetMapping(value = "/{wizardId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Returns a wizard record in the system")
	public ResponseEntity<WizardDto> wizardRetrievalRequestHandler(
			@PathVariable(name = "wizardId", required = true) final UUID wizardId) {
		return ResponseEntity.ok(wizardService.retreiveById(wizardId));
	}

	@DeleteMapping(value = "/{wizardId}")
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Deletes a wizard record in the system")
	public ResponseEntity<?> wizardDeletionHandler(
			@PathVariable(name = "wizardId", required = true) final UUID wizardId) {
		wizardService.delete(wizardId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
