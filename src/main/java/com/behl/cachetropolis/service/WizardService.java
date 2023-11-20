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

@Service
@RequiredArgsConstructor
public class WizardService {

	private final ModelMapper modelMapper;
	private final CacheManager listCacheManager;
	private final WizardRepository wizardRepository;

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
	     
	@CachePut(value = "wizard", key = "#wizardId", cacheManager = "objectCacheManager")
	public WizardDto update(@NonNull final UUID wizardId, @NonNull final WizardUpdationRequestDto WizardUpdationRequest) {		
		final var wizard = getWizardById(wizardId);
		modelMapper.map(WizardUpdationRequest, wizard);
		
		final var updatedWizard = wizardRepository.save(wizard);
		listCacheManager.getCache("wizards").evict(wizard.getHouseId());;
		return modelMapper.map(updatedWizard, WizardDto.class);
	}

	@Cacheable(value = "wizard", key = "#wizardId", cacheManager = "objectCacheManager")
	public WizardDto retrieveById(@NonNull final UUID wizardId) {
		final var retrievedWizard = getWizardById(wizardId);
		return modelMapper.map(retrievedWizard, WizardDto.class);
	}

	@CacheEvict(value = "wizard", key = "#wizardId", cacheManager = "objectCacheManager")
	public void delete(@NonNull final UUID wizardId) {
		final var wizard = getWizardById(wizardId);
		wizardRepository.deleteById(wizardId);
		listCacheManager.getCache("wizards").evict(wizard.getHouseId());;
	}
	
	private Wizard getWizardById(@NonNull final UUID wizardId) {
		return wizardRepository.findById(wizardId).orElseThrow(InvalidWizardIdException::new);
	}

}
