package com.behl.cachetropolis.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.behl.cachetropolis.DataSourceExtension;
import com.behl.cachetropolis.entity.MasterHouse;

import junit.framework.AssertionFailedError;

@DataJpaTest
@ExtendWith(DataSourceExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MasterHouseRepositoryTest {

	@Autowired
	private MasterHouseRepository masterHouseRepository;

	@Test
	void shouldFetchRandomHouseRecordFromDataSource() {
		// Fetch random house record from data source
		Optional<MasterHouse> masterHouse = masterHouseRepository.fetchRandom();

		// assert fetched record's attributes
		assertThat(masterHouse).isPresent().get().satisfies(house -> {
			assertThat(house.getId()).isNotNull();
			assertThat(house.getName()).isNotNull();
			assertThat(house.getCreatedAt()).isNotNull();
			assertThat(house.getUpdatedAt()).isNotNull();
		});
	}

	@Test
	void shouldNotFetchSameHouseRecordForEachInvocation() {
		// Retrieve IDs of all houses present in the data source
		final List<UUID> houseIds = masterHouseRepository.findAll().stream().map(MasterHouse::getId).toList();
		assertThat(houseIds).isNotEmpty().hasSizeGreaterThan(1);

		// invoke method under test multiple times
		final var batchSize = 100;
		final List<MasterHouse> masterHouses = new ArrayList<MasterHouse>();
		for (int i = 0; i < batchSize; i++) {
			final var masterHouse = masterHouseRepository.fetchRandom().orElseThrow(AssertionFailedError::new);
			masterHouses.add(masterHouse);
		}

		// Ensure randomly fetched houses contains all house-ids present in datasource
		final var totalHouses = houseIds.size();
		List<UUID> uniqueHouseIds = masterHouses.stream().map(MasterHouse::getId).distinct().toList();
		assertThat(uniqueHouseIds).hasSize(totalHouses).containsAll(houseIds);
	}

}
