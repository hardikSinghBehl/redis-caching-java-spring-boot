package com.hardik.bojack.service;

import java.util.UUID;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.hardik.bojack.dto.WizardCreationRequestDto;
import com.hardik.bojack.dto.WizardDto;
import com.hardik.bojack.entity.Wizard;
import com.hardik.bojack.repository.WizardRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WizardService {

	private final WizardRepository wizardRepository;

	@Caching(evict = { @CacheEvict(value = "wizards", allEntries = true) })
	public UUID create(final WizardCreationRequestDto wizardCreationRequestDto) throws JSONException {

		final var wizard = new Wizard();
		wizard.setFirstName(wizardCreationRequestDto.getFirstName());
		wizard.setMiddleName(wizardCreationRequestDto.getMiddleName());
		wizard.setLastName(wizardCreationRequestDto.getLastName());
		wizard.setGender(wizardCreationRequestDto.getGender());
		wizard.setHouseId(wizardCreationRequestDto.getHouseId());

		final var savedWizard = wizardRepository.save(wizard);

		if (savedWizard == null)
			throw new RuntimeException("Invalid Wizard id");

		return savedWizard.getId();
	}

	@Caching(evict = { @CacheEvict(value = "wizards", allEntries = true) }, put = {
			@CachePut(value = "wizard", key = "#wizardId") })
	public UUID update(final UUID wizardId, final WizardCreationRequestDto wizardCreationRequestDto)
			throws JSONException {

		final var wizard = wizardRepository.findById(wizardId).get();
		wizard.setFirstName(wizardCreationRequestDto.getFirstName());
		wizard.setMiddleName(wizardCreationRequestDto.getMiddleName());
		wizard.setLastName(wizardCreationRequestDto.getLastName());
		wizard.setGender(wizardCreationRequestDto.getGender());
		wizard.setHouseId(wizardCreationRequestDto.getHouseId());

		final var updatedWizard = wizardRepository.save(wizard);

		if (updatedWizard == null)
			throw new RuntimeException("Invalid Wizard id");

		return updatedWizard.getId();
	}

	@Cacheable(value = "wizard", key = "#wizardId")
	public WizardDto retreiveById(final UUID wizardId) {

		final var wizard = wizardRepository.findById(wizardId);

		if (wizard.isEmpty())
			throw new RuntimeException("Invalid Wizard id");

		final var retreivedWizard = wizard.get();
		return WizardDto.builder().createdAt(retreivedWizard.getCreatedAt()).firstName(retreivedWizard.getFirstName())
				.gender(retreivedWizard.getGender()).houseId(retreivedWizard.getHouseId()).id(retreivedWizard.getId())
				.updatedAt(retreivedWizard.getUpdatedAt()).middleName(retreivedWizard.getMiddleName())
				.lastName(retreivedWizard.getLastName()).build();
	}

	@Caching(evict = { @CacheEvict(value = "wizard", key = "#wizardId"),
			@CacheEvict(value = "wizards", allEntries = true) })
	public void delete(final UUID wizardId) {
		wizardRepository.deleteById(wizardId);
	}

}
