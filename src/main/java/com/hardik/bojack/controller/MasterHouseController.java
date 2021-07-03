package com.hardik.bojack.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hardik.bojack.entity.MasterHouse;
import com.hardik.bojack.repository.MasterHouseRepository;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/house")
@AllArgsConstructor
public class MasterHouseController {

	private final MasterHouseRepository masterHouseRepository;

	@GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Returns list of houses")
	public ResponseEntity<List<MasterHouse>> masterHouseListRetreivalHandler() {
		return ResponseEntity.ok(masterHouseRepository.findAll());
	}
}
