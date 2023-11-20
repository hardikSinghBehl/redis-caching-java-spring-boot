package com.behl.cachetropolis.service;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.behl.cachetropolis.dto.HouseDto;
import com.behl.cachetropolis.dto.WizardDto;
import com.behl.cachetropolis.exception.InvalidHouseIdException;
import com.behl.cachetropolis.repository.MasterHouseRepository;
import com.behl.cachetropolis.repository.WizardRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MasterHouseService {

	private final ModelMapper modelMapper;
	private final WizardRepository wizardRepository;
	private final MasterHouseRepository masterHouseRepository;

	public List<HouseDto> retrieve() {
		return masterHouseRepository.findAll()
				.stream()
				.map(house -> modelMapper.map(house, HouseDto.class))
				.toList();
	}
	
	public HouseDto retrieveRandom() {
		final var house = masterHouseRepository.fetchRandom().orElseThrow(IllegalStateException::new);
		return modelMapper.map(house, HouseDto.class);
	}

	@Cacheable(value = "wizards", key = "#houseId")
	public List<WizardDto> retrieveWizardsByHouseId(@NonNull final UUID houseId) {
		final var wizards = wizardRepository.findByHouseId(houseId);
		if (wizards.isEmpty()) {
			throw new InvalidHouseIdException("Invalid house ID or no wizards found for the specified house.");
		}
		return wizards.stream()
				.map(wizard -> modelMapper.map(wizard, WizardDto.class))
				.toList();
	}

}
