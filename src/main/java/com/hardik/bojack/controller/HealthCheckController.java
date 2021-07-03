package com.hardik.bojack.controller;

import java.util.Random;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hardik.bojack.repository.MasterHouseRepository;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/health-check")
@AllArgsConstructor
public class HealthCheckController {

	private final MasterHouseRepository masterHouseRepository;

	@GetMapping(value = "sorting-hat", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Service health check endpoint")
	public ResponseEntity<?> healthCheckResponseHandler() throws JSONException {
		final var response = new JSONObject();
		response.put("House",
				masterHouseRepository.findAll().get(new Random().ints(1, 0, 4).sum()).getName().toUpperCase());

		return ResponseEntity.ok(response.toString());
	}

}
