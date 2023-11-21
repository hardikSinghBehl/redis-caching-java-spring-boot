package com.behl.cachetropolis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;

import com.behl.cachetropolis.CacheExtension;
import com.behl.cachetropolis.DataSourceExtension;
import com.behl.cachetropolis.dto.HouseDto;
import com.behl.cachetropolis.dto.WizardDto;
import com.behl.cachetropolis.entity.Wizard;
import com.behl.cachetropolis.exception.InvalidHouseIdException;
import com.behl.cachetropolis.repository.MasterHouseRepository;
import com.behl.cachetropolis.repository.WizardRepository;

import junit.framework.AssertionFailedError;
import net.bytebuddy.utility.RandomString;

@SpringBootTest
@ExtendWith({ DataSourceExtension.class, CacheExtension.class })
class MasterHouseServiceTest {

	@Autowired
	private MasterHouseService masterHouseService;
	
	@SpyBean
	private WizardRepository wizardRepository;

	@SpyBean
	private MasterHouseRepository masterHouseRepository;
	
	@Autowired
	private CacheManager listCacheManager;

	@Test
	void shouldRetrieveWizardsAgainstValidHouseId() {
		// fetch random house record from datasource
		final var house = masterHouseRepository.fetchRandom().orElseThrow(AssertionFailedError::new);
		
		// create wizard record
		final var firstName = RandomString.make();
		final var lastName = RandomString.make();
		final var wizard = new Wizard();
		wizard.setFirstName(firstName);
		wizard.setLastName(lastName);
		wizard.setGender("Male");
		wizard.setHouseId(house.getId());
		wizardRepository.save(wizard);
	
		// invoke method under test
		final List<WizardDto> retrievedWizards = masterHouseService.retrieveWizardsByHouseId(house.getId());
		
		// assert presence of saved wizard in retrieved records and verify datasource interaction
		assertThat(retrievedWizards).isNotNull().isNotEmpty();
		assertThat(retrievedWizards).map(WizardDto::getFirstName).contains(firstName);
		assertThat(retrievedWizards).map(WizardDto::getLastName).contains(lastName);
		verify(wizardRepository, times(1)).findByHouseId(house.getId());
	}
	
	@Test
	void shouldRetrieveWizardRecordsByHouseIdFromCacheAfterInitialDatabaseRetrieval() {
		// fetch random house record from datasource
		final var house = masterHouseRepository.fetchRandom().orElseThrow(AssertionFailedError::new);
		final var houseId = house.getId();

		// create wizard record
		final var firstName = RandomString.make();
		final var lastName = RandomString.make();
		final var wizard = new Wizard();
		wizard.setFirstName(firstName);
		wizard.setLastName(lastName);
		wizard.setGender("Male");
		wizard.setHouseId(houseId);
		wizardRepository.save(wizard);
		
		// assert cache does not contain wizard records corresponding to house-id
		var cachedWizardRecords = listCacheManager.getCache("wizards").get(houseId);
		assertThat(cachedWizardRecords).isNull();
		
		// invoke method under test and assert that initial retrieval is done from datasource
		masterHouseService.retrieveWizardsByHouseId(houseId);
		verify(wizardRepository, times(1)).findByHouseId(houseId);
		Mockito.clearInvocations(wizardRepository);
		
		// invoke method under test multiple times to verify subsequent reads are made from cache and datasource is not queried
		final var queryTimes = 100;
		for (int i = 1; i < queryTimes; i++) {
			masterHouseService.retrieveWizardsByHouseId(houseId);
		}
		verify(wizardRepository, times(0)).findByHouseId(houseId);
	}
		
	@Test
	void shouldThrowExceptionForInvalidHouseId() {
		// prepare invalid house-id
		final var houseId = UUID.randomUUID();
		
		// invoke method under test and verify datasource interaction
		assertThrows(InvalidHouseIdException.class, () -> masterHouseService.retrieveWizardsByHouseId(houseId));
		verify(wizardRepository, times(1)).findByHouseId(houseId);
	}
	
	@Test
	void shouldReturnNonEmptyHouseListPostFlywayMigrationExecution() {
		// Retrieve the list of house records saved in the datasource
        final var retrievedHouses = masterHouseService.retrieve();
        
        // Verify that the list of countries is not empty and flyway migration script is executed
        assertThat(retrievedHouses).isNotNull().isNotEmpty().hasSizeGreaterThanOrEqualTo(4);
        assertThat(retrievedHouses).map(HouseDto::getId).doesNotContainNull();
        assertThat(retrievedHouses).map(HouseDto::getName).doesNotContainNull();
	}
	
	@Test
	void shouldFetchRandomHouseRecordFromDataSource() {
		// invoke method under test
		final var retrievedHouse = masterHouseService.retrieveRandom();

		// assert response attributes
		assertThat(retrievedHouse).isNotNull().isInstanceOf(HouseDto.class)
			.satisfies(house -> {
				assertThat(house.getId()).isNotNull();
				assertThat(house.getName()).isNotNull();
		});
	}

}
