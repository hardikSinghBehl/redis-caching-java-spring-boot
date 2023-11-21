package com.behl.cachetropolis.service;

import java.util.UUID;

import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.behl.cachetropolis.dto.WizardCreationRequestDto;
import com.behl.cachetropolis.dto.WizardDto;
import com.behl.cachetropolis.dto.WizardUpdationRequestDto;
import com.behl.cachetropolis.entity.Wizard;
import com.behl.cachetropolis.exception.InvalidHouseIdException;
import com.behl.cachetropolis.exception.InvalidWizardIdException;
import com.behl.cachetropolis.repository.WizardRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Service class responsible for performing CRUD operations on {@link Wizard}
 * entity. Caching mechanisms are employed to improve latency and reduce network
 * calls. It maintains strong read consistency and ensures the provisioned cache
 * is synced with the datasource.  
 * 
 * @see com.behl.cachetropolis.configuration.RedisConfiguration
 */
@Service
@RequiredArgsConstructor
public class WizardService {

	private final ModelMapper modelMapper;
	private final CacheManager listCacheManager;
	private final WizardRepository wizardRepository;

	/**
	 * Creates a new wizard record corresponding to provided details in {@link WizardCreationRequestDto}
	 * On successful save operation, the list of wizard records corresponding to the house-id 
	 * of the newly created wizard record is cleared from the provisioned cache to maintain 
	 * strong read consistency.
	 * 
	 * @param wizardCreationRequest The request for creating a new wizard (must not be {@code null})
	 * @return {@code Non null} primary identifier ({@link UUID}) of the newly created wizard record.
	 * @throws IllegalArgumentException if the provided wizardCreationRequest is {@code null}
	 * @throws InvalidHouseIdException if no house record exists in the datasource
	 *                                  corresponding to given house-id in the request.
	 */
	@CacheEvict(value = "wizards", key = "#wizardCreationRequest.houseId")
	public UUID create(@NonNull final WizardCreationRequestDto wizardCreationRequest) {
		final var wizard = modelMapper.map(wizardCreationRequest, Wizard.class);
		final Wizard savedWizard;
		
		try {
			savedWizard = wizardRepository.save(wizard);			
		} catch (final DataIntegrityViolationException exception) {
			if (exception.getCause() instanceof ConstraintViolationException) {
				throw new InvalidHouseIdException();				
			}
			throw exception;
		}
		return savedWizard.getId();
	}
	    
	/**
	 * Updates wizard record by its ID. Post record updation in the datasource, the
	 * wizard record is also updated in the provisioned cache if present.
	 * Additionally, the list of wizard records corresponding to the house-id of the
	 * updated wizard is evicted from the cache to maintain strong read consistency.
	 * 
	 * This method is designed as a partial update (PATCH) where only the specified
	 * field(s) in the {@link WizardUpdationRequestDto} are updated. {@code null}
	 * field(s) are ignored. 
	 *
	 * @param wizardId The Id of the wizard to be updated. (must not be {@code null})
	 * @param WizardUpdationRequest containing the field(s) to be updated. (must not be {@code null})
	 * @return {@link WizardDto} representing the updated wizard record.
	 * @throws IllegalArgumentException if any of the parameter provided is {@code null}
	 * @throws InvalidWizardIdException if no wizard record exists in the datasource
	 *                                  corresponding to the given wizardId.
	 */
	@CachePut(value = "wizard", key = "#wizardId", cacheManager = "objectCacheManager")
	public WizardDto update(@NonNull final UUID wizardId, @NonNull final WizardUpdationRequestDto WizardUpdationRequest) {		
		final var wizard = getWizardById(wizardId);
		modelMapper.map(WizardUpdationRequest, wizard);
		
		final var updatedWizard = wizardRepository.save(wizard);
		listCacheManager.getCache("wizards").evict(wizard.getHouseId());;
		return modelMapper.map(updatedWizard, WizardDto.class);
	}

	/**
	 * Retrieves a {@code non null} {@link WizardDto} by its ID. Post initial datasource
	 * retrieval, the response is stored in the provisioned cache. For all subsequent 
	 * calls with the same {@code wizardId}, the record is fetched from the cache itself, 
	 * reducing network calls and improving latency.
	 *
	 * @param wizardId The id of the wizard record to be retrieved. (must not be {@code null})
	 * @return {@link WizardDto} representing the details of the retrieved wizard.
	 * @throws IllegalArgumentException if the provided wizardId is {@code null}
	 * @throws InvalidWizardIdException if no wizard record exists in the datasource
	 *                                  corresponding to given wizardId.
	 */
	@Cacheable(value = "wizard", key = "#wizardId", cacheManager = "objectCacheManager")
	public WizardDto retrieveById(@NonNull final UUID wizardId) {
		final var retrievedWizard = getWizardById(wizardId);
		return modelMapper.map(retrievedWizard, WizardDto.class);
	}

	/**
	 * Deletes a wizard record by its ID. In addition to record deletion from
	 * datasource, the wizard record is also cleared from the provisioned cache.
	 * List of wizard records corresponding to the house-id of the deleted wizard
	 * are also cleared from the cache to maintain strong read consistency.
	 *
	 * @param wizardId The Id of the wizard to be deleted. (must not be {@code null})
	 * @throws IllegalArgumentException if the provided wizardId is {@code null}
	 * @throws InvalidWizardIdException if no wizard record exists in the datasource
	 *                                  corresponding to given wizardId.
	 */
	@CacheEvict(value = "wizard", key = "#wizardId", cacheManager = "objectCacheManager")
	public void delete(@NonNull final UUID wizardId) {
		final var wizard = getWizardById(wizardId);
		wizardRepository.deleteById(wizardId);
		listCacheManager.getCache("wizards").evict(wizard.getHouseId());;
	}
	
	/**
	 * Retrieves a {@link Wizard} record by its ID. The returned entity will never
	 * be null.
	 *
	 * @param wizardId The id of the wizard record to be retrieved. (must not be {@code null})
	 * @return {@link Wizard} entity representing the retrieved wizard record from the datasource.
	 * @throws IllegalArgumentException if the provided wizardId is {@code null}
	 * @throws InvalidWizardIdException if no wizard record exists in the datasource
	 *                                  corresponding to given wizardId.
	 */
	private Wizard getWizardById(@NonNull final UUID wizardId) {
		return wizardRepository.findById(wizardId).orElseThrow(InvalidWizardIdException::new);
	}

}
