package com.behl.cachetropolis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;

import com.behl.cachetropolis.RedisCacheInitializer;
import com.behl.cachetropolis.MySQLDataSourceInitializer;
import com.behl.cachetropolis.dto.WizardCreationRequestDto;
import com.behl.cachetropolis.dto.WizardDto;
import com.behl.cachetropolis.dto.WizardUpdationRequestDto;
import com.behl.cachetropolis.entity.Wizard;
import com.behl.cachetropolis.exception.InvalidHouseIdException;
import com.behl.cachetropolis.exception.InvalidWizardIdException;
import com.behl.cachetropolis.repository.MasterHouseRepository;
import com.behl.cachetropolis.repository.WizardRepository;

import junit.framework.AssertionFailedError;
import net.bytebuddy.utility.RandomString;

@SpringBootTest
@ExtendWith({ MySQLDataSourceInitializer.class, RedisCacheInitializer.class })
class WizardServiceTest {
	
	@Autowired
	private MasterHouseRepository masterHouseRepository;
	
	@Autowired
	private WizardService wizardService;
	
	@Autowired
	private MasterHouseService masterHouseService;
	
	@SpyBean
	private WizardRepository wizardRepository;
	
	@Autowired
	private CacheManager listCacheManager;
	
	@Autowired
	private CacheManager objectCacheManager;
	
	@Test
	void shouldCreateWizardRecordForValidHouseId() {
		// fetch random house record from datasource
		final var house = masterHouseRepository.fetchRandom().orElseThrow(AssertionFailedError::new);
		
		// populate cache with wizards by house-id
		final var cachedWizardDto = new WizardDto();
		cachedWizardDto.setId(UUID.randomUUID());
		cachedWizardDto.setFirstName(RandomString.make());
		listCacheManager.getCache("wizards").put(house.getId(), List.of(cachedWizardDto));

		// prepare wizard creation request
		final var firstName = RandomString.make();
		final var wizardCreationRequest = new WizardCreationRequestDto();
		wizardCreationRequest.setFirstName(firstName);
		wizardCreationRequest.setGender("Male");
		wizardCreationRequest.setHouseId(house.getId());
		
		// invoke method under test
		final var savedWizardId = wizardService.create(wizardCreationRequest);
		
		// assert response and repository invocation
		assertThat(savedWizardId).isNotNull().isInstanceOf(UUID.class);
		verify(wizardRepository, times(1)).save(any(Wizard.class));
		
		// assert wizard record present in datasource
		final var retrievedWizard = wizardRepository.findById(savedWizardId);
		assertThat(retrievedWizard).isPresent().isNotNull().map(Wizard::getFirstName).hasValue(firstName);
	
		// assert cache containing wizards by house-id is deleted
		final var cachedWizardsByHouseId = listCacheManager.getCache("wizards").get(house.getId());
		assertThat(cachedWizardsByHouseId).isNull();
	}
	
	@Test
	void shouldNotCreateWizardRecordForInvalidHouseId() {
		// prepare invalid house-id
		final var houseId = UUID.randomUUID();
		
		// prepare wizard creation request
		final var wizardCreationRequest = new WizardCreationRequestDto();
		wizardCreationRequest.setFirstName(RandomString.make());
		wizardCreationRequest.setGender("Male");
		wizardCreationRequest.setHouseId(houseId);
		
		// invoke method under test and verify mock interactions
		assertThrows(InvalidHouseIdException.class, () -> wizardService.create(wizardCreationRequest));
		verify(wizardRepository, times(1)).save(any(Wizard.class));
	}
	
	@Test
	void shouldUpdateWizardRecordInDatabaseAndCache() {
		// fetch random house record from datasource
		final var house = masterHouseRepository.fetchRandom().orElseThrow(AssertionFailedError::new);
		
		// create wizard record
		final var originalLastName = RandomString.make();
		final var wizardCreationRequest = new WizardCreationRequestDto();
		wizardCreationRequest.setFirstName(RandomString.make());
		wizardCreationRequest.setLastName(originalLastName);
		wizardCreationRequest.setGender("Male");
		wizardCreationRequest.setHouseId(house.getId());
		final var savedWizardId = wizardService.create(wizardCreationRequest);
		
		// verify repository invocation
		verify(wizardRepository, times(1)).save(any(Wizard.class));
		Mockito.clearInvocations(wizardRepository);
		
		// retrieve wizard record to store it in cache
		wizardService.retrieveById(savedWizardId);
		
		// assert presence of wizard record in cache
		var cachedWizardRecord = objectCacheManager.getCache("wizard").get(savedWizardId);
		var wizardDto = new ModelMapper().map(cachedWizardRecord.get(), WizardDto.class);
		assertThat(wizardDto).isNotNull();
		assertThat(wizardDto.getLastName()).isEqualTo(originalLastName);
		
		// update wizard record
		final var updatedLastName = RandomString.make();
		final var wizardUpdationRequest = new WizardUpdationRequestDto();
		wizardUpdationRequest.setLastName(updatedLastName);
		wizardService.update(savedWizardId, wizardUpdationRequest);
		
		// verify repository invocation
		verify(wizardRepository, times(1)).save(any(Wizard.class));
		
		// assert that cache contains wizard record with updated values
		cachedWizardRecord = objectCacheManager.getCache("wizard").get(savedWizardId);
		wizardDto = new ModelMapper().map(cachedWizardRecord.get(), WizardDto.class);
		assertThat(wizardDto.getLastName()).isEqualTo(updatedLastName);
	}
	
	@Test
	void shouldRetrieveWizardRecordFromCacheAfterInitialDatabaseRetrieval() {
		// fetch random house record from datasource
		final var house = masterHouseRepository.fetchRandom().orElseThrow(AssertionFailedError::new);
		
		// create wizard record
		final var originalLastName = RandomString.make();
		final var wizardCreationRequest = new WizardCreationRequestDto();
		wizardCreationRequest.setFirstName(RandomString.make());
		wizardCreationRequest.setLastName(originalLastName);
		wizardCreationRequest.setGender("Male");
		wizardCreationRequest.setHouseId(house.getId());
		final var savedWizardId = wizardService.create(wizardCreationRequest);
		
		// assert cache does not contain created wizard record
		var cachedWizardRecord = objectCacheManager.getCache("wizard").get(savedWizardId);
		assertThat(cachedWizardRecord).isNull();
		
		// assert that initial retrieval is done from datasource
		wizardService.retrieveById(savedWizardId);
		verify(wizardRepository, times(1)).findById(savedWizardId);
		Mockito.clearInvocations(wizardRepository);
		
		// verify subsequent reads are made from cache and datasource is not queried
		final var queryTimes = 100;
		for (int i = 1; i < queryTimes; i++) {
			wizardService.retrieveById(savedWizardId);
		}
		verify(wizardRepository, times(0)).findById(savedWizardId);
	}
	
	@Test
	void shouldThrowExceptionForInvalidWizardId() {
		// prepare invalid wizard-id that does not exist in datasource
		final var invalidWizardId = UUID.randomUUID();
		
		// invoke method under test(s) and verify datasource interactions
		assertThrows(InvalidWizardIdException.class, () -> wizardService.retrieveById(invalidWizardId));
		assertThrows(InvalidWizardIdException.class, () -> wizardService.update(invalidWizardId, new WizardUpdationRequestDto()));
		assertThrows(InvalidWizardIdException.class, () -> wizardService.delete(invalidWizardId));
		verify(wizardRepository, times(3)).findById(invalidWizardId);
		verify(wizardRepository, times(0)).save(any(Wizard.class));
		verify(wizardRepository, times(0)).deleteById(invalidWizardId);
	}
	
	@Test
	void shouldDeleteWizardRecordFromDatasourceAndCache() {
		// fetch random house record from datasource
		final var house = masterHouseRepository.fetchRandom().orElseThrow(AssertionFailedError::new);
		
		// create wizard record
		final var firstName = RandomString.make();
		final var wizardCreationRequest = new WizardCreationRequestDto();
		wizardCreationRequest.setFirstName(firstName);
		wizardCreationRequest.setGender("Male");
		wizardCreationRequest.setHouseId(house.getId());
		final var savedWizardId = wizardService.create(wizardCreationRequest);
		
		// verify repository invocation
		verify(wizardRepository, times(1)).save(any(Wizard.class));
		Mockito.clearInvocations(wizardRepository);
		
		// invoke GET methods to store data in cache
		wizardService.retrieveById(savedWizardId);
		masterHouseService.retrieveWizardsByHouseId(house.getId());
		
		// assert presence of wizard record in cache
		var cachedWizardRecord = objectCacheManager.getCache("wizard").get(savedWizardId);
		var cachedWizardRecordsByHouseId = listCacheManager.getCache("wizards").get(house.getId());
		assertThat(cachedWizardRecord).isNotNull();
		assertThat(cachedWizardRecordsByHouseId).isNotNull();
		
		// delete wizard record
		wizardService.delete(savedWizardId);
		verify(wizardRepository).deleteById(savedWizardId);
		assertThrows(InvalidWizardIdException.class, () -> wizardService.retrieveById(savedWizardId));
		
		// assert that cache has been cleared corresponding to wizard-id and house-id
		cachedWizardRecord = objectCacheManager.getCache("wizard").get(savedWizardId);
		cachedWizardRecordsByHouseId = listCacheManager.getCache("wizards").get(house.getId());
		assertThat(cachedWizardRecord).isNull();
		assertThat(cachedWizardRecordsByHouseId).isNull();
	}
	
}
