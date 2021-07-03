package com.hardik.bojack.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hardik.bojack.dto.HouseDto;
import com.hardik.bojack.dto.WizardDto;
import com.hardik.bojack.service.MasterHouseService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/house")
@AllArgsConstructor
public class MasterHouseController {

	private final MasterHouseService masterHouseService;

	@GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Returns list of houses")
	public ResponseEntity<List<HouseDto>> masterHouseListRetreivalHandler() {
		return ResponseEntity.ok(masterHouseService.retreiveAll());
	}

	@GetMapping(value = "/{houseId}/wizards/all", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Returns list of wizards of provided house-id")
	public ResponseEntity<List<WizardDto>> wizardsByHouseRetreivalHandler(
			@PathVariable(name = "houseId", required = true) final UUID houseId) {
		return ResponseEntity.ok(masterHouseService.retreiveWizardsByHouseId(houseId));
	}
}
