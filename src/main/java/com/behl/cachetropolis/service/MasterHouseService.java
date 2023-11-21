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

	/**
	 * Retrieves list of all house records stored in the datasource. The house
	 * records are populated using flyway migration script, ensuring that the
	 * response should never be {@code null} or {@code empty}.
	 *
	 * @see <a href="file:src/main/resources/db/migration/V002__adding_data_in_master_houses.sql">Flyway migrations</a>
	 * @return List of {@link HouseDto}.
	 */
	public List<HouseDto> retrieve() {
		return masterHouseRepository.findAll()
				.stream()
				.map(house -> modelMapper.map(house, HouseDto.class))
				.toList();
	}
	
    /**
     * Retrieves details of a random house from the datasource.
     *
     * @return {@link HouseDto} representing the details of a randomly selected house.
     */
	public HouseDto retrieveRandom() {
		final var house = masterHouseRepository.fetchRandom().orElseThrow(IllegalStateException::new);
		return modelMapper.map(house, HouseDto.class);
	}

	/**
	 * Retrieves a list of wizard records associated with the provided house ID.
	 * Post initial datasource retrieval, the response is stored in the provisioned
	 * cache. For all subsequent calls with the same {@code houseId}, the records
	 * are fetched from the cache itself, reducing network calls and improving
	 * latency.
	 *
	 * @param houseId The ID of the house against which wizard records are to be retrieved.
	 * @return List of {@link WizardDto} representing the details of wizards
	 *         associated with the provided house ID.
	 * @throws IllegalArgumentException if houseId is {@code null}
	 * @throws InvalidHouseIdException  if no house or wizard records exist
	 *                                  corresponding to provided house ID.
	 */
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
